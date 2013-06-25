package fi.eyecloud.gui.heatmap;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.input.ReadTextFile;

public class HeatmapIntensity {
	private ReadTextFile dataFile;
	private double intensity[][];
	private int width;
	private int height;
	
	/**
	 * Generate heatmap intensity
	 * 
	 * @param input
	 * @param option: 0 from fixation and 1 from raw data 
	 */
	public HeatmapIntensity(String input, int option, int w, int h){
		dataFile = new ReadTextFile(input);
		width = w;
		height = h;
		intensity = new double[width][height];
		
		for (int i=0; i < width; i++){
			for (int j=0; j < height; j++){
				intensity[i][j] = 0;
			}
		}
		
		if (option == 0){
			fromFixation();
		}
	}
	
	public double gaussian(int x, int y, int i, int j, int duration){
		double value;
		
		value = Math.exp(-((x-i)*(x-i) + (y-j)*(y-j))/(2*Constants.SIGMA*Constants.SIGMA));
		// Linear with duration
		value = value*duration;
		
		return value;
	}
	
	/**
	 * Calculate intensity from Fixation
	 */
	public void fromFixation(){
		while (dataFile.readNextLine() != null){
			int x = (int)Float.parseFloat(dataFile.getField(Constants.GazePointX));
			int y = (int)Float.parseFloat(dataFile.getField(Constants.GazePointY));
			int d = Integer.parseInt(dataFile.getField(Constants.Duration));
			
			for (int i=0; i < width; i++){
				for (int j=0; j < height; j++){
					intensity[i][j] += gaussian(x, y, i, j, d);
				}
			}
		}
	}
	
	public double[][] getIntensity(){
		return intensity;
	}
}
