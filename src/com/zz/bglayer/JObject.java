package com.zz.bglayer;

import java.io.*;
import java.awt.*;
import java.awt.geom.*;
//import java.awt.geom.*;
/** 
 * Object目标抽象类
 * @author L J
 * @version 1.0 
 */
public abstract class JObject {
	//private Boolean isDraw; 
	private Rectangle2D.Double rect;
	//public abstract char GetType();
    /**
     * 读取Raster栅格数据
     * @param dataPath 栅格文件路径
     * @return void
     * @throws 
     * @since 1.0
     */
	protected void ReadBufferedImage(String dataPath){
		
	}
	public JObject(){
		//isDraw = false;
		rect = new Rectangle2D.Double();
	}
    /**
     * 绘图函数抽象方法
     * @param g2D Java绘图对象
     * @return void
     * @throws 
     * @since 1.0
     */
//	public abstract void Draw(Graphics2D g2D,Affine affTrans);
	public abstract void Draw(Graphics2D g2D);
	public abstract void ReadObjectInfoText();
    /**
     * 数据载入函数抽象方法
     * @param dis 文件路径指针
     * @return void
     * @throws 
     * @since 1.0
     */
	public abstract void ReadObjectInfoBin(DataInputStream dis)throws IOException;
	//public abstract void ReadObjectInfoBin(DataInputStream dis,Graphics2D g2D)throws IOException;//李坚修改
	//Vector<JPoint> vecPt;
	/*
	float flineWidth;
	int intlineColor;
	int intlineStyle;
	char chrobjType;
	
	JObject(){
		flineWidth = 0.0f;
		intlineColor = 0;
		intlineStyle = 0;
		chrobjType = 'L';
		vecPt = new Vector();
	}
	
	public char getobjType() {
		return chrobjType;
	}
	
	public void setobjType(char chrobjType) {
		this.chrobjType = chrobjType;
	}
	
	public int getlineColor() {
		return intlineColor;
	}
	
	public void setlineColor(int clineColor) {
		this.intlineColor = intlineColor;
	}
	
	public float getlineWidth() {
		return flineWidth;
	}
	
	public void setlineWidth(float flineWidth) {
		this.flineWidth = flineWidth;
	}
	
	public int getlineStyle() {
		return intlineStyle;
	}
	
	public void setlineStyle(int intlineStyle) {
		this.intlineStyle = intlineStyle;
	}
	
	public void AddPoint(int x1, int y1) {
	    vecPt.add(new Point(x1, y1));
	}
	
	public void Draw(Graphics2D g2D, Rectangle2D.Double WC, Rectangle2D.Double DC){};
	

	public Boolean getIsDraw() {
		return isDraw;
	}
	public void setIsDraw(Boolean isDraw) {
		this.isDraw = isDraw;
	}*/
	public Rectangle2D.Double getRect() {
		return rect;
	}
	public void setRect(Rectangle2D.Double rect) {
		this.rect = rect;
	}
}
