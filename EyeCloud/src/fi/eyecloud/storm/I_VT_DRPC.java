package fi.eyecloud.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.coordination.BatchOutputCollector;
import backtype.storm.coordination.CoordinatedBolt.FinishedCallback;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseBatchBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.conf.Library;
import fi.eyecloud.input.ReadTextFile;

public class I_VT_DRPC {
    public static class ProcessData extends BaseBasicBolt {
    	private int sumX;
    	private int sumY;
    	private int count;
    	private int startTime;
    	private int duration = 0;

    	private int currentLine = 0;
    	private int numberFixation = 0;
    	private String result = "";
    	
        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
        	result = "";
        	numberFixation = 0;
        	
        	Object id = tuple.getValue(0);
        	String data[] = tuple.getString(1).split(Constants.PARAMETER_SPLIT);
        	int length = data.length/Constants.PARAMETER_NUMBER;
        	int x1,y1,time1,x2,y2,time2;
        	float dis1, dis2;
        	x1 = y1 = time1 = 0;
        	dis1 = 0;
        	
        	for (int i=0; i < length; i++){
        		x2 = Integer.parseInt(data[i*Constants.PARAMETER_NUMBER]);
        		y2 = Integer.parseInt(data[i*Constants.PARAMETER_NUMBER + 1]);
        		time2 = Integer.parseInt(data[i*Constants.PARAMETER_NUMBER + 2]);
        		dis2 = Float.parseFloat(data[i*Constants.PARAMETER_NUMBER + 3]);
        		
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
        	
        	result = result + numberFixation;
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
    			}else{
    				count++;
    				sumX += x;
    				sumY += y;

    				currentLine = lineId;
    				duration = time - startTime;
    			}
    		}else {
    			if (count > 0 && duration > Constants.FIXATION_DURATION_THRESHOLD) {
    				result = result + (float) sumX / count
    						+ Constants.PARAMETER_SPLIT + (float) sumY
    						/ count + Constants.PARAMETER_SPLIT + startTime
    						+ Constants.PARAMETER_SPLIT + duration + Constants.PARAMETER_SPLIT;

    				numberFixation++;
    			}
    			count = 0;
    			count++;
    			sumX = x;
    			sumY = y;
    			startTime = time;
    			currentLine = lineId;
    		}		
    	}        
    }   
        
    public static LinearDRPCTopologyBuilder construct(int numberBolt) {
    	LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("I_VT");
        builder.addBolt(new ProcessData(), numberBolt);
        return builder;
    }
    
    public static void main(String[] args) throws Exception {
        LinearDRPCTopologyBuilder builder = construct(Integer.parseInt(args[1]));
        
        Config conf = new Config();
        
        if(args==null || args.length==0) {
            conf.setMaxTaskParallelism(3);
            LocalDRPC drpc = new LocalDRPC();
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("reach-drpc", conf, builder.createLocalTopology(drpc));
            
            ReadTextFile data = new ReadTextFile("/Users/daothanhchung/Desktop/EyeCloud/Data/test.txt");
            String send = "";
            while (data.readNextLine() != null){
            	send = send + data.getField(Constants.GazePointX) + Constants.PARAMETER_SPLIT;
            	send = send + data.getField(Constants.GazePointY) + Constants.PARAMETER_SPLIT;
            	send = send + data.getField(Constants.Timestamp) + Constants.PARAMETER_SPLIT;
            
    			float dis = (Float.parseFloat(data.getField(Constants.DistanceLeft)) + 
    					Float.parseFloat(data.getField(Constants.DistanceRight))) / 2;
    			
    			send = send + dis + Constants.PARAMETER_SPLIT;
    			System.out.println(data.getCurrentLine());
            }
            
            System.out.println(send);
            long start = System.currentTimeMillis();
            System.out.println("Output file: " + drpc.execute("I_VT", send));
            System.out.println("Running time: " + (float)(System.currentTimeMillis() - start)/1000);
            
            cluster.shutdown();
            drpc.shutdown();
        } else {
            conf.setNumWorkers(Integer.parseInt(args[1]));
            StormSubmitter.submitTopology(args[0], conf, builder.createRemoteTopology());
        }
    }
}
