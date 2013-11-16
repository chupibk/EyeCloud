package fi.eyecloud.svm;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class RunSVM {

	public static double do_cross_validation(svm_problem prob, svm_parameter param,
			int nr_fold) {
		int i;
		int total_correct = 0;
		double[] target = new double[prob.l];
		double nFoldAccuracy;

		svm.svm_cross_validation(prob, param, nr_fold, target);
		for (i = 0; i < prob.l; i++)
			if (target[i] == prob.y[i])
				++total_correct;
		nFoldAccuracy = 100.0 * total_correct / prob.l;
		
		return nFoldAccuracy;
	}
	
	public static svm_model svmTrain(svm_problem prob, svm_parameter param){
		String error_msg;
		error_msg = svm.svm_check_parameter(prob, param);

		if (error_msg != null) {
			System.err.print("ERROR: " + error_msg + "\n");
			return null;
		}		
		
		return svm.svm_train(prob, param);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
