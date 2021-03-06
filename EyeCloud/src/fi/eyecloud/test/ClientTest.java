package fi.eyecloud.test;

import org.apache.thrift7.TException;
import backtype.storm.generated.DRPCExecutionException;
import backtype.storm.utils.DRPCClient;

public class ClientTest {

	/**
	 * Client test on the client
	 * 
	 * @param args
	 * @throws DRPCExecutionException 
	 * @throws TException 
	 */
	public static void main(String[] args) throws TException, DRPCExecutionException {
		// TODO Auto-generated method stub
		String HOSTNAME = "ec2-54-229-95-189.eu-west-1.compute.amazonaws.com";
		String METHOD	= "website_rendering";
		
		DRPCClient client = new DRPCClient(HOSTNAME, 3772);
		long start = System.currentTimeMillis();
		int x = 500;
		for (int i=0; i < 100; i++){
			System.out.println(client.execute(METHOD, x + ",200,20,1100,500,1"));
			x += 3;
		}
		//System.out.println(client.execute(METHOD, "0"));
		System.out.println("Runnung time: " + (System.currentTimeMillis() - start));
		client.close();
	}

}

