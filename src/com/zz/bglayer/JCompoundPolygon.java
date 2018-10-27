package com.zz.bglayer;

import java.awt.BasicStroke;
import java.awt.geom.*;
import java.awt.Graphics2D;
//import java.awt.RenderingHints;
import java.util.*;
import java.io.*;
import java.awt.Color;
import java.awt.geom.GeneralPath;

import com.zz.util.JUtil;
/** 
 * CompoundPolygon复杂对象读取数据绘图类
 * @author L J
 * @version 1.0 
 */
public class JCompoundPolygon extends JObject {

	// private JSegment [] arrSegment;
	// private Vector Points;

	// private JPolygonStruct Polygon;

	private JLayer Layer;

	//private Rectangle2D.Double rect;

	//private int targetNum;

	//private float[] lineStyles;

	// private JSegment segment;
	private JPolygonStruct arrPolygon;
	//private Vector segments;// store polygon

	public JCompoundPolygon() {
		// arrSegment = null;
		// Points = new Vector();
		arrPolygon = new JPolygonStruct();
		//segments = new Vector();
		Layer = null;
		//Rect = null;
		//targetNum = 0;
		//lineStyles = null;
		// segment = new JSegment();
		//arrPolygon = null;
	}

	/*public char GetType() {
		return 0;
	}*/

	/*
	 * public JPolygonStruct getPolygon() { return Polygon; }
	 * 
	 * public void setPolygon(JPolygonStruct polygon) { Polygon = polygon; }
	 */

	public JLayer getLayer() {
		return Layer;
	}

	public void setLayer(JLayer layer) {
		Layer = layer;
	}

    /**
     * CompoundPolygon绘图函数
     * @param g2D Java绘图对象
     * @param affTrans 仿射变换对象
     * @return void
     * @throws 
     * @since 1.0
     */
	public void Draw(Graphics2D g2D) {

//		int size=0;
//		JSegment []curSegment = new JSegment[arrPolygon.getSegNum()];
//		curSegment = arrPolygon.getSegment();
//		for(int i=0;i<arrPolygon.getSegNum();i++){
//			size = size+curSegment[i].getNPtCount();
//		}
//		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
//				size);
//		//Affine af = new Affine(wcRect, dcRect);
//		double Scale = affTrans.getScale();
//		for (int i = 0; i < arrPolygon.getSegNum(); i++) {
//			ArrayList <Point2D.Double>Points = curSegment[i].getPoints();
//			if (curSegment[i].isBIsBezier() == false) {//不是Bezier的情况
//				LinePath bz = new LinePath(path,affTrans,Points);
//				path = bz.getPath();
//			}else{
//				BezierPath bz = new BezierPath(path,affTrans,Points);
//				path = bz.getPath();
//			}
//		}
		
		GeneralPath path = arrPolygon.getPath();
		
		/*g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);*/
		if(arrPolygon.isBHasBoundCol()==true){
			g2D.setColor(arrPolygon.getLLineColor());
		}
//		if (arrPolygon.getNLineStyleCount() == 0) {// 不是虚线的情况
//			BasicStroke bs = new BasicStroke(
//					(float) (arrPolygon
//							.getFLineWidth()/Scale),
//					BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
//			g2D.setStroke(bs);
//
//		} else {
//			float[] arrPolygonStyle = arrPolygon.getLineStyle();
//			float[] curlineStyles = new float [arrPolygon.getNLineStyleCount()];//生成一个线型数组
//			for(int j=0;j<arrPolygon.getNLineStyleCount();j++){
//				//lineStyles1[j] = lineStyles[j];
//				curlineStyles[j] = (float)(arrPolygonStyle[j]/Scale);
//			}
//			BasicStroke bs = new BasicStroke(
//					(float) (arrPolygon.getFLineWidth()/Scale),
//					BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
//					0, curlineStyles, 0);
//			g2D.setStroke(bs);
//		}
		
		BasicStroke bs = arrPolygon.getBs();
		g2D.setStroke(bs);
		
		g2D.draw(path);
		if (arrPolygon.isBIsFill() == true) {
			g2D.setColor(arrPolygon.getLFillColor());
			g2D.fill(path);
		}


	}

	// Read map infomations
	public void ReadObjectInfoText() {

	}

