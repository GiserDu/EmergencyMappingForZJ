package com.zz.bglayer;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.lang.Math;

/** 
 * Map底图类
 * @author L J
 * @version 1.0 
 */
public class JMap {

	private ArrayList<JLayer> mapLayers;
	private ArrayList<String> layerNameIndex;
	private JRegionLayer regQULayer;//分为区和街道对应的地理层
	private JRegionLayer regJDLayer;
	private Rectangle2D.Double WC;
	// private double mapScale;
	// private long totalObjNum;
	private Boolean hasGrid;
	private Boolean hasSCA;

	// add by LJ 09 03 11
	// private boolean bolDrawRegionLayer;
	private AffineGeoToAI affGeoToAI;

	// private Affine affTrans;

	private double[] gridSize;// 每个网格的大小

	private int lineNum, rowNum;
	private final static double DCWIDTH = 1000;
	private final static double DCHEIGHT = 540;

	private HashMap<String, double[]> nameMapLayerScale;

	private double minLayerScale;
	private double maxLayerScale;
	
//	private final static int REGQUNUM = 16;//深圳市区的个数
	private final static int REGQUNUM = 31;//全国省级个数
	// add by LJ

	public JMap() {
		mapLayers = new ArrayList<JLayer>();
		regQULayer = null;
		regJDLayer = null;
		layerNameIndex = new ArrayList<String>();
		WC = new Rectangle2D.Double();
		// mapScale = 1;
		// totalObjNum = 0;
		hasGrid = false;
		hasSCA = false;

		// add by LJ 09 03 11
		// bolDrawRegionLayer = false;
		affGeoToAI = null;
		// affTrans = null;
		gridSize = null;
		lineNum = 0;
		rowNum = 0;

		minLayerScale = 0.0;
		maxLayerScale = 0.0;
	}

	public void AddLayer(JLayer Layer) {
		mapLayers.add(Layer);
	}

	public int GetlayerNumber() {

		return mapLayers.size();
	}

	/*
	 * public void SetBoundary(double x, double y, double w, double h) {
	 * recRect.setMaxX(x); recRect.setMaxY(y); recRect.setMinX(w);
	 * recRect.setMinY(h); }
	 */

	/**
	 * 计算当前图层比例系数
	 * @param AIWC 当前WC请求范围 
	 * @return double
	 * @throws 
	 * @since 1.0
	 */
	private Double CalLayerDisplayScale(Rectangle2D.Double AIWC,
			Rectangle2D.Double DC) {
		double scale1 = this.WC.width / DCWIDTH;
		double scale2 = this.WC.height / DCHEIGHT;
		double scale;
		if (scale1 >= scale2) {
			scale = AIWC.width / (DC.width * scale1);
		} else {
			scale = AIWC.height / (DC.height * scale2);
		}
		return scale;
	}

	/*
	 * private void IteratorLayer(ArrayList<JLayer> mapLayers,Graphics2D g2D,
	 * Rectangle2D.Double WC, Rectangle2D.Double DC,Affine affTrans){ double
	 * layerDisplayScale = this.CalLayerDisplayScale(WC); for (int i = 0; i <
	 * mapLayers.size(); i++) { JLayer mapLayer = mapLayers.get(i); //if
	 * (mapLayer.getDisplayScale()[1] <= layerDisplayScale //&&
	 * layerDisplayScale <= mapLayer.getDisplayScale()[0]) mapLayer.Draw(g2D,
	 * WC, DC, affTrans); } }
	 */

