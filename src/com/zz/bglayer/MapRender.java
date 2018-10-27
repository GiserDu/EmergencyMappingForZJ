package com.zz.bglayer;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*; //import java.lang.*;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.zz.util.JUtil;

//import java.awt.font.*;    
import java.awt.image.*;

import javax.imageio.ImageIO;

import com.zz.bglayer.JGeoRegion;

/**
 * 引擎最外层接口，提供对外界各种方法
 * 
 * @author L J
 * @version 1.0
 */
public class MapRender {

	// private JAtlas geoAtlas;
	private JMap map;
	private Rectangle2D.Double WC;
	private Rectangle2D.Double DC;
	private String dataPath;
	private String outputPath;

	// private double Scale;

	public MapRender() {
		// geoAtlas = null;
		map = null;
		WC = null;
		DC = null;
		dataPath = "";
		outputPath = "";
		// Scale = 0;
	}

	/**
	 * 数据载入接口
	 * 
	 * @param dataPath
	 *            数据路径
	 * @return boolean
	 * @throws
	 * @since 1.0
	 */
	public boolean LoadData(String dataPath) {
		this.dataPath = dataPath;
		// FileInputStream fis=new FileInputStream(dataPath);
		// DataInputStream dis=new DataInputStream(fis);

		map = new JMap();
		map.ReadMapInfoBin(dataPath);

		// 目前只包含一幅地图
		// dis.close();
//		System.out.println("File end");

		return true;
	}

