package com.mjp.image;

public class ImageParam {
	
	/**
	 * 文件服务器文件名
	 */
	private String imgName;
	
	/**
	 * 文件服务器路径名
	 */
	private String imgPath;
	
	/**
	 * 
	 */
	private int x ;
	/**
	 * 
	 */
	private int y;
	/**
	 * 图片宽度
	 */
	private int w;
	/**
	 * 图片高度
	 */
	private int h;
	/**
	 * 图片旋转角度
	 */
	private int r;
	/**
	 * 
	 */
	private int cw;
	/**
	 * 
	 */
	private int ch;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	public int getCw() {
		return cw;
	}
	public void setCw(int cw) {
		this.cw = cw;
	}
	public int getCh() {
		return ch;
	}
	public void setCh(int ch) {
		this.ch = ch;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		StringBuffer buf = new StringBuffer();
		buf.append("x:" + x +"y:" + y + "w:" + w + "h:" + h + "cw:" + cw + "ch:" + ch);
		buf.append("imgName:" + imgName + "\timgPath:" + imgPath);
		return buf.toString();
	}

}
