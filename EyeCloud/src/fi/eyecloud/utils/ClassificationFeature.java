package fi.eyecloud.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import fi.eyecloud.alone.I_VT_KeyPress;

public class ClassificationFeature {

	private	BufferedWriter svmTrain, svmTest;
	private long readingTime, processingTime;
	private int intention, nonintention;
	
	/**
	 * Testing features
	 * 
	 * @throws IOException
	 */
	public ClassificationFeature() throws IOException{
		System.gc();
		readingTime = processingTime = 0;
		intention = nonintention = 0;
		
		FileWriter fwTrain, fwTest;
		fwTrain = new FileWriter("dataset/svm/eye.train");
		fwTest = new FileWriter("dataset/svm/eye.test");
		svmTrain = new BufferedWriter(fwTrain);
		svmTest = new BufferedWriter(fwTest);
		
		for (int i=1; i <= 12; i++){
			I_VT_KeyPress data = new I_VT_KeyPress("dataset/" + i + ".txt", "dataset/fixation/tmp.txt", svmTrain, svmTest);
			readingTime += data.getReadingTime();
			processingTime += data.getProcessingTime();
			intention += data.getNumberIntention();
			nonintention += data.getNumberNonIntention();
		}
			
		System.out.println("Reading time: " + readingTime);
		System.out.println("Processing time: " + processingTime);
		System.out.println("Intention: " + intention);
		System.out.println("Nonintention: " + nonintention);
		svmTest.close();
		svmTrain.close();
		fwTest.close();
		fwTrain.close();
		
		// Shuffle
		new ShuffleFile("dataset/svm/eye.train");
		new ShuffleFile("dataset/svm/eye.test");
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new ClassificationFeature();
	}

}
