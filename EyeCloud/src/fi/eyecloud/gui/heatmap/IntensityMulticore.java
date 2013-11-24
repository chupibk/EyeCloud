package fi.eyecloud.gui.heatmap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.gui.lib.GuiConstants;

public class IntensityMulticore implements Callable<double[][]> {
	private Map<String, Integer> mapHeader;
	private List<String> childData;
	private double gaussian[][];
	private int w;
	private int h;
	private int d;

	public IntensityMulticore(Map<String, Integer> m, List<String> c, int w, int h, double[][] g, int d) {
		this.mapHeader = m;
		this.childData = c;
		this.w = w;
		this.h = h;
		this.gaussian = g;
		this.d = d;
	}

	@Override
	public double[][] call() throws Exception {
		return childIntensive(childData, w, h, gaussian, d);
	}

	/**
	 * Get information of a field
	 * 
	 * @param fieldName
	 * @return
	 */
	public String getField(String str, String fieldName){
		String split[] = str.split(Constants.SPLIT_MARK);
		if (mapHeader.get(fieldName) == -1 || mapHeader.get(fieldName) >= split.length) 
			return Integer.toString(Constants.UNKNOWN);
		if (split[mapHeader.get(fieldName)].equals("") || split[mapHeader.get(fieldName)].equals(" ")) 
			return Integer.toString(Constants.UNKNOWN);
		
		return split[mapHeader.get(fieldName)];
	} 	
	
	public double[][] childIntensive(List<String> childData, int w, int h, double gaussian[][], int d){
		double[][] childIntensity = new double[w][h];
		for (int i=0; i < w; i++){
			for (int j=0; j < h; j++){
				childIntensity[i][j] = 0;
			}
		}		
		
		for(int c=0; c < childData.size(); c++){
			int x = (int)Float.parseFloat(getField(childData.get(c), Constants.GazePointX))/GuiConstants.SCREEN_RATE;
			int y = (int)Float.parseFloat(getField(childData.get(c), Constants.GazePointY))/GuiConstants.SCREEN_RATE;
			
			for (int i=x - GuiConstants.KERNEL_SIZE_USED; i <= x + GuiConstants.KERNEL_SIZE_USED; i++){
				for (int j=y - GuiConstants.KERNEL_SIZE_USED; j <= y + GuiConstants.KERNEL_SIZE_USED; j++){
					if (i >=0 && j >=0 && i < w && j < h)
						childIntensity[i][j] += gaussian[Math.abs(x-i)][Math.abs(y-j)]*d;
				}
			}
		}
		
		System.out.println("Hello");
		return childIntensity;
	}	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
