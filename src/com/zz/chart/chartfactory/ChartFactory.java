package com.zz.chart.chartfactory;
/**
 * 符号工厂类，生成符号对象
 * @author lmk
 *
 */
public class ChartFactory {
	public IChart createcChart(String chartID)
	{
		String className = new JClassName().getChartClassName(chartID);
		if(className == null)
		{
			System.out.println("ERROR CHARTID");
			return null;
		}
		else
			try {
//				System.out.println("class Name: " +className);
				//Object o1 = Class.forName("org.jfree.chart.LegendItemSource").newInstance();
				//Object o = Class.forName("com.zz.chart.obj.bar.BarChart").newInstance();
				IChart chart = (IChart)Class.forName(className).newInstance();
				return chart;
			} catch (InstantiationException e) {
				System.out.println("instantiate exception.");
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				System.out.println("illegal access.");
				e.printStackTrace();
				return null;
			} catch (ClassNotFoundException e) {
				System.out.print("class not found: " + className);
				e.printStackTrace();
				return null;
			}
	}
}
