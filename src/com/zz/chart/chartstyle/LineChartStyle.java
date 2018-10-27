package com.zz.chart.chartstyle;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


//import org.w3c.dom.Document;

public class LineChartStyle extends ChartStyle {

	public void Load(String xmlPath) {

		SAXReader reader = new SAXReader();
		Document doc;
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				xmlPath);

		try {

			doc = reader.read(is);
			Element cRoot = doc.getRootElement();

			chartID = cRoot.elementText("chartid");
			BaseShapesVisible = Boolean.parseBoolean(cRoot.element("renderer")
					.elementText("BaseShapesVisible"));
			DrawOutlines = Boolean.parseBoolean(cRoot.element("renderer")
					.elementText("DrawOutline"));
			UseFillPaint = Boolean.parseBoolean(cRoot.element("renderer")
					.elementText("UseFillPaint"));
			BaseFillPaint = cRoot.element("renderer").elementText(
					"BaseFillPaint");
			Stroke = Float.parseFloat(cRoot.element("renderer").elementText(
					"Stroke"));
			OutlineStroke = Float.parseFloat(cRoot.element("renderer")
					.elementText("OutlineStroke"));
			BaseAreaAlpha = Float.parseFloat(cRoot.element("renderer")
					.elementText("BaseAreaAlpha"));
			lineLable = Boolean.parseBoolean(cRoot.element("Lable")
					.elementText("bLable"));
			lineItemLabelFontName = cRoot.element("Lable").element(
					"ItemLabelFont").elementText("Name");
			lineItemLabelFontSize = Integer.parseInt(cRoot.element("Lable")
					.element("ItemLabelFont").elementText("Size"));
			String tempString;
			String[] tempcolorStrings;
			int colortemp;
			tempString = cRoot.element("Lable").elementText("ItemLabelPaint");
			tempcolorStrings = tempString.split(",");
			try {
				colortemp = Integer.parseInt(tempcolorStrings[3]);
				colortemp = colortemp * 256
						+ Integer.parseInt(tempcolorStrings[0]);
				colortemp = colortemp * 256
						+ Integer.parseInt(tempcolorStrings[1]);
				colortemp = colortemp * 256
						+ Integer.parseInt(tempcolorStrings[2]);
			} catch (Exception ex) {
				colortemp = 0xFF000000;
			}
			lineItemLabelPaint = colortemp;
			rangAxisVisible = Boolean.parseBoolean(cRoot.element("rangAxis")
					.elementText("visible"));
			rTickLabelsVisible = Boolean.parseBoolean(cRoot.element("rangAxis")
					.elementText("TickLabelsVisible"));
			domainAxisVisible = Boolean.parseBoolean(cRoot
					.element("domainAxis").elementText("visible"));
			dTickLabelsVisible = Boolean.parseBoolean(cRoot.element(
					"domainAxis").elementText("TickLabelsVisible"));
			LowerMargin = Integer.parseInt(cRoot.element("domainAxis")
					.elementText("LowerMargin"));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void Load(Element chartElement) {

		Element cRoot = chartElement;

		chartID = cRoot.elementText("chartid");
		BaseShapesVisible = Boolean.parseBoolean(cRoot.element("renderer")
				.elementText("BaseShapesVisible"));
		DrawOutlines = Boolean.parseBoolean(cRoot.element("renderer")
				.elementText("DrawOutline"));
		UseFillPaint = Boolean.parseBoolean(cRoot.element("renderer")
				.elementText("UseFillPaint"));
		BaseFillPaint = cRoot.element("renderer").elementText("BaseFillPaint");
		Stroke = Float.parseFloat(cRoot.element("renderer").elementText(
				"Stroke"));
		OutlineStroke = Float.parseFloat(cRoot.element("renderer").elementText(
				"OutlineStroke"));
		BaseAreaAlpha = Float.parseFloat(cRoot.element("renderer").elementText(
				"BaseAreaAlpha"));

		if (cRoot.element("Lable") != null) {
			lineLable = Boolean.parseBoolean(cRoot.element("Lable")
					.elementText("bLable"));
			lineItemLabelFontName = cRoot.element("Lable").element(
					"ItemLabelFont").elementText("Name");
			labelFontSizemm = Float.parseFloat(cRoot.element("Lable").element(
					"ItemLabelFont").elementText("Size"));
			String tempString;
			String[] tempcolorStrings;
			int colortemp;
			tempString = cRoot.element("Lable").elementText("ItemLabelPaint");
			tempcolorStrings = tempString.split(",");
			try {
				colortemp = Integer.parseInt(tempcolorStrings[3]);
				colortemp = colortemp * 256
						+ Integer.parseInt(tempcolorStrings[0]);
				colortemp = colortemp * 256
						+ Integer.parseInt(tempcolorStrings[1]);
				colortemp = colortemp * 256
						+ Integer.parseInt(tempcolorStrings[2]);
			} catch (Exception ex) {
				colortemp = 0xFF000000;
			}
			lineItemLabelPaint = colortemp;
		} else {
			lineLable = false;
			lineItemLabelFontName = "Dialog";
			labelFontSizemm = 2;
			lineItemLabelPaint = 0xFF000000;
		}

		rangAxisVisible = Boolean.parseBoolean(cRoot.element("rangAxis")
				.elementText("visible"));
		rTickLabelsVisible = Boolean.parseBoolean(cRoot.element("rangAxis")
				.elementText("TickLabelsVisible"));
		domainAxisVisible = Boolean.parseBoolean(cRoot.element("domainAxis")
				.elementText("visible"));
		dTickLabelsVisible = Boolean.parseBoolean(cRoot.element("domainAxis")
				.elementText("TickLabelsVisible"));
		LowerMargin = Integer.parseInt(cRoot.element("domainAxis").elementText(
				"LowerMargin"));
		this.setLabels(lineLable);

	}

	public boolean isBaseShapesVisible() {
		return BaseShapesVisible;
	}

	public void setBaseShapesVisible(boolean baseShapesVisible) {
		BaseShapesVisible = baseShapesVisible;
	}

	public boolean isDrawOutlines() {
		return DrawOutlines;
	}

	public void setDrawOutlines(boolean drawOutlines) {
		DrawOutlines = drawOutlines;
	}

	public boolean isUseFillPaint() {
		return UseFillPaint;
	}

	public void setUseFillPaint(boolean useFillPaint) {
		UseFillPaint = useFillPaint;
	}

	public String getBaseFillPaint() {
		return BaseFillPaint;
	}

	public void setBaseFillPaint(String baseFillPaint) {
		BaseFillPaint = baseFillPaint;
	}

	public float getOutlineStroke() {
		return OutlineStroke;
	}

	public void setOutlineStroke(float outlineStroke) {
		OutlineStroke = outlineStroke;
	}

	public float getStroke() {
		return Stroke;
	}

	public void setStroke(float stroke) {
		Stroke = stroke;
	}

	public float getBaseAreaAlpha() {
		return BaseAreaAlpha;
	}

	public void setBaseAreaAlpha(float baseAreaAlpha) {
		BaseAreaAlpha = baseAreaAlpha;
	}

	public boolean isRangAxisVisible() {
		return rangAxisVisible;
	}

	public void setRangAxisVisible(boolean rangAxisVisible) {
		this.rangAxisVisible = rangAxisVisible;
	}

	public boolean isDomainAxisVisible() {
		return domainAxisVisible;
	}

	public void setDomainAxisVisible(boolean domainAxisVisible) {
		this.domainAxisVisible = domainAxisVisible;
	}

	public boolean isrTickLabelsVisible() {
		return rTickLabelsVisible;
	}

	public void setrTickLabelsVisible(boolean rTickLabelsVisible) {
		this.rTickLabelsVisible = rTickLabelsVisible;
	}

	public boolean isdTickLabelsVisible() {
		return dTickLabelsVisible;
	}

	public void setdTickLabelsVisible(boolean dTickLabelsVisible) {
		this.dTickLabelsVisible = dTickLabelsVisible;
	}

	public int getLowerMargin() {
		return LowerMargin;
	}

	public void setLowerMargin(int lowerMargin) {
		LowerMargin = lowerMargin;
	}

}
