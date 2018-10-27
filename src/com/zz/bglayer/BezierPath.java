package com.zz.bglayer;

import java.awt.geom.*;
import java.util.*;
/** 
 * Bezier曲线底层类
 * @author L J
 * @version 1.0 
 */
public class BezierPath {
	//private Graphics2D g2D;
	//private Affine bezierAf;
	//private JPolylineStruct bezierLineStruct;
	private ArrayList<Point2D.Double> Pt;
	private GeneralPath path;
	
	public GeneralPath getPath() {
		return path;
	}

	public void setPath(GeneralPath path) {
		this.path = path;
	}

	public BezierPath(){
		//Graphics2D g2D = null;
		//bezierAf = new Affine();
		//bezierLineStruct = new JPolylineStruct();
		Pt = new ArrayList<Point2D.Double>();
		path = null;
	}
	/** 
	 * 带参数构造函数,初始化path，af,pt,生成Bezier路径
	 * @param path
	 * @param af
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
	
	
	/*public Bezier(Graphics2D g2D,Affine af,JPolygonStruct tagPolygon,ArrayList<Point2D.Double> Pt){
	    //构造函数直接画图
			Point2D.Double pt1 = new Point2D.Double();
			Point2D.Double pt2 = new Point2D.Double();
			Point2D.Double pt3 = new Point2D.Double();
			Point2D.Double pt4 = new Point2D.Double();
			int size = tagPolygon.getNPtCount();
			GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
					size);
			
			pt1.setLocation((Point2D.Double) Pt.get(0));
			pt1 = af.TransPoint(pt1);
			path.moveTo((float) pt1.getX(), (float) pt1.getY());
			for (int i = 0; i < size - 3; i = i + 3) {
				pt2.setLocation((Point2D.Double) Pt.get(i + 1));
				pt2 = af.TransPoint(pt2);

				pt3.setLocation((Point2D.Double) Pt.get(i + 2));
				pt3 = af.TransPoint(pt3);

				pt4.setLocation((Point2D.Double) Pt.get(i + 3));
				pt4 = af.TransPoint(pt4);

				path.curveTo((float) pt2.getX(), (float) pt2.getY(),
						(float) pt3.getX(), (float) pt3.getY(), (float) pt4
								.getX(), (float) pt4.getY());
			}
			g2D.draw(path);
			this.path = path;
		}*/
	

	/*public Affine getBezierAf() {
		return bezierAf;
	}

	public void setBezierAf(Affine bezierAf) {
		this.bezierAf = bezierAf;
	}

	public JPolylineStruct getBezierLineStruct() {
		return bezierLineStruct;
	}

	public void setBezierLineStruct(JPolylineStruct bezierLineStruct) {
		this.bezierLineStruct = bezierLineStruct;
	}
	

	public Graphics2D getG2D() {
		return g2D;
	}

	public void setG2D(Graphics2D g2d) {
		g2D = g2d;
	}*/
	
	public ArrayList<Point2D.Double> getPt() {
		return Pt;
	}

	public void setPt(ArrayList<Point2D.Double> pt) {
		Pt = pt;
	}

}
