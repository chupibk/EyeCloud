package fi.eyecloud.storm.classification;

import org.apache.thrift7.TException;

import fi.eyecloud.conf.Constants;
import backtype.storm.generated.DRPCExecutionException;
import backtype.storm.utils.DRPCClient;

public class CheckStatus {
	
	/**
	 * @param args
	 * @throws DRPCExecutionException 
	 * @throws TException 
	 */
	public static void main(String[] args) throws TException, DRPCExecutionException {
		// TODO Auto-generated method stub
		DRPCClient client = new DRPCClient(args[0], 3772);
		long start = System.currentTimeMillis();
		System.out.println(client.execute("TrainTest", args[1] + Constants.PARAMETER_SPLIT + args[2]));
		System.out.println("Running time of testing: " + (System.currentTimeMillis() - start));	
		client.close();
	}

}
