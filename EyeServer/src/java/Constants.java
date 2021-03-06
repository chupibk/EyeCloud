/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author chung-pi <cdao@cs.uef.fi>
 */
public class Constants {
    /**
     * Host name
     */
    public final static String HOSTNAME = "localhost";
    //public final static String  HOSTNAME = "ec2-54-229-95-189.eu-west-1.compute.amazonaws.com";
    public final static int     PORT        = 3772;
    
    // Method name in the cluster
    public final static String METHOD_HEATMAP  = "website_rendering";
    
    // Tag
    public final static String TAG_DATA = "data";
    public final static String TAG_CALLBACK = "callback";
    public final static String TAG_HEATMAP_ID = "heatmapid";
    public final static String TAG_FRAME_ID = "frameid";
    
    /* Servlet */
    public static final String UPLOAD_NAME  = "UploadServlet";
    public static final String UPLOAD_LINK  = "/UploadServlet";
    public static final String UPLOAD_PATH  = "/upload/";
    public static final int maxFileSize     = 10 * 1024 * 1024;
    public static final int maxMemSize      = 100 * 1024 * 1024;
    public static final String TEMP         = "/tmp/";
    public static final String FRAME_TYPE   = ".png";
    
    /* Upload path */
    public static final String FIXATION_UPLOAD_PATH = "/files/fixation/upload/";
    public static final String FIXATION_UPLOAD_RESULT = "/files/fixation/result/";
    
    public static final String HEATMAP_UPLOAD_PATH = "/files/heatmap/upload/";
    public static final String HEATMAP_UPLOAD_RESULT = "/files/heatmap/result/";
}
