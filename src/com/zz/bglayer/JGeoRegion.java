package com.zz.bglayer;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
/** 
 * GeoRegion地理目标对象读取数据绘图类，主要用于分级底图
 * @author L J
 * @version 1.0 
 */
public class JGeoRegion {
	private String regName;
	private String regGeoCode;
	private Rectangle2D.Double rect;
	private JSegment regSegment;
	//private Color fillColor;
	private Boolean boolSetColor;
	
	private GeneralPath path;

	public JGeoRegion() {
		regName = "";
		regGeoCode = "";
		rect = null;
		regSegment = null;
		//fillColor = null;
		boolSetColor = false;
		
		path = null;
	}

	/*public char GetType() {
		return 0;
	}*/

	public void ReadObjectInfoText() {
	}

    /**
     * 读取GeoRegion数据
     * @param dis 文件头指针
     * @return void
     * @throws 
     * @since 1.0
     */
	public void ReadObjectInfoBin(DataInputStream dis) {
		try {
			regName = dis.readUTF();
			regGeoCode = dis.readUTF();
			double x1 = dis.readDouble();
			double y1 = dis.readDouble();
			double x2 = dis.readDouble();
			double y2 = dis.readDouble();
			rect = new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
            //this.setRect(rect);
			regSegment = new JSegment();
			regSegment.setBIsBezier(dis.readBoolean());
			regSegment.setNPtCount(dis.readInt());
			ArrayList<Point2D.Double> Points = new ArrayList<Point2D.Double>();
			for (int i = 0; i < regSegment.getNPtCount(); i++) {
				Point2D.Double pt = new Point2D.Double();
				double x = dis.readDouble();
				double y = dis.readDouble();
				pt.setLocation(x, y);
				Points.add(pt);
			}
			regSegment.setPoints(Points);
			
			int size = regSegment.getNPtCount();

			path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, size);
			//Affine af = new Affine(wcRect, dcRect);
//			ArrayList<Point2D.Double> Points = regSegment.getPoints();
			if (regSegment.isBIsBezier() == false) {// 不是Bezier的情况
				LinePath bz = new LinePath(path, Points);
				path = bz.getPath();
			} else {
				BezierPath bz = new BezierPath(path, Points);
				path = bz.getPath();
			}

			/*int[] rgb = Util.GetRGB(15919339);
			Color color = new Color(rgb[2], rgb[1], rgb[0]);
			fillColor = color;*/
		} catch (IOException e) {

		}
	}

    /**
     * GeoRegion绘图函数
     * @param g2D Java绘图对象
     * @param affTrans 仿射变换对象
     * @return void
     * @throws 
     * @since 1.0
     */
	public void Draw(Graphics2D g2D, Rectangle2D.Double wcRect,
			Rectangle2D.Double dcRect, Affine affTrans,Color fillColor) {
//			int size = regSegment.getNPtCount();
//
//			GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, size);
//			//Affine af = new Affine(wcRect, dcRect);
//			ArrayList<Point2D.Double> Points = regSegment.getPoints();
//			if (regSegment.isBIsBezier() == false) {// 不是Bezier的情况
//				LinePath bz = new LinePath(path, affTrans, Points);
//				path = bz.getPath();
//			} else {
//				BezierPath bz = new BezierPath(path, affTrans, Points);
//				path = bz.getPath();
//			}
//
//			/*g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//					RenderingHints.VALUE_ANTIALIAS_ON);*/

			g2D.setColor(fillColor);
			g2D.fill(path);
		

	}

	public String getRegName() {
		return regName;
	}

	public void setRegName(String regName) {
		this.regName = regName;
	}

	/*public Rectangle2D.Double getRect() {
		return rect;
	}

	public void setRect(Rectangle2D.Double rect) {
		this.rect = rect;
	}*/

	public JSegment getRegSegment() {
		return regSegment;
	}

	public void setRegSegment(JSegment regSegment) {
		this.regSegment = regSegment;
	}

	/*public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}*/

	public Boolean getBoolSetColor() {
		return boolSetColor;
	}

	public void setBoolSetColor(Boolean boolSetColor) {
		this.boolSetColor = boolSetColor;
	}

	public Rectangle2D.Double getRect() {
		return rect;
	}

	public void setRect(Rectangle2D.Double rect) {
		this.rect = rect;
	}

	public String getRegGeoCode() {
		return regGeoCode;
	}

	public void setRegGeoCode(String regGeoCode) {
		this.regGeoCode = regGeoCode;
	}

	public GeneralPath getPath() {
		return path;
	}

	public void setPath(GeneralPath path) {
		this.path = path;
	}

}
