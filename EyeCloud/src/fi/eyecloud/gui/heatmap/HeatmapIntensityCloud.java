package fi.eyecloud.gui.heatmap;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.gui.lib.Intensity;

public class HeatmapIntensityCloud implements Intensity{

	private double intensity[][];
	
	@Override
	public void run(String input, int option, int w, int h) {
		// TODO Auto-generated method stub
		intensity = new double[w][h];
		
		String data[] = input.split(Constants.PARAMETER_SPLIT);
		for (int i=0; i < data.length; i++){
			intensity[i/h][i%h] = Double.parseDouble(data[i]);
		}
	}

	@Override
	public void fromFixation() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void fromGazePoints(int duration) {
		// TODO Auto-generated method stub
		
	}	

	@Override
	public double[][] getIntensity() {
		// TODO Auto-generated method stub
		return intensity;
	}

}
