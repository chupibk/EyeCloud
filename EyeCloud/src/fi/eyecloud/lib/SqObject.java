package fi.eyecloud.lib;

import java.util.ArrayList;
import java.util.List;

public class SqObject {

	int intention;
	List<FObject> fObjects;
	List<SObject> sObjects;
	
	/**
	 * Sequence object
	 * 
	 */
	public SqObject(){
		fObjects = new ArrayList<FObject>();
		sObjects = new ArrayList<SObject>();
		intention = 0;
	}
	
	public void printSequence(){
		for (int i=0; i < fObjects.size(); i++){
			//System.out.println(sObjects.get(i).getStartTime() + " , " + sObjects.get(i).getDuration() + " , " + sObjects.get(i).getRawNumber() + " , " + sObjects.get(i).getVelocity());
			//System.out.println(fObjects.get(i).getStartTime() + " , " + fObjects.get(i).getDuration());
		}
		System.out.println();
	}
	
	public void addFObject(FObject o){
		fObjects.add(o);
	}
	
	public void addSObject(SObject o){
		sObjects.add(o);
	}	
	
	public void setIntention(){
		intention = 1;
	}
	
	public int getFNumber(){
		return fObjects.size();
	}
	
	public int getSNumber(){
		return sObjects.size();
	}	
	
	public int checkIntention(){
		return intention;
	}
	
	public FObject getFObject(int i){
		return fObjects.get(i);
	}
	
	public SObject getSObject(int i){
		return sObjects.get(i);
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
