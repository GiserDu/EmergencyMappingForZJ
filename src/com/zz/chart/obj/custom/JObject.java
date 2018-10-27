package com.zz.chart.obj.custom;



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.IOException;


/** 
 * Object目标抽象类
 * @author L J
 * @version 1.0 
 */
public abstract class JObject {
	
	private Rectangle2D.Double rect;
	//lmk 2013.11.27
	private Area area;
	//lmk 2014.2.27
	private Color fillColor;
	public JObject(){
		rect = new Rectangle2D.Double();
		area = new Area();
		setFillColor(null);
	}

    /**
     * 绘图函数抽象方法
     * @param g2D Java绘图对象
     * @return void
     * @throws 
     * @since 1.0
     */
	public abstract void Draw(Graphics2D g2D);

	/**
	 * 数据载入函数抽象方法
	 * @param dis 文件路径指针
	 * @param aff
	 * @throws IOException
	 */
	public abstract void ReadObjectInfoBin(DataInputStream dis, Affine aff)throws IOException;
	
	public Rectangle2D.Double getRect() {
		return rect;
	}
	public void setRect(Rectangle2D.Double rect) {
		this.rect = rect;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public Color getFillColor() {
		return fillColor;
	}
}