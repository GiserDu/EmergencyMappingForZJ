package com.zz.chart.chartstyle;

import com.zz.chart.data.IndicatorData;
import com.zz.util.JUtil;

/**
 * 制图数据参数类
 * @author QTQ
 *
 */
public class ChartDataPara {
	
	private String[] domainAxis;

	/**
	 * 自变量单位  eg：年
	 */
	private String domainAxisUnit;
	
	/**
	 * 指标对应的颜色数组  
	 */
	private int[] fieldColor;
	/**
	 * 指标名称数组
	 */
	private String[] fieldName;
	/**
	 * 组合符号分组
	 */
	//private byte[] fieldGroup;
	/**
	 * 指标单位
	 */
	private String[] fieldUnits;
	private String fieldSource;

	/**
	 * 单个区域绘图宽度
	 */
	private int width;
	/**
	 * 单个区域绘图高度
	 */
	private int height;
	/**
	 * 一个像素表示多大的值
	 */
	private double[] scales;
	
	/**
	 * 默认绘制符号的ID
	 */
	public void initial(String themeData,String yearString){
		String[] temp = themeData.split(","); 
		String[] years = yearString.split(",");
 //		String table = temp[0];//表名
		String[] fn = new String[temp.length-1];
		String[] fu = new String[temp.length-1];
		String tempSource=null;
		for (int i = 0; i < temp.length-1; i++) {
			String cnNameAndUnit = null;
			cnNameAndUnit = JUtil.getCnNameAndUnit(temp[i+1]);
			fn[i] = cnNameAndUnit.split(",")[0];  
			fu[i] = cnNameAndUnit.split(",")[1];
			tempSource = cnNameAndUnit.split(",")[2];
		}
		this.setFieldName(fn); //指标名
		this.setFieldUnits(fu);//单位
		this.setFieldSource(tempSource);//单位
		this.setDomainAxis(years);
		this.setDomainAxisUnit("年");
	}


	public void initialAsAPI(IndicatorData[] indicatorDatas){
//		String[] temp = themeData.split(",");
//		String[] years = yearString.split(",");
//		//		String table = temp[0];//表名
//		String[] fn = new String[temp.length-1];
//		String[] fu = new String[temp.length-1];
//		String tempSource=null;
//		for (int i = 0; i < temp.length-1; i++) {
//			String cnNameAndUnit = null;
//			cnNameAndUnit = JUtil.getCnNameAndUnit(temp[i+1]);
//			fn[i] = cnNameAndUnit.split(",")[0];
//			fu[i] = cnNameAndUnit.split(",")[1];
//			tempSource = cnNameAndUnit.split(",")[2];
//		}
		String[] fn=indicatorDatas[0].getNames();
		String[] fu={""};
		String tempSource=null;
		String[] years=null;
		this.setFieldName(fn); //指标名
		this.setFieldUnits(fu);//单位
		this.setFieldSource(tempSource);//单位
		this.setDomainAxis(years);
		this.setDomainAxisUnit("年");
	}


	public String[] getDomainAxis() {
		return domainAxis;
	}

	public void setDomainAxis(String[] domainAxis) {
		this.domainAxis = domainAxis;
	}

	public String getDomainAxisUnit() {
		return domainAxisUnit;
	}

	public void setDomainAxisUnit(String domainAxisUnit) {
		this.domainAxisUnit = domainAxisUnit;
	}

	public int[] getFieldColor() {
		return fieldColor;
	}

	public void setFieldColor(int[] fieldColor) {
		this.fieldColor = fieldColor;
	}

	public String[] getFieldName() {
		return fieldName;
	}

	public void setFieldName(String[] fieldName) {
		this.fieldName = fieldName;
	}

	public void setFieldSource(String fieldSource) {
		this.fieldSource = fieldSource;
	}
	public String getFieldSource() {
		return fieldSource;
	}
//	public byte[] getFieldGroup() {
//		return fieldGroup;
//	}
//
//	public void setFieldGroup(byte[] fieldGroup) {
//		this.fieldGroup = fieldGroup;
//	}

	public String[] getFieldUnits() {
		return fieldUnits;
	}

	public void setFieldUnits(String[] fieldUnits) {
		this.fieldUnits = fieldUnits;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double[] getScales() {
		return scales;
	}

	public void setScales(double[] scales) {
		this.scales = scales;
	}

}

