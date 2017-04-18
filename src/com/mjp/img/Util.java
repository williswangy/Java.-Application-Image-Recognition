package com.mjp.img;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Util {

	/**
	 * @param args
	 */
	public static int byte2Int(byte a) {
		int b;
		if (a < 0){
			b = a & 0x7f + 128;
		}else {	
			b = a;
		}
		return b;
	}
	
	/**
	 * 将内容写入到指定的文件中 ；创建文件
	 * 
	 * @param fileName
	 * @param content
	 * @throws IOException
	 */
	public static void write(String fileName, String content)
			throws IOException {
		BufferedOutputStream outputStream = new BufferedOutputStream(
				new FileOutputStream(fileName));
		outputStream.write(content.getBytes("utf-8"));
		outputStream.flush();
		outputStream.close();
	}

	/**
	 * 将stream 转换成 字符串
	 * 
	 * @param ins
	 * @return
	 */
	public static final String ins2String(InputStream ins) {
		try {
			java.io.BufferedReader breader = null;
			breader = new BufferedReader(new InputStreamReader(ins, "utf-8"));
			StringBuffer buf = new StringBuffer();
			String currentLine;
			while ((currentLine = breader.readLine()) != null) {
				buf.append(currentLine).append("\n");
			}
			return buf.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	public static final byte[] ins2Bytes(InputStream ins) {
		ByteArrayOutputStream outs = ins2Outs(ins);
		return outs.toByteArray();
	}

	public static final ByteArrayOutputStream ins2Outs(InputStream ins) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = ins.read(b, 0, 1024)) != -1) {
				baos.write(b, 0, len);
			}
			baos.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return baos;
	}

	/**
	 * 将stream 转换成 字符串
	 * 
	 * @param ins
	 * @return
	 */
	public static final InputStream str2InputSteam(String str) {
		try {
			byte[] bytes = str.getBytes("utf-8");
			return str2InputSteam(bytes);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 将stream 转换成 字符串
	 * 
	 * @param ins
	 * @return
	 */
	public static final InputStream str2InputSteam(byte[] bytes) {
		try {
			ByteArrayInputStream ins = new ByteArrayInputStream(bytes);
			return ins;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static final OutputStream bytes2OutputStream(byte[] bytes) {
		try {
			ByteArrayInputStream ins = new ByteArrayInputStream(bytes);
			return ins2Outs(ins);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}
}
