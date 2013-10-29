package fi.eyecloud.storm.fixation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.thrift7.TException;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.input.ReadTextFile;
import backtype.storm.generated.DRPCExecutionException;
import backtype.storm.utils.DRPCClient;

public class EyeTracker {
	
	public EyeTracker(String hostName, String dataPath, String prefix, int segment, int id) throws TException, DRPCExecutionException, InterruptedException, IOException{
		DRPCClient client = new DRPCClient(hostName, 3772);
		ReadTextFile data = new ReadTextFile(dataPath);
		
		// Output file
		BufferedWriter out = null;
		File folder = new File(prefix);
		if (!folder.exists()){
			folder.mkdir();
		}
		FileWriter fw = new FileWriter(prefix + "/" + id + "_" + segment);
		out = new BufferedWriter(fw);
		out.write("GazePointX\tGazePointY\tRecordingTimestamp\tDuration\tDelay\n");
		
		String currentSend = "";
		int currentTime = 0;
		long tmpTime = System.currentTimeMillis();
			
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
			
			if (timestamp - currentTime >= segment){
				currentSend = currentSend + id;
				String result = client.execute("CloudFixation", currentSend);
				writeFile(out, result);
				
				long dif = System.currentTimeMillis() - tmpTime;
				if (dif < segment)
					Thread.sleep(segment - dif);
				tmpTime = System.currentTimeMillis();
				
				currentTime = timestamp;
				currentSend = "";
			}
		}	
		
		if (!currentSend.equals("")){
			currentSend = currentSend + id;
			String result = client.execute("CloudFixation", currentSend);
			writeFile(out, result);
		}
		
		// Finish sending by send empty string
		String result = client.execute("CloudFixation", Integer.toString(id));
		writeFile(out, result);	
		
		// Close all
		data.closeFile();
		client.close();
		out.close();
	}
	
	public static void writeFile(BufferedWriter o, String data){
		String split[] = data.split(Constants.PARAMETER_SPLIT);
		for (int i=0; i < split.length/5; i++){
			try {
				o.write(split[i*5]
						+ Constants.SPLIT_MARK + split[i*5 + 1]
						+ Constants.SPLIT_MARK + split[i*5 + 2]
						+ Constants.SPLIT_MARK + split[i*5 + 3]
						+ Constants.SPLIT_MARK + split[i*5 + 4]
						+ "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @throws DRPCExecutionException 
	 * @throws TException 
	 */
	public static void main(String[] args) throws TException, DRPCExecutionException, InterruptedException, IOException {
		// TODO Auto-generated method stub
		new EyeTracker("54.229.164.16", "data/17June.txt", "test", 1000, 10);
	}
}
