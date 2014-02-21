import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import backtype.storm.utils.DRPCClient;
import libs.ReadTextFile;

public class IVTSendToStorm {

	public static void writeFile(BufferedWriter o, String data){
		String split[] = data.split(libs.Constants.PARAMETER_SPLIT);
		for (int i=0; i < split.length/libs.Constants.PARAMETER_NUMBER_FIXATION; i++){
			try {
				o.write(split[i*libs.Constants.PARAMETER_NUMBER_FIXATION]
						+ libs.Constants.SPLIT_MARK + split[i*libs.Constants.PARAMETER_NUMBER_FIXATION + 1]
						+ libs.Constants.SPLIT_MARK + split[i*libs.Constants.PARAMETER_NUMBER_FIXATION + 2]
						+ libs.Constants.SPLIT_MARK + split[i*libs.Constants.PARAMETER_NUMBER_FIXATION + 3]
						+ "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
        /**
         * Send data to the cluster for multi-source gaze visualization.
         * 
         * @param filePath
         * @param output
         * @throws Exception 
         */
	public IVTSendToStorm(String filePath, String output) throws Exception {
		int timePeriod = 10000;
		
		DRPCClient client = new DRPCClient(Constants.HOSTNAME, 3772);
		
		ReadTextFile data = new ReadTextFile(filePath);
		String currentSend = "";
		int currentTime = 0;
		
		BufferedWriter out = null;
		try {
			FileWriter fw = new FileWriter(output);
			out = new BufferedWriter(fw);
			out.write("GazePointX\tGazePointY\tRecordingTimestamp\tDuration\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while (data.readNextLine() != null) {
			currentSend = currentSend + data.getField(libs.Constants.GazePointX)
					+ libs.Constants.PARAMETER_SPLIT;
			currentSend = currentSend + data.getField(libs.Constants.GazePointY)
					+ libs.Constants.PARAMETER_SPLIT;
			int timestamp = Integer.parseInt(data.getField(libs.Constants.Timestamp));
			currentSend = currentSend + timestamp + libs.Constants.PARAMETER_SPLIT;

			float dis = (Float
					.parseFloat(data.getField(libs.Constants.DistanceLeft)) + Float
					.parseFloat(data.getField(libs.Constants.DistanceRight))) / 2;

			currentSend = currentSend + dis + libs.Constants.PARAMETER_SPLIT;
			
			if (timestamp - currentTime >= timePeriod){
				//System.out.println(currentSend);
				String result = client.execute("ivt", currentSend);
				//System.out.println(result);
				writeFile(out, result);
				currentTime = timestamp;
				currentSend = "";
			}
		}
		
		if (!currentSend.equals("")){
			//System.out.println(currentSend);
			String result = client.execute("ivt", currentSend);
			//System.out.println(result);
			writeFile(out, result);
		}
		
		data.closeFile();
		client.close();
		out.close();
	}

}
