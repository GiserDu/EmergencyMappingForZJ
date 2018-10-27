package com.zz.chart.obj.bar;

import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;


public class PyramidChart extends BarChart {
	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,double[] averageValues, IndicatorData[] indicatorDatas) {

		// 消除线条锯齿
		y = y+height/2;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		double maxValue = maxValues[0];
		
		double value;
//		if (maxValue > (averageValue * 4)) {
//			maxValue = averageValue * 4 * (1 + 1.0/9);
//		}else {
//			maxValue = maxValue * (1 + 1.0/9);
//		}

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
		HashMap<String, Color> hashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			hashMap.put(thematicRendition.getFieldName()[i], new Color(
					thematicRendition.getFieldColor()[i], false));
		}
		
//		double barWidth = (height - itemMargin*(thematicRendition.getDomainAxis().length-1)) / thematicRendition.getDomainAxis().length;
		double maxBarWidth = 0.51*height;
		
		double realItemMargin = itemMargin/minimumBarLength*(thematicRendition.getDomainAxis().length-1);
		double barWidth = height/( thematicRendition.getDomainAxis().length+realItemMargin);
		       itemMargin = itemMargin/minimumBarLength*barWidth;
		if (barWidth > maxBarWidth) {
			barWidth = maxBarWidth;
		}
		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			for (int j = 0; j < indicatorDatas.length; j++) {
				if (indicatorDatas[j] == null) {
					System.out.println("区域数据为空！");
					return;
				}else {
					if (i == 0) {
						
						value = indicatorDatas[j].getValues()[i];
						
						double barY0 = 0;
						if (barWidth < maxBarWidth) {
							barY0 = y - barWidth - j*(barWidth + itemMargin);
						}else {
							barY0 = y- height*1.0/2-barWidth/2;//一根柱子时
						}
						double barHeight = value / maxValue * width/2;
						double barX0 = x - barHeight;
						
						Rectangle2D.Double rectangleLeft = new Rectangle2D.Double(barX0,barY0,barHeight,barWidth);
						GradientPaint gradientPaint = new GradientPaint((float) barX0,
								(float) (barY0), hashMap
										.get(thematicRendition.getFieldName()[i]),
								(float) (barX0 ), (float) (barY0 - barWidth / 2),
								new Color(255, 255, 255), true);
						if(GradientPaint){
						   g.setPaint(gradientPaint);
						}else{
						   g.setColor(hashMap.get(thematicRendition.getFieldName()[i]));
						}
						g.fill(rectangleLeft);
						if (drawBarOutline) {
							g.setColor(new Color(outLinePaint, false));
							BasicStroke basicStroke = new BasicStroke(
									outLineBasicStroke);
							g.setStroke(basicStroke);
							g.draw(rectangleLeft);
						}
						if (isLable) {
							Font font = new Font(itemLabelFontName, Font.PLAIN,
									itemLabelFontSize);
							g.setColor(new Color(itemLabelPaint, false));
							g.setFont(font);
							FontMetrics fm = g.getFontMetrics();  
				            Rectangle2D rec=fm.getStringBounds(Double.toString(value), g); 
				            int labelLength = (int)rec.getWidth();
							g.drawString(Float.toString((float) value), (int)barX0 - labelLength,
									(int) (barY0 + barWidth/2));
						}
					}else {
						value = indicatorDatas[j].getValues()[i];
						
						double barY0 = 0;
						if (barWidth < maxBarWidth) {
							barY0 = y - barWidth - j*(barWidth + itemMargin);
						}else {
							barY0 = y- height*1.0/2-barWidth/2;//一根柱子时
						}
						double barHeight = value / maxValue * width/2;
						double barX0 = x;
						
						Rectangle2D.Double rectangleRight = new Rectangle2D.Double(barX0,barY0,barHeight,barWidth);
						GradientPaint gradientPaint = new GradientPaint((float) barX0,
								(float) (barY0), hashMap
										.get(thematicRendition.getFieldName()[i]),
								(float) (barX0 ), (float) (barY0 - barWidth / 2),
								new Color(255, 255, 255), false);
						if(GradientPaint){
							   g.setPaint(gradientPaint);
							}else{
							   g.setColor(hashMap.get(thematicRendition.getFieldName()[i]));
							}
						g.fill(rectangleRight);
						if (drawBarOutline) {
							g.setColor(new Color(outLinePaint, false));
							BasicStroke basicStroke = new BasicStroke(
									outLineBasicStroke);
							g.setStroke(basicStroke);
							g.draw(rectangleRight);
						}
						if (isLable) {
							Font font = new Font(itemLabelFontName, Font.PLAIN,
									itemLabelFontSize);
							g.setColor(new Color(itemLabelPaint, false));
							g.setFont(font);
							g.drawString(Float.toString((float) value), (int)(barX0 + barHeight),
									(int) (barY0 + barWidth/2));
						}
					}
				}
			}
		}
	}

	public BufferedImage drawLegend(int width, int height,
			ChartDataPara thematicRendition, ChartStyle chartStyle,
			double[] maxValues, double[] minValues,double[] averageValues,
			IndicatorData[] indicatorDatas) {
		
		double maxValue = maxValues[0];
		
		double value;
		String[] fieldUnits = thematicRendition.getFieldUnits();
		//String domainAxisUnit = thematicRendition.getDomainAxisUnit();

		String[] fieldList = thematicRendition.getFieldName();

		// 获取符号绘制样式
		@SuppressWarnings("unused")
		String chartID = chartStyle.getChartID();// 符号ID
		boolean drawBarOutline = chartStyle.isDrawBarOutLine();// 是否绘制柱子边线
		int outLinePaint = chartStyle.getOutLinePaint();// 柱子边线颜色
		float outLineBasicStroke = chartStyle.getOutLineBasicStroke();// 柱子边线粗细
		double itemMargin = chartStyle.getItemMargin();// 柱子间的距离，像素为单位
		//int transparent = chartStyle.getTransparent();// 填充色透明度
		
		HashMap<String, Color> hashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldColor().length; i++) {
			hashMap.put(thematicRendition.getFieldName()[i], new Color(
					thematicRendition.getFieldColor()[i], false));
		}
		double barWidth = (height - itemMargin*(thematicRendition.getDomainAxis().length-1)) / thematicRendition.getDomainAxis().length;
		double maxBarWidth = 0.51*height;
		if (barWidth > maxBarWidth) {
			barWidth = maxBarWidth;
		}
		int symbolWidth = width + 130;
		int symbolHeight = (int) (60 + height + 30 + 21 * fieldList.length);

		BufferedImage image = new BufferedImage(symbolWidth, symbolHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
//		g.setBackground(Color.WHITE);
//		g.clearRect(0, 0, symbolWidth, symbolHeight);
		g.setColor(Color.BLACK);
		g.draw(new Rectangle(1, 1, symbolWidth - 2, symbolHeight - 2));

		g.setFont(new Font("黑体", Font.PLAIN, 18));
		g.drawString("统计图图例", symbolWidth /2 - 35, 30);
		g.setFont(new Font("黑体", Font.PLAIN, 16));
		if ((fieldUnits[0]!=null)&&(!(fieldUnits[0].length()==0))) {
			g.drawString("单位：（" + fieldUnits[0] + "）", symbolWidth /2 - 40, 55);
		}
			
		//axis
        Line2D rangeAxis = new Line2D.Double(symbolWidth /2 - width/2 + 15, 60,symbolWidth /2 - width/2 + 15, 60+height + 5);
        Line2D domainAxis = new Line2D.Double(symbolWidth /2 - width/2 + 15, 60 + height + 5, symbolWidth /2 - width/2 + 15 + width, 60+height + 5);

        
        g.draw(domainAxis);
        g.draw(rangeAxis);

		if (barWidth > maxBarWidth) {
			barWidth = maxBarWidth;
		}

		//刻度
		Font font = new Font("宋体", Font.PLAIN, 10);
		g.setFont(font);
		g.setColor(Color.BLACK);
		for (int i = 0; i < indicatorDatas.length; i++) {
			Line2D line2dRange = new Line2D.Double(domainAxis.getX1() - 2, domainAxis.getY2() - 5 - 0.5*barWidth - (barWidth + itemMargin)*i, domainAxis.getX1(), domainAxis.getY2() - 5 - 0.5*barWidth - (barWidth + itemMargin)*i);
			g.draw(line2dRange);
			FontMetrics fm = g.getFontMetrics();
	        Rectangle2D rec=fm.getStringBounds(thematicRendition.getDomainAxis()[i], g); 

			g.drawString(thematicRendition.getDomainAxis()[i], (int)(domainAxis.getX1() - 2 - rec.getWidth() - 5),  (int)(domainAxis.getY2() - 5 - barWidth*0.5 - (barWidth + itemMargin)*i + 5));
		}
		Line2D line2dDomain1 = new Line2D.Double(domainAxis.getX1(), domainAxis.getY2(), domainAxis.getX1(), domainAxis.getY2()+2);
		g.draw(line2dDomain1);
		g.drawString(Double.toString(maxValue), (int)line2dDomain1.getX1() - 10, (int)line2dDomain1.getY2() + 2 + 10);
		Line2D line2dDomain2 = new Line2D.Double(domainAxis.getX1() +width*0.5, domainAxis.getY2(), domainAxis.getX1() +width*0.5, domainAxis.getY2()+2);
		g.draw(line2dDomain2);
		g.drawString(Double.toString(0), (int)line2dDomain2.getX1() - 5, (int)line2dDomain2.getY2() + 2 + 10);
		Line2D line2dDomain3 = new Line2D.Double(domainAxis.getX1() +width, domainAxis.getY2(), domainAxis.getX1() +width, domainAxis.getY2()+2);
		g.draw(line2dDomain3);
		g.drawString(Double.toString(maxValue), (int)line2dDomain3.getX1() - 10, (int)line2dDomain3.getY2() + 2 + 10);
		
		//图
		double x = domainAxis.getX1() + width*1.0/2;
		double y = domainAxis.getY2() - 5;
		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			for (int j = 0; j < indicatorDatas.length; j++) {
				if (indicatorDatas[j] == null) {
					System.out.println("区域数据为空！");
					return null;
				}else {
						value = maxValue;
						
						double barY0 = 0;
						if (barWidth < maxBarWidth) {
							barY0 = y - barWidth - j*(barWidth + itemMargin);
						}else {
							barY0 = y- height*1.0/2-barWidth/2;//一根柱子时
						}
						double barHeight = value / maxValue * width/2;
						double barX0 = 0;
						if (i == 1) {
							barX0 = x;
						}else {
							barX0 = x - barHeight;
						}
						
						Rectangle2D.Double rectangleLeft = new Rectangle2D.Double(barX0,barY0,barHeight,barWidth);
//						GradientPaint gradientPaint = new GradientPaint((float) barX0,
//								(float) (barY0), hashMap
//										.get(thematicRendition.getFieldName()[i]),
//								(float) (barX0 ), (float) (barY0 - barWidth / 2),
//								new Color(255, 255, 255, 255), true);
//						g.setPaint(gradientPaint);
						g.setColor(hashMap.get(thematicRendition.getFieldName()[i]));
						g.fill(rectangleLeft);
						if (drawBarOutline) {
							g.setColor(new Color(outLinePaint, false));
							BasicStroke basicStroke = new BasicStroke(
									outLineBasicStroke);
							g.setStroke(basicStroke);
							g.draw(rectangleLeft);
						}
					
				}
			}
		}
		//图例
		FontMetrics fm = g.getFontMetrics();
        Rectangle2D rec=fm.getStringBounds(fieldList[0], g); 
		Font font2 = new Font("宋体", Font.PLAIN, 15);
		int xx = symbolWidth / 2 - 6 - (int)(rec.getWidth()/2);
		for (int i = 0; i < fieldList.length; i++) {
			

			Rectangle2D.Double fieldRect = new Rectangle2D.Double(xx, domainAxis.getY1() +4 +10 +5 + 15-7 + 21*i, 7, 7);
			g.setColor(hashMap.get(fieldList[i]));
			g.fill(fieldRect);
			g.setColor(Color.BLACK);
			g.setFont(font2);
			g.drawString(fieldList[i], xx+13,(int) (domainAxis.getY1() +4 +10 +5 + 15 + 21*i));
		}
		
        return image;
	}


	public HashMap<String, Shape> generateHotArea(double x, double y,
			int width, int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		double maxValue = maxValues[0];
//		Rectangle2D.Double symbolEnvlope = new Rectangle2D.Double(x
//				- thematicRendition.getHeight() / 2, y
//				- thematicRendition.getWidth(), height, width); // 设置绘图区域
	
		double minimumBarLength = chartStyle.getMinimumBarLength();
		double itemMargin = chartStyle.getItemMargin();
//
//		double realItemMargin = itemMargin
//				* (thematicRendition.getDomainAxis().length - 1) * 1.0
//				/ height;// 根据设置的柱子间的间距，求出所有间距之和占据绘图区域宽度的百分比
//		double barWidth = (height * (1 - realItemMargin) / thematicRendition.getDomainAxis().length);
//		
//	double maxBarWidth = 0.51*height;
		
		double realItemMargin = itemMargin/minimumBarLength*(thematicRendition.getDomainAxis().length-1);
		double barWidth = height/( thematicRendition.getDomainAxis().length+realItemMargin);
		       itemMargin = itemMargin/minimumBarLength*barWidth;
		double maxBarWidth = 0.51*height;
		if (barWidth > maxBarWidth) {
			barWidth = maxBarWidth;
		}

		HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
		double value = 0;
		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			for (int j = 0; j < indicatorDatas.length; j++) {
				if (indicatorDatas[j] == null) {
					System.out.println("区域数据为空！");
					return null;
				}else {
					if (i == 0) {
						
						value = indicatorDatas[j].getValues()[i];
						
						double barY0 = 0;
						if (barWidth < maxBarWidth) {
							barY0 = y - barWidth - j*(barWidth + itemMargin);
						}else {
							barY0 = y- height*1.0/2-barWidth/2;//一根柱子时
						}
						double barHeight = value / maxValue * width/2;
						double barX0 = x - barHeight;
						
						Rectangle2D.Double rectangleLeft = new Rectangle2D.Double(barX0,barY0,barHeight,barWidth);
						String text = "";
						if ((thematicRendition.getDomainAxis()[j] == null)||(thematicRendition.getDomainAxis()[j].length()==0)) {
							text = "(" + indicatorDatas[j].getNames()[i] + ")="
								+ indicatorDatas[j].getValues()[i];
						}else {
							text = "("
								+ thematicRendition.getDomainAxis()[j] + ","
								+ indicatorDatas[j].getNames()[i] + ")="
								+ indicatorDatas[j].getValues()[i];
						}
						hotAreahHashMap.put(text, rectangleLeft);

					}else {
						value = indicatorDatas[j].getValues()[i];
						
						double barY0 = 0;
						if (barWidth < maxBarWidth) {
							barY0 = y - barWidth - j*(barWidth + itemMargin);
						}else {
							barY0 = y- height*1.0/2-barWidth/2;//一根柱子时
						}
						double barHeight = value / maxValue * width/2;
						double barX0 = x;
						
						Rectangle2D.Double rectangleRight = new Rectangle2D.Double(barX0,barY0,barHeight,barWidth);
						String text = "";
						if (thematicRendition.getDomainAxis()[j] == null||thematicRendition.getDomainAxis()[j].length()==0) {
							text = "(" + indicatorDatas[j].getNames()[i] + ")="
								+ indicatorDatas[j].getValues()[i];
						}else {
							text = "("
								+ thematicRendition.getDomainAxis()[j] + ","
								+ indicatorDatas[j].getNames()[i] + ")="
								+ indicatorDatas[j].getValues()[i];
						}
						hotAreahHashMap.put(text, rectangleRight);

					}
				}
			}
		}
		return hotAreahHashMap;
	}

}
