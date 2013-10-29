package fi.eyecloud.storm.fixation;

import fi.eyecloud.conf.Constants;
import fi.eyecloud.input.ReadTextFile;

public class FixationHistogram {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadTextFile fixation = new ReadTextFile("data/17JuneResult.txt");
		int ms_40 = 0;
		int ms_100 = 0;
		int ms_500 = 0;
		
		while (fixation.readNextLine() != null){
			int duration = Integer.parseInt(fixation.getField(Constants.Duration));
			if (duration >= 500) ms_500++;
			if (duration >= 100 && duration < 500) ms_100++;
			if (duration >=40 && duration < 100) ms_40++;
		}
		
		int total = (ms_40 + ms_100 + ms_500);
		
		System.out.println((float)ms_40*100/total);
		System.out.println((float)ms_100*100/total);
		System.out.println((float)ms_500*100/total);
	}

}
