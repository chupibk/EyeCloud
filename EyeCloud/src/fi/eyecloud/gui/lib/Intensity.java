package fi.eyecloud.gui.lib;

public interface Intensity {
	void run(String input, int option, int w, int h);
	
	void fromFixation();
	
	double[][] getIntensity();
}
