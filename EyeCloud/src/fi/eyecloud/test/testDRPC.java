package fi.eyecloud.test;

import backtype.storm.utils.DRPCClient;

public class testDRPC {
    
    public static void main(String[] args) throws Exception {
 
    		DRPCClient client = new DRPCClient("176.34.136.152", 3772);
            
            long start = System.currentTimeMillis();
            for (int i=0; i < 200; i++){
            	System.out.println("Output file: " + client.execute("exclamation", Integer.toString(i)));
            }
            //System.out.println("Output file: " + client.execute("I_VT", args[0]));
            //System.out.println(client.execute("exclamation", "chupi_bull"));
            //System.out.println(client.execute("reach", "foo.com/blog/1"));
            System.out.println("Running time: " + (float)(System.currentTimeMillis() - start)/1000);
            
            client.close();
    }
}
