package fi.eyecloud.evaluation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.input.ReadTextFile;

public class ClientMerge {
	
	public ClientMerge(String onlineFile){
		ReadTextFile online = new ReadTextFile(onlineFile);
		
		BufferedWriter out = null;
		try {
			FileWriter fw = new FileWriter(onlineFile + "_Merge");
			out = new BufferedWriter(fw);
			out.write("GazePointX\tGazePointY\tRecordingTimestamp\tDuration\n");
			
			float currentX = 0;
			float currentY = 0;
			int count = 0;
			int startTime = 0;
			int duration = 0;
			int mergeNumber = 0;
			
			while (online.readNextLine() != null){
				if (count == 0){
					count++;
					currentX = Float.parseFloat(online.getField(Constants.GazePointX));
					currentY = Float.parseFloat(online.getField(Constants.GazePointY));
					startTime= Integer.parseInt(online.getField(Constants.Timestamp));
					duration = Integer.parseInt(online.getField(Constants.Duration));
				}else{
					if (startTime + duration == 
							Integer.parseInt(online.getField(Constants.Timestamp))){
						count++;
						currentX += Float.parseFloat(online.getField(Constants.GazePointX));
						currentY += Float.parseFloat(online.getField(Constants.GazePointY));
						duration += Integer.parseInt(online.getField(Constants.Duration));
						mergeNumber ++;
					}else{
						out.write((float) currentX / count
								+ Constants.SPLIT_MARK + (float) currentY
								/ count + Constants.SPLIT_MARK + startTime
								+ Constants.SPLIT_MARK + duration
								+ "\n");
						
						count = 0;
						count++;
						currentX = Float.parseFloat(online.getField(Constants.GazePointX));
						currentY = Float.parseFloat(online.getField(Constants.GazePointY));
						startTime= Integer.parseInt(online.getField(Constants.Timestamp));
						duration = Integer.parseInt(online.getField(Constants.Duration));						
					}
				}
			}
			
			out.close();
			System.out.println("Merge Time: " + mergeNumber);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ClientMerge("data/17June_2000");
	}

}
