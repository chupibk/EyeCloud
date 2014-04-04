package fi.eyecloud.conf;

public class Constants {
	/**
	 * Header setting for input file. Change if they are different from your file.
	 */
	public final static String Timestamp 			= "RecordingTimestamp";
	public final static String Number 				= "GazePointIndex";
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
	public final static String Duration				= "Duration";
	public final static String Delay				= "Delay";
	public final static String EventKey				= "EventKey";
	public final static String Fixation				= "Fixation";
	
	// Don't change here
	public static final String HEADER[] = {	Timestamp, Number, GazePointXLeft, GazePointYLeft, CamXLeft, CamYLeft, 
											DistanceLeft, PupilLeft, ValidityLeft, GazePointXRight, GazePointYRight, 
											CamXRight, CamYRight, DistanceRight, PupilRight, ValidityRight, GazePointX,
											GazePointY, StimuliName, StimuliID, Duration, Delay, EventKey, Fixation};	
	
	/**
	 * Screen setting of stimulus
	 */
	public static int RESOLUTION_WIDTH					= 1280; //1920
	public static int RESOLUTION_HEIGHT					= 1024; //1080
	public static int SCREEN_WIDTH					= 35;
	public static int SCREEN_HEIGHT					= 26;
	
	/**
	 * Eye tracker setting: sample rate
	 */
	public static int SAMPLE_RATE				= 50;
	public static int THOUSAND					= 1000;
	public static int TEN						= 10;
	
	/**
	 * Text file setting: how to split a line
	 */
	public static final String SPLIT_MARK			= "\t";
	public static int UNKNOWN						= -1;	
	
	/**
	 * Fixation setting
	 */
	public static int FIXATION_DURATION_THRESHOLD	= 100; // Millisecond
	public static float VELOCITY_THRESHOLD			= 100;	// Degree
	public static String OUTPUT_FILE				= "/Users/daothanhchung/Desktop/EyeCloud/Data/Output/result_drpc.txt";
	public static int KIND_UNKNOWN					= 0;
	public static int KIND_FIXATION					= 1;
	
	/**
	 * Storm sending data setting: number of elements
	 */
	public static String PARAMETER_SPLIT			= ",";
	public static int PARAMETER_NUMBER_FIXATION		= 7;
	public static int PARAMETER_NUMBER_FIXATION_IVT		= 4;
	
	/**
	 * Heatmap: number of elements for sending
	 */
	public static int PARAMETER_NUMBER_HEATMAP		= 3;
	
	/**
	 * Classification settings: N-Fold, C, and Gamma constants
	 */
	public static int FIXATION_SEQUENCE_NUMBER		= 3;
	public static int N_FOLD_CROSS_VALIDATION		= 5;
	public static int C_START						= -5;
	public static int C_END							= 15;
	public static int GAMMA_START					= -15;
	public static int GAMMA_END						= 3;
	public static int STEP							= 4;
	
	/**
	 * Upload host for uploadig gaze points
	 */
	public static String UPLOAD_HOST = "http://master/UploadHeatmapFromStorm";
	public static String HOSTNAME = "master";
}



