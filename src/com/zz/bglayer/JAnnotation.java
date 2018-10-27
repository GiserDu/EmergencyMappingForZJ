package com.zz.bglayer;

import java.awt.*;
import java.awt.geom.*;
import java.awt.Font;
import java.awt.Graphics2D;
//import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.io.*;

import com.zz.util.JUtil;
/** 
 * Annotation读取数据绘图类
 * @author L J
 * @version 1.0 
 */
public class JAnnotation extends JObject {

	// private JPoint point;
	// private String text;
	private JTextStruct tagText;
	//Rectangle2D.Double rect;

	// private JLayer Layer;

	public JAnnotation() {
		// point = new JPoint();
		// text = null;
		tagText = new JTextStruct();
		//rect = new Rectangle2D.Double();
		// Layer = null;

	}

	/*
	 * public JPoint getPoint() { return point; }
	 * 
	 * public void setPoint(JPoint point) { this.point = point; }
	 */

	/*
	 * public String getText() { return text; }
	 * 
	 * public void setText(String text) { this.text = text; }
	 */

	public JTextStruct getTagText() {
		return tagText;
	}

	public void setTagText(JTextStruct tagText) {
		this.tagText = tagText;
	}

	/*public char GetType() {
		return 0;
	}*/

	// draw image
    /**
     * Annotation绘图函数
     * @param g2D Java绘图对象
     * @param affTrans 仿射变换对象
     * @return void
     * @throws 
     * @since 1.0
     */
	public void Draw(Graphics2D g2D) {// 李坚添加
		/*g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);*/
//		double Scale = affTrans.getScale();
//		float size = (float) (tagText.getDFHeight());
//		Font font = new Font(tagText.getTextType(), Font.PLAIN, (int) (size/Scale));
//		Point2D.Double pt = new Point2D.Double();
//		pt.setLocation(affTrans.TransPoint(tagText.getPoint()));
//		g2D.setFont(font);
//		g2D.setColor(tagText.getLTextColor());
//
//		if (tagText.getIsTrans() == false) {
//			g2D.drawString(tagText.getTextContent(), (float) pt.getX(),
//					(float) pt.getY());
//		}
//
//		else {// 存在变换时
//			double[] transArray = new double[4];
//			transArray = tagText.getAffineArray();
//			AffineTransform AT = new AffineTransform(transArray[0],
//					-transArray[1], -transArray[2], transArray[3], pt.getX(),
//					pt.getY());
//			GeneralPath path = new GeneralPath();
//			// path.moveTo((float)pt.getX(),(float)pt.getY());
//			GlyphVector gv = font.createGlyphVector(g2D.getFontRenderContext(),
//					tagText.getTextContent());
//			int gvSize = gv.getNumGlyphs();
//			for (int i = 0; i < gvSize; i++) {
//				Shape shp = gv.getGlyphOutline(i);
//				path.append(shp, false);
//			}
//			path.transform(AT);
//			g2D.fill(path);
//
//		}
		
//		Font font = tagText.getFt();
//		
//		AffineTransform ATWC = g2D.getTransform();
//		Point2D.Double ptWC = tagText.getPoint();
//		
//		Point2D.Double ptDC = new Point2D.Double();
//		ATWC.transform(ptWC, ptDC);
//		
//		g2D.setColor(tagText.getLTextColor());
//		
//		if (tagText.getIsTrans() == false) {
//			g2D.setFont(font);
//			g2D.drawString(tagText.getTextContent(), (float) ptDC.getX(),(float) ptDC.getY());
//		} else {
//			double[] transArray = new double[4];
//			transArray = tagText.getAffineArray();
//			AffineTransform ATFont = new AffineTransform(transArray[0],
//					-transArray[1], -transArray[2], transArray[3], ptDC.getX(),
//					ptDC.getY());
//			
//			Font transFont = font.deriveFont(ATFont);
//			g2D.setFont(transFont);
//			g2D.drawString(tagText.getTextContent(), (float) ptDC.getX(),(float) ptDC.getY());
//		}
		
		Font font = tagText.getFt();
		//Font font = new Font("Arial", Font.PLAIN,10);
		g2D.setFont(font);
		g2D.setColor(tagText.getLTextColor());
		
		Point2D.Double pt = tagText.getPoint();
		
		g2D.drawString(tagText.getTextContent(), (float) pt.getX(),
		(float) pt.getY());
	}

