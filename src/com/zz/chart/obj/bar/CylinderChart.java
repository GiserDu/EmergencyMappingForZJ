package com.zz.chart.obj.bar;

import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.render.bar.CylinderChartRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.Size2D;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class CylinderChart extends BarChart3D {
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
		// 因为普通柱状图只适合单变量多指标这样的数据结构，其余的不适用
		values = indicatorDatas[0].getValues();// 因为普通柱状图只适合单变量多指标这样的数据结构，其余的不适用
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
		double minimumBarLength = chartStyle.getMinimumBarLength();// 百分比
		double itemMargin = chartStyle.getItemMargin();// 柱子间的距离，像素为单位
		boolean isLable = chartStyle.isLable();// 是否绘制标签
		String itemLabelFontName = chartStyle.getItemLabelFontName();// 标签字体名称
		int itemLabelFontSize = chartStyle.getItemLabelFontSize();// 标签字体大小
		int itemLabelPaint = chartStyle.getItemLabelPaint();// 标签颜色
		//int transparent = chartStyle.getTransparent();// 填充色透明度
		boolean GradientPaint = chartStyle.isGradientPaint();
		
		HashMap<String, Color> fieldNameAndColorHashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			fieldNameAndColorHashMap.put(thematicRendition.getFieldName()[i],
					new Color(thematicRendition.getFieldColor()[i], false));
		}


		// 处理极端数据
