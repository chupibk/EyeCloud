package fi.eyecloud.storm.heatmap;

import fi.eyecloud.storm.fixation.IVT;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;

@SuppressWarnings("deprecation")
public class DeployAll {

	/**
	 * @param args
	 * @throws InvalidTopologyException 
	 * @throws AlreadyAliveException 
	 */
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
		// TODO Auto-generated method stub
		
		int nodes = Integer.parseInt(args[0]);
		
		Config conf = new Config();
		
		conf.setNumWorkers(4);
		LinearDRPCTopologyBuilder website = WebsiteRendering.construct(nodes/2, 1, nodes/2);
		StormSubmitter.submitTopology("website", conf, website.createRemoteTopology());		
		
		conf.setNumWorkers(4);
		LinearDRPCTopologyBuilder youtube = YoutubeRendering.construct(2, 2, 2);
		StormSubmitter.submitTopology("youtube", conf, youtube.createRemoteTopology());	
		
		conf.setNumWorkers(1);
		LinearDRPCTopologyBuilder fixation = IVT.construct(1);
		StormSubmitter.submitTopology("fixation", conf, fixation.createRemoteTopology());
		
		conf.setNumWorkers(1);
		LinearDRPCTopologyBuilder heatmap = OfflineHeatmap.construct(1, 1, 1);
		StormSubmitter.submitTopology("heatmap", conf, heatmap.createRemoteTopology());
	}

}
