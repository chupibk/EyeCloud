package fi.eyecloud.storm.fixation;

import fi.eyecloud.evaluation.AccuracyRate;

public class Evaluation {

	public Evaluation(String prefix, int s, int min, int max){
		float correctRate = 0;
		float delay = 0;
		
		for (int i=min; i < max; i++){
			AccuracyRate result = new AccuracyRate(prefix + "/" + i + "_" + s, "eyecloud/17JuneResult.txt");
			correctRate += (float)result.getMF()/(float)result.getAFN();
			delay += result.getDelay();
		}
		
		System.out.println("Correct Rate: " + correctRate/(max-min));
		System.out.println("Average delay: " + delay/(max-min));
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Evaluation(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		//new Evaluation(40, 1, 50);
	}

}
