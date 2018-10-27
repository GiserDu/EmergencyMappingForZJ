package com.zz.chart.obj.line;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;

public class DoubleLineChart implements IChart {

	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (indicatorDatas[0] == null) {
			System.out.println("绘制专题符号图层时发现专题数据中区域数据不完整！");
			return;
		}
		double[] valueRight = new double[indicatorDatas.length];
		double[] valueLeft = new double[indicatorDatas.length];
//		byte[] fieldGroup = thematicRendition.getFieldGroup();
//		byte group = fieldGroup[0];
		String[] fieldGroup = thematicRendition.getDomainAxis();
		byte group = Byte.parseByte(fieldGroup[0]);
		int margin = (int) (height * 1.0 / (indicatorDatas.length - 1));
		double maxValue = 0;
		if (maxValues[0] > maxValues[1]) {
			maxValue = maxValues[0];
		} else {
			maxValue = maxValues[1];
		}
		// String chartid=chartStyle.getChartID();
		String bfp = chartStyle.getBaseFillPaint();
		boolean lineLable = chartStyle.isLineLable();
		int lineItemLabelFontSize = chartStyle.getLineItemLabelFontSize();
		String lineItemLabelFontName = chartStyle.getLineItemLabelFontName();
		int lineItemLabelPaint = chartStyle.getLineItemLabelPaint();
		

		String[] tempcolorStrings;
		int colortemp = 0;
		tempcolorStrings = bfp.split(",");
		for (int j = 0; j < tempcolorStrings.length; j++) {
			if (0 == j) {
				colortemp = Integer
						.parseInt(tempcolorStrings[tempcolorStrings.length - j
								- 1]);
			} else {
				colortemp = Integer
						.parseInt(tempcolorStrings[tempcolorStrings.length - j
								- 1])
						+ colortemp * 256;
			}
		}
		int baseFillPaint = colortemp;

		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			for (int j = 0; j < indicatorDatas.length; j++) {
				if (i < group) {
					valueLeft[j] = indicatorDatas[j].getValues()[i];
				} else {
					valueRight[j] = indicatorDatas[j].getValues()[i];// 默认第一组为左边的
				}

			}

