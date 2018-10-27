package com.zz.bglayer;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
//import java.awt.Rectangle;
//import java.awt.RenderingHints;
import java.util.*;
import java.io.*;
import java.awt.Color;
import java.awt.geom.*;

import com.zz.util.JUtil;
/** 
 * Polygon读取数据绘图类
 * @author L J
 * @version 1.0 
 */
public class JPolygon extends JObject {

	//private Vector Points;

	private JPolygonStruct tagPolygon;

	private JLayer Layer;

	//private float[] lineStyles;

	public JPolygon() {
		//Points = new ArrayList();
		tagPolygon = new JPolygonStruct();
		Layer = null;
		//lineStyles = null;
	}

	/*public char GetType() {
		return 0;
	}*/
/*
	public ArrayList getPoints() {
		return Points;
	}

	public void setPoints(ArrayList points) {
		Points = points;
	}*/

	public JPolygonStruct getTagPolygon() {
		return tagPolygon;
	}

	public void setTagPolygon(JPolygonStruct tagPolygon) {
		this.tagPolygon = tagPolygon;
	}

	public JLayer getLayer() {
		return Layer;
	}

	public void setLayer(JLayer layer) {
		Layer = layer;
	}

    /**
     * Polygon绘图函数
     * @param g2D Java绘图对象
     * @param affTrans 仿射变换对象
     * @return void
     * @throws 
     * @since 1.0
     */
	public void Draw(Graphics2D g2D) {
//		JSegment[] curSegment = new JSegment[1];
//		curSegment = tagPolygon.getSegment();
//		int size = curSegment[0].getNPtCount();
//		
//		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, size);
//		//Affine af = new Affine(wcRect, dcRect);
//		double Scale = affTrans.getScale();
//		ArrayList<Point2D.Double> Points = curSegment[0].getPoints();
//		if (curSegment[0].isBIsBezier() == false) {// 不是Bezier的情况
//			LinePath bz = new LinePath(path, affTrans, Points);
//			path = bz.getPath();
//		} else {
//			BezierPath bz = new BezierPath(path, affTrans, Points);
//			path = bz.getPath();
//		}
		
		GeneralPath path = tagPolygon.getPath();
		
		/*g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);*/
		if(tagPolygon.isBHasBoundCol()==true){
			g2D.setColor(tagPolygon.getLLineColor());
		}
//		if (tagPolygon.getNLineStyleCount() == 0) {// 不是虚线的情况
//			BasicStroke bs = new BasicStroke(
//					(float) (tagPolygon.getFLineWidth()/Scale),
//					BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
//			g2D.setStroke(bs);
//
//		} else {
//			float[] tagPolygonStyle = tagPolygon.getLineStyle();
//			float[] curlineStyles = new float [tagPolygon.getNLineStyleCount()];//生成一个线型数组
//			for(int j=0;j<tagPolygon.getNLineStyleCount();j++){
//				//lineStyles1[j] = lineStyles[j];
//				curlineStyles[j] = (float)(tagPolygonStyle[j]/Scale);
//			}
//			BasicStroke bs = new BasicStroke(
//					(float) (tagPolygon.getFLineWidth()/Scale),
//					BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
//					0, curlineStyles, 0);
//			g2D.setStroke(bs);
//		}
		
		BasicStroke bs = tagPolygon.getBs();
		g2D.setStroke(bs);
		
		g2D.draw(path);
		if (tagPolygon.isBIsFill() == true) {
			g2D.setColor(tagPolygon.getLFillColor());
			g2D.fill(path);
		}

	}

	// Read map infomations
	public void ReadObjectInfoText() {

	}

