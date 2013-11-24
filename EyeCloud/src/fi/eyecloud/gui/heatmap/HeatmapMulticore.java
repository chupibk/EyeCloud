package fi.eyecloud.gui.heatmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.gui.lib.GuiConstants;
import fi.eyecloud.gui.lib.Intensity;
import fi.eyecloud.input.ReadTextFile;

public class HeatmapMulticore implements Intensity{
	private ReadTextFile dataFile;
	private Map<String, Integer> mapHeader;
	private double intensity[][];
	private double gaussianWindow[][];
	private int width;
	private int height;
	private int duration;
	
	/**
	 * Generate heatmap intensity
	 * 
	 * @param input
	 * @param option: 0 from fixation and 1 from raw data 
	 */
	@Override
	public void run(String input, int option, int w, int h){
		dataFile = new ReadTextFile(input);
		mapHeader = dataFile.getMapHeader();
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
		}else{
			try {
				fromGazePointsMulticore(Constants.THOUSAND/Constants.SAMPLE_RATE);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public double gaussian2D(int x, int y){
		double value;
		value = Math.exp(-(x*x + y*y)/(2*GuiConstants.SIGMA_USED*GuiConstants.SIGMA_USED));		
		return value;
	}
	
	@Override
	public double[][] getIntensity(){
		return intensity;
	}	

	@Override
	public void fromFixation() {
		// TODO Auto-generated method stub		
	}
		
	@Override
	public void fromGazePoints(int duration) {
		// TODO Auto-generated method stub
		
	}	
	
	public void fromGazePointsMulticore(int d) throws InterruptedException, ExecutionException {
		duration = d;
		int cpus = Runtime.getRuntime().availableProcessors();
		List<List<String>> data = new ArrayList<List<String>>();
		for (int i=0; i < cpus; i++){
			List<String> childData = new ArrayList<String>();
			data.add(childData);
		}
		
		// Get data
		String line;
		int count = 0;
		while ((line = dataFile.readNextLine()) != null){
			data.get(count).add(line);
			count = (count + 1)%cpus;
		}
		
		// Multicore processing
		ExecutorService pool = Executors.newFixedThreadPool(cpus);
		List<IntensityMulticore> tasks = new ArrayList<IntensityMulticore>();
		for (int i = 0; i < cpus; i++) {
			tasks.add(new IntensityMulticore(mapHeader, data.get(i), width, height, gaussianWindow, duration));
		}
		List<Future<double[][]>> results = pool.invokeAll(tasks);		
		
		// Aggregate
		for (Future<double[][]> result : results) {
			if (result.isCancelled()) {
				System.err.println("Life is a bitch");
			} else {
				double[][] childIntensity = result.get();
				
				for (int i=0; i < width; i++){
					for (int j=0; j < height; j++){
						intensity[i][j] += childIntensity[i][j];
					}
				}				
			}
		}		
	}	
		
	public HeatmapMulticore(){
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
