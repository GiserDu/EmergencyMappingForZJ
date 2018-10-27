package com.zz.chart.obj.custom;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/** 
 * Map底图类
 * @author L.J
 * @version 1.0 
 */
public class JMap {

	private ArrayList<JLayer> mapLayers;
	private ArrayList<String> layerNameIndex;
	private Rectangle2D.Double WC;
	private Rectangle2D.Double DC;
	
	//lmk 2013.11.27
	private Area area;
	//lmk 2014.2.27
	private Color fillColor;

	public JMap() {
		mapLayers = new ArrayList<JLayer>();
		layerNameIndex = new ArrayList<String>();
		WC = new Rectangle2D.Double();
		DC = new Rectangle2D.Double();
		area = new Area();
		fillColor = null;
	}
	/**
	 * Map绘图函数
	 * @param g2D Java绘图对象
	 * @return void
	 * @throws
	 * @since 1.0
	 */
	public void Draw(Graphics2D g2D) {
		for (int i = 0; i < mapLayers.size(); i++) {
			JLayer mapLayer = mapLayers.get(i);
			mapLayer.Draw(g2D);
		}
	}
	
	/**
	 * 读取Map数据
	 * @param dataPath 文件路径
	 * @return void
	 * @throws 
	 * @since 1.0
	 */
	public void ReadMapInfoBin(String dataPath) {
		try {
			//System.out.println(this.getClass().getResource("/").getPath());获取返回class文件所在的顶级目录，一般为包名的顶级目录。 --> file:/home/duanyong/workspace/cxxx/xxxx/bin/WEB-INF/classes/ 

			String temp = dataPath;
			//web project
			dataPath = new File(this.getClass().getResource("/").getPath(), "../../" + temp).getCanonicalPath();
			dataPath = dataPath.replace("%20", " ");//路径中空格会变为%20，这里将它换过来

			//System.out.println(dataPath);
			File file = new File(dataPath);

			//System.out.println(System.getProperty("user.dir"));当前调用java的目录 
			if (!file.isDirectory()) {
				//application 
				dataPath = new File(this.getClass().getResource("/").getPath(), "../" + temp).getCanonicalPath();
				dataPath = dataPath.replace("%20", " ");//路径中空格会变为%20，这里将它换过来
				//System.out.println(dataPath);
				file = new File(dataPath);
				if (!file.isDirectory()) {
					System.out.println("Map文件夹为空,请重新读入数据！");
					return;
				}
				
			}

			String layerPath = null;
			String tofPath = null;

			String[] fileList = file.list();
			for (int i = 0; i < fileList.length; i++) {
				File listPath = new File(dataPath + "/" + fileList[i]);
				if (listPath.isDirectory()) {
					layerPath = dataPath + "/" + fileList[i];// D:\Atlas\20w\20w_Lyrs
				} else if (fileList[i].toLowerCase().endsWith(".tof")) {
					tofPath = dataPath + "/" + fileList[i];// D:\Atlas\20w\20w.TOF
				} else {
					System.out.println("数据有误！");
					return;
				}
			}
			this.readTOF(tofPath, layerPath);// 读TOF文件
			
		} catch (IOException e) {

		}
	}

	/**
	 * 读取.tof文件数据
	 * @param tofPath .tof文件路径
	 * @param layerPath .ent文件夹路径
	 * @return void
	 * @throws 
	 * @since 1.0
	 */
	private void readTOF(String tofPath, String layerPath) throws IOException {		
		FileInputStream fis = new FileInputStream(tofPath);
		DataInputStream dis = new DataInputStream(fis);
		String begin;
		begin = dis.readUTF();
		if (!begin.equals("TOFMAPFORMAT")) {
			System.out.println("TOF文件出错！");
			return;
		}

		double bx1 = dis.readDouble();
		double by1 = dis.readDouble();
		double bx2 = dis.readDouble();
		double by2 = dis.readDouble();
		WC = new Rectangle2D.Double(bx1, by1, bx2 - bx1, by2 - by1);
		
		Affine aff = new Affine(WC, DC);

		@SuppressWarnings("unused")
		int rowNum = dis.readInt();//网格行数
		@SuppressWarnings("unused")
		int lineNum = dis.readInt();//网格列数		
		@SuppressWarnings("unused")
		long totalObjNum = dis.readLong();//总实体数

		int layerNum = dis.readInt();
		for (int j = 0; j < layerNum; j++) {
			String layerName = dis.readUTF();
			layerNameIndex.add(j, layerName);
			String layerPathFull = layerPath + "/" + layerName;
			JLayer layer = new JLayer();
			//lmk 2014.2.27
			layer.setFillColor(fillColor);
			layer.ReadLayerInfoBin(layerPathFull, aff);// 得出每个文件的绝对路径,缺后缀名
			mapLayers.add(j, layer);
			
			//lmk 2013.11.27
			Area tempArea = this.getArea();
			tempArea.add(layer.getArea());
			this.setArea(tempArea);
		}
		layerNameIndex.trimToSize();
		mapLayers.trimToSize();
		dis.close();
		fis.close();
	}
	
	public Rectangle2D.Double getWC() {
		return WC;
	}
	public void setWC(Rectangle2D.Double wc) {
		WC = wc;
	}	
	public Rectangle2D.Double getDC() {
		return DC;
	}
	public void setDC(Rectangle2D.Double dc) {
		DC = dc;
	}

	public ArrayList<JLayer> getMapLayers() {
		return mapLayers;
	}

	public void setMapLayers(ArrayList<JLayer> mapLayers) {
		this.mapLayers = mapLayers;
	}

	public ArrayList<String> getLayerNameIndex() {
		return layerNameIndex;
	}

	public void setLayerNameIndex(ArrayList<String> layerNameIndex) {
		this.layerNameIndex = layerNameIndex;
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