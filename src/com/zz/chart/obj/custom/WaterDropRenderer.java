package com.zz.chart.obj.custom;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
/**
 * 
 * @author lmk
 * @version 1.0
 * 2013.12.9
 */
public class WaterDropRenderer
{
	private final double PI = 3.141592653;
	private final Color STARTCOLOR = new Color(0xffffff);
	private Color colorIndex1;
	private Color colorIndex2;//地图定义文件提供
	private Color colorOutFill;
	private Color colorOutDraw;
	private Color colorCenter;
	private double ratioOffset;
	private double ratioInMax;
	private double ratioCenterOffset;
	private double ratioUpDownUnitValue;
	private boolean isLabel;//符号描述文件提供
	private double valueIndex1;
	private double valueIndex2;
	private double MaxValue;
	private double MinValue;//专题数据文件提供
	private double x, y;
	private double width, height;
	private Font font;
	private Color colorFont;//绘制时候提供
	private double maxRout;//getMaxROut()
	private double minRout;//getMinRout()
	private double rOut;
	private double rIn;
	private double rCenter;//getRAll();
	private Shape shapeIndex1;
	private Shape shapeIndex2;
	private Point2D.Double[] str1Pt;
	private Point2D.Double[] str2Pt;
	
	public void draw(Graphics2D g2D)
	{
		maxRout = getMaxROut();
		if(maxRout < rOut)
		{
			System.out.println("绘图区域不够！！！");
			return;
		}
		minRout = getMinRout();
		getRAll();
		if(rOut < minRout)
		{
			rIn = minRout / rOut * rIn;
			rCenter = minRout / rOut * rCenter;
			rOut = minRout;
			isLabel = true;
		}
		Point2D.Double[] p2Ds = new Point2D.Double[4];
		double rectCenterX = x;
		double rectCenterY = y - (ratioOffset + 1) * Math.sqrt(.5) * rOut;
		double rectR = (1 - ratioOffset) * Math.sqrt(.5) * rOut;
		for(int i = 0; i < p2Ds.length; i++)
		{
			Point2D.Double curP2D = new Point2D.Double();
			curP2D.x = rectCenterX + rectR * Math.cos(i * PI / 2);
			curP2D.y = rectCenterY - rectR * Math.sin(i * PI / 2);
			p2Ds[i] = curP2D;
		}
		GeneralPath gp = new GeneralPath();
		gp.moveTo(p2Ds[0].x, p2Ds[0].y);
		for(int i = 1; i < p2Ds.length; i++)
			gp.lineTo(p2Ds[i].x, p2Ds[i].y);
		Area area1 = new Area(gp);
		fillGradientByLineVU(area1, g2D, STARTCOLOR, colorIndex1);//画上面的矩形,VU渐变填色
		Rectangle2D.Double rectOut = new Rectangle2D.Double(x - rOut, y - rOut, rOut * 2, rOut * 2);
		Arc2D.Double arcOut = new Arc2D.Double(rectOut, 135, 270, Arc2D.PIE);
		fillGradientByLineHL(arcOut, g2D, STARTCOLOR, colorOutFill);//画最外面的扇形,HL渐变填色
		g2D.setColor(colorOutDraw);
		g2D.draw(arcOut);//画最外面的扇形边线
		Ellipse2D.Double ellipseCenter = new Ellipse2D.Double(x - rCenter, y - rCenter, rCenter * 2, rCenter * 2);
		fillGradientByCircle(ellipseCenter, g2D, STARTCOLOR, colorCenter);//画中间的圆,渐变填色，圆心向外
		Ellipse2D.Double ellipseIn = new Ellipse2D.Double(x - rIn, y - rIn, rIn * 2, rIn * 2);
		fillGradientByCircle(ellipseIn, g2D, STARTCOLOR, colorIndex2);//画里面的圆,渐变填色,圆心向外
		if(isLabel)
		{
			//注记最后写
			Color color = g2D.getColor();
			g2D.setColor(colorFont);
			g2D.setFont(font);
			FontMetrics fm = g2D.getFontMetrics(font);
			String str1 = "" + valueIndex1;
			double stringWidth1 = fm.stringWidth(str1);
			double stringAscent1 = fm.getAscent();
			double stringDescent1 = fm.getDescent();
			int stringX1 = (int) (rectCenterX - stringWidth1 / 2.0);
			//lmk修改 2014.2.14
			int StringY1 = (int) (rectCenterY + stringDescent1 / 2.0 - rectR * 1.0 / 3);//上方字体置于中线2/3处
			g2D.drawString(str1, stringX1, StringY1);
			
			String str2 = "" + valueIndex2;
			double stringWidth2 = fm.stringWidth(str2);
			double stringAscent2 = fm.getAscent();
			double stringDescent2 = fm.getDescent();
			int stringX2 = (int) (x - stringWidth2 / 2.0);
			int StringY2 = (int) (y + stringDescent2 / 2.0);
			g2D.drawString(str2, stringX2, StringY2);
			//lmk修改 2014.2.14
			Point2D.Double end1Pt = new Point2D.Double();
			end1Pt.x = rectCenterX + stringWidth1 / 2.0;
			end1Pt.y = rectCenterY - rectR * 1.0 / 3;
			Point2D.Double end2Pt = new Point2D.Double();
			end2Pt.x = x + stringWidth2 / 2.0;
			end2Pt.y = y;
			if(area1.contains(end1Pt) && ellipseCenter.contains(end2Pt))
			{
				str1Pt = new Point2D.Double[2];
				str1Pt[0] = end1Pt;
				str1Pt[1] = new Point2D.Double(arcOut.getMaxX() + arcOut.getWidth() * .02, str1Pt[0].y);
				str2Pt = new Point2D.Double[2];
				str2Pt[0] = end2Pt;
				str2Pt[1] = new Point2D.Double(arcOut.getMaxX() + arcOut.getWidth() * .02, str2Pt[0].y);
			}
			else
			{
				str1Pt = new Point2D.Double[3];
				str1Pt[0] = new Point2D.Double(rectCenterX, rectCenterY - stringAscent1 / 2.0 - rectR * 1.0 / 3);
				str1Pt[1] = new Point2D.Double(str1Pt[0].x + 5, str1Pt[0].y - 5);
				str1Pt[2] = new Point2D.Double(arcOut.getMaxX() + arcOut.getWidth() * .02, str1Pt[1].y);
				str2Pt = new Point2D.Double[3];
				str2Pt[0] = new Point2D.Double(x, y - stringAscent2 / 2.0);
				str2Pt[1] = new Point2D.Double(str2Pt[0].x + 5, str2Pt[0].y - 5);
				str2Pt[2] = new Point2D.Double(arcOut.getMaxX() + arcOut.getWidth() * .02, str2Pt[1].y);
			}
			g2D.setColor(color);
		}
	}
	//热区
	public void loadArea()
	{
		maxRout = getMaxROut();
		if(maxRout < rOut)
		{
			System.out.println("绘图区域不够！！！");
			return;
		}
		minRout = getMinRout();
		getRAll();
		if(rOut < minRout)
		{
			rIn = minRout / rOut * rIn;
			rCenter = minRout / rOut * rCenter;
			rOut = minRout;
		}
		Point2D.Double[] p2Ds = new Point2D.Double[4];
		double rectCenterX = x;
		double rectCenterY = y - (ratioOffset + 1) * Math.sqrt(.5) * rOut;
		double rectR = (1 - ratioOffset) * Math.sqrt(.5) * rOut;
		for(int i = 0; i < p2Ds.length; i++)
		{
			Point2D.Double curP2D = new Point2D.Double();
			curP2D.x = rectCenterX + rectR * Math.cos(i * PI / 2);
			curP2D.y = rectCenterY - rectR * Math.sin(i * PI / 2);
			p2Ds[i] = curP2D;
		}
		GeneralPath gp = new GeneralPath();
		gp.moveTo(p2Ds[0].x, p2Ds[0].y);
		//lmk修改 2014.2.14，改变水滴图热区
//		for(int i = 1; i < p2Ds.length; i++)
//			gp.lineTo(p2Ds[i].x, p2Ds[i].y);
		for(int i = 1; i < p2Ds.length - 1; i++)
			gp.lineTo(p2Ds[i].x, p2Ds[i].y);
		shapeIndex1 = new Area(gp);
		shapeIndex2 = new Ellipse2D.Double(x - rIn, y - rIn, rIn * 2, rIn * 2);
	}
	private void fillGradientByLineHL(Arc2D.Double arc, Graphics2D g2D, Color color1, Color color2)
	{
		Rectangle2D.Double bound = (Double) arc.getBounds2D();
		Point2D.Double pt1 = new Point2D.Double(bound.getMinX(), bound.getMinY());
		Point2D.Double pt2 = new Point2D.Double(bound.getMaxX(), bound.getMinY());
		GradientPaint paint = new GradientPaint(pt1, color2, pt2, color1);
		g2D.setPaint(paint);
		g2D.fill(arc);
	}
	private void fillGradientByLineVU(Area area, Graphics2D g2D, Color color1, Color color2)
	{
		Rectangle2D.Double bound = (Double) area.getBounds2D();
		Point2D.Double pt1 = new Point2D.Double(bound.getMinX(), bound.getMinY());
		Point2D.Double pt2 = new Point2D.Double(bound.getMinX(), bound.getMaxY());
		GradientPaint paint = new GradientPaint(pt1, color2, pt2, color1);
		g2D.setPaint(paint);
		g2D.fill(area);
	}
	private void fillGradientByCircle(Ellipse2D.Double ellipse, Graphics2D g2D, Color color1, Color color2)
	{
		Point2D.Double pt1 = new Point2D.Double(ellipse.getCenterX(), ellipse.getCenterY());
		Point2D.Double pt2 = new Point2D.Double();
		double width = ellipse.getWidth();
		double height = ellipse.getHeight();
		if(Math.abs(width - height) > 1.0e-3)
			return;
		Rectangle2D.Double ellipseBound = (Double) ellipse.getBounds2D();
		for(int i = 0; i < 360; i = i + 1)
		{
			Arc2D.Double arc2D = new Arc2D.Double(ellipseBound, i, 2, Arc2D.PIE);
			pt2.x = pt1.x + width / 2 * Math.cos((i + .5) * PI / 180);
			pt2.y = pt1.y - height / 2 * Math.sin((i + .5) * PI / 180);
			GradientPaint paint = new GradientPaint(pt1, color1, pt2, color2);
			g2D.setPaint(paint);
			g2D.fill(arc2D);
		}
	}
	//lmk 2014.2.25
	private double getMaxROut()
	{
		double curMaxROut = 0;
		if(width > height / Math.sqrt(2))
			curMaxROut = height / (2 * Math.sqrt(2));
		else
			curMaxROut = width / 2;
		return curMaxROut;
	}
	private double getMinRout()
	{
		double k = maxRout * maxRout / MaxValue;
		double curMinROut = Math.sqrt(MinValue * k) / (1 - ratioOffset);
		return curMinROut;
	}
	private void getRAll()
	{
		double k = maxRout * maxRout / MaxValue;
		rOut = Math.sqrt(valueIndex1 * k) / (1 - ratioOffset);
		rIn = Math.sqrt(valueIndex2 * k) * ratioUpDownUnitValue;
		rCenter = rIn + ratioCenterOffset * rOut;
	}
	public Color getColorOutFill() {
		return colorOutFill;
	}

