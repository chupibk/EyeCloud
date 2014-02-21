package fi.eyecloud.storm;

import backtype.storm.utils.DRPCClient;
import fi.eyecloud.conf.Constants;
import fi.eyecloud.input.ReadTextFile;

public class I_VT_Test {

	/**
	 * Client testing
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String HOSTNAME = "192.168.40.9"; //"54.229.50.244";
		int timePeriod = 2000; 	//ms
		
		DRPCClient client = new DRPCClient(HOSTNAME, 3772);
		long totalTime = 0;
		
		ReadTextFile data = new ReadTextFile(
				"data/17June.txt");
		String currentSend = "";
		int currentTime = 0;
		int currentPacket = 0;
		long tmpTime = System.currentTimeMillis();
		long startTest = System.currentTimeMillis();
			
		while (data.readNextLine() != null) {
			currentSend = currentSend + data.getField(Constants.GazePointX)
					+ Constants.PARAMETER_SPLIT;
			currentSend = currentSend + data.getField(Constants.GazePointY)
					+ Constants.PARAMETER_SPLIT;
			int timestamp = Integer.parseInt(data.getField(Constants.Timestamp));
			currentSend = currentSend + timestamp + Constants.PARAMETER_SPLIT;

			float dis = (Float
					.parseFloat(data.getField(Constants.DistanceLeft)) + Float
					.parseFloat(data.getField(Constants.DistanceRight))) / 2;

			currentSend = currentSend + dis + Constants.PARAMETER_SPLIT;
			
			if (timestamp - currentTime >= timePeriod){
				//System.out.println(currentSend);
				long start = System.currentTimeMillis();
				client.execute("ivt", currentSend);
				totalTime = totalTime + System.currentTimeMillis() - start;
				
				long dif = System.currentTimeMillis() - tmpTime;
				if (dif < timePeriod)
					Thread.sleep(timePeriod - dif);
				tmpTime = System.currentTimeMillis();
				
				currentPacket++;
				currentTime = timestamp;
				currentSend = "";
				break;
			}
		}
		
		System.out.println("Time Period: " + timePeriod + " - " + "Packet sent: " + currentPacket);
		System.out.println("Running time: " + totalTime + " - ");
		System.out.println("Average Time: " + (float)totalTime/(float)currentPacket);
		System.out.println("Testing time: " + (System.currentTimeMillis() - startTest));
		data.closeFile();
		client.close();
	}

}
