package com.mjp.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageUtil {

	/**
	 * @param ins
	 * @return
	 * @throws Exception
	 */
	public static String parseImg(String path,boolean isRemote) throws Exception {
		byte[] bytes = null;
		int width = 80;
		int height = 23;
		InputStream ins = null;
		if(isRemote){
			BufferedImage bufferedImage = ImageIO.read(new URL(path));    
			width = bufferedImage.getWidth();    
			height = bufferedImage.getHeight(); 
			ins = getImgInputStream(path);
		}else{
			BufferedImage bufferedImage = ImageIO.read(new File(path));    
			width = bufferedImage.getWidth();    
			height = bufferedImage.getHeight();  
			ins = new FileInputStream( path );
		}
		bytes = getBytes(ins,width,height);
		return parseImg(bytes, width, height);
	}
	
	private static byte[] getBytes(InputStream fis,int width,int height) throws Exception{
		byte tempBuffer[] = new byte[100];
		fis.read(tempBuffer, 0, 14);
		fis.read( tempBuffer, 0, 4 );
		fis.read( tempBuffer, 0, 4 );
		fis.read( tempBuffer, 0, 4 );	
		fis.read( tempBuffer, 0, 28 );	
		int bytesPerLine = width*3;
		if( bytesPerLine%4 != 0 ){
			bytesPerLine += 4 - (bytesPerLine%4);
		}
		
		byte[] bytes = new byte[height*bytesPerLine];
		fis.read( bytes, 0, height*bytesPerLine );
		fis.close();
		return bytes;
	}
	
	

	/**
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static String parseImg(byte[] bytes, int width, int height)
			throws Exception {
		ProcessImage ImageProcessor = new ProcessImage();
		String r = ImageProcessor.parse(bytes, width, height);
		return r;
	}

	/**
	 * @param httpUrl
	 * @return
	 * @throws Exception
	 */
	private static InputStream getImgInputStream(String httpUrl) throws Exception {
		InputStream in;
		URL url = new java.net.URL(httpUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("User-Agent", "Mozilla/4.0");
		connection.connect();
		in = connection.getInputStream();
		return in;
	}

	public static void main(String[] args) throws Exception {
		String v = parseImg("Users/WANG/Desktop/Missions/imgpro/test/8436.bmp",false);
		System.out.println(v);
	}

}
