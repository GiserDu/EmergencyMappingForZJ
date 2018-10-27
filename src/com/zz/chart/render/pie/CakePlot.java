package com.zz.chart.render.pie;

import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.Polygon;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;



public class CakePlot {
	int x;//中心定位点，即所给的x，y
	int y;//
	int width;//整个椭圆的外界矩形的宽
	int height;//整个椭圆的外界矩形的高
	double[] angles;//各个部分所占的百分比换算的角度
	double startAngle = 90;//起始计算角度
	double[] heights;//各个部分所换算的高度
	Color[] colors;//填充的颜色

	public void drawCake(Graphics2D g) {
		boolean counterclockwise = true;//判断是否顺时针，以此来区分绘制的起点
		for (int i = 0; i < angles.length; i++) {
			if (counterclockwise) {
				if ((startAngle >= 90) &&(startAngle < 180) && (startAngle + angles[i]) <= 180) {
					// 上扇形
					Rectangle2D rectangle2dUp = new Rectangle2D.Double(x, y
							- heights[i], width, height);
					Arc2D arc2dUp = new Arc2D.Double(rectangle2dUp, startAngle,
							angles[i], Arc2D.PIE);
//					// 下扇形
//					Rectangle2D rectangle2dDown = new Rectangle2D.Double(x, y,
//							width, height);
//					Arc2D arc2dDown = new Arc2D.Double(rectangle2dDown, startAngle,
//							angles[i], Arc2D.PIE);
					// 由圆心向四周看分左右
					// 右平行四边形
					@SuppressWarnings("unused")
					Polygon rectangleRightPolygon = rectangleRight(startAngle, (int) heights[i]);
					// 左平行四边形
					Polygon rectangleLeftPolygon = rectangleLeft(startAngle,
							angles[i], (int) heights[i]);
					g.setColor(colors[i]);
					g.fill(arc2dUp);
					g.fill(rectangleLeftPolygon);
					g.setColor(Color.WHITE);
					g.draw(arc2dUp);
					g.draw(rectangleLeftPolygon);
					startAngle = startAngle + angles[i];
				}else if ((startAngle >= 90) &&(startAngle < 180) && (startAngle + angles[i]) > 180) {
					// 上扇形
					Rectangle2D rectangle2dUp = new Rectangle2D.Double(x, y
							- heights[i], width, height);
					Arc2D arc2dUp = new Arc2D.Double(rectangle2dUp, startAngle,
							angles[i], Arc2D.PIE);
					// 由圆心向四周看分左右
					
					// 左平行四边形
					Polygon rectangleLeftPolygon = rectangleLeft(startAngle,
							angles[i], (int) heights[i]);
					//中间的多边形
					GeneralPath generalPathMid = generalPathMid(startAngle, angles[i], (int)heights[i]);
					g.setColor(colors[i]);
					g.fill(arc2dUp);
					g.fill(rectangleLeftPolygon);
					g.fill(generalPathMid);
					g.setColor(Color.WHITE);
					g.draw(arc2dUp);
					g.draw(rectangleLeftPolygon);
					g.draw(generalPathMid);
					startAngle = startAngle + angles[i];
				}else if ((startAngle >= 180) && (startAngle + angles[i]) > 180) {
					// 上扇形
					Rectangle2D rectangle2dUp = new Rectangle2D.Double(x, y
							- heights[i], width, height);
					Arc2D arc2dUp = new Arc2D.Double(rectangle2dUp, startAngle,
							angles[i], Arc2D.PIE);
					// 由圆心向四周看分左右
					
					// 左平行四边形
					Polygon rectangleLeftPolygon = rectangleLeft(startAngle,
							angles[i], (int) heights[i]);
					//中间的多边形
					GeneralPath generalPathMid = generalPathMid(startAngle, angles[i], (int)heights[i]);
					g.setColor(colors[i]);
					g.fill(arc2dUp);
					g.fill(rectangleLeftPolygon);
					g.fill(generalPathMid);
					g.setColor(Color.WHITE);
					g.draw(arc2dUp);
					g.draw(rectangleLeftPolygon);
					g.draw(generalPathMid);
					startAngle = startAngle + angles[i];
				}
				
				if (startAngle + angles[i+1] > 270) {
					startAngle = 90;
					counterclockwise = false;
				}
				
			}else {
				if ((startAngle > 0) &&(startAngle <= 90) && (startAngle - angles[i]) > 0) {
					// 上扇形
					Rectangle2D rectangle2dUp = new Rectangle2D.Double(x, y
							- heights[i], width, height);
					Arc2D arc2dUp = new Arc2D.Double(rectangle2dUp, startAngle - angles[i],
							angles[i], Arc2D.PIE);
					// 由圆心向四周看分左右
					// 右平行四边形
					Polygon rectangleRightPolygon = rectangleRight(startAngle - angles[i], (int) heights[i]);

					g.setColor(colors[i]);
					g.fill(arc2dUp);
					g.fill(rectangleRightPolygon);
					g.setColor(Color.WHITE);
					g.draw(arc2dUp);
					g.draw(rectangleRightPolygon);
					startAngle = startAngle - angles[i];
				}else if ((startAngle > 0) &&(startAngle <= 90) && (startAngle - angles[i]) < 0) {
					Rectangle2D rectangle2dUp = new Rectangle2D.Double(x, y
							- heights[i], width, height);
					Arc2D arc2dUp = new Arc2D.Double(rectangle2dUp, startAngle - angles[i],
							angles[i], Arc2D.PIE);
					// 由圆心向四周看分左右
					// 右平行四边形
					Polygon rectangleRightPolygon = rectangleRight(startAngle - angles[i], (int) heights[i]);

					//中间的多边形
					GeneralPath generalPathMid = generalPathMid(startAngle, angles[i], (int)heights[i]);
					g.setColor(colors[i]);
					g.fill(arc2dUp);
					g.fill(rectangleRightPolygon);
					g.fill(generalPathMid);
					g.setColor(Color.WHITE);
					g.draw(arc2dUp);
					g.draw(rectangleRightPolygon);
					g.draw(generalPathMid);
					startAngle = startAngle - angles[i];
				}else if ((startAngle < 0) && (startAngle - angles[i]) >= 0 ){
					
				}
			}
			
		}
	}
	public GeneralPath generalPathMid(double startAngle, double extentAngle,int thickness) {
		GeneralPath generalPath = new GeneralPath();
		if (startAngle<180&&startAngle>=90) {
			double angleSE = startAngle + extentAngle;
			generalPath.moveTo(x - width/2, y - thickness);
			generalPath.lineTo(x - width/2, y);
			Arc2D arc2dDown = new Arc2D.Double(x - width/2, y - height/2, width, height, 180, angleSE - 180, Arc2D.OPEN);
			generalPath.append(arc2dDown, true);
			double cosSE = Math.cos((angleSE - 180) * 1.0 / 180
					* Math.PI);
			double sinSE = Math.sin((angleSE - 180) * 1.0 / 180
					* Math.PI); 
			generalPath.lineTo(x - cosSE*width/2, y + sinSE*width/2 - thickness);
			Arc2D arc2dUp = new Arc2D.Double(x - width/2, y - height/2 -thickness, width, height, angleSE, -angleSE + 180, Arc2D.OPEN);
			generalPath.append(arc2dUp, true);
			generalPath.closePath();
			return generalPath;
		}else if (startAngle>180){
			double angleSE = startAngle + extentAngle;
			double cosS = Math.cos((startAngle - 180) * 1.0 / 180
					* Math.PI);
			double sinS = Math.sin((startAngle - 180) * 1.0 / 180
					* Math.PI);
			generalPath.moveTo(x - width/2*cosS, y + sinS*width/2 - thickness);
			generalPath.lineTo(x - width/2*cosS, y + sinS*width/2);
			Arc2D arc2dDown = new Arc2D.Double(x - width/2, y - height/2, width, height, startAngle, angleSE - startAngle, Arc2D.OPEN);
			generalPath.append(arc2dDown, true);
			double cosSE = Math.cos((angleSE - 180) * 1.0 / 180
					* Math.PI);
			double sinSE = Math.sin((angleSE - 180) * 1.0 / 180
					* Math.PI); 

			generalPath.lineTo(x - cosSE*width/2, y + sinSE*width/2 - thickness);
			Arc2D arc2dUp = new Arc2D.Double(x - width/2, y - height/2 -thickness, width, height, angleSE, -angleSE + startAngle, Arc2D.OPEN);
			generalPath.append(arc2dUp, true);
			generalPath.closePath();
			return generalPath;
		}else if (startAngle>0&&startAngle<=90){
			double angleSE = startAngle - extentAngle;
			double cosSE = Math.cos((-angleSE) * 1.0 / 180
					* Math.PI);
			double sinSE = Math.sin((-angleSE) * 1.0 / 180
					* Math.PI); 
			generalPath.moveTo(x + cosSE*width/2, y + sinSE*width/2 - thickness);
			generalPath.lineTo(x + cosSE*width/2, y + sinSE*width/2);
			Arc2D arc2dDown = new Arc2D.Double(x - width/2, y - height/2, width, height, angleSE, -angleSE, Arc2D.OPEN);
			generalPath.append(arc2dDown, true);

			generalPath.lineTo(x + width/2, y);
			Arc2D arc2dUp = new Arc2D.Double(x - width/2, y - height/2 -thickness, width, height, 0, angleSE, Arc2D.OPEN);
			generalPath.append(arc2dUp, true);
			generalPath.closePath();
			return generalPath;
		}else {
			double angleSE = startAngle - extentAngle;
			double cosSE = Math.cos((-angleSE) * 1.0 / 180
					* Math.PI);
			double sinSE = Math.sin((-angleSE) * 1.0 / 180
					* Math.PI); 
			generalPath.moveTo(x + cosSE*width/2, y + sinSE*width/2 - thickness);
			generalPath.lineTo(x + cosSE*width/2, y + sinSE*width/2);
			Arc2D arc2dDown = new Arc2D.Double(x - width/2, y - height/2, width, height, angleSE, -angleSE, Arc2D.OPEN);
			generalPath.append(arc2dDown, true);
			double cosS = Math.cos((-startAngle) * 1.0 / 180
					* Math.PI);
			double sinS = Math.sin((-startAngle) * 1.0 / 180
					* Math.PI);
			generalPath.lineTo(x + width/2*cosS, y + width/2*sinS -thickness);
			Arc2D arc2dUp = new Arc2D.Double(x - width/2, y - height/2 -thickness, width, height, 0, angleSE, Arc2D.OPEN);
			generalPath.append(arc2dUp, true);
			generalPath.closePath();
			return generalPath;
		}

	}

