package fi.eyecloud.storm;

import java.util.Map;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.conf.Library;
import fi.eyecloud.input.ReadTextFile;

@SuppressWarnings("deprecation")
/**
 * IVT merging on the cloud
 * 
 * @author chung
 *
 */
public class I_VT_Merge {
    @SuppressWarnings("serial")
	public static class ProcessData extends BaseBasicBolt {
    	private int sumX;
    	private int sumY;
    	private int count;
    	private int startTime;
    	private int duration = 0;

    	private int currentLine = 0;
    	private String result = "";
    	
        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
        	result = "";
        	count = 0;
        	currentLine = 0;
        	duration = 0;
        	
        	Object id = tuple.getValue(0);
        	String data[] = tuple.getString(1).split(Constants.PARAMETER_SPLIT);
        	// Length = 0 means finishing
        	if (data.length == 1){
        		collector.emit(new Values(id, "stop"));
        		return;
        	}
        	int length = data.length/Constants.PARAMETER_NUMBER_FIXATION;
        	int x1,y1,time1,x2,y2,time2;
        	float dis1, dis2;
        	x1 = y1 = time1 = 0;
        	dis1 = 0;
        	
        	for (int i=0; i < length; i++){
        		x2 = Integer.parseInt(data[i*Constants.PARAMETER_NUMBER_FIXATION]);
        		y2 = Integer.parseInt(data[i*Constants.PARAMETER_NUMBER_FIXATION + 1]);
        		time2 = Integer.parseInt(data[i*Constants.PARAMETER_NUMBER_FIXATION + 2]);
        		dis2 = Float.parseFloat(data[i*Constants.PARAMETER_NUMBER_FIXATION + 3]);
        		
        		if (i == 0){
        			x1 = x2;
        			y1 = y2;
        			time1 = time2;
        			dis1 = dis2;
        		}
        		
    			if (x1 >= 0 && y1 >= 0 && time1 >= 0 && dis1 >= 0 && x2 >= 0 && y2 >= 0 && time2 >= 0 && dis2 >= 0){
    				float tmp = Library.VT_Degree(x1, y1, x2, y2, dis1, dis2,
    						time2 - time1);
    				// time1 = time2 --> first coordinate
    				if ( tmp <= Constants.VELOCITY_THRESHOLD || time1 == time2) {
    					putXY(x2, y2, time2, i + 1);
    				}
    			}
    			
    			x1 = x2;
    			y1 = y2;
    			time1 = time2;
    			dis1 = dis2;
        	}
        	
			if (count > 0 && duration > Constants.FIXATION_DURATION_THRESHOLD) {
				result = result + (float) sumX / count
						+ Constants.PARAMETER_SPLIT + (float) sumY
						/ count + Constants.PARAMETER_SPLIT + startTime
						+ Constants.PARAMETER_SPLIT + duration + Constants.PARAMETER_SPLIT;
			}
        	
