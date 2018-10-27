package com.zz.chart.obj.bar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.HashMap;

import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;

public class StackedCylinderChart extends StackedBarChart {

	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		// 消除线条锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		double maxValue = maxValues[0];
		@SuppressWarnings("unused")
		Rectangle2D.Double symbolEnvlope = new Rectangle2D.Double(x
				- thematicRendition.getWidth() / 2, y
				- thematicRendition.getHeight(), width, height);// 设置绘图区域

		int fieldNum = thematicRendition.getFieldName().length;

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
		double minimumBarLength = chartStyle.getMinimumBarLength();
		boolean GradientPaint = chartStyle.isGradientPaint();
		//int transparent = chartStyle.getTransparent();// 填充色透明度
		if (itemMargin < 0 || itemMargin * fieldNum > width) {
			System.out.print("柱子间距设置有误！");
		}
//		double realItemMargin = itemMargin
//				* (thematicRendition.getDomainAxis().length - 1) * 1.0
//				/ thematicRendition.getWidth();// 根据设置的柱子间的间距，求出所有间距之和占据绘图区域宽度的百分比
//		double barWidth = (symbolEnvlope.getWidth() * (1 - realItemMargin) / indicatorDatas.length);
		double realItemMargin = itemMargin/minimumBarLength*(thematicRendition.getDomainAxis().length - 1);
		double barWidth = width/(thematicRendition.getDomainAxis().length+realItemMargin);
		       itemMargin = itemMargin/minimumBarLength*barWidth;
		double maxBarWidth = 0.51 * width;
		if (barWidth > maxBarWidth) {
			barWidth = maxBarWidth;
		}
		maxValue = thematicRendition.getScales()[0] * width;

