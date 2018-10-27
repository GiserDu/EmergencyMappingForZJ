package com.zz.chart.chartfactory;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;



/**
 * 
 * @author chen
 *				chart interface
 *				
 */
public interface IChart {


	public void drawChart(Graphics2D g, double x, double y, int width, int height,
                          ChartDataPara chartDataPara, ChartStyle chartStyle, double[] maxValues, double[] minValues, double[] averageValues
            , IndicatorData[] indicatorDatas);

	public BufferedImage drawLegend(int width, int height,
                                    ChartDataPara chartDataPara, ChartStyle chartStyle, double[] maxValues, double[] minValues, double[] averageValues
            , IndicatorData[] indicatorDatas);

	public HashMap<String, Shape> generateHotArea(double x, double y, int width, int height,
                                                  ChartDataPara chartDataPara, ChartStyle chartStyle, double[] maxValues, double[] minValues, double[] averageValues
            , IndicatorData[] indicatorDatas);

}
