package com.zz.chart.chartstyle;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 
 * @author lmk
 * @version 1.0
 * 2013.12.9
 *
 */
public class FlatChartStyle extends ChartStyle
{
	private int perValue;
	private int perLength;
	private int perWidth;
	private int perHeight;
	private double ratioLengthGap;
	private double ratioWidthGap;
	private boolean isLabel;
	
	public void Load(String chartPath) {
		SAXReader saxReader = new SAXReader();
		Document doc;
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				chartPath);
		try {
			doc = saxReader.read(is);
			Element rootElement = doc.getRootElement();
			this.perValue = Integer.parseInt(rootElement.elementText("perValue"));
			this.perLength = Integer.parseInt(rootElement.elementText("perLength"));
			this.perWidth = Integer.parseInt(rootElement.elementText("perWidth"));
			this.perHeight = Integer.parseInt(rootElement.elementText("perHeight"));
			this.ratioLengthGap = Double.parseDouble(rootElement.elementText("ratioLengthGap"));
			this.ratioWidthGap = Double.parseDouble(rootElement.elementText("ratioWidthGap"));
			this.isLabel = IntegerToBoolean(Integer.parseInt(rootElement.elementText("isLabel")));	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void Load(Element chartElement) {
		Element rootElement = chartElement;
		this.perValue = Integer.parseInt(rootElement.elementText("perValue"));
		this.perLength = Integer.parseInt(rootElement.elementText("perLength"));
		this.perWidth = Integer.parseInt(rootElement.elementText("perWidth"));
		this.perHeight = Integer.parseInt(rootElement.elementText("perHeight"));
		this.ratioLengthGap = Double.parseDouble(rootElement.elementText("ratioLengthGap"));
		this.ratioWidthGap = Double.parseDouble(rootElement.elementText("ratioWidthGap"));
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
	public int getPerValue() {
		return perValue;
	}
	public void setPerValue(int perValue) {
		this.perValue = perValue;
	}
	public int getPerLength() {
		return perLength;
	}
	public void setPerLength(int perLength) {
		this.perLength = perLength;
	}

	public int getPerHeight() {
		return perHeight;
	}
	public void setPerHeight(int perHeight) {
		this.perHeight = perHeight;
	}

	public boolean isLabel() {
		return isLabel;
	}

	public void setLabel(boolean isLabel) {
		this.isLabel = isLabel;
	}
	public double getRatioLengthGap() {
		return ratioLengthGap;
	}
	public void setRatioLengthGap(double ratioLengthGap) {
		this.ratioLengthGap = ratioLengthGap;
	}
	public double getRatioWidthGap() {
		return ratioWidthGap;
	}
	public void setRatioWidthGap(double ratioWidthGap) {
		this.ratioWidthGap = ratioWidthGap;
	}
	public int getPerWidth() {
		return perWidth;
	}
	public void setPerWidth(int perWidth) {
		this.perWidth = perWidth;
	}

}
