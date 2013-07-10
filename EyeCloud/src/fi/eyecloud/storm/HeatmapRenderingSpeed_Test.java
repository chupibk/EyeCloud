package fi.eyecloud.storm;

import backtype.storm.utils.DRPCClient;
import fi.eyecloud.conf.Constants;
import fi.eyecloud.gui.lib.GuiConstants;

public class HeatmapRenderingSpeed_Test {
    public static void main(String[] args) throws Exception {
			String HOSTNAME = args[0];
			String fileName = args[1];
			int gazeNumber = Integer.parseInt(args[2]);
			int numberPart = Integer.parseInt(args[3]);
			
			DRPCClient client = new DRPCClient(HOSTNAME, 3772);    
            
            String send = fileName + Constants.PARAMETER_SPLIT + gazeNumber + Constants.PARAMETER_SPLIT;
            
            send = send + GuiConstants.MEDIA_WIDTH + Constants.PARAMETER_SPLIT 
            			+ GuiConstants.MEDIA_HEIGHT + Constants.PARAMETER_SPLIT
            			+ numberPart;
            
            // Send data
            long start = System.currentTimeMillis();
            System.out.println(client.execute("heatmap_speed", send));
            System.out.println("Running time: " + (System.currentTimeMillis() - start));
            
            client.close();
    }
}
