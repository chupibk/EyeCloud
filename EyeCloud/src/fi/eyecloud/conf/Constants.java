package fi.eyecloud.conf;

public class Constants {
	/**
	 * Header setting
	 */
	public final static String Timestamp 			= "Timestamp";
	public final static String Number 				= "Number";
	public final static String GazePointXLeft 		= "GazePointXLeft";
	public final static String GazePointYLeft		= "GazePointYLeft";
	public final static String CamXLeft 			= "CamXLeft";
	public final static String CamYLeft 			= "CamYLeft";
	public final static String DistanceLeft 		= "DistanceLeft";
	public final static String PupilLeft 			= "PupilLeft";
	public final static String ValidityLeft 		= "ValidityLeft";
	public final static String GazePointXRight 		= "GazePointXRight";
	public final static String GazePointYRight 		= "GazePointYRight";
	public final static String CamXRight 			= "CamXRight";
	public final static String CamYRight 			= "CamYRight";
	public final static String DistanceRight 		= "DistanceRight";
	public final static String PupilRight 			= "PupilRight";
	public final static String ValidityRight 		= "ValidityRight";
	public final static String GazePointX 			= "GazePointX";
	public final static String GazePointY 			= "GazePointY";
	public final static String StimuliName 			= "StimuliName";
	public final static String StimuliID			= "StimuliID";
	
	public static final String HEADER[] = {	Timestamp, Number, GazePointXLeft, GazePointYLeft, CamXLeft, CamYLeft, 
											DistanceLeft, PupilLeft, ValidityLeft, GazePointXRight, GazePointYRight, 
											CamXRight, CamYRight, DistanceRight, PupilRight, ValidityRight, GazePointX,
											GazePointY, StimuliName, StimuliID	};	
	
	/**
	 * Text file setting
	 */
	public static final String SPLIT_MARK			= "\t";
	public static int UNKNOWN						= -1;
	
	/**
	 * Screen setting
	 */
	public static int RESOLUTION_WIDTH					= 1024;
	public static int RESOLUTION_HEIGHT					= 768;
	public static int SCREEN_WIDTH					= 53;
	public static int SCREEN_HEIGHT					= 45;
	
	/**
	 * Eye tracker setting
	 */
	public static int SYSTEM_TIME					= 1000;
	
	/**
	 * Fixation setting
	 */
	public static int FIXATION_DURATION_THRESHOLD	= 200; // Millisecond
	public static float VELOCITY_THRESHOLD			= 20;	// Degree
	public static String OUTPUT_FILE				= "/home/hadoop/eye/result_storm.txt";
	//public static String OUTPUT_FILE				= "result_storm.txt";
	public static int KIND_UNKNOWN					= 0;
	public static int KIND_FIXATION					= 1;
	
	/**
	 * Storm setting
	 */
	public static String PARAMETER_SPLIT			= "@";
}
