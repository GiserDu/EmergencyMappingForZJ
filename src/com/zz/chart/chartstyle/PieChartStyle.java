package com.zz.chart.chartstyle;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class PieChartStyle extends ChartStyle {

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
				BackgroundAlpha = Float.parseFloat(cRoot.element("Plot")
						.elementText("BackgroundAlpha"));
				ForegroundAlpha = Float.parseFloat(cRoot.element("Plot")
						.elementText("ForegroundAlpha"));
				OutlineVisble = Boolean.parseBoolean(cRoot.element("Plot")
						.elementText("OutlineVisble"));
				SectionOutlinesVisible = Boolean.parseBoolean(cRoot.element(
						"Plot").elementText("SectionOutlinesVisible"));
				String tempString;
				tempString = cRoot.element("Plot").elementText(
						"BaseSectionOutlinePaint");
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
				BaseSectionOutlinePaint = colortemp;
				BaseSectionOutlineStroke = Float.parseFloat(cRoot.element(
						"Plot").elementText("BaseSectionOutlineStroke"));
				Circular = Boolean.parseBoolean(cRoot.element("Plot")
						.elementText("Circular"));
				StartAngle = Double.parseDouble(cRoot.element("Plot")
						.elementText("StartAngle"));
				LabelLinkMargin = Double.parseDouble(cRoot.element("Plot")
						.element("Label").elementText("LabelLinkMargin"));
				LabelGenerator = cRoot.element("Plot").element("Label")
						.elementText("LabelGenerator");
				LabelGap = Double.parseDouble(cRoot.element("Plot").element(
						"Label").elementText("LabelGap"));
				labelFontSizemm = Float.parseFloat(cRoot.element("Plot")
						.element("Label").elementText("LabelFontSize"));
				LabelFontName = cRoot.element("Plot").element("Label")
						.elementText("LabelFontName");
				tempString = cRoot.element("Plot").element("Label")
						.elementText("LabelFontPaint");
				tempcolorStrings = tempString.split(",");
				try{
					colortemp = Integer.parseInt(tempcolorStrings[3]);
					colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[0]);
					colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[1]);
					colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[2]);
				}catch(Exception ex) {
					colortemp = 0xFF000000;
				}
				LabelFontPaint = colortemp;
				bLabel = Boolean.parseBoolean(cRoot.element("Plot").element(
						"Label").elementText("bLabel"));
//			   TiltAngle = Double.parseDouble(cRoot.element("Plot").elementText("TiltAngle"));
				if (cRoot.element("Plot").element("TiltAngle")==null) {
					TiltAngle = 0.75;
				}else{
				 TiltAngle = Double.parseDouble(cRoot.element("Plot").elementText("TiltAngle"));
				}
				if(cRoot.element("Plot").element("DepthFactor")==null){
					DepthFactor = 0.175;
				}else{
				 DepthFactor = Double.parseDouble(cRoot.element("Plot").elementText("DepthFactor"));
				}
				if(cRoot.element("Plot").element("SectionDepth")==null){
					SectionDepth = 0.5;
				}else{
				 SectionDepth = Double.parseDouble(cRoot.element("Plot").elementText("SectionDepth"));
				}

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
		BackgroundAlpha = Float.parseFloat(cRoot.element("Plot").elementText(
				"BackgroundAlpha"));
		ForegroundAlpha = Float.parseFloat(cRoot.element("Plot").elementText(
				"ForegroundAlpha"));
		OutlineVisble = Boolean.parseBoolean(cRoot.element("Plot").elementText(
				"OutlineVisble"));
		SectionOutlinesVisible = Boolean.parseBoolean(cRoot.element("Plot")
				.elementText("SectionOutlinesVisible"));
		String tempString;
		tempString = cRoot.element("Plot").elementText(
				"BaseSectionOutlinePaint");
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
		BaseSectionOutlinePaint = colortemp;
		BaseSectionOutlineStroke = Float.parseFloat(cRoot.element("Plot")
				.elementText("BaseSectionOutlineStroke"));
		Circular = Boolean.parseBoolean(cRoot.element("Plot").elementText(
				"Circular"));
		StartAngle = Double.parseDouble(cRoot.element("Plot").elementText(
				"StartAngle"));
		LabelLinkMargin = Double.parseDouble(cRoot.element("Plot").element(
				"Label").elementText("LabelLinkMargin"));
		LabelGenerator = cRoot.element("Plot").element("Label").elementText(
				"LabelGenerator");
		LabelGap = Double.parseDouble(cRoot.element("Plot").element("Label")
				.elementText("LabelGap"));
		labelFontSizemm = Float.parseFloat(cRoot.element("Plot").element(
				"Label").elementText("LabelFontSize"));
		LabelFontName = cRoot.element("Plot").element("Label").elementText(
				"LabelFontName");
		tempString = cRoot.element("Plot").element("Label").elementText(
				"LabelFontPaint");
		tempcolorStrings = tempString.split(",");
		try{
			colortemp = Integer.parseInt(tempcolorStrings[3]);
			colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[0]);
			colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[1]);
			colortemp = colortemp * 256 + Integer.parseInt(tempcolorStrings[2]);
		}catch(Exception ex) {
			colortemp = 0xFF000000;
		}
		LabelFontPaint = colortemp;
		bLabel = Boolean.parseBoolean(cRoot.element("Plot").element("Label")
				.elementText("bLabel"));
		this.setLabels(bLabel);
		if (cRoot.element("Plot").element("TiltAngle")==null) {
			TiltAngle = 0.75;
		}else{
		 TiltAngle = Double.parseDouble(cRoot.element("Plot").elementText("TiltAngle"));
		}
		if(cRoot.element("Plot").element("DepthFactor")==null){
			DepthFactor = 0.175;
		}else{
		 DepthFactor = Double.parseDouble(cRoot.element("Plot").elementText("DepthFactor"));
		}
		if(cRoot.element("Plot").element("SectionDepth")==null){
			SectionDepth = 0.5;
		}else{
		 SectionDepth = Double.parseDouble(cRoot.element("Plot").elementText("SectionDepth"));
		}
	}
		
}
