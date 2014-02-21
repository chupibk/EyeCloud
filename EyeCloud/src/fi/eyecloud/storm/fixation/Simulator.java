package fi.eyecloud.storm.fixation;

import java.io.IOException;

import org.apache.thrift7.TException;

import backtype.storm.generated.DRPCExecutionException;

public class Simulator {

	/**
	 * Simulation function
	 * 
	 * @param server
	 * @param p
	 * @param s
	 * @param min
	 * @param max
	 */
	public Simulator(String server[], String p, int s, int min, int max) {
		System.gc();
		for (int i = min; i < max; i++) {
			int h = (int)(Math.random()*5 + 1);
			int d = (int)(Math.random()*10 + 1);
			EyeThread eye = new EyeThread(server[h], d, p, s, i);
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
		String serverList[] = {	"", 
							"176.34.136.152",
							"54.246.244.184", 
							"176.34.136.192", 
							"176.34.136.133", 
							"176.34.136.175"};
		new Simulator(serverList, args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		//new Simulator("54.229.233.105", "test", 100, 0, 2);
	}

}
