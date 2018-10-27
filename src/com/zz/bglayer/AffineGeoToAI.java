package com.zz.bglayer;

import java.awt.geom.*;
/** 
 * 坐标变换类,从地理坐标到AI坐标
 * @author L J
 * @version 1.0 
 */
public class AffineGeoToAI {
	private Point2D.Double[] originalPoints;
	private Point2D.Double[] destPoints;
	private double []affPara;
	public AffineGeoToAI(){
		originalPoints = null;
		destPoints = null;
		affPara = null;
	}
	/** 
	 * 带参数构造函数,初始化originalPoints，destPoints,计算affPara变换参数
	 * @param originalPoints 
	 * @param destPoints
	 * @since 1.0 
	 */ 
	public AffineGeoToAI(Point2D.Double[] originalPoints,Point2D.Double[] destPoints){
		this.originalPoints = originalPoints;
		this.destPoints = destPoints;
		affPara = new double[6];
		double temp;
		temp=(originalPoints[0].getX()-originalPoints[1].getX())*(originalPoints[1].getY()-originalPoints[2].getY())
			-(originalPoints[1].getX()-originalPoints[2].getX())*(originalPoints[0].getY()-originalPoints[1].getY());
		affPara[0]=((originalPoints[1].getY()-originalPoints[2].getY())*(destPoints[0].getX()-destPoints[1].getX())-
			(originalPoints[0].getY()-originalPoints[1].getY())*(destPoints[1].getX()-destPoints[2].getX()))/temp;
		affPara[1]=(-(originalPoints[1].getX()-originalPoints[2].getX())*(destPoints[0].getX()-destPoints[1].getX())+
			(originalPoints[0].getX()-originalPoints[1].getX())*(destPoints[1].getX()-destPoints[2].getX()))/temp;
		affPara[2]=destPoints[0].getX()-affPara[0]*originalPoints[0].getX()-affPara[1]*originalPoints[0].getY();
		
		affPara[3]=((originalPoints[1].getY()-originalPoints[2].getY())*(destPoints[0].getY()-destPoints[1].getY())-
			(originalPoints[0].getY()-originalPoints[1].getY())*(destPoints[1].getY()-destPoints[2].getY()))/temp;
		affPara[4]=(-(originalPoints[1].getX()-originalPoints[2].getX())*(destPoints[0].getY()-destPoints[1].getY())+
			(originalPoints[0].getX()-originalPoints[1].getX())*(destPoints[1].getY()-destPoints[2].getY()))/temp;
		affPara[5]=destPoints[0].getY()-affPara[3]*originalPoints[0].getX()-affPara[4]*originalPoints[0].getY();
	}
	public double[] getAffPara() {
		return affPara;
	}
	public void setAffPara(double[] affPara) {
		this.affPara = affPara;
	}
    /**
     * 将范围矩形由地理坐标转换为AI坐标
     * @param WC
     * @return Point2D.Double
     * @throws 
     * @since 1.0
     */
	public Rectangle2D.Double GetAIWC(Rectangle2D.Double WC){
		Point2D.Double pt1 = new Point2D.Double(WC.getX(), WC.getY());
		Point2D.Double pt2 = new Point2D.Double(WC.getX() + WC.getWidth(), WC
				.getY()
				+ WC.getHeight());
		Point2D.Double pt3 = new Point2D.Double();
		Point2D.Double pt4 = new Point2D.Double();
		// Rectangle2D.Double AIWC = new Rectangle2D.Double();
		pt3.x = affPara[0] * pt1.x + affPara[1] * pt1.y + affPara[2];
		pt3.y = affPara[3] * pt1.x + affPara[4] * pt1.y + affPara[5];
		pt4.x = affPara[0] * pt2.x + affPara[1] * pt2.y + affPara[2];
		pt4.y = affPara[3] * pt2.x + affPara[4] * pt2.y + affPara[5];

		Rectangle2D.Double AIWC = new Rectangle2D.Double(pt3.x, pt3.y, pt4.x
				- pt3.x, pt4.y - pt3.y);
		return AIWC;	
	}

}
