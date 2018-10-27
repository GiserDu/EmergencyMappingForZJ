package com.zz.bglayer;

import java.awt.geom.*;
import java.awt.geom.Rectangle2D.Double;

public class CalAffineTrans {
	private Rectangle2D.Double WC;
	private Rectangle2D.Double DC;
	private AffineTransform AT;

	public CalAffineTrans() {
		WC = null;
		DC = null;
		AT = null;
	}

	public CalAffineTrans(Rectangle2D.Double WC, Rectangle2D.Double DC) {
		this.WC = WC;
		this.DC = DC;
//		AT = new AffineTransform();

	    AT = new AffineTransform(1.0d,0.0d,0.0d,-1.0d,0.0d,DC.getHeight());

		double scale1 = DC.getWidth() / WC.getWidth();
		double scale2 = DC.getHeight() / WC.getHeight();
		double scale = scale1 < scale2 ? scale1 : scale2;

//		System.out.println(scale);

		double sx = (DC.getWidth() - (WC.getWidth() * scale)) / 2;
		double sy = (DC.getHeight() - (WC.getHeight() * scale)) / 2;
//		 System.out.println(sx+" "+sy);

		 AT.translate(sx, sy);
		
		 AT.scale(scale, scale);
				
		 AT.translate(-WC.getX(),-WC.getY());
		// AffineTransform AT1 = new
		// AffineTransform(1.0d,0.0d,0.0d,-1.0d,0.0d,0.0d);
		// AT.concatenate(AT1);
//		AT.translate(sx, sy+1.2*DC.height);
//		AT.scale(scale, scale);
//
//		
//		
//		AffineTransform AT1 = new AffineTransform(1.0d, 0.0d, 0.0d, -1.0d,
//				0.0d, 0.0d);
//		AT.concatenate(AT1);
//		AT.translate(-WC.getX(), -WC.getY());
		
//		AT.scale(scale, scale);

		//		AT.translate(sx, sy);
		//		AT.concatenate(AT1);

		// System.out.println(scale);

		// Point2D.Double pt1 = new Point2D.Double(0.0,0.0);
		// Point2D.Double pt2 = new Point2D.Double(355000.0,370000.0);
		// Point2D.Double pt3 = new Point2D.Double();
		// Point2D.Double pt4 = new Point2D.Double();
		// AT.transform(pt1, pt3);
		// AT.transform(pt2, pt4);
		// System.out.println(pt3);
		// System.out.println(pt4);

//		Point2D.Double pt1 = new Point2D.Double(219000.0d, 326000.0d);
//		Point2D.Double pt2 = new Point2D.Double(375000.0d, 516000.0d);
//		Point2D.Double pt3 = new Point2D.Double();
//		Point2D.Double pt4 = new Point2D.Double();
//		AT.transform(pt1, pt3);
//		AT.transform(pt2, pt4);
//		System.out.println(pt3);
//		System.out.println(pt4);
		//		

	}

	public AffineTransform getAT() {
		return AT;
	}

	public void setAT(AffineTransform at) {
		AT = at;
	}

}
