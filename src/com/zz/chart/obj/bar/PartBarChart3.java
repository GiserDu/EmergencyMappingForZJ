package com.zz.chart.obj.bar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;

public class PartBarChart3 extends PartBarChart {
	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		// 消除线条锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		double maxValue = maxValues[0];
		double[] values;// 存放单变量多指标的值，例如存放2012年男女指标的人口数
		if (indicatorDatas[0] ==null) {
			System.out.println("绘制专题符号图层时发现专题数据中区域数据不完整！");
			return;
		}
		values = indicatorDatas[0].getValues();

		maxValue = maxValue * (1 + 1.0 / 9);
		

		// 获取符号绘制样式
		@SuppressWarnings("unused")
		String chartID = chartStyle.getChartID();// 符号ID
		boolean drawBarOutline = chartStyle.isDrawBarOutLine();// 是否绘制柱子边线
		int outLinePaint = chartStyle.getOutLinePaint();// 柱子边线颜色
		float outLineBasicStroke = chartStyle.getOutLineBasicStroke();// 柱子边线粗细
		double itemMargin = chartStyle.getItemMargin();// 柱子间的距离，像素为单位
		//int transparent = chartStyle.getTransparent();// 填充色透明度
		boolean GradientPaint = chartStyle.isGradientPaint();

