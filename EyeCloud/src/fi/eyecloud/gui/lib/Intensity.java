package fi.eyecloud.gui.lib;

/*
 * Interface for intensity class
 */
public interface Intensity {
	void run(String input, int option, int w, int h);
	
	void fromFixation();
	
	void fromGazePoints(int duration);
	
	double[][] getIntensity();
}
