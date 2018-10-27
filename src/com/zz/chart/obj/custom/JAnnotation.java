package com.zz.chart.obj.custom;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.IOException;

/** 
 * Annotation读取数据绘图类
 * @author L J
 * @version 1.0 
 */
public class JAnnotation extends JObject {
	
	private JTextStruct tagText;

	public JAnnotation() {
		tagText = new JTextStruct();

	}

	public JTextStruct getTagText() {
		return tagText;
	}

	public void setTagText(JTextStruct tagText) {
		this.tagText = tagText;
	}

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
		
		Font font = tagText.getFt();
		
		g2D.setFont(font);
		g2D.setColor(tagText.getLTextColor());
		
		Point2D.Double pt = tagText.getPoint();
		
		g2D.drawString(tagText.getTextContent(), (float) pt.getX(),
		(float) pt.getY());
		System.out.println(tagText.getTextContent());
	}

    /**
     * 读取Annotation数据
     * @param dis 文件头指针
     * @return void
     * @throws 
     * @since 1.0
     */
	public void ReadObjectInfoBin(DataInputStream dis, Affine aff) throws IOException {
		try {
			double bx1 = dis.readDouble();
			double by1 = dis.readDouble();
			double bx2 = dis.readDouble();
			double by2 = dis.readDouble();
			Rectangle2D.Double rect = new Rectangle2D.Double(bx1,by1,bx2-bx1,by2-by1);
			this.setRect(rect);
			
			String textContent;
			textContent = dis.readUTF();// 字体内容
			tagText.setTextContent(textContent);
			// System.out.println(textContent);

			int linecolor = dis.readInt();// 字体颜色
			int[] rgb = Util.GetRGB(linecolor);
			Color color = new Color(rgb[2], rgb[1], rgb[0]);
			tagText.setLTextColor(color);
			// System.out.println(linecolor);
			//lmk 2014.2.27
			if(super.getFillColor() != null)
				tagText.setLTextColor(super.getFillColor());
			
			Affine curAff = aff;
			
			double x1 = dis.readDouble();
			double y1 = dis.readDouble();
			Point2D.Double pt = new Point2D.Double(x1, y1);
			pt = curAff.TransPoint(pt);
			tagText.setPoint(pt);

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

			String textType = dis.readUTF();// 字体
			tagText.setTextType(textType);
			// System.out.println(textType);

			double textHeight;
			textHeight = dis.readDouble();
			textHeight = textHeight / curAff.getScale();
			tagText.setDFHeight(textHeight);
			// System.out.println(textHeight);

			int textPond;
			textPond = dis.readInt();
			textPond = (int) (textPond / curAff.getScale());
			tagText.setLTextban(textPond);
			// System.out.println(textPond);

			int textEscape;
			textEscape = dis.readInt();
			tagText.setLTextlfEscapement(textEscape);
			// System.out.println(textEscape);
			
			Font font = new Font(tagText.getTextType(), Font.PLAIN, (int) (tagText.getDFHeight()));	
			tagText.setFt(font);

		} catch (IOException e) {

		}

	}
}
