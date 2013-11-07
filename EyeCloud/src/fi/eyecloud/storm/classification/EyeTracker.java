package fi.eyecloud.storm.classification;

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
	
	public EyeTracker(String hostName, int dataPath, String prefix, int segment, int id) throws TException, DRPCExecutionException, InterruptedException, IOException{
		DRPCClient client = new DRPCClient(hostName, 3772);
		ReadTextFile data = new ReadTextFile("classification/LauraTest.txt");
		
		// Output file
		BufferedWriter out = null;
		File folder = new File(prefix);
		if (!folder.exists()){
			folder.mkdir();
		}
		FileWriter fw = new FileWriter(prefix + "/" + id + "_" + dataPath + "_" + segment + ".txt");
		out = new BufferedWriter(fw);
		out.write("GazePointX\tGazePointY\tRecordingTimestamp\tDuration\tDelay\n");
		
		String currentSend = "";
		int currentTime = 0;
		long tmpTime = System.currentTimeMillis();
			
		while (data.readNextLine() != null) {
			int x = (Integer.parseInt(data.getField(Constants.GazePointXLeft)) +
					Integer.parseInt(data.getField(Constants.GazePointXRight)))/2;
			int y = (Integer.parseInt(data.getField(Constants.GazePointYLeft)) +
					Integer.parseInt(data.getField(Constants.GazePointYRight)))/2;			
			currentSend = currentSend + x
					+ Constants.PARAMETER_SPLIT;
			currentSend = currentSend + y
					+ Constants.PARAMETER_SPLIT;
			int timestamp = Integer.parseInt(data.getField(Constants.Timestamp));
			currentSend = currentSend + timestamp + Constants.PARAMETER_SPLIT;

			float dis = (Float
					.parseFloat(data.getField(Constants.DistanceLeft)) + Float
					.parseFloat(data.getField(Constants.DistanceRight))) / 2;

			currentSend = currentSend + dis + Constants.PARAMETER_SPLIT;
			currentSend = currentSend + System.currentTimeMillis() + Constants.PARAMETER_SPLIT;
			currentSend = currentSend + Integer.parseInt(data.getField(Constants.Number)) 
										+ Constants.PARAMETER_SPLIT;
			
			int keypress = 0;
			if (Integer.parseInt(data.getField(Constants.EventKey)) != Constants.UNKNOWN){
				keypress = Integer.parseInt(data.getField(Constants.EventKey));
			}
			currentSend = currentSend + keypress + Constants.PARAMETER_SPLIT;
			
			if (timestamp - currentTime >= segment){
				currentSend = currentSend + id;
				System.out.println(currentSend);
				String result = client.execute("Classification", currentSend);
				System.out.println(result);
				//writeFile(out, result, System.currentTimeMillis());
				
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
			String result = client.execute("Classification", currentSend);
			writeFile(out, result, System.currentTimeMillis());
		}
		
		// Finish sending by send empty string
		String result = client.execute("Classification", Integer.toString(id));
		writeFile(out, result, System.currentTimeMillis());	
		
		// Close all
		data.closeFile();
		client.close();
		out.close();
	}
	
	public static void writeFile(BufferedWriter o, String data, long time){
		String split[] = data.split(Constants.PARAMETER_SPLIT); 
		for (int i=0; i < split.length/5; i++){
			long delay = time - Long.parseLong(split[i*5 + 4]) - Long.parseLong(split[i*5 + 3]);
			try {
				o.write(split[i*5]
						+ Constants.SPLIT_MARK + split[i*5 + 1]
						+ Constants.SPLIT_MARK + split[i*5 + 2]
						+ Constants.SPLIT_MARK + split[i*5 + 3]
						+ Constants.SPLIT_MARK + delay
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
		new EyeTracker("54.194.20.206", 2, "test", 20, 200);
	}
}
