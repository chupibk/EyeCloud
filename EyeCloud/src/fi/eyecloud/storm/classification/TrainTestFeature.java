package fi.eyecloud.storm.classification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.conf.Library;
import fi.eyecloud.input.ReadTextFile;
import fi.eyecloud.lib.FObject;
import fi.eyecloud.lib.FObjectSerializer;
import fi.eyecloud.lib.FeatureObject;
import fi.eyecloud.lib.SObject;
import fi.eyecloud.lib.SObjectSerializer;
import fi.eyecloud.lib.SqObject;
import fi.eyecloud.lib.SqObjectSerializer;
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

/**
 * Testing by features
 * 
 * @author chung
 *
 */
public class TrainTestFeature {

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
		private static String SOBJECT_TMP	= "sobject_tmp";
		private static String FOBJECTS		= "fobjects_";
		private static String SOBJECTS		= "sobjects_";
		private static String KEYPRESS		= "keypress_";
		
    	private int sumX;
    	private int sumY;
    	private int count = 0;
    	private int startTime;
    	private int duration = 0;
    	private long sendtime = 0;
    	private int currentLine = 0;
    	private float preVelocity = 0;
    	private SObject sObjectTmp;
    	private List<FObject> fObjects;
    	private List<SObject> sObjects;
    	private int keypress;
    	
    	BasicOutputCollector collectorEmit;
    	Object idEmit;
    	Integer id;
    	
		@SuppressWarnings("rawtypes")
		@Override
		public void prepare(Map conf, TopologyContext context) {
			contextData = context;
		}		
		
		@SuppressWarnings("unchecked")
		@Override
		public void execute(Tuple input, BasicOutputCollector collector) {
			String data[] = input.getString(0).split(Constants.PARAMETER_SPLIT);
			id = Integer.parseInt(data[data.length - 1]);
			idEmit = input.getValue(1);			
			
			collectorEmit = collector;
			
			// Set values
			sumX = sumY = count = startTime = duration = currentLine = 0;
			preVelocity = 0;
			sObjectTmp = new SObject(0, 0, 0, 0, 0);
			fObjects = new ArrayList<FObject>();
			sObjects = new ArrayList<SObject>();
			keypress = 0;
			
			//idEmit = input.getValue(2);
			//id = input.getInteger(0);
			//String data[] = (String[]) input.getValue(1);
        	int length = data.length/Constants.PARAMETER_NUMBER_FIXATION;
        	int x1, y1, time1, x2, y2, time2, index2, event;
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
				preVelocity = Float.parseFloat(contextData.getTaskData(VELOCITY + id).toString());	
			if (contextData.getTaskData(SOBJECT_TMP + id) != null)
				sObjectTmp = (SObject) contextData.getTaskData(SOBJECT_TMP + id);				
			if (contextData.getTaskData(FOBJECTS + id) != null)
				fObjects = (List<FObject>) contextData.getTaskData(FOBJECTS + id);				
			if (contextData.getTaskData(SOBJECTS + id) != null)
				sObjects = (List<SObject>) contextData.getTaskData(SOBJECTS + id);							
			if (contextData.getTaskData(KEYPRESS + id) != null)
				keypress = Integer.parseInt(contextData.getTaskData(KEYPRESS + id).toString());
			
			if (data.length == 1){
				if (count > 0 && duration > Constants.FIXATION_DURATION_THRESHOLD) {
					storeFix(0, 0, 0, 0, 0, 0);
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
	        	contextData.setTaskData(SOBJECT_TMP + id, null);
	        	contextData.setTaskData(FOBJECTS + id, null);
	        	contextData.setTaskData(SOBJECTS + id, null);
	        	contextData.setTaskData(KEYPRESS + id, null);
	        	
	        	collectorEmit.emit(new Values((int)0, null, (int)1, id, idEmit));
				return;
			}
			
        	for (int i=0; i < length; i++){
        		x2 = Integer.parseInt(data[i*Constants.PARAMETER_NUMBER_FIXATION]);
        		y2 = Integer.parseInt(data[i*Constants.PARAMETER_NUMBER_FIXATION + 1]);
        		time2 = Integer.parseInt(data[i*Constants.PARAMETER_NUMBER_FIXATION + 2]);
        		dis2 = Float.parseFloat(data[i*Constants.PARAMETER_NUMBER_FIXATION + 3]);
        		send2 = Long.parseLong(data[i*Constants.PARAMETER_NUMBER_FIXATION + 4]);
        		index2 = Integer.parseInt(data[i*Constants.PARAMETER_NUMBER_FIXATION + 5]);
        		event = Integer.parseInt(data[i*Constants.PARAMETER_NUMBER_FIXATION + 6]);
        		if (event != 0) keypress = 1;
        		
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
    					putXY(x2, y2, time2, send2, index2, event);
    				}else{
    					storeFix(x2, y2, time2, send2, index2, event);
						float d = (float)Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
						float a = (tmp - preVelocity) / (time2 - time1);
						sObjectTmp.addRawData(x1, y1, time1, time2 - time1, d, tmp, a);    					
    				}
    				
    				preVelocity = tmp;
    			}
    			
    			if (event == 0){
    				x1 = x2;
    				y1 = y2;
    				time1 = time2;
    				dis1 = dis2;
    			}
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
        	contextData.setTaskData(SOBJECT_TMP + id, sObjectTmp);
        	contextData.setTaskData(FOBJECTS + id, fObjects);
        	contextData.setTaskData(SOBJECTS + id, sObjects);
        	contextData.setTaskData(KEYPRESS + id, keypress);
        	
        	collectorEmit.emit(new Values((int)0, null, (int) 0, id, idEmit));
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
				int lineId, int event) {
			if (lineId - currentLine == 1) {
				count++;
				sumX += x;
				sumY += y;

				currentLine = lineId;
				duration = time - startTime;
			} else {
				storeFix(x, y, time, send, lineId, event);
			}
		} 		

