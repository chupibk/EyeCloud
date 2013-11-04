package fi.eyecloud.alone;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import fi.eyecloud.conf.Constants;
import fi.eyecloud.conf.Library;
import fi.eyecloud.input.ReadTextFile;
import fi.eyecloud.lib.FObject;
import fi.eyecloud.lib.FeatureObject;
import fi.eyecloud.lib.SObject;
import fi.eyecloud.lib.SqObject;

public class I_VT_KeyPress {
	private int sumX;
	private int sumY;
	private int count = 0;
	private int startTime;
	private int duration = 0;

	private int currentLine = 0;
	private int numberFixation;
	private String keypress = "0";
	private Map<String, Integer> mapHeader;
	BufferedWriter out, svm;
	
	List<SqObject> sequences = new ArrayList<SqObject>();
	List<FObject> fObjects = new ArrayList<FObject>();
	List<SObject> sObjects = new ArrayList<SObject>();
	
	private SObject sObjectTmp = null;
	
	/**
	 * I-VT algorithm
	 * 
	 * @param filePath: text file
	 * @param VT: velocity threshold
	 */
	public I_VT_KeyPress(String filePath, String output) {
		// Velocity of a gaze
		float preVelocity = 0;
		sObjectTmp = new SObject();
		
		ReadTextFile data = new ReadTextFile(filePath);
		sumX = sumY = count = numberFixation = 0;
		try {
			FileWriter fw = new FileWriter(output);
			out = new BufferedWriter(fw);
			out.write("GazePointX\tGazePointY\tRecordingTimestamp\tDuration\tEventKey\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mapHeader = data.getMapHeader();
		String pre = null;
		String current = null;
		while ((current = data.readNextLine()) != null) {
			// System.out.println(current);

			if (!getField(current.split(Constants.SPLIT_MARK),
					Constants.EventKey).equals("-1")) {
				keypress = getField(current.split(Constants.SPLIT_MARK),
						Constants.EventKey);
			} else {
				if (pre == null)
					pre = current;
				String split[] = pre.split(Constants.SPLIT_MARK);
				int x1 = (Integer.parseInt(getField(split,
						Constants.GazePointXLeft)) + Integer.parseInt(getField(
						split, Constants.GazePointXRight))) / 2;
				int y1 = (Integer.parseInt(getField(split,
						Constants.GazePointYLeft)) + Integer.parseInt(getField(
						split, Constants.GazePointYRight))) / 2;
				int time1 = Integer.parseInt(getField(split,
						Constants.Timestamp));
				float dis1 = (Float.parseFloat(getField(split,
						Constants.DistanceLeft)) + Float.parseFloat(getField(
						split, Constants.DistanceRight))) / 2;

				split = current.split(Constants.SPLIT_MARK);
				int x2 = (Integer.parseInt(getField(split,
						Constants.GazePointXLeft)) + Integer.parseInt(getField(
						split, Constants.GazePointXRight))) / 2;
				int y2 = (Integer.parseInt(getField(split,
						Constants.GazePointYLeft)) + Integer.parseInt(getField(
						split, Constants.GazePointYRight))) / 2;
				int time2 = Integer.parseInt(getField(split,
						Constants.Timestamp));
				float dis2 = (Float.parseFloat(getField(split,
						Constants.DistanceLeft)) + Float.parseFloat(getField(
						split, Constants.DistanceRight))) / 2;
				int line2 = Integer.parseInt(getField(split, Constants.Number));
				// System.out.println(getField(split,
				// Constants.GazePointYLeft));
				if (x1 >= 0 && y1 >= 0 && time1 >= 0 && dis1 >= 0 && x2 >= 0
						&& y2 >= 0 && time2 >= 0 && dis2 >= 0) {
					float tmp = 0;
					if (time2 != time1){
						tmp = Library.VT_Degree(x1, y1, x2, y2, dis1, dis2,
								time2 - time1);
					}
					// System.out.println(tmp);
					// time1 = time2 --> first coordinate
					if (tmp <= Constants.VELOCITY_THRESHOLD || time1 == time2) {
						// System.out.println(line1 + " , " + line2 + " , " +
						// currentLine + " , " + time2 + " , " + duration);
						putXY(x2, y2, time2, line2);
					}else{						
						storeFix(x2, y2, time2, line2);	
						//if (!Float.isNaN(tmp)){
							float d = (float)Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
							float a = (tmp - preVelocity) / (time2 - time1);
							sObjectTmp.addRawData(x1, y1, time1, time2 - time1, d, tmp, a);
						//}						
					}
					
					preVelocity = tmp;
				}

				pre = current;
			}
		}

		storeFix(0, 0, 0, 0);
		
		try {
			out.write(numberFixation + "\n");
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		data.closeFile();
		
		// Sequence Object
		List<FeatureObject> features = new ArrayList<FeatureObject>();
		System.out.println(sequences.size());
		
		FileWriter fw;
		try {
			fw = new FileWriter("classification/result/LauraTest.txt");
			svm = new BufferedWriter(fw);
			for (int i=50; i < sequences.size(); i++){
				FeatureObject f = new FeatureObject(sequences.get(i));
				features.add(f);
				svm.write(f.getIntention() + " ");
				svm.write("1:" + f.getFixationMean() + " ");
				svm.write("2:" + f.getFixationSum() + " ");
				svm.write("3:" + f.getFixationEvent() + " ");
				svm.write("4:" + f.getFixationPrior() + " ");
				svm.write("5:" + f.getSaccadeMean() + " ");
				svm.write("6:" + f.getSaccadeSum() + " ");
				svm.write("7:" + f.getSaccadeLast() + " ");
				svm.write("8:" + f.getSaccadeMeanDistance() + " ");
				svm.write("9:" + f.getSaccadeSumDistance() + " ");
				svm.write("10:" + f.getSaccadeLastDistance() + " ");
				svm.write("11:" + f.getSaccadeVelocityMean() + " ");
				svm.write("12:" + f.getSaccadeLastVelocity() + " ");
				svm.write("13:" + f.getSaccadeAccelerationMean());
				svm.write("\n");
				//System.out.println(f.getIntention() + " , " + f.getFixationMean() + "," + f.getFixationSum() + "," + f.getSaccadeMean() + "," + f.getSaccadeSum());
			}	
			svm.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(sObjects.size());
		for (int i=0; i < 100; i++){
			//sequences.get(i).printSequence();
		}
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
		if (split[mapHeader.get(fieldName)].equals("") || split[mapHeader.get(fieldName)].equals(" ") ||
				split[mapHeader.get(fieldName)].equals("  ")) 
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
				duration = time - startTime;
				//System.out.println(time + " - " + startTime + " - " + duration + " - " + lineId + " - " + currentLine);
		}else {
				storeFix(x, y, time, lineId);
		}		
	}
	
	public void storeFix(int x, int y, int time, int lineId){
		if (count > 0 && duration > Constants.FIXATION_DURATION_THRESHOLD) {
			try {
				out.write((float) sumX / count
						+ Constants.SPLIT_MARK + (float) sumY
						/ count + Constants.SPLIT_MARK + startTime
						+ Constants.SPLIT_MARK + duration
						+ Constants.SPLIT_MARK + keypress
						+ "\n");
				FObject fo = new FObject(sumX/count, sumY/count, startTime, duration, Integer.parseInt(keypress));
				if (sObjectTmp.getRawNumber() >= 0){
					sObjectTmp.calValues();
					sObjects.add(sObjectTmp);
				}						
				fObjects.add(fo);
				
				if (fObjects.size() >= Constants.FIXATION_SEQUENCE_NUMBER){
					SqObject sqo = new SqObject(); 
					for (int i = Constants.FIXATION_SEQUENCE_NUMBER - 1; i >= 0; i--){
						sqo.addFObject(fObjects.get(fObjects.size() - 1 - i));
					}
					
					for (int i = Constants.FIXATION_SEQUENCE_NUMBER - 1; i >= 0; i--){
						sqo.addSObject(sObjects.get(sObjects.size() - 1 - i));
					}
					
					sequences.add(sqo);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			numberFixation++;
			keypress = "0";
			
			if (count > 1)
				sObjectTmp = new SObject();
		}
		
		//System.out.println(count + " - " + startTime + " - " + duration);
		count = 0;
		count++;
		sumX = x;
		sumY = y;
		startTime = time;
		duration = 0;
		currentLine = lineId;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		new I_VT_KeyPress("classification/Laura.txt", "classification/result/LauraResult.txt");
		System.out.println("Running time: " + (float)(System.currentTimeMillis() - start)/1000);
	}
}
