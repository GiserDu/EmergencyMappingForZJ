package com.zz.chart.obj.bar;

import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class PartCylinderChart extends CylinderChart {

	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		// 消除线条锯齿
		double temp = height*0.9;
		height = (new Double(temp)).intValue();
		y = y + height/2;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		double maxValue = maxValues[0];
		double averageValue = averageValues[0];
		double[] values;// 存放单变量多指标的值，例如存放2012年男女指标的人口数
		if (indicatorDatas[0] ==null) {
			System.out.println("绘制专题符号图层时发现专题数据中区域数据不完整！");
			return;
		}
		values = indicatorDatas[0].getValues();
		boolean isCrack = chartStyle.isIsCrack();
//		boolean isFracture = chartStyle.isFracture();
		if (maxValue > (averageValue * 4)&&isCrack) {
			maxValue = averageValue * 4 * (1 + 1.0 / 9);
		} else {
			maxValue = maxValue * (1 + 1.0 / 9);
		}



		// 获取符号绘制样式
		@SuppressWarnings("unused")
		String chartID = chartStyle.getChartID();// 符号ID
		boolean drawBarOutline = chartStyle.isDrawBarOutLine();// 是否绘制柱子边线
		int outLinePaint = chartStyle.getOutLinePaint();// 柱子边线颜色
		float outLineBasicStroke = chartStyle.getOutLineBasicStroke();// 柱子边线粗细
		double itemMargin = chartStyle.getItemMargin();// 柱子间的距离，像素为单位
		boolean isLable = chartStyle.isLable();// 是否绘制标签
		String itemLabelFontName = chartStyle.getItemLabelFontName();// 标签字体名称
		int itemLabelFontSize = chartStyle.getItemLabelFontSize();// 标签字体大小
		int itemLabelPaint = chartStyle.getItemLabelPaint();// 标签颜色
		//int transparent = chartStyle.getTransparent();// 填充色透明度
		double minimumBarLength = chartStyle.getMinimumBarLength();
		boolean GradientPaint = chartStyle.isGradientPaint();
		int[] color;

		int[] crgb = thematicRendition.getFieldColor();
		if (crgb.length != values.length) {
			color = new int[values.length];
			for (int i = 0; i < color.length; i++) {
				color[i] = (((int) (255 * Math.random()) * 256) + (int) (255 * Math
						.random()))
						* 256 + (int) (255 * Math.random());
			}
		} else {
			color = crgb;
		}

		// 处理极端数据

//		double realItemMargin = itemMargin * (values.length - 1) * 1.0 / width;// 根据设置的柱子间的间距，求出所有间距之和占据绘图区域宽度的百分比
//		double barWidth = (width * (1 - realItemMargin) / values.length);
		double realItemMargin = itemMargin/minimumBarLength*(values.length-1);
		double barWidth = width/(values.length+realItemMargin);
		       itemMargin = itemMargin/minimumBarLength*barWidth;
		
		if (barWidth > width * 0.51) {// 设置柱子最大宽度为绘图区域的51%
			barWidth = width * 0.51;
		}

		double bigvalue = 5 * maxValue / 24;// 规定最多有4个大的矩形，4个小的矩形
		double smallvalue = bigvalue / 5;
		double bigbarHeight = 0.0;
		bigbarHeight = bigvalue / (maxValue * (1 + 1 / 24)) * height;// 设定max值刚好达到最大长度，每个小矩形间的空隙为smallvalue矩形的1/3,而小矩形加空隙为bigvalue矩形的1/5，假设空隙宽度为1/4height，则整个为30height，所以其中1/29是根据5/24/（1+x）=
		// 5/25
		double smallbarHeight = 0.0;
		smallbarHeight = bigbarHeight / 5 * 2 / 4;
		double spaceHeight = 0.0;
		spaceHeight = bigbarHeight / 10;
		double radius = barWidth / 4;

		BasicStroke basicStroke = new BasicStroke(outLineBasicStroke);
		g.setStroke(basicStroke);

		for (int i = 0; i < values.length; i++) {
			if (values[i] > (averageValue * 4)&&isCrack) {
				int bigint = 4;
				int barX0 = 0;
				barX0 = (int)(x - barWidth * values.length * 1.0 / 2-itemMargin*(values.length-1)/2 + i
						* (barWidth + itemMargin));
				GradientPaint tGradientPaint = new GradientPaint(
						(float) (barX0), (float) y, new Color(color[i], false),
						(float) (barX0 + 0.5 * barWidth), (float) y, new Color(
								255, 255, 255), true);
				GradientPaint tGradientPaintbrighter = new GradientPaint(
						(float) (barX0), (float) y, new Color(color[i], true).brighter(),
						(float) (barX0 + 0.5 * barWidth), (float) y, new Color(
								255, 255, 255), true);

				for (int j = 0; j < bigint; j++) {

					double rx = barX0;
					double ry = y - bigbarHeight * (1 + j) - spaceHeight * j;
					double rw = barWidth;
					double rh = bigbarHeight;
					Ellipse2D ellipse2DDOWN = new Ellipse2D.Double(rx, ry - 0.5
							* radius + bigbarHeight, rw, radius);

					Rectangle2D.Double tRectangle2D = new Rectangle2D.Double(
							rx, ry, rw, rh);
					Ellipse2D ellipse2DUP = new Ellipse2D.Double(rx, ry - 0.5
							* radius, rw, radius);
					if(GradientPaint){
						g.setPaint(tGradientPaint);
					}else {
						g.setColor(new Color(color[i],false));
					}					
					g.fill(tRectangle2D);
					g.fill(ellipse2DDOWN);
					if(GradientPaint){
						g.setPaint(tGradientPaintbrighter);
					}else {
						g.setColor(new Color(color[i],false).darker());
					}		
					g.fill(ellipse2DUP);

					if (drawBarOutline) {
						g.setColor(new Color(outLinePaint, false));
						Area areaEllipse2DDOWN = new Area(ellipse2DDOWN);
						Area areaRectangle2D = new Area(tRectangle2D);
						Area areaEllipse2DUP = new Area(ellipse2DUP);
						areaEllipse2DDOWN.add(areaRectangle2D);
						areaEllipse2DDOWN.add(areaEllipse2DUP);

						g.draw(areaEllipse2DDOWN);
						g.draw(ellipse2DUP);
					}
				}
				int innerHeight = (int) (0.02 * height);// 锯齿高度
				int barHeight = (int) ((bigbarHeight - innerHeight) / 2);// 共90个像素，断裂前80
				int innerWidth = (int) (barWidth / 3);// 锯齿宽度
				int barY0 = (int) (y - (bigbarHeight + spaceHeight) * 4);

				int[] xpointsDown = { barX0, barX0, barX0 + innerWidth,
						barX0 + 2 * innerWidth, barX0 + 3 * innerWidth,
						barX0 + 3 * innerWidth };
				int[] ypointsDown = { barY0, barY0 - barHeight,
						barY0 - barHeight + innerHeight, barY0 - barHeight,
						barY0 - barHeight + innerHeight, barY0 };

				GeneralPath polygonDown = new GeneralPath();
				GeneralPath polygonUp = new GeneralPath();

				polygonDown.moveTo((float) xpointsDown[1],
						(float) ypointsDown[1]);
				polygonDown.lineTo((float) xpointsDown[0],
						(float) ypointsDown[0]);
				Arc2D arcDown = new Arc2D.Double(barX0, barY0 - radius * 1.0
						/ 2, barWidth, radius, 180, 180, Arc2D.OPEN);
				polygonDown.append(arcDown, true);
				polygonDown.lineTo((float) xpointsDown[4],
						(float) ypointsDown[4]);
				polygonDown.lineTo((float) xpointsDown[3],
						(float) ypointsDown[3]);
				polygonDown.lineTo((float) xpointsDown[2],
						(float) ypointsDown[2]);
				polygonDown.closePath();

				barHeight = barHeight + 2;
				int[] xpointsUp = { barX0, barX0, barX0 + innerWidth,
						barX0 + 2 * innerWidth, barX0 + 3 * innerWidth,
						barX0 + 3 * innerWidth };
				int[] ypointsUp = { barY0 - (int) bigbarHeight,
						barY0 - barHeight, barY0 - barHeight + innerHeight,
						barY0 - barHeight, barY0 - barHeight + innerHeight,
						barY0 - (int) bigbarHeight };

				polygonUp.moveTo((float) xpointsUp[1], (float) ypointsUp[1]);
				polygonUp.lineTo((float) xpointsUp[0], (float) ypointsUp[0]);
				Arc2D arcUp = new Arc2D.Double(barX0, barY0
						- (int) bigbarHeight - radius * 1.0 / 2, barWidth,
						radius, 180, -180, Arc2D.OPEN);
				polygonUp.append(arcUp, true);
				polygonUp.lineTo((float) xpointsUp[4], (float) ypointsUp[4]);
				polygonUp.lineTo((float) xpointsUp[3], (float) ypointsUp[3]);
				polygonUp.lineTo((float) xpointsUp[2], (float) ypointsUp[2]);
				polygonUp.closePath();

				Shape top = new Ellipse2D.Double(barX0, barY0
						- (int) bigbarHeight - radius * 1.0 / 2, barWidth,
						radius);
				g.setColor(new Color(color[i],false));
				g.fill(polygonUp);
				g.fill(polygonDown);
				g.setColor(new Color(color[i], false).darker());
				g.fill(top);

				// 断裂处理的必须标上标签
				Font font = new Font(itemLabelFontName, Font.PLAIN,
						itemLabelFontSize);
				g.setColor(new Color(itemLabelPaint, false));
				g.setFont(font);
				g.drawString(Float.toString((float) values[i]), (int) barX0,
						(int) barY0 - (int) (bigbarHeight + spaceHeight));

				if (drawBarOutline) {

					g.setColor(new Color(outLinePaint, false));
					g.setStroke(basicStroke);
					g.draw(polygonUp);
					g.draw(polygonDown);
					g.draw(top);
				}

			} else {
				int bigint = (int) (values[i] / bigvalue);
				int smallint = (int) ((values[i] % bigvalue) / smallvalue);
				int N = 0;
				int M = 0;
				double barX0 = x - barWidth * values.length * 1.0 / 2-itemMargin*(values.length-1)/2 + i
				* (barWidth + itemMargin);

				GradientPaint tGradientPaint = new GradientPaint((float) (barX0), (float) y, new Color(
						color[i]), (float) (barX0 + 0.5 * barWidth),
						(float) y, new Color(255, 255, 255), true);
				GradientPaint tGradientPaintbrighter = new GradientPaint(
						(float) (barX0), (float) y,
						new Color(color[i]).brighter(), (float) (barX0 + 0.5 * barWidth),
						(float) y, new Color(255, 255, 255), true);

				for (int j = 0; j < bigint; j++) {

					double rx = barX0;
					double ry = y - bigbarHeight * (1 + j) - spaceHeight * j;
					double rw = barWidth;
					double rh = bigbarHeight;
					Ellipse2D ellipse2DDOWN = new Ellipse2D.Double(rx, ry - 0.5
							* radius + bigbarHeight, rw, radius);

					Rectangle2D.Double tRectangle2D = new Rectangle2D.Double(
							rx, ry, rw, rh);
					Ellipse2D ellipse2DUP = new Ellipse2D.Double(rx, ry - 0.5
							* radius, rw, radius);

					if(GradientPaint){
						g.setPaint(tGradientPaint);
					}else{
						g.setColor(new Color(color[i],false));
					}
					g.fill(tRectangle2D);
					g.fill(ellipse2DDOWN);
					if(GradientPaint){
						g.setPaint(tGradientPaint);
					}else{
					 g.setColor(new Color(color[i],false).darker());
					}
					
					g.fill(ellipse2DUP);

					if (drawBarOutline) {
						g.setColor(new Color(outLinePaint, false));
						Area areaEllipse2DDOWN = new Area(ellipse2DDOWN);
						Area areaRectangle2D = new Area(tRectangle2D);
						Area areaEllipse2DUP = new Area(ellipse2DUP);
						areaEllipse2DDOWN.add(areaRectangle2D);
						areaEllipse2DDOWN.add(areaEllipse2DUP);

						g.draw(areaEllipse2DDOWN);
						g.draw(ellipse2DUP);
					}

					N = j + 1;
				}
				for (int j = 0; j < smallint; j++) {
					if (N == 0) {
						double rx = barX0;
						double ry = y - smallbarHeight * (1 + j) - spaceHeight
								* j;
						double rw = barWidth;
						double rh = smallbarHeight;
						Ellipse2D ellipse2DDOWN = new Ellipse2D.Double(rx, ry
								- 0.5 * radius + smallbarHeight, rw, radius);
						Rectangle2D.Double tRectangle2D = new Rectangle2D.Double(
								rx, ry, rw, rh);
						Ellipse2D ellipse2DUP = new Ellipse2D.Double(rx, ry
								- 0.5 * radius, rw, radius);
						if(GradientPaint){
							g.setPaint(tGradientPaint);
						}else{
							g.setColor(new Color(color[i],false));
						}
						g.fill(tRectangle2D);
						g.fill(ellipse2DDOWN);
						if(GradientPaint){
							g.setPaint(tGradientPaint);
						}else{
						 g.setColor(new Color(color[i],false).darker());
						}
						 g.fill(ellipse2DUP);

						if (drawBarOutline) {
							g.setColor(new Color(outLinePaint, false));
							Area areaEllipse2DDOWN = new Area(ellipse2DDOWN);
							Area areaRectangle2D = new Area(tRectangle2D);
							Area areaEllipse2DUP = new Area(ellipse2DUP);
							areaEllipse2DDOWN.add(areaRectangle2D);
							areaEllipse2DDOWN.add(areaEllipse2DUP);

							g.draw(areaEllipse2DDOWN);
							g.draw(ellipse2DUP);
						}
						M++;
					} else {
						double rx = barX0;
						double ry = y - bigbarHeight * N - spaceHeight * N
								- smallbarHeight * (1 + j) - spaceHeight * j;
						double rw = barWidth;
						double rh = smallbarHeight;
						Ellipse2D ellipse2DDOWN = new Ellipse2D.Double(rx, ry
								- 0.5 * radius + smallbarHeight, rw, radius);
						Rectangle2D.Double tRectangle2D = new Rectangle2D.Double(
								rx, ry, rw, rh);
						Ellipse2D ellipse2DUP = new Ellipse2D.Double(rx, ry
								- 0.5 * radius, rw, radius);
						if(GradientPaint){
							g.setPaint(tGradientPaint);
						}else {
							g.setColor(new Color(color[i],false));
						}
						
						g.fill(tRectangle2D);
						g.fill(ellipse2DDOWN);
						if(GradientPaint){	
							g.setPaint(tGradientPaintbrighter);
						}else {
							g.setColor(new Color(color[i],false).darker());
						}
							
						g.fill(ellipse2DUP);

						if (drawBarOutline) {
							g.setColor(new Color(outLinePaint, false));
							Area areaEllipse2DDOWN = new Area(ellipse2DDOWN);
							Area areaRectangle2D = new Area(tRectangle2D);
							Area areaEllipse2DUP = new Area(ellipse2DUP);
							areaEllipse2DDOWN.add(areaRectangle2D);
							areaEllipse2DDOWN.add(areaEllipse2DUP);

							g.draw(areaEllipse2DDOWN);
							g.draw(ellipse2DUP);
						}
						M++;
					}

				}
				if (isLable && (M != 0 || N != 0)) {
					g.setFont(new Font(itemLabelFontName, Font.PLAIN,
							itemLabelFontSize));
					g.setColor(new Color(itemLabelPaint, false));
					g.drawString(Float.toString((float) values[i]), (int) (barX0),
							(int) (y - bigbarHeight * N - spaceHeight * N
									- smallbarHeight * (1 + M) - spaceHeight
									* M));
				}

			}
		}

	}

	public BufferedImage drawLegend(int width, int height,
			ChartDataPara thematicRendition, ChartStyle chartStyle,
			double[] maxValues, double[] minValues,double[] averageValues,
			IndicatorData[] indicatorDatas) {
		double maxValue = maxValues[0];
		double averageValue = averageValues[0];
		boolean isCrack = chartStyle.isIsCrack();
//		boolean isFracture = chartStyle.isFracture();
		if (maxValue > (averageValue * 4)&&isCrack) {
			maxValue = averageValue * 4 * (1 + 1.0 / 9);
		} else {
			maxValue = maxValue * (1 + 1.0 / 9);
		}


		double bigvalue = 5 * maxValue / 24;// 规定最多有4个大的矩形，4个小的矩形
		double smallvalue = bigvalue / 5;
		String[] yearList = new String[indicatorDatas.length];
		for (int i = 0; i < yearList.length; i++) {
			yearList[i] = indicatorDatas[i].getDomainAxis();
		}
		String domainAxisUnit = thematicRendition.getDomainAxisUnit();
		String[] fieldUnits = thematicRendition.getFieldUnits();

		String[] seriesList = thematicRendition.getFieldName();

		String[] domainAxisList = new String[indicatorDatas.length];
		for (int i = 0; i < domainAxisList.length; i++) {
			domainAxisList[i] = indicatorDatas[i].getDomainAxis();
		}
		int[] color;

		int[] crgb = thematicRendition.getFieldColor();
		if (crgb.length != seriesList.length) {
			color = new int[seriesList.length];
			for (int i = 0; i < color.length; i++) {
				color[i] = (((int) (255 * Math.random()) * 256) + (int) (255 * Math
						.random()))
						* 256 + (int) (255 * Math.random());
			}
		} else {
			color = crgb;
		}

		double barWidth = width * (1.0 - 0.1 * seriesList.length)
				/ seriesList.length;
		double maxbarWidth = 0.3 * width;
		if (barWidth > maxbarWidth) {
			barWidth = maxbarWidth;
		}
		double bigbarHeight = 0.0;
		bigbarHeight = bigvalue / (maxValue * (1 + 1 / 24)) * height;// 设定max值刚好达到最大长度，每个小矩形间的空隙为smallvalue矩形的1/3,而小矩形加空隙为bigvalue矩形的1/5，假设空隙宽度为1/4height，则整个为30height，所以其中1/29是根据5/24/（1+x）=
		// 5/25
		double smallbarHeight = 0.0;
		smallbarHeight = bigbarHeight / 5 * 2 / 4;
		double radius = barWidth / 4;

		int symbolWidth = 280;
		int symbolHeight = (int) (66 + bigbarHeight + 33 + 21 * seriesList.length);

		BufferedImage image = new BufferedImage(symbolWidth, symbolHeight,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
//		g.setBackground(Color.WHITE);
//		g.clearRect(0, 0, symbolWidth, symbolHeight);
		g.setColor(Color.BLACK);
		g.draw(new Rectangle(1, 1, symbolWidth - 2, symbolHeight - 2));

		g.setFont(new Font("黑体", Font.PLAIN, 20));
		g.drawString("统计图图例", 92, 30);
		g.setFont(new Font("黑体", Font.PLAIN, 16));
		if ((fieldUnits[0]!=null)&&(!(fieldUnits[0].length()==0))) {
			g.drawString("单位：（" + fieldUnits[0] + "）", 92, 55);
		}
		g.setFont(new Font("黑体", Font.PLAIN, 15));

		double rx = 72;
		double ry = 66 + 0.5 * radius;
		double rw = barWidth;
		//double rh = bigbarHeight;
		
		GeneralPath polygonBig = new GeneralPath();
		GeneralPath polygonSmall = new GeneralPath();
		polygonBig.moveTo(rx, ry);
		polygonBig.lineTo(rx, ry + bigbarHeight);
		Arc2D arcDown = new Arc2D.Double(rx, ry + bigbarHeight - radius * 0.5
				, barWidth, radius, 180, 180, Arc2D.OPEN);
		polygonBig.append(arcDown, true);
		polygonBig.lineTo(rx + barWidth, ry);
		Arc2D arcUp = new Arc2D.Double(rx, ry - radius * 0.5
				, barWidth, radius, 0, 180, Arc2D.OPEN);
		polygonBig.append(arcUp, true);
		polygonBig.closePath();
//		Ellipse2D bigellipse2DDOWN = new Ellipse2D.Double(rx, ry - 0.5 * radius
//				+ bigbarHeight, rw, radius);
//
//		Rectangle2D.Double bigtRectangle2D = new Rectangle2D.Double(rx, ry, rw,
//				rh);
		Ellipse2D bigEllipse2DUP = new Ellipse2D.Double(rx, ry - 0.5 * radius,
				rw, radius);

		rx = 172;
		ry = 66 + bigbarHeight - smallbarHeight + 0.5 * radius;
		rw = barWidth;
		//rh = smallbarHeight;
		polygonSmall.moveTo(rx, ry);
		polygonSmall.lineTo(rx, ry + smallbarHeight);
		arcDown = new Arc2D.Double(rx, ry + smallbarHeight - radius * 0.5
				, barWidth, radius, 180, 180, Arc2D.OPEN);
		polygonSmall.append(arcDown, true);
		polygonSmall.lineTo(rx + barWidth, ry);
		arcUp = new Arc2D.Double(rx, ry - radius * 0.5
				, barWidth, radius, 0, 180, Arc2D.OPEN);
		polygonSmall.append(arcUp, true);
		polygonSmall.closePath();
//		Ellipse2D smallellipse2DDOWN = new Ellipse2D.Double(rx, ry - 0.5
//				* radius + smallbarHeight, rw, radius);
//
//		Rectangle2D.Double smalltRectangle2D = new Rectangle2D.Double(rx, ry,
//				rw, rh);
		Ellipse2D smallEllipse2DUP = new Ellipse2D.Double(rx,
				ry - 0.5 * radius, rw, radius);

		g.setColor(Color.BLACK);
		g.draw(polygonBig);
		g.draw(bigEllipse2DUP);
		g.draw(smallEllipse2DUP);
		g.draw(polygonSmall);
//		g.fill(smalltRectangle2D);
//		g.fill(smallellipse2DDOWN);
//		g.fill(bigellipse2DDOWN);
//		g.fill(bigtRectangle2D);
//		g.setColor(Color.GRAY);
//		g.fill(bigellipse2DUP);
//		g.fill(smallellipse2DUP);

		g.setColor(Color.BLACK);
		g.drawString(Integer.toString((int) bigvalue), 72,
				66 + (int) bigbarHeight + 5 + 15);
		g.drawString(Integer.toString((int) smallvalue), 172,
				66 + (int) bigbarHeight + 5 + 15);

		if (domainAxisUnit != null) {
			g.setColor(Color.BLACK);
			g.drawString(domainAxisList[0] + domainAxisUnit, 72, 66
					+ (int) bigbarHeight + 5 + 15 + 5 + 21
					* (seriesList.length / 2 + 1));
		}


		for (int i = 0; i < seriesList.length; i++) {
			int xx =172;
			if (domainAxisUnit == null) {
				xx = 92;
			}
			Rectangle2D.Double fieldRect = new Rectangle2D.Double(xx, 66
					+ (int) bigbarHeight + 5 + 15 + 5 + 21 * (i + 1) - 10, 7, 7);
			g.setColor(new Color(color[i],false));
			g.fill(fieldRect);
			g.setColor(Color.BLACK);
			g.drawString(seriesList[i], xx+13, 66 + (int) bigbarHeight + 5 + 15
					+ 5 + 21 * (i + 1));
		}
		g.dispose();

		return image;
	}
}
