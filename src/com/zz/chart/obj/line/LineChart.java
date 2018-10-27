package com.zz.chart.obj.line;

import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.HashMap;


public class LineChart implements IChart {

	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		y = y + height/2;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		double maxValue = maxValues[0];
		String bfp = chartStyle.getBaseFillPaint();
		// System.out.println(bfp);

		String[] tempcolorStrings;
		int colortemp = 0;
		tempcolorStrings = bfp.split(",");
		for (int j = 0; j < tempcolorStrings.length; j++) {
			if (0 == j) {
				colortemp = Integer
						.parseInt(tempcolorStrings[j]);
			} else {
				colortemp = Integer
						.parseInt(tempcolorStrings[j])
						+ colortemp * 256;
			}
		}
		int baseFillPaint = colortemp;

		float baa = chartStyle.getBaseAreaAlpha();
		boolean lineLable = chartStyle.isLineLable();
		int lineItemLabelFontSize = chartStyle.getLineItemLabelFontSize();
		String lineItemLabelFontName = chartStyle.getLineItemLabelFontName();
		int lineItemLabelPaint = chartStyle.getLineItemLabelPaint();
		// int LM=chartStyle.getLowerMargin();

		double x0 = x;// 当前点
		double y0 = y;
		double x1 = 0;// 记录上一个点
		double y1 = 0;
		double margin = width * 1.0 / indicatorDatas.length;
		for (int i = 0; i < indicatorDatas[0].getValues().length; i++) {
			for (int j = 0; j < indicatorDatas.length; j++) {
				x0 = x - width * 1.0 / 2 + margin / 2 + margin * j;
				y0 = y - indicatorDatas[j].getValues()[i] / maxValue * height;
				Shape shape = new Ellipse2D.Double(x0 - 2, y0 - 2, 4, 4);
				if (j == 0) {
					g.setColor(new Color(baseFillPaint));
					g.fill(shape);
					
					g.setColor(new Color(thematicRendition.getFieldColor()[i]));
					g.draw(shape);
				} else {
					g.setColor(new Color(thematicRendition.getFieldColor()[i]));
					g.draw(shape);
					Line2D line2d = new Line2D.Double(x1, y1, x0, y0);
					g.draw(line2d);
					if (baa != 0) {
						GeneralPath generalPath = new GeneralPath();
						generalPath.moveTo(x0, y0);
						generalPath.lineTo(x1, y1);
						generalPath.lineTo(x1, y);
						generalPath.lineTo(x0, y);
						generalPath.closePath();
						g.setColor(new Color(
								thematicRendition.getFieldColor()[i],
								false));
						g.fill(generalPath);
					}
					g.setColor(new Color(baseFillPaint));
					g.fill(shape);
					g.setColor(new Color(thematicRendition.getFieldColor()[i]));
					g.draw(shape);
				}
				if (lineLable) {
					
					Font font = new Font(lineItemLabelFontName, Font.PLAIN,lineItemLabelFontSize);
					g.setColor(new Color(lineItemLabelPaint, false));
					g.setFont(font);
					g.drawString(Float.toString((float) indicatorDatas[j].getValues()[i]), (int)x0,
							(int)y0 -4);
				}
				x1 = x0;
				y1 = y0;
			}
		}
		g.setColor(Color.BLACK);
		Line2D rangeAxis = new Line2D.Double(x - width * 1.0 / 2, y, x - width
				* 1.0 / 2, y - height);
		Line2D domainAxis = new Line2D.Double(x - width * 1.0 / 2, y, x + width
				* 1.0 / 2, y);
		g.draw(rangeAxis);
		g.draw(domainAxis);
		for (int j = 0; j < indicatorDatas[0].getValues().length; j++) {
			Line2D line2dRange = new Line2D.Double(x - width * 1.0 / 2  - 2, y - height/indicatorDatas[0].getValues().length*(j+1), x - width
					* 1.0 / 2, y - height/indicatorDatas[0].getValues().length*(j+1));
			g.draw(line2dRange);
		}
		// DefaultCategoryDataset linedataset = new DefaultCategoryDataset();
		//			
		//			
		// for (int i = 0; i < indicatorDatas.length; i++) {
		// if(null != indicatorDatas[i]) {
		// tempvalue = indicatorDatas[i].getValues();
		// //System.out.println(indicatorDatas[i].getDomainAxis());
		//				
		// for (int j = 0; j < nameStrings.length; j++) {
		// linedataset.setValue(tempvalue[j], nameStrings[j],
		// indicatorDatas[i].getDomainAxis());
		// }
		// }
		// }
		//
		//
		// AffineTransform beforeTransform = g.getTransform();
		//			
		// HashMap<String, Color>has=new HashMap<String, Color>();
		// for (int i = 0; i < thematicRendition.getFieldColor().length; i++) {
		//				
		// has.put(thematicRendition.getFieldName()[i],new
		// Color(thematicRendition.getFieldColor()[i]) );
		// }
		//			
		// CategoryAxis categoryAxis = new CategoryAxis(null);
		// ValueAxis valueAxis = new NumberAxis("");
		//
		// NumberFormat numberFormat1 = NumberFormat.getNumberInstance();
		// numberFormat1.setGroupingUsed(false);
		//
		// TLineAndShapeRenderer renderer1 = new TLineAndShapeRenderer(true,
		// false);
		// //renderer1.setBaseToolTipGenerator(new
		// StandardCategoryToolTipGenerator("({0},{1})={2}", numberFormat1));
		//
		// CategoryPlot plot1 = new CategoryPlot(linedataset, categoryAxis,
		// valueAxis,renderer1);
		// plot1.setOrientation(PlotOrientation.VERTICAL);
		// JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
		// plot1, false);
		// ChartUtilities.applyCurrentTheme(chart);
		//
		//		
		//			
		// chart.setBackgroundPaint(null);
		// CategoryPlot plot2 = chart.getCategoryPlot();
		// //plot2.setAxisOffset(RectangleInsets.ZERO_INSETS);
		// plot2.setInsets(RectangleInsets.ZERO_INSETS);
		// plot2.setAxisOffset(RectangleInsets.ZERO_INSETS);
		// plot2.setDrawSharedDomainAxis(false);
		//			
		//			
		// //背景网格
		// plot2.setOutlinePaint(null);
		// plot2.setBackgroundPaint(null);
		// plot2.setDomainGridlinesVisible(false);
		// plot2.setRangeGridlinesVisible(false);
		// plot2.setDomainGridlinePaint(Color.black);
		// plot2.setDomainGridlineStroke(new BasicStroke(1f));
		// plot2.setRangeGridlineStroke(new BasicStroke(1f));
		//			
		// TLineAndShapeRenderer renderer = (TLineAndShapeRenderer)
		// plot2.getRenderer();
		// //renderer.setBaseToolTipGenerator(new
		// StandardCategoryToolTipGenerator("({0},{1})={2}", numberFormat));
		// //每一个点的设置
		// renderer.setBaseShapesVisible(isbsv);
		// renderer.setDrawOutlines(isdo);
		// renderer.setUseFillPaint(isufp);
		// renderer.setBaseFillPaint(new Color(baseFillPaint));
		// renderer.setShape(new Ellipse2D.Double(-2, -2, 4, 4));
		//	        
		// //每条线颜色设置
		// for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
		//				
		// renderer.setSeriesPaint(i,
		// has.get(thematicRendition.getFieldName()[i]));
		// // System.out.println(has.get(thematicRendition.getFieldName()[i]));
		// }
		//       
		// renderer.setOutlineStroke(new BasicStroke(olStroke));
		// renderer.setStroke(new BasicStroke(Stroke));
		//
		// //透明度
		// renderer.setBaseAreaAlpha(baa);
		// plot2.setRenderer(renderer);
		//
		//
		// //纵坐标
		// NumberAxis rangeAxis = (NumberAxis) plot2.getRangeAxis();
		// rangeAxis.setVisible(israv);
		// rangeAxis.setTickLabelsVisible(isrtlv);
		// rangeAxis.setLowerMargin(0.0);
		// //rangeAxis.setUpperMargin(0.0);
		// //横坐标
		// rangeAxis.setRange(0, maxValue+1000);
		// CategoryAxis domainAxis = plot2.getDomainAxis();
		//			
		// domainAxis.setTickMarksVisible(true);
		// domainAxis.setTickMarkOutsideLength(0.0f);
		// domainAxis.setTickLabelsVisible(isdtlv);
		// domainAxis.setVisible(isdav);
		//			
		//
		// Rectangle2D.Double symbolEnvlope = new
		// Rectangle2D.Double(x-(width+10)/2, y-height, width+10, height);
		// ChartRenderingInfo info = new ChartRenderingInfo(new
		// StandardEntityCollection());
		// //g.rotate(180*Math.PI/180, symbolEnvlope.getCenterX(),
		// symbolEnvlope.getCenterY());
		//
		// chart.draw(g, symbolEnvlope, null,info);
		// //chart.draw(g, symbolEnvlope);
		// g.setTransform(beforeTransform);
		// g.setColor(Color.BLACK);
		// g.draw(symbolEnvlope);
		// break;

	}

	public BufferedImage drawLegend(int width, int height,
			ChartDataPara thematicRendition, ChartStyle chartStyle,
			double[] maxValues, double[] minValues, double[] averageValues,
			IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		double maxValue = maxValues[0];


		String[] fieldUnits = thematicRendition.getFieldUnits();
		//String domainAxisUnit = thematicRendition.getDomainAxisUnit();

		String[] fieldList = thematicRendition.getFieldName();

		String bfp = chartStyle.getBaseFillPaint();
		// System.out.println(bfp);

		String[] tempcolorStrings;
		int colortemp = 0;
		tempcolorStrings = bfp.split(",");
		for (int j = 0; j < tempcolorStrings.length; j++) {
			if (0 == j) {
				colortemp = Integer
						.parseInt(tempcolorStrings[j]);
			} else {
				colortemp = Integer
						.parseInt(tempcolorStrings[j])
						+ colortemp * 256;
			}
		}
		int baseFillPaint = colortemp;

		float baa = chartStyle.getBaseAreaAlpha();
		
		double margin = width * 1.0 / indicatorDatas.length;

		HashMap<String, Color> fieldNameAndColorHashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldColor().length; i++) {
			fieldNameAndColorHashMap.put(thematicRendition.getFieldName()[i],
					new Color(thematicRendition.getFieldColor()[i], false));
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
		Line2D rangeAxis = new Line2D.Double(symbolWidth / 2 - width / 2 + 15 - 5,
				60 + 5 , symbolWidth / 2 - width / 2 + 15 - 5, 60 + height + 5);
		Line2D domainAxis = new Line2D.Double(symbolWidth / 2 - width / 2 + 15 - 5,
				60 + height + 5, symbolWidth / 2 - width / 2 + 15 + width - 5,
				60 + height + 5);

		g.draw(domainAxis);
		g.draw(rangeAxis);


		// 刻度
		Font font = new Font("宋体", Font.PLAIN, 10);
		g.setFont(font);
		g.setColor(Color.BLACK);
		for (int i = 0; i < thematicRendition.getFieldName().length + 1; i++) {
			Line2D line2dRange = new Line2D.Double(domainAxis.getX1() - 2,
					domainAxis.getY2()
							- height/thematicRendition.getFieldName().length * i, domainAxis.getX1(),
							domainAxis.getY2()
							- height/thematicRendition.getFieldName().length * i);
			g.draw(line2dRange);
			FontMetrics fm = g.getFontMetrics();
			NumberFormat ddf1=NumberFormat.getNumberInstance();
		    if (maxValue<10) {
		    	ddf1.setMaximumFractionDigits(2);
			}else {
				ddf1.setMaximumFractionDigits(0);
			}
		    
		    String tempString= ddf1.format(maxValue/thematicRendition.getFieldName().length * i);
			Rectangle2D rec = fm.getStringBounds(tempString, g);

			g.drawString(tempString,
					(int) (domainAxis.getX1() - 2 - rec.getWidth() - 5 ),
					(int) (domainAxis.getY2()
							- height/thematicRendition.getFieldName().length * i));
		}
		for (int i = 0; i < thematicRendition.getDomainAxis().length; i++) {
			Line2D line2dDomain;
			line2dDomain = new Line2D.Double(domainAxis.getX1() + margin / 2 + margin*i , domainAxis
						.getY2(), domainAxis.getX1() + margin / 2 + margin*i, domainAxis.getY2() + 2);
	
			g.draw(line2dDomain);
			g.drawString(thematicRendition.getDomainAxis()[i],
					(int) line2dDomain.getX1() - 10 + 5,
					(int) line2dDomain.getY2() + 2 + 10);
		}

		// 图
		double x = (domainAxis.getX1() + domainAxis.getX2())/2;
		double y = domainAxis.getY1();
		
		double x0 = x;// 当前点
		double y0 = y;
		
		double x1 = 0;// 记录上一个点
		double y1 = 0;
		
		for (int i = 0; i < indicatorDatas[0].getValues().length; i++) {
			for (int j = 0; j < indicatorDatas.length; j++) {
				x0 = x - width * 1.0 / 2 + margin / 2 + margin * j;
				y0 = y - height / indicatorDatas[0].getValues().length* (i+1);
				Shape shape = new Ellipse2D.Double(x0 - 2, y0 - 2, 4, 4);
				if (j == 0) {
					g.setColor(new Color(baseFillPaint));
					g.fill(shape);
					
					g.setColor(new Color(thematicRendition.getFieldColor()[i]));
					g.draw(shape);
				} else {
					g.setColor(new Color(thematicRendition.getFieldColor()[i]));
					g.draw(shape);
					Line2D line2d = new Line2D.Double(x1, y1, x0, y0);
					g.draw(line2d);
					if (baa != 0) {
						GeneralPath generalPath = new GeneralPath();
						generalPath.moveTo(x0, y0);
						generalPath.lineTo(x1, y1);
						generalPath.lineTo(x1, y);
						generalPath.lineTo(x0, y);
						generalPath.closePath();
						g.setColor(new Color(
								thematicRendition.getFieldColor()[i],
								false));
						g.fill(generalPath);
					}
					g.setColor(new Color(baseFillPaint));
					g.fill(shape);
					g.setColor(new Color(thematicRendition.getFieldColor()[i]));
					g.draw(shape);
				}
				x1 = x0;
				y1 = y0;
			}
		}

		// 图例
		FontMetrics fm = g.getFontMetrics();
		Rectangle2D rec = fm.getStringBounds(fieldList[0], g);
		Font font2 = new Font("宋体", Font.PLAIN, 15);
		int xx = symbolWidth / 2 - 6 - (int) (rec.getWidth() / 2);
		for (int i = 0; i < fieldList.length; i++) {

			Shape shape = new Ellipse2D.Double(xx + 4 - 2,domainAxis.getY1() + 4 + 10 + 5 + 15 - 4 + 21 * i - 2, 4, 4);
			Line2D fieldLine2d = new Line2D.Double(xx, domainAxis.getY1() + 4 + 10 + 5 + 15 - 4 + 21 * i, xx + 8, domainAxis.getY1() + 4 + 10 + 5 + 15 - 4 + 21 * i);
			g.setColor(fieldNameAndColorHashMap.get(fieldList[i]));
			g.draw(fieldLine2d);
			g.setColor(new Color(baseFillPaint));
			g.fill(shape);
			
			g.setColor(new Color(thematicRendition.getFieldColor()[i]));
			g.draw(shape);
			g.setColor(Color.BLACK);
			g.setFont(font2);
			g.drawString(fieldList[i], xx + 13, (int) (domainAxis.getY1() + 4
					+ 10 + 5 + 15 + 21 * i));
		}

		return image;

	}
	public HashMap<String, Shape> generateHotArea(double x, double y,
			int width, int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		HashMap<String, Shape> hotAreaHashMap = new HashMap<String, Shape>();

		double maxValue = maxValues[0];
		double x0 = x;// 当前点
		double y0 = y;

		double margin = width * 1.0 / indicatorDatas.length;
		for (int j = 0; j < indicatorDatas[0].getValues().length; j++) {
			for (int i = 0; i < indicatorDatas.length; i++) {
				x0 = x - width * 1.0 / 2 + margin / 2 + margin * i;
				y0 = y - indicatorDatas[i].getValues()[j] / maxValue * height;
				Shape shape = new Ellipse2D.Double(x0 - 2, y0 - 2, 4, 4);
				String text = "";

				if (thematicRendition.getDomainAxis()[0] == null||thematicRendition.getDomainAxis()[0].length() == 0) {
					text = "("+indicatorDatas[i].getNames()[j] + ")="
					+ indicatorDatas[i].getValues()[j];
				}else {
					text = "(" + thematicRendition.getDomainAxis()[i] + ","
					+ indicatorDatas[i].getNames()[j] + ")="
					+ indicatorDatas[i].getValues()[j];
				}
				hotAreaHashMap.put(text, shape);
			}
		}
		return hotAreaHashMap;
	}

}
