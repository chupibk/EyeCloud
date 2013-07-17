package fi.eyecloud.gui.lib;

public class Rainbow {
	private int rgb[];
	
	public Rainbow(){
		/*
	    int width;
	    int height;
	    
	    int rate = 4;
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
	    }*/
		rgb = new int[101];
		int count = 0;
		for (int i=0; i < 25; i++){
			int red = 0;
			int green = (int)(10.2 * i);
			int blue = 255;
			rgb[count++] = (red << 16) | (green << 8) | blue;
		}

		for (int i=25; i < 50; i++){
			int red = 0;
			int green = 255;
			int blue = (int)(-10.2 * i + 510);
			rgb[count++] = (red << 16) | (green << 8) | blue;
		}	
		
		for (int i=0; i < 25; i++){
			int red = (int)(10.2 * i);
			int green = 255;
			int blue = 0;
			rgb[count++] = (red << 16) | (green << 8) | blue;
		}
		
		for (int i=25; i <= 50; i++){
			int red = 255;
			int green = 0;
			int blue = (int)(-10.2 * i + 510);
			rgb[count++] = (red << 16) | (green << 8) | blue;
		}		
	}
	
	public int[] getRGB(){
		return rgb;
	}
}
