package fi.eyecloud.lib;

public class FObject {

	private int gazeX;
	private int gazeY;
	private int startTime;
	private int duration;
	private int keyPress;
	private int rawNumber;
	
	public FObject(){
		rawNumber = 0;
		keyPress = 0;
		duration = 0;
		startTime = 0;
		gazeX = 0;
		gazeY = 0;
	}
	
	public int getGazeX(){
		return gazeX;
	}
	
	public int getGazeY(){
		return gazeY;
	}
	
	public int getStartTime(){
		return startTime;
	}
	
	public int getDuration(){
		return duration;
	}
	
	public int getKeyPress(){
		return keyPress;
	}
	
	public int getRawNumber(){
		return rawNumber;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
