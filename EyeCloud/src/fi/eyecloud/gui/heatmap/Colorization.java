package fi.eyecloud.gui.heatmap;

import java.awt.Color;
import java.awt.image.BufferedImage;
import fi.eyecloud.gui.lib.GuiConstants;

public class Colorization {

	private BufferedImage image;
	
	public Colorization(double[][] intensity, int width, int height){
        GetMaxMin maxMin = new GetMaxMin(intensity, width, height);
		double max = maxMin.getMax();
        System.out.println(max);
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i=0; i < width; i++){
			for (int j=0; j < height; j++){
				float red = (float) (intensity[i][j]/max);
				if (red > 1) red = 1;
				if (red < 0) red = 0;
				Color c = new Color(red, GuiConstants.GREEN_COLOR, GuiConstants.BLUE_COLOR);
				image.setRGB(i, j, c.getRGB());
			}
		}		
	}
	
	public BufferedImage getImage(){
		return image;
	}
}
