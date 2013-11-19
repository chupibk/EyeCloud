package fi.eyecloud.svm;

import java.io.IOException;

import fi.eyecloud.conf.Constants;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		
		// TODO Auto-generated method stub
		// Scale Train
		svm_scale scaleTrain = new svm_scale();
		String argvScaleTrain[] = {"-l", "-1", "-u", "1", "-s", "dataset/svm/eye.range", "dataset/svm/eye.train"};
		scaleTrain.run(argvScaleTrain, "dataset/svm/eye.train.scale");
		System.out.println("Scale Training file: Done");
		
		// Scale Test
		svm_scale scaleTest = new svm_scale();
		String argvScaleTest[] = {"-r", "dataset/svm/eye.range", "dataset/svm/eye.test"};
		scaleTest.run(argvScaleTest, "dataset/svm/eye.test.scale");		
		System.out.println("Scale Testing file: Done");
		
		System.out.println();
		// Grid search for finding c and g
		svm_grid grid = new svm_grid(Constants.N_FOLD_CROSS_VALIDATION, "dataset/svm/eye.train.scale");
		System.out.println("Grid done: " + grid.getC() + " , " + grid.getG() + " , " + grid.getAccuracy());
		System.out.println("Training done");
		System.out.println("Training time:" + (System.currentTimeMillis() - start) + "ms");
		
		System.out.println();
		// Predict
		start = System.currentTimeMillis();
		svm_predict predict = new svm_predict();
		String argvPredict[] = {"dataset/svm/eye.test.scale","", "dataset/svm/eye.predict"};
		predict.run(argvPredict, grid.getModel());
		System.out.println("Predicting done");
		System.out.println("Predicting time:" + (System.currentTimeMillis() - start) + "ms");
	}

}
