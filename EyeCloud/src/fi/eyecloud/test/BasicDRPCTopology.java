package fi.eyecloud.test;

import fi.eyecloud.lib.FObject;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.drpc.DRPCSpout;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.drpc.ReturnResults;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * This topology is a basic example of doing distributed RPC on top of Storm. It implements a function
 * that appends a "!" to any string you send the DRPC function.
 * 
 * See https://github.com/nathanmarz/storm/wiki/Distributed-RPC for more information on 
 * doing distributed RPC on top of Storm.
 */
@SuppressWarnings({ "deprecation", "unused" })
public class BasicDRPCTopology {
    @SuppressWarnings("serial")
	public static class ExclaimBolt extends BaseBasicBolt {
        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("result", "return-info"));
        }

        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
            Object retInfo = tuple.getValue(1);
            int t = Integer.parseInt(tuple.getString(0));
            FObject f = new FObject(0, 0, 0, t, 0);
            collector.emit(new Values(f, retInfo));
        }
    
    }
    
    @SuppressWarnings("serial")
	public static class Next extends BaseBasicBolt {
        @Override
        public void execute(Tuple tuple, BasicOutputCollector collector) {
        	FObject f = (FObject) tuple.getValue(0);
            collector.emit(new Values(Integer.toString(f.getDuration()), tuple.getValue(1)));
        }

        @Override
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declare(new Fields("id", "result"));
        }
    
    }    
    
    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();
        LocalDRPC drpc = new LocalDRPC();
        DRPCSpout spout = new DRPCSpout("exclamation");
        builder.setSpout("drpc", spout, 3);
        builder.setBolt("process", new ExclaimBolt(), 3).shuffleGrouping("drpc");
        builder.setBolt("next", new Next(), 3).shuffleGrouping("process");
        builder.setBolt("return", new ReturnResults(), 3).shuffleGrouping("next");
        //builder.addBolt(new Next(), 3);
        
        Config conf = new Config();
        
        if(args==null || args.length==0) {
            LocalCluster cluster = new LocalCluster();
            
            cluster.submitTopology("drpc-demo", conf, builder.createTopology());

            for(String word: new String[] {"hello", "goodbye"}) {
                System.out.println("Result for \"" + word + "\": "
                        + drpc.execute("exclamation", word));
            }
            
            cluster.shutdown();
            drpc.shutdown();
        } else {
            conf.setNumWorkers(3);
            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        }
    }
}
