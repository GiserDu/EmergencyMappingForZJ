package com.zz.chart.obj.custom;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.GeneralPath;

/** 
 * Polyline对象属性结构类，JPolyline由此类组成
 * @author L J
 * @version 1.0 
 */
public class JPolylineStruct {
	
	private int nLineStyleCount;
	private float fLineWidth;
	private Color lLineColor;
	private float []lineStyle;
	private GeneralPath path;//绘制路径
	private BasicStroke bs;
	
	public JPolylineStruct()
	{   
		nLineStyleCount=0;
		fLineWidth=-1;
		lLineColor=null;
		lineStyle = new float[nLineStyleCount];
		path = new GeneralPath();
		bs = null;
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
	public float[] getLineStyle() {
		return lineStyle;
	}
	public void setLineStyle(float[] lineStyle) {
		this.lineStyle = lineStyle;
	}
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
