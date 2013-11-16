package fi.eyecloud.svm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import libsvm.svm;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_print_interface;
import libsvm.svm_problem;

public class ReadData {

	private static double atof(String s) {
		double d = Double.valueOf(s).doubleValue();
		if (Double.isNaN(d) || Double.isInfinite(d)) {
			System.err.print("NaN or Infinity in input\n");
			System.exit(1);
		}
		return (d);
	}

	private static int atoi(String s) {
		return Integer.parseInt(s);
	}	
	
	public static svm_problem runProb(String inputFile) throws IOException {
		svm_problem prob;
		BufferedReader fp = new BufferedReader(new FileReader(inputFile));
		Vector<Double> vy = new Vector<Double>();
		Vector<svm_node[]> vx = new Vector<svm_node[]>();
		int max_index = 0;

		while (true) {
			String line = fp.readLine();
			if (line == null)
				break;

			StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");

			vy.addElement(atof(st.nextToken()));
			int m = st.countTokens() / 2;
			svm_node[] x = new svm_node[m];
			for (int j = 0; j < m; j++) {
				x[j] = new svm_node();
				x[j].index = atoi(st.nextToken());
				x[j].value = atof(st.nextToken());
			}
			if (m > 0)
				max_index = Math.max(max_index, x[m - 1].index);
			vx.addElement(x);
		}

		prob = new svm_problem();
		prob.l = vy.size();
		prob.x = new svm_node[prob.l][];
		for (int i = 0; i < prob.l; i++)
			prob.x[i] = vx.elementAt(i);
		prob.y = new double[prob.l];
		for (int i = 0; i < prob.l; i++)
			prob.y[i] = vy.elementAt(i);

		fp.close();
		
		return prob;
	}

	private static svm_print_interface svm_print_null = new svm_print_interface() {
		public void print(String s) {
		}
	};	
	
	public static svm_parameter runParameter(double c, double g){
		svm_parameter param; 
		svm.svm_set_print_string_function(svm_print_null); // default printing to stdout

		param = new svm_parameter();
		// default values
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		param.gamma = g;
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 100;
		param.C = c;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];
		
		return param;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