		HashMap<String, Color> fieldNameAndColorHashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			fieldNameAndColorHashMap.put(thematicRendition.getFieldName()[i],
					new Color(thematicRendition.getFieldColor()[i], false));
		}
		int yOffSet = (int) (barWidth / 4);
		//int xOffSet = (int) (barWidth / 4);

		for (int i = 0; i < thematicRendition.getDomainAxis().length; i++) {
			if (indicatorDatas[i] != null) {
				double y0 = y;
				double x0 = 0;
				if (barWidth < width * 0.5) {
					x0 = x - thematicRendition.getWidth() / 2
							+ (barWidth + itemMargin) * i;
				} else {
					x0 = x - barWidth / 2;
				}
				double totalBarHeight = 0;
				double totalValues = 0;
				for (int j = 0; j < indicatorDatas[0].getNames().length; j++) {

					double barHeight = indicatorDatas[i].getValues()[j]
							/ maxValue * height;

					totalBarHeight = totalBarHeight + barHeight;
					totalValues = totalValues
							+ indicatorDatas[i].getValues()[j];

					GradientPaint gradientPaint = new GradientPaint((float) x0,
							(float) (y0), fieldNameAndColorHashMap
									.get(thematicRendition.getFieldName()[j]),
							(float) (x0 + barWidth / 2), (float) (y0),
							new Color(255, 255, 255), true);

					int[] xpointsDown = { (int) x0, (int) x0,
							(int) (x0 + barWidth), (int) (x0 + barWidth) };
					int[] ypointsDown = { (int) y0, (int) y0 - (int) barHeight,
							(int) y0 - (int) barHeight, (int) y0 };

					GeneralPath polygon = new GeneralPath();

					polygon.moveTo((float) xpointsDown[1],
							(float) ypointsDown[1]);
					polygon.lineTo((float) xpointsDown[0],
							(float) ypointsDown[0]);
					Arc2D arcDown = new Arc2D.Double(x0,
							y0 - yOffSet * 1.0 / 2, barWidth, yOffSet, 180,
							180, Arc2D.OPEN);
					polygon.append(arcDown, true);
					polygon.lineTo((float) xpointsDown[2],
							(float) ypointsDown[2]);
					Arc2D arcUp = new Arc2D.Double(x0, y0 - barHeight - yOffSet
							* 1.0 / 2, barWidth, yOffSet, 0, 180, Arc2D.OPEN);
					polygon.append(arcUp, true);
					polygon.closePath();
					Shape top = new Ellipse2D.Double(x0, y0 - barHeight
							- yOffSet * 1.0 / 2, barWidth, yOffSet);
					if(GradientPaint){
						g.setPaint(gradientPaint);
					}else {
						g.setColor(fieldNameAndColorHashMap.get(thematicRendition.getFieldName()[j]));	
					}
					g.fill(polygon);
					g.setColor(fieldNameAndColorHashMap.get(
							thematicRendition.getFieldName()[j]).darker());
					g.fill(top);

					if (drawBarOutline) {

						g.setColor(new Color(outLinePaint, false));
						BasicStroke basicStroke = new BasicStroke(
								outLineBasicStroke);
						g.setStroke(basicStroke);
						g.draw(polygon);
						g.draw(top);
					}
					y0 = y0 - barHeight;

				}
				if (isLable) {
					Font font = new Font(itemLabelFontName, Font.PLAIN,
							itemLabelFontSize);
					g.setColor(new Color(itemLabelPaint, false));
					g.setFont(font);
					g.drawString(Float.toString((float) totalValues),
							(int) x0, (int) (y - totalBarHeight));
				}
			} else {
				System.out.println("绘制专题符号图层时发现专题数据中区域数据不完整！");

			}

		}

	}

	public BufferedImage drawLegend(int width, int height,
			ChartDataPara thematicRendition, ChartStyle chartStyle,
			double[] maxValues, double[] minValues, double[] averageValues,
			IndicatorData[] indicatorDatas) {
		double maxValue = maxValues[0];

		String[] fieldUnits = thematicRendition.getFieldUnits();

		String[] fieldList = thematicRendition.getFieldName();

		// 获取符号绘制样式
		@SuppressWarnings("unused")
		String chartID = chartStyle.getChartID();// 符号ID
		boolean drawBarOutline = chartStyle.isDrawBarOutLine();// 是否绘制柱子边线
		int outLinePaint = chartStyle.getOutLinePaint();// 柱子边线颜色
		float outLineBasicStroke = chartStyle.getOutLineBasicStroke();// 柱子边线粗细
		double itemMargin = chartStyle.getItemMargin();// 柱子间的距离，像素为单位
		//int transparent = chartStyle.getTransparent();// 填充色透明度

		HashMap<String, Color> fieldNameAndColorHashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldColor().length; i++) {
			fieldNameAndColorHashMap.put(thematicRendition.getFieldName()[i],
					new Color(thematicRendition.getFieldColor()[i], false));
		}
		double realItemMargin = itemMargin
				* (thematicRendition.getDomainAxis().length - 1) * 1.0
				/ thematicRendition.getWidth();// 根据设置的柱子间的间距，求出所有间距之和占据绘图区域宽度的百分比
		double barWidth = (width * (1 - realItemMargin) / indicatorDatas.length);
		double maxBarWidth = 0.51 * width;
		if (barWidth > maxBarWidth) {
			barWidth = maxBarWidth;
		}
		maxValue = thematicRendition.getScales()[0] * width;
		int symbolWidth = width + 120;
		int symbolHeight = (int) (66 + height + 33 + 21 * fieldList.length + 10 + 4);

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
		g.drawString("图例", symbolWidth / 2 - 25, 30);
		g.setFont(new Font("黑体", Font.PLAIN, 20));
		if ((fieldUnits[0] != null) && (!(fieldUnits[0].length() == 0))) {
			g
					.drawString("单位：（" + fieldUnits[0] + "）",
							symbolWidth / 2 - 60, 55);
		}

		// axis
		Line2D rangeAxis = new Line2D.Double(symbolWidth / 2 - width / 2 + 15,
				60, symbolWidth / 2 - width / 2 + 15, 60 + height + 5);
		Line2D domainAxis = new Line2D.Double(symbolWidth / 2 - width / 2 + 15,
				60 + height + 5, symbolWidth / 2 - width / 2 + 15 + width,
				60 + height + 5);

		g.draw(domainAxis);
		g.draw(rangeAxis);

		if (barWidth > maxBarWidth) {
			barWidth = maxBarWidth;
		}

		// 刻度
		Font font = new Font("宋体", Font.PLAIN, 10);
		g.setFont(font);
		g.setColor(Color.BLACK);
		for (int i = 0; i < thematicRendition.getFieldName().length + 1; i++) {
			Line2D line2dRange = new Line2D.Double(domainAxis.getX1() - 2,
					domainAxis.getY2() - height
							/ thematicRendition.getFieldName().length * i,
					domainAxis.getX1(), domainAxis.getY2() - height
							/ thematicRendition.getFieldName().length * i);
			g.draw(line2dRange);
			FontMetrics fm = g.getFontMetrics();
			NumberFormat ddf1 = NumberFormat.getNumberInstance();
			if (maxValue < 10) {
				ddf1.setMaximumFractionDigits(2);
			} else {
				ddf1.setMaximumFractionDigits(0);
			}

			String tempString = ddf1.format(maxValue
					/ thematicRendition.getFieldName().length * i);
			Rectangle2D rec = fm.getStringBounds(tempString, g);

			g.drawString(tempString, (int) (domainAxis.getX1() - 2
					- rec.getWidth() - 5), (int) (domainAxis.getY2() - height
					/ thematicRendition.getFieldName().length * i));
		}
		for (int i = 0; i < thematicRendition.getDomainAxis().length; i++) {
			Line2D line2dDomain;
			if (barWidth < width * 0.5) {
				line2dDomain = new Line2D.Double(domainAxis.getX1() + barWidth
						/ 2 + (barWidth + itemMargin) * i, domainAxis.getY2(),
						domainAxis.getX1() + barWidth / 2
								+ (barWidth + itemMargin) * i, domainAxis
								.getY2() + 2);
			} else {
				line2dDomain = new Line2D.Double(
						domainAxis.getX1() + width / 2, domainAxis.getY2(),
						domainAxis.getX1() + width / 2, domainAxis.getY2() + 2);

			}
			g.draw(line2dDomain);
			g.drawString(thematicRendition.getDomainAxis()[i],
					(int) line2dDomain.getX1() - 10,
					(int) line2dDomain.getY2() + 2 + 10);
		}

		// 图
		double x = domainAxis.getX1() + width * 1.0 / 2;
		double y = domainAxis.getY2();

		int yOffSet = (int) (barWidth / 4);
		//int xOffSet = (int) (barWidth / 4);

		for (int i = 0; i < thematicRendition.getDomainAxis().length; i++) {
			if (indicatorDatas[i] != null) {
				double y0 = y;
				double x0 = 0;
				if (barWidth < width * 0.5) {
					x0 = x - thematicRendition.getWidth() / 2
							+ (barWidth + itemMargin) * i;
				} else {
					x0 = x - barWidth / 2;
				}
				double totalBarHeight = 0;
				double totalValues = 0;
				for (int j = 0; j < indicatorDatas[0].getNames().length; j++) {

					double barHeight = thematicRendition.getHeight()
					* 1.0/thematicRendition.getFieldName().length;

					totalBarHeight = totalBarHeight + barHeight;
					totalValues = totalValues
							+ indicatorDatas[i].getValues()[j];

//					GradientPaint gradientPaint = new GradientPaint((float) x0,
//							(float) (y0), fieldNameAndColorHashMap
//									.get(thematicRendition.getFieldName()[j]),
//							(float) (x0 + barWidth / 2), (float) (y0),
//							new Color(255, 255, 255, 255), true);

					int[] xpointsDown = { (int) x0, (int) x0,
							(int) (x0 + barWidth), (int) (x0 + barWidth) };
					int[] ypointsDown = { (int) y0, (int) y0 - (int) barHeight,
							(int) y0 - (int) barHeight, (int) y0 };

					GeneralPath polygon = new GeneralPath();

					polygon.moveTo((float) xpointsDown[1],
							(float) ypointsDown[1]);
					polygon.lineTo((float) xpointsDown[0],
							(float) ypointsDown[0]);
					Arc2D arcDown = new Arc2D.Double(x0,
							y0 - yOffSet * 1.0 / 2, barWidth, yOffSet, 180,
							180, Arc2D.OPEN);
					polygon.append(arcDown, true);
					polygon.lineTo((float) xpointsDown[2],
							(float) ypointsDown[2]);
					Arc2D arcUp = new Arc2D.Double(x0, y0 - barHeight - yOffSet
							* 1.0 / 2, barWidth, yOffSet, 0, 180, Arc2D.OPEN);
					polygon.append(arcUp, true);
					polygon.closePath();
					Shape top = new Ellipse2D.Double(x0, y0 - barHeight
							- yOffSet * 1.0 / 2, barWidth, yOffSet);
//					g.setPaint(gradientPaint);
					g.setColor(fieldNameAndColorHashMap.get(thematicRendition.getFieldName()[j]));
					g.fill(polygon);
					g.setColor(fieldNameAndColorHashMap.get(
							thematicRendition.getFieldName()[j]).darker());
					g.fill(top);

					if (drawBarOutline) {

						g.setColor(new Color(outLinePaint, false));
						BasicStroke basicStroke = new BasicStroke(
								outLineBasicStroke);
						g.setStroke(basicStroke);
						g.draw(polygon);
						g.draw(top);
					}
					y0 = y0 - barHeight;

				}
			} else {
				System.out.println("绘制专题符号图层时发现专题数据中区域数据不完整！");

			}

		}

		// 图例
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D rec = fm.getStringBounds(fieldList[0], g);
		Font font2 = new Font("宋体", Font.PLAIN, 15);
		int xx = symbolWidth / 2 - 6 - (int) (rec.getWidth() / 2);
		for (int i = 0; i < fieldList.length; i++) {

			Rectangle2D.Double fieldRect = new Rectangle2D.Double(xx,
					domainAxis.getY1() + 4 + 10 + 5 + 15 - 7 + 21 * i, 7, 7);
			g.setColor(fieldNameAndColorHashMap.get(fieldList[i]));
			g.fill(fieldRect);
			g.setColor(Color.BLACK);
			g.setFont(font2);
			g.drawString(fieldList[i], xx + 13, (int) (domainAxis.getY1() + 4
					+ 10 + 5 + 15 + 21 * i));
		}

		return image;

	}
}