			double x0 = x;// 当前点
			double y0 = y;
			double x1 = 0;// 记录上一个点
			double y1 = 0;
			double width0 = width * 19.0 / 20 / 2;// 两组数据间距为总宽度的1/20
			if (i < group) {
				for (int j = 0; j < valueLeft.length; j++) {
					x0 = x - width * 1.0 / 20 / 2 - width0 * valueLeft[j]
							/ maxValue;
					
					Shape shape = new Ellipse2D.Double(x0 - 2, y0 - 2, 4, 4);
					if (j == 0) {
						g.setColor(new Color(baseFillPaint));
						g.fill(shape);
						g.setColor(new Color(
								thematicRendition.getFieldColor()[i]));
						g.draw(shape);
					} else {
						g.setColor(new Color(baseFillPaint));
						g.fill(shape);
						g.setColor(new Color(
								thematicRendition.getFieldColor()[i]));
						g.draw(shape);
						g.setColor(new Color(
								thematicRendition.getFieldColor()[i]));
						Line2D line2d = new Line2D.Double(x1, y1, x0, y0);
						g.draw(line2d);
						GeneralPath generalPath = new GeneralPath();
						generalPath.moveTo(x0, y0);
						generalPath.lineTo(x1, y1);
						generalPath.lineTo(x - width * 1.0 / 20 / 2, y1);
						generalPath.lineTo(x - width * 1.0 / 20 / 2, y0);
						generalPath.closePath();
						g.setColor(new Color(
								thematicRendition.getFieldColor()[i],
								false));
						g.fill(generalPath);
					}
					if (lineLable) {
						
						Font font = new Font(lineItemLabelFontName, Font.PLAIN,lineItemLabelFontSize);
						g.setColor(new Color(lineItemLabelPaint, false));
						g.setFont(font);
						g.drawString(Float.toString((float)valueLeft[j]), (int)x0,
								(int)y0 -4);
					}
					x1 = x0;
					y1 = y0;
					y0 = y0 - margin;
				}
			} else {
				for (int j = 0; j < valueRight.length; j++) {
					x0 = x + width * 1.0 / 20 / 2 + width0 * valueRight[j]
							/ maxValue;
					
					Shape shape = new Ellipse2D.Double(x0 - 2, y0 - 2, 4, 4);
					if (j == 0) {
						g.setColor(new Color(baseFillPaint));
						g.fill(shape);
						g.setColor(new Color(
								thematicRendition.getFieldColor()[i]));
						g.draw(shape);
					} else {
						g.setColor(new Color(baseFillPaint));
						g.fill(shape);
						g.setColor(new Color(
								thematicRendition.getFieldColor()[i]));
						g.draw(shape);
						g.setColor(new Color(
								thematicRendition.getFieldColor()[i]));
						Line2D line2d = new Line2D.Double(x1, y1, x0, y0);
						g.draw(line2d);
						GeneralPath generalPath = new GeneralPath();
						generalPath.moveTo(x0, y0);
						generalPath.lineTo(x1, y1);
						generalPath.lineTo(x + width * 1.0 / 20 / 2, y1);
						generalPath.lineTo(x + width * 1.0 / 20 / 2, y0);
						generalPath.closePath();
						g.setColor(new Color(
								thematicRendition.getFieldColor()[i]
										,
								false));
						g.fill(generalPath);
					}
					if (lineLable) {
						
						Font font = new Font(lineItemLabelFontName, Font.PLAIN,lineItemLabelFontSize);
						g.setColor(new Color(lineItemLabelPaint, false));
						g.setFont(font);
						g.drawString(Float.toString((float) valueRight[j]), (int)x0,
								(int)y0 -4);
					}
					x1 = x0;
					y1 = y0;
					y0 = y0 - margin;
				}
			}
		}
	}

	public BufferedImage drawLegend(int width, int height,
			ChartDataPara thematicRendition, ChartStyle chartStyle,
			double[] maxValues, double[] minValues, double[] averageValues,
			IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		String bfp = chartStyle.getBaseFillPaint();

		String[] tempcolorStrings;
		int colortemp = 0;
		tempcolorStrings = bfp.split(",");

		for (int j = 0; j < tempcolorStrings.length; j++) {
			if (0 == j) {
				colortemp = Integer
						.parseInt(tempcolorStrings[tempcolorStrings.length - j
								- 1]);
			} else {
				colortemp = Integer
						.parseInt(tempcolorStrings[tempcolorStrings.length - j
								- 1])
						+ colortemp * 256;
			}
		}
		int baseFillPaint = colortemp;

		BufferedImage image = new BufferedImage(width + 60 + 50, height + 25
				+ thematicRendition.getFieldName().length / 2 * 15 + 10 + 20 + 20 + 10,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// g.setBackground(Color.WHITE);
		// g.clearRect(0, 0, image.getWidth(), image.getHeight());
		double maxValue = 0;
		if (maxValues[0] > maxValues[1]) {
			maxValue = maxValues[0];
		} else {
			maxValue = maxValues[1];
		}

//		byte[] fieldGroup = thematicRendition.getFieldGroup();
//		byte group = fieldGroup[0];
		
		String[] fieldGroup = thematicRendition.getDomainAxis();
		byte group = Byte.parseByte(fieldGroup[0]);
		int x = (int) (image.getWidth() * 1.0 / 2);
		int margin = (int) (height * 1.0 / (indicatorDatas.length - 1));
		g.setColor(Color.BLACK);
		g
				.draw(new Rectangle(1, 1, image.getWidth() - 2, image
						.getHeight() - 2));
		g.setFont(new Font("黑体", Font.PLAIN, 14));
		if ((thematicRendition.getDomainAxisUnit() != null)
				&& (!(thematicRendition.getDomainAxisUnit().length() == 0))) {
			g.drawString("(" + thematicRendition.getDomainAxisUnit() + ")",
					x - 21, 20);
		}

		// Ellipse2D ellipse2d = new Ellipse2D.Double(x-1,
		// image.getHeight()/2-1, 5, 5);
		// g.fill(ellipse2d);
		FontMetrics fm = g.getFontMetrics();
		double x0L = x - 30;// 当前点Left
		double x0R = x + 30;// 当前点Right
		double y0 = 20 + 5;
		double x1 = 0;// 记录上一个点
		double y1 = 0;
		for (int i = 0; i < thematicRendition.getDomainAxis().length; i++) {

			g.setFont(new Font("宋体", Font.BOLD, 12));
			g.setColor(Color.BLACK);
			Rectangle2D rec = fm.getStringBounds(thematicRendition
					.getDomainAxis()[thematicRendition.getDomainAxis().length
					- 1 - i], g);
			double stringLength = rec.getWidth();
			g.drawString(thematicRendition.getDomainAxis()[thematicRendition
					.getDomainAxis().length
					- 1 - i], x - (int) (stringLength / 2), (int)y0
					+ 6);

			
			for (int j = 0; j < thematicRendition.getFieldName().length; j++) {

				if (j < group) {
					x0L = x - 30 - width * 1.0 / 2
							/ indicatorDatas[0].getValues().length * 2
							* (j + 1);
					Shape shapeL = new Ellipse2D.Double(x0L - 2, y0 - 2, 4, 4);
					if (i == 0) {
						g.setColor(new Color(baseFillPaint));
						g.fill(shapeL);
						g.setColor(new Color(
								thematicRendition.getFieldColor()[j]));
						g.draw(shapeL);
					} else {
						g.setColor(new Color(baseFillPaint));
						g.fill(shapeL);
						g.setColor(new Color(
								thematicRendition.getFieldColor()[j]));
						g.draw(shapeL);
						g.setColor(new Color(
								thematicRendition.getFieldColor()[j]));
						x1 = x0L;
						y1 = y0 - margin;
						Line2D line2dL = new Line2D.Double(x1, y1, x0L, y0);
						g.draw(line2dL);
						GeneralPath generalPathL = new GeneralPath();
						generalPathL.moveTo(x0L, y0);
						generalPathL.lineTo(x1, y1);
						generalPathL.lineTo(x - 30, y1);
						generalPathL.lineTo(x - 30, y0);
						generalPathL.closePath();
						g.setColor(new Color(
								thematicRendition.getFieldColor()[j],
								false));
						g.fill(generalPathL);
					}

					x1 = x0L;
					y1 = y0;
				} else {
					x0R = x + 30 + width * 1.0 / 2
							/ indicatorDatas[0].getValues().length * 2
							* (j - group + 1);
					Shape shapeR = new Ellipse2D.Double(x0R - 2, y0 - 2, 4, 4);
					if (i == 0) {
						g.setColor(new Color(baseFillPaint));
						g.fill(shapeR);
						g.setColor(new Color(
								thematicRendition.getFieldColor()[j]));
						g.draw(shapeR);
					} else {
						g.setColor(new Color(baseFillPaint));
						g.fill(shapeR);
						g.setColor(new Color(
								thematicRendition.getFieldColor()[j]));
						g.draw(shapeR);
						g.setColor(new Color(
								thematicRendition.getFieldColor()[j]));
						x1 = x0R;
						y1 = y0 - margin;
						Line2D line2dR = new Line2D.Double(x1, y1, x0R, y0);
						g.draw(line2dR);
						GeneralPath generalPathR = new GeneralPath();
						generalPathR.moveTo(x0R, y0);
						generalPathR.lineTo(x1, y1);
						generalPathR.lineTo(x + 30, y1);
						generalPathR.lineTo(x + 30, y0);
						generalPathR.closePath();
						g.setColor(new Color(
								thematicRendition.getFieldColor()[j],
								false));
						g.fill(generalPathR);
					}
				}
			}
			y0 = y0 + margin;
		}
		if (thematicRendition.getFieldUnits()[0] != null&&thematicRendition.getFieldUnits()[0].length()!=0) {
			g.setFont(new Font("宋体", Font.PLAIN, 10));
			g.setColor(Color.black);
			g.drawString(thematicRendition.getFieldUnits()[0],
					x
							- 30
							- (int) (width * 1.0 / 2) - 5, height + 25 + 15 + 15);
			g.drawString(thematicRendition.getFieldUnits()[1],
					x
							+ 30
							+ (int) (width * 1.0 / 2) - 5, height + 25 + 15 + 15);
		}

		int value = (int) (maxValue / thematicRendition.getFieldName().length * 2);
		boolean isDrawMark = true;//尽量避免刻度出现重叠
		boolean isDrawMark2 = true;
		for (int k = 0; k < thematicRendition.getFieldName().length; k++) {
			if (k < group) {
				g.setFont(new Font("宋体", Font.PLAIN, 10));
				g.setColor(Color.black);
				
				if (k > 0) {
					Rectangle2D rec1 = fm.getStringBounds(Integer.toString(value * (k + 1)), g);
					Rectangle2D rec2 = fm.getStringBounds(Integer.toString(value * (k)), g);
					if (rec1.intersects(rec2)&&isDrawMark) {
						isDrawMark = false;
					}else {
						isDrawMark = true;
					}
					
				}
				if (isDrawMark) {
					g.drawString(Integer.toString(value * (group - k)),
							x
									- 30 - (int) (width * 1.0 / 2)
									+ (int) (width * 1.0 / 2
											/ indicatorDatas[0].getValues().length
											*2* (k)) - 5, height + 15 + 25);
				}
				
				Rectangle2D.Double rectangleDouble = new Rectangle2D.Double(x
						- 30 - width * 1.0 / 2 + 3, height + 35 + 40 + (k + 1)
						* 15 - 10, 7, 7);
				g.setColor(new Color(thematicRendition.getFieldColor()[k]
						, false));
				g.fill(rectangleDouble);
				g.setFont(new Font("宋体", Font.PLAIN, 15));
				g.setColor(Color.black);
				g.drawString(thematicRendition.getFieldName()[k], x - 30
						- width / 2 + 15, height + 35 + 40 + (k + 1) * 15);
			} else {
				
				g.setFont(new Font("宋体", Font.PLAIN, 10));
				g.setColor(Color.black);
				if (k - group > 0) {
					Rectangle2D rec1 = fm.getStringBounds(Integer.toString(value * (k + 1)), g);
					Rectangle2D rec2 = fm.getStringBounds(Integer.toString(value * (k)), g);
					if (rec1.intersects(rec2)&&isDrawMark2) {
						isDrawMark2 = false;
					}else {
						isDrawMark2 = true;
					}
				}
				if (isDrawMark2) {
					g.drawString(Integer.toString(value * (thematicRendition.getFieldName().length - k)), x
							+ 30 + (int) (width * 1.0 / 2)
							- (int) (width * 1.0 / 2
									/ indicatorDatas[0].getValues().length * 2 * (k
									- group)) - 5, height + 15 + 25);
				}
				
				Rectangle2D.Double rectangleDouble = new Rectangle2D.Double(
						x + 30 + 3, height + 35 + 40 + (k - group + 1) * 15
								- 10, 7, 7);
				g.setColor(new Color(thematicRendition.getFieldColor()[k]
						, false));
				g.fill(rectangleDouble);
				g.setFont(new Font("宋体", Font.PLAIN, 15));
				g.setColor(Color.black);
				g.drawString(thematicRendition.getFieldName()[k], x + 30 + 15,
						height + 35 + 40 + (k - group + 1) * 15);
			}
		}
		return image;
	}

	public HashMap<String, Shape> generateHotArea(double x, double y,
			int width, int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		HashMap<String, Shape> hotAreaHashMap = new HashMap<String, Shape>();

		if (indicatorDatas[0] == null) {
			System.out.println("获取专题符号热区时发现专题数据中区域数据不完整！");
			return null;
		}
		double[] valueRight = new double[indicatorDatas.length];
		double[] valueLeft = new double[indicatorDatas.length];
//		byte[] fieldGroup = thematicRendition.getFieldGroup();
//		byte group = fieldGroup[0];
		
		String[] fieldGroup = thematicRendition.getDomainAxis();
		byte group = Byte.parseByte(fieldGroup[0]);
		
		int margin = (int) (height * 1.0 / (indicatorDatas.length - 1));

		double maxValue = 0;
		if (maxValues[0] > maxValues[1]) {
			maxValue = maxValues[0];
		} else {
			maxValue = maxValues[1];
		}

		String bfp = chartStyle.getBaseFillPaint();

		String[] tempcolorStrings;
		int colortemp = 0;
		tempcolorStrings = bfp.split(",");
		for (int j = 0; j < tempcolorStrings.length; j++) {
			if (0 == j) {
				colortemp = Integer
						.parseInt(tempcolorStrings[tempcolorStrings.length - j
								- 1]);
			} else {
				colortemp = Integer
						.parseInt(tempcolorStrings[tempcolorStrings.length - j
								- 1])
						+ colortemp * 256;
			}
		}

		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			for (int j = 0; j < indicatorDatas.length; j++) {
				if (i < group) {
					valueLeft[j] = indicatorDatas[j].getValues()[i];
				} else {
					valueRight[j] = indicatorDatas[j].getValues()[i];// 默认第一组为左边的
				}

			}

			double x0 = x;// 当前点
			double y0 = y;
			double width0 = width * 19.0 / 20 / 2;// 两组数据间距为总宽度的1/20

			if (i < group) {
				for (int j = 0; j < valueLeft.length; j++) {
					x0 = x - width * 1.0 / 20 / 2 - width0 * valueLeft[j]
							/ maxValue;
					
					Shape shape = new Ellipse2D.Double(x0 - 2, y0 - 2, 4, 4);
					String text = "";

					if (thematicRendition.getDomainAxis()[j] == null
							|| thematicRendition.getDomainAxis()[j].length() == 0) {
						text = "(" + indicatorDatas[j].getNames()[i] + ")="
								+ indicatorDatas[j].getValues()[i];
					} else {
						text = "(" + thematicRendition.getDomainAxis()[j] + ","
								+ indicatorDatas[j].getNames()[i] + ")="
								+ indicatorDatas[j].getValues()[i];
					}
					hotAreaHashMap.put(text, shape);
					y0 = y0 - margin;
				}
			} else {
				for (int j = 0; j < valueRight.length; j++) {
					x0 = x + width * 1.0 / 20 / 2 + width0 * valueRight[j]
							/ maxValue;
					Shape shape = new Ellipse2D.Double(x0 - 2, y0 - 2, 4, 4);
					String text = "";

					if (thematicRendition.getDomainAxis()[j] == null
							|| thematicRendition.getDomainAxis()[j].length() == 0) {
						text = "(" + indicatorDatas[j].getNames()[i] + ")="
								+ indicatorDatas[j].getValues()[i];
					} else {
						text = "(" + thematicRendition.getDomainAxis()[j] + ","
								+ indicatorDatas[j].getNames()[i] + ")="
								+ indicatorDatas[j].getValues()[i];
					}
					hotAreaHashMap.put(text, shape);
					y0 = y0 - margin;
				}
			}
		}
		return hotAreaHashMap;
	}

}
