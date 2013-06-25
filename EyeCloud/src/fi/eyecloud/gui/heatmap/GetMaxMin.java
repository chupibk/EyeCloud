package fi.eyecloud.gui.heatmap;

import fi.eyecloud.conf.Constants;

public class GetMaxMin {

	private double min, max;
	
	public GetMaxMin(double[][] intensity, int width, int height){
		min = max = Constants.UNKNOWN;
		for (int i=0; i < width; i++){
			for (int j=0; j < height; j++){
				 if (min == Constants.UNKNOWN || min > intensity[i][j])
					 min = intensity[i][j];
				 if (max == Constants.UNKNOWN || max < intensity[i][j])
					 max = intensity[i][j];				 
			}
		}
	}
	
	public double getMin(){
		return min;
	}
	
	public double getMax(){
		return max;
	}
}
