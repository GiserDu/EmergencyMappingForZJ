package com.zz.bglayer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
//import java.awt.image.AffineTransformOp;
/** 
 * Raster读取数据绘图类
 * @author L J
 * @version 1.0 
 */
public class JRaster extends JObject{
	/*public char GetType() {
		return 0;
	}*/
	private JRasterStruct tagRaster;
	
	public JRaster() {
		tagRaster = new JRasterStruct();
    }

	public JRasterStruct getTagRaster() {
		return tagRaster;
	}

	public void setTagRaster(JRasterStruct tagRaster) {
		this.tagRaster = tagRaster;
	}
    /**
     * Raster绘图函数
     * @param g2D Java绘图对象
     * @param affTrans 仿射变换对象
     * @return void
     * @throws 
     * @since 1.0
     */
	public void Draw(Graphics2D g2D) {
//		BufferedImage image = tagRaster.getBImage();
//		double[] transArray = new double[4];
//		transArray = tagRaster.getAffineArray();
//		//Affine af = new Affine(wcRect, dcRect);
//		double Scale = affTrans.getScale();
//		Point2D.Double pt = new Point2D.Double();
//		pt.setLocation(affTrans.TransPoint(tagRaster.getPoint()));
//		//System.out.println(Scale);
//		AffineTransform AT = new AffineTransform(transArray[0]/Scale,-transArray[1]/Scale, -transArray[2]/Scale, transArray[3]/Scale, pt.getX(),pt.getY());
//		g2D.drawImage(image, AT, null);
		
		
		BufferedImage image = tagRaster.getBImage();
//		double[] transArray = new double[4];
//		transArray = tagRaster.getAffineArray();
		//Affine af = new Affine(wcRect, dcRect);
//		double Scale = affTrans.getScale();
//		Point2D.Double pt = new Point2D.Double();
//		pt.setLocation(affTrans.TransPoint(tagRaster.getPoint()));
		//System.out.println(Scale);
//		AffineTransform AT = new AffineTransform(transArray[0]/Scale,-transArray[1]/Scale, -transArray[2]/Scale, transArray[3]/Scale, pt.getX(),pt.getY());
		AffineTransform AT = tagRaster.getATRaster();
		g2D.drawImage(image, AT, null);

		
	}
	
	public void ReadObjectInfoText() {
	}

    /**
     * 读取Raster属性数据
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
			Rectangle2D.Double rect = new Rectangle2D.Double(bx1,by1,bx2-bx1,by2-by1);
			this.setRect(rect);
			//tagRaster.setRect(rect);
			
			double x1 = dis.readDouble();
			double y1 = dis.readDouble();
			Point2D.Double pt = new Point2D.Double(x1, y1);
			tagRaster.setPoint(pt);
			
			int [] size = new int [2];
			for (int i = 0; i < 2; i++) {
				size[i] = dis.readInt();
			}
			tagRaster.setRasSize(size);
			
			double[] affineArray = new double[4];
			for (int i = 0; i < 4; i++) {
				affineArray[i] = dis.readDouble();
				//affineArray[i] = affineArray[i];
			}
			tagRaster.setAffineArray(affineArray);
			
			String rasName;
			rasName = dis.readUTF();
			tagRaster.setRasName(rasName);	
			
			AffineTransform AT = new AffineTransform(affineArray[0],affineArray[1], affineArray[2], affineArray[3], pt.getX(),pt.getY());
			AffineTransform ATYInvert = new AffineTransform(1.0d, 0.0d, 0.0d, -1.0d, 0.0d, 0.0d);
	        AT.concatenate(ATYInvert);
			tagRaster.setATRaster(AT);
		} catch (IOException e) {

		}
	}
    /**
     * 读取Raster栅格数据
     * @param dataPath 数据路径
     * @return void
     * @throws 
     * @since 1.0
     */
	public void ReadBufferedImage(String dataPath){
		try{
			int [] size = new int [2];
			size = tagRaster.getRasSize();
			int WIDTH = size[0];
			int HEIGHT = size[1];
			String imagePath = new String();
			String imageName = new String();
			imageName = tagRaster.getRasName();
			imagePath = dataPath + "/" +imageName;
			BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
					BufferedImage.TYPE_INT_RGB);
			image = ImageIO.read(new File(imagePath));
			tagRaster.setBImage(image);
		}catch(IOException e){
			
		}
	}

}
