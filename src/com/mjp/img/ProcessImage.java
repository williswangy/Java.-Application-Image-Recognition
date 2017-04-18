package com.mjp.img;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Administrator
 * 
 */
public class ProcessImage {
	private byte patternbuffer[][];

	ProcessImage() {
		try {
			InputStream fis = ProcessImage.class
			.getResourceAsStream("Pattern.dat");
			int h = 0, w = 0, i = 0;
			byte picnumber[] = new byte[4];
			fis.read(picnumber, 0, 4);
			int total;
			total = picnumber[3] + picnumber[2] * 128 + picnumber[1] * 32768
			+ picnumber[0] * 8388608;
			patternbuffer = new byte[total][];
			// System.out.print(total);
			for (i = 0; i < total; i++) {
				fis.read(picnumber, 0, 4);
				w = picnumber[1] + picnumber[0] * 128;
				h = picnumber[3] + picnumber[2] * 128;
				patternbuffer[i] = new byte[w * h];
				fis.read(patternbuffer[i], 0, w * h);
			}
			fis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * 彩色变灰度
	 */
	private static int[] rgb2gray(int imagebufferint[], int width, int height,
			int perline) {
		int r, g, b;
		int realimagebuffer[] = new int[width * height];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j = j + 1) {
				b = (int) (imagebufferint[perline * i + j * 3] * 0.11);
				g = (int) (imagebufferint[perline * i + j * 3 + 1] * 0.59);
				r = (int) (imagebufferint[perline * i + j * 3 + 2] * 0.3);
				realimagebuffer[i * width + j] = b + g + r;// 注意行列
			}
		}
		return realimagebuffer;
	}

	/* 灰度变二值 */