    /**
     * 读取CompoundPolygon数据
     * @param dis 文件头指针
     * @return void
     * @throws 
     * @since 1.0
     */
	public void ReadObjectInfoBin(DataInputStream dis) throws IOException {
		double bx1 = dis.readDouble();
		double by1 = dis.readDouble();
		double bx2 = dis.readDouble();
		double by2 = dis.readDouble();
		//System.out.println(bx1+" "+by1+" "+bx2+" "+by2);
		Rectangle2D.Double rect = new Rectangle2D.Double(bx1,by1,bx2-bx1,by2-by1);
		this.setRect(rect);
		//arrPolygon.setRect(rect);
		arrPolygon.setFLineWidth(dis.readFloat());
		//System.out.println(arrPolygon.getFLineWidth());
		arrPolygon.setBHasBoundCol(dis.readBoolean());
		//System.out.println(arrPolygon.isBHasBoundCol());
		if (arrPolygon.isBHasBoundCol() == true) {
			int lineColor = dis.readInt();
			int[] rgb = JUtil.GetRGB(lineColor);
			Color color = new Color(rgb[2], rgb[1], rgb[0]);
			arrPolygon.setLLineColor(color);
			//System.out.println(arrPolygon.getLLineColor());
		}
		arrPolygon.setBIsFill(dis.readBoolean());
		//System.out.println(arrPolygon.isBIsFill());
		if (arrPolygon.isBIsFill() == true) {
			int lineColor = dis.readInt();
			int[] rgb = JUtil.GetRGB(lineColor);
			Color color = new Color(rgb[2], rgb[1], rgb[0]);
			arrPolygon.setLFillColor(color);
			//System.out.println(arrPolygon.getLFillColor());
		}

		int lineStyleNum = dis.readInt();
		//System.out.println(lineStyleNum);
		arrPolygon.setNLineStyleCount(lineStyleNum);
		if (arrPolygon.getNLineStyleCount() > 0) {
			float []lineStyles = new float[lineStyleNum];
			for (int t = 0; t < arrPolygon.getNLineStyleCount(); t++)
				lineStyles[t] = (float) (dis.readDouble()*arrPolygon.getFLineWidth());
			//System.out.println(lineStyles[t]);
			arrPolygon.setLineStyle(lineStyles);
		}

		int segmentsNum = dis.readInt();
		//System.out.println(segmentsNum);
		// arrPolygon = new JPolygonStruct[targetNum];
		//segments.setSize(targetNum);
		arrPolygon.setSegNum(segmentsNum);
		JSegment [] arrSegments = new JSegment[arrPolygon.getSegNum()];
		for(int i=0;i<arrPolygon.getSegNum();i++){
			arrSegments[i] = new JSegment();
			String segmentsStyle = dis.readUTF();
			//System.out.println(segmentsStyle);
			arrSegments[i].setBIsBezier(dis.readBoolean());
			//System.out.println(arrSegments[i].isBIsBezier());
			arrSegments[i].setNPtCount(dis.readInt());
			//System.out.println(arrSegments[i].getNPtCount());
			ArrayList <Point2D.Double>Points = new ArrayList<Point2D.Double>();
			for (int j = 0; j < arrSegments[i].getNPtCount(); j++) {
				Point2D.Double pt = new Point2D.Double();
				double x = dis.readDouble();
				double y = dis.readDouble();
				//System.out.println(x+" "+y);
				pt.setLocation(x,y);
				Points.add(pt);
			}
			arrSegments[i].setPoints(Points);
		    //arrPolygon.setSegment(arrSegments[i],i);
		}
//		arrPolygon.setSegment(arrSegments);
		
		
		int size=0;
		for(int i=0;i<arrPolygon.getSegNum();i++){
			size = arrSegments[i].getNPtCount();
		}
		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
				size);
		//Affine af = new Affine(wcRect, dcRect);
		for (int i = 0; i < arrPolygon.getSegNum(); i++) {
			ArrayList <Point2D.Double>Points = arrSegments[i].getPoints();
			if (arrSegments[i].isBIsBezier() == false) {//不是Bezier的情况
				LinePath bz = new LinePath(path,Points);
				path = bz.getPath();
			}else{
				BezierPath bz = new BezierPath(path,Points);
				path = bz.getPath();
			}
		}
		arrPolygon.setPath(path);
		
		
		BasicStroke bs;
		if (arrPolygon.getNLineStyleCount() == 0) {// 不是虚线的情况
			bs = new BasicStroke((float) (arrPolygon.getFLineWidth()), BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_ROUND);

		} else {
			bs = new BasicStroke((float) (arrPolygon.getFLineWidth()), BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_ROUND, 0, arrPolygon.getLineStyle(), 0);
		}
		arrPolygon.setBs(bs);
		

	}

	public JPolygonStruct getArrPolygon() {
		return arrPolygon;
	}

	public void setArrPolygon(JPolygonStruct arrPolygon) {
		this.arrPolygon = arrPolygon;
	}
}
