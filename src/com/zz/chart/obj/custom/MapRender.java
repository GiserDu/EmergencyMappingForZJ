package com.zz.chart.obj.custom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * 引擎最外层接口，提供对外界各种方法
 * 
 * @author lmk
 * @version 1.0
 */
public class MapRender {

	private JMap map;
	private String dataPath;
	private Rectangle2D.Double DC;
	//lmk 2013.11.27
	private Area area;
	//lmk 2014.2.27
	private Color fillColor;//定制自定义符号的填充颜色，默认为艺术符号本来的颜色
	public MapRender() {
		map = null;
		dataPath = "";
		DC = new Rectangle2D.Double();
		area = new Area();
		fillColor = null;
	}

	/**
	 * 数据载入接口
	 * 
	 * @param dataPath
	 *            数据路径
	 * @return boolean
	 * @throws
	 */
	public boolean LoadData(String dataPath) {
		this.dataPath = dataPath;
		map = new JMap();
		map.setDC(DC);
		//lmk 2014.2.27
		map.setFillColor(fillColor);
		map.ReadMapInfoBin(dataPath);
			
		//lmk 2013.11.27
		this.setArea(map.getArea());
		
		//System.out.println("File end");
		return true;
	}

	/**
	 * 绘图接口
	 * @return BufferImage
	 * @throws
	 */
	public BufferedImage OutputMap() {
		try {
			BufferedImage bi = new BufferedImage((int) DC.getWidth(), (int) DC
					.getHeight(), BufferedImage.TYPE_INT_RGB);

			Graphics2D g2D = (Graphics2D) bi.getGraphics();
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			g2D.setColor(Color.WHITE);
			g2D.fill(DC);

			map.Draw(g2D);
			g2D.dispose();
			bi.flush();
			return bi;
		} catch (Exception e) {
			System.out.println("Map render error");
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * 绘图接口
	 * @param g2D
	 */
	public void draw(Graphics2D g2D)
	{
		map.Draw(g2D);
	}
	
	public JMap getMap() {
		return map;
	}

	public void setMap(JMap map) {
		this.map = map;
	}
	
	public String getDataPath()
	{
		return dataPath;
	}
	
	public void setDataPath(String dataPath)
	{
		this.dataPath = dataPath;
	}
	
	public Rectangle2D.Double getDC()
	{
		return DC;
	}
	
	public void setDC(Rectangle2D.Double dc)
	{
		this.DC = dc;
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
