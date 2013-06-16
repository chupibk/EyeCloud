package fi.eyecloud.conf;

public class Library {
	/**
	 * Convert pixel to centimeter
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static float pixalToCenti(int x1, int y1, int x2, int y2){
		float x;
		float y;
		x = Math.abs((float)(x1-x2))*Constants.SCREEN_WIDTH/Constants.RESOLUTION_WIDTH*Constants.TEN;
		y = Math.abs((float)(y1-y2))*Constants.SCREEN_HEIGHT/Constants.RESOLUTION_HEIGHT*Constants.TEN;
		return (float)Math.sqrt(x*x + y*y);
	}
	
	/**
	 * Calculate velocity = degree/second
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param a
	 * @param b
	 * @param dur
	 */
	public static float VT_Degree(int x1, int y1, int x2, int y2, float a, float b, int dur){
		float c = pixalToCenti(x1, y1, x2, y2);
		
		// c2 = a2 + b2 - 2ab cos(C)
		float cosC;
		cosC = (a*a + b*b - c*c)/(2*a*b);
		//System.out.println(cosC);
		float degree;
		degree = (float)Math.acos(cosC)*180/(float)Math.PI;
		//System.out.println(degree/dur * Constants.THOUSAND + " " + dur);
		return degree/dur * Constants.THOUSAND;
	}
}
