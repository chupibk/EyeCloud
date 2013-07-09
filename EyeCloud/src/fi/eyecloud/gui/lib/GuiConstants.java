package fi.eyecloud.gui.lib;

import fi.eyecloud.conf.Constants;

public class GuiConstants {
	// Screen
	/**
	 * ZOOM Rate
	 */
	public final static int SCREEN_RATE = 1;
	public final static int SCREEN_WIDTH = Constants.RESOLUTION_WIDTH/SCREEN_RATE + 100;
	public final static int SCREEN_HEIGHT = Constants.RESOLUTION_HEIGHT/SCREEN_RATE + 50;
	public final static int MEDIA_WIDTH = Constants.RESOLUTION_WIDTH/SCREEN_RATE;
	public final static int MEDIA_HEIGHT = Constants.RESOLUTION_HEIGHT/SCREEN_RATE;
	
	/**
	 * Error of position
	 */
	public final static int ERROR				= 20; // Change it
	public final static int DOT_SIZE			= 3;
	public final static int CIRCLE_SIZE_RATE	= 20;
	public final static int SMALLEST_SIZE		= 5;
	// Do not change the below line
	public final static int ERROR_USED			= ERROR/SCREEN_RATE;
	
	// Heatmap
	public final static int ALPHA				= 150;
	public final static int RAINBOW_SIZE		= 1000;
	
	// Gaussian
	/**
	 * Sigma of Gaussian
	 */
	public static int SIGMA							= 40;
	/**
	 * Kernel size
	 */
	public static int KERNEL_SIZE					= 160;
	// Do not change the below line
	public static int SIGMA_USED					= SIGMA/SCREEN_RATE;
	// Do not change the below line
	public static int KERNEL_SIZE_USED				= KERNEL_SIZE/SCREEN_RATE/2;
}