	/**
	 * 绘图接口，不绘制分级底图的情况
	 * 
	 * @param g2D
	 *            Java绘图对象
	 * @param WC
	 *            AI范围
	 * @param DC
	 *            像素范围
	 * @return Image
	 * @throws
	 * @since 1.0
	 */
	public Image OutputMap(Rectangle2D.Double curWC, Rectangle2D.Double DC) {
		try {
			BufferedImage bi = new BufferedImage((int) DC.getWidth(), (int) DC
					.getHeight(), BufferedImage.TYPE_INT_RGB);

			// GraphicsEnvironment ge =
			// GraphicsEnvironment.getLocalGraphicsEnvironment();
			// GraphicsDevice gd = ge.getDefaultScreenDevice();
			// GraphicsConfiguration gc = gd.getDefaultConfiguration();
			// BufferedImage bi = gc.createCompatibleImage((int)DC.getWidth(),
			// (int)DC.getHeight());

			Graphics2D g2D = (Graphics2D) bi.getGraphics();
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			g2D.setBackground(Color.WHITE);
			g2D.clearRect(0, 0, (int) DC.getWidth(), (int) DC.getHeight());
			// g2D.setPaint(Color.BLACK);
			// FontRenderContext context = g2D.getFontRenderContext();

			if (curWC.width <= 0 || curWC.height <= 0)
				curWC = map.getWC();

			// double Scale = 1.0;

			map.Draw(g2D, curWC, DC);

			g2D.dispose();
			bi.flush();
			return bi;
		} catch (Exception e) {
			System.out.println("Map render error");
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * 绘图接口，绘制分级底图的情况
	 * 
	 * @param g2D
	 *            Java绘图对象
	 * @param WC
	 *            AI范围
	 * @param DC
	 *            像素范围
	 * @param regNameMapColor
	 *            分级底图HashMap对象
	 * @return Image
	 * @throws
	 * @since 1.0
	 */
	public Image OutputMap(Rectangle2D.Double curWC, Rectangle2D.Double DC,
			HashMap<String, Color> regNameMapColor) {
		try {
			BufferedImage bi = new BufferedImage((int) DC.getWidth(), (int) DC
					.getHeight(), BufferedImage.TYPE_INT_RGB);

			Graphics2D g2D = (Graphics2D) bi.getGraphics();
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			g2D.setBackground(Color.WHITE);
			g2D.clearRect(0, 0, (int) DC.getWidth(), (int) DC.getHeight());

			if (curWC.width <= 0 || curWC.height <= 0)
				curWC = map.getWC();


			map.Draw(g2D, curWC, DC, regNameMapColor);
			g2D.dispose();
			bi.flush();
			return bi;
		} catch (Exception e) {
			System.out.println("Map render error");
			System.out.println(e.getMessage());
			return null;
		}
	}

	public Image SaveAnotherImage(Rectangle2D.Double curWC, double scale,
			String paperSize, int dpi) {
		try {
			Rectangle2D.Double DC;
			DC = JUtil.CalDCSize(paperSize, dpi);
			BufferedImage bi = new BufferedImage((int) DC.getWidth(), (int) DC
					.getHeight(), BufferedImage.TYPE_INT_BGR);
			Graphics2D g2D = (Graphics2D) bi.getGraphics();
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			g2D.setBackground(Color.WHITE);
			g2D.clearRect(0, 0, (int) DC.getWidth(), (int) DC.getHeight());
			// g2D.setPaint(Color.WHITE);
			// FontRenderContext context = g2D.getFontRenderContext();

			map.Draw(g2D, curWC, DC);
			g2D.dispose();
			bi.flush();
			return bi;
		} catch (Exception e) {
			System.out.println("Map render error");
			System.out.println(e.getMessage());
			return null;
		}
	}

	public Rectangle2D.Double GetBoundary() {
		if (map == null)
			return null;
		return map.getWC();
	}

	/**
	 * 保存图片方法，将BufferImage存为图片二进制流
	 * 
	 * @param filePath
	 *            存储路径
	 * @param img
	 *            BufferImageduixiang
	 * @return void
	 * @throws
	 * @since 1.0
	 */
	public void SaveToJPG(String filePath, Image image) {
		if (filePath.equals("") || image == null)
			return;
		try {
			BufferedImage bimg = (BufferedImage) image;
			FileOutputStream fos = new FileOutputStream(new File(filePath));
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
			encoder.encode(bimg);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 保存图片方法，将BufferImage存为图片二进制流
	 * 
	 * @param filePath
	 *            存储路径
	 * @param img
	 *            BufferImageduixiang
	 * @return void
	 * @throws
	 * @since 1.0
	 */
	public void SaveImage(String filePath, BufferedImage img) {
		try {
			String format = (filePath.endsWith(".png")) ? "png" : "jpg";
			ImageIO.write(img, format, new File(filePath));
			// ImageIO.write(img, "bmp", new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public JMap getMap() {
		return map;
	}

	public void setMap(JMap map) {
		this.map = map;
	}

	/*
	 * public void setGeoRegionColor(HashMap regNameMapColor){
	 * map.setBolDrawRegionLayer(true); ArrayList<JGeoRegion> geoRegion = new
	 * ArrayList<JGeoRegion>(); geoRegion = map.getRegLayer().getArrObjects();
	 * 
	 * Iterator it = regNameMapColor.keySet().iterator(); while(it.hasNext()){
	 * String regName = (String)it.next(); for(int i=0;i<geoRegion.size();i++){
	 * if(geoRegion.get(i).getBoolSetColor() == false){
	 * if(geoRegion.get(i).getRegName().equals(regName)){
	 * geoRegion.get(i).setFillColor((Color)regNameMapColor.get(regName));
	 * geoRegion.get(i).setBoolSetColor(true); } } } }
	 *  }
	 */

	/**
	 * 政区查询，返回政区外接矩形框
	 * 
	 * @param regGeoCode
	 *            查询目标的GeoCode
	 * @return Rectangle2D.Double
	 * @exception NoSuchFieldException
	 *                抛出未找到相匹配的地理数据异常
	 * @since 1.0
	 */
	// public Rectangle2D.Double getGeoRegionBound(String regGeoCode) {
	// // ArrayList<JGeoRegion> arrObjects = map.getRegLayer().getArrObjects();
	// // Rectangle2D.Double geoBound = new Rectangle2D.Double();
	// //
	// // int j = 0;
	// // for (int i = 0; i < arrObjects.size(); i++) {
	// // if (arrObjects.get(i).getRegGeoCode().equals(regGeoCode)) {
	// // //一个GeoCode对应多个政区时，需合并矩形框
	// // if (j == 0) {
	// // geoBound = arrObjects.get(i).getRect();
	// // } else {
	// // geoBound = (Rectangle2D.Double) geoBound
	// // .createUnion(arrObjects.get(i).getRect());
	// // }
	// // j++;
	// // }
	// // }
	// // try {
	// // if (geoBound.width == 0 || geoBound.height == 0) {
	// // String msg = "未找到相匹配的地理数据，请重新查询！";
	// // throw new NoSuchFieldException(msg);
	// // }
	// // } catch (NoSuchFieldException e) {
	// // e.printStackTrace();
	// // }
	// // return geoBound;
	//		
	//		
	// ArrayList<JGeoRegion> arrObjects = map.getRegLayer().getArrObjects();
	// Rectangle2D.Double geoBound = new Rectangle2D.Double();
	//		
	// return geoBound;
	// }
	public Rectangle2D.Double getGeoRegionBound(String regGeoCode) {
		//政区查询，只查询街道
		ArrayList<JGeoRegion> arrObjects = map.getRegJDLayer().getArrObjects();

//		
//		if (regGeoCode.length() == 6) {
//			arrObjects = map.getRegQULayer().getArrObjects();
//		} else {
//			arrObjects = map.getRegJDLayer().getArrObjects();
//		}

		Rectangle2D.Double geoBound = new Rectangle2D.Double();

		// 查询区的情况
		// if (regGeoCode.length() == 6) {
		int j = 0;
		for (int i = 0; i < arrObjects.size(); i++) {
			if (arrObjects.get(i).getRegGeoCode().equals(regGeoCode)) {
				// 一个GeoCode对应多个政区时，需合并矩形框
				if (j == 0) {
					geoBound = arrObjects.get(i).getRect();
				} else {
					geoBound = (Rectangle2D.Double) geoBound
							.createUnion(arrObjects.get(i).getRect());
				}
				j++;
			}
		}

		// 查询街道的情况
		// }
		// else if (regGeoCode.length() == 9) {
		// regGeoCode = regGeoCode.substring(0, 6);
		// int j = 0;
		// for (int i = 0; i < arrObjects.size(); i++) {
		// if (arrObjects.get(i).getRegGeoCode().equals(regGeoCode)) {
		// //一个GeoCode对应多个政区时，需合并矩形框
		// if (j == 0) {
		// geoBound = arrObjects.get(i).getRect();
		// } else {
		// geoBound = (Rectangle2D.Double) geoBound
		// .createUnion(arrObjects.get(i).getRect());
		// }
		// j++;
		// }
		// }
		// }
		try {
			if (geoBound.width == 0 || geoBound.height == 0) {
				String msg = "未找到相匹配的地理数据，请重新查询！";
				throw new NoSuchFieldException(msg);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return geoBound;
	}
	
	//修改于10月14日，使查询能返回GeneralPath以进行红色边界的绘制
	public JGeoRegion queryGeoRegion(String regGeoCode){
		//政区查询，只查询街道
		ArrayList<JGeoRegion> arrObjects = map.getRegJDLayer().getArrObjects();
		
		JGeoRegion jgr = new JGeoRegion();
		for (int i = 0; i < arrObjects.size(); i++) {
			if (arrObjects.get(i).getRegGeoCode().equals(regGeoCode)) {
					jgr = arrObjects.get(i);
			}
		}
		try {
			if (jgr.getRegGeoCode() == null) {
				String msg = "未找到相匹配的地理数据，请重新查询！";
				throw new NoSuchFieldException(msg);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return jgr;
	}

	public ArrayList<JGeoRegion> getGeoQURegion() {
		ArrayList<JGeoRegion> geoRegions = map.getRegQULayer().getArrObjects();
		return geoRegions;
	}
	
	public ArrayList<JGeoRegion> getGeoJDRegion() {
		ArrayList<JGeoRegion> geoRegions = map.getRegJDLayer().getArrObjects();
		return geoRegions;
	}
	
}
