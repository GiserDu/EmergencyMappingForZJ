package com.zz.bglayer;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
/** 
 * Raster对象属性结构类，JRaster由此类组成
 * @author L J
 * @version 1.0 
 */
public class JRasterStruct {
	//private Rectangle2D.Double rect;
	private Point2D.Double point;
	private int [] rasSize;
	private double [] affineArray;
	private String rasName;
	private BufferedImage bImage; 
	
	private AffineTransform ATRaster;
	public void JRasterStruct(){
		//rect = null;
		point = null;
		rasSize = new int [2];
		affineArray = new double [4];
		rasName = null;
		bImage = null;
		ATRaster = null;
	}
	/*public Rectangle2D.Double getRect() {
		return rect;
	}
	public void setRect(Rectangle2D.Double rect) {
		this.rect = rect;
	}*/
	public Point2D.Double getPoint() {
		return point;
	}
	public void setPoint(Point2D.Double point) {
		this.point = point;
	}
	public int[] getRasSize() {
		return rasSize;
	}
	public void setRasSize(int[] rasSize) {
		this.rasSize = rasSize;
	}
	public double[] getAffineArray() {
		return affineArray;
	}
	public void setAffineArray(double[] affineArray) {
		this.affineArray = affineArray;
	}
	public String getRasName() {
		return rasName;
	}
	public void setRasName(String rasName) {
		this.rasName = rasName;
	}
	public BufferedImage getBImage() {
		return bImage;
	}
	public void setBImage(BufferedImage image) {
		bImage = image;
	}
	public AffineTransform getATRaster() {
		return ATRaster;
	}
	public void setATRaster(AffineTransform raster) {
		ATRaster = raster;
	}

}
