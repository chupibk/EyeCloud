package fi.eyecloud.storm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.coordination.BatchOutputCollector;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseBatchBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.gui.heatmap.Colorization;
import fi.eyecloud.gui.heatmap.HeatmapFromText;
import fi.eyecloud.gui.lib.GuiConstants;
import fi.eyecloud.input.ReadTextFile;
import fi.eyecloud.utils.ClientFile;
import fi.eyecloud.utils.ImageUtils;

@SuppressWarnings("deprecation")
public class HeatmapRenderingStaticNoDivide {
	@SuppressWarnings("serial")
	public static class ProcessData extends BaseBasicBolt {
		private double gaussianWindow[][];
		
		public ProcessData(){
			gaussianWindow = new double[GuiConstants.KERNEL_SIZE_USED + 1][GuiConstants.KERNEL_SIZE_USED + 1];
			for (int i=0; i <= GuiConstants.KERNEL_SIZE_USED; i++){
				for (int j=0; j <= GuiConstants.KERNEL_SIZE_USED; j++){
					gaussianWindow[i][j] = gaussian2D(i, j);
				}
			}
		}
		
		public double gaussian2D(int x, int y){
			double value;
			value = Math.exp(-(x*x + y*y)/(2*GuiConstants.SIGMA_USED*GuiConstants.SIGMA_USED));		
			return value;
		}
		
		@Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
        	Object id = tuple.getValue(0);
        	String tmp = tuple.getString(1);
        	String data[] = tmp.split(Constants.PARAMETER_SPLIT);
        	int width = Integer.parseInt(data[data.length - 3]);
        	int height = Integer.parseInt(data[data.length - 2]);        	
        	
        	int value[] = new int[data.length - 3];
        	for (int i=0; i < data.length - 3; i++){
        		value[i] = Math.round(Float.parseFloat(data[i]));
        	}
        	
        	double[][] intensity = new double[width][height];
        	
    		for (int i=0; i < width; i++){
    			for (int j=0; j < height; j++){
    				intensity[i][j] = 0;
    			}
    		}
    		
    		for (int k=0; k < value.length/Constants.PARAMETER_NUMBER_HEATMAP; k++){
    			int x = value[k*Constants.PARAMETER_NUMBER_HEATMAP]/GuiConstants.SCREEN_RATE;;
    			int y = value[k*Constants.PARAMETER_NUMBER_HEATMAP +1]/GuiConstants.SCREEN_RATE;;
    			int d = value[k*Constants.PARAMETER_NUMBER_HEATMAP +2];
    			
    			for (int i=x - GuiConstants.KERNEL_SIZE_USED; i <= x + GuiConstants.KERNEL_SIZE_USED; i++){
    				for (int j=y - GuiConstants.KERNEL_SIZE_USED; j <= y + GuiConstants.KERNEL_SIZE_USED; j++){
    					if (i >=0 && j >=0 && i < width && j < height)
    						intensity[i][j] += gaussianWindow[Math.abs(x-i)][Math.abs(y-j)]*d;
    				}
    			}
    		}
    		
        	collector.emit(new Values(id, intensity, width, height));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "intensity", "width", "height"));           
        }
    }    
     
	@SuppressWarnings({ "serial", "rawtypes" })
	public static class AggregatorData extends BaseBatchBolt {
		BatchOutputCollector _collector;
        Object _id;
        double intensity[][] = null;
        int width = 0;
        int height = 0;
        TopologyContext contextData;
        
        @Override
        public void prepare(Map conf, TopologyContext context, BatchOutputCollector collector, Object id) {
            _collector = collector;
            _id = id;
            contextData = context;
            intensity = (double[][]) context.getTaskData("data");
        }		
		
    	@Override
        public void execute(Tuple tuple) {
    		double[][] data = (double[][]) tuple.getValue(1);
    		if (width == 0) width = tuple.getInteger(2);
    		if (height == 0) height = tuple.getInteger(3);
    		
    		if (intensity == null){
    			intensity = new double[width][height];
        		for (int i=0; i < width; i++){
        			for (int j=0; j < height; j++){
        				intensity[i][j] = 0;
        			}
        		}
    		}
    		
    		for (int i=0; i < width; i++){
    			for (int j=0; j < height; j++){
    				intensity[i][j] += data[i][j];
    			}
    		}
    		
        }

        @Override
        public void finishBatch() {
        	contextData.setTaskData("data", intensity);
        	Colorization color = new Colorization(intensity, width, height);
        	try {
				new ClientFile(color.getImage());
				_collector.emit(new Values(_id, "Ok"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				_collector.emit(new Values(_id, "Fail"));
			}
        	//String result = ImageUtils.encodeToString(color.getImage(), "png");
        }
    	
        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "result"));           
        }
    }	
	
    public static LinearDRPCTopologyBuilder construct(int numberProcess, int numberAggregator) {
    	LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("heatmap_static");
        builder.addBolt(new ProcessData(), numberProcess);
        builder.addBolt(new AggregatorData(), numberAggregator).fieldsGrouping(new Fields("id"));
        return builder;
    }
    
    public static void main(String[] args) throws Exception {
        LinearDRPCTopologyBuilder builder = construct(Integer.parseInt(args[1]), 1);
    	//LinearDRPCTopologyBuilder builder = construct(3, 1);
        Config conf = new Config();
        
        if(args==null || args.length==0) {
            conf.setMaxTaskParallelism(3);
            LocalDRPC drpc = new LocalDRPC();
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("reach-drpc", conf, builder.createLocalTopology(drpc));
            
            ReadTextFile data = new ReadTextFile("data/heatmap.txt");
            String send = "";
            while (data.readNextLine() != null){
            	send = send + data.getField(Constants.GazePointX) + Constants.PARAMETER_SPLIT;
            	send = send + data.getField(Constants.GazePointY) + Constants.PARAMETER_SPLIT;
            	send = send + data.getField(Constants.Duration) + Constants.PARAMETER_SPLIT;
            }
            
            send = send + GuiConstants.MEDIA_WIDTH + Constants.PARAMETER_SPLIT 
            			+ GuiConstants.MEDIA_HEIGHT + Constants.PARAMETER_SPLIT
            			+ "3";
            
            // Send data
            long start = System.currentTimeMillis();
            String imageString = drpc.execute("heatmap_static", send);
            System.out.println("Running time: " + (float)(System.currentTimeMillis() - start)/1000);
            
            
            BufferedWriter out = null;
			FileWriter fw = new FileWriter("data/base64");
			out = new BufferedWriter(fw);
			out.write(imageString);
			out.close();
            
            // Draw
            new ClientFile(ImageUtils.decodeToImage(imageString));
            new HeatmapFromText("data/17JuneMedia.png", ImageUtils.decodeToImage(imageString));
            
            cluster.shutdown();
            drpc.shutdown();
        } else {
            conf.setNumWorkers(Integer.parseInt(args[1]));
            StormSubmitter.submitTopology(args[0], conf, builder.createRemoteTopology());
        }
    }
}
