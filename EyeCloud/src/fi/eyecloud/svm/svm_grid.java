package fi.eyecloud.svm;

import java.io.IOException;

public class svm_grid {

	private double c;
	private double g;
	private double accuracy;
	
	public svm_grid(int nFold, String trainFile) throws IOException{
		accuracy = 0;
		
		for (int i=-5; i <= 15; i=i+2){
			for (int j=3; j >= -15; j=j-2){				
				svm_train train = new svm_train();
				double tmpC = Math.pow(2, i);
				double tmpG = Math.pow(2, j);
				String argv[] = {"-c", Double.toString(tmpC), "-g", Double.toString(tmpG), "-v", Integer.toString(nFold), "-q",trainFile};
				train.run(argv, 0);
				
				if (train.getNFoldAccuracy() > accuracy){
					c = tmpC;
					g = tmpG;
					accuracy = train.getNFoldAccuracy();
					//System.out.println(c + " , " + g + " , " + accuracy);
				}
			}
		}
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
