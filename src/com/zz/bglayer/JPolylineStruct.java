package com.zz.bglayer;
import java.awt.*;
import java.awt.geom.*;
/** 
 * Polyline对象属性结构类，JPolyline由此类组成
 * @author L J
 * @version 1.0 
 */
public class JPolylineStruct {
	
	private int nLineStyleCount;
	private float fLineWidth;
	//private float* fLineStyle;
	private Color lLineColor;
	//private int nPtCount;
	//private Rectangle2D.Double rect;
	private float []lineStyle;
	private GeneralPath path;
	BasicStroke bs;
	//private JSegment segment;
	//private boolean bIsBezier;
	
	public JPolylineStruct()
	{   nLineStyleCount=0;
		fLineWidth=-1;
		lLineColor=null;
		//nPtCount=-1;
		//rect=null;
		lineStyle = new float[nLineStyleCount];
		path = null;
		bs = null;
		//segment = new JSegment();
		//bIsBezier=false;
	}
	public int getNLineStyleCount() {
		return nLineStyleCount;
	}
	public void setNLineStyleCount(int lineStyleCount) {
		nLineStyleCount = lineStyleCount;
	}
	public float getFLineWidth() {
		return fLineWidth;
	}
	public void setFLineWidth(float lineWidth) {
		fLineWidth = lineWidth;
	}
	public Color getLLineColor() {
		return lLineColor;
	}
	public void setLLineColor(Color lineColor) {
		lLineColor = lineColor;
	}
	/*public int getNPtCount() {
		return nPtCount;
	}
	public void setNPtCount(int ptCount) {
		nPtCount = ptCount;
	}*/
	/*public Rectangle2D.Double getRect() {
		return rect;
	}
	public void setRect(Rectangle2D.Double rect) {
		this.rect = rect;
	}*/
	/*public boolean isBIsBezier() {
		return bIsBezier;
	}
	public void setBIsBezier(boolean isBezier) {
		bIsBezier = isBezier;
	}*/
	public float[] getLineStyle() {
		return lineStyle;
	}
	public void setLineStyle(float[] lineStyle) {
		this.lineStyle = lineStyle;
	}
//	public JSegment getSegment() {
//		return segment;
//	}
//	public void setSegment(JSegment segment) {
//		this.segment = segment;
//	}
	public GeneralPath getPath() {
		return path;
	}
	public void setPath(GeneralPath path) {
		this.path = path;
	}
	public BasicStroke getBs() {
		return bs;
	}
	public void setBs(BasicStroke bs) {
		this.bs = bs;
	}
	
	
}
