package fi.eyecloud.test;

import java.io.IOException;
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
import fi.eyecloud.gui.heatmap.Colorization;
import fi.eyecloud.gui.lib.GuiConstants;
import fi.eyecloud.input.ReadTextFile;
import fi.eyecloud.utils.ClientFile;
 
/**
 * 
 * Rendering testing
 * @author chung
 *
 */
@SuppressWarnings("deprecation")
public class WebsiteRenderingNoID {

	@SuppressWarnings("serial")
	public static class ProcessData extends BaseBasicBolt {
		private double gaussianWindow[][] = null;
		private TopologyContext _context;
		private static String GAUSSIAN = "gaussian";

		public double gaussian2D(int x, int y) {
			double value;
			value = Math.exp(-(x * x + y * y)
					/ (2 * GuiConstants.SIGMA_USED * GuiConstants.SIGMA_USED));
			return value;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public void prepare(Map conf, TopologyContext context) {
			_context = context;
			gaussianWindow = (double[][]) _context.getTaskData(GAUSSIAN);
			if (gaussianWindow == null) {
				gaussianWindow = new double[GuiConstants.KERNEL_SIZE_USED + 1][GuiConstants.KERNEL_SIZE_USED + 1];
				for (int i = 0; i <= GuiConstants.KERNEL_SIZE_USED; i++) {
					for (int j = 0; j <= GuiConstants.KERNEL_SIZE_USED; j++) {
						gaussianWindow[i][j] = gaussian2D(i, j);
					}
				}
				_context.setTaskData(GAUSSIAN, gaussianWindow);
			}
		}

		@Override
		public void execute(Tuple tuple, BasicOutputCollector collector) {
			Object id = tuple.getValue(0);
			String tmp = tuple.getString(1);
			String data[] = tmp.split(Constants.PARAMETER_SPLIT);
			int type = Integer.parseInt(data[data.length - 1]);

			if (type == 1 || type == 2) {
				int numberParticipant = Integer.parseInt(data[data.length - 2]);
				int timeId = Integer.parseInt(data[data.length - 3]);
				int height = Integer.parseInt(data[data.length - 4]);
				int width = Integer.parseInt(data[data.length - 5]);
				int heatmapId = Integer.parseInt(data[data.length - 6]);
				
				int value[] = new int[data.length - 3];
				for (int i = 0; i < data.length - 3; i++) {
					value[i] = Math.round(Float.parseFloat(data[i]));
				}

				double[][] intensity = new double[width][height];
				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						intensity[i][j] = 0;
					}
				}

				for (int k = 0; k < value.length
						/ Constants.PARAMETER_NUMBER_HEATMAP; k++) {
					int x = value[k * Constants.PARAMETER_NUMBER_HEATMAP]
							/ GuiConstants.SCREEN_RATE;
					;
					int y = value[k * Constants.PARAMETER_NUMBER_HEATMAP + 1]
							/ GuiConstants.SCREEN_RATE;
					;
					int d = value[k * Constants.PARAMETER_NUMBER_HEATMAP + 2];

					for (int i = x - GuiConstants.KERNEL_SIZE_USED; i <= x
							+ GuiConstants.KERNEL_SIZE_USED; i++) {
						for (int j = y - GuiConstants.KERNEL_SIZE_USED; j <= y
								+ GuiConstants.KERNEL_SIZE_USED; j++) {
							if (i >= 0 && j >= 0 && i < width && j < height)
								intensity[i][j] += gaussianWindow[Math.abs(x
										- i)][Math.abs(y - j)]
										* d;
						}
					}
				}

				collector.emit(new Values(id, type, intensity, width, height, timeId, numberParticipant, heatmapId));
			} else {
				collector.emit(new Values(id, type, Constants.UNKNOWN,
						Constants.UNKNOWN, Constants.UNKNOWN, Constants.UNKNOWN, Constants.UNKNOWN, Constants.UNKNOWN));
			}
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(new Fields("id", "type", "intensity", "width",
					"height", "timeid", "numberparticipant", "heatmapid"));
		}
	}

	@SuppressWarnings("serial")
	public static class AggregatorData extends BaseBasicBolt {
		private TopologyContext contextData;
		private double intensity[][] = null;
		private int width = 0;
		private int height = 0;
		private int timeId = 0;
		private int numberParticipant = 0;
		private int currentParticipant = 0;
		private int heatmapId = 0;
		
		private static String INTENSITY = "intensity";
		private static String PARTICIPANT = "participant";
		
		@SuppressWarnings("rawtypes")
		@Override
		public void prepare(Map conf, TopologyContext context) {
			contextData = context;
		}

		@Override
		public void execute(Tuple tuple, BasicOutputCollector collector) {
			Object id = tuple.getValue(0);
			int type = tuple.getInteger(1);
			currentParticipant = 0;
			
			if (type == 1 || type == 2) {
				double[][] data = (double[][]) tuple.getValue(2);
				if (width == 0)
					width = tuple.getInteger(3);
				if (height == 0)
					height = tuple.getInteger(4);
				timeId = tuple.getInteger(5);
				numberParticipant = tuple.getInteger(6);
				heatmapId = tuple.getInteger(7);
				
				intensity = (double[][]) contextData.getTaskData(Integer.toString(timeId) + INTENSITY);
				if (contextData.getTaskData(Integer.toString(timeId) + PARTICIPANT) != null)
					currentParticipant = Integer.parseInt(contextData.getTaskData(Integer.toString(timeId) + PARTICIPANT).toString());
				currentParticipant++;
				
				if (intensity == null) {
					intensity = new double[width][height];
					for (int i = 0; i < width; i++) {
						for (int j = 0; j < height; j++) {
							intensity[i][j] = 0;
						}
					}
				}

				for (int i = 0; i < width; i++) {
					for (int j = 0; j < height; j++) {
						intensity[i][j] += data[i][j];
					}
				}
				
				if (numberParticipant == currentParticipant){
					// Find the previous intensity
					int pre = 0;
					for (int k=timeId-1; k > 0; k--){
						if (contextData.getTaskData(Integer.toString(k) + INTENSITY) != null){
							pre = k;
							break;
						}
					}
					double[][] intensityPre = (double[][]) contextData.getTaskData(Integer.toString(pre) + INTENSITY);
					if (intensityPre != null){
						for (int i = 0; i < width; i++) {
							for (int j = 0; j < height; j++) {
								intensity[i][j] += intensityPre[i][j];
							}
						}
					}
					contextData.setTaskData(Integer.toString(timeId) + INTENSITY, intensity);
					contextData.setTaskData(Integer.toString(pre) + INTENSITY, null);
					contextData.setTaskData(Integer.toString(pre) + PARTICIPANT, null);
					
					// Reset data in topology when receiving the last packet signal
					if (type == 2){
						contextData.setTaskData(Integer.toString(timeId) + INTENSITY, null);
						contextData.setTaskData(Integer.toString(timeId) + PARTICIPANT, null);						
					}
					
					collector.emit(new Values(id, type, intensity, width, height, timeId, heatmapId));
				}else{
					contextData.setTaskData(Integer.toString(timeId) + INTENSITY, intensity);
					contextData.setTaskData(Integer.toString(timeId) + PARTICIPANT, currentParticipant);
					collector.emit(new Values(id, 0, Constants.UNKNOWN, Constants.UNKNOWN, Constants.UNKNOWN, Constants.UNKNOWN, Constants.UNKNOWN));
				}
			}else{
				collector.emit(new Values(id, 0, Constants.UNKNOWN, Constants.UNKNOWN, Constants.UNKNOWN, Constants.UNKNOWN, Constants.UNKNOWN));
			}
		}
		
		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(new Fields("id", "type", "intensity", "width", "height", "timeid", "heatmapid"));
		}
	}

	@SuppressWarnings("serial")
	public static class ReturnData extends BaseBasicBolt {

		@Override
		public void execute(Tuple tuple, BasicOutputCollector collector) {
			// TODO Auto-generated method stub
			Object id = tuple.getValue(0);
			int type = tuple.getInteger(1);
			
			if (type == 1 || type == 2){
				double[][] data = (double[][]) tuple.getValue(2);
				int	width = tuple.getInteger(3);
				int	height = tuple.getInteger(4);
				int timeId = tuple.getInteger(5);
				int heatmapId = tuple.getInteger(6);
				
				collector.emit(new Values(id, "Generated Ok"));
				Colorization color = new Colorization(data, width, height);
				try {
					new ClientFile(color.getImage(), Integer.toString(timeId), Integer.toString(heatmapId));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				collector.emit(new Values(id, "Stored  Ok"));
			}
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			// TODO Auto-generated method stub
			declarer.declare(new Fields("id", "result"));
		}
		
	}
	
	public static LinearDRPCTopologyBuilder construct(int numberProcess,int numberAggregator, int numberReturn) {
		LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder(
				"website_rendering");
		builder.addBolt(new ProcessData(), numberProcess);
		builder.addBolt(new AggregatorData(), numberAggregator).shuffleGrouping();
		builder.addBolt(new ReturnData(), numberReturn).shuffleGrouping();
		return builder;
	}

	public static void main(String[] args) throws Exception {
		LinearDRPCTopologyBuilder builder = construct(Integer.parseInt(args[1]) + 1, 1, Integer.parseInt(args[1]));
		//LinearDRPCTopologyBuilder builder = construct(3, 1, 3);
		Config conf = new Config();

		if (args == null || args.length == 0) {
			conf.setMaxTaskParallelism(3);
			LocalDRPC drpc = new LocalDRPC();
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("reach-drpc", conf,
					builder.createLocalTopology(drpc));

			ReadTextFile data = new ReadTextFile("data/heatmap.txt");
			String send = "";
			while (data.readNextLine() != null) {
				send = send + data.getField(Constants.GazePointX)
						+ Constants.PARAMETER_SPLIT;
				send = send + data.getField(Constants.GazePointY)
						+ Constants.PARAMETER_SPLIT;
				send = send + data.getField(Constants.Duration)
						+ Constants.PARAMETER_SPLIT;
			}

			send = send + GuiConstants.MEDIA_WIDTH + Constants.PARAMETER_SPLIT
					+ GuiConstants.MEDIA_HEIGHT + Constants.PARAMETER_SPLIT
					+ "1";

			// Send data
			long start = System.currentTimeMillis();
			System.out.println(drpc.execute("website_rendering", send));
			System.out.println("Running time: "
					+ (float) (System.currentTimeMillis() - start) / 1000);

			cluster.shutdown();
			drpc.shutdown();
		} else {
			conf.setNumWorkers(Integer.parseInt(args[1])*2 + 2);
			StormSubmitter.submitTopology(args[0], conf,
					builder.createRemoteTopology());
		}
	}
}