		HashMap<String, Color> fieldNameAndColorHashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			fieldNameAndColorHashMap.put(thematicRendition.getFieldName()[i],
					new Color(thematicRendition.getFieldColor()[i], true));
		}

		int partsNum = 3;// 分三级
		//int partMaxNUM = 10;// 每级最多有十个
		int orientation = 1;// 1表示垂直方向叠放，2表示水平方向放置

		String maxValueString = Double.toString(maxValue);
		String[] numArray = new String[maxValueString.length()];
		int indexOfDot = 0;
		for (int i = 0; i < numArray.length; i++) {
			Character ch = maxValueString.charAt(i);
			numArray[i] = ch.toString();
			if (numArray[i].equals(".")) {
				indexOfDot = i;
			}
		}

		double[] valueArray = new double[partsNum];// 每级代表多少
		int[] intArray = new int[partsNum];// 每级有多少个
		int index = 0;
		boolean isZero = false;//之前是否有过不为零的数
		for (int i = 0; i < numArray.length; i++) {
			if (index != partsNum) {
				if (Integer.parseInt(numArray[0]) == 0) {
					if (isZero == true || Integer.parseInt(numArray[i]) != 0) {
						// intArray[index] = Integer.parseInt(numArray[i]);
						int temp = 1;
						for (int j = 0; j < i - 1; j++) {
							temp = temp * 10;
						}
						valueArray[index] = 1.0 / temp;
						index++;
						isZero = true;
					}
				} else {
					if (i < indexOfDot) {
						// intArray[index] = Integer.parseInt(numArray[i]);
						int temp = 1;
						for (int j = 0; j < indexOfDot - i - 1; j++) {
							temp = temp * 10;
						}
						valueArray[index] = 1.0 * temp;
						index++;
					} else if (i == indexOfDot) {
						continue;
					} else {
						// intArray[index] = Integer.parseInt(numArray[i]);
						int temp = 1;
						for (int j = 0; j < i - indexOfDot; j++) {
							temp = temp * 10;
						}
						valueArray[index] = 1.0 / temp;
						index++;
					}
				}
			}
		}
		int[] barHeights = new int[partsNum];// 柱子高度数组
		int[] barWidths = new int[partsNum];// 柱子宽度数组
		width = width - (int)(itemMargin* (thematicRendition.getFieldName().length - 1));
		int plusH = (int)(3.0/240*height);// 垂直方向递增大小
		int plusW = (int)(4.0/50*width);// 水平方向递增大小
		for (int i = 0; i < barHeights.length; i++) {
			if (i == 0) {
				barHeights[i] = (int)(4.0/240*height);// 最矮的
			} else {
				barHeights[i] = barHeights[i - 1] + plusH;// 依次递增
			}
		}
		for (int i = 0; i < barWidths.length; i++) {
			if (i == 0) {
				barWidths[i] = (int)(5.0/50*width);// 最窄的
			} else {
				barWidths[i] = barWidths[i - 1] + plusW;// 依次递增
			}
		}
		
		int space = 1;// 空隙

		// Rectangle2D.Double testDouble = new Rectangle2D.Double(x,y -
		// height,width,height);
		// g.draw(testDouble);
		if (orientation == 1) {// 垂直
			x = x
					- (barWidths[partsNum - 1] / 2 + values.length
							* (int) (barWidths[partsNum - 1] + itemMargin)) / 2;

			for (int i = 0; i < values.length; i++) {
				for (int j = 0; j < intArray.length; j++) {
					if (j == 0) {
						intArray[j] = (int) (values[i] / valueArray[j]);
					} else {
						int[] tempArray = new int[j];
						int tempNum = 1;
						for (int j2 = 0; j2 < j; j2++) {
							tempNum = 10 * tempNum;
							tempArray[j2] = intArray[j - j2 - 1] * tempNum;
						}
						intArray[j] = (int) (values[i] / valueArray[j]);
						for (int k = 0; k < tempArray.length; k++) {
							intArray[j] = intArray[j] - tempArray[k];
						}

					}

				}
				int barX0 = (int) x + barWidths[partsNum - 1] / 2 + i
						* (int) (barWidths[partsNum - 1] + itemMargin);
				int barY0 = (int) y;

				int barMaxY = barY0;// 记录每一级最大Y
				for (int j = 0; j < intArray.length; j++) {
					int barX = barX0 + plusW / 2 * j;
					int N = 0;
					int barHeight = barHeights[partsNum - 1 - j];
					int barWidth = barWidths[partsNum - 1 - j];
					for (int k = 0; k < intArray[j]; k++) {

						int barY = barMaxY - barHeight - (barHeight + space)
								* k;
						Rectangle2D.Double bar = new Rectangle2D.Double(barX,
								barY, barWidth, barHeight);
						//设置填充颜色
						if(GradientPaint){
						 GradientPaint gradientPaint = new GradientPaint((float)
						 barX0,
						 (float) (barY0), fieldNameAndColorHashMap
						 .get(thematicRendition.getFieldName()[i]),
						 (float) (barX0 + barWidth / 2), (float) (barY0),
						 new Color(255, 255, 255, 255), true);
						 g.setPaint(gradientPaint);
						}else {
							g.setColor(fieldNameAndColorHashMap.get(thematicRendition
									.getFieldName()[i]));
						}
						
						g.fill(bar);
						if (drawBarOutline) {
							g.setColor(new Color(outLinePaint, true));
							BasicStroke basicStroke = new BasicStroke(
									outLineBasicStroke);
							g.setStroke(basicStroke);
							g.draw(bar);
						}
						N++;
					}
					barMaxY = barMaxY - (barHeight + space) * N;
				}
			}
		}

	}

	public BufferedImage drawLegend(int width, int height,
			ChartDataPara thematicRendition, ChartStyle chartStyle,
			double[] maxValues, double[] minValues,double[] averageValues,
			IndicatorData[] indicatorDatas) {
		double maxValue = maxValues[0];

		maxValue = maxValue * (1 + 1.0 / 9);
		

		double itemMargin = chartStyle.getItemMargin();
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

		int partsNum = 3;// 分三级
		//int partMaxNUM = 10;// 每级最多有十个
		//int orientation = 1;// 1表示垂直方向叠放，2表示水平方向放置

		String maxValueString = Double.toString(maxValue);
		String[] maxValuenumArray = new String[maxValueString.length()];
		int indexOfDot = 0;
		for (int i = 0; i < maxValuenumArray.length; i++) {
			Character ch = maxValueString.charAt(i);
			maxValuenumArray[i] = ch.toString();
			if (maxValuenumArray[i].equals(".")) {
				indexOfDot = i;
			}
		}

		double[] valueArray = new double[partsNum];// 每级代表多少
		//int[] intArray = new int[partsNum];// 每级有多少个
		int index = 0;// 每级代表值大小的下标
		boolean isZero = false;
		for (int i = 0; i < maxValuenumArray.length; i++) {
			if (index != partsNum) {
				if (Integer.parseInt(maxValuenumArray[0]) == 0) {
					if (isZero == true
							|| Integer.parseInt(maxValuenumArray[i]) != 0) {
						// intArray[index] = Integer.parseInt(numArray[i]);
						int temp = 1;
						for (int j = 0; j < i - 1; j++) {
							temp = temp * 10;
						}
						valueArray[index] = 1.0 / temp;
						index++;
						isZero = true;
					}
				} else {
					if (i < indexOfDot) {
						// intArray[index] = Integer.parseInt(numArray[i]);
						int temp = 1;
						for (int j = 0; j < indexOfDot - i - 1; j++) {
							temp = temp * 10;
						}
						valueArray[index] = 1.0 * temp;
						index++;
					} else if (i == indexOfDot) {
						continue;
					} else {
						// intArray[index] = Integer.parseInt(numArray[i]);
						int temp = 1;
						for (int j = 0; j < i - indexOfDot; j++) {
							temp = temp * 10;
						}
						valueArray[index] = 1.0 / temp;
						index++;
					}
				}
			}
		}

		int[] barHeights = new int[partsNum];// 柱子高度数组
		int[] barWidths = new int[partsNum];// 柱子宽度数组
		width = width - (int)(itemMargin* (thematicRendition.getFieldName().length - 1));
		int plusH = (int)(3.0/240*height);// 垂直方向递增大小
		int plusW = (int)(4.0/50*width);// 水平方向递增大小
		for (int i = 0; i < barHeights.length; i++) {
			if (i == 0) {
				barHeights[i] = (int)(4.0/240*height);// 最矮的
			} else {
				barHeights[i] = barHeights[i - 1] + plusH;// 依次递增
			}
		}
		for (int i = 0; i < barWidths.length; i++) {
			if (i == 0) {
				barWidths[i] = (int)(5.0/50*width);// 最窄的
			} else {
				barWidths[i] = barWidths[i - 1] + plusW;// 依次递增
			}
		}
		
		//int space = (int)(1.0/240*height);// 空隙

		int symbolWidth = 337;
		int symbolHeight = (int) (80 + barHeights[partsNum - 1] + 33 + 21 * seriesList.length);

		BufferedImage image = new BufferedImage(symbolWidth, symbolHeight,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
//		g.setBackground(Color.WHITE);
//		g.clearRect(0, 0, symbolWidth, symbolHeight);
		g.setColor(Color.BLACK);
		g.draw(new Rectangle(1, 1, symbolWidth - 2, symbolHeight - 2));

		g.setFont(new Font("黑体", Font.PLAIN, 25));
		g.drawString("图例", 142, 30);
		g.setFont(new Font("黑体", Font.PLAIN, 20));
		if ((fieldUnits[0]!=null)&&(!(fieldUnits[0].length()==0))) {
			g.drawString("单位：（" + fieldUnits[0] + "）", 92, 55);
		}
		g.setFont(new Font("黑体", Font.PLAIN, 15));


		int barX0 = 170 - 65 * barWidths.length / 2;

		for (int i = 0; i < barWidths.length; i++) {
			int barX = barX0 + 65 * i;
			Rectangle2D.Double tRectangle2D = new Rectangle2D.Double(barX,
					80 - barHeights[partsNum - i - 1], barWidths[partsNum - i
							- 1], barHeights[partsNum - i - 1]);
			g.setColor(Color.BLACK);
			g.drawString(Double.toString(valueArray[i]), barX,
					80 + 5 + 15);
			g.draw(tRectangle2D);
			//g.fill(tRectangle2D);
		}
		if (domainAxisUnit != null) {
			g.setColor(Color.BLACK);
			g.drawString(domainAxisList[0] + domainAxisUnit, 72, 80
					+ barHeights[partsNum - 1] + 5 + 15 + 5 + 21
					* (seriesList.length / 2 + 1));
		}


		for (int i = 0; i < seriesList.length; i++) {
			int xx =172;
			if (domainAxisUnit == null) {
				xx = 92;
			}
			Rectangle2D.Double fieldRect = new Rectangle2D.Double(xx,
					80 + barHeights[partsNum - 1] + 5 + 15 + 5 + 21 * (i + 1)
							- 10, 7, 7);
			g.setColor(new Color(color[i],true));
			g.fill(fieldRect);
			g.setColor(Color.BLACK);
			g.drawString(seriesList[i], xx+13, 80 + barHeights[partsNum - 1] + 5
					+ 15 + 5 + 21 * (i + 1));
		}
		g.dispose();

		return image;
	}

	public HashMap<String, Shape> generateHotArea(double x, double y,
			int width, int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		double maxValue = maxValues[0];
		double[] values;// 存放单变量多指标的值，例如存放2012年男女指标的人口数

		if (indicatorDatas[0] ==null) {
			System.out.println("获取专题符号图层热区时发现专题数据中区域数据不完整！");
			return null;
		}
		values = indicatorDatas[0].getValues();

		maxValue = maxValue * (1 + 1.0 / 9);
		

		// 获取符号绘制样式
		@SuppressWarnings("unused")
		String chartID = chartStyle.getChartID();// 符号ID
		double itemMargin = chartStyle.getItemMargin();// 柱子间的距离，像素为单位

		int transparent = chartStyle.getTransparent();// 填充色透明度

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
		HashMap<String, Color> fieldNameAndColorHashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldColor().length; i++) {
			fieldNameAndColorHashMap.put(thematicRendition.getFieldName()[i],
					new Color(transparent * 256 * 256 * 256
							+ thematicRendition.getFieldColor()[i], true));
		}

		int partsNum = 3;// 分三级
		//int partMaxNUM = 10;// 每级最多有十个
		int orientation = 1;// 1表示垂直方向叠放，2表示水平方向放置

		String maxValueString = Double.toString(maxValue);
		String[] numArray = new String[maxValueString.length()];
		int indexOfDot = 0;
		for (int i = 0; i < numArray.length; i++) {
			Character ch = maxValueString.charAt(i);
			numArray[i] = ch.toString();
			if (numArray[i].equals(".")) {
				indexOfDot = i;
			}
		}

		double[] valueArray = new double[partsNum];// 每级代表多少
		int[] intArray = new int[partsNum];// 每级有多少个
		int index = 0;
		boolean isZero = false;
		for (int i = 0; i < numArray.length; i++) {
			if (index != partsNum) {
				if (Integer.parseInt(numArray[0]) == 0) {
					if (isZero == true || Integer.parseInt(numArray[i]) != 0) {
						// intArray[index] = Integer.parseInt(numArray[i]);
						int temp = 1;
						for (int j = 0; j < i - 1; j++) {
							temp = temp * 10;
						}
						valueArray[index] = 1.0 / temp;
						index++;
						isZero = true;
					}
				} else {
					if (i < indexOfDot) {
						// intArray[index] = Integer.parseInt(numArray[i]);
						int temp = 1;
						for (int j = 0; j < indexOfDot - i - 1; j++) {
							temp = temp * 10;
						}
						valueArray[index] = 1.0 * temp;
						index++;
					} else if (i == indexOfDot) {
						continue;
					} else {
						// intArray[index] = Integer.parseInt(numArray[i]);
						int temp = 1;
						for (int j = 0; j < i - indexOfDot; j++) {
							temp = temp * 10;
						}
						valueArray[index] = 1.0 / temp;
						index++;
					}
				}
			}
		}
		int[] barHeights = new int[partsNum];// 柱子高度数组
		int[] barWidths = new int[partsNum];// 柱子宽度数组
		width = width - (int)(itemMargin* (thematicRendition.getFieldName().length - 1));
		int plusH = (int)(3.0/240*height);// 垂直方向递增大小
		int plusW = (int)(4.0/50*width);// 水平方向递增大小
		for (int i = 0; i < barHeights.length; i++) {
			if (i == 0) {
				barHeights[i] = (int)(4.0/240*height);// 最矮的
			} else {
				barHeights[i] = barHeights[i - 1] + plusH;// 依次递增
			}
		}
		for (int i = 0; i < barWidths.length; i++) {
			if (i == 0) {
				barWidths[i] = (int)(5.0/50*width);// 最窄的
			} else {
				barWidths[i] = barWidths[i - 1] + plusW;// 依次递增
			}
		}
		
		int space = 1;// 空隙

		// Rectangle2D.Double testDouble = new Rectangle2D.Double(x,y -
		// height,width,height);
		// g.draw(testDouble);
		HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
		if (orientation == 1) {// 垂直
			x = x
					- (barWidths[partsNum - 1] / 2 + values.length
							* (int) (barWidths[partsNum - 1] + itemMargin)) / 2;

			for (int i = 0; i < values.length; i++) {
				for (int j = 0; j < intArray.length; j++) {
					if (j == 0) {
						intArray[j] = (int) (values[i] / valueArray[j]);
					} else {
						int[] tempArray = new int[j];
						int tempNum = 1;
						for (int j2 = 0; j2 < j; j2++) {
							tempNum = 10 * tempNum;
							tempArray[j2] = intArray[j - j2 - 1] * tempNum;
						}
						intArray[j] = (int) (values[i] / valueArray[j]);
						for (int k = 0; k < tempArray.length; k++) {
							intArray[j] = intArray[j] - tempArray[k];
						}

					}

				}
				int barX0 = (int) x + barWidths[partsNum - 1] / 2 + i
						* (int) (barWidths[partsNum - 1] + itemMargin);
				int barY0 = (int) y;

				int barMaxY = barY0;// 记录每一级最大Y
				Area area = new Area();
				for (int j = 0; j < intArray.length; j++) {
					int barX = barX0 + plusW / 2 * j;
					int N = 0;
					int barHeight = barHeights[partsNum - 1 - j];
					int barWidth = barWidths[partsNum - 1 - j];
					for (int k = 0; k < intArray[j]; k++) {
						N++;
					}
					Rectangle rectangle = new Rectangle(barX, barMaxY
							- (barHeight + space) * N, barWidth,
							(barHeight + space) * N);
					Area area1 = new Area(rectangle);
					area.add(area1);
					barMaxY = barMaxY - (barHeight + space) * N;
				}
				String text = "";

				if ((thematicRendition.getDomainAxis()[0] == null)||(thematicRendition.getDomainAxis()[0].length()==0)) {
					text = "("+indicatorDatas[0].getNames()[i] + ")="
					+ indicatorDatas[0].getValues()[i];
				}else {
					text = "(" + thematicRendition.getDomainAxis()[0] + ","
					+ indicatorDatas[0].getNames()[i] + ")="
					+ indicatorDatas[0].getValues()[i];
				}
				hotAreahHashMap.put(text, area);
			}
		}

		return hotAreahHashMap;
	}
}
