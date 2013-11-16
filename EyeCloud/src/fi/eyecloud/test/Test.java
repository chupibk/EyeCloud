package fi.eyecloud.test;

import java.math.BigDecimal;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "5E-4";
		BigDecimal bg = new BigDecimal(s);
		System.out.println(bg);
		System.out.println(Math.log(bg.doubleValue()) / Math.log(2));
	}

}
