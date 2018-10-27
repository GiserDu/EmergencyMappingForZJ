package com.zz.chart.chartstyle;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


/**
 * 自定义符号样式
 * 
 * @author lmk
 * @version 1.0
 */
public class CustomChartStyle extends ChartStyle {
	
	/**
	 * 
	 * @param chartPath
	 */
	public void Load(String chartPath) {
		SAXReader saxReader = new SAXReader();
		Document doc;
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				chartPath);
		try {
			doc = saxReader.read(is);
			Element rootElement = doc.getRootElement();
			this.path = rootElement.elementText("path");
			this.hGap = Integer.parseInt(rootElement.elementText("hGap"));
			this.vGap = Integer.parseInt(rootElement.elementText("vGap"));
			this.arrange = Integer.parseInt(rootElement.elementText("arrange"));
			this.perValue = Integer.parseInt(rootElement
					.elementText("perValue"));
			this.perWidth = Integer.parseInt(rootElement
					.elementText("perWidth"));
			this.perHeight = Integer.parseInt(rootElement
					.elementText("perHeight"));
			this.isLabel = IntegerToBoolean(Integer.parseInt(rootElement.elementText("isLabel")));	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Load(Element chartElement) {

		Element rootElement = chartElement;
		this.path = rootElement.elementText("path");
		this.hGap = Integer.parseInt(rootElement.elementText("hGap"));
		this.vGap = Integer.parseInt(rootElement.elementText("vGap"));
		this.arrange = Integer.parseInt(rootElement.elementText("arrange"));
		this.perValue = Integer.parseInt(rootElement.elementText("perValue"));
		this.perWidth = Integer.parseInt(rootElement.elementText("perWidth"));
		this.perHeight = Integer.parseInt(rootElement.elementText("perHeight"));
		this.isLabel = IntegerToBoolean(Integer.parseInt(rootElement.elementText("isLabel")));	
		this.setLabels(isLabel);
		lineItemLabelFontName = rootElement.element("Lable").element("ItemLabelFont")
		.elementText("Name");
		labelFontSizemm = Float.parseFloat(rootElement.element("Lable")
		.element("ItemLabelFont").elementText("Size"));
		String tempString;
		String[] tempcolorStrings;
		int colortemp;
		tempString = rootElement.element("Lable").elementText("ItemLabelPaint");
		tempcolorStrings = tempString.split(",");
		try {
			colortemp = Integer.parseInt(tempcolorStrings[3]);
			colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[0]);
			colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[1]);
			colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[2]);
		} catch (Exception ex) {
			colortemp = 0xFF000000;
		}
		customItemLabelPaint = colortemp;
	}
	private boolean IntegerToBoolean(int a)
	{
		if(a == 0)
			return false;
		else
			return true;
	}
	/**
	 * 
	 * @return String
	 */
	public String getPath() {
		return path;
	}

	/**
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 
	 * @return int
	 */
	public int gethGap() {
		return hGap;
	}

	/**
	 * 
	 * @param hGap
	 */
	public void sethGap(int hGap) {
		this.hGap = hGap;
	}

	/**
	 * 
	 * @return int
	 */
	public int getvGap() {
		return vGap;
	}

	/**
	 * 
	 * @param vGap
	 */
	public void setvGap(int vGap) {
		this.vGap = vGap;
	}

	/**
	 * 
	 * @return int
	 */
	public int getArrange() {
		return arrange;
	}

	/**
	 * 
	 * @param arrange
	 */
	public void setArrange(int arrange) {
		this.arrange = arrange;
	}

	/**
	 * 
	 * @return int
	 */
	public int getPerValue() {
		return perValue;
	}

	/**
	 * 
	 * @param perValue
	 */
	public void setPerValue(int perValue) {
		this.perValue = perValue;
	}

	/**
	 * 
	 * @return int
	 */
	public int getPerWidth() {
		return perWidth;
	}

	/**
	 * 
	 * @param perWidth
	 */
	public void setPerWidth(int perWidth) {
		this.perWidth = perWidth;
	}

	/**
	 * 
	 * @return int
	 */
	public int getPerHeight() {
		return perHeight;
	}

	/**
	 * 
	 * @param perHeight
	 */
	public void setPerHeight(int perHeight) {
		this.perHeight = perHeight;
	}
}