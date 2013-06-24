package fi.eyecloud.storm;

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
	private String onlineFile;
	private String offlineFile;
	
	public AccuracyRate(String online, String offline){
		AFN = 0;
		AFD = 0;
		MF	= 0;
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
		}
		
		AFD = (float)totalDuration / AFN;
	}
	
	public void calMF(){
		ReadTextFile online = new ReadTextFile(onlineFile);
		ReadTextFile offline = new ReadTextFile(offlineFile);
		
		while (online.readNextLine() != null){
			while (offline.readNextLine() != null){
				if (Integer.parseInt(online.getField(Constants.Timestamp)) == 
					Integer.parseInt(offline.getField(Constants.Timestamp)) &&
					Integer.parseInt(online.getField(Constants.Duration)) == 
					Integer.parseInt(offline.getField(Constants.Duration)) ){
					MF++;
					break;
				}
			}
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
	
	public int getMF(){
		return MF;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AccuracyRate test = new AccuracyRate("data/17June_100", "data/17JuneResult.txt");
		System.out.println("AFN: " + test.getAFN());
		System.out.println("AFD: " + test.getAFD());
		System.out.println("MF: " + test.getMF());
	}

}
