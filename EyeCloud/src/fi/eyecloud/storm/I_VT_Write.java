package fi.eyecloud.storm;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import backtype.storm.utils.DRPCClient;
import fi.eyecloud.conf.Constants;
import fi.eyecloud.input.ReadTextFile;

public class I_VT_Write {

	public static void writeFile(BufferedWriter o, String data){
		String split[] = data.split(Constants.PARAMETER_SPLIT);
		for (int i=0; i < split.length/Constants.PARAMETER_NUMBER; i++){
			try {
				o.write(split[i*Constants.PARAMETER_NUMBER]
						+ Constants.SPLIT_MARK + split[i*Constants.PARAMETER_NUMBER + 1]
						+ Constants.SPLIT_MARK + split[i*Constants.PARAMETER_NUMBER + 2]
						+ Constants.SPLIT_MARK + split[i*Constants.PARAMETER_NUMBER + 3]
						+ "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	public static void main(String[] args) throws Exception {
		String HOSTNAME = "54.229.69.151";//args[0]; //"54.229.50.244";
		int timePeriod = 40;//Integer.parseInt(args[1]); 	//ms
		
		DRPCClient client = new DRPCClient(HOSTNAME, 3772);
		
		ReadTextFile data = new ReadTextFile(
				"data/17June.txt");
		String currentSend = "";
		int currentTime = 0;
		long startTest = System.currentTimeMillis();
		
		BufferedWriter out = null;
		try {
			FileWriter fw = new FileWriter("data/17June_" + timePeriod);
			out = new BufferedWriter(fw);
			out.write("GazePointX\tGazePointY\tRecordingTimestamp\tDuration\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
				String result = client.execute("I_VT", currentSend);
				//System.out.println(result);
				writeFile(out, result);
				currentTime = timestamp;
				currentSend = "";
			}
		}
		
		if (!currentSend.equals("")){
			System.out.println(currentSend);
			String result = client.execute("I_VT", currentSend);
			System.out.println(result);
			writeFile(out, result);
		}
		
		System.out.println("Time Period: " + timePeriod);
		System.out.println("Testing time: " + (System.currentTimeMillis() - startTest));
		data.closeFile();
		client.close();
		out.close();
	}

}
