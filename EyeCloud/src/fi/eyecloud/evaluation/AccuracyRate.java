package fi.eyecloud.evaluation;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.input.ReadTextFile;

public class AccuracyRate {
	/**
	 * Average Fixation Number
	 */
	private int AFN;
	/**
	 * Average Fixation Duration
	 */
	private float AFD;
	/**
	 * Matching Fixation
	 */
	private int MF;
	private int totalDuration;
	private long delay;
	private String onlineFile;
	private String offlineFile;
	
	public AccuracyRate(String online, String offline){
		System.out.println(online);
		AFN = 0;
		AFD = 0;
		MF	= 0;
		delay = 0;
		totalDuration = 0;
		onlineFile = online;
		offlineFile = offline;
		
		this.calAFN_AFD();
		this.calMF();
	}
	
	public void calAFN_AFD(){
		ReadTextFile online = new ReadTextFile(onlineFile);
		while (online.readNextLine() != null){
			AFN ++;
			totalDuration += Integer.parseInt(online.getField(Constants.Duration));
			delay += Long.parseLong(online.getField(Constants.Delay));
		}
		
		AFD = (float)totalDuration / AFN;
	}
	
	public void calMF(){
		ReadTextFile online = new ReadTextFile(onlineFile);
		ReadTextFile offline = new ReadTextFile(offlineFile);
		
		while (online.readNextLine() != null){
			int check = 0;
			while (offline.readNextLine() != null){
				if (Integer.parseInt(online.getField(Constants.Timestamp)) == 
					Integer.parseInt(offline.getField(Constants.Timestamp)) &&
					Integer.parseInt(online.getField(Constants.Duration)) == 
					Integer.parseInt(offline.getField(Constants.Duration)) ){
					MF++;
					check = 1;
					break;
				}
			}
			if (check == 0)
				System.out.println(online.getField(Constants.Timestamp) + " - " + online.getField(Constants.Duration));
				
			// Reset offline file result
			offline.resetFile();
		}
	}
	
	public int getAFN(){
		return AFN;
	}
	
	public float getAFD(){
		return AFD;
	}
	
	public float getTotalDuration(){
		return totalDuration;
	}	
	
	public int getMF(){
		return MF;
	}
	
	public float getDelay(){
		return (float)delay/(float)AFN;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AccuracyRate test = new AccuracyRate("data/17June_1000_Cloud", "data/17JuneResult.txt");
		System.out.println("FN: " + test.getAFN());
		System.out.println("FD: " + test.getTotalDuration());
		System.out.println("MF: " + test.getMF());
		System.out.println("Delay: " + test.getDelay());
	}

}
