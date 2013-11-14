package fi.eyecloud.svm;

import java.io.IOException;

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
		String argvScaleTrain[] = {"-l", "-1", "-u", "1", "-s", "svm/AjayaCMD.range", "svm/AjayaCMD.train"};
		scaleTrain.run(argvScaleTrain, "svm/AjayaCMD.train.scale");
		System.out.println("Scale Training file: Done");
		
		// Scale Test
		svm_scale scaleTest = new svm_scale();
		String argvScaleTest[] = {"-r", "svm/AjayaCMD.range", "svm/AjayaCMD.test"};
		scaleTest.run(argvScaleTest, "svm/AjayaCMD.test.scale");		
		System.out.println("Scale Testing file: Done");
		
		// Grid search for finding c and g
		svm_grid grid = new svm_grid(5, "svm/AjayaCMD.train.scale");
		System.out.println("Grid done: " + grid.getC() + " , " + grid.getG() + " , " + grid.getAccuracy());
		
		// Train with c and g
		svm_train train = new svm_train();
		String argvTrain[] = {	"-c", Double.toString(grid.getC()), "-g", Double.toString(grid.getG()), "-q", 
								"svm/AjayaCMD.train.scale", "svm/AjayaCMD.model"};
		train.run(argvTrain, 0);
		System.out.println("Training done");
		
		// Predict
		svm_predict predict = new svm_predict();
		String argvPredict[] = {"svm/AjayaCMD.test.scale","", "svm/AjayaCMD.predict"};
		predict.run(argvPredict, train.getModel());
		System.out.println("Predicting done");
		
		System.out.println();
		System.out.println("Running time:" + (System.currentTimeMillis() - start) + "ms");
	}

}
