package fi.eyecloud.storm.fixation;

import java.io.File;

import fi.eyecloud.evaluation.AccuracyRate;

public class Evaluation {

	public Evaluation(String prefix){
		float correctRate = 0;
		float delay = 0;
		File f = new File(prefix);
		File[] list = f.listFiles();
		int count = 0;
		
		for (int i=0; i < list.length;i++){
			String split[] = list[i].getName().split("\\.");
			if (list[i].isFile() && split[split.length - 1].endsWith("txt")){
				count++;
				String id = split[0].split("_")[1];
				AccuracyRate result = new AccuracyRate(prefix + "/" + list[i].getName(), "exp/result/" + id + "r.txt");
				correctRate += (float)result.getMF()/(float)result.getAFN();
				delay += result.getDelay();
			}
		}
		
		System.out.println("Correct Rate: " + correctRate/count);
		System.out.println("Average delay: " + delay/count);
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