	/**
	 * Map绘图函数,不绘制分级底图的情况
	 * @param g2D Java绘图对象
	 * @param WC AI范围
	 * @param DC 像素范围
	 * @return void
	 * @throws
	 * @since 1.0
	 */
	public void Draw(Graphics2D g2D, Rectangle2D.Double WC,
			Rectangle2D.Double DC) {
		// Rectangle2D.Double AIWC = this.AffGeoToAI(WC);
		// System.out.println(AIWC);
		// Rectangle2D.Double AIWC = affGeoToAI.GetAIWC(WC);
		Rectangle2D.Double AIWC = WC;
		// Rectangle2D.Double WC1 = new
		// Rectangle2D.Double(78827.65194362706,-5470.596746257016,100902.9,68612.744);
		// Rectangle2D.Double AIWC1 = affGeoToAI.GetAIWC(WC1);
		//		CalAffineTrans CAT1 = new CalAffineTrans();
		CalAffineTrans CAT = new CalAffineTrans(AIWC, DC);
		AffineTransform AT = CAT.getAT();
		g2D.transform(AT);
		Affine affTrans = new Affine(AIWC, DC);
		// this.affTrans = new Affine(AIWC, DC);

		double layerDisplayScale = this.CalLayerDisplayScale(AIWC, DC);
		// layerDisplayScale = 1.0;
		// layerDisplayScale = 0.3;
		// layerDisplayScale = 0.4;
		// System.out.println(layerDisplayScale);
		hasGrid = false;

		//当计算出的layerDisplayScale小于最小时，以最小计算，大于最大时，以最大计算
		if (layerDisplayScale > maxLayerScale)
			layerDisplayScale = maxLayerScale;
		if (layerDisplayScale < minLayerScale)
			layerDisplayScale = minLayerScale;

		if (hasGrid == false) {
			for (int i = 0; i < mapLayers.size(); i++) {
				JLayer mapLayer = mapLayers.get(i);
				if (mapLayer.getDisplayScale()[1] <= layerDisplayScale
						&& layerDisplayScale <= mapLayer.getDisplayScale()[0])
					mapLayer.Draw(g2D, AIWC, DC, affTrans);
			}
		} else {
			//			if ((AIWC.width * AIWC.height) <= 0.6 * (this.WC.height * this.WC.width)) {
			int[] drawGrid = this
					.selectDrawGrid(rowNum, lineNum, AIWC, this.WC);
			for (int i = 0; i < mapLayers.size(); i++) {
				JLayer mapLayer = mapLayers.get(i);
				if (mapLayer.getDisplayScale()[1] <= layerDisplayScale
						&& layerDisplayScale <= mapLayer.getDisplayScale()[0])
					mapLayer.Draw(g2D, AIWC, DC, affTrans, drawGrid);
			}
			//			}
			//			} else {
			//				for (int i = 0; i < mapLayers.size(); i++) {
			//					JLayer mapLayer = mapLayers.get(i);
			//					if (mapLayer.getDisplayScale()[1] <= layerDisplayScale
			//							&& layerDisplayScale <= mapLayer.getDisplayScale()[0])
			//						mapLayer.Draw(g2D, AIWC, DC, affTrans);
			//				}
			//			}

			// int[] drawGrid = this
			// .selectDrawGrid(rowNum, lineNum, AIWC, this.WC);
			// for (int i = 0; i < mapLayers.size(); i++) {
			// JLayer mapLayer = mapLayers.get(i);
			// if (mapLayer.getDisplayScale()[1] <= layerDisplayScale
			// && layerDisplayScale <= mapLayer.getDisplayScale()[0])
			// mapLayer.Draw(g2D, AIWC, DC, affTrans, drawGrid);
			// }
		}

		// Color c = new Color(255,235,255);
		// g2D.setColor(c);
		// //g2D.drawRect(0, 0, 300, 300);
		// g2D.fillRect(0, 0, 300, 300);

	}

