package com.zz.chart.obj.custom;
import java.awt.geom.*;

/** 
 * 仿射变换类,从AI坐标到像素坐标
 * @author lmk
 * @version 1.0 
 */
public class Affine {
	private Rectangle2D.Double WC;
	private Rectangle2D.Double DC;

	private double scale;

	public Affine() {
		WC = new Rectangle2D.Double();
		DC = new Rectangle2D.Double();
		scale = 1.0; //默认比例尺为1.0
	}

	/** 
	 * 带参数构造函数,初始化wc，dc并计算scale 
	 * @param wc
	 * @param dc
	 * @since 1.0 
	 */ 
	public Affine(Rectangle2D.Double wc, Rectangle2D.Double dc) { //构造函数中直接仿射变换
		this.WC = wc;
		this.DC = dc;
		if (Math.abs(DC.width) < 1e-5 || Math.abs(DC.height) < 1e-5) {
			return;
		}
		double scale1 = WC.getWidth() / DC.width;
		double scale2 = WC.getHeight() / DC.height;
		scale = scale1 < scale2 ? scale2 : scale1;//scale作为除数，因此取大的
		
	}

    /**
     * 对点仿射变换
     * @param pt
     * @return Point2D.Double
     * @throws 
     * @since 1.0
     */
	public Point2D.Double TransPoint(Point2D.Double pt) { //自然坐标点－－>设备坐标点转换
		Point2D.Double pt1 = new Point2D.Double();
	
		double x = (pt.getX() - WC.getX()) / scale ;
		double y = (pt.getY() - WC.getY()) / scale;
		
		//lmk 2013.11.27
		x = DC.x + x + (DC.getWidth() - WC.getWidth() / scale) / 2;
		y = DC.y + y + (DC.getHeight() - WC.getHeight() / scale) / 2;
		y = DC.y * 2 + DC.getHeight()-y;//图形翻转
		
		pt1.setLocation(x,y);
		return pt1;
	}
	public double getScale(){
		return this.scale;
	}
	
	public void setScale(double scale){
		this.scale = scale;
	}

}
