package com.zz.util;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.zz.chart.data.LocationPoint;

public class JAffine {
	
	private double[] X;
	private double[] Y;
	public JAffine(String[] regioncodes,String[] xStrings,String[] yStrings,
			Rectangle2D.Double WC,Rectangle2D.Double DC){
		HashMap<String, LocationPoint>WCpoint = new HashMap<String, LocationPoint>();// 所有统计单元编码及其定位点
		
		for (int j = 0; j < regioncodes.length; j++) {			
			LocationPoint TEMPoint = new LocationPoint();
			TEMPoint.setX(Double.parseDouble(xStrings[j]));
			TEMPoint.setY(Double.parseDouble(yStrings[j]));
			WCpoint.put(regioncodes[j], TEMPoint);
		}
		
		double[] tempX = new double[regioncodes.length];
		double[] tempY = new double[regioncodes.length];
		for (int j = 0; j < regioncodes.length; j++) {
			Set<String> setRegonCode = WCpoint.keySet();
			Iterator<String> iterator1 = setRegonCode.iterator();
			while (iterator1.hasNext()) {
				String tempString = iterator1.next();
				if (regioncodes[j].equals(tempString)) {

					double scale1 = DC.getWidth() / WC.getWidth();
					double scale2 = DC.getHeight() / WC.getHeight();
					double scale = scale1 < scale2 ? scale1 : scale2;
					// //坐标转换后偏移量校正
					double sx = (DC.getWidth() - (WC.getWidth() * scale)) / 2;
					double sy = (DC.getHeight() - (WC.getHeight() * scale)) / 2;
					// // //定位点坐标变换
//					System.out.println((WCpoint.get(tempString).getX() - WC.getX()) * scale);
					tempX[j] = (WCpoint.get(tempString).getX() - WC.getX()) * scale;
					tempY[j] = (WCpoint.get(tempString).getY() - WC.getY()) * scale;

					tempX[j] = DC.x + tempX[j] + sx;
					tempY[j] = DC.y + tempY[j] + sy;
					// 新增部分
					tempY[j] = DC.getHeight() - tempY[j];
				}
			}
			this.setX(tempX);
			this.setY(tempY);
		}
	}
	
	public double[] getX() {
		return X;
	}
	public void setX(double[] x) {
		X = x;
	}
	public double[] getY() {
		return Y;
	}
	public void setY(double[] y) {
		Y = y;
	}
}
