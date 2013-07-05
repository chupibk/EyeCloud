package fi.eyecloud.utils;

import org.apache.thrift7.TException;

import backtype.storm.generated.DRPCExecutionException;
import backtype.storm.utils.DRPCClient;

public class DRPC_Test {

	/**
	 * @param args
	 * @throws DRPCExecutionException 
	 * @throws TException 
	 */
	public static void main(String[] args) throws TException, DRPCExecutionException {
		// TODO Auto-generated method stub
		String HOSTNAME = "54.229.84.177";
		DRPCClient client = new DRPCClient(HOSTNAME, 3772);
		
		System.out.println(client.execute("method_test", "Ok"));
		client.close();
	}

}
