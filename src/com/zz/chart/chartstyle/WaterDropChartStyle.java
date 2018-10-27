package com.zz.chart.chartstyle;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.awt.*;
import java.io.InputStream;

/**
 * 
 * @author lmk
 * @version 1.0
 * 2013.12.9
 */
public class WaterDropChartStyle extends ChartStyle
{
	private Color colorOutFill;
	private Color colorOutDraw;
	private Color colorCenter;
	private double ratioOffset;
	private double ratioInMax;
	private double ratioCenterOffset;
	private double ratioUpDownUnitValue;
	private boolean isLabel;
	public void Load(String chartPath) {
		SAXReader saxReader = new SAXReader();
		Document doc;
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				chartPath);
		try {
			doc = saxReader.read(is);
			Element rootElement = doc.getRootElement();
			this.colorOutFill = stringToColor(rootElement.elementText("colorOutFill"));
			this.colorOutDraw = stringToColor(rootElement.elementText("colorOutDraw"));
			this.colorCenter = stringToColor(rootElement.elementText("colorCenter"));
			this.ratioOffset = Double.parseDouble(rootElement.elementText("ratioOffset"));
			this.ratioInMax = Double.parseDouble(rootElement.elementText("ratioInMax"));
			this.ratioCenterOffset = Double.parseDouble(rootElement.elementText("ratioCenterOffset"));
			//lmk 2014.2.16
			this.ratioUpDownUnitValue = Double.parseDouble(rootElement.elementText("ratioUpDownUnitValue"));
			this.isLabel = IntegerToBoolean(Integer.parseInt(rootElement.elementText("isLabel")));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Load(Element chartElement) {
		Element rootElement = chartElement;
		this.colorOutFill = stringToColor(rootElement.elementText("colorOutFill"));
		this.colorOutDraw = stringToColor(rootElement.elementText("colorOutDraw"));
		this.colorCenter = stringToColor(rootElement.elementText("colorCenter"));
		this.ratioOffset = Double.parseDouble(rootElement.elementText("ratioOffset"));
		this.ratioInMax = Double.parseDouble(rootElement.elementText("ratioInMax"));
		this.ratioCenterOffset = Double.parseDouble(rootElement.elementText("ratioCenterOffset"));
		this.ratioUpDownUnitValue = Double.parseDouble(rootElement.elementText("ratioUpDownUnitValue"));
		//lmk 2014.2.16
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
	
	private Color stringToColor(String colorString)
	{
		String[] curStrings = colorString.split(",");
		int r = Integer.parseInt(curStrings[0]);
		int g = Integer.parseInt(curStrings[1]);
		int b = Integer.parseInt(curStrings[2]);
		Color curColor = new Color(r,g,b);
		return curColor;
	}
	private boolean IntegerToBoolean(int a)
	{
		if(a == 0)
			return false;
		else
			return true;
	}
	public Color getColorOutFill() {
		return colorOutFill;
	}
	public void setColorOutFill(Color colorOutFill) {
		this.colorOutFill = colorOutFill;
	}
	public Color getColorOutDraw() {
		return colorOutDraw;
	}
	public void setColorOutDraw(Color colorOutDraw) {
		this.colorOutDraw = colorOutDraw;
	}
	public Color getColorCenter() {
		return colorCenter;
	}
	public void setColorCenter(Color colorCenter) {
		this.colorCenter = colorCenter;
	}

	public double getRatioOffset() {
		return ratioOffset;
	}

	public void setRatioOffset(double ratioOffset) {
		this.ratioOffset = ratioOffset;
	}

	public double getRatioInMax() {
		return ratioInMax;
	}

	public void setRatioInMax(double ratioInMax) {
		this.ratioInMax = ratioInMax;
	}

	public double getRatioCenterOffset() {
		return ratioCenterOffset;
	}

	public void setRatioCenterOffset(double ratioCenterOffset) {
		this.ratioCenterOffset = ratioCenterOffset;
	}

	public double getRatioUpDownUnitValue() {
		return ratioUpDownUnitValue;
	}

	public void setRatioUpDownUnitValue(double ratioUpDownUnitValue) {
		this.ratioUpDownUnitValue = ratioUpDownUnitValue;
	}

	public boolean isLabel() {
		return isLabel;
	}

	public void setLabel(boolean isLabel) {
		this.isLabel = isLabel;
	}
}
