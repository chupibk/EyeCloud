package fi.eyecloud.gui.lib;

public class Rainbow {
	private int rgb[];
	
	public Rainbow(){
	    int width;
	    int height;
	    
	    int rate = 3;
	    width = GuiConstants.RAINBOW_SIZE/rate;
	    height = rate;
	    rgb = new int[width * height];
	    int index = 0;
	    
	    for (int i = 0; i < height; i++) {
	      int green = (i * 255) / (height - 1);
	      for (int j = 0; j < width; j++) {
	        int blue = (j * 255) / (width - 1);
	        int red = 255;
	        rgb[index++] = (red << 16) | (green << 8) | blue;
	      }
	    }		
	}
	
	public int[] getRGB(){
		return rgb;
	}
}
