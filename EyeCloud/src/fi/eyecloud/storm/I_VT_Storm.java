package fi.eyecloud.storm;

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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.conf.Library;
import fi.eyecloud.input.ReadTextFile;

@SuppressWarnings("deprecation")
/**
 * IVT Testing on the cloud not using DRPC
 * 
 * @author chung
 *
 */
public class I_VT_Storm {
    @SuppressWarnings("serial")
	public static class LoadData extends BaseBasicBolt {
        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
        	Object id = tuple.getValue(0);
        	String arg[] = tuple.getString(1).split(Constants.PARAMETER_SPLIT);
            String filePath = arg[0];
            int packetNumber = Integer.parseInt(arg[1]);
            ReadTextFile data = new ReadTextFile(filePath);
            
            int count = 1;
            int packetId = 0;
            List<String> packetData = new ArrayList<String>();
            String current;
            current = data.readNextLine();
            packetData.add(current);
            packetData.add(current);
            
            while ((current = data.readNextLine()) != null){
            	count++;
            	packetData.add(current);
            	if (count == packetNumber){
            		List<String> tmp = new ArrayList<String>();
            		for (int i =0; i < packetData.size(); i++)
            			tmp.add(packetData.get(i));
            		collector.emit(new Values(id, packetId, tmp, data.getMapHeader(), packetNumber));
            		packetId++;
            		count = 0;
            		packetData.clear();
            		packetData.add(current);
            	}
            }
            if (count > 0){
        		collector.emit(new Values(id, packetId, packetData, data.getMapHeader(), packetNumber));
        		packetId++;
            }
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "packetId", "packetData", "header", "length"));           
        }        
    }
    
    @SuppressWarnings("serial")
	public static class SplitData extends BaseBasicBolt {
    	private Map<String, Integer> mapHeader;
    	private List<String> packetData;
    	
        @SuppressWarnings("unchecked")
		@Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
        	Object id = tuple.getValue(0);
            int packetId = tuple.getInteger(1);
            packetData = (List<String>) tuple.getValue(2);
			mapHeader = (Map<String, Integer>) tuple.getValue(3);
			int packetNumber = tuple.getInteger(4);
			//System.out.println(packetId + " - " + packetData.size());
			//System.out.println(packetData.get(0));			
			
			List<Integer> x = new ArrayList<Integer>();
			List<Integer> y = new ArrayList<Integer>();
			List<Integer> time = new ArrayList<Integer>();
			List<Integer> kind = new ArrayList<Integer>();
			
			String split[] = packetData.get(0).split(Constants.SPLIT_MARK);
			int x1 = Integer
					.parseInt(getField(split, Constants.GazePointX));
			int y1 = Integer
					.parseInt(getField(split, Constants.GazePointY));
			int time1 = Integer.parseInt(getField(split,
					Constants.Timestamp));
			float dis1 = (Float.parseFloat(getField(split,
					Constants.DistanceLeft)) + Float.parseFloat(getField(
					split, Constants.DistanceRight))) / 2;			
			
			for (int i = 1; i < packetData.size(); i++) {
				split = packetData.get(i).split(Constants.SPLIT_MARK);
				int x2 = Integer
						.parseInt(getField(split, Constants.GazePointX));
				int y2 = Integer
						.parseInt(getField(split, Constants.GazePointY));
				int time2 = Integer.parseInt(getField(split,
						Constants.Timestamp));
				float dis2 = (Float.parseFloat(getField(split,
						Constants.DistanceLeft)) + Float.parseFloat(getField(
						split, Constants.DistanceRight))) / 2;
				
				x.add(x2);
				y.add(y2);
				time.add(time2);
				
				if (x1 >= 0 && y1 >= 0 && time1 >= 0 && dis1 >= 0 && x2 >= 0
						&& y2 >= 0 && time2 >= 0 && dis2 >= 0) {
					float tmp = Library.VT_Degree(x1, y1, x2, y2, dis1, dis2,
							time2 - time1);
					// time1 = time2 --> first coordinate
					if (tmp <= Constants.VELOCITY_THRESHOLD || time1 == time2) {
						kind.add(Constants.KIND_FIXATION);
					}else{
						kind.add(Constants.KIND_UNKNOWN);
					}
				}else{
					kind.add(Constants.KIND_UNKNOWN);
				}
				
				x1 = x2;
				y1 = y2;
				time1 = time2;
				dis1 = dis2;
			}
			
			collector.emit(new Values(id, packetId, x, y, time, kind, packetNumber));
        }

    	/**
    	 * Get information of a field
    	 * 
    	 * @param fieldName
    	 * @return
    	 */
    	public String getField(String[] split, String fieldName){
    		if (mapHeader.get(fieldName) == -1 || mapHeader.get(fieldName) >= split.length) 
    			return Integer.toString(Constants.UNKNOWN);
    		if (split[mapHeader.get(fieldName)].equals("")) 
    			return Integer.toString(Constants.UNKNOWN);
    		return split[mapHeader.get(fieldName)];
    	}        
        
        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "packetId", "x", "y", "time", "kind", "length"));
        }
    }
    
    @SuppressWarnings({ "serial", "rawtypes" })
	public static class MergeAggregator extends BaseBatchBolt {
        BatchOutputCollector _collector;
        Object _id;
        private Map<Integer, Integer> mapX    = new HashMap<Integer, Integer>();
        private Map<Integer, Integer> mapY    = new HashMap<Integer, Integer>();
        private Map<Integer, Integer> mapTime = new HashMap<Integer, Integer>();
        int max = Constants.UNKNOWN;
        int min = Constants.UNKNOWN;
        List<Integer> x, y, time, kind;
        
        @Override
        public void prepare(Map conf, TopologyContext context, BatchOutputCollector collector, Object id) {
            _collector = collector;
            _id = id;
        }

        @SuppressWarnings("unchecked")
		@Override
        public void execute(Tuple tuple) {
        	int packetId = tuple.getInteger(1);
        	x = (List<Integer>) tuple.getValue(2);
        	y = (List<Integer>) tuple.getValue(3);
        	time = (List<Integer>) tuple.getValue(4);
        	kind = (List<Integer>) tuple.getValue(5);
        	int packetNumber = tuple.getInteger(6);
        	
			for (int i = 0; i < x.size(); i++) {
				if (kind.get(i) == Constants.KIND_FIXATION) {
					int lineId = packetId*packetNumber + i + 1;
					mapX.put(lineId, x.get(i));
					mapY.put(lineId, y.get(i));
					mapTime.put(lineId, time.get(i));
					if (lineId > max || max == Constants.UNKNOWN)
						max = lineId;
					if (lineId < min || min == Constants.UNKNOWN)
						min = lineId;
				}
			}
        }
        
        @Override
        public void finishBatch() {
    		try {
    			FileWriter fw = new FileWriter(Constants.OUTPUT_FILE);
    			BufferedWriter out = new BufferedWriter(fw);
    			int count = 0;
    			int sumX = 0;
    			int sumY = 0;
    			int startTime = 0;
    			int duration = 0;
    			int numberFixation = 0;
            	for (int i = min; i <= max; i++){
            		if (mapX.containsKey(i)){          			          	
            			if (count == 0){
            				count++;
                			sumX = mapX.get(i);
                			sumY = mapY.get(i);
                			startTime = mapTime.get(i);
            			}else{
            				count++;
            				sumX += mapX.get(i);
            				sumY += mapY.get(i);
            				duration = mapTime.get(i) - startTime;
            			}
            		}else{
            			if (count > 0 && duration > Constants.FIXATION_DURATION_THRESHOLD){
            				out.write((float) sumX / count + Constants.SPLIT_MARK + (float) sumY / count + Constants.SPLIT_MARK 
            						+ startTime + Constants.SPLIT_MARK + duration + "\n");
            				numberFixation++;
            			}
            			count = 0;
            		}
            	}
            	out.write(numberFixation + "\n");
            	out.close();
            	_collector.emit(new Values(_id, min + "-" + max + "-" + numberFixation));
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}   
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "filePath"));
        }
    }
    
    public static LinearDRPCTopologyBuilder construct() {
    	LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("I_VT");
        builder.addBolt(new LoadData(), 1);
        builder.addBolt(new SplitData(), 7)
                 .shuffleGrouping();
        builder.addBolt(new MergeAggregator(), 1)
                 .fieldsGrouping(new Fields("id"));
        return builder;
    }
    
    public static void main(String[] args) throws Exception {
        LinearDRPCTopologyBuilder builder = construct();
        
        Config conf = new Config();
        
        if(args==null || args.length==0) {
            conf.setMaxTaskParallelism(3);
            LocalDRPC drpc = new LocalDRPC();
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("reach-drpc", conf, builder.createLocalTopology(drpc));
            
            long start = System.currentTimeMillis();
            System.out.println("Output file: " + drpc.execute("I_VT", "/Users/daothanhchung/Desktop/01-01-All-Data.txt@100000"));
            System.out.println("Running time: " + (float)(System.currentTimeMillis() - start)/1000);
            
            cluster.shutdown();
            drpc.shutdown();
        } else {
            conf.setNumWorkers(7);
            StormSubmitter.submitTopology(args[0], conf, builder.createRemoteTopology());
        }
    }
}
