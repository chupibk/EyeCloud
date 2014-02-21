package fi.eyecloud.storm.fixation;

import java.io.File;

import fi.eyecloud.evaluation.AccuracyRate;

public class Evaluation {

	/**
	 * Evaluation of results
	 * 
	 * @param prefix
	 */
	public Evaluation(String prefix){
		int correctRate = 0;
		float delay = 0;
		long maxDelay = 0;
		File f = new File(prefix);
		File[] list = f.listFiles();
		int count = 0;
		
		for (int i=0; i < list.length;i++){
			String split[] = list[i].getName().split("\\.");
			if (list[i].isFile() && split[split.length - 1].endsWith("txt")){
				String id = split[0].split("_")[1];
				AccuracyRate result = new AccuracyRate(prefix + "/" + list[i].getName(), "exp/result/" + id + "r.txt");
				if (result.getAFN() > 0){
					count++;
					correctRate += result.getCorrectRate();
					delay += result.getDelay();
					if (maxDelay < result.getMaxDelay()){
						maxDelay = result.getMaxDelay();
					}
					System.out.println(delay);
				}
			}
		}
		
		System.out.println("Count: " + count);
		System.out.println("Correct Rate: " + (float)correctRate/(float)count);
		System.out.println("Average delay: " + delay/count);
		System.out.println("Maximum delay: " + maxDelay);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Evaluation(args[0]);
		//new Evaluation("test");
	}

}
