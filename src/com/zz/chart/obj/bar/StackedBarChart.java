package com.zz.chart.obj.bar;

import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.render.bar.StackedBarChartRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;


public class StackedBarChart extends BarChart {
	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		// 消除线条锯齿
		y = y+height/2;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		double maxValue = maxValues[0];
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
//		double realItemMargin = itemMargin * (thematicRendition.getDomainAxis().length - 1) * 1.0
//				/ thematicRendition.getWidth();// 根据设置的柱子间的间距，求出所有间距之和占据绘图区域宽度的百分比
//		double barWidth = (symbolEnvlope.getWidth() * (1 - realItemMargin) / indicatorDatas.length);
		double realItemMargin = itemMargin/minimumBarLength*(thematicRendition.getDomainAxis().length - 1);
		double barWidth = width/(thematicRendition.getDomainAxis().length+realItemMargin);
		       itemMargin = itemMargin/minimumBarLength*barWidth;
		
		maxValue = thematicRendition.getScales()[0] * width;

		HashMap<String, Color> fieldNameAndColorHashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			fieldNameAndColorHashMap.put(thematicRendition.getFieldName()[i], new Color(
					thematicRendition.getFieldColor()[i], false));
		}
		
		if (barWidth < symbolEnvlope.getWidth() * 0.5) {
			for (int i = 0; i < thematicRendition.getDomainAxis().length; i++) {
				if (indicatorDatas[i] != null) {
					double y0 = y;
					double x0 = x - thematicRendition.getWidth() / 2
					+ (barWidth + itemMargin) * i;
					double totalBarHeight = 0;
					double totalValues = 0;
					for (int j = 0; j < indicatorDatas[0].getNames().length; j++) {
						
						y0 = y0 - thematicRendition.getHeight()
								* indicatorDatas[i].getValues()[j] / maxValue;
						double barHeight = thematicRendition.getHeight()
						* indicatorDatas[i].getValues()[j] / maxValue;
						totalValues = totalValues + indicatorDatas[i].getValues()[j];
						totalBarHeight = totalBarHeight + barHeight;

						Rectangle2D.Double rectangleDouble = new Rectangle2D.Double(
								x0, y0,
								barWidth,barHeight );
						
						GradientPaint gradientPaint = new GradientPaint((float) x0,
								(float) (y0 + barHeight/2), fieldNameAndColorHashMap
										.get(thematicRendition.getFieldName()[j]),
								(float) (x0 + barWidth), (float) (y0+ barHeight),
								new Color(255, 255, 255), true);
						if(GradientPaint){
							g.setPaint(gradientPaint);
						}else {
							g.setColor(fieldNameAndColorHashMap.get(thematicRendition.getFieldName()[j]));	
						}
		
						g.fill(rectangleDouble);
						
						if (drawBarOutline) {
							g.setColor(new Color(outLinePaint, false));
							BasicStroke basicStroke = new BasicStroke(
									outLineBasicStroke);
							g.setStroke(basicStroke);
							g.draw(rectangleDouble);
						}
					}
					if (isLable) {
						Font font = new Font(itemLabelFontName, Font.PLAIN,
								itemLabelFontSize);
						g.setColor(new Color(itemLabelPaint, false));
						g.setFont(font);
						g.drawString(Float.toString((float) totalValues), (int)x0,
								(int) (y - totalBarHeight));
					}
				}else {
					System.out.println("绘制专题符号图层时发现专题数据中区域数据不完整！");
					
				}
				

			}
		} else {
			if (indicatorDatas[0] != null) {
				// 说明只有一个指标,因为柱子的最大宽度为51%，当指标大于1时宽度必然小于51%
				double y0 = y;
				double x0 = x - barWidth / 2;
				double totalBarHeight = 0;
				double totalValues = 0;
				for (int j = 0; j < indicatorDatas[0].getNames().length; j++) {
					y0 = y0 - thematicRendition.getHeight()
							* indicatorDatas[0].getValues()[j] / maxValue;
					double barHeight = thematicRendition.getHeight()
					* indicatorDatas[0].getValues()[j] / maxValue;
					
					totalValues = totalValues + indicatorDatas[0].getValues()[j];
					totalBarHeight = totalBarHeight + barHeight;

					Rectangle2D.Double rectangleDouble = new Rectangle2D.Double(
							x0, y0,
							barWidth, barHeight);
					
					GradientPaint gradientPaint = new GradientPaint((float) x0,
							(float) (y0 + barHeight/2), fieldNameAndColorHashMap
									.get(thematicRendition.getFieldName()[j]),
							(float) (x0 + barWidth), (float) (y0 +barHeight),
							new Color(255, 255, 255), true);
					if(GradientPaint){
						g.setPaint(gradientPaint);
					}else {
						g.setColor(fieldNameAndColorHashMap.get(thematicRendition.getFieldName()[j]));	
					}
					g.fill(rectangleDouble);

					if (drawBarOutline) {
						g.setColor(new Color(outLinePaint, false));
						BasicStroke basicStroke = new BasicStroke(
								outLineBasicStroke);
						g.setStroke(basicStroke);
						g.draw(rectangleDouble);
					}
					
				}
				if (isLable) {
					Font font = new Font(itemLabelFontName, Font.PLAIN,
							itemLabelFontSize);
					g.setColor(new Color(itemLabelPaint, false));
					g.setFont(font);
					g.drawString(Float.toString((float) totalValues), (int)x0,
							(int) (y - totalBarHeight));
				}

				
			}else {
				System.out.println("获取专题符号图层热区时发现专题数据中区域数据不完整！");
				
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

		double[] values;// 存放单变量多指标的值，例如存放2012年男女指标的人口数

		// 因为层叠柱状图只适合单变量多指标这样的数据结构，其余的不适用

		if (maxValue > (averageValue * 4)) {
			maxValue = averageValue * 4 * (1 + 1.0/9);
		}else {
			maxValue = maxValue * (1 + 1.0/9);
		}
		for (int i = 0; i < indicatorDatas.length; i++) {
			values = indicatorDatas[i].getValues();

			if (null == values) {
				System.out.println("区域数据为空！");
				return null;
			}
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
				categoryDataset.addValue(thematicRendition.getScales()[0] * width
						/ thematicRendition.getFieldName().length,
						seriesList[i], String.valueOf(domainAxisList[j])
								+ domainAxisUnit);
			}
		}
		// 获取符号绘制样式
		//int transparent = chartStyle.getTransparent();// 填充色透明度

		HashMap<String, Color> hashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			hashMap.put(thematicRendition.getFieldName()[i], new Color(
					thematicRendition.getFieldColor()[i], false));
		}

		JFreeChart barLegendChart = this.setStackedBarLegend(width,chartStyle,categoryDataset,
				thematicRendition, maxValue, indicatorDatas, "", "",
				"", hashMap);
		barLegendChart.setBackgroundPaint(null);
		BufferedImage barLegendImg = barLegendChart
				.createBufferedImage((int) (120 + width),
						(int) (height-40),BufferedImage.TYPE_INT_ARGB, null);

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

	public JFreeChart setStackedBarLegend(int width,ChartStyle chartStyle,
			DefaultCategoryDataset categoryDataset2,
			ChartDataPara thematicRendition, double maxValue,
			IndicatorData[] indicatorDatas, String titleName,
			String xUnit, String yUnit, HashMap<String, Color> hashMap) {
		CategoryDataset categoryDataset = (CategoryDataset) categoryDataset2;
		JFreeChart barChart = ChartFactory.createStackedBarChart(titleName, // 图表标题
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
		TextTitle title = barChart.getTitle();
		title.setFont(new Font("黑体", Font.PLAIN, 15));

		CategoryPlot plot = barChart.getCategoryPlot();

		plot.setBackgroundPaint(null);
		plot.setOutlineVisible(false);
		plot.setDomainGridlinesVisible(true);
		plot.setAxisOffset(RectangleInsets.ZERO_INSETS);

		// 定义是否绘制轮廓线
		StackedBarChartRenderer renderer = new StackedBarChartRenderer();
		// 获取符号绘制样式
		@SuppressWarnings("unused")
		String chartID = chartStyle.getChartID();// 符号ID
		boolean drawBarOutline = chartStyle.isDrawBarOutLine();// 是否绘制柱子边线
		int outLinePaint = chartStyle.getOutLinePaint();// 柱子边线颜色
		float outLineBasicStroke = chartStyle.getOutLineBasicStroke();// 柱子边线粗细
		renderer.setDrawBarOutline(drawBarOutline);
		if (drawBarOutline) {
			renderer.setBaseOutlinePaint(new Color(outLinePaint, false));// 设置柱子边线颜色
			BasicStroke basicStroke = new BasicStroke(outLineBasicStroke);
			renderer.setBaseOutlineStroke(basicStroke);// 设置柱子边线宽度
		}
		// 设置bar间距
		renderer.setMaximumBarWidth(0.2);
		renderer.setItemMargin(0);
		renderer.setShadowVisible(false);

		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			renderer.setSeriesPaint(i, hashMap.get(thematicRendition
					.getFieldName()[i]));
		}
		plot.setRenderer(renderer);

		// ----设置横轴(类别)标题文字的字体及旋转方向
		CategoryAxis domainAxis = plot.getDomainAxis();
		// 设置横轴标题文字字体
		domainAxis.setLabelFont(new Font("宋体", Font.PLAIN, 10));
		// 设置横坐标字体
		domainAxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 10));
		domainAxis.setLowerMargin(0);

		// ----设置纵轴（值）标题文字的字体及旋转方向
		ValueAxis rangeAxis = plot.getRangeAxis();
		// 设置横轴标题文字字体
		rangeAxis.setLabelFont(new Font("宋体", Font.BOLD, 10));
		// 设置横坐标字体
		rangeAxis.setTickLabelFont(new Font("黑体", Font.PLAIN, 10));
		rangeAxis.setRange(0, thematicRendition.getScales()[0] *width);
		return barChart;
	}

	public HashMap<String, Shape> generateHotArea(double x, double y,
			int width, int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		double maxValue = maxValues[0];
//		double averageValue = averageValues[0];
		Rectangle2D.Double symbolEnvlope = new Rectangle2D.Double(x
				- thematicRendition.getWidth() / 2, y
				- thematicRendition.getHeight(), width, height);// 设置绘图区域
		
//		if (maxValue > (averageValue * 4)) {
//			maxValue = averageValue * 4 * (1 + 1.0/9);
//		}else {
//			maxValue = maxValue * (1 + 1.0/9);
//		}
		int fieldNum = thematicRendition.getFieldName().length;
		double itemMargin = chartStyle.getItemMargin();
		if (itemMargin < 0 || itemMargin * fieldNum > width) {
			System.out.print("柱子间距设置有误！");
		}
		double realItemMargin = itemMargin * (thematicRendition.getDomainAxis().length - 1) * 1.0
				/ thematicRendition.getWidth();// 根据设置的柱子间的间距，求出所有间距之和占据绘图区域宽度的百分比
		double barWidth = (symbolEnvlope.getWidth() * (1 - realItemMargin) / indicatorDatas.length);
		double maxBarWidth = 0.51 * width;
		if (barWidth > maxBarWidth) {
			barWidth = maxBarWidth;
		}
		maxValue = thematicRendition.getScales()[0] * width;
		HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
		if (barWidth < symbolEnvlope.getWidth() * 0.5) {
			for (int i = 0; i < thematicRendition.getDomainAxis().length; i++) {
				if (indicatorDatas[i] != null) {
					double y0 = y;
					for (int j = 0; j < indicatorDatas[0].getNames().length; j++) {
						y0 = y0 - thematicRendition.getHeight()
								* indicatorDatas[i].getValues()[j] / maxValue;

						Rectangle2D.Double rectangleDouble = new Rectangle2D.Double(
								x - thematicRendition.getWidth() / 2
										+ (barWidth + itemMargin) * i, y0,
								barWidth, thematicRendition.getHeight()
										* indicatorDatas[i].getValues()[j]
										/ maxValue);
						String text = "";
						
						if ((thematicRendition.getDomainAxis()[i] == null)||(thematicRendition.getDomainAxis()[i].length()==0)) {
							text = "(" + indicatorDatas[i].getNames()[j] + ")="
							         + indicatorDatas[i].getValues()[j];
						}else {
							text = "(" + thematicRendition.getDomainAxis()[i]
							          + "," + indicatorDatas[i].getNames()[j] + ")="
							         + indicatorDatas[i].getValues()[j];
						}
						hotAreahHashMap.put(text, rectangleDouble);
					}
				}else {
					System.out.println("获取专题符号图层热区时发现专题数据中区域数据不完整！");
					return null;
				}
				

			}
			return hotAreahHashMap;
		} else {
			if (indicatorDatas[0] != null) {
				// 说明只有一个指标,因为柱子的最大宽度为51%，当指标大于1时宽度必然小于51%
				double y0 = y;
				for (int j = 0; j < indicatorDatas[0].getNames().length; j++) {
					y0 = y0 - thematicRendition.getHeight()
							* indicatorDatas[0].getValues()[j] / maxValue;

					Rectangle2D.Double rectangleDouble = new Rectangle2D.Double(x
							- barWidth / 2, y0, barWidth, thematicRendition
							.getHeight()
							* indicatorDatas[0].getValues()[j] / maxValue);
					String text = "";
					if ((thematicRendition.getDomainAxis()[0] == null)||(thematicRendition.getDomainAxis()[0].length()==0)) {
						text = "(" + indicatorDatas[0].getNames()[j] + ")="
						+ indicatorDatas[0].getValues()[j];
					}else {
						text = "(" + thematicRendition.getDomainAxis()[0] + ","
						+ indicatorDatas[0].getNames()[j] + ")="
						+ indicatorDatas[0].getValues()[j];
					}
					
					hotAreahHashMap.put(text, rectangleDouble);
				}

				return hotAreahHashMap;
			}else {
				System.out.println("获取专题符号图层热区时发现专题数据中区域数据不完整！");
				return null;
			}
			
		}

	}

}
