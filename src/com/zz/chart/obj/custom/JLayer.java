package com.zz.chart.obj.custom;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Layer底图图层类
 * 
 * @author lmk
 * @version 1.0
 */
public class JLayer {

	private ArrayList<JObject> arrObjects;//实体数组
	private Boolean boolDraw;//是否画出(第二行)
	private double displayScale[];//显示比例尺(第三行)
	
	//lmk 2013.11.27
	private Area area;
	//lmk 2014.2.27
	private Color fillColor;

	public JLayer() {
		arrObjects = new ArrayList<JObject>();
		boolDraw = false;
		displayScale = null;
		area = new Area();
		fillColor = null;
	}

	public void AddObject(JObject Obj) {
		arrObjects.add(Obj);
	}

	/**
	 * Layer绘图函数
	 * 
	 * @param g2D
	 *            Java绘图对象
	 * @return void
	 * @throws
	 * @since 1.0
	 */
	public void Draw(Graphics2D g2D) {
		if (boolDraw == true) {
			for(int i = 0; i < arrObjects.size(); i++)
			{
				JObject obj = arrObjects.get(i);
				obj.Draw(g2D);
			}
		}
	}

	/**
	 * 读取Layer数据
	 * @param dataPath
	 * @param aff
	 * @throws IOException
	 */
	public void ReadLayerInfoBin(String dataPath, Affine aff)
			throws IOException {
		// Boolean hasGrid = hasGrid;
		String objPath = dataPath + ".ent";
		this.readEnt(objPath, aff);
	}

	/**
	 * 读取LayerEnt实体数据
	 * @param objPath
	 * @param aff
	 */
	private void readEnt(String objPath, Affine aff) {
		try {
			String begin;
			int objNum;
			FileInputStream fis;
			fis = new FileInputStream(objPath);
			DataInputStream dis = new DataInputStream(fis);
			begin = dis.readUTF();
			if (!begin.equals("TOFENTITYFORMAT")) {
				System.out.println("ENT文件出错！");
				return;
			}

			boolDraw = dis.readBoolean();

			displayScale = new double[2];
			for (int i = 0; i < 2; i++) {
				displayScale[i] = dis.readDouble();
			}

			objNum = dis.readInt();

			for (int i = 0; i < objNum; i++) {
				String objStyle = dis.readUTF();
				// System.out.println(objStyle);

				JObject jo = null;
				// System.out.println(objStyle);

				if (objStyle.equals("A")) {
					jo = new JAnnotation();
					jo.setFillColor(fillColor);
					jo.ReadObjectInfoBin(dis, aff);
					arrObjects.add(i, jo);
				} else if (objStyle.equals("L")) {
					jo = new JPolyline();
					jo.setFillColor(fillColor);
					jo.ReadObjectInfoBin(dis, aff);
					arrObjects.add(i, jo);
				} else if (objStyle.equals("M")) {
					jo = new JPolygon();
					jo.setFillColor(fillColor);
					jo.ReadObjectInfoBin(dis, aff);					
					arrObjects.add(i, jo);
				} 
				//lmk 2013.11.27
				Area tempArea = jo.getArea();
				tempArea.add(this.getArea());
				this.setArea(tempArea);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public double[] getDisplayScale() {
		return displayScale;
	}

	public void setDisplayScale(double[] displayScale) {
		this.displayScale = displayScale;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}
	
}