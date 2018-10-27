package com.zz.bglayer;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.geom.*;

//import whu.map.JAnnotation;
//import whu.map.JCompoundPolygon;
//import whu.map.JObject;
//import whu.map.JPolygon;
//import whu.map.JPolyline;
//import whu.map.JRaster;
/**
 * Layer底图图层类
 * 
 * @author L J
 * @version 1.0
 */
public class JLayer {

	ArrayList<JObject> arrObjects;
	// Rectangle2D.Double gridRect[][];
	TreeSet<Integer> objIndex[][];
	int rowNum, lineNum;
	boolean isVisible;

	private Boolean boolDraw;
	private double displayScale[];
	// private Boolean isReplaceLayer;
	private String layerName;

	// private Boolean hasGrid;

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public JLayer() {
		arrObjects = new ArrayList<JObject>();
		// gridRect = null;
		objIndex = null;
		rowNum = 0;
		lineNum = 0;
		isVisible = true;

		boolDraw = false;
		displayScale = null;
		// isReplaceLayer = false;
		// hasGrid = false;
	}

	public void AddObject(JObject Obj) {
		arrObjects.add(Obj);
	}

	/**
	 * Layer绘图函数,当不存在索引的情况
	 * 
	 * @param g2D
	 *            Java绘图对象
	 * @param WC
	 *            AI范围
	 * @param DC
	 *            像素范围
	 * @param affTrans
	 *            仿射变换对象
	 * @return void
	 * @throws
	 * @since 1.0
	 */
	public void Draw(Graphics2D g2D, Rectangle2D.Double WC,
			Rectangle2D.Double DC, Affine affTrans) {
		if (boolDraw == true) {
			for (int i = 0; i < arrObjects.size(); i++) {
				JObject jo = arrObjects.get(i);
				if (jo.getRect().intersects(WC)) {
					jo.Draw(g2D);
				} else {
					continue;
				}
			}
		}
	}

	/*
	 * public void Draw(Graphics2D g2D, Rectangle2D.Double WC,
	 * Rectangle2D.Double DC, Affine affTrans, int[] drawGrid) { if (boolDraw ==
	 * true) { for(int i=drawGrid[0];i<=drawGrid[2];i++) for(int
	 * j=drawGrid[1];j<=drawGrid[3];j++){ //if (objIndex[i][j] == null)
	 * //continue; for (int k = 0; k < objIndex[i][j].size(); k++) {
	 * arrObjects.get(objIndex[i][j].get(k)).setIsDraw(//改变私有属性待完善 true); } }
	 * for (int i = 0; i < arrObjects.size(); i++) { if
	 * (arrObjects.get(i).getIsDraw() == true) { arrObjects.get(i).Draw(g2D, WC,
	 * DC, affTrans); arrObjects.get(i).setIsDraw(false); } else { continue; } } } }
	 */
	/**
	 * Layer绘图函数,存在索引的情况
	 * 
	 * @param g2D
	 *            Java绘图对象
	 * @param WC
	 *            AI范围
	 * @param DC
	 *            像素范围
	 * @param affTrans
	 *            仿射变换对象
	 * @param drawGrid
	 *            选中索引的行列号
	 * @return void
	 * @throws
	 * @since 1.0
	 */
	public void Draw(Graphics2D g2D, Rectangle2D.Double WC,
			Rectangle2D.Double DC, Affine affTrans, int[] drawGrid) {
		if (boolDraw == true) {
			TreeSet<Integer> selectDrawObj = new TreeSet<Integer>();
			for (int i = drawGrid[1]; i <= drawGrid[3]; i++)
				for (int j = drawGrid[0]; j <= drawGrid[2]; j++) {
					// if (objIndex[i][j] == null)
					// continue;
					Union(selectDrawObj, objIndex[i][j]);

				}

			Iterator<Integer> iter = selectDrawObj.iterator();
			// System.out.println(Scale);
			while (iter.hasNext()) {
				JObject obj = arrObjects.get(iter.next());
				obj.Draw(g2D);
				// g2D.drawRect((int)obj.getRect().getX(),(int)obj.getRect().getY()
				// , (int)obj.getRect().getWidth(),
				// (int)obj.getRect().getHeight());
			}

		}
	}

	/**
	 * 将setB并入setA集合中
	 * 
	 * @param setA
	 *            TreeSet目标集
	 * @param setB
	 *            TreeSet目标集
	 * @return void
	 * @throws
	 * @since 1.0
	 */
	private void Union(TreeSet<Integer> setA, TreeSet<Integer> setB) {
		setA.addAll(setB);
	}