	public Polygon rectangleRight(double startAngle, int thickness) {

		// 开始角度以及角度跨度

		double cosS = Math.cos(startAngle * 1.0 / 180 * Math.PI);

		double sinS = Math.sin(startAngle * 1.0 / 180 * Math.PI);

		// left
		int[] xpointsRight = { x, x, x + (int) (width / 2 * cosS),
				x + (int) (width / 2 * cosS) };
		int[] ypointsRight = { y, y + thickness,
				y - (int) (height / 2 * sinS) + thickness,
				y - (int) (height / 2 * sinS) };

		Polygon rectangleRight = new Polygon(xpointsRight, ypointsRight, 4);

		return rectangleRight;

	}

	public Polygon rectangleLeft(double startAngle, double extentAngle,
			int thickness) {

		double cosSE = Math.cos((startAngle + extentAngle) * 1.0 / 180
				* Math.PI);
		double sinSE = Math.sin((startAngle + extentAngle) * 1.0 / 180
				* Math.PI);

		// left
		int[] xpointsLeft = { x + (int) (width / 2 * cosSE),
				x + (int) (width / 2 * cosSE), x, x };
		int[] ypointsLeft = { y - (int) (height / 2 * sinSE),
				y - (int) (height / 2 * sinSE) + thickness, y + thickness, y };

		Polygon rectangleLeft = new Polygon(xpointsLeft, ypointsLeft, 4);

		return rectangleLeft;

	}

}
