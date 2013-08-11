package libs;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ReadTextFile {
	private BufferedReader br;
	private Map<String, Integer> mapHeader = new HashMap<String, Integer>();
	private String currentLine;
	private String inputFile;
	
	/**
	 * Read Text File
	 * 
	 * @param filePath: path of file
	 */
	public ReadTextFile(String filePath){
		FileInputStream fstream;
		inputFile = filePath;
		
		br = null;
		try {
			fstream = new FileInputStream(filePath);
			DataInputStream in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			mapHeader();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Map Header from Constants declaration to text file
	 * 
	 */
	public void mapHeader(){
		String header = readNextLine();
		if (header == null) return;
		
		String split[] = header.split(Constants.SPLIT_MARK);
		for (int i=0; i < Constants.HEADER.length; i++){
			mapHeader.put(Constants.HEADER[i], Constants.UNKNOWN);
			for (int j = 0; j < split.length; j++){
				if (Constants.HEADER[i].equals(split[j])){
					mapHeader.put(Constants.HEADER[i], j);
					break;
				}
			}
		}
	}
	
	/**
	 * Read next line
	 * 
	 * @return Next line or null if EOF
	 */
	public String readNextLine(){
		try {
			currentLine = br.readLine();
			return currentLine;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get information of a field
	 * 
	 * @param fieldName
	 * @return
	 */
	public String getField(String fieldName){
		String split[] = currentLine.split(Constants.SPLIT_MARK);
		if (mapHeader.get(fieldName) == -1 || mapHeader.get(fieldName) >= split.length) 
			return Integer.toString(Constants.UNKNOWN);
		if (split[mapHeader.get(fieldName)].equals("")) 
			return Integer.toString(Constants.UNKNOWN);
		return split[mapHeader.get(fieldName)];
	}
	
	public void resetFile(){
		try {
			closeFile();
			FileInputStream fstream = new FileInputStream(inputFile);
			DataInputStream in = new DataInputStream(fstream);
			br = new BufferedReader(new InputStreamReader(in));
			readNextLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeFile(){
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Map<String, Integer> getMapHeader(){
		return mapHeader;
	}
	
	public String getCurrentLine(){
		return currentLine;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
