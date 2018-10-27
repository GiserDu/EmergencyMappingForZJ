package com.zz.bglayer;
import java.awt.*;
//import java.util.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
/** 
 * Polygon对象属性结构类，JPolygon，JComPolygon均由此类组成
 * @author L J
 * @version 1.0 
 */
public class JPolygonStruct {
	private int nLineStyleCount;
	private float fLineWidth;
	private float []lineStyle;
	private Color lLineColor;
	private Color lFillColor;
	private boolean bIsFill;
	private boolean bHasBoundCol;
	
	private GeneralPath path;
	BasicStroke bs;
	//private int nPtCount;
	//private Rectangle2D.Double rect;
	//private boolean bIsBezier;
	//private Vector<Point2D.Double> vecPoints;
	private int segNum;
//	private JSegment []segment;
	public int getSegNum() {
		return segNum;
	}
	public void setSegNum(int segNum) {
		this.segNum = segNum;
	}
	public JPolygonStruct()
	{  nLineStyleCount=0;
	   fLineWidth=-1;
	   lLineColor=null;
	   lFillColor=null;
	   bIsFill=false;
	   bHasBoundCol=false;
	   //nPtCount=-1;
	   //rect=null;
	   //bIsBezier=false;
	   //vecPoints = null;
//	   segNum =0;
//	   segment = new JSegment[segNum];
	   lineStyle = new float[nLineStyleCount];
	   
		path = null;
		bs = null;
	   
	   
	}
	/*public JPolygonStruct(JPolygonStruct polygonStrct){
		this.fLineWidth = polygonStrct.getFLineWidth();
		this.bHasBoundCol = polygonStrct.isbHasBoundCol();
		this.lLineColor = polygonStrct.getLLineColor();
		this.bIsFill = polygonStrct.isBIsFill();
		this.lFillColor = polygonStrct.getLFillColor();
		this.nLineStyleCount = polygonStrct.getNLineStyleCount();
	}*/
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
	/*public int getNPtCount() {
		return nPtCount;
	}
	public void setNPtCount(int ptCount) {
		nPtCount = ptCount;
	}*/

	/*public boolean isBIsBezier() {
		return bIsBezier;
	}
	public void setBIsBezier(boolean isBezier) {
		bIsBezier = isBezier;
	}*/
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
	/*public Vector<Point2D.Double> getVecPoints() {
		return vecPoints;
	}
	public void setVecPoints(Vector<Point2D.Double> vecPoints) {
		this.vecPoints = vecPoints;
	}*/
	/*public JSegment getSegment(int i) {
		return segment[i];
	}
	public void setSegment(JSegment segments,int i) {
	    segment[i] = new JSegment();
	    segment[i] = segments;
	}*/
	public float[] getLineStyle() {
		return lineStyle;
	}
	public void setLineStyle(float[] lineStyle) {
		this.lineStyle = lineStyle;
	}
//	public JSegment[] getSegment() {
//		return segment;
//	}
//	public void setSegment(JSegment[] segment) {
//		this.segment = segment;
//	}
	/*public Rectangle2D.Double getRect() {
		return rect;
	}
	public void setRect(Rectangle2D.Double rect) {
		this.rect = rect;
	}*/
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