//		double realItemMargin = itemMargin * (values.length - 1) * 1.0 / width;// 根据设置的柱子间的间距，求出所有间距之和占据绘图区域宽度的百分比
//		double barWidth = (width * (1 - realItemMargin) / values.length);
		
		double realItemMargin = itemMargin/minimumBarLength*(values.length-1);
		double barWidth = width/(values.length+realItemMargin);
		       itemMargin = itemMargin/minimumBarLength*barWidth;
		if (barWidth > width * 0.51) {
			barWidth = width * 0.51;
		}
		int yOffSet = (int) (barWidth / 4);//水平方向时用
		//int xOffSet = (int) (barWidth / 4);//垂直方向用
		for (int i = 0; i < values.length; i++) {
			if (values[i] > (averageValue * 4)&&isCrack) {
				int barX0 = 0;
				if (barWidth < width * 0.51) {
					barX0 = (int) x - (int) (width / 2)
							+ (int) (barWidth + itemMargin) * i;
				} else {
					barX0 = (int) (x - width * 0.51 / 2);
				}
				int barHeight = (int) (0.8 * height);// 断裂前80%
				int innerHeight = (int) (0.02 * height);// 锯齿高度
				int innerWidth = (int) (barWidth / 3);// 锯齿宽度
				int barY0 = (int) y;

//				GradientPaint gradientPaint = new GradientPaint((float) barX0,
//						(float) (barY0), fieldNameAndColorHashMap
//								.get(thematicRendition.getFieldName()[i]),
//						(float) (barX0 + barWidth / 2), (float) (barY0),
//						new Color(255, 255, 255, 255), true);

				barHeight = (int) (0.8 * height);// 断裂前80%
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
				Arc2D arcDown = new Arc2D.Double(barX0, barY0 - yOffSet * 1.0
						/ 2, barWidth, yOffSet, 180, 180, Arc2D.OPEN);
				polygonDown.append(arcDown, true);
				polygonDown.lineTo((float) xpointsDown[4],
						(float) ypointsDown[4]);
				polygonDown.lineTo((float) xpointsDown[3],
						(float) ypointsDown[3]);
				polygonDown.lineTo((float) xpointsDown[2],
						(float) ypointsDown[2]);
				polygonDown.closePath();

				barHeight = barHeight + innerHeight;
				int[] xpointsUp = { barX0, barX0, barX0 + innerWidth,
						barX0 + 2 * innerWidth, barX0 + 3 * innerWidth,
						barX0 + 3 * innerWidth };
				int[] ypointsUp = { barY0 - (int) (0.9 * height),
						barY0 - barHeight, barY0 - barHeight + innerHeight,
						barY0 - barHeight, barY0 - barHeight + innerHeight,
						barY0 - (int) (0.9 * height) };

				polygonUp.moveTo((float) xpointsUp[1], (float) ypointsUp[1]);
				polygonUp.lineTo((float) xpointsUp[0], (float) ypointsUp[0]);
				Arc2D arcUp = new Arc2D.Double(barX0, barY0
						- (int) (0.9 * height) - yOffSet * 1.0 / 2, barWidth,
						yOffSet, 180, -180, Arc2D.OPEN);
				polygonUp.append(arcUp, true);
				polygonUp.lineTo((float) xpointsUp[4], (float) ypointsUp[4]);
				polygonUp.lineTo((float) xpointsUp[3], (float) ypointsUp[3]);
				polygonUp.lineTo((float) xpointsUp[2], (float) ypointsUp[2]);
				polygonUp.closePath();

				Shape top = new Ellipse2D.Double(barX0, barY0
						- (int) (0.9 * height) - yOffSet * 1.0 / 2, barWidth,
						yOffSet);
//						//设置填充颜色
				if(GradientPaint){
					 GradientPaint gradientPaint = new GradientPaint((float)
					 barX0,
					 (float) (barY0), fieldNameAndColorHashMap
					 .get(thematicRendition.getFieldName()[i]),
					 (float) (barX0 + barWidth / 2), (float) (barY0),
					 new Color(255, 255, 255), true);
					 g.setPaint(gradientPaint);
					}else {
						g.setColor(fieldNameAndColorHashMap.get(thematicRendition
								.getFieldName()[i]));
					}
					
				g.fill(polygonUp);
				g.fill(polygonDown);
				g.setColor(fieldNameAndColorHashMap.get(
						thematicRendition.getFieldName()[i]).darker());
				g.fill(top);
				// 断裂处理的必须标上标签
				Font font = new Font(itemLabelFontName, Font.PLAIN,
						itemLabelFontSize);
				g.setColor(new Color(itemLabelPaint, false));
				g.setFont(font);
				g.drawString(Float.toString((float) values[i]), (int) barX0,
						(int) barY0 - (int) (0.9 * height));

				if (drawBarOutline) {

					g.setColor(new Color(outLinePaint, false));
					BasicStroke basicStroke = new BasicStroke(
							outLineBasicStroke);
					g.setStroke(basicStroke);
					g.draw(polygonUp);
					g.draw(polygonDown);
					g.draw(top);
				}

			} else {
				int barX0 = 0;
				if (barWidth < width * 0.51) {
					barX0 = (int) x - (int) (width / 2)
							+ (int) (barWidth + itemMargin) * i;
				} else {
					barX0 = (int) (x - width * 0.51 / 2);
				}
				int barY0 = (int) y;
				double barHeight = values[i] / maxValue * height;
//				if (barHeight < minimumBarLength) {
//					barHeight = minimumBarLength;
//				}

//				GradientPaint gradientPaint = new GradientPaint((float) barX0,
//						(float) (barY0), fieldNameAndColorHashMap
//								.get(thematicRendition.getFieldName()[i]),
//						(float) (barX0 + barWidth / 2), (float) (barY0),
//						new Color(255, 255, 255, 255), true);

				int[] xpointsDown = { barX0, barX0, (int) (barX0 + barWidth),
						(int) (barX0 + barWidth) };
				int[] ypointsDown = { barY0, barY0 - (int) barHeight,
						barY0 - (int) barHeight, barY0 };

				GeneralPath polygon = new GeneralPath();

				polygon.moveTo((float) xpointsDown[1], (float) ypointsDown[1]);
				polygon.lineTo((float) xpointsDown[0], (float) ypointsDown[0]);
				Arc2D arcDown = new Arc2D.Double(barX0, barY0 - yOffSet * 1.0
						/ 2, barWidth, yOffSet, 180, 180, Arc2D.OPEN);
				polygon.append(arcDown, true);
				polygon.lineTo((float) xpointsDown[2], (float) ypointsDown[2]);
				Arc2D arcUp = new Arc2D.Double(barX0, barY0 - barHeight
						- yOffSet * 1.0 / 2, barWidth, yOffSet, 0, 180,
						Arc2D.OPEN);
				polygon.append(arcUp, true);
				polygon.closePath();
				Shape top = new Ellipse2D.Double(barX0, barY0 - barHeight
						- yOffSet * 1.0 / 2, barWidth, yOffSet);
				//设置填充颜色
				if(GradientPaint){
				 GradientPaint gradientPaint = new GradientPaint((float)
				 barX0,
				 (float) (barY0), fieldNameAndColorHashMap
				 .get(thematicRendition.getFieldName()[i]),
				 (float) (barX0 + barWidth / 2), (float) (barY0),
				 new Color(255, 255, 255), true);
				 g.setPaint(gradientPaint);
				}else {
					g.setColor(fieldNameAndColorHashMap.get(thematicRendition
							.getFieldName()[i]));
				}
				
				g.fill(polygon);
				g.setColor(fieldNameAndColorHashMap.get(
						thematicRendition.getFieldName()[i]).darker());
				g.fill(top);

				if (drawBarOutline) {

					g.setColor(new Color(outLinePaint, false));
					BasicStroke basicStroke = new BasicStroke(
							outLineBasicStroke);
					g.setStroke(basicStroke);
					g.draw(polygon);
					g.draw(top);
				}
				if (isLable) {
					Font font = new Font(itemLabelFontName, Font.PLAIN,
							itemLabelFontSize);
					g.setColor(new Color(itemLabelPaint, false));
					g.setFont(font);
					g.drawString(Float.toString((float) values[i]), barX0,
							(int) (barY0 - barHeight));
				}
			}
		}

	}

	public BufferedImage drawLegend(int width, int height,
			ChartDataPara thematicRendition, ChartStyle chartStyle,
			double[] maxValues, double[] minValues,double[] averageValues,
			IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		double maxValue = maxValues[0];
		double averageValue = averageValues[0];
		boolean isCrack = chartStyle.isIsCrack();
//		boolean isFracture = chartStyle.isFracture();
		if (maxValue > (averageValue * 4)&&isCrack) {
			maxValue = averageValue * 4 * (1 + 1.0 / 9);
		} else {
			maxValue = maxValue * (1 + 1.0 / 9);
		}


		String[] domainAxisList = new String[indicatorDatas.length];
		for (int i = 0; i < domainAxisList.length; i++) {
			domainAxisList[i] = indicatorDatas[i].getDomainAxis();
		}
		String domainAxisUnit = thematicRendition.getDomainAxisUnit();
		String[] fieldUnits = thematicRendition.getFieldUnits();

		DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
		String[] seriesList = thematicRendition.getFieldName();
		for (int i = 0; i < seriesList.length; i++) {
			for (int j = 0; j < domainAxisList.length; j++) {
				categoryDataset.addValue(maxValue, seriesList[i], String
						.valueOf(domainAxisList[j])
						+ domainAxisUnit);
			}
		}
		// 获取符号绘制样式
		//int transparent = chartStyle.getTransparent();// 填充色透明度

		HashMap<String, Color> hashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldColor().length; i++) {
			hashMap.put(thematicRendition.getFieldName()[i], new Color(
					thematicRendition.getFieldColor()[i], false));
		}

		JFreeChart barLegendChart = this.setCylinderLegend(width,chartStyle,categoryDataset,
				thematicRendition, maxValue,indicatorDatas, "", "",
				"", hashMap);
		barLegendChart.setBackgroundPaint(null);
		BufferedImage barLegendImg = barLegendChart
				.createBufferedImage((int) (100 + width),
						(int) (height -40),BufferedImage.TYPE_INT_ARGB, null);

		// 绘制标题，构成，留空，后面完
		TextTitle textTitle = barLegendChart.getTitle();
		textTitle.setText("统计图图例");
		textTitle.setFont(new Font("黑体", Font.PLAIN, 20));
		textTitle.setMargin(1, 1, 1, 1);
		BufferedImage imgTitle = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2Title = imgTitle.createGraphics();
		Size2D sizeTitle = textTitle.arrange(g2Title);
		g2Title.dispose();
		int wTitle = (int) Math.rint(sizeTitle.width);
		int hTitle = (int) Math.rint(sizeTitle.height);

		// 绘制单位
		TextTitle unitTitle = new TextTitle();
		if ((fieldUnits[0]!=null)&&(!(fieldUnits[0].length()==0))) {
			unitTitle.setText("单位：" + "（" + fieldUnits[0] + "）");
		}else {
			unitTitle.setText("");
		}
		unitTitle.setFont(new Font("黑体", Font.PLAIN, 16));
		unitTitle.setMargin(1, 1, 1, 1);
		BufferedImage imgUnit = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2Unit = imgUnit.createGraphics();
		Size2D sizeUnit = unitTitle.arrange(g2Unit);
		g2Unit.dispose();
		int wUnite = (int) Math.rint(sizeUnit.width);
		int hUnit = (int) Math.rint(sizeUnit.height);

		// 绘制图例
		LegendTitle legend = new LegendTitle(barLegendChart.getPlot());
		legend.setItemFont(new Font("黑体", Font.PLAIN, 15));
		legend.setMargin(1, 1, 1, 1);
		legend.setPosition(RectangleEdge.RIGHT);
		legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = img.createGraphics();
		Size2D size = legend.arrange(g2);
		g2.dispose();
		int w = (int) Math.rint(size.width);
		int h = (int) Math.rint(size.height);

		int maxW1 = Math.max(wTitle, wUnite);
		int maxW2 = Math.max(wTitle, w);
		int maxW3 = Math.max(maxW1, maxW2);
		int maxW = Math.max(maxW3, barLegendImg.getWidth());

		int maxH = hTitle + hUnit + h + barLegendImg.getHeight();

		BufferedImage image = new BufferedImage(maxW, maxH,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.createGraphics();
//		g2d.setBackground(Color.WHITE);
//		g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		textTitle.draw(g2d, new Rectangle2D.Double(
				(image.getWidth() - wTitle) / 2, 0, wTitle, hTitle));
		unitTitle.draw(g2d, new Rectangle2D.Double(
				(image.getWidth() - wUnite) / 2, hTitle, wUnite + 50, hUnit));
		// 绘制bar图
		g2d.drawImage(barLegendImg,
				(image.getWidth() - barLegendImg.getWidth()) / 2, hTitle
						+ hUnit, barLegendImg.getWidth(), barLegendImg
						.getHeight(), null);
		legend.draw(g2d, new Rectangle2D.Double((image.getWidth() - w) / 2,
				barLegendImg.getHeight() + hTitle + hUnit, w, h));

