package com.zz.bglayer;
import java.awt.geom.*;
/** 
 * 
 * @author L J
 * @version 创建时间：May 31, 2009 9:37:42 PM 
 */
public class CoorsTrans {
	double a1;
	double b1;
	double c1;
	double a2;
	double b2;
	double c2;
	Point2D.Double[] originalPoints;
	Point2D.Double[] destPoints;

	public CoorsTrans() {
		a1 = 0;
		b1 = 0;
		c1 = 0;
		a2 = 0;
		b2 = 0;
		c2 = 0;
		originalPoints = new Point2D.Double[3];
		destPoints = new Point2D.Double[3];
	}

	public Point2D.Double GeoToCorel(Point2D.Double Pt) {
		
		originalPoints[0] = new Point2D.Double();
		originalPoints[1] = new Point2D.Double();
		originalPoints[2] = new Point2D.Double();
		destPoints[0] = new Point2D.Double();
		destPoints[1] = new Point2D.Double();
		destPoints[2] = new Point2D.Double();
		
   		originalPoints[0].setLocation(88915.515,46873.030);//左边的控制点
   		originalPoints[1].setLocation(101531.337,22243.435);
   		originalPoints[2].setLocation(161597.047,32388.461);
		
   		destPoints[0].setLocation(-286291,676219);//右边的待计算点
   		destPoints[1].setLocation(-109886,330658);
   		destPoints[2].setLocation(732925,472680);
		
		double temp;
		temp=(originalPoints[0].getX()-originalPoints[1].getX())*(originalPoints[1].getY()-originalPoints[2].getY())
			-(originalPoints[1].getX()-originalPoints[2].getX())*(originalPoints[0].getY()-originalPoints[1].getY());
		a1=((originalPoints[1].getY()-originalPoints[2].getY())*(destPoints[0].getX()-destPoints[1].getX())-
			(originalPoints[0].getY()-originalPoints[1].getY())*(destPoints[1].getX()-destPoints[2].getX()))/temp;
		b1=(-(originalPoints[1].getX()-originalPoints[2].getX())*(destPoints[0].getX()-destPoints[1].getX())+
			(originalPoints[0].getX()-originalPoints[1].getX())*(destPoints[1].getX()-destPoints[2].getX()))/temp;
		c1=destPoints[0].getX()-a1*originalPoints[0].getX()-b1*originalPoints[0].getY();
		
		a2=((originalPoints[1].getY()-originalPoints[2].getY())*(destPoints[0].getY()-destPoints[1].getY())-
			(originalPoints[0].getY()-originalPoints[1].getY())*(destPoints[1].getY()-destPoints[2].getY()))/temp;
		b2=(-(originalPoints[1].getX()-originalPoints[2].getX())*(destPoints[0].getY()-destPoints[1].getY())+
			(originalPoints[0].getX()-originalPoints[1].getX())*(destPoints[1].getY()-destPoints[2].getY()))/temp;
		c2=destPoints[0].getY()-a2*originalPoints[0].getX()-b2*originalPoints[0].getY();
		
		double ptX;
		double ptY;
		Point2D.Double Pt1 = new Point2D.Double();
		ptX = a1*Pt.getX() + b1*Pt.getY() + c1;
		ptY = a2*Pt.getX() + b2*Pt.getY() + c2;
		Pt1.setLocation(ptX, ptY);
		return Pt1;

	}
	
	public Point2D.Double CorelToGeo(Point2D.Double Pt) {
		
		originalPoints[0] = new Point2D.Double();
		originalPoints[1] = new Point2D.Double();
		originalPoints[2] = new Point2D.Double();
		destPoints[0] = new Point2D.Double();
		destPoints[1] = new Point2D.Double();
		destPoints[2] = new Point2D.Double();
		
   	    destPoints[0].setLocation(88915.515,46873.030);
   	    destPoints[1].setLocation(101531.337,22243.435);
   	    destPoints[2].setLocation(161597.047,32388.461);
		
   	    originalPoints[0].setLocation(-286291,676219);
   	    originalPoints[1].setLocation(-109886,330658);
   	    originalPoints[2].setLocation(732925,472680);

		
		double temp;
		temp=(originalPoints[0].getX()-originalPoints[1].getX())*(originalPoints[1].getY()-originalPoints[2].getY())
			-(originalPoints[1].getX()-originalPoints[2].getX())*(originalPoints[0].getY()-originalPoints[1].getY());
		a1=((originalPoints[1].getY()-originalPoints[2].getY())*(destPoints[0].getX()-destPoints[1].getX())-
			(originalPoints[0].getY()-originalPoints[1].getY())*(destPoints[1].getX()-destPoints[2].getX()))/temp;
		b1=(-(originalPoints[1].getX()-originalPoints[2].getX())*(destPoints[0].getX()-destPoints[1].getX())+
			(originalPoints[0].getX()-originalPoints[1].getX())*(destPoints[1].getX()-destPoints[2].getX()))/temp;
		c1=destPoints[0].getX()-a1*originalPoints[0].getX()-b1*originalPoints[0].getY();
		
		a2=((originalPoints[1].getY()-originalPoints[2].getY())*(destPoints[0].getY()-destPoints[1].getY())-
			(originalPoints[0].getY()-originalPoints[1].getY())*(destPoints[1].getY()-destPoints[2].getY()))/temp;
		b2=(-(originalPoints[1].getX()-originalPoints[2].getX())*(destPoints[0].getY()-destPoints[1].getY())+
			(originalPoints[0].getX()-originalPoints[1].getX())*(destPoints[1].getY()-destPoints[2].getY()))/temp;
		c2=destPoints[0].getY()-a2*originalPoints[0].getX()-b2*originalPoints[0].getY();
		
		double ptX;
		double ptY;
		Point2D.Double Pt1 = new Point2D.Double();
		ptX = a1*Pt.getX() + b1*Pt.getY() + c1;
		ptY = a2*Pt.getX() + b2*Pt.getY() + c2;
		Pt1.setLocation(ptX, ptY);
		return Pt1;

	}
	

	
}
