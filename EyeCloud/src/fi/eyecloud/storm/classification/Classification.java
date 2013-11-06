package fi.eyecloud.storm.classification;

import java.util.Map;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.conf.Library;
import fi.eyecloud.input.ReadTextFile;
import fi.eyecloud.lib.SObject;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.drpc.DRPCSpout;
import backtype.storm.drpc.ReturnResults;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class Classification {

	@SuppressWarnings("serial")
	public static class ReceiveData extends BaseBasicBolt{

		@Override
		public void execute(Tuple input, BasicOutputCollector collector) {
			// TODO Auto-generated method stub
			String data[] = input.getString(0).split(Constants.PARAMETER_SPLIT);
			Integer id = Integer.parseInt(data[data.length - 1]);
			collector.emit(new Values(id, data, input.getValue(1)));
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			// TODO Auto-generated method stub
			declarer.declare(new Fields("id", "data", "requestid"));
		}
		
	}
	
	@SuppressWarnings("serial")
	public static class ProcessData extends BaseBasicBolt{
		private TopologyContext contextData;
		private static String X_1 = "x1_";
		private static String Y_1 = "y1_";
		private static String TIME_1 = "time1_";
		private static String DIS_1 = "dis1_";
		private static String INDEX_1 = "index1_";
		private static String COUNT = "count_";				
		private static String SUM_X = "sumX_";
		private static String SUM_Y = "sumY_";
		private static String START_TIME = "starttime_";
		private static String DURATION = "duration_";		
		private static String SENDTIME = "sendtime_";
		private static String VELOCITY = "velocity_";
		private static String SOBJECT	= "sobject_tmp";
		
    	private int sumX;
    	private int sumY;
    	private int count = 0;
    	private int startTime;
    	private int duration = 0;
    	private long sendtime = 0;
    	private int currentLine = 0;
    	private float preVelocity = 0;
    	private SObject sObjectTmp;
    	
    	private String result = "";		
		
		@SuppressWarnings("rawtypes")
		@Override
		public void prepare(Map conf, TopologyContext context) {
			contextData = context;
		}		
		
		@Override
		public void execute(Tuple input, BasicOutputCollector collector) {
			// Set values
			sumX = sumY = count = startTime = duration = currentLine = 0;
			result = "";
			preVelocity = 0;
			sObjectTmp = new SObject();
			
			Integer id = input.getInteger(0);
			String data[] = (String[]) input.getValue(1);
        	int length = data.length/Constants.PARAMETER_NUMBER_FIXATION;
        	int x1, y1, time1, x2, y2, time2, index2;
        	long send2;
        	float dis1, dis2;
        	x1 = y1 = time1 = 0;
        	dis1 = 0;			

			// Get data
			if (contextData.getTaskData(X_1 + id) != null)
				x1 = Integer.parseInt(contextData.getTaskData(X_1 + id).toString());
			if (contextData.getTaskData(Y_1 + id) != null)
				y1 = Integer.parseInt(contextData.getTaskData(Y_1 + id).toString());
			if (contextData.getTaskData(TIME_1 + id) != null)
				time1 = Integer.parseInt(contextData.getTaskData(TIME_1 + id).toString());	
			if (contextData.getTaskData(DIS_1 + id) != null)
				dis1 = Float.parseFloat(contextData.getTaskData(DIS_1 + id).toString());	
			if (contextData.getTaskData(INDEX_1 + id) != null)
				currentLine = Integer.parseInt(contextData.getTaskData(INDEX_1 + id).toString());			
			if (contextData.getTaskData(COUNT + id) != null)
				count = Integer.parseInt(contextData.getTaskData(COUNT + id).toString());		        	
			if (contextData.getTaskData(SUM_X + id) != null)
				sumX = Integer.parseInt(contextData.getTaskData(SUM_X + id).toString());	
			if (contextData.getTaskData(SUM_Y + id) != null)
				sumY = Integer.parseInt(contextData.getTaskData(SUM_Y + id).toString());	
			if (contextData.getTaskData(START_TIME + id) != null)
				startTime = Integer.parseInt(contextData.getTaskData(START_TIME + id).toString());		
			if (contextData.getTaskData(DURATION + id) != null)
				duration = Integer.parseInt(contextData.getTaskData(DURATION + id).toString());				
			if (contextData.getTaskData(SENDTIME + id) != null)
				sendtime = Long.parseLong(contextData.getTaskData(SENDTIME + id).toString());				
			if (contextData.getTaskData(VELOCITY + id) != null)
				preVelocity = Long.parseLong(contextData.getTaskData(VELOCITY + id).toString());	
			if (contextData.getTaskData(SOBJECT + id) != null)
				sObjectTmp = (SObject) contextData.getTaskData(SOBJECT + id);				
			
			if (data.length == 1){
				if (count > 0 && duration > Constants.FIXATION_DURATION_THRESHOLD) {
					result = result + (float) sumX / count
							+ Constants.PARAMETER_SPLIT + (float) sumY
							/ count + Constants.PARAMETER_SPLIT + startTime
							+ Constants.PARAMETER_SPLIT + duration 
							+ Constants.PARAMETER_SPLIT + sendtime + Constants.PARAMETER_SPLIT;
				}
				
	        	contextData.setTaskData(X_1 + id, null);
	        	contextData.setTaskData(Y_1 + id, null);
	        	contextData.setTaskData(TIME_1 + id, null);
	        	contextData.setTaskData(DIS_1 + id, null);
	        	contextData.setTaskData(INDEX_1 + id, null);
	        	contextData.setTaskData(COUNT + id, null);
	        	contextData.setTaskData(SUM_X + id, null);
	        	contextData.setTaskData(SUM_Y + id, null);
	        	contextData.setTaskData(START_TIME + id, null);
	        	contextData.setTaskData(DURATION + id, null);
	        	contextData.setTaskData(SENDTIME + id, null);
	        	contextData.setTaskData(VELOCITY + id, null);
	        	contextData.setTaskData(SOBJECT + id, null);
				collector.emit(new Values(result, input.getValue(2)));
				return;
			}
			
        	for (int i=0; i < length; i++){
        		x2 = Integer.parseInt(data[i*Constants.PARAMETER_NUMBER_FIXATION]);
        		y2 = Integer.parseInt(data[i*Constants.PARAMETER_NUMBER_FIXATION + 1]);
        		time2 = Integer.parseInt(data[i*Constants.PARAMETER_NUMBER_FIXATION + 2]);
        		dis2 = Float.parseFloat(data[i*Constants.PARAMETER_NUMBER_FIXATION + 3]);
        		send2 = Long.parseLong(data[i*Constants.PARAMETER_NUMBER_FIXATION + 4]);
        		index2 = Integer.parseInt(data[i*Constants.PARAMETER_NUMBER_FIXATION + 5]);
        		
        		if (i == 0 && contextData.getTaskData(X_1 + id) == null){
        			x1 = x2;
        			y1 = y2;
        			time1 = time2;
        			dis1 = dis2;
        			sendtime = send2;
        		}
        		
    			if (x1 >= 0 && y1 >= 0 && time1 >= 0 && dis1 >= 0 && x2 >= 0 && y2 >= 0 && time2 >= 0 && dis2 >= 0){
    				float tmp = Library.VT_Degree(x1, y1, x2, y2, dis1, dis2,
    						time2 - time1);
    				// time1 = time2 --> first coordinate
    				if ( tmp <= Constants.VELOCITY_THRESHOLD || time1 == time2) {
    					putXY(x2, y2, time2, send2, index2);
    				}else{
    					storeFix(x2, y2, time2, send2, index2);
						float d = (float)Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
						float a = (tmp - preVelocity) / (time2 - time1);
						sObjectTmp.addRawData(x1, y1, time1, time2 - time1, d, tmp, a);    					
    				}
    				
    				preVelocity = tmp;
    			}
    			
    			x1 = x2;
    			y1 = y2;
    			time1 = time2;
    			dis1 = dis2;
        	}
        	
        	// Set data
        	contextData.setTaskData(X_1 + id, x1);
        	contextData.setTaskData(Y_1 + id, y1);
        	contextData.setTaskData(TIME_1 + id, time1);
        	contextData.setTaskData(DIS_1 + id, dis1);
        	contextData.setTaskData(INDEX_1 + id, currentLine);
        	contextData.setTaskData(COUNT + id, count);
        	contextData.setTaskData(SUM_X + id, sumX);
        	contextData.setTaskData(SUM_Y + id, sumY);
        	contextData.setTaskData(START_TIME + id, startTime);
        	contextData.setTaskData(DURATION + id, duration);
        	contextData.setTaskData(SENDTIME + id, sendtime);
        	contextData.setTaskData(VELOCITY + id, preVelocity);
        	contextData.setTaskData(SOBJECT + id, sObjectTmp);
        	
        	collector.emit(new Values(result, input.getValue(2)));
		}
		
    	/**
    	 * Process each x,y coordinate
    	 * 
    	 * @param x
    	 * @param y
    	 * @param time
    	 * @param dis
    	 */
		public void putXY(int x, int y, int time, long send,
				int lineId) {
			if (lineId - currentLine == 1) {
				count++;
				sumX += x;
				sumY += y;

				currentLine = lineId;
				duration = time - startTime;
			} else {
				storeFix(x, y, time, send, lineId);
			}
		} 		

		public void storeFix(int x, int y, int time, long send, int lineId){
			if (count > 0
					&& duration > Constants.FIXATION_DURATION_THRESHOLD) {
				result = result + (float) sumX / count
						+ Constants.PARAMETER_SPLIT + (float) sumY / count
						+ Constants.PARAMETER_SPLIT + startTime
						+ Constants.PARAMETER_SPLIT + duration
						+ Constants.PARAMETER_SPLIT + sendtime
						+ Constants.PARAMETER_SPLIT;
			}
			count = 0;
			count++;
			sumX = x;
			sumY = y;
			startTime = time;
			duration = 0;
			currentLine = lineId;
			sendtime = send;
		}		
		
		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			// TODO Auto-generated method stub
			declarer.declare(new Fields("result", "return-info"));
		}
	}
	
	@SuppressWarnings("serial")
	public static class Feature extends BaseBasicBolt{

		@Override
		public void execute(Tuple input, BasicOutputCollector collector) {
			// TODO Auto-generated method stub
			String data[] = input.getString(0).split(Constants.PARAMETER_SPLIT);
			Integer id = Integer.parseInt(data[data.length - 1]);
			collector.emit(new Values(id, data, input.getValue(1)));
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			// TODO Auto-generated method stub
			declarer.declare(new Fields("id", "data", "requestid"));
		}
		
	}	
	
    public static TopologyBuilder construct(int dataSpout, int receiveBolt, int processBolt, int returnBolt) {
    	TopologyBuilder builder = new TopologyBuilder();
    	DRPCSpout spout = new DRPCSpout("Classification");
    	builder.setSpout("drpc", spout, dataSpout);
        builder.setBolt("receive", new ReceiveData(), receiveBolt).shuffleGrouping("drpc");
        builder.setBolt("process", new ProcessData(), processBolt).fieldsGrouping("receive", new Fields("id"));
        builder.setBolt("return", new ReturnResults(), returnBolt).shuffleGrouping("process");
        return builder;
    }
	
	/**
	 * @param args
	 * @throws InvalidTopologyException 
	 * @throws AlreadyAliveException 
	 */
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
		// TODO Auto-generated method stub
        TopologyBuilder builder = construct(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]), Integer.parseInt(args[5]));
        //LinearDRPCTopologyBuilder builder = construct(3, 3);
        Config conf = new Config();
        
        if(args==null || args.length==0) {
            conf.setMaxTaskParallelism(3);
            LocalDRPC drpc = new LocalDRPC();
            LocalCluster cluster = new LocalCluster();
            //cluster.submitTopology("reach-drpc", conf, builder.createLocalTopology(drpc));
            
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
            
            send = send + "1";
            System.out.println(send);
            long start = System.currentTimeMillis();
            System.out.println("Output file: " + drpc.execute("Classification", send));
            System.out.println("Running time: " + (float)(System.currentTimeMillis() - start)/1000);
            System.out.println("Output file: " + drpc.execute("Classification", "1"));
            cluster.shutdown();
            drpc.shutdown();
        } else {
            conf.setNumWorkers(Integer.parseInt(args[1]));
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        }        
	}

}
