package com.zz.bglayer;

import java.awt.Graphics2D; //import java.awt.Point;
//import java.awt.Rectangle;
//import java.awt.RenderingHints; //import java.awt.geom.GeneralPath;
import java.io.*;
import java.util.*;
import java.awt.Color;
import java.awt.BasicStroke; //import java.awt.geom.GeneralPath;
import java.awt.geom.*;

import com.zz.util.JUtil;
/** 
 * Polyline对象读取数据绘图类
 * @author L J
 * @version 1.0 
 */
public class JPolyline extends JObject {

	/*public char GetType() {
		return 0;
	}*/

	//private ArrayList<Point2D.Double> Points;

	private JPolylineStruct tagLine;

	private JLayer Layer;

	// private JRect Rect;

	//private Boolean isBezier;

	//private float[] lineStyles;// 修改

	public JPolyline() {
		//Points = new ArrayList<Point2D.Double>();
		tagLine = new JPolylineStruct();
		// Rect = null;
		Layer = null;
		//isBezier = false;
		//lineStyles = null;

	}

	/*public ArrayList<Point2D.Double> getPoints() {
		return Points;
	}

	public void setPoints(ArrayList<Point2D.Double> points) {
		Points = points;
	}*/

	public JPolylineStruct getTagLine() {
		return tagLine;
	}

	public void setTagLine(JPolylineStruct tagLine) {
		this.tagLine = tagLine;
	}

	public JLayer getLayer() {
		return Layer;
	}

	public void setLayer(JLayer layer) {
		Layer = layer;
	}

	/*public Boolean getIsBezier() {
		return isBezier;
	}

	public void setIsBezier(Boolean isBezier) {
		this.isBezier = isBezier;
	}*/

    /**
     * Polyline绘图函数
     * @param g2D Java绘图对象
     * @param affTrans 仿射变换对象
     * @return void
     * @throws 
     * @since 1.0
     */
	public void Draw(Graphics2D g2D) {
//		JSegment curSegment = new JSegment();
//		curSegment = tagLine.getSegment();
//		int size = curSegment.getNPtCount();
//
//		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, size);
//		//Affine af = new Affine(wcRect, dcRect);
//		double Scale = affTrans.getScale();
//		ArrayList<Point2D.Double> Points = curSegment.getPoints();
//		if (curSegment.isBIsBezier() == false) {// 不是Bezier的情况
//			LinePath lp = new LinePath(path, affTrans, Points);
//			path = lp.getPath();
//		} else {
//			BezierPath bp = new BezierPath(path, affTrans, Points);
//			path = bp.getPath();
//		}

		/*g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);*/
		
		GeneralPath path = tagLine.getPath();
		
		g2D.setColor(tagLine.getLLineColor());

//		if (tagLine.getNLineStyleCount() == 0) {// 不是虚线的情况
//			BasicStroke bs = new BasicStroke((float) (tagLine.getFLineWidth()/Scale), BasicStroke.CAP_BUTT,
//					BasicStroke.JOIN_ROUND);
//			g2D.setStroke(bs);
//
//		} else {
//			float[] tagLineStyle = tagLine.getLineStyle();
//			float[] curlineStyles = new float[tagLine.getNLineStyleCount()];//生成一个线型数组
//			for (int j = 0; j < tagLine.getNLineStyleCount(); j++) {
//				//lineStyles1[j] = lineStyles[j];
//				curlineStyles[j] = (float) (tagLineStyle[j]/Scale);
//			}
//			BasicStroke bs = new BasicStroke((float) (tagLine.getFLineWidth()/Scale), BasicStroke.CAP_BUTT,
//					BasicStroke.JOIN_ROUND, 0, curlineStyles, 0);
//			g2D.setStroke(bs);
//		}
		
		BasicStroke bs = tagLine.getBs();
		g2D.setStroke(bs);
		g2D.draw(path);

	}

	// Read map infomations
	public void ReadObjectInfoText() {
	}

    /**
     * 读取Polyline数据
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
			//tagLine.setRect(rect);
			
			float lineWidth = dis.readFloat();
			tagLine.setFLineWidth(lineWidth);//精度有损失，待完善
			//System.out.println(lineWidth);

			int lineColor = dis.readInt();
			int[] rgb = JUtil.GetRGB(lineColor);
			Color color = new Color(rgb[2], rgb[1], rgb[0]);
			tagLine.setLLineColor(color);
			//System.out.println(lineColor);

			int lineStyleNum = dis.readInt();
			tagLine.setNLineStyleCount(lineStyleNum);
			//System.out.println(lineStyleNum);

			if (tagLine.getNLineStyleCount() > 0) {// 修改
				float[] lineStyles = new float[lineStyleNum];
				for (int t = 0; t < tagLine.getNLineStyleCount(); t++)
			lineStyles[t] = (float) (dis.readDouble()*tagLine.getFLineWidth());
				//System.out.println(lineStyles[t]);
				tagLine.setLineStyle(lineStyles);
			}

			JSegment tagSegments = new JSegment();
			tagSegments.setBIsBezier(dis.readBoolean());
			//System.out.println(tagSegments.isBIsBezier());
			tagSegments.setNPtCount(dis.readInt());
			//System.out.println(tagSegments.getNPtCount());
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
//			tagLine.setSegment(tagSegments);
			

			
			int size = tagSegments.getNPtCount();
			GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, size);
			//Affine af = new Affine(wcRect, dcRect);
			if (tagSegments.isBIsBezier() == false) {// 不是Bezier的情况
				LinePath lp = new LinePath(path, Points);
				path = lp.getPath();
			} else {
				BezierPath bp = new BezierPath(path, Points);
				path = bp.getPath();
			}
			tagLine.setPath(path);
		
			
			
			BasicStroke bs;
			if (tagLine.getNLineStyleCount() == 0) {// 不是虚线的情况
				bs = new BasicStroke((float) (tagLine.getFLineWidth()), BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_ROUND);

			} else {
				bs = new BasicStroke((float) (tagLine.getFLineWidth()), BasicStroke.CAP_BUTT,
						BasicStroke.JOIN_ROUND, 0, tagLine.getLineStyle(), 0);
			}
			tagLine.setBs(bs);
			
			


		} catch (IOException e) {

		}
	}

}
