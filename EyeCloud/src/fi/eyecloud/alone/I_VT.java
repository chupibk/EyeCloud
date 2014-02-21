package fi.eyecloud.alone;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import fi.eyecloud.conf.Constants;
import fi.eyecloud.conf.Library;
import fi.eyecloud.input.ReadTextFile;

public class I_VT {
	private int sumX;
	private int sumY;
	private int count = 0;
	private int startTime;
	private int duration = 0;

	private int currentLine = 0;
	private int numberFixation;
	private Map<String, Integer> mapHeader;
	BufferedWriter out;
	
	/**
	 * Standard alone of I-VT algorithm used for testing results
	 * 
	 * @param filePath: input text file
	 * @param output: output text file
	 */
	public I_VT(String filePath, String output) {
		ReadTextFile data = new ReadTextFile(filePath);
		sumX = sumY = count = numberFixation = 0;
		try {
			FileWriter fw = new FileWriter(output);
			out = new BufferedWriter(fw);
			out.write("GazePointX\tGazePointY\tRecordingTimestamp\tDuration\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mapHeader = data.getMapHeader();
		String pre = null;
		String current = null;
		while ((current = data.readNextLine()) != null) {
			//System.out.println(current);
			if (pre == null) pre = current;
			String split[] = pre.split(Constants.SPLIT_MARK);
			int x1 = (Integer.parseInt(getField(split, Constants.GazePointXLeft)) +
					Integer.parseInt(getField(split, Constants.GazePointXRight)))/2;
			int y1 = (Integer.parseInt(getField(split, Constants.GazePointYLeft)) +
					Integer.parseInt(getField(split, Constants.GazePointYRight)))/2;
			int time1 = Integer.parseInt(getField(split, Constants.Timestamp));			
			float dis1 = (Float.parseFloat(getField(split, Constants.DistanceLeft)) + 
						 Float.parseFloat(getField(split, Constants.DistanceRight))) / 2;
			int line1 = Integer.parseInt(getField(split, Constants.Number));
			
			split = current.split(Constants.SPLIT_MARK);
			int x2 = (Integer.parseInt(getField(split, Constants.GazePointXLeft)) +
					Integer.parseInt(getField(split, Constants.GazePointXRight)))/2;
			int y2 = (Integer.parseInt(getField(split, Constants.GazePointYLeft)) +
					Integer.parseInt(getField(split, Constants.GazePointYRight)))/2;
			int time2 = Integer.parseInt(getField(split, Constants.Timestamp));
			float dis2 = (Float.parseFloat(getField(split, Constants.DistanceLeft)) + 
						 Float.parseFloat(getField(split, Constants.DistanceRight))) / 2;
			int line2 = Integer.parseInt(getField(split, Constants.Number));
			//System.out.println(getField(split, Constants.GazePointYLeft));
			if (x1 >= 0 && y1 >= 0 && time1 >= 0 && dis1 >= 0 && x2 >= 0 && y2 >= 0 && time2 >= 0 && dis2 >= 0){
				float tmp = Library.VT_Degree(x1, y1, x2, y2, dis1, dis2,
						time2 - time1);
				//System.out.println(tmp);
				// time1 = time2 --> first coordinate
				if ( tmp <= Constants.VELOCITY_THRESHOLD || time1 == time2) {
					System.out.println(line1 + " , " + line2 + " , " + currentLine + " , " + time2 + " , " + duration);
					putXY(x2, y2, time2, line2);
				}
			}
			
			pre = current;
		}

		if (count > 0 && duration > Constants.FIXATION_DURATION_THRESHOLD) {
			try {
				out.write((float) sumX / count
						+ Constants.SPLIT_MARK + (float) sumY
						/ count + Constants.SPLIT_MARK + startTime
						+ Constants.SPLIT_MARK + duration
						+ "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			numberFixation++;
		}
		
		try {
			out.write(numberFixation + "\n");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		data.closeFile();
	}

	/**
	 * Get information of a field
	 * 
	 * @param fieldName
	 * @return
	 */
	public String getField(String[] split, String fieldName){
		if (mapHeader.get(fieldName) == -1 || mapHeader.get(fieldName) >= split.length) 
			return Integer.toString(Constants.UNKNOWN);
		if (split[mapHeader.get(fieldName)].equals("") || split[mapHeader.get(fieldName)].equals(" ")) 
			return Integer.toString(Constants.UNKNOWN);
		
		return split[mapHeader.get(fieldName)];
	} 	
	
	/**
	 * Process each x,y coordinate
	 * 
	 * @param x
	 * @param y
	 * @param time
	 * @param dis
	 */
	public void putXY(int x, int y, int time, int lineId) {
		if (lineId - currentLine == 1) {
				count++;
				sumX += x;
				sumY += y;

				currentLine = lineId;	
				duration = time - startTime + Constants.THOUSAND / Constants.SAMPLE_RATE;
				//System.out.println(time + " - " + startTime + " - " + duration + " - " + lineId + " - " + currentLine);
		}else {
			if (count > 0 && duration > Constants.FIXATION_DURATION_THRESHOLD) {
				try {
					out.write((float) sumX / count
							+ Constants.SPLIT_MARK + (float) sumY
							/ count + Constants.SPLIT_MARK + startTime
							+ Constants.SPLIT_MARK + duration
							+ "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				numberFixation++;
			}
			//System.out.println(count + " - " + startTime + " - " + duration);
			count = 0;
			count++;
			sumX = x;
			sumY = y;
			startTime = time;
			duration = Constants.THOUSAND / Constants.SAMPLE_RATE;
			currentLine = lineId;
		}		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		new I_VT("exp/10.txt", "exp/result/10r.txt");
		//new I_VT("/home/hadoop/eye/01-01-All-Data.txt", "result.txt");
		System.out.println("Running time: " + (float)(System.currentTimeMillis() - start)/1000);
	}
}