	public void setColorOutFill(Color colorOutFill) {
		this.colorOutFill = colorOutFill;
	}

	public Color getColorOutDraw() {
		return colorOutDraw;
	}

	public void setColorOutDraw(Color colorOutDraw) {
		this.colorOutDraw = colorOutDraw;
	}

	public Color getColorCenter() {
		return colorCenter;
	}

	public void setColorCenter(Color colorCenter) {
		this.colorCenter = colorCenter;
	}

	public double getRatioOffset() {
		return ratioOffset;
	}

	public void setRatioOffset(double ratioOffset) {
		this.ratioOffset = ratioOffset;
	}

	public double getRatioInMax() {
		return ratioInMax;
	}

	public void setRatioInMax(double ratioInMax) {
		this.ratioInMax = ratioInMax;
	}

	public double getRatioCenterOffset() {
		return ratioCenterOffset;
	}

	public void setRatioCenterOffset(double ratioCenterOffset) {
		this.ratioCenterOffset = ratioCenterOffset;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public Color getColorIndex1() {
		return colorIndex1;
	}
	public void setColorIndex1(Color colorIndex1) {
		this.colorIndex1 = colorIndex1;
	}
	public Color getColorIndex2() {
		return colorIndex2;
	}
	public void setColorIndex2(Color colorIndex2) {
		this.colorIndex2 = colorIndex2;
	}
	public double getPI() {
		return PI;
	}
	public Color getSTARTCOLOR() {
		return STARTCOLOR;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getValueIndex1() {
		return valueIndex1;
	}
	public void setValueIndex1(double valueIndex1) {
		this.valueIndex1 = valueIndex1;
	}
	public double getValueIndex2() {
		return valueIndex2;
	}
	public void setValueIndex2(double valueIndex2) {
		this.valueIndex2 = valueIndex2;
	}
	public double getMaxValue() {
		return MaxValue;
	}
	public void setMaxValue(double maxValue) {
		MaxValue = maxValue;
	}
	public double getMinValue() {
		return MinValue;
	}
	public void setMinValue(double minValue) {
		MinValue = minValue;
	}
	public Shape getShapeIndex1() {
		return shapeIndex1;
	}
	public Shape getShapeIndex2() {
		return shapeIndex2;
	}
	
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public Color getColorFont() {
		return colorFont;
	}
	public void setColorFont(Color colorFont) {
		this.colorFont = colorFont;
	}
	public boolean isLabel() {
		return isLabel;
	}
	public void setLabel(boolean isLabel) {
		this.isLabel = isLabel;
	}
	public double getRatioUpDownUnitValue() {
		return ratioUpDownUnitValue;
	}
	public void setRatioUpDownUnitValue(double ratioUpDownUnitValue) {
		this.ratioUpDownUnitValue = ratioUpDownUnitValue;
	}
	public Point2D.Double[] getStr1Pt() {
		return str1Pt;
	}
	public Point2D.Double[] getStr2Pt() {
		return str2Pt;
	}
}