//		g2d.drawRect(0, 0, image.getWidth() - 1, image.getHeight() - 1);
		g2d.dispose();
		return image;
	}

	public JFreeChart setCylinderLegend(int width,ChartStyle chartStyle,
			DefaultCategoryDataset categoryDataset2,
			ChartDataPara thematicRendition, double maxValue,
			IndicatorData[] indicatorDatas, String titleName,
			String xUnit, String yUnit, HashMap<String, Color> hashMap) {
		CategoryDataset categoryDataset = (CategoryDataset) categoryDataset2;
		JFreeChart cylinderChart = ChartFactory.createBarChart(titleName, // 图表标题
				// chart
				// title
				xUnit, // 横坐标标题 domain axis label
				yUnit, // 纵坐标标题 range axis label
				categoryDataset, // 定义绘制数据 data
				PlotOrientation.VERTICAL, // 直方图的方向 orientation
				false, // 定义图表是否包含图例 include legend
				false, // 定义图表是否包含提示 tooltips?
				false // 定义图表是否包含URL URLs?
				);


		CategoryPlot plot = (CategoryPlot)cylinderChart.getPlot();
		plot.setBackgroundPaint(null);
		plot.setOutlineVisible(false);		
		plot.setDomainGridlinesVisible(false);
		plot.setRangeGridlinesVisible(false);
		plot.setAxisOffset(RectangleInsets.ZERO_INSETS);

		//ChartUtilities.applyCurrentTheme(cylinderChart);//会导致x轴与y轴断开
		// 获取符号绘制样式
		@SuppressWarnings("unused")
		String chartID = chartStyle.getChartID();// 符号ID
		boolean drawBarOutline = chartStyle.isDrawBarOutLine();// 是否绘制柱子边线
		int outLinePaint = chartStyle.getOutLinePaint();// 柱子边线颜色
		float outLineBasicStroke = chartStyle.getOutLineBasicStroke();// 柱子边线粗细
		double itemMargin = chartStyle.getItemMargin();// 柱子间的距离，像素为单位
		
		double realItemMargin = itemMargin * (indicatorDatas[0].getValues().length - 1) * 1.0 / width;// 根据设置的柱子间的间距，求出所有间距之和占据绘图区域宽度的百分比
		double barWidth = (width * (1 - realItemMargin) / indicatorDatas[0].getValues().length);
		if (barWidth > width * 0.51) {
			barWidth = width * 0.51;
		}
		int yOffSet = (int) (barWidth / 4);//水平方向时用
		int xOffSet = (int) (barWidth / 4);//垂直方向用

		CylinderChartRenderer renderer = new CylinderChartRenderer(xOffSet,yOffSet);

		cylinderChart.setBackgroundPaint(null);

		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			renderer.setSeriesPaint(i, hashMap.get(thematicRendition
					.getFieldName()[i]));
		}

		renderer.setGradientPaintTransformer(null);
		renderer.setDrawBarOutline(drawBarOutline);
		if (drawBarOutline) {
			renderer.setBaseOutlinePaint(new Color(outLinePaint, false));// 设置柱子边线颜色
			BasicStroke basicStroke = new BasicStroke(outLineBasicStroke);
			renderer.setBaseOutlineStroke(basicStroke);// 设置柱子边线宽度
		}
		renderer
				.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());

		// 设置bar间距,series间的距离；category间距离通过domain坐标轴来设置
		// 设置最大柱子宽度为plot的10%
		//renderer.setMaximumBarWidth(0.2);
		//renderer.setItemMargin(0);// 设置好最大宽度后，必须把这个值设为负数，才能抱着item间距很小

		plot.setRenderer(renderer);

		// ----设置横轴(类别)标题文字的字体及旋转方向
		CategoryAxis domainAxis = plot.getDomainAxis();
		// 设置横轴标题文字字体
		domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 15));
		// 设置横坐标字体
		domainAxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 15));
		domainAxis.setLowerMargin(0.05);
		domainAxis.setUpperMargin(0);

		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);

		// ----设置纵轴（值）标题文字的字体及旋转方向
		ValueAxis rangeAxis = plot.getRangeAxis();
		// 设置横轴标题文字字体
		rangeAxis.setLabelFont(new Font("宋体", Font.BOLD, 15));
		// 设置横坐标字体
		rangeAxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 15));
		rangeAxis.setRange(0, maxValue);
		rangeAxis.setLowerMargin(0);
		rangeAxis.setUpperMargin(0);
		return cylinderChart;
	}

}
