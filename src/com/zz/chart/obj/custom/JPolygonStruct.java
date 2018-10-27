package com.zz.chart.obj.custom;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.GeneralPath;

/** 
 * Polygon对象属性结构类，JPolygon由此类组成
 * @author L.J
 * @version 1.0 
 */
public class JPolygonStruct {
	private int nLineStyleCount;//线样式(虚线,直线) 0为直线, 非0为虚线
	private float fLineWidth;//线宽
	private float[] lineStyle;//虚线类型
	private Color lLineColor;//边界颜色值
	private Color lFillColor;//填充颜色值
	private boolean bIsFill;//填充颜色(bool)
	private boolean bHasBoundCol;//边界颜色(bool)
	
	private GeneralPath path;//绘制路径
	private BasicStroke bs;//线粗
	private int segNum;//复杂多边形中会用到, 一个复杂多边形所含部分数
	
	public JPolygonStruct()
	{  nLineStyleCount=0;
	   fLineWidth=-1;
	   lineStyle = new float[nLineStyleCount];	
	   lLineColor=null;
	   lFillColor=null;
	   bIsFill=false;
	   bHasBoundCol=false;
	   
	   path = new GeneralPath();
	   bs = null;
	   segNum = -1;
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
	public Color getLFillColor() {
		return lFillColor;
	}
	public void setLFillColor(Color fillColor) {
		lFillColor = fillColor;
	}
	public float[] getLineStyle() {
		return lineStyle;
	}
	public void setLineStyle(float[] lineStyle) {
		this.lineStyle = lineStyle;
	}
	public boolean isBIsFill() {
		return bIsFill;
	}
	public void setBIsFill(boolean isFill) {
		bIsFill = isFill;
	}
	public boolean isBHasBoundCol() {
		return bHasBoundCol;
	}
	public void setBHasBoundCol(boolean hasBound) {
		bHasBoundCol = hasBound;
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
	public int getSegNum() {
		return segNum;
	}
	public void setSegNum(int segNum) {
		this.segNum = segNum;
	}

}
