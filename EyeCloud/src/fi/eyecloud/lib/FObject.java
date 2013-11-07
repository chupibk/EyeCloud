package fi.eyecloud.lib;

public class FObject {

	private int gazeX;
	private int gazeY;
	private int startTime;
	private int duration;
	private int keyPress;
	
	int rawNumber;
	
	public FObject(int x, int y, int t, int d, int k){
		rawNumber = 0;
		keyPress = k;
		duration = d;
		startTime = t;
		gazeX = x;
		gazeY = y;
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