		public void storeFix(int x, int y, int time, long send, int lineId, int event){
			if (count > 0
					&& duration > Constants.FIXATION_DURATION_THRESHOLD) {
				FObject fo = new FObject(sumX/count, sumY/count, startTime, duration, keypress);
				if (sObjectTmp.getRawNumber() >= 0){
					sObjectTmp.calValues();
					sObjects.add(sObjectTmp);
				}						
				fObjects.add(fo);
				
				if (fObjects.size() >= Constants.FIXATION_SEQUENCE_NUMBER){
					SqObject sqo = new SqObject(); 
					for (int i = Constants.FIXATION_SEQUENCE_NUMBER - 1; i >= 0; i--){
						sqo.addFObject(fObjects.get(fObjects.size() - 1 - i));
					}
					
					for (int i = Constants.FIXATION_SEQUENCE_NUMBER - 1; i >= 0; i--){
						sqo.addSObject(sObjects.get(sObjects.size() - 1 - i));
					}
					
					collectorEmit.emit(new Values((int)1, sqo, (int) 0, id, idEmit));
				}				
				
				keypress = 0;
				
				if (count > 1)
					sObjectTmp = new SObject(0, 0, 0, 0, 0);				
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
			declarer.declare(new Fields("check", "sequence", "state", "id", "requestid"));
		}
	}
	
	@SuppressWarnings("serial")
	public static class Feature extends BaseBasicBolt{

		@Override
		public void execute(Tuple input, BasicOutputCollector collector) {
			// TODO Auto-generated method stub
			String result = "";
			if (input.getInteger(0) == 1){
				SqObject sqo = (SqObject) input.getValue(1);
				FeatureObject f = new FeatureObject(sqo);
				result = 	f.getIntention() + " "
							+"1:" + f.getFixationMean() + " "
							+"2:" + f.getFixationSum() + " "
							+"3:" + f.getFixationEvent() + " "
							+"4:" + f.getFixationPrior() + " "
							+"5:" + f.getSaccadeMean() + " "
							+"6:" + f.getSaccadeSum() + " "
							+"7:" + f.getSaccadeLast() + " "
							+"8:" + f.getSaccadeMeanDistance() + " "
							+"9:" + f.getSaccadeSumDistance() + " "
							+"10:" + f.getSaccadeLastDistance() + " "
							+"11:" + f.getSaccadeVelocityMean() + " "
							+"12:" + f.getSaccadeLastVelocity() + " "
							+"13:" + f.getSaccadeAccelerationMean()
							+"\n";	
				collector.emit(new Values((int)1, result, input.getInteger(2), input.getInteger(3), input.getValue(4)));
			}else{				
				collector.emit(new Values((int)0, null, input.getInteger(2), input.getInteger(3), input.getValue(4)));
			}	
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			// TODO Auto-generated method stub
			declarer.declare(new Fields("check", "feature", "state", "id", "requestid"));
		}
	}
		
