package fi.eyecloud.lib;

public class FeatureObject {
	
	/**
	 * 1. The average time of fixation duration
	 */
	private float fixMean;
	
	/**
	 * 2. Sum of fixation duration
	 */
	private int fixSum;
	
	/**
	 * 3. Fixation duration of ongoing event
	 */
	private int fixEvent;
	
	/**
	 * 4. Fixation duration of prior event
	 */
	private int fixPrior;
	
	/**
	 * 5. The average time of saccade duration
	 */
	private float sacMean;
	
	/**
	 * 6. Sum of saccade duration
	 */
	private int sacSum;
	
	/**
	 * 7. Duration of saccade before the event
	 */
	private int sacLast;
	
	/**
	 * 8. The average distance of saccade
	 */
	private float sacMeanDistance;
	
	/**
	 * 9. Sum of saccade distance
	 */
	private float sacSumDistance;
	
	/**
	 * 10. Distance of saccade before the event
	 */
	private float sacLastDistance;
	
	/**
	 * 11. The average speed of saccades
	 */
	private float sacVelocityMean;
	
	/**
	 * 12. Speed of the saccade before the event
	 */
	private float sacLastVelocity;
	
	/**
	 * 13. Acceleration of saccades
	 */
	private float sacAccelerationMean;
	
	public FeatureObject(SqObject o){
		fixSum = 0;
		fixEvent = 0;
		fixPrior = 0;
		
		sacSum = 0;
		sacLast = 0;
		sacSumDistance = 0;
		sacLastDistance = 0;
		sacLastVelocity = 0;
		calFeature(o);
	}
	
	private void calFeature(SqObject object){
		int check = 0;
		float sumVelocity = 0;
		float sumAcceleration = 0;
		
		for (int i=0; i < object.getFNumber(); i++){
			FObject f = object.getFObject(i);
			SObject s = null;
			
			fixSum += f.getDuration();
			if (i < object.getFNumber() - 1){
				s = object.getSObject(i);
				sacSum += s.getDuration();
				sacSumDistance += s.getDistance();
				sumVelocity += s.getVelocity();
				sumAcceleration += s.getAcceleration();
			}			
			
			if (f.getKeyPress() > 0){
				fixEvent += f.getDuration();
				check = 1;	
			}
			
			if (check == 0){
				fixPrior += f.getDuration();
				if (s != null){
					sacLast  += s.getDuration();
					sacLastDistance += s.getDistance();
					sacLastVelocity += s.getVelocity();
				}
			}			
		}
				
		fixMean = (float)fixSum / (float) object.getFNumber();
		sacMean = (float)sacSum / (float) object.getSNumber();
		sacMeanDistance = sacSumDistance / (float) object.getSNumber();
		sacVelocityMean = sumVelocity / (float) object.getSNumber();
		sacAccelerationMean = sumAcceleration / (float) object.getSNumber();
	}
	
	public float getFixationMean(){
		return fixMean;
	}
	
	public int getFixationSum(){
		return fixSum;
	}
	
	public int getFixationEvent(){
		return fixEvent;
	}
	
	public int getFixationPrior(){
		return fixPrior;
	}
	
	public float getSaccadeSum(){
		return sacSum;
	}	
	
	public float getSaccadeMean(){
		return sacMean;
	}
	
	public int getSaccadeLast(){
		return sacLast;
	}
	
	public float getSaccadeMeanDistance(){
		return sacMeanDistance;
	}
	
	public float getSaccadeSumDistance(){
		return sacSumDistance;
	}
	
	public float getSaccadeLastDistance(){
		return sacLastDistance;
	}
	
	public float getSacVelocityMean(){
		return sacVelocityMean;
	}
	
	public float getSacLastVelocity(){
		return sacLastVelocity;
	}
	
	public float getSacAccelerationMean(){
		return sacAccelerationMean;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
