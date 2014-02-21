package fi.eyecloud.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShuffleFile {

	/**
	 * Shuffle data
	 * 
	 * @param inputFile
	 * @throws IOException
	 */
	public ShuffleFile(String inputFile) throws IOException{
		FileReader fr = new FileReader(inputFile);
		BufferedReader in = new BufferedReader(fr);
		List<String> data = new ArrayList<String>();
		String tmp;
		
		while ((tmp = in.readLine()) != null){
			data.add(tmp);
		}
		in.close();
		fr.close();
		
		FileWriter fw = new FileWriter(inputFile);
		BufferedWriter out = new BufferedWriter(fw);
		
		while (data.size() > 0){
			Random ran = new Random();
			int n = ran.nextInt(data.size());
			out.write(data.get(n));
			out.write("\n");
			data.remove(n);
		}
		
		out.close();
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new ShuffleFile("dataset/svm/eye.test");
	}

}
