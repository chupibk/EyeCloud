package fi.eyecloud.gui.lib;

import fi.eyecloud.conf.Constants;

public class GuiConstants {
	/**
	 * Fixation screen
	 */
	
	public final static int SCREEN_RATE = 1;
	public final static int SCREEN_WIDTH = Constants.RESOLUTION_WIDTH/SCREEN_RATE + 100;
	public final static int SCREEN_HEIGHT = Constants.RESOLUTION_HEIGHT/SCREEN_RATE + 50;
	public final static int MEDIA_WIDTH = Constants.RESOLUTION_WIDTH/SCREEN_RATE;
	public final static int MEDIA_HEIGHT = Constants.RESOLUTION_HEIGHT/SCREEN_RATE;
	
	public final static int ERROR				= 20;
	public final static int DOT_SIZE			= 3;
	public final static int CIRCLE_SIZE_RATE	= 5;
}