        	//result = result + numberFixation;
        	collector.emit(new Values(id, result));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "result"));           
        }
        
    	/**
    	 * Process each x,y coordinate
    	 * 
    	 * @param x
    	 * @param y
    	 * @param time
    	 * @param dis
    	 */
    	public void putXY(int x, int y, int time, int lineId) {
    		if (currentLine == 0 || lineId - currentLine == 1) {
    			if (count == 0){
    				count++;
    				sumX = x;
    				sumY = y;
    				startTime = time;
    				currentLine = lineId;
    				duration = Constants.THOUSAND / Constants.SAMPLE_RATE;
    			}else{
    				count++;
    				sumX += x;
    				sumY += y;

    				currentLine = lineId;
    				duration = time - startTime + Constants.THOUSAND / Constants.SAMPLE_RATE;
    			}
    		}else {
    			if (count > 0 && duration > Constants.FIXATION_DURATION_THRESHOLD) {
    				result = result + (float) sumX / count
    						+ Constants.PARAMETER_SPLIT + (float) sumY
    						/ count + Constants.PARAMETER_SPLIT + startTime
    						+ Constants.PARAMETER_SPLIT + duration + Constants.PARAMETER_SPLIT;
    			}
    			count = 0;
    			count++;
    			sumX = x;
    			sumY = y;
    			startTime = time;
    			duration = Constants.THOUSAND / Constants.SAMPLE_RATE;
    			currentLine = lineId;
    		}		
    	}        
    }   
        
    @SuppressWarnings("serial")
	public static class AggregateData extends BaseBasicBolt {
    	private TopologyContext contextData;
		private float currentX = -1;
		private float currentY = -1;
		private int startTime = -1;
		private int duration = -1;
		private static String CURRENT_X = "currentX";
		private static String CURRENT_Y = "currentY";
		private static String START_TIME = "startTime";
		private static String DURATION = "duration";
		
		@SuppressWarnings("rawtypes")
		@Override
		public void prepare(Map conf, TopologyContext context) {
			contextData = context;
			if (context.getTaskData(CURRENT_X) != null)
				currentX = Float.parseFloat(context.getTaskData(CURRENT_X).toString());
			if (context.getTaskData(CURRENT_Y) != null)
				currentY = Float.parseFloat(context.getTaskData(CURRENT_Y).toString());
			if (context.getTaskData(START_TIME) != null)
				startTime = Integer.parseInt(context.getTaskData(START_TIME).toString());
			if (context.getTaskData(DURATION) != null)
				duration = Integer.parseInt(context.getTaskData(DURATION).toString());
		}    	
    	
		@Override
		public void execute(Tuple tuple, BasicOutputCollector collector) {
			// TODO Auto-generated method stub
			Object id = tuple.getValue(0);
			String data[] = tuple.getString(1).split(Constants.PARAMETER_SPLIT);
			
			if (data.length == 1){
				if (!data[0].equals("stop")){
					collector.emit(new Values(id, ""));
					return;
				}
				String result;
				result = Float.toString(currentX) + Constants.PARAMETER_SPLIT +
						Float.toString(currentY) + Constants.PARAMETER_SPLIT  +	
						Integer.toString(startTime) + Constants.PARAMETER_SPLIT +
						Integer.toString(duration) + Constants.PARAMETER_SPLIT;
				collector.emit(new Values(id, result));
				contextData.setTaskData(DURATION, 0);
				contextData.setTaskData(START_TIME, 0);
				contextData.setTaskData(CURRENT_Y, 0);
				contextData.setTaskData(CURRENT_X, 0);
				return;
			}
			
			int time = Integer.parseInt(data[2]);
			if (startTime + duration == time){
				data[0] = Float.toString((currentX + Float.parseFloat(data[0]))/2);
				data[1] = Float.toString((currentY + Float.parseFloat(data[1]))/2);
				data[2] = Integer.toString(startTime);
				data[3] = Integer.toString(duration + Integer.parseInt(data[3]));
			}
			
			contextData.setTaskData(DURATION, data[data.length - 1]);
			contextData.setTaskData(START_TIME, data[data.length - 2]);
			contextData.setTaskData(CURRENT_Y, data[data.length - 3]);
			contextData.setTaskData(CURRENT_X, data[data.length - 4]);
			
			String result = "";
			for (int i=0; i < data.length - 4; i++){
				result = result + data[i] + Constants.PARAMETER_SPLIT;
			}
			
			collector.emit(new Values(id, result));
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			// TODO Auto-generated method stub
			declarer.declare(new Fields("id", "result")); 
		}
    	
    }
    
    public static LinearDRPCTopologyBuilder construct(int numberBolt) {
    	LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("I_VT_Merge");
        builder.addBolt(new ProcessData(), numberBolt);
        builder.addBolt(new AggregateData(), 1).shuffleGrouping();
        return builder;
    }
    
    public static void main(String[] args) throws Exception {
        LinearDRPCTopologyBuilder builder = construct(Integer.parseInt(args[1]));
        //LinearDRPCTopologyBuilder builder = construct(3);
        Config conf = new Config();
        
        if(args==null || args.length==0) {
            conf.setMaxTaskParallelism(3);
            LocalDRPC drpc = new LocalDRPC();
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("reach-drpc", conf, builder.createLocalTopology(drpc));
            
            ReadTextFile data = new ReadTextFile("data/17June.txt");
            String send = "";
            int count = 0;
            while (data.readNextLine() != null){
            	send = send + data.getField(Constants.GazePointX) + Constants.PARAMETER_SPLIT;
            	send = send + data.getField(Constants.GazePointY) + Constants.PARAMETER_SPLIT;
            	send = send + data.getField(Constants.Timestamp) + Constants.PARAMETER_SPLIT;
            
    			float dis = (Float.parseFloat(data.getField(Constants.DistanceLeft)) + 
    					Float.parseFloat(data.getField(Constants.DistanceRight))) / 2;
    			
    			send = send + dis + Constants.PARAMETER_SPLIT;
    			count++;
    			if (count == 100) break;
            }
            
            System.out.println(send);
            long start = System.currentTimeMillis();
            System.out.println("Output file: " + drpc.execute("I_VT_Merge", send));
            System.out.println("Running time: " + (float)(System.currentTimeMillis() - start)/1000);
            System.out.println("Output file: " + drpc.execute("I_VT_Merge", ""));
            cluster.shutdown();
            drpc.shutdown();
        } else {
            conf.setNumWorkers(Integer.parseInt(args[1]));
            StormSubmitter.submitTopology(args[0], conf, builder.createRemoteTopology());
        }
    }
}