    /**
     * 读取Polygon数据
     * @param dis 文件头指针
     * @return void
     * @throws 
     * @since 1.0
     */
	public void ReadObjectInfoBin(DataInputStream dis) {
		try {
			double bx1 = dis.readDouble();
			double by1 = dis.readDouble();
			double bx2 = dis.readDouble();
			double by2 = dis.readDouble();
			//System.out.println(bx1+" "+by1+" "+bx2+" "+by2);
			Rectangle2D.Double rect = new Rectangle2D.Double(bx1,by1,bx2-bx1,by2-by1);
			this.setRect(rect);
			//tagPolygon.setRect(rect);
			
			tagPolygon.setFLineWidth(dis.readFloat());
			//System.out.println(tagPolygon.getFLineWidth());
			tagPolygon.setBHasBoundCol(dis.readBoolean());
			 //System.out.println(tagPolygon.isBHasBoundCol());
			if (tagPolygon.isBHasBoundCol() == true) {
				int linecolor = dis.readInt();
				int[] rgb = JUtil.GetRGB(linecolor);
				Color color = new Color(rgb[2], rgb[1], rgb[0]);
				tagPolygon.setLLineColor(color);
				 //System.out.println(tagPolygon.getLLineColor());
			}
			tagPolygon.setBIsFill(dis.readBoolean());
			//System.out.println(tagPolygon.isBIsFill());
			if (tagPolygon.isBIsFill() == true) {
				int linecolor = dis.readInt();
				int[] rgb = JUtil.GetRGB(linecolor);
				Color color = new Color(rgb[2], rgb[1], rgb[0]);
				tagPolygon.setLFillColor(color);
				 //System.out.println(tagPolygon.getLFillColor());
			}

			int lineStyleNum = dis.readInt();
			//System.out.println(lineStyleNum);
			tagPolygon.setNLineStyleCount(lineStyleNum);
			if (tagPolygon.getNLineStyleCount() > 0) {
				float[] lineStyles = new float[lineStyleNum];
				for (int t = 0; t < tagPolygon.getNLineStyleCount(); t++)
					lineStyles[t] = (float) (dis.readDouble()*tagPolygon.getFLineWidth());
				//System.out.println(lineStyles[t]);
				tagPolygon.setLineStyle(lineStyles);
			}

//			tagPolygon.setSegNum(1);
			JSegment tagSegments = new JSegment();
			tagSegments.setBIsBezier(dis.readBoolean());
			//System.out.println(tagSegments[0].isBIsBezier());
			tagSegments.setNPtCount(dis.readInt());
			//System.out.println(tagSegments[0].getNPtCount());
			ArrayList<Point2D.Double> Points = new ArrayList<Point2D.Double>();
			for (int j = 0; j < tagSegments.getNPtCount(); j++) {
				Point2D.Double pt = new Point2D.Double();
				double x = dis.readDouble();
				double y = dis.readDouble();
				//System.out.println(x+" "+y);
				pt.setLocation(x,y);
				Points.add(pt);
			}
			tagSegments.setPoints(Points);
			//tagPolygon.setSegment(arrSegments[i],i);
//			tagPolygon.setSegment(tagSegments);
		
			
			
			

//			curSegment = tagPolygon.getSegment();
			int size = tagSegments.getNPtCount();
			
			GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, size);
			//Affine af = new Affine(wcRect, dcRect);
//			ArrayList<Point2D.Double> Points = tagSegments.getPoints();
			if (tagSegments.isBIsBezier() == false) {// 不是Bezier的情况
				LinePath bz = new LinePath(path, Points);
				path = bz.getPath();
			} else {
				BezierPath bz = new BezierPath(path, Points);
				path = bz.getPath();
			}
			tagPolygon.setPath(path);
			
			BasicStroke bs;
			if (tagPolygon.getNLineStyleCount() == 0) {// 不是虚线的情况
				bs = new BasicStroke((float) (tagPolygon.getFLineWidth()), BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_ROUND);

			} else {
				bs = new BasicStroke((float) (tagPolygon.getFLineWidth()), BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_ROUND, 0, tagPolygon.getLineStyle(), 0);
			}
			tagPolygon.setBs(bs);
			


		} catch (IOException e) {
		}
	}
}
