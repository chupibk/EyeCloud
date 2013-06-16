package fi.eyecloud.storm;

import backtype.storm.utils.DRPCClient;
import fi.eyecloud.conf.Constants;
import fi.eyecloud.input.ReadTextFile;

public class I_VT_Test {

	public static void main(String[] args) throws Exception {
		String HOSTNAME = "54.229.50.244";
		int timePeriod = 1000;//Integer.parseInt(args[0]); 	//ms
		int packNumber = 10;//Integer.parseInt(args[1]);		//time
		
		DRPCClient client = new DRPCClient(HOSTNAME, 3772);
		long totalTime = 0;
		
		ReadTextFile data = new ReadTextFile(
				"data/testPDF.txt");
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
				System.out.println(currentSend);
				long start = System.currentTimeMillis();
				client.execute("I_VT", currentSend);
				totalTime = totalTime + System.currentTimeMillis() - start;
				
				long dif = System.currentTimeMillis() - tmpTime;
				tmpTime = System.currentTimeMillis();
				if (dif < timePeriod)
					Thread.sleep(timePeriod - dif);
				
				currentPacket++;
				currentTime = timestamp;
				currentSend = "";
			}
			
			if (currentPacket == packNumber) break;
		}
		
		System.out.println("Time Period: " + timePeriod + " - " + "Packet Number: " + packNumber);
		System.out.println("Running time: " + totalTime);
		System.out.println("Average Time: " + (float)totalTime/(float)packNumber);
		System.out.println("Testing time: " + (System.currentTimeMillis() - startTest));
		client.close();
	}

}
