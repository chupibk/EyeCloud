package fi.eyecloud.lib;

import java.util.ArrayList;
import java.util.List;

public class SObject {

	private int duration;
	private float distance;
	private float velocity;
	private float acceleration;
	
	private List<Integer> listX, listY, listTime, listDur;
	private List<Float> listVel, listDis, listAcc;
	
	public SObject(){
		duration = 0;
		distance = 0;
		velocity = 0;
		acceleration = 0;
		
		listX = new ArrayList<Integer>();
		listY = new ArrayList<Integer>();
		listTime = new ArrayList<Integer>();
		listDur = new ArrayList<Integer>();		
		
		listVel = new ArrayList<Float>();
		listDis = new ArrayList<Float>();
		listAcc = new ArrayList<Float>();
	}
	
	public void addRawData(int x, int y, int t, int dur, float dis, float v, float a){
		// Raw data
		listX.add(x);
		listY.add(y);
		listTime.add(t);
		listDur.add(dur);
		
		// Features
		listDis.add(dis);
		listVel.add(v);
		listAcc.add(a);
	}
	
	public void printList(){
		for (int i=0; i < listX.size(); i++){
			System.out.println(listTime.get(i) + "\t" + listX.get(i) + "\t" + listY.get(i) + "\t" + listVel.get(i));
		}
	}
	
	public void calValues() {
		if (listX.size() > 0) {
			for (int i = 0; i < listX.size(); i++) {
				duration += listDur.get(i);
				distance += listDis.get(i);
				velocity += listVel.get(i);
				acceleration += listAcc.get(i);
			}

			velocity = velocity / listX.size();
			acceleration = acceleration / listX.size();
		}
	}
	
	public int getDuration(){
		return duration;
	}
	
	public int getRawNumber(){
		return listX.size();
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
	
	public int getStartTime(){
		if (listTime.size() > 0)
			return listTime.get(0);
		return 0;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
