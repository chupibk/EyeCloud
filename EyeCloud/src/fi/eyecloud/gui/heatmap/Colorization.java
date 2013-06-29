package fi.eyecloud.gui.heatmap;

import java.awt.image.BufferedImage;
import fi.eyecloud.gui.lib.GuiConstants;
import fi.eyecloud.gui.lib.Rainbow;

public class Colorization {

	private BufferedImage image;
	
	public Colorization(double[][] intensity, int width, int height){
        GetMaxMin maxMin = new GetMaxMin(intensity, width, height);
		double max = maxMin.getMax();
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int rgb[] = new Rainbow().getRGB();
		
		for (int i=0; i < width; i++){
			for (int j=0; j < height; j++){
				float red = (float) (intensity[i][j]/max);
				if (red < 0) red = 0;
				if (red > 1) red = 1;
				int color = rgb[rgb.length - (int)(red*(rgb.length-1)) - 1];

	    		int alpha = GuiConstants.ALPHA;
	            int newcolor = (alpha << 24) | color;
	            
	            if (j - GuiConstants.ERROR_USED >= 0)
	            	image.setRGB(i, j - GuiConstants.ERROR_USED, newcolor);
	            else
	            	image.setRGB(i, j, newcolor);
			}
		}		
	}
	
	public BufferedImage getImage(){
		return image;
	}
}