	public static TopologyBuilder construct(int dataSpout, int processBolt, int featureBolt, int returnBolt) {
		TopologyBuilder builder = new TopologyBuilder();
		DRPCSpout spout = new DRPCSpout("TrainTest");
		builder.setSpout("drpc", spout, dataSpout);
		builder.setBolt("process", new ProcessData(), processBolt)
				.shuffleGrouping("drpc");
		builder.setBolt("feature", new Feature(), featureBolt).shuffleGrouping(
				"process");
		builder.setBolt("return", new ReturnResults(), returnBolt)
				.shuffleGrouping("process");
		return builder;
	}

	/**
	 * @param args
	 * @throws InvalidTopologyException
	 * @throws AlreadyAliveException
	 */
	public static void main(String[] args) throws AlreadyAliveException,
			InvalidTopologyException {
		// TODO Auto-generated method stub
		int n = Integer.parseInt(args[1]);
		TopologyBuilder builder = construct(1, 1, n, 1);
		LocalDRPC drpc = new LocalDRPC();
		//TopologyBuilder builder = construct(drpc, 1, 1, 1, 3, 1, 3, 1, 1);
		
		Config conf = new Config();
		conf.registerSerialization(FObject.class, FObjectSerializer.class);
		conf.registerSerialization(SObject.class, SObjectSerializer.class);
		conf.registerSerialization(SqObject.class, SqObjectSerializer.class);

		if (args == null || args.length == 0) {
			conf.setMaxTaskParallelism(3);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("reach-drpc", conf, builder.createTopology());

			ReadTextFile data = new ReadTextFile("classification/AjayaCMD.txt");
			String currentSend = "";
			int count = 0;
			while (data.readNextLine() != null) {
				int x = (Integer.parseInt(data
						.getField(Constants.GazePointXLeft)) + Integer
						.parseInt(data.getField(Constants.GazePointXRight))) / 2;
				int y = (Integer.parseInt(data
						.getField(Constants.GazePointYLeft)) + Integer
						.parseInt(data.getField(Constants.GazePointYRight))) / 2;
				currentSend = currentSend + x + Constants.PARAMETER_SPLIT;
				currentSend = currentSend + y + Constants.PARAMETER_SPLIT;
				int timestamp = Integer.parseInt(data
						.getField(Constants.Timestamp));
				currentSend = currentSend + timestamp
						+ Constants.PARAMETER_SPLIT;

				float dis = (Float.parseFloat(data
						.getField(Constants.DistanceLeft)) + Float
						.parseFloat(data.getField(Constants.DistanceRight))) / 2;

				currentSend = currentSend + dis + Constants.PARAMETER_SPLIT;
				currentSend = currentSend + System.currentTimeMillis()
						+ Constants.PARAMETER_SPLIT;
				currentSend = currentSend
						+ Integer.parseInt(data.getField(Constants.Number))
						+ Constants.PARAMETER_SPLIT;

				int keypress = 0;
				if (Integer.parseInt(data.getField(Constants.EventKey)) != Constants.UNKNOWN) {
					keypress = Integer.parseInt(data
							.getField(Constants.EventKey));
				}
				currentSend = currentSend + keypress
						+ Constants.PARAMETER_SPLIT;
				count++;

				if (count == 5000) {
					currentSend = currentSend + "1";
					System.out.println(currentSend);
					System.out.println("Output file: "
							+ drpc.execute("TrainTest", currentSend));
					count = 0;
					currentSend = "";
				}
			}

			if (count > 0) {
				currentSend = currentSend + "1";
				System.out.println("Output file: "
						+ drpc.execute("TrainTest", currentSend));
				count = 0;
				currentSend = "";
			}

			long start = System.currentTimeMillis();
			System.out.println("Final Send: " + drpc.execute("TrainTest", "1"));
			System.out.println("Running time: "
					+ (float) (System.currentTimeMillis() - start) / 1000);
			cluster.shutdown();
			drpc.shutdown();
		} else {
			conf.setNumWorkers(Integer.parseInt(args[1]));
			StormSubmitter.submitTopology(args[0], conf,
					builder.createTopology());
		}
	}
}
