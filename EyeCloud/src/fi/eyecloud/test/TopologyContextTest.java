package fi.eyecloud.test;

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

@SuppressWarnings("deprecation")
public class TopologyContextTest {
    @SuppressWarnings("serial")
	public static class Process1 extends BaseBasicBolt {

    	@Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
        	Object id = tuple.getValue(0);
        	long data = tuple.getLong(0);
        	
        	collector.emit(new Values(id, Long.toString(data)));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "data"));           
        }
    }
    
	@SuppressWarnings("serial")
	public static class Process2 extends BaseBasicBolt {
		TopologyContext context;
		int count;
		
		@SuppressWarnings("rawtypes")
		@Override
		public void prepare(Map m, TopologyContext c){
			context = c;
			Object data = context.getTaskData("data");
			if (data != null){
				count = Integer.parseInt(data.toString());
			}else{
				count = 0;
			}
		}
		
		@Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
        	Object id = tuple.getValue(0);
        	
        	count++;
        	context.setTaskData("data", count);
        	
        	collector.emit(new Values(id, Integer.toString(count)));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "data"));           
        }
    }    
     
	@SuppressWarnings({ "serial", "rawtypes" })
	public static class Process3 extends BaseBatchBolt {
		BatchOutputCollector _collector;
        Object _id;
        
        @Override
        public void prepare(Map conf, TopologyContext context, BatchOutputCollector collector, Object id) {
            _collector = collector;
            _id = id;
        }		
		
    	@Override
        public void execute(Tuple tuple) {    		
        }

        @Override
        public void finishBatch() {
        }
    	
        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "result"));           
        }
    }	
	
    public static LinearDRPCTopologyBuilder construct(int numberProcess1, int numberProcess2) {
    	LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("topology_test");
        builder.addBolt(new Process1(), numberProcess1);
        builder.addBolt(new Process2(), numberProcess2).shuffleGrouping();
        //builder.addBolt(new AggregatorData(), numberAggregator).fieldsGrouping(new Fields("id"));
        return builder;
    }
    
    public static void main(String[] args) throws Exception {
        LinearDRPCTopologyBuilder builder = construct(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
    	//LinearDRPCTopologyBuilder builder = construct(2, 2);
        Config conf = new Config();
        
        if(args==null || args.length==0) {
            conf.setMaxTaskParallelism(3);
            LocalDRPC drpc = new LocalDRPC();
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("reach-drpc", conf, builder.createLocalTopology(drpc));
                     
            // Send data
            long start = System.currentTimeMillis();
            System.out.println(drpc.execute("topology_test", "Chung"));
            System.out.println("Running time: " + (float)(System.currentTimeMillis() - start)/1000);
            
            cluster.shutdown();
            drpc.shutdown();
        } else {
            conf.setNumWorkers(Integer.parseInt(args[1]));
            StormSubmitter.submitTopology(args[0], conf, builder.createRemoteTopology());
        }
    }
}
