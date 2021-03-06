/**
 * 
 */
package fi.eyecloud.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;

/**
 * @author daothanhchung
 * 
 */
public class UploadData {
	
	/**
	 * Upload data to clients
	 * 
	 * @param host
	 * @param img
	 * @param output
	 * @return
	 */
	public static String upload(String host, BufferedImage img, String output) {
		HttpClient httpClient;
		HttpPost postRequest;
			
		httpClient = new DefaultHttpClient();
		postRequest = new HttpPost(host);
		
		String result = "";

		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(img, "png", bos);
			byte[] data = bos.toByteArray();
			ByteArrayBody image = new ByteArrayBody(data, "image/jpg",
					output + ".jpg");
			
			MultipartEntity reqEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart("image", image);

			postRequest.setEntity(reqEntity);
			HttpResponse response = httpClient.execute(postRequest);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String sResponse;
			StringBuilder s = new StringBuilder();

			while ((sResponse = reader.readLine()) != null) {
				s = s.append(sResponse);
			}
			result = s.toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return result;
	}
	
	public static void main(String args[]) throws IOException{
		//BufferedImage originalImage = ImageIO.read(new File("/Users/daothanhchung/Desktop/Bull/Avatar/bull.jpg"));
		//System.out.println(UploadData.upload("http://54.229.100.28:8080/EyeServer/UploadServlet", originalImage));
	}
}
