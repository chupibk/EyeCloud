package fi.eyecloud.storm.fixation;

import java.io.IOException;

import org.apache.thrift7.TException;

import backtype.storm.generated.DRPCExecutionException;

public class Simulator {

	public Simulator(String h, String p, int s, int min, int max) {
		for (int i = min; i < max; i++) {
			int d = (int)(Math.random()*10 + 1);
			EyeThread eye = new EyeThread(h, d, p, s, i);
			eye.start();
		}
	}

	public class EyeThread extends Thread {
		String hostName;
		int dataPath;
		String prefix;
		int segment;
		int id;
		
		public EyeThread(String h, int d, String p, int s, int i) {
			hostName = h;
			dataPath = d;
			prefix = p;
			segment = s;
			id = i;
		}

		@Override
		public void run() {
			try {
				new EyeTracker(hostName, dataPath, prefix, segment, id);
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
		new Simulator("54.229.233.105", args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		//new Simulator("54.229.233.105", "test", 100, 0, 2);
	}

}
