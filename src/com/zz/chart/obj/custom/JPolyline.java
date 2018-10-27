package com.zz.chart.obj.custom;



import java.awt.Graphics2D; 
import java.awt.Color;
import java.awt.BasicStroke; 
import java.awt.geom.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;


/** 
 * Polyline对象读取数据绘图类
 * @author L J
 * @version 1.0 
 */
public class JPolyline extends JObject {

	private JPolylineStruct tagLine;
	private JLayer Layer;

	public JPolyline() {
		tagLine = new JPolylineStruct();
		Layer = null;
	}

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

    /**
     * Polyline绘图函数
     * @param g2D Java绘图对象
     * @return void
     * @throws 
     * @since 1.0
     */
	public void Draw(Graphics2D g2D) {
		GeneralPath path = tagLine.getPath();
		g2D.setColor(tagLine.getLLineColor());
		
		BasicStroke bs = tagLine.getBs();
		g2D.setStroke(bs);
		g2D.draw(path);
	}

    /**
     * 读取Polyline数据
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
			float lineWidth = dis.readFloat();
			lineWidth = (float) (lineWidth / aff.getScale());
			tagLine.setFLineWidth(lineWidth);//精度有损失，待完善
			//System.out.println(lineWidth);

			int lineColor = dis.readInt();
			int[] rgb = Util.GetRGB(lineColor);
			Color color = new Color(rgb[2], rgb[1], rgb[0]);
			tagLine.setLLineColor(color);
			//System.out.println(lineColor);
			//lmk 2014.2.27
			if(super.getFillColor() != null)
				tagLine.setLLineColor(super.getFillColor());

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
				pt = curAff.TransPoint(pt);
				Points.add(pt);
			}
			tagSegments.setPoints(Points);
			
			int size = tagSegments.getNPtCount();
			GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, size);
			if (tagSegments.isBIsBezier() == false) {// 不是Bezier的情况
				LinePath lp = new LinePath(path, Points);
				path = lp.getPath();
			} else {
				BezierPath bp = new BezierPath(path, Points);
				path = bp.getPath();
			}
			tagLine.setPath(path);
			
			//lmk 2013.11.27
			Area area = new Area(path);
			this.setArea(area);
			
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
