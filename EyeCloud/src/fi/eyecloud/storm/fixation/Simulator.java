package fi.eyecloud.storm.fixation;

import java.io.IOException;

import org.apache.thrift7.TException;

import backtype.storm.generated.DRPCExecutionException;

public class Simulator {

	public Simulator(String h, String d, int s, int min, int max) {
		for (int i = min; i < max; i++) {
			EyeThread eye = new EyeThread(h, d, s, i);
			eye.start();
		}
	}

	public class EyeThread extends Thread {
		String hostName;
		String dataPath;
		int segment;
		int id;
		
		public EyeThread(String h, String d, int s, int i) {
			hostName = h;
			dataPath = d;
			segment = s;
			id = i;
		}

		@Override
		public void run() {
			try {
				new EyeTracker(hostName, dataPath, segment, id);
			} catch (TException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DRPCExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
