package fi.eyecloud.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

import javax.imageio.ImageIO;

import fi.eyecloud.conf.Constants;

public class ClientFile {
	public ClientFile (BufferedImage image) throws IOException{
		String fileName = "/home/eyecloud/data/tmp" + System.currentTimeMillis() + ".png";
		File file = new File(fileName);
		ImageIO.write(image, "png",file);
		
		Socket socket = new Socket(Constants.HOSTNAME, 3332);
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		ObjectOutputStream oos = new ObjectOutputStream(
				socket.getOutputStream());

		oos.writeObject(file.getName());
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[ServerFile.BUFFER_SIZE];
		Integer bytesRead = 0;

		while ((bytesRead = fis.read(buffer)) > 0) {
			oos.writeObject(bytesRead);
			oos.writeObject(Arrays.copyOf(buffer, buffer.length));
		}

		fis.close();
		oos.close();
		ois.close();		
	}
	
	public ClientFile (BufferedImage image, String name, String id) throws IOException{
		String fileName = "/home/eyecloud/data/tmp/" + id + Constants.PARAMETER_SPLIT + name + ".png";
		File file = new File(fileName);
		ImageIO.write(image, "png",file);
		
		Socket socket = new Socket(Constants.HOSTNAME, 3332);
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		ObjectOutputStream oos = new ObjectOutputStream(
				socket.getOutputStream());

		oos.writeObject(file.getName());
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[ServerFile.BUFFER_SIZE];
		Integer bytesRead = 0;

		while ((bytesRead = fis.read(buffer)) > 0) {
			oos.writeObject(bytesRead);
			oos.writeObject(Arrays.copyOf(buffer, buffer.length));
		}

		fis.close();
		oos.close();
		ois.close();		
	}	
	
	public static void main(String[] args) throws Exception {
	}
}