package fi.eyecloud.svm;

import java.util.ArrayList;
import java.util.List;

import fi.eyecloud.input.ReadTextFile;

public class CreateBalancedData {

	public List<Integer> getListFixation(String inputFile){
		List<Integer> fixations = new ArrayList<Integer>();
		ReadTextFile data = new ReadTextFile(inputFile);
		while (data.readNextLine() != null){
			
		}
		
		data.closeFile();
		return fixations;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
