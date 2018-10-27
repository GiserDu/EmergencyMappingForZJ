package com.zz.chart.obj.bar;

import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;


public class PartBarChart extends BarChart {
	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		// 消除线条锯齿
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
		color = crgb;

		// 处理极端数据
		y = y+height /2;
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
		bigbarHeight = bigvalue / (maxValue * (1 + 1 / 24)) * height;
		// 设定max值刚好达到最大长度，每个小矩形间的空隙为smallvalue矩形的1/3,而小矩形加空隙为bigvalue矩形的1/5，假设空隙宽度为1/4height，则整个为30height，所以其中1/29是根据5/24/（1+x）=
		// 5/25
		double smallbarHeight = 0.0;
		smallbarHeight = bigbarHeight / 5 * 3 / 4;
		double spaceHeight = 0.0;
		spaceHeight = bigbarHeight / 20;

		for (int i = 0; i < values.length; i++) {
			if (values[i] > (averageValue * 4)&&isCrack) {
				int bigint = 4;
				int barX0 = 0;
				barX0 = (int)(x - barWidth * values.length * 1.0 / 2-itemMargin*(values.length-1)/2 + i
						* (barWidth + itemMargin));

				GradientPaint gradientPaint = new GradientPaint(
						(float) (barX0), (float) y, new Color(color[i], false),
						(float) (barX0 + 0.5 * barWidth), (float) y, new Color(
								255, 255,color[i]/(256*256*256)), true);

				for (int j = 0; j < bigint; j++) {
					if(GradientPaint){
						g.setPaint(gradientPaint);
					}else {
						g.setColor(new Color(color[i],false));
					}
										
					Rectangle2D.Double tRectangle2D = new Rectangle2D.Double(
							barX0,
							y - bigbarHeight * (1 + j) - spaceHeight * j,
							barWidth, bigbarHeight);
					g.fill(tRectangle2D);
					if (drawBarOutline) {
						g.setColor(new Color(outLinePaint, false));
						BasicStroke basicStroke = new BasicStroke(
								outLineBasicStroke);
						g.setStroke(basicStroke);
						g.draw(tRectangle2D);
					}
				}

				int innerHeight = (int) (0.02 * height);// 锯齿高度
				int barHeight = (int) ((bigbarHeight - innerHeight) / 2);// 共90个像素，断裂前80
				int innerWidth = (int) (barWidth / 3);// 锯齿宽度
				int barY0 = (int) (y - (bigbarHeight + spaceHeight) * 4);
				int[] xpointsDown = { barX0, barX0, barX0 + innerWidth,
						barX0 + 2 * innerWidth, barX0 + (int) barWidth,
						barX0 + (int) barWidth };
				int[] ypointsDown = { barY0, barY0 - barHeight,
						barY0 - barHeight + innerHeight, barY0 - barHeight,
						barY0 - barHeight + innerHeight, barY0 };
				int npointsDown = xpointsDown.length;
				Polygon polygonDown = new Polygon(xpointsDown, ypointsDown,
						npointsDown);

				barHeight = barHeight + 2;
				int[] xpointsUp = { barX0, barX0, barX0 + innerWidth,
						barX0 + 2 * innerWidth, barX0 + (int) barWidth,
						barX0 + (int) barWidth };
				int[] ypointsUp = { barY0 - (int) bigbarHeight,
						barY0 - barHeight, barY0 - barHeight + innerHeight,
						barY0 - barHeight, barY0 - barHeight + innerHeight,
						barY0 - (int) bigbarHeight };
				int npointsUp = xpointsUp.length;
				Polygon polygonUP = new Polygon(xpointsUp, ypointsUp, npointsUp);

				if(GradientPaint){
					g.setPaint(gradientPaint);
				}else {
					g.setColor(new Color(color[i],false));
				}
				g.fill(polygonUP);
				g.fill(polygonDown);

				// 断裂处理的必须标上标签
				Font font = new Font(itemLabelFontName, Font.PLAIN,
						itemLabelFontSize);
				g.setColor(new Color(itemLabelPaint, false));
				g.setFont(font);
				g.drawString(Float.toString((float) values[i]), (int) barX0,
						barY0 - (int) (bigbarHeight + spaceHeight));
				if (drawBarOutline) {

					g.setColor(new Color(outLinePaint, false));
					BasicStroke basicStroke = new BasicStroke(
							outLineBasicStroke);
					g.setStroke(basicStroke);
					g.draw(polygonUP);
					g.draw(polygonDown);
				}
			} else {

				double bax0 = x - barWidth * values.length * 1.0 / 2-itemMargin*(values.length-1)/2 + i
				* (barWidth + itemMargin);
				int bigint = (int) (values[i] / bigvalue);
				int smallint = (int) ((values[i] % bigvalue) / smallvalue);
				int N = 0;
				int M = 0;
				GradientPaint gradientPaint = new GradientPaint((float) (bax0), (float) y, new Color(
						color[i], false),
						(float) (bax0 + 0.5 * barWidth),
						(float) y, new Color(255, 255, color[i]/(256*256*256)), true);

				for (int j = 0; j < bigint; j++) {
					if(GradientPaint){
						g.setPaint(gradientPaint);
					}else {
						g.setColor(new Color(color[i],false));
					}
					Rectangle2D.Double tRectangle2D = new Rectangle2D.Double(bax0, y - bigbarHeight
							* (1 + j) - spaceHeight * j, barWidth, bigbarHeight);
					g.fill(tRectangle2D);
					if (drawBarOutline) {
						g.setColor(new Color(outLinePaint, false));
						BasicStroke basicStroke = new BasicStroke(
								outLineBasicStroke);
						g.setStroke(basicStroke);
						g.draw(tRectangle2D);
					}
					N = j + 1;
				}
				for (int j = 0; j < smallint; j++) {
					if (N == 0) {
					if(GradientPaint){
						g.setPaint(gradientPaint);
					}else {
						g.setColor(new Color(color[i],false));
					}
						Rectangle2D.Double tRectangle2D = new Rectangle2D.Double(
								bax0, y - smallbarHeight * (1 + j)
										- spaceHeight * j, barWidth,
								smallbarHeight);
						g.fill(tRectangle2D);
						if (drawBarOutline) {
							g.setColor(new Color(outLinePaint, false));
							BasicStroke basicStroke = new BasicStroke(
									outLineBasicStroke);
							g.setStroke(basicStroke);
							g.draw(tRectangle2D);
						}
						M++;
					} else {
						if(GradientPaint){
							g.setPaint(gradientPaint);
						}else {
							g.setColor(new Color(color[i],false));
						}
						Rectangle2D.Double tRectangle2D = new Rectangle2D.Double(
								bax0, y
										- bigbarHeight * N - spaceHeight * N
										- smallbarHeight * (1 + j)
										- spaceHeight * j, barWidth,
								smallbarHeight);

						g.fill(tRectangle2D);
						if (drawBarOutline) {
							g.setColor(new Color(outLinePaint, false));
							BasicStroke basicStroke = new BasicStroke(
									outLineBasicStroke);
							g.setStroke(basicStroke);
							g.draw(tRectangle2D);
						}
						M++;
					}
				}
				if (isLable && (M != 0 || N != 0)) {
					g.setFont(new Font(itemLabelFontName, Font.PLAIN,
							itemLabelFontSize));
					g.setColor(new Color(itemLabelPaint, false));
					g.drawString(Float.toString((float) values[i]), (int) (bax0),
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
//		boolean isFracture = chartStyle.isFracture();
		boolean isCrack = chartStyle.isIsCrack();
		if (maxValue > (averageValue * 4)&&isCrack) {
			maxValue = averageValue * 4 * (1 + 1.0 / 9);
		} else {
			maxValue = maxValue * (1 + 1.0 / 9);
		}


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

		double bigvalue = 5 * maxValue / 24;// 规定最多有4个大的矩形，4个小的矩形
		double smallvalue = bigvalue / 5;

		double barWidth = width * (1.0 - 0.1 * domainAxisList.length)
				/ domainAxisList.length;
		double maxbarWidth = 0.3 * width;
		if (barWidth > maxbarWidth) {
			barWidth = maxbarWidth;
		}
		double bigbarHeight = 0.0;
		bigbarHeight = bigvalue / (maxValue * (1 + 1 / 24)) * height;// 设定max值刚好达到最大长度，每个小矩形间的空隙为smallvalue矩形的1/3,而小矩形加空隙为bigvalue矩形的1/5，假设空隙宽度为1/4height，则整个为30height，所以其中1/29是根据5/24/（1+x）=
		// 5/25
		double smallbarHeight = 0.0;
		smallbarHeight = bigbarHeight / 5 * 3 / 4;

		int symbolWidth = 300;
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
		g.drawString("统计图图例", 105, 30);
		g.setFont(new Font("黑体", Font.PLAIN, 16));
		if ((fieldUnits[0]!=null)&&(!(fieldUnits[0].length()==0))) {
			g.drawString("单位：（" + fieldUnits[0] + "）", 92, 55);
		}
		g.setFont(new Font("黑体", Font.PLAIN, 15));
		Rectangle2D.Double tbigRectangle2D = new Rectangle2D.Double(72, 66,
				barWidth, bigbarHeight);
		g.drawString(Integer.toString((int) bigvalue), 72,
				66 + (int) bigbarHeight + 5 + 15);
		Rectangle2D.Double tsmallRectangle2D = new Rectangle2D.Double(172, 66
				+ bigbarHeight - smallbarHeight, barWidth, smallbarHeight);
		g.drawString(Integer.toString((int) smallvalue), 172,
				66 + (int) bigbarHeight + 5 + 15);
		g.draw(tsmallRectangle2D);
		g.draw(tbigRectangle2D);
//		g.fill(tsmallRectangle2D);
//		g.fill(tbigRectangle2D);

		
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
