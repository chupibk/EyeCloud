package fi.eyecloud.svm;

import java.io.IOException;

import fi.eyecloud.conf.Constants;

import libsvm.svm_model;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class svm_grid {

	private double c;
	private double g;
	private double accuracy;
	private svm_model model;
	
	public svm_grid(int nFold, String trainFile) throws IOException{
		accuracy = 0;
		svm_problem prob = ReadData.runProb(trainFile);
		svm_parameter bestParam = null;
		
		for (int i=Constants.C_START; i <= Constants.C_END; i=i+Constants.STEP){
			for (int j=Constants.GAMMA_START; j <= Constants.GAMMA_END; j=j+Constants.STEP){				
				double tmpC = Math.pow(2, i);
				double tmpG = Math.pow(2, j);
				svm_parameter param = ReadData.runParameter(tmpC, tmpG);
				double tmpA = RunSVM.do_cross_validation(prob, param, nFold);
				
				if (tmpA > accuracy){
					c = tmpC;
					g = tmpG;
					accuracy = tmpA;
					bestParam = param;
				}
			}
		}
		
		model = RunSVM.svmTrain(prob, bestParam);
	}
	
	public double getC(){
		return c;
	}
	
	public double getG(){
		return g;
	}
	
	public double getAccuracy(){
		return accuracy;
	}
	
	public svm_model getModel(){
		return model;
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();
		
		svm_grid grid = new svm_grid(5, "svm/AjayaCMD.train.scale");
		System.out.println(grid.getC());
		System.out.println(grid.getG());
		System.out.println(grid.getAccuracy());
		
		System.out.println("Running time: " + (System.currentTimeMillis() - start) + "ms");
	}

}
