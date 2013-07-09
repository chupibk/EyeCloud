package fi.eyecloud.storm;

import backtype.storm.utils.DRPCClient;
import fi.eyecloud.conf.Constants;
import fi.eyecloud.gui.lib.GuiConstants;

public class HeatmapRenderingSpeed_Test {
    public static void main(String[] args) throws Exception {
			String HOSTNAME = "54.229.78.45"; //args[0];
			String fileName = "data/heatmap/100000.txt"; //args[1];
			int gazeNumber = 1000; //Integer.parseInt(args[2]);
			int numberPart = 3; //Integer.parseInt(args[3]);
			
			DRPCClient client = new DRPCClient(HOSTNAME, 3772);    
            
            String send = fileName + Constants.PARAMETER_SPLIT + gazeNumber + Constants.PARAMETER_SPLIT;
            
            send = send + GuiConstants.MEDIA_WIDTH + Constants.PARAMETER_SPLIT 
            			+ GuiConstants.MEDIA_HEIGHT + Constants.PARAMETER_SPLIT
            			+ numberPart;
            
            // Send data
            long start = System.currentTimeMillis();
            System.out.println(client.execute("Intensity", send));
            System.out.println("Running time: " + (System.currentTimeMillis() - start));
            
            client.close();
    }
}
