package fi.eyecloud.storm.fixation;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.input.ReadTextFile;

public class FixationHistogram {

	/**
	 * Calculate histogram of fixation duration distribution
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadTextFile fixation = new ReadTextFile("exp/result/all.txt");
		int ms_40 = 0;
		int ms_80 = 0;
		int ms_120 = 0;
		int ms_160 = 0;
		int ms_200 = 0;
		int ms_240 = 0;
		int ms_280 = 0;
		int ms_320 = 0;
		int ms_360 = 0;
		int ms_400 = 0;
		
		while (fixation.readNextLine() != null){
			int duration = Integer.parseInt(fixation.getField(Constants.Duration));
			if (duration >= 400) ms_400++;
			if (duration < 400 && duration >= 360) ms_360++;
			if (duration < 360 && duration >= 320) ms_320++;
			if (duration < 320 && duration >= 280) ms_280++;
			if (duration < 280 && duration >= 240) ms_240++;
			if (duration < 240 && duration >= 200) ms_200++;
			if (duration < 200 && duration >= 160) ms_160++;
			if (duration < 160 && duration >= 120) ms_120++;
			if (duration < 120 && duration >= 80) ms_80++;
			if (duration < 80 && duration >= 40) ms_40++;
			System.out.print(duration + ",");
		}
		
		int total = (ms_40 + ms_80 + ms_120 + ms_160 + ms_200 + ms_240 + ms_280 + ms_320 + ms_360 + ms_400);
		
		System.out.println((float)ms_40*100/total);
		System.out.println((float)ms_80*100/total);
		System.out.println((float)ms_120*100/total);
		System.out.println((float)ms_160*100/total);
		System.out.println((float)ms_200*100/total);
		System.out.println((float)ms_240*100/total);
		System.out.println((float)ms_280*100/total);
		System.out.println((float)ms_320*100/total);
		System.out.println((float)ms_360*100/total);
		System.out.println((float)ms_400*100/total);
	}

}
