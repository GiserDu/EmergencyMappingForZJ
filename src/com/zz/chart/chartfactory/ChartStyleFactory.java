package com.zz.chart.chartfactory;

import com.zz.chart.chartstyle.ChartStyle;
/**
 * 符号样式工厂类，生成符号样式对象
 * @author lmk
 */
public class ChartStyleFactory
{
	public ChartStyle createcChartStyle(String chartID)
	{
		String className = new JClassName().getChartStyleClassName(chartID);
		if(className == null)
		{
			System.out.println("ERROR CHARTID");
			return null;
		}
		else
			try {
				ChartStyle chartStyle = (ChartStyle) Class.forName(className).newInstance();
				return chartStyle;
			} catch (InstantiationException e) {
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
	}
}