	/**
	 * Map绘图函数,绘制分级底图的情况
	 * @param g2D Java绘图对象
	 * @param WC AI范围
	 * @param DC 像素范围
	 * @param  regNameMapColor 分级底图HashMap对象
	 * @return void
	 * @throws
	 * @since 1.0
	 */
	public void Draw(Graphics2D g2D, Rectangle2D.Double WC,
			Rectangle2D.Double DC, HashMap regNameMapColor) {
		// Rectangle2D.Double AIWC = affGeoToAI.GetAIWC(WC);
		Rectangle2D.Double AIWC = WC;
		Affine affTrans = new Affine(AIWC, DC);
		// this.affTrans = new Affine(AIWC, DC);

		CalAffineTrans CAT = new CalAffineTrans(AIWC, DC);
		AffineTransform AT = CAT.getAT();
		g2D.transform(AT);

		double layerDisplayScale = this.CalLayerDisplayScale(AIWC, DC);

		//当计算出的layerDisplayScale小于最小时，以最小计算，大于最大时，以最大计算
		if (layerDisplayScale > maxLayerScale)
			layerDisplayScale = maxLayerScale;
		if (layerDisplayScale < minLayerScale)
			layerDisplayScale = minLayerScale;

		hasGrid = false;

		if (hasGrid == false) {
			
			//判断分级是区还是街道
			JRegionLayer regLayer;
			if(regNameMapColor.keySet().size() <= REGQUNUM){//对应区分级的情况,即有6对的时候
				regLayer = regQULayer;
			}else{
				regLayer = regJDLayer;
			}
			
			
			for (int i = 0; i < mapLayers.size(); i++) {
				JLayer mapLayer = mapLayers.get(i);
				if (mapLayer.getDisplayScale()[1] <= layerDisplayScale
						&& layerDisplayScale <= mapLayer.getDisplayScale()[0])
					if (!regLayer.getRegLayerName().equals(
							mapLayer.getLayerName())) {
						mapLayer.Draw(g2D, AIWC, DC, affTrans);
					} else {
						regLayer.Draw(g2D, AIWC, DC, affTrans, regNameMapColor);
					}
			}
		} else {
			
			
			JRegionLayer regLayer;
			if(regNameMapColor.keySet().size() == REGQUNUM){//对应区分级的情况,即有6对的时候
				regLayer = regQULayer;
			}else{
				regLayer = regJDLayer;
			}
			
			
			//if ((AIWC.width * AIWC.height) <= 0.6 * (this.WC.height * this.WC.width)) {
			int[] drawGrid = this
					.selectDrawGrid(rowNum, lineNum, AIWC, this.WC);
			for (int i = 0; i < mapLayers.size(); i++) {
				JLayer mapLayer = mapLayers.get(i);
				if (mapLayer.getDisplayScale()[1] <= layerDisplayScale
						&& layerDisplayScale <= mapLayer.getDisplayScale()[0])
					if (!regLayer.getRegLayerName().equals(
							mapLayer.getLayerName())) {
						mapLayer.Draw(g2D, AIWC, DC, affTrans, drawGrid);
					} else {
						regLayer.Draw(g2D, AIWC, DC, affTrans, regNameMapColor);
					}
			}
			//			} else {
			//				for (int i = 0; i < mapLayers.size(); i++) {
			//					JLayer mapLayer = mapLayers.get(i);
			//					if (mapLayer.getDisplayScale()[1] <= layerDisplayScale
			//							&& layerDisplayScale <= mapLayer.getDisplayScale()[0])
			//						if (!regLayer.getRegLayerName().equals(
			//								mapLayer.getLayerName())) {
			//							mapLayer.Draw(g2D, AIWC, DC, affTrans);
			//						} else {
			//							regLayer.Draw(g2D, AIWC, DC, affTrans,
			//									regNameMapColor);
			//						}
			//				}
			//			}
		}

	}

