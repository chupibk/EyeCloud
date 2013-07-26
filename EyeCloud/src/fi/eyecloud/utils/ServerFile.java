package fi.eyecloud.utils;

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import fi.eyecloud.conf.Constants;

public class ServerFile extends Thread {
	public static final int PORT = 3332;
	public static final int BUFFER_SIZE = 100;

	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);

			while (true) {
				Socket s = serverSocket.accept();
				saveFile(s);
			}
		} catch (EOFException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveFile(Socket socket) {
		try {
		ObjectOutputStream oos = new ObjectOutputStream(
				socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		FileOutputStream fos = null;
		byte[] buffer = new byte[BUFFER_SIZE];

		// 1. Read file name.
		Object o = ois.readObject();

		if (o instanceof String) {
			String split[] = o.toString().split(Constants.PARAMETER_SPLIT);
			File file = new File("./" + split[0]);
			if (!file.exists()) {
				if (file.mkdir()) {
					System.out.println("Directory " + split[0] + " is created!");
				}
			}
		 
			fos = new FileOutputStream("./" + split[0] + "/" + split[1]);
		} else {
			throwException("Something is wrong");
		}

		// 2. Read file to the end.
		Integer bytesRead = 0;

		do {
			o = ois.readObject();

			if (!(o instanceof Integer)) {
				throwException("Something is wrong");
			}

			bytesRead = (Integer) o;

			o = ois.readObject();

			if (!(o instanceof byte[])) {
				throwException("Something is wrong");
			}

			buffer = (byte[]) o;

			// 3. Write data to output file.
			fos.write(buffer, 0, bytesRead);

		} while (bytesRead == BUFFER_SIZE);

		System.out.println("File transfer success");

		fos.close();

		ois.close();
		oos.close();
		}catch (EOFException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void throwException(String message) throws Exception {
		throw new Exception(message);
	}

	public static void main(String[] args) {
		new ServerFile().start();
	}
}
