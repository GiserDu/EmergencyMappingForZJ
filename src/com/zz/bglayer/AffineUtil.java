package com.zz.bglayer;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class AffineUtil {
	public AffineUtil() {

	}
	public static Point2D.Double Affine(Rectangle2D.Double WC, Rectangle2D.Double DC,Point2D.Double pt) { 
		double width = DC.getWidth();
		double height = DC.getHeight();
		double scale1 = WC.getWidth() / width;
		double scale2 = WC.getHeight() / height;
		double scale = scale1 < scale2 ? scale2 : scale1;//scale作为除数，因此取大的
		Point2D.Double pt1 = new Point2D.Double();
	
		double x = (pt.getX() - WC.getX()) / scale ;
		double y = (pt.getY() - WC.getY()) / scale;
		x = x + (DC.getWidth() - WC.getWidth() / scale) / 2;
		y = y + (DC.getHeight() - WC.getHeight() / scale) / 2;
		y = DC.getHeight()-y;//图形翻转
		pt1.setLocation(x,y);
		return pt1;
	}
	public static double CalSymbolLength(Rectangle2D.Double WC, Rectangle2D.Double DC,double AILength){
		double DCLength;
		double width = DC.getWidth();
		double height = DC.getHeight();
		double scale1 = WC.getWidth() / width;
		double scale2 = WC.getHeight() / height;
		double scale = scale1 < scale2 ? scale2 : scale1;//scale作为除数，因此取大的
		scale = scale*1.1;
		DCLength = AILength/scale;
		return DCLength;
	}

}
