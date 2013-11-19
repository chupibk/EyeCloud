package fi.eyecloud.svm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FScore {

	public FScore(String testFile, String predictFile) throws IOException{
		FileReader frTest = new FileReader(testFile);
		BufferedReader test = new BufferedReader(frTest);
		
		FileReader frPredict = new FileReader(predictFile);
		BufferedReader predict = new BufferedReader(frPredict);
		
		String t, p;
		int TP = 0;
		int FP = 0;
		int FN = 0;
		while ((t = test.readLine()) != null && (p = predict.readLine()) != null){
			int tt = (int)Float.parseFloat(t.split(" ")[0]);
			int pp = (int)Float.parseFloat(p.split(" ")[0]);
			if (tt == pp && tt == 1){
				TP++;
			}
			
			if (tt != pp && tt == 0 && pp == 1){
				FP++;
			}
			
			if (tt != pp && tt == 1 && pp == 0){
				FN++;
			}			
		}
		
		predict.close();
		frPredict.close();
		test.close();
		frTest.close();
		
		System.out.println("FScore: " + (float) 2*TP/(2*TP + FP + FN));
		System.out.println("Recall: " + (float) TP/(TP+FN));
		System.out.println("Precision: " + (float) TP/(TP+FP));
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new FScore("dataset/svm/eye.test.scale", "dataset/svm/eye.predict");
	}

}
