package fi.eyecloud.storm.classification;

import org.apache.thrift7.TException;

import backtype.storm.generated.DRPCExecutionException;
import backtype.storm.utils.DRPCClient;
import fi.eyecloud.conf.Constants;
import fi.eyecloud.input.ReadTextFile;

public class SendDataTest {

	public SendDataTest(String hostName, String inputFile, int max, int id) throws TException, DRPCExecutionException{
		DRPCClient client = new DRPCClient(hostName, 3772);
		ReadTextFile data = new ReadTextFile(inputFile);
		
        String currentSend = "";
        int count = 0;
        long start = System.currentTimeMillis();
        while (data.readNextLine() != null){
			int x = (Integer.parseInt(data.getField(Constants.GazePointXLeft)) +
					Integer.parseInt(data.getField(Constants.GazePointXRight)))/2;
			int y = (Integer.parseInt(data.getField(Constants.GazePointYLeft)) +
					Integer.parseInt(data.getField(Constants.GazePointYRight)))/2;			
			currentSend = currentSend + x
					+ Constants.PARAMETER_SPLIT;
			currentSend = currentSend + y
					+ Constants.PARAMETER_SPLIT;
			int timestamp = Integer.parseInt(data.getField(Constants.Timestamp));
			currentSend = currentSend + timestamp + Constants.PARAMETER_SPLIT;

			float dis = (Float
					.parseFloat(data.getField(Constants.DistanceLeft)) + Float
					.parseFloat(data.getField(Constants.DistanceRight))) / 2;

			currentSend = currentSend + dis + Constants.PARAMETER_SPLIT;
			currentSend = currentSend + System.currentTimeMillis() + Constants.PARAMETER_SPLIT;
			currentSend = currentSend + Integer.parseInt(data.getField(Constants.Number)) 
										+ Constants.PARAMETER_SPLIT;
			
			int keypress = 0;
			if (Integer.parseInt(data.getField(Constants.EventKey)) != Constants.UNKNOWN){
				keypress = Integer.parseInt(data.getField(Constants.EventKey));
			}
			currentSend = currentSend + keypress + Constants.PARAMETER_SPLIT;   			
			count++;
			
			if (count == max){
				currentSend = currentSend + id;
				//System.out.println(currentSend);
				//System.out.println("Output file: " + client.execute("TrainTest", currentSend));
				client.execute("TrainTest", currentSend);
				count = 0;
				currentSend = "";
			}
        }
        
		if (count > 0){
			currentSend = currentSend + id;
			//System.out.println("Output file: " + client.execute("TrainTest", currentSend));
			client.execute("TrainTest", currentSend);
			count = 0;
			currentSend = "";
		}            
		System.out.println("Running time of featuring: " + (float)(System.currentTimeMillis() - start)/1000);  
		
        start = System.currentTimeMillis();
        System.out.println("Final Send: " + client.execute("TrainTest", Integer.toString(id)));
        System.out.println("Running time of training: " + (float)(System.currentTimeMillis() - start)/1000);            

        data.closeFile();
        client.close();		
	}
	
	/**
	 * @param args
	 * @throws DRPCExecutionException 
	 * @throws TException 
	 */
	public static void main(String[] args) throws TException, DRPCExecutionException {
		// TODO Auto-generated method stub
		//new SendDataTest("54.194.14.116", "classification/AjayaCMD.txt", 5000, 1);
		new SendDataTest(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
	}

}