	private static byte[] gray2bool(int realimagebuffer[], int width,
			int height, int Threshold) {
		byte byteimagebuffer[] = new byte[height * width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j = j + 1) {
				if (realimagebuffer[i * width + j] < Threshold) {
					byteimagebuffer[i * width + j] = 1;
				} else {
					byteimagebuffer[i * width + j] = 0;
				}
			}
		}
		return byteimagebuffer;
	}

	/*
	 * 比较一个矩阵是否同另一个矩阵的某一部分相等
	 */
	private static boolean matrixequal(byte imagebufferbool[],
			byte partimage[][], int clside[], int rowside[], int bianchang[],
			int y, int width, int k) {
		int flag = 0;
		for (int i = 0; i < bianchang[y * 2 + 1]; i++) {

			if (flag == 1) {
				break;
			}
			for (int j = 0; j < bianchang[y * 2]; j++) {

				if (partimage[k][i * bianchang[y * 2] + j] != imagebufferbool[(i + rowside[y * 2])
				                                                              * width + (j + clside[y * 2])]) {
					flag = 1;
					break;
				} else if ((i == (bianchang[y * 2 + 1] - 1))
						&& (j == (bianchang[y * 2] - 1))) {
					return true;
				}
			}
		}
		if (flag == 1) {
			return false;
		}
		return false;
	}

	/*
	 * 彩色聚类后变为二值
	 */
	@SuppressWarnings("unused")
	private static byte[] colour2bool(int imagebufferint[], int perline,
			int height, int width) {
		int rgb[] = new int[100];
		int colournumber[] = new int[100];
		int end = 1;
		int RGB;
		int r, g, b;
		int realimagebuffer[] = new int[height * width];
		for (int i = 0; i < 100; i++) {
			rgb[i] = 0;
			colournumber[i] = 0;
		}
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				b = (int) imagebufferint[perline * i + j * 3];
				g = (int) imagebufferint[perline * i + j * 3 + 1];
				r = (int) imagebufferint[perline * i + j * 3 + 2];
				RGB = r * 1000000 + g * 1000 + b;
				for (int k = 0; k < end; k++) {
					if (rgb[k] == RGB) {
						colournumber[k]++;
						break;
					} else {
						if (k == (end - 1)) {
							end++;
							rgb[end - 1] = RGB;
							colournumber[end - 1]++;
							break;
						}
					}

				}
				realimagebuffer[i * width + j] = RGB;
			}
		}

		// 找出个数最多的4个，除了0和255
		int temp;
		// 用快速排序
		for (int i = 0; i < end; i++) {
			for (int j = i; j > 0; j--) {
				if (colournumber[j] > colournumber[j - 1]) {
					temp = colournumber[j];
					colournumber[j] = colournumber[j - 1];
					colournumber[j - 1] = temp;
					temp = rgb[j];
					rgb[j] = rgb[j - 1];
					rgb[j - 1] = temp;
				}
			}
		}
		byte byteimagebuffer[] = new byte[height * width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j = j + 1) {
				if ((realimagebuffer[i * width + j] == rgb[2])
						|| (realimagebuffer[i * width + j] == rgb[3])
						|| (realimagebuffer[i * width + j] == rgb[4])
						|| (realimagebuffer[i * width + j] == rgb[5])) {
					byteimagebuffer[i * width + j] = 1;
				} else {
					byteimagebuffer[i * width + j] = 0;
				}
			}
		}
		return byteimagebuffer;
	}

	/*
	 * //////////////////////////////////////////////////////////////////////////
	 * 主函数
	 */// /////////////////////////////////////////////////////////////////////////
	public String parse(byte imagebuffer[], int width, int height) {
		int i = 0;
		int j = 0;
		int perline = width * 3;
		if ((perline % 4) != 0) {
			perline = perline + (4 - (perline % 4));

		}
		int imagebufferint[] = new int[perline * height];
		int realimagebuffer[] = new int[width * height];
		byte imagebufferbool[] = new byte[width * height];
		// 将byte转成int,因为没有无符号数
		for (i = 0; i < height * perline; i++) {
			if (imagebuffer[i] < 0) {
				imagebufferint[i] = imagebuffer[i] + 256;
			} else {
				imagebufferint[i] = imagebuffer[i];
			}
		}

		realimagebuffer = rgb2gray(imagebufferint, width, height, perline);
		imagebufferbool = gray2bool(realimagebuffer, width, height, 100);

		// 识别图像过程
		int totalsum[] = new int[width];
		// 列加和
		for (j = 0; j < width; j++)// 初始化
		{
			totalsum[j] = 0;
		}
		for (j = 0; j < width; j++) {
			for (i = 0; i < height; i++) {
				totalsum[j] = totalsum[j] + imagebufferbool[i * width + j];
			}
		}
		// 找列边
		int clside[] = new int[20];
		int prenumber = 0;
		int index = 0;
		for (j = 0; j < width; j++) {
			if ((totalsum[j]) > 0 && (prenumber == 0)) {
				clside[index] = j;
				prenumber = totalsum[j];
				index++;
			} else if ((totalsum[j] == 0) && (prenumber > 0)) {
				clside[index] = j - 1;
				prenumber = totalsum[j];
				index++;
			} else if (totalsum[j] > 0 && j == (width - 1)) {
				clside[index] = j;
			}

		}
		// 找行边
		int alphnumber = ((index + 1) / 2);
		index = 0;
		int rowside[] = new int[20];
		int totalrowsum[] = new int[height];
		for (j = 0; j < height; j++)// 初始化
		{
			totalrowsum[j] = 0;
		}
		for (int y = 0; y < alphnumber; y++) {
			// 加和
			for (i = 0; i < height; i++) {
				for (j = clside[y * 2]; j <= clside[y * 2 + 1]; j++) {
					totalrowsum[i] = totalrowsum[i]
					                             + imagebufferbool[i * width + j];
				}
			}
			// 找行边
			prenumber = 0;
			for (i = 0; i < height; i++) {
				if ((totalrowsum[i]) > 0 && (prenumber == 0)) {
					rowside[index] = i;
					prenumber = totalrowsum[i];
					index++;
				} else if ((totalrowsum[i] == 0) && (prenumber > 0)) {
					rowside[index] = i - 1;
					prenumber = totalrowsum[i];
					index++;
				} else if (totalrowsum[i] > 0 && i == (height - 1)) {
					rowside[index] = i;
				}

			}
			for (i = 0; i < height; i++)// 清零
			{
				totalrowsum[i] = 0;
			}
		}
		// 模板写成文件
		// patternwrite(rowside, clside, width , alphnumber, imagebufferbool);
		// 识别

		int bianchang[] = new int[20];
		for (int y = 0; y < 20; y++) {
			bianchang[y] = 0;
		}
		String str[] = new String[10];
		for (int y = 0; y < 10; y++) {
			str[y] = "e";
		}

		for (int y = 0; y < alphnumber; y++) {
			bianchang[y * 2] = clside[y * 2 + 1] - clside[y * 2] + 1;
			bianchang[y * 2 + 1] = rowside[y * 2 + 1] - rowside[y * 2] + 1;
		}

		for (int y = 0; y < 4; y++) {
			if (bianchang[y * 2] == 8 && bianchang[y * 2 + 1] == 8) {
				str[y] = "0";
			} else if ((bianchang[y * 2] == 4 && bianchang[y * 2 + 1] == 7)
					|| (bianchang[y * 2] == 4 && bianchang[y * 2 + 1] == 8)
					|| (bianchang[y * 2] == 4 && bianchang[y * 2 + 1] == 9)
					|| (bianchang[y * 2] == 6 && bianchang[y * 2 + 1] == 10)) {
				str[y] = "1";
			} else if ((bianchang[y * 2] == 14 && bianchang[y * 2 + 1] == 21)
					|| (bianchang[y * 2] == 18 && bianchang[y * 2 + 1] == 27)) {
				str[y] = "2";
			} else if ((bianchang[y * 2] == 19 && bianchang[y * 2 + 1] == 26)) {
				str[y] = "3";
			} else if ((bianchang[y * 2] == 21 && bianchang[y * 2 + 1] == 27)) {
				str[y] = "4";
			} else if ((bianchang[y * 2] == 8 && bianchang[y * 2 + 1] == 7)) {
				str[y] = "5";
			} else if ((bianchang[y * 2] == 24 && bianchang[y * 2 + 1] == 26)) {
				str[y] = "6";
			} else if ((bianchang[y * 2] == 29 && bianchang[y * 2 + 1] == 26)) {
				str[y] = "7";
			} else if ((bianchang[y * 2] == 16 && bianchang[y * 2 + 1] == 21)
					|| (bianchang[y * 2] == 23 && bianchang[y * 2 + 1] == 27)) {
				str[y] = "8";
			} else if ((bianchang[y * 2] == 24 && bianchang[y * 2 + 1] == 34)) {
				str[y] = "9";
			} else if ((bianchang[y * 2] == 8 && bianchang[y * 2 + 1] == 10)) {
				if (matrixequal(imagebufferbool, patternbuffer, clside,
						rowside, bianchang, y, width, 0)) {
					str[y] = "0";
				} else if (matrixequal(imagebufferbool, patternbuffer, clside,
						rowside, bianchang, y, width, 1)) {
					str[y] = "5";
				}
			} else if ((bianchang[y * 2] == 15 && bianchang[y * 2 + 1] == 21)) {
				if (matrixequal(imagebufferbool, patternbuffer, clside,
						rowside, bianchang, y, width, 2)) {
					str[y] = "3";
				} else if (matrixequal(imagebufferbool, patternbuffer, clside,
						rowside, bianchang, y, width, 3)) {
					str[y] = "4";
				} else if (matrixequal(imagebufferbool, patternbuffer, clside,
						rowside, bianchang, y, width, 4)) {
					str[y] = "6";
				} else if (matrixequal(imagebufferbool, patternbuffer, clside,
						rowside, bianchang, y, width, 5)) {
					str[y] = "7";
				} else if (matrixequal(imagebufferbool, patternbuffer, clside,
						rowside, bianchang, y, width, 6)) {
					str[y] = "9";
				}
			} else if ((bianchang[y * 2] == 8) && (bianchang[y * 2 + 1] == 9)) {
				if (matrixequal(imagebufferbool, patternbuffer, clside,
						rowside, bianchang, y, width, 7)) {
					str[y] = "0";
				} else if (matrixequal(imagebufferbool, patternbuffer, clside,
						rowside, bianchang, y, width, 8)) {
					str[y] = "5";
				}
			}
		}

		return str[0] + str[1] + str[2] + str[3];
	}

}
