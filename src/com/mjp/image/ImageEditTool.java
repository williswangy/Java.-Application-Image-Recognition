package com.mjp.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import Acme.JPM.Encoders.GifEncoder;

/**
 * 图片编辑类
 * 
 * @author ce
 * 
 */
public class ImageEditTool {

	private ImageEditTool() {
	}

	private static final ImageEditTool instance = new ImageEditTool();

	/**
	 * 获取实例
	 * 
	 * @return
	 */
	public static final ImageEditTool getInstance() {
		return instance;
	}

	/**
	 * 图片编辑
	 * 
	 * @param imgParam
	 * @throws IOException
	 */
	public boolean editImage(ImageParam imgParam) {
		if (imgParam == null) {
			return false;
		}
		try {
			// 服务器文件全名
			String fullFileName = imgParam.getImgPath() + imgParam.getImgName();
			System.out.println("server imgName:" + fullFileName);
			File f = new File(fullFileName);
			if (!f.exists()) {
				return false;
			}
			BufferedImage img = ImageIO.read(f);
			if ((imgParam.getW() != 0 && imgParam.getH() != 0)
					&& (imgParam.getW() != img.getWidth() || imgParam.getH() != img
							.getHeight())) {
				AffineTransform t = AffineTransform.getScaleInstance(imgParam
						.getW()
						/ img.getWidth(), imgParam.getH() / img.getHeight());
				AffineTransformOp sOp = new AffineTransformOp(t, null);
				img = sOp.filter(img, null);
			}
			if (imgParam.getR() != 0
					&& (imgParam.getR() > -360 && imgParam.getR() < 360)) {
				img = rotateImage(img, imgParam.getR());
			}
			if (imgParam.getX() >= 0 && imgParam.getY() >= 0
					&& imgParam.getCw() > 0 && imgParam.getCh() > 0) {
				img = cropImage(img, imgParam.getX(), imgParam.getY(), imgParam
						.getCw(), imgParam.getCh());
			}
			String ext = "jpg";
			if (imgParam.getImgName().lastIndexOf('.') != -1) {
				ext = imgParam.getImgName().substring(
						imgParam.getImgName().lastIndexOf('.') + 1);
			}
			if (ext.equalsIgnoreCase("gif")) {
				FileOutputStream out = new FileOutputStream(f);
				try {
					GifEncoder encoder = new GifEncoder(img, out);
					encoder.encode();
				} finally {
					out.close();
				}
			} else {
				ImageIO.write(img, ext, f);
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	private BufferedImage cropImage(BufferedImage img, int x, int y, int w,
			int h) {
		return img.getSubimage(x, y, w, h);
	}

	private BufferedImage rotateImage(BufferedImage img, int r) {
		AffineTransform transform = AffineTransform.getRotateInstance(Math
				.toRadians(r), img.getWidth() / 2, img.getHeight() / 2);
		transform.preConcatenate(findTranslation(transform, img));

		int t = (r % 90 == 0) ? AffineTransformOp.TYPE_NEAREST_NEIGHBOR
				: AffineTransformOp.TYPE_BICUBIC;
		AffineTransformOp op = new AffineTransformOp(transform, t);

		BufferedImage filteredImage = op.filter(img, null);

		filteredImage = new BufferedImage(filteredImage.getWidth(),
				filteredImage.getHeight(), img.getType());
		Graphics g = filteredImage.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, filteredImage.getWidth(), filteredImage.getHeight());
		op.filter(img, filteredImage);

		return filteredImage;
	}

	private AffineTransform findTranslation(AffineTransform at, BufferedImage bi) {
		Point2D p2din, p2dout;

		p2din = new Point2D.Double(0.0, 0.0);
		p2dout = at.transform(p2din, null);
		double ytrans = p2dout.getY();

		p2din = new Point2D.Double(0, bi.getHeight());
		p2dout = at.transform(p2din, null);
		double xtrans = p2dout.getX();

		AffineTransform tat = new AffineTransform();
		tat.translate(-xtrans, -ytrans);
		return tat;
	}

}