	// Read map infomations
	public void ReadObjectInfoText() {

	}

    /**
     * 读取Annotation数据
     * @param dis 文件头指针
     * @return void
     * @throws 
     * @since 1.0
     */
	public void ReadObjectInfoBin(DataInputStream dis) throws IOException {
		try {
			double bx1 = dis.readDouble();
			double by1 = dis.readDouble();
			double bx2 = dis.readDouble();
			double by2 = dis.readDouble();
			Rectangle2D.Double rect = new Rectangle2D.Double(bx1,by1,bx2-bx1,by2-by1);
			this.setRect(rect);
			//tagText.setRect(rect);
			
			String textContent;
			textContent = dis.readUTF();// 字体内容
			tagText.setTextContent(textContent);
			// System.out.println(textContent);

			int linecolor = dis.readInt();// 字体颜色
			int[] rgb = JUtil.GetRGB(linecolor);
			Color color = new Color(rgb[2], rgb[1], rgb[0]);
			tagText.setLTextColor(color);
			// System.out.println(linecolor);
			// tagText.set(new Color(linecolor));

			double x1 = dis.readDouble();
			double y1 = dis.readDouble();
			Point2D.Double pt = new Point2D.Double(x1, y1);
			tagText.setPoint(pt);
			/*
			 * point.setX(Util.getCoorZoomRatio() * x1);// 读取x坐标 //
			 * System.out.println(x1);
			 * 
			 * point.setY(Util.getCoorZoomRatio() * y1);// 读取y坐标 //
			 * System.out.println(y1);
			 */

			double[] x = new double[4];
			for (int i = 0; i < 4; i++) {
				x[i] = dis.readDouble();
				// System.out.println(x[i]);
			}
			tagText.setAffineArray(x);
			if (x[0] == 1 && x[1] == 0 && x[2] == 0 && x[3] == 1) {// 李坚修改
				tagText.setIsTrans(false);
			} else {
				tagText.setIsTrans(true);
			}
			/*
			 * tagText.setAffineArray(dis.readDouble());//变换参数
			 * tagText.setDAffineB(dis.readDouble());
			 * tagText.setDAffineC(dis.readDouble());
			 * tagText.setDAffineD(dis.readDouble());
			 */

			String textType = dis.readUTF();// 字体
			tagText.setTextType(textType);
			// System.out.println(textType);
			// tagText.setTagLogFont(new Font(text, Font.BOLD, 72));

			double textHeight;
			textHeight = dis.readDouble();
			tagText.setDFHeight(textHeight);
			// System.out.println(textHeight);

			int textPond;
			textPond = dis.readInt();
			tagText.setLTextban(textPond);
			// System.out.println(textPond);

			int textEscape;
			textEscape = dis.readInt();
			tagText.setLTextlfEscapement(textEscape);
			// System.out.println(textEscape);
			
			Font font = new Font(tagText.getTextType(), Font.PLAIN, (int) (tagText.getDFHeight()));
			AffineTransform ATYInvert = new AffineTransform(1.0d, 0.0d, 0.0d, -1.0d, 0.0d, 0.0d);
			
			if(tagText.getIsTrans() == false){		
				font = font.deriveFont(ATYInvert);
			}else if (tagText.getIsTrans() == true) {		
				double[] transArray = new double[4];
				transArray = tagText.getAffineArray();
				AffineTransform ATFont = new AffineTransform(transArray[0],
						transArray[1], transArray[2], transArray[3], 0.0d, 0.0d);
				ATFont.concatenate(ATYInvert);
				font = font.deriveFont(ATFont);
			}
			
			tagText.setFt(font);

		} catch (IOException e) {

		}

	}
}
