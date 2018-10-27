package com.zz.bglayer;

import java.awt.geom.GeneralPath;
import java.awt.geom.*;
import java.util.ArrayList;
/** 
 * line直线段底层类
 * @author L J
 * @version 1.0 
 */
public class LinePath {
	private ArrayList<Point2D.Double> Pt;
	private GeneralPath path;
	
	public GeneralPath getPath() {
		return path;
	}

	public void setPath(GeneralPath path) {
		this.path = path;
	}

	public LinePath(){
		Pt = new ArrayList<Point2D.Double>();
		path = null;
	}
	/** 
	 * 带参数构造函数,初始化path，af,pt,生成linePath路径
	 * @param path
	 * @param af
	 * @param Pt
	 * @since 1.0 
	 */ 
	public LinePath(GeneralPath path,ArrayList<Point2D.Double> Pt){
		Point2D.Double pt1 = new Point2D.Double();
		Point2D.Double pt2 = new Point2D.Double();
		int size = Pt.size();
		pt1.setLocation((Point2D.Double) Pt.get(0));
		path.moveTo((float) pt1.getX(), (float) pt1.getY());
		for (int j = 1; j < size; j++) {// 第一个点使用moveTo，第二个点使用lineTo
			pt2.setLocation((Point2D.Double) Pt.get(j));
			path.lineTo((float) pt2.getX(), (float) pt2.getY());
		}
		this.path = path;
	}
	
	
	
	public ArrayList<Point2D.Double> getPt() {
		return Pt;
	}

	public void setPt(ArrayList<Point2D.Double> pt) {
		Pt = pt;
	}

}