	/**
	 * 计算当前请求范围矩形所包含的索引行列号
	 * @param rowNum 索引行号 
	 * @param lineNum 索引列号
	 * @param curWC 当前矩形范围 
	 * @param WC 矩形范围
	 * @return int[]
	 * @throws 
	 * @since 1.0
	 */
	private int[] selectDrawGrid(int rowNum, int lineNum,
			Rectangle2D.Double curWC, Rectangle2D.Double WC) {
		int[] drawGrid = new int[4];
		double pt1X, pt1Y, pt2X, pt2Y;
		pt1X = curWC.getX();
		pt1Y = curWC.getY();
		pt2X = curWC.getX() + curWC.getWidth();
		pt2Y = curWC.getY() + curWC.getHeight();

		// drawGrid[0] = Math.max(0,(int)((pt1X-WC.getX())/gridSize[0]));
		// drawGrid[1] = Math.max(0,(int)((pt1Y-WC.getY())/gridSize[1]));
		// drawGrid[2] = Math.min(rowNum-1,(int)((pt2X-WC.getX())/gridSize[0]));
		// drawGrid[3] =
		// Math.min(lineNum-1,(int)((pt2Y-WC.getY())/gridSize[1]));

		drawGrid[0] = Math.min(lineNum - 1,
				(int) ((pt1X - WC.getX()) / gridSize[0]));
		drawGrid[1] = Math.min(rowNum - 1,
				(int) ((pt1Y - WC.getY()) / gridSize[1]));
		drawGrid[2] = Math.min(lineNum - 1,
				(int) ((pt2X - WC.getX()) / gridSize[0]));
		drawGrid[3] = Math.min(rowNum - 1,
				(int) ((pt2Y - WC.getY()) / gridSize[1]));
		for (int i = 0; i < 4; i++) {
			if (drawGrid[i] < 0)
				drawGrid[i] = 0;
		}

		// drawGrid[0] = (int) ((pt1X - WC.getX()) / gridSize[0]);
		// drawGrid[1] = (int) ((pt1Y - WC.getY()) / gridSize[1]);
		// drawGrid[2] = (int) ((pt2X - WC.getX()) / gridSize[0]);
		// drawGrid[3] = (int) ((pt2Y - WC.getY()) / gridSize[1]);
		//
		// System.out.println(drawGrid[1]+" "+drawGrid[0]+" "+drawGrid[3]+"
		// "+drawGrid[2]);

		return drawGrid;
	}

	public Rectangle2D.Double getWC() {
		return WC;
	}

	public void setWC(Rectangle2D.Double wc) {
		WC = wc;
	}

