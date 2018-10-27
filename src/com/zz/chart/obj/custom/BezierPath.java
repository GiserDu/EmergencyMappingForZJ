package com.zz.chart.obj.custom;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/** 
 * Bezier曲线底层类
 * @author L J
 * @version 1.0 
 */
public class BezierPath {
	
	private ArrayList<Point2D.Double> Pt;
	private GeneralPath path;
	
	public GeneralPath getPath() {
		return path;
	}

	public void setPath(GeneralPath path) {
		this.path = path;
	}

	public BezierPath(){
		Pt = new ArrayList<Point2D.Double>();
		path = null;
	}
	/** 
	 * 带参数构造函数,初始化path，af,pt,生成Bezier路径
	 * @param path
	 * @param Pt
	 * @since 1.0 
	 */ 
	public BezierPath(GeneralPath path,ArrayList<Point2D.Double> Pt){
    //构造函数直接画图
		Point2D.Double pt1 = new Point2D.Double();
		Point2D.Double pt2 = new Point2D.Double();
		Point2D.Double pt3 = new Point2D.Double();
		Point2D.Double pt4 = new Point2D.Double();
		int size = Pt.size();	
		pt1.setLocation((Point2D.Double) Pt.get(0));
		path.moveTo((float) pt1.getX(), (float) pt1.getY());
		for (int i = 0; i < size - 3; i = i + 3) {
			pt2.setLocation((Point2D.Double) Pt.get(i + 1));

			pt3.setLocation((Point2D.Double) Pt.get(i + 2));

			pt4.setLocation((Point2D.Double) Pt.get(i + 3));

			path.curveTo((float) pt2.getX(), (float) pt2.getY(),
					(float) pt3.getX(), (float) pt3.getY(), (float) pt4
							.getX(), (float) pt4.getY());
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