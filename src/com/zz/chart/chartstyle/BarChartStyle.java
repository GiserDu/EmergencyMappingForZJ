package com.zz.chart.chartstyle;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class BarChartStyle extends ChartStyle {

	public void Load(String chartPath) {
		// TODO Auto-generated constructor stub

		Document doc;

		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				"/assets/20101.xml");
		try { // 加载配置文件
			doc = new SAXReader().read(is);
			Element cRoot = doc.getRootElement();
			chartID = cRoot.elementText("chartid");

			drawBarOutLine = Boolean.parseBoolean(cRoot.element("renderer")
					.elementText("DrawBarOutline"));
			String tempString;
			tempString = cRoot.element("renderer").element("Outline")
					.elementText("Paint");
			String[] tempcolorStrings;
			int colortemp = 0;
			tempcolorStrings = tempString.split(",");
			try{
				colortemp = Integer.parseInt(tempcolorStrings[3]);
				colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[0]);
				colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[1]);
				colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[2]);
			}catch(Exception ex) {
				colortemp = 0xFF000000;
			}
			outLinePaint = colortemp;

			outLineBasicStroke = Float.parseFloat(cRoot.element("renderer")
					.element("Outline").elementText("BasicStroke"));

			minimumBarLength = Double.parseDouble(cRoot.element("renderer")
					.elementText("MinimumBarLength"));
			itemMargin = Double.parseDouble(cRoot.element("renderer")
					.elementText("ItemMargin"));
			transparent = Integer.parseInt(cRoot.elementText("Transparent"));
			isLable = Boolean.parseBoolean(cRoot.element("Lable").elementText(
					"bLable"));
			itemLabelFontName = cRoot.element("Lable").element("ItemLabelFont")
					.elementText("Name");
			itemLabelFontSize = Integer.parseInt(cRoot.element("Lable")
					.element("ItemLabelFont").elementText("Size"));
			tempString = cRoot.element("Lable").elementText("ItemLabelPaint");
			tempcolorStrings = tempString.split(",");
//			for (int j = 0; j < tempcolorStrings.length; j++) {
//				if (0 == j) {
//					colortemp = Integer
//							.parseInt(tempcolorStrings[tempcolorStrings.length
//									- j - 1]);
//				} else {
//					colortemp = Integer
//							.parseInt(tempcolorStrings[tempcolorStrings.length
//									- j - 1])
//							+ colortemp * 256;
//				}
//			}
			try{
				colortemp = Integer.parseInt(tempcolorStrings[3]);
				colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[0]);
				colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[1]);
				colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[2]);
			}catch(Exception ex) {
				colortemp = 0xFF000000;
			}
			itemLabelPaint = colortemp;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void Load(Element chartElement) {
		// TODO Auto-generated constructor stub
		Element cRoot = chartElement;

		chartID = cRoot.elementText("chartid");

		drawBarOutLine = Boolean.parseBoolean(cRoot.element("renderer")
				.elementText("DrawBarOutline"));
		String tempString;
		tempString = cRoot.element("renderer").element("Outline").elementText(
				"Paint");
		String[] tempcolorStrings;
		int colortemp = 0;
		tempcolorStrings = tempString.split(",");
//		for (int j = 0; j < tempcolorStrings.length; j++) {
//			if (0 == j) {
//				colortemp = Integer
//						.parseInt(tempcolorStrings[tempcolorStrings.length - j
//								- 1]);
//			} else {
//				colortemp = Integer
//						.parseInt(tempcolorStrings[tempcolorStrings.length - j
//								- 1])
//						+ colortemp * 256;
//			}
//		}
		try{
			colortemp = Integer.parseInt(tempcolorStrings[3]);
			colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[0]);
			colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[1]);
			colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[2]);
		}catch(Exception ex) {
			colortemp = 0xFF000000;
		}

		outLinePaint = colortemp;

		outLineBasicStroke = Float.parseFloat(cRoot.element("renderer")
				.element("Outline").elementText("BasicStroke"));

		minimumBarLength = Double.parseDouble(cRoot.element("renderer")
				.elementText("MinimumBarLength"));
		itemMargin = Double.parseDouble(cRoot.element("renderer").elementText(
				"ItemMargin"));
		transparent = Integer.parseInt(cRoot.elementText("Transparent"));
		isLable = Boolean.parseBoolean(cRoot.element("Lable").elementText(
				"bLable"));
		itemLabelFontName = cRoot.element("Lable").element("ItemLabelFont")
				.elementText("Name");
		labelFontSizemm = Float.parseFloat(cRoot.element("Lable")
				.element("ItemLabelFont").elementText("Size"));

		tempString = cRoot.element("Lable").elementText("ItemLabelPaint");
		tempcolorStrings = tempString.split(",");
//		for (int j = 0; j < tempcolorStrings.length; j++) {
//			if (0 == j) {
//				colortemp = Integer
//						.parseInt(tempcolorStrings[tempcolorStrings.length - j
//								- 1]);
//			} else {
//				colortemp = Integer
//						.parseInt(tempcolorStrings[tempcolorStrings.length - j
//								- 1])
//						+ colortemp * 256;
//			}
//		}
		try{
			colortemp = Integer.parseInt(tempcolorStrings[3]);
			colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[0]);
			colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[1]);
			colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[2]);
		}catch(Exception ex) {
			colortemp = 0xFF000000;
		}

		itemLabelPaint = colortemp;
		if(cRoot.element("IsCrack")==null){
			IsCrack = true;
		}else{
		IsCrack = Boolean.parseBoolean(cRoot.elementText("IsCrack"));
		}
		if(cRoot.element("GradientPaint")==null){
			GradientPaint = false;
		}else {
			GradientPaint = Boolean.parseBoolean(cRoot.elementText("GradientPaint"));
		}
		if(cRoot.element("xOffSet")==null){
			xOffSet=5;
		}else {
			xOffSet = Double.parseDouble(cRoot.elementText("xOffSet"));
		}
		this.setLabels(isLable);
		this.setEdges(drawBarOutLine);

	}

	public BarChartStyle save() {
		return this;
	}

}
