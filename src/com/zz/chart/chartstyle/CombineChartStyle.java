package com.zz.chart.chartstyle;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class CombineChartStyle extends ChartStyle {
	public void Load(String chartPath) {
		// TODO Auto-generated constructor stub

		try {
			Document doc;
			InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream(chartPath);
			try { // 加载配置文件
				doc = new SAXReader().read(is);
				Element cRoot = doc.getRootElement();
				chartID = cRoot.elementText("chartid");
				BackgroundAlphas = Float.parseFloat(cRoot.elementText("BackgroundAlphas"));
				ForegroundAlphas = Float.parseFloat(cRoot.elementText("ForegroundAlphas"));
				OutlinesVisble = Boolean.parseBoolean(cRoot.elementText("OutlinesVisble"));
				OutLines = Boolean.parseBoolean(cRoot.element("renderer")
						.elementText("Outlines"));
				String tempString;
				tempString = cRoot.element("renderer").element("Outline").elementText(
						"Paints");
				String[] tempcolorStrings;
				int colortemp = 0;
				tempcolorStrings = tempString.split(",");
//				for (int j = 0; j < tempcolorStrings.length; j++) {
//					if (0 == j) {
//						colortemp = Integer
//								.parseInt(tempcolorStrings[tempcolorStrings.length - j
//										- 1]);
//					} else {
//						colortemp = Integer
//								.parseInt(tempcolorStrings[tempcolorStrings.length - j
//										- 1])
//								+ colortemp * 256;
//					}
//				}
				try{
					colortemp = Integer.parseInt(tempcolorStrings[3]);
					colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[0]);
					colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[1]);
					colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[2]);
				}catch(Exception ex) {
					colortemp = 0xFF000000;
				}

				outLinePaints = colortemp;

				outLineBasicStrokes = Float.parseFloat(cRoot.element("renderer")
						.element("Outline").elementText("BasicStrokes"));

//				minimumBarLength = Double.parseDouble(cRoot.element("renderer")
//						.elementText("MinimumBarLength"));
//				itemMargin = Double.parseDouble(cRoot.element("renderer").elementText(
//						"ItemMargin"));
//				transparent = Integer.parseInt(cRoot.elementText("Transparent"));
				isLables = Boolean.parseBoolean(cRoot.element("Lable").elementText(
						"bLables"));
				itemLabelFontNames = cRoot.element("Lable").element("ItemLabelFonts")
						.elementText("Names");
				labelFontSizemm = Float.parseFloat(cRoot.element("Lable")
						.element("ItemLabelFonts").elementText("Sizes"));

				tempString = cRoot.element("Lable").elementText("ItemLabelPaints");
				tempcolorStrings = tempString.split(",");
//				for (int j = 0; j < tempcolorStrings.length; j++) {
//					if (0 == j) {
//						colortemp = Integer
//								.parseInt(tempcolorStrings[tempcolorStrings.length - j
//										- 1]);
//					} else {
//						colortemp = Integer
//								.parseInt(tempcolorStrings[tempcolorStrings.length - j
//										- 1])
//								+ colortemp * 256;
//					}
//				}
				try{
					colortemp = Integer.parseInt(tempcolorStrings[3]);
					colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[0]);
					colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[1]);
					colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[2]);
				}catch(Exception ex) {
					colortemp = 0xFF000000;
				}

				itemLabelPaints = colortemp;
				this.setLabels(isLables);

			

			} catch (Exception e2) {
				e2.printStackTrace();
			}

		} catch (Exception e3) {
			// TODO: handle exception
			e3.printStackTrace();

		}
	}
	public void Load(Element chartElement) {
		// TODO Auto-generated constructor stub
		Element cRoot = chartElement;

		chartID = cRoot.elementText("chartid");
		BackgroundAlphas = Float.parseFloat(cRoot.elementText("BackgroundAlphas"));
		ForegroundAlphas = Float.parseFloat(cRoot.elementText("ForegroundAlphas"));
		OutlinesVisble = Boolean.parseBoolean(cRoot.elementText("OutlinesVisble"));
		OutLines = Boolean.parseBoolean(cRoot.element("renderer")
				.elementText("Outlines"));
		String tempString;
		tempString = cRoot.element("renderer").element("Outline").elementText(
				"Paints");
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

		outLinePaints = colortemp;

		outLineBasicStrokes = Float.parseFloat(cRoot.element("renderer")
				.element("Outline").elementText("BasicStrokes"));

//		minimumBarLength = Double.parseDouble(cRoot.element("renderer")
//				.elementText("MinimumBarLength"));
//		itemMargin = Double.parseDouble(cRoot.element("renderer").elementText(
//				"ItemMargin"));
//		transparent = Integer.parseInt(cRoot.elementText("Transparent"));
		isLables = Boolean.parseBoolean(cRoot.element("Lable").elementText(
				"bLables"));
		itemLabelFontNames = cRoot.element("Lable").element("ItemLabelFonts")
				.elementText("Names");
		labelFontSizemm = Float.parseFloat(cRoot.element("Lable")
				.element("ItemLabelFonts").elementText("Sizes"));

		tempString = cRoot.element("Lable").elementText("ItemLabelPaints");
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

		itemLabelPaints = colortemp;
		this.setLabels(isLables);

	}

	public CombineChartStyle save() {
		return this;
	}

}
