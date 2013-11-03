package fi.eyecloud.lib;

public class SObject {

	private int duration;
	private int rawNumber;
	private float distance;
	private float velocity;
	private float acceleration;
	
	public SObject(){
		duration = 0;
		rawNumber = 0;
		distance = 0;
		velocity = 0;
	}
	
	public int getDuration(){
		return duration;
	}
	
	public int getRawNumber(){
		return rawNumber;
	}
	
	public float getDistance(){
		return distance;
	}
	
	public float getVelocity(){
		return velocity;
	}
	
	public float getAcceleration(){
		return acceleration;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
