package fi.eyecloud.storm;

import backtype.storm.utils.DRPCClient;
import backtype.storm.utils.Utils;
import fi.eyecloud.conf.Constants;
import fi.eyecloud.gui.lib.GuiConstants;
import fi.eyecloud.input.ReadTextFile;

public class Heatmap_Send {
	/**
	 * Heatmap sending testing
	 * 
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
			String HOSTNAME = "54.229.84.177";//args[0];
			int numberPart = 1;//Integer.parseInt(args[1]);
		
			DRPCClient client = new DRPCClient(HOSTNAME, 3772);    
            
            ReadTextFile data = new ReadTextFile("data/heatmap.txt");
            String send = "";
            int count = 0;
            while (data.readNextLine() != null){
            	send = send + data.getField(Constants.GazePointX) + Constants.PARAMETER_SPLIT;
            	send = send + data.getField(Constants.GazePointY) + Constants.PARAMETER_SPLIT;
            	send = send + data.getField(Constants.Duration) + Constants.PARAMETER_SPLIT;
            	count++;
            	if (count == 40){
                    send = send + GuiConstants.MEDIA_WIDTH + Constants.PARAMETER_SPLIT 
                			+ GuiConstants.MEDIA_HEIGHT + Constants.PARAMETER_SPLIT
                			+ numberPart;
                
                    // Send data
                    long start = System.currentTimeMillis();
                    System.out.println(client.execute("Intensity", send));
                    System.out.println("Running time: " + (float)(System.currentTimeMillis() - start)/1000);
                    send = "";
                    count = 0;
                    Utils.sleep(2000);
            	}
            }
            
            if (!send.equals("")){
                send = send + GuiConstants.MEDIA_WIDTH + Constants.PARAMETER_SPLIT 
            			+ GuiConstants.MEDIA_HEIGHT + Constants.PARAMETER_SPLIT
            			+ numberPart;
            
                // Send data
                long start = System.currentTimeMillis();
                System.out.println(client.execute("Intensity", send));
                System.out.println("Running time: " + (float)(System.currentTimeMillis() - start)/1000);
            }
            
            data.closeFile();
            client.close();
    }
}
