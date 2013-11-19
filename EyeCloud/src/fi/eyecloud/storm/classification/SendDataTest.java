package fi.eyecloud.storm.classification;

import org.apache.thrift7.TException;

import backtype.storm.generated.DRPCExecutionException;
import backtype.storm.utils.DRPCClient;
import fi.eyecloud.conf.Constants;
import fi.eyecloud.input.ReadTextFile;

public class SendDataTest {

	public SendDataTest(String hostName, String folder, int numberFile, int max, int id)
			throws TException, DRPCExecutionException {
		DRPCClient client = new DRPCClient(hostName, 3772);
		long start = System.currentTimeMillis();
		long processingTime = 0;

		for (int i = 1; i <= numberFile; i++) {
			ReadTextFile data = new ReadTextFile(folder + "/" + i + ".txt");
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

				if (count == max) {
					currentSend = currentSend + id;
					String result = client.execute("TrainTest", currentSend);
					processingTime += Long.parseLong(result);
					count = 0;
					currentSend = "";
				}
			}

			if (count > 0) {
				currentSend = currentSend + id;
				String result = client.execute("TrainTest", currentSend);
				processingTime += Long.parseLong(result);
				count = 0;
				currentSend = "";
			}
			data.closeFile();
		}
		
		System.out.println("Sending time: " + (System.currentTimeMillis() - start - processingTime));
		System.out.println("Feature computation time: " + processingTime);
	
		start = System.currentTimeMillis();
		System.out.println("Train: "
				+ client.execute("TrainTest", "1" + Constants.PARAMETER_SPLIT + Integer.toString(id)));
		System.out.println("Running time of training: " + (System.currentTimeMillis() - start));
		
		/**
		start = System.currentTimeMillis();
		System.out.println("Test: "
				+ client.execute("TrainTest", "2" + Constants.PARAMETER_SPLIT + Integer.toString(id)));
		System.out.println("Running time of testing: " + (System.currentTimeMillis() - start));		
		*/

		client.close();
	}

	/**
	 * @param args
	 * @throws DRPCExecutionException
	 * @throws TException
	 */
	public static void main(String[] args) throws TException,
			DRPCExecutionException {
		new SendDataTest(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]),
				Integer.parseInt(args[4]));
	}

}
