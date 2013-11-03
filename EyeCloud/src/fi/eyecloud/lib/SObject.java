package fi.eyecloud.lib;

import java.util.ArrayList;
import java.util.List;

public class SObject {

	private int duration;
	private int rawNumber;
	private float distance;
	private float velocity;
	private float acceleration;
	
	private List<Integer> listX, listY, listTime;
	private List<Float> listV;
	
	public SObject(){
		duration = 0;
		rawNumber = 0;
		distance = 0;
		velocity = 0;
		acceleration = 0;
		
		listX = new ArrayList<Integer>();
		listY = new ArrayList<Integer>();
		listTime = new ArrayList<Integer>();
		
		listV = new ArrayList<Float>();
	}
	
	public void addRawData(int x, int y, int t, float v){
		listX.add(x);
		listY.add(y);
		listTime.add(t);
		listV.add(v);
	}
	
	public void printList(){
		for (int i=0; i < listX.size(); i++){
			System.out.println(listTime.get(i) + "\t" + listX.get(i) + "\t" + listY.get(i) + "\t" + listV.get(i));
		}
	}
	
	public void calValues(){
		if (listX.size() > 0) {
			duration = listTime.get(listX.size() - 1) - listTime.get(0);
			velocity += listV.get(0);
			
			for (int i = 1; i < listX.size(); i++) {
				distance += Math.sqrt((listX.get(i) - listX.get(i - 1))
						* (listX.get(i) - listX.get(i - 1))
						+ (listY.get(i) - listY.get(i - 1))
						* (listY.get(i) - listY.get(i - 1)));
				velocity += listV.get(i);
				acceleration += (listV.get(i) - listV.get(i-1))/(listTime.get(i) - listTime.get(i-1));
			}
			
			velocity = velocity/listX.size();
			acceleration = acceleration/(listX.size() - 1);
		}
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
