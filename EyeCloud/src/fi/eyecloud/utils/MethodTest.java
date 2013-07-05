package fi.eyecloud.utils;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

@SuppressWarnings("deprecation")
public class MethodTest {
    @SuppressWarnings("serial")
	public static class ReturnData extends BaseBasicBolt {    	
        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
        	Object id = tuple.getValue(0);
        	collector.emit(new Values(id, tuple.getString(1) + "Chung pro"));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "result"));           
        }
        
    }
        
    public static LinearDRPCTopologyBuilder construct(int numberBolt) {
    	LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("method_test");
        builder.addBolt(new ReturnData(), numberBolt);
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
            cluster.submitTopology("test", conf, builder.createLocalTopology(drpc));
            
            long start = System.currentTimeMillis();
            System.out.println("Return: " + drpc.execute("method_test", "OkOk"));
            System.out.println("Running time: " + (float)(System.currentTimeMillis() - start)/1000);
            
            cluster.shutdown();
            drpc.shutdown();
        } else {
            conf.setNumWorkers(Integer.parseInt(args[1]));
            StormSubmitter.submitTopology(args[0], conf, builder.createRemoteTopology());
        }
    }
}
