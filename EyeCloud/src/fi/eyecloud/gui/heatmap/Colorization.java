package fi.eyecloud.gui.heatmap;

import java.awt.image.BufferedImage;
import fi.eyecloud.gui.lib.GuiConstants;
import fi.eyecloud.gui.lib.Rainbow;

public class Colorization {

	private BufferedImage image;
	private long timeMax;
	
	public Colorization(double[][] intensity, int width, int height){
		long start = System.currentTimeMillis();
        GetMaxMin maxMin = new GetMaxMin(intensity, width, height);
        timeMax = System.currentTimeMillis() - start;
        System.out.println("Max finding time: " + timeMax);
		double max = maxMin.getMax();
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int rgb[] = new Rainbow().getRGB();
		
		for (int i=0; i < width; i++){
			for (int j=0; j < height; j++){
				float red;
				if (max != 0)
					red = (float) (intensity[i][j]/max);
				else 
					red = 0;
				if (red < 0) red = 0;
				if (red > 1) red = 1;
				int color = rgb[(int)(red*(rgb.length-1))];

	    		int alpha = GuiConstants.ALPHA;
	            int newcolor = (alpha << 24) | color;
	            
	            if (j - GuiConstants.ERROR_USED >= 0)
	            	image.setRGB(i, j - GuiConstants.ERROR_USED, newcolor);
	            else
	            	image.setRGB(i, j, newcolor);
			}
		}		
	}
	
	public long getTimeMax(){
		return timeMax;
	}
	
	public BufferedImage getImage(){
		return image;
	}
}
