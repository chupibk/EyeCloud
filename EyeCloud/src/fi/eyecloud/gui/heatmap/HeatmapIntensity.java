package fi.eyecloud.gui.heatmap;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.gui.lib.GuiConstants;
import fi.eyecloud.gui.lib.Intensity;
import fi.eyecloud.input.ReadTextFile;

public class HeatmapIntensity implements Intensity{
	private ReadTextFile dataFile;
	private double intensity[][];
	private double gaussianWindow[][];
	private int width;
	private int height;
	
	/**
	 * Generate heatmap intensity
	 * 
	 * @param input
	 * @param option: 0 from fixation and 1 from raw data 
	 */
	@Override
	public void run(String input, int option, int w, int h){
		dataFile = new ReadTextFile(input);
		width = w;
		height = h;
		intensity = new double[width][height];
		gaussianWindow = new double[GuiConstants.KERNEL_SIZE_USED + 1][GuiConstants.KERNEL_SIZE_USED + 1];
		
		for (int i=0; i < width; i++){
			for (int j=0; j < height; j++){
				intensity[i][j] = 0;
			}
		}
		
		for (int i=0; i <= GuiConstants.KERNEL_SIZE_USED; i++){
			for (int j=0; j <= GuiConstants.KERNEL_SIZE_USED; j++){
				gaussianWindow[i][j] = gaussian2D(i, j);
			}
		}
		
		if (option == 0){
			fromFixation();
		}
	}
	
	public double gaussian2D(int x, int y){
		double value;
		value = Math.exp(-(x*x + y*y)/(2*GuiConstants.SIGMA_USED*GuiConstants.SIGMA_USED));		
		return value;
	}
	
	/**
	 * Calculate intensity from Fixation
	 */
	@Override
	public void fromFixation(){
		while (dataFile.readNextLine() != null){
			int x = (int)Float.parseFloat(dataFile.getField(Constants.GazePointX))/GuiConstants.SCREEN_RATE;
			int y = (int)Float.parseFloat(dataFile.getField(Constants.GazePointY))/GuiConstants.SCREEN_RATE;
			int d = Integer.parseInt(dataFile.getField(Constants.Duration));
			
			for (int i=x - GuiConstants.KERNEL_SIZE_USED; i <= x + GuiConstants.KERNEL_SIZE_USED; i++){
				for (int j=y - GuiConstants.KERNEL_SIZE_USED; j <= y + GuiConstants.KERNEL_SIZE_USED; j++){
					if (i >=0 && j >=0 && i < width && j < height)
						intensity[i][j] += gaussianWindow[Math.abs(x-i)][Math.abs(y-j)]*d;
				}
			}
		}
	}
	
	@Override
	public double[][] getIntensity(){
		return intensity;
	}
}
