package com.zz.bglayer;
import java.awt.*;
import java.awt.geom.*;
//import java.awt.image.BufferedImage;
/** 
 * Annotation对象属性结构类，JAnnotation由此类组成
 * @author L J
 * @version 1.0 
 */
public class JTextStruct {
	
	private double [] affineArray;
	private double dFHeight;
	private Color lTextColor;
	private int lTextban;
	private String textType; 
	private String textContent;
	//private Font tagLogFont;
	private int  lTextlfEscapement;
	private Boolean isTrans;
	//private Rectangle2D.Double rect;
	private Point2D.Double point;
	
	private Font ft;
	public void JTextStruct(){
		affineArray = new double [4];
		dFHeight=-1;
	    lTextColor=null;
		lTextban=0;
		//tagLogFont=null;
		lTextlfEscapement=0;
		textType = null;
		textContent = null;
		isTrans = false;
		//rect = null;
		ft = null;
	}
	
	/*public double getDAffineA() {
		return dAffineA;
	}
	public void setDAffineA(double affineA) {
		dAffineA = affineA;
	}
	public double getDAffineB() {
		return dAffineB;
	}
	public void setDAffineB(double affineB) {
		dAffineB = affineB;
	}
	public double getDAffineC() {
		return dAffineC;
	}
	public void setDAffineC(double affineC) {
		dAffineC = affineC;
	}
	public double getDAffineD() {
		return dAffineD;
	}
	public void setDAffineD(double affineD) {
		dAffineD = affineD;
	}*/
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
	/*public Font getTagLogFont() {
		return tagLogFont;
	}
	public void setTagLogFont(Font tagLogFont) {
		this.tagLogFont = tagLogFont;
	}*/
	
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


	/*public Rectangle2D.Double getRect() {
		return this.rect;
	}
	
	public void setRect(Rectangle2D.Double rect) {
		this.rect = rect;
	}*/

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