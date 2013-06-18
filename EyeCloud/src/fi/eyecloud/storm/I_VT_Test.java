package fi.eyecloud.storm;

import backtype.storm.utils.DRPCClient;
import fi.eyecloud.conf.Constants;
import fi.eyecloud.input.ReadTextFile;

public class I_VT_Test {

	public static void main(String[] args) throws Exception {
		String HOSTNAME = args[0]; //"54.229.50.244";
		int timePeriod = Integer.parseInt(args[1]); 	//ms
		int packNumber = Integer.parseInt(args[2]);		//time
		
		DRPCClient client = new DRPCClient(HOSTNAME, 3772);
		long totalTime = 0;
		
		ReadTextFile data = new ReadTextFile(
				"data/17June.txt");
		String currentSend = "";
		int currentTime = 0;
		int currentPacket = 0;
		long tmpTime = System.currentTimeMillis();
		long startTest = System.currentTimeMillis();
			
		data.readNextLine();
		for (;;) {
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
				client.execute("I_VT", currentSend);
				totalTime = totalTime + System.currentTimeMillis() - start;
				
				long dif = System.currentTimeMillis() - tmpTime;
				if (dif < timePeriod)
					Thread.sleep(timePeriod - dif);
				tmpTime = System.currentTimeMillis();
				
				currentPacket++;
				currentTime = timestamp;
				currentSend = "";
			}
			
			if (currentPacket == packNumber) break;
			if (data.readNextLine() == null){
				data.resetFile();
				data.readNextLine();
				currentTime = 0;
			}
		}
		
		System.out.println("Time Period: " + timePeriod + " - " + "Packet Number: " + packNumber);
		System.out.println("Running time: " + totalTime + " - " + "Packet sent: " + currentPacket);
		System.out.println("Average Time: " + (float)totalTime/(float)packNumber);
		System.out.println("Testing time: " + (System.currentTimeMillis() - startTest));
		data.closeFile();
		//client.close();
	}

}
