package com.zz.chart.data;

/**
 * 
 * @author chen 
 * 				用来存储一个区域中一个自变量所对应的指标和值
 *
 */
public class IndicatorData {
	public String getDataTime() {
		return dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

	/**
	 * 专题数据中的自变量
	 */

	private String dataTime=null;//指标时间点

	public String getRegionClass() {
		return regionClass;
	}

	public void setRegionClass(String regionClass) {
		this.regionClass = regionClass;
	}

	private String regionClass=null;//指标行政级别
	private String domainAxis = null;

	public void setDomainAxis(String domainAxis) {
		this.domainAxis = domainAxis;
	}

	public void setNames(String[] names) {
		this.names = names;
	}

	public void setValues(double[] values) {
		this.values = values;
	}

	/**
	 * 专题数据各数据项的字段名称
	 */
	private String[] names = null;

	/**
	 * 专题数据各数据项的值
	 */
	private double[] values = null;

	/**
	 * 根据自变量、数据项名称和对应数值构造专题数据值对象
	 * 
	 * @param domainAxis
	 *            专题数据中的自变量
	 * @param names
	 *            数据名称数组
	 * @param values
	 *            数据值数组
	 */
	public IndicatorData(String domainAxis, String[] names, double[] values) {
		this.domainAxis = domainAxis;
		this.names = names;
		this.values = values;
	}

	/**
	 * 获取数据名称数组
	 * 
	 * @return 名称数组
	 */
	public String[] getNames() {
		return this.names;
	}

	/**
	 * 获取专题数据的自变量
	 * 
	 * @return 自变量
	 */
	public String getDomainAxis() {
		return this.domainAxis;
	}

	/**
	 * 获取专题数据各数据项的值
	 * 
	 * @return 数据值数组
	 */
	public double[] getValues() {
		return this.values;
	}

}
