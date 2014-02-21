import backtype.storm.utils.DRPCClient;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import libs.ReadTextFile;

public class OfflineHeatmapSendToStorm {

    /**
     *  Send data to the cluster for offline heatmap.
     * 
     * @param textPath
     * @param mediaPath
     * @param outputName
     * @throws Exception 
     */
    public OfflineHeatmapSendToStorm(String textPath, String mediaPath, String outputName) throws Exception {
        int numberPart = 5;

        DRPCClient client = new DRPCClient(Constants.HOSTNAME, 3772);
        ReadTextFile data = new ReadTextFile(textPath);
        String send = "";
        while (data.readNextLine() != null) {
            send = send + data.getField(libs.Constants.GazePointX) + libs.Constants.PARAMETER_SPLIT;
            send = send + data.getField(libs.Constants.GazePointY) + libs.Constants.PARAMETER_SPLIT;
            send = send + "8" + libs.Constants.PARAMETER_SPLIT;
        }

        File media = new File(mediaPath);
        BufferedImage image = ImageIO.read(media);
        send = send + image.getWidth() + libs.Constants.PARAMETER_SPLIT
                + image.getHeight() + libs.Constants.PARAMETER_SPLIT
                + numberPart + libs.Constants.PARAMETER_SPLIT
                + outputName;

        client.execute("offlineheatmap", send);

        data.closeFile();
        client.close();
    }
}
