package com.zz.chart.obj.line;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;


public class DoubleLine implements IChart{

	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		double[] valueRight = new double[indicatorDatas.length];
		double[] valueLeft = new double[indicatorDatas.length];
		String[] fieldGroup = thematicRendition.getDomainAxis();
		@SuppressWarnings("unused")
		byte group = Byte.parseByte(fieldGroup[0]);
		for (int i = 0; i < indicatorDatas.length; i++) {
				for (int j = 0; j < indicatorDatas[0].getValues().length; j++) {
					valueRight[j] = indicatorDatas[i].getValues()[j];
				}
				for (int j = 0; j < indicatorDatas[0].getValues().length; j++) {
					valueLeft[j] = indicatorDatas[i].getValues()[j];
				}
		}
		
		
		for (int i = 0; i < valueRight.length; i++) {
			if (i == 0) {
				
			}
		}
		
	}

	public BufferedImage drawLegend(int width, int height,
			ChartDataPara thematicRendition, ChartStyle chartStyle,
			double[] maxValues, double[] minValues,double[] averageValues,
			IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap<String, Shape> generateHotArea(double x, double y,
			int width, int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		return null;
	}

}
