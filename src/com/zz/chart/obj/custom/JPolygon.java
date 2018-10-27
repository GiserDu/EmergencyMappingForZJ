package com.zz.chart.obj.custom;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

/** 
 * Polygon读取数据绘图类
 * @author L J
 * @version 1.0 
 */
public class JPolygon extends JObject {

	private JPolygonStruct tagPolygon;
//	private JLayer Layer;
	public JPolygon() {
		tagPolygon = new JPolygonStruct();
//		Layer = null;
	}
	
	public JPolygonStruct getTagPolygon() {
		return tagPolygon;
	}

	public void setTagPolygon(JPolygonStruct tagPolygon) {
		this.tagPolygon = tagPolygon;
	}

//	public JLayer getLayer() {
//		return Layer;
//	}
//
//	public void setLayer(JLayer layer) {
//		Layer = layer;
//	}

    /**
     * Polygon绘图函数
     * @param g2D Java绘图对象
     * @param affTrans 仿射变换对象
     * @return void
     * @throws 
     * @since 1.0
     */
	public void Draw(Graphics2D g2D) {
		GeneralPath path = tagPolygon.getPath();
		if(tagPolygon.isBHasBoundCol()==true){
			g2D.setColor(tagPolygon.getLLineColor());
		}
		BasicStroke bs = tagPolygon.getBs();
		g2D.setStroke(bs);
		g2D.draw(path);
		if (tagPolygon.isBIsFill() == true) {
			g2D.setColor(tagPolygon.getLFillColor());
			g2D.fill(path);
		}
	}
	
	 /**
     * 读取Polygon数据
     * @param dis 文件头指针
     * @param aff 放射变换
     * @return void
     * @throws 
     * @since 1.0
     */
	public void ReadObjectInfoBin(DataInputStream dis, Affine aff) {
		try {
			double bx1 = dis.readDouble();
			double by1 = dis.readDouble();
			double bx2 = dis.readDouble();
			double by2 = dis.readDouble();
			//System.out.println(bx1+" "+by1+" "+bx2+" "+by2);
			Rectangle2D.Double rect = new Rectangle2D.Double(bx1,by1,bx2-bx1,by2-by1);
			this.setRect(rect);
			
			Affine curAff = aff;
			float fLineWidth = dis.readFloat();
			fLineWidth = (float) (fLineWidth / curAff.getScale());
			tagPolygon.setFLineWidth(fLineWidth);
			//System.out.println(tagPolygon.getFLineWidth());
			tagPolygon.setBHasBoundCol(dis.readBoolean());
			 //System.out.println(tagPolygon.isBHasBoundCol());
			if (tagPolygon.isBHasBoundCol() == true) {
				int linecolor = dis.readInt();
				int[] rgb = Util.GetRGB(linecolor);
				Color color = new Color(rgb[2], rgb[1], rgb[0]);
				tagPolygon.setLLineColor(color);
				 //System.out.println(tagPolygon.getLLineColor());
				//lmk 2014.2.27
				if(super.getFillColor() != null)
					tagPolygon.setLLineColor(super.getFillColor());
			}
			tagPolygon.setBIsFill(dis.readBoolean());
			//System.out.println(tagPolygon.isBIsFill());
			if (tagPolygon.isBIsFill() == true) {
				int linecolor = dis.readInt();
				int[] rgb = Util.GetRGB(linecolor);
				Color color = new Color(rgb[2], rgb[1], rgb[0]);
				tagPolygon.setLFillColor(color);
				 //System.out.println(tagPolygon.getLFillColor());
				//lmk 2014.2.27
				if(super.getFillColor() != null)
					tagPolygon.setLFillColor(super.getFillColor());
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
				pt = curAff.TransPoint(pt);
				Points.add(pt);
			}
			tagSegments.setPoints(Points);

			int size = tagSegments.getNPtCount();
			
			GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, size);
			if (tagSegments.isBIsBezier() == false) {// 不是Bezier的情况
				LinePath bz = new LinePath(path, Points);
				path = bz.getPath();
			} else {
				BezierPath bz = new BezierPath(path, Points);
				path = bz.getPath();
			}
			tagPolygon.setPath(path);
			
			//lmk 2013.11.27
			Area area = new Area(path);
			this.setArea(area);
			
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