	public void ReadMapInfoText() {

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

			File file = new File(dataPath);
			if (!file.isDirectory()) {
//				System.out.println(dataPath);
				System.out.println("Map文件夹为空,请重新读入数据！");
				return;
			}

			String layerPath = null;
			String tofPath = null;
			String scaPath = null;

			String[] fileList = file.list();
			for (int i = 0; i < fileList.length; i++) {
				File listPath = new File(dataPath + "/" + fileList[i]);
				if (listPath.isDirectory()) {
					layerPath = dataPath + "/" + fileList[i];// D:\Atlas\20w\20w_Lyrs
				} else if (fileList[i].toLowerCase().endsWith(".tof")) {
					tofPath = dataPath + "/" + fileList[i];// D:\Atlas\20w\20w.TOF
				} else if (fileList[i].toLowerCase().endsWith(".sca")) {
					this.setHasSCA(true);
					scaPath = dataPath + "/" + fileList[i];
				} else {
					System.out.println("数据有误！");
					return;
				}
			}
			// 读TOF文件

			this.readTOF(tofPath, layerPath);
			this.readTAB(layerPath);
			if (this.getHasSCA() == true) {
				this.readSCA(scaPath);
			}

		} catch (IOException e) {

		}

	}

	/**
	 * 读取.tab文件数据
	 * @param layerPath 文件路径
	 * @return void
	 * @throws 
	 * @since 1.0
	 */
	private void readTAB(String layerPath) throws IOException {//仅保存为局部变量，有待完善
		String tabPath = layerPath + "/" + "NameTab.tab";
		FileInputStream fis = new FileInputStream(tabPath);
		DataInputStream dis = new DataInputStream(fis);
		HashMap<String, String> fileName = new HashMap<String, String>();
		HashMap<String, String> layerName = new HashMap<String, String>();
		HashMap<String, String> sysLayerName = new HashMap<String, String>();

		String fileChiName, fileEngName;
		fileEngName = dis.readUTF();
		fileChiName = dis.readUTF();
		fileName.put(fileEngName, fileChiName);

		int layerNum;
		layerNum = dis.readInt();
		for (int i = 0; i < layerNum; i++) {
			String layerEngName = dis.readUTF();
			String layerChiName = dis.readUTF();
			layerName.put(layerEngName, layerChiName);
		}

		int sysLayerNum;
		sysLayerNum = dis.readInt();
		for (int i = 0; i < sysLayerNum; i++) {
			String sysLayerEngName = dis.readUTF();
			String sysLayerChiName = dis.readUTF();
			sysLayerName.put(sysLayerEngName, sysLayerChiName);
		}
		dis.close();
		fis.close();
	}

	/**
	 * 读取.sca文件数据
	 * @param scaPath 文件路径
	 * @return void
	 * @throws 
	 * @since 1.0
	 */
	private void readSCA(String scaPath) throws IOException {
		FileInputStream fis = new FileInputStream(scaPath);
		DataInputStream dis = new DataInputStream(fis);
		String begin;
		int layerNum;
		begin = dis.readUTF();
		if (!begin.equals("LYRSCALES")) {
			System.out.println("TOF文件出错！");
			return;
		}
		layerNum = dis.readInt();
		nameMapLayerScale = new HashMap<String, double[]>(layerNum);
		for (int i = 0; i < layerNum; i++) {
			String layerName = dis.readUTF();
			double[] layerDisplayScale = new double[2];
			layerDisplayScale[0] = dis.readDouble();
			layerDisplayScale[1] = dis.readDouble();
			nameMapLayerScale.put(layerName, layerDisplayScale);

			//计算最大最小Scale
			if (i == 0) {
				maxLayerScale = layerDisplayScale[0];
				minLayerScale = layerDisplayScale[1];
			} else {
				if (layerDisplayScale[0] > maxLayerScale) {
					maxLayerScale = layerDisplayScale[0];
				}
				if (layerDisplayScale[1] < minLayerScale) {
					minLayerScale = layerDisplayScale[1];
				}
			}
		}

		dis.close();
		fis.close();
	}

	/**
	 * 读取.tof文件数据
	 * @param tofPath 文件路径
	 * @return void
	 * @throws 
	 * @since 1.0
	 */
	private void readTOF(String tofPath, String layerPath) throws IOException {
		String regionLayerPath = null;
		int layerNum, sysLayerNum;
		// int rowNum, lineNum;
		final int INT_DELETE = 4; //删除Geo_QU 或 Geo_JD
		// Boolean hasGrid;
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
		// System.out.println(WC);

		rowNum = dis.readInt();
		lineNum = dis.readInt();
		if (rowNum * lineNum < 0) {
			System.out.println("网格数错误！");
			return;
		} else if (rowNum == 0 && lineNum == 0) {
			hasGrid = false;
		} else {
			hasGrid = true;
			gridSize = new double[2];
			gridSize[0] = (bx2 - bx1) / lineNum;
			gridSize[1] = (by2 - by1) / rowNum;
		}
		// Rectangle2D.Double gridRect[][] = new
		// Rectangle2D.Double[rowNum][lineNum];
		// CalculateGrids cg = new CalculateGrids(rowNum, lineNum, WC);
		// gridRect = cg.getIndexRect();

		// recRect = new Rectangle2D.Double(x1, y1, x2, y2);
		long totalObjNum = dis.readLong();

		layerNum = dis.readInt();
		for (int j = 0; j < layerNum; j++) {
			String layerName = dis.readUTF();
			layerNameIndex.add(j, layerName);
			String layerPathFull = layerPath + "/" + layerName;
			JLayer layer = new JLayer();
			layer.setLayerName(layerName);// 增加每层的name属性
			layer.ReadLayerInfoBin(layerPathFull, hasGrid);// 得出每个文件的绝对路径,缺后缀名
			mapLayers.add(j, layer);
		}
		layerNameIndex.trimToSize();
		mapLayers.trimToSize();
		sysLayerNum = dis.readInt();// 读取sys文件，待完善
		for (int k = 0; k < sysLayerNum; k++) {
			String sysLayerName = dis.readUTF();
			if (sysLayerName.toLowerCase().equals("sys_ctrlpoints")) {
				String ctrlPtPath = layerPath + "/" + sysLayerName + ".GLF";
				this.readCtrlPT(ctrlPtPath);

			} else if (sysLayerName.toLowerCase().equals("sys_georoute")) {

			} else {
//				if (sysLayerName.startsWith("Geo_QU")) {
				if (sysLayerName.startsWith("Geo_")) {
					regQULayer = new JRegionLayer();

					regionLayerPath = layerPath + "/" + sysLayerName + ".GLF";
					regQULayer.ReadLayerInfoBin(regionLayerPath);

					// 将RegionLayer对应不画的层标出

					sysLayerName = sysLayerName.substring(INT_DELETE);
//					System.out.println(sysLayerName);
					regQULayer.setRegLayerName(sysLayerName);
				} else {
					regJDLayer = new JRegionLayer();
					regionLayerPath = layerPath + "/" + sysLayerName + ".GLF";
					regJDLayer.ReadLayerInfoBin(regionLayerPath);

					// 将RegionLayer对应不画的层标出

					sysLayerName = sysLayerName.substring(INT_DELETE);
					regJDLayer.setRegLayerName(sysLayerName);
				}
				/*
				 * for (int i = 0; i < mapLayers.size(); i++) { if
				 * (mapLayers.get(i).getLayerName().equals(sysLayerName)) {
				 * mapLayers.get(i).setIsReplaceLayer(true); } }
				 */
			}
		}
		dis.close();
		fis.close();
	}

	/**
	 * 读取SYS_CtrlPoints.GLF文件数据
	 * @param ctrlPtPath 文件路径
	 * @return void
	 * @throws 
	 * @since 1.0
	 */
	private void readCtrlPT(String ctrlPtPath) {
		try {
			FileInputStream fis = new FileInputStream(ctrlPtPath);
			DataInputStream dis = new DataInputStream(fis);
			Point2D.Double[] AIPt, GeoPt;
			String begin;
			int ctrlPtNum;
			begin = dis.readUTF();
			if (!begin.equals("TOFGEOLYAER")) {
				System.out.println("TOF文件出错！");
				return;
			}
			ctrlPtNum = dis.readInt();
			if (ctrlPtNum < 3) {
				System.out.println("控制点数目有误！");
				return;
			}
			AIPt = new Point2D.Double[ctrlPtNum];
			GeoPt = new Point2D.Double[ctrlPtNum];
			for (int i = 0; i < ctrlPtNum; i++) {
				double AIX = dis.readDouble();
				double AIY = dis.readDouble();
				double GeoX = dis.readDouble();
				double GeoY = dis.readDouble();
				AIPt[i] = new Point2D.Double();
				GeoPt[i] = new Point2D.Double();
				AIPt[i].setLocation(AIX, AIY);
				GeoPt[i].setLocation(GeoX, GeoY);
			}
			affGeoToAI = new AffineGeoToAI(GeoPt, AIPt);
			dis.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * public double getMapScale() { return mapScale; }
	 * 
	 * public void setMapScale(double mapScale) { this.mapScale = mapScale; }
	 */

	/*
	 * public boolean isBolDrawRegionLayer() { return bolDrawRegionLayer; }
	 * 
	 * public void setBolDrawRegionLayer(boolean bolDrawRegionLayer) {
	 * this.bolDrawRegionLayer = bolDrawRegionLayer; }
	 */

//	public JRegionLayer getRegLayer() {
//		return regLayer;
//	}
//
//	public void setRegLayer(JRegionLayer regLayer) {
//		this.regLayer = regLayer;
//	}

	public Boolean getHasSCA() {
		return hasSCA;
	}

	public void setHasSCA(Boolean hasSCA) {
		this.hasSCA = hasSCA;
	}

	public AffineGeoToAI getAffGeoToAI() {
		return affGeoToAI;
	}

	public void setAffGeoToAI(AffineGeoToAI affGeoToAI) {
		this.affGeoToAI = affGeoToAI;
	}

	public JRegionLayer getRegQULayer() {
		return regQULayer;
	}

	public void setRegQULayer(JRegionLayer regQULayer) {
		this.regQULayer = regQULayer;
	}

	public JRegionLayer getRegJDLayer() {
		return regJDLayer;
	}

	public void setRegJDLayer(JRegionLayer regJDLayer) {
		this.regJDLayer = regJDLayer;
	}

	/*
	 * public double[] getAffPara() { return affPara; }
	 * 
	 * public void setAffPara(double[] affPara) { this.affPara = affPara; }
	 */

	/*
	 * public Affine getAffTrans() { return affTrans; }
	 * 
	 * public void setAffTrans(Affine affTrans) { this.affTrans = affTrans; }
	 */

}
