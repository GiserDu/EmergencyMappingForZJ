package com.zz.chart.obj.custom;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;

/** 
 * Annotation对象属性结构类，JAnnotation由此类组成
 * @author L J
 * @version 1.0 
 */
public class JTextStruct {
	
	private double [] affineArray;//仿射变换
	private double dFHeight;//字体的高
	private Color lTextColor;//字体颜色
	private int lTextban;//字体的磅
	private String textType; //字体
	private String textContent;//字体内容
	private int  lTextlfEscapement;
	private Boolean isTrans;//是否已仿射变换
	private Point2D.Double point;//绘制点坐标
	
	private Font ft;
	public JTextStruct(){
		affineArray = new double [4];
		dFHeight=-1;
	    lTextColor=null;
		lTextban=0;
		lTextlfEscapement=0;
		textType = null;
		textContent = null;
		isTrans = false;
		ft = null;
	}
	
	public double getDFHeight() {
		return dFHeight;
	}
	public void setDFHeight(double height) {
		dFHeight = height;
	}
	public Color getLTextColor() {
		return lTextColor;
	}
	public void setLTextColor(Color textColor) {
		lTextColor = textColor;
	
	}
	
	public int getLTextban() {
		return lTextban;
	}
	public void setLTextban(int textban) {
		lTextban = textban;
	}
	public void setLTextlfEscapement(int ltextlfescapement){
		lTextlfEscapement=ltextlfescapement;
	}
	public int getLTextlfEscapement(){
	    return lTextlfEscapement;
	}
	public String getTextType() {
		return textType;
	}
	public void setTextType(String textType) {
		this.textType = textType;
	}
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public double[] getAffineArray() {
		return affineArray;
	}

	public void setAffineArray(double[] affineArray) {
		this.affineArray = affineArray;
	}

	public Boolean getIsTrans() {
		return isTrans;
	}

	public void setIsTrans(Boolean isTrans) {
		this.isTrans = isTrans;
	}

	public Point2D.Double getPoint() {
		return point;
	}

	public void setPoint(Point2D.Double point) {
		this.point = point;
	}

	public Font getFt() {
		return ft;
	}

	public void setFt(Font ft) {
		this.ft = ft;
	}
	
}