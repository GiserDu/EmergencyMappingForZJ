package com.zz.bglayer;
import java.util.*;
import java.awt.geom.*;
/** 
 * Segment对象属性结构类，此类为最基本的点集的抽象
 * @author L J
 * @version 1.0 
 */
public class JSegment {
	private boolean bIsBezier;
	private ArrayList<Point2D.Double> Points;
	private int nPtCount;
	public JSegment()
	{   Points=null;
		bIsBezier=false;
		nPtCount = 0;
	}
	public boolean isBIsBezier() {
		return bIsBezier;
	}
	public void setBIsBezier(boolean isBezier) {
		bIsBezier = isBezier;
	}
	public ArrayList<Point2D.Double> getPoints() {
		return Points;
	}
	public void setPoints(ArrayList<Point2D.Double> points) {
		Points = points;
	}
	public int getNPtCount() {
		return nPtCount;
	}
	public void setNPtCount(int ptCount) {
		nPtCount = ptCount;
	}
}