	/*
	 * private void selectIsDrawObj(Rectangle2D.Double WC){ Point2D.Double pt1 =
	 * new Point2D.Double(WC.getX(), WC.getY()); Point2D.Double pt2 = new
	 * Point2D.Double(WC.getX() + WC.getWidth(), WC.getY() + WC.getHeight());
	 * int pt1RowNum = 0; int pt1LineNum = 0; int pt2RowNum =
	 * rowNum-1;//对于Contains不包含边界的处理,针对于全图的情况 int pt2LineNum = lineNum-1; outer:
	 * for (int i = 0; i < rowNum; i++) { for (int j = 0; j < lineNum; j++) { if
	 * (gridRect[i][j].contains(pt1)) { pt1RowNum = i; pt1LineNum = j; break
	 * outer; } } } outer: for (int i = pt1RowNum; i < rowNum; i++) { for (int j =
	 * pt1LineNum; j < lineNum; j++) { if (gridRect[i][j].contains(pt2)) {
	 * pt2RowNum = i; pt2LineNum = j; break outer; } } } /*if (pt2RowNum == 0 ||
	 * pt2LineNum == 0) {//对于Contains不包含边界的处理,针对于全图的情况 pt2RowNum = rowNum - 1;
	 * pt2LineNum = lineNum - 1; }
	 */
	/*
	 * for (int i = pt1RowNum; i <= pt2RowNum; i++) { for (int j = pt1LineNum; j <=
	 * pt2LineNum; j++) { if (objIndex[i][j] == null) continue; for (int k = 0;
	 * k < objIndex[i][j].size(); k++) {
	 * arrObjects.get(objIndex[i][j].get(k)).setIsDraw( true); } } } }
	 */

	public void ReadLayerInfoText() {

	}

	/**
	 * 读取Layer数据
	 * 
	 * @param dataPath
	 *            文件路径
	 * @param hasGrid
	 *            是否存在索引
	 * @return void
	 * @throws
	 * @since 1.0
	 */
	public void ReadLayerInfoBin(String dataPath, Boolean hasGrid)
			throws IOException {
		// Boolean hasGrid = hasGrid;
		String objPath = dataPath + ".ent";
		this.readEnt(objPath, dataPath);
		if (hasGrid == true) {
			String gridPath = dataPath + ".grd";
			this.readGrd(gridPath);
		}
	}

	/**
	 * 读取LayerEnt实体数据
	 * 
	 * @param objPath
	 *            ent文件路径
	 * @param dataPath
	 *            文件路径
	 * @return void
	 * @throws
	 * @since 1.0
	 */
	private void readEnt(String objPath, String dataPath) {
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

				JObject jo;
				// System.out.println(objStyle);

				if (objStyle.equals("A")) {
					jo = new JAnnotation();

					jo.ReadObjectInfoBin(dis);
					arrObjects.add(i, jo);
				} else if (objStyle.equals("L")) {
					jo = new JPolyline();

					jo.ReadObjectInfoBin(dis);
					arrObjects.add(i, jo);

				} else if (objStyle.equals("M")) {
					jo = new JPolygon();

					jo.ReadObjectInfoBin(dis);
					arrObjects.add(i, jo);

				} else if (objStyle.equals("C")) {
					jo = new JCompoundPolygon();

					jo.ReadObjectInfoBin(dis);
					arrObjects.add(i, jo);
				}

				else if (objStyle.equals("R")) {
					jo = new JRaster();

					jo.ReadObjectInfoBin(dis);
					jo.ReadBufferedImage(dataPath);
					arrObjects.add(i, jo);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 读取Grid索引数据
	 * 
	 * @param gridPath
	 *            grid文件路径
	 * @return void
	 * @throws
	 * @since 1.0
	 */
	private void readGrd(String gridPath) {
		try {
			FileInputStream fis = new FileInputStream(gridPath);// 读grd文件
			DataInputStream dis = new DataInputStream(fis);

			rowNum = dis.readInt();
			lineNum = dis.readInt();

			if (rowNum * lineNum <= 0) {
				System.out.println("网格数错误！");
				return;
			}
			// this.gridRect = new Rectangle2D.Double[rowNum][lineNum];
			// this.gridRect = gridRect;
			objIndex = new TreeSet[rowNum][lineNum];

			for (int i = 0; i < rowNum; i++) {
				for (int j = 0; j < lineNum; j++) {
					int indexNum = dis.readInt();// 格网号
					int objIndexNum = dis.readInt();// 个网中的目标数
					objIndex[i][j] = new TreeSet<Integer>();
					if (objIndexNum == 0) {// 个网中的目标数
						// objIndex[i][j] = null;
					} else {
						for (int k = 0; k < objIndexNum; k++) {
							int objIndexCode = dis.readInt();// 目标索引号
							objIndex[i][j].add(objIndexCode);
						}
					}
				}
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

	/*
	 * public Boolean getIsReplaceLayer() { return isReplaceLayer; }
	 * 
	 * public void setIsReplaceLayer(Boolean isReplaceLayer) {
	 * this.isReplaceLayer = isReplaceLayer; }
	 */
}
