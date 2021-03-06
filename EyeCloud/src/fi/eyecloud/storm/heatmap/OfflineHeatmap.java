package fi.eyecloud.storm.heatmap;

import java.util.ArrayList;
import java.util.List;
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
import fi.eyecloud.gui.lib.GuiConstants;
import fi.eyecloud.input.ReadTextFile;
import fi.eyecloud.utils.UploadData;

@SuppressWarnings("deprecation")
public class OfflineHeatmap {
	/**
	 * Read data from text files
	 * 
	 * @author chung
	 *
	 */
    @SuppressWarnings("serial")
	public static class ReadData extends BaseBasicBolt {

    	@Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
        	Object id = tuple.getValue(0);
        	String tmp = tuple.getString(1);
        	String data[] = tmp.split(Constants.PARAMETER_SPLIT);
        	String outputName = data[data.length - 1];
        	int numberPart = Integer.parseInt(data[data.length - 2]);
        	int heightMedia = Integer.parseInt(data[data.length - 3]);
        	int widthMedia = Integer.parseInt(data[data.length - 4]);
        	
        	float value[] = new float[data.length - 3];
        	for (int i=0; i < data.length - 3; i++){
        		value[i] = Float.parseFloat(data[i]);
        	}
        	
        	numberPart = value.length/(Constants.PARAMETER_NUMBER_HEATMAP*numberPart);
        	
        	int count = 0;
        	List<Integer> send = new ArrayList<Integer>();
        	for (int i=0; i < value.length/Constants.PARAMETER_NUMBER_HEATMAP; i++){
        		count++;
        		send.add(Math.round(value[i*Constants.PARAMETER_NUMBER_HEATMAP]));
        		send.add(Math.round(value[i*Constants.PARAMETER_NUMBER_HEATMAP + 1]));
        		send.add(Math.round(value[i*Constants.PARAMETER_NUMBER_HEATMAP + 2]));
        		if (count == numberPart){
        			send.add(widthMedia);
        			send.add(heightMedia);
        			// Avoid reseting send to empty values
        			List<Integer> tmpSend = new ArrayList<Integer>();
        			for (int j=0; j < send.size(); j++)
        				tmpSend.add(send.get(j));
        			collector.emit(new Values(id, tmpSend, outputName));
        			send.clear();
        			count = 0;
        		}
        	}
        	
        	if (send.size() > 0){
        		send.add(widthMedia);
        		send.add(heightMedia);
        		collector.emit(new Values(id, send, outputName));
        	}
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "data", "output"));           
        }
    }
    
    /**
     * Calculate intensity for each pixel
     * 
     * @author chung
     *
     */
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
		
    	@SuppressWarnings("unchecked")
		@Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
        	Object id = tuple.getValue(0);
        	List<Integer> data = (List<Integer>) tuple.getValue(1);
        	String outputName = tuple.getString(2);
        	int width = data.get(data.size() - 2);
        	int height = data.get(data.size() - 1);
        	
        	double[][] intensity = new double[width][height];
        	
    		for (int i=0; i < width; i++){
    			for (int j=0; j < height; j++){
    				intensity[i][j] = 0;
    			}
    		}
    		
    		for (int k=0; k < data.size()/Constants.PARAMETER_NUMBER_HEATMAP; k++){
    			int x = data.get(k*Constants.PARAMETER_NUMBER_HEATMAP)/GuiConstants.SCREEN_RATE;;
    			int y = data.get(k*Constants.PARAMETER_NUMBER_HEATMAP + 1)/GuiConstants.SCREEN_RATE;;
    			int d = data.get(k*Constants.PARAMETER_NUMBER_HEATMAP + 2);
    			
    			for (int i=x - GuiConstants.KERNEL_SIZE_USED; i <= x + GuiConstants.KERNEL_SIZE_USED; i++){
    				for (int j=y - GuiConstants.KERNEL_SIZE_USED; j <= y + GuiConstants.KERNEL_SIZE_USED; j++){
    					if (i >=0 && j >=0 && i < width && j < height)
    						intensity[i][j] += gaussianWindow[Math.abs(x-i)][Math.abs(y-j)]*d;
    				}
    			}
    		}
    		
        	collector.emit(new Values(id, intensity, width, height, outputName));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "intensity", "width", "height", "output"));           
        }
    }    
     
	/**
	 * Aggregate results
	 * 
	 * @author chung
	 *
	 */
	@SuppressWarnings({ "serial", "rawtypes" })
	public static class AggregatorData extends BaseBatchBolt {
		BatchOutputCollector _collector;
        Object _id;
        double intensity[][] = null;
        int width = 0;
        int height = 0;
        TopologyContext contextData;
        String outputName = null;
        
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
    		if (outputName == null) outputName = tuple.getString(4);
    		
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
        	UploadData.upload(Constants.UPLOAD_HOST, color.getImage(), outputName);
			_collector.emit(new Values(_id, "Ok"));
        }
    	
        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "result"));           
        }
    }	
	
    public static LinearDRPCTopologyBuilder construct(int numberRead, int numberProcess, int numberAggregator) {
    	LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("offlineheatmap");
    	builder.addBolt(new ReadData(), numberRead);
        builder.addBolt(new ProcessData(), numberProcess).shuffleGrouping();
        builder.addBolt(new AggregatorData(), numberAggregator).fieldsGrouping(new Fields("id"));
        return builder;
    }
    
    public static void main(String[] args) throws Exception {
        LinearDRPCTopologyBuilder builder = construct(1, Integer.parseInt(args[1]), 1);
    	//LinearDRPCTopologyBuilder builder = construct(1, 3, 1);
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
            			+ "3" + Constants.PARAMETER_SPLIT + "lala";
            
            // Send data
            long start = System.currentTimeMillis();
            String result = drpc.execute("offlineheatmap", send);
            System.out.println(result + " Running time: " + (float)(System.currentTimeMillis() - start)/1000);
            
            cluster.shutdown();
            drpc.shutdown();
        } else {
            conf.setNumWorkers(Integer.parseInt(args[1]));
            StormSubmitter.submitTopology(args[0], conf, builder.createRemoteTopology());
        }
    }
}
