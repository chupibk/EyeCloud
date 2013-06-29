package fi.eyecloud.storm;

import java.io.BufferedWriter;
import java.io.FileWriter;

import backtype.storm.utils.DRPCClient;
import fi.eyecloud.conf.Constants;
import fi.eyecloud.gui.heatmap.HeatmapFromText;
import fi.eyecloud.gui.lib.GuiConstants;
import fi.eyecloud.input.ReadTextFile;
import fi.eyecloud.utils.ImageUtils;

public class Heatmap_Test {
    public static void main(String[] args) throws Exception {
			String HOSTNAME = args[0];
			int numberPart = Integer.parseInt(args[1]);
		
			DRPCClient client = new DRPCClient(HOSTNAME, 3772);    
            
            ReadTextFile data = new ReadTextFile("data/17JuneResult.txt");
            String send = "";
            while (data.readNextLine() != null){
            	send = send + data.getField(Constants.GazePointX) + Constants.PARAMETER_SPLIT;
            	send = send + data.getField(Constants.GazePointY) + Constants.PARAMETER_SPLIT;
            	send = send + data.getField(Constants.Duration) + Constants.PARAMETER_SPLIT;
            }
            
            send = send + GuiConstants.MEDIA_WIDTH + Constants.PARAMETER_SPLIT 
            			+ GuiConstants.MEDIA_HEIGHT + Constants.PARAMETER_SPLIT
            			+ numberPart;
            
            // Send data
            long start = System.currentTimeMillis();
            String imageString = client.execute("Intensity", send);
            System.out.println("Running time: " + (float)(System.currentTimeMillis() - start)/1000);
            
            BufferedWriter out = null;
			FileWriter fw = new FileWriter("data/base64");
			out = new BufferedWriter(fw);
			out.write(imageString);
			out.close();
            
            // Draw
            new HeatmapFromText("data/17JuneMedia.png", ImageUtils.decodeToImage(imageString));
            
            data.closeFile();
            client.close();
    }
}
