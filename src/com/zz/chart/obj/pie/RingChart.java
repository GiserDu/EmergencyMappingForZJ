package com.zz.chart.obj.pie;

import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.render.pie.TRingPlot;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.Size2D;
import org.jfree.util.Rotation;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.HashMap;

public class RingChart implements IChart {

	@SuppressWarnings("deprecation")
	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub

		//获取饼图所需要的数据
		DefaultPieDataset pieDataset= new DefaultPieDataset();

		for (int i = 0; i < indicatorDatas[0].getNames().length; i++) {
			pieDataset.setValue(thematicRendition.getFieldName()[i], indicatorDatas[0].getValues()[i]);
		}
		
		//创建饼图
		TRingPlot pieplot = new TRingPlot(pieDataset);	
		JFreeChart pieChart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
				pieplot, false);	
		ChartUtilities.applyCurrentTheme(pieChart);	
		pieChart.setBackgroundPaint(null);
		
		// 获得图表对象的引用  
		TRingPlot plot = (TRingPlot) pieChart.getPlot();
		
		//获取饼图绘制样式
		float BackgroundAlpha = chartStyle.getBackgroundAlpha();
		float ForegroundAlpha = chartStyle.getForegroundAlpha();
		boolean OutlineVisible = chartStyle.isOutlineVisble();
		boolean SectionOutlinesVisible = chartStyle.isSectionOutlinesVisible();
		float BaseSectionOutlineStroke = chartStyle.getBaseSectionOutlineStroke();
		int BaseSectionOutlinePaint = chartStyle.getBaseSectionOutlinePaint();
		boolean Circular = chartStyle.isCircular();
		double StartAngle = chartStyle.getStartAngle();
//		double LabelGap = chartStyle.getLabelGap();
		boolean isLabel = chartStyle.isbLabel();
		int LabelFontSize = chartStyle.getLabelFontSize();
		String labelFontName = chartStyle.getLabelFontName();
		double SectionDepth = chartStyle.getSectionDepth();
		double LabelLinkMargin = chartStyle.getLabelLinkMargin();
		//设置饼图颜色

		for(int i=0;i<thematicRendition.getFieldName().length;i++){
			plot.setSectionPaint(i, new Color(thematicRendition.getFieldColor()[i],false));
		}
		//设置饼图样式
		plot.setBackgroundAlpha(BackgroundAlpha);
		plot.setForegroundAlpha(ForegroundAlpha);
		plot.setSectionOutlinesVisible(SectionOutlinesVisible);
		plot.setOutlineVisible(OutlineVisible);
		if(SectionOutlinesVisible){
			BasicStroke basicStroke = new BasicStroke(
					BaseSectionOutlineStroke);
			plot.setBaseSectionOutlineStroke(basicStroke);
			plot.setBaseSectionOutlinePaint(new Color(BaseSectionOutlinePaint,false));
			}
		plot.setShadowPaint(null);
		plot.setCircular(Circular);
		plot.setDirection(Rotation.ANTICLOCKWISE);
		plot.setStartAngle(StartAngle);
		plot.setIgnoreNullValues(true);
		plot.setIgnoreZeroValues(true);
		plot.setNoDataMessage("");          
        plot.setNoDataMessageFont(new Font("宋体", Font.ITALIC, 20)); // 设置没有数据时显示的信息的字体         
        plot.setNoDataMessagePaint(Color.orange); // 设置没有数据时显示的信息的颜色  
        
        //设置饼图大小
        double sum = 0;
        for(int i=0;i<indicatorDatas[0].getValues().length;i++){
        	sum = sum + indicatorDatas[0].getValues()[i];
        }
        double pieRadius = sum*1.0/width/thematicRendition.getScales()[0];
        pieRadius = Math.sqrt(pieRadius);
        double interiorGap = 0.4 - pieRadius / 3;
        if(interiorGap<0.425){
        	plot.setInteriorGap(interiorGap);
        }else {
			plot.setInteriorGap(interiorGap);
		}
		
        //环状饼图特
        plot.setSectionDepth(SectionDepth);
        plot.setLabelGenerator(null);
		//绘制饼图
        ChartRenderingInfo info = new 
        		ChartRenderingInfo(new StandardEntityCollection());
	
		Rectangle2D.Double symbolEnvlope =  new 
				Rectangle2D.Double( x-width/2 ,y-width/2 , width,width); 

		pieChart.draw(g, symbolEnvlope, info);
		//绘制标签
		//绘制标签
		if(isLabel){
			g.setColor(Color.black);
			Font font = new Font(labelFontName, Font.PLAIN,LabelFontSize);
			g.setFont(font);//设置字体
			double stringlength = 0;
			@SuppressWarnings("unused")
			double stringheight = 0 ; //指标对应的值得字符串长高
//			double Rmax = width/2;
			double R = width/2*(1-interiorGap*2);//饼图的半径 
			double Angle = 0;//指标对应的角度
			double HAngle = 0;//图例绘制在指标对应角度的1/2处
			double Angle1 = 0;
			//饼图占制图区域大于一半时，标签绘制在饼图上
				for(int i=0;i<indicatorDatas[0].getValues().length;i++){
					FontMetrics fm = g.getFontMetrics();  
			            Rectangle2D rec=fm.getStringBounds(Float.toString((float)indicatorDatas[0].getValues()[i]), g); 
					
			//		String value = Double.toString(indicatorDatas[0].getValues()[i]);
					stringlength = rec.getWidth();//字符串的长度
					stringheight = rec.getHeight();	
					g.setStroke(new BasicStroke(1f));
					if(i==0){
						//绘制第一个值得标注，分为四种种情况，标注绘制在终边的一半上
						Angle = indicatorDatas[0].getValues()[i]/sum*360;
						HAngle = Angle/2;
						int labelx = (int)(x+R/4*3*Math.cos(HAngle*Math.PI/180));
					    int labely = (int)(y-R/4*3*Math.sin(HAngle*Math.PI/180));
						if(HAngle<=90){
							g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), 
									(int) (labelx+LabelLinkMargin*R),(int) (labely));
							g.drawLine(labelx, labely, (int) (labelx+LabelLinkMargin*R), labely);
						
						}else if (HAngle>90&&HAngle<=180) {
							g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), 
									(int) (labelx-LabelLinkMargin*R-stringlength),(int) (labely));
							g.drawLine(labelx, labely, (int) (labelx-LabelLinkMargin*R), labely);
						}
						//绘制中间的角度
					}else if(i>0&&i<indicatorDatas[0].getValues().length-1){
						Angle = indicatorDatas[0].getValues()[i]/sum*360;
						Angle1 = Angle1 + indicatorDatas[0].getValues()[i-1]/sum*360;
						HAngle = Angle1 + Angle/2;
						int labelx = (int)(x+R/4*3*Math.cos(HAngle*Math.PI/180));
					    int labely = (int)(y-R/4*3*Math.sin(HAngle*Math.PI/180));
						if(HAngle<=60){
							g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), 
									(int) (labelx+LabelLinkMargin*R),(int) (labely));
							g.drawLine(labelx, labely, (int) (labelx+LabelLinkMargin*R), labely);
						
						}else if(HAngle>60&&HAngle<90){
							g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), 
									(int) (labelx+R/3+LabelLinkMargin*R), (int) (labely-R/3));
							g.drawLine(labelx, labely, (int) (labelx+R/3), (int) (labely-R/3));
							g.drawLine((int) (labelx+R/3), (int) (labely-R/3), (int) (labelx+LabelLinkMargin*R/3+R), (int) (labely-R/3));
						
						}else if (HAngle==90){
							g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), 
									(int) (labelx+LabelLinkMargin*R), (int) (labely-R/2));
							g.drawLine(labelx, labely, (int) (labelx), (int) (labely-R/2));
							g.drawLine((int) (labelx), (int) (labely-R/2), (int) (labelx+LabelLinkMargin*R), (int) (labely-R/2));
				
						}else if (HAngle>90&&HAngle<=120){
							g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), 
									(int) (labelx-R/3-LabelLinkMargin*R-stringlength), (int) (labely-R/3));
							g.drawLine(labelx, labely, (int) (labelx-R/3), (int) (labely-R/3));
							g.drawLine((int) (labelx-R/3), (int) (labely-R/3), (int) (labelx-R/3-LabelLinkMargin*R), (int) (labely-R/3));
						
						}else if (HAngle>120&&HAngle<=240){
							g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), 
									(int) (labelx-LabelLinkMargin*R-stringlength),(int) (labely));
							g.drawLine(labelx, labely, (int) (labelx-LabelLinkMargin*R), labely);
						
						}else if (HAngle>240&&HAngle<270){
							g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), 
									(int) (labelx-R/3-LabelLinkMargin*R-stringlength), (int) (labely+R/3));
							g.drawLine(labelx, labely, (int) (labelx-R/3), (int) (labely+R/3));
							g.drawLine((int) (labelx-R/3), (int) (labely+R/3), (int) (labelx-R/3-LabelLinkMargin*R), (int) (labely+R/3));
						
						}else if(HAngle==270){
							g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), 
									(int) (labelx+LabelLinkMargin*R), (int) (labely+R/2));
							g.drawLine(labelx, labely, (int) (labelx), (int) (labely+R/2));
							g.drawLine((int) (labelx), (int) (labely+R/2), (int) (labelx+LabelLinkMargin*R), (int) (labely+R/2));
				
						}else if(HAngle>270&&HAngle<=360){
							g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), 
									(int) (labelx+LabelLinkMargin*R),(int) (labely));
							g.drawLine(labelx, labely, (int) (labelx+LabelLinkMargin*R), labely);
						}
					}else{
						//绘制最后一个角度
						Angle1 = Angle1 + indicatorDatas[0].getValues()[indicatorDatas[0].getValues().length-1]/sum*360;
						HAngle = Angle1 + Angle/2;
						int labelx = (int)(x+R/4*3*Math.cos(HAngle*Math.PI/180));
					    int labely = (int)(y-R/4*3*Math.sin(HAngle*Math.PI/180));
						
						 if (HAngle>180&&HAngle<=270) {
							 g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), 
										(int) (labelx-LabelLinkMargin*R-stringlength),(int) (labely));
								g.drawLine(labelx, labely, (int) (labelx-LabelLinkMargin*R), labely);
							
						 }else if(HAngle>270&&HAngle<360){
							g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), 
									(int) (labelx+LabelLinkMargin*R),(int) (labely));
							g.drawLine(labelx, labely, (int) (labelx+LabelLinkMargin*R), labely);
						}	
					}
				
				}
			}else{
				plot.setLabelGenerator(null);
			}
    
      

	}


	public BufferedImage drawLegend(int width, int height,
			ChartDataPara thematicRendition, ChartStyle chartStyle,
			double[] maxValues, double[] minValues, double[] averageValues,
			IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub


		String[] yearList = new String[indicatorDatas.length];
		for (int i = 0; i < yearList.length; i++) {
			yearList[i] = indicatorDatas[i].getDomainAxis();
		}
		

		// 绘制结构
		DefaultPieDataset pieStruDataset = new DefaultPieDataset();
		String[] seriesList = thematicRendition.getFieldName();
		for (int i = 0; i < seriesList.length; i++) 
		{
			pieStruDataset.setValue(seriesList[i],300);		
		}
		HashMap<String,Color> hashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			hashMap.put(thematicRendition.getFieldName()[i], new Color(thematicRendition.getFieldColor()[i],false));
		}
		JFreeChart pieStruChart = this.setStruLegend(chartStyle,pieStruDataset,"",hashMap, thematicRendition);
		 if (width<76) {
				width = 76;
			}
		BufferedImage pieStruLegend = pieStruChart.createBufferedImage(width*2+50, (int) (width+25),null);
		// 绘制构成标题
		TextTitle struTitle = pieStruChart.getTitle();
		struTitle.setText("统计图图例");
		struTitle.setFont(new Font("黑体", Font.PLAIN, 18));
		struTitle.setMargin(1, 1, 1, 1);
		BufferedImage imgStruTitle = new BufferedImage(1, 1,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2StruTitle = imgStruTitle.createGraphics();
        Size2D sizeStruTitle = struTitle.arrange(g2StruTitle);
        g2StruTitle.dispose();
        int wStruTitle = (int) Math.rint(sizeStruTitle.width);
        int hStruTitle = (int) Math.rint(sizeStruTitle.height);    
        int maxH = hStruTitle + pieStruLegend.getHeight();
        
		BufferedImage image = new BufferedImage((int) (width*2+50),hStruTitle+maxH+width, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.createGraphics();

//		g2d.setBackground(Color.WHITE);
//		g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		struTitle.draw(g2d, new Rectangle2D.Double((image.getWidth()-wStruTitle)/2,0,wStruTitle,hStruTitle));
		g2d.drawImage(pieStruLegend,(image.getWidth()-pieStruLegend.getWidth())/2,hStruTitle,pieStruLegend.getWidth(),pieStruLegend.getHeight(), null);
//		amountTitle.draw(g2d, new Rectangle2D.Double((image.getWidth()-wAmountTitle)/2,hStruTitle+pieStruLegend.getHeight(),wAmountTitle,hAmountTitle));
//		g2d.drawImage(bubbleSymbol,(image.getWidth()-bubbleSymbol.getWidth())/2,hStruTitle+pieStruLegend.getHeight()+hAmountTitle,bubbleSymbol.getWidth(),bubbleSymbol.getHeight(), null);	
//		g2d.drawRect(0,0, image.getWidth()-1, image.getHeight()-1);
		
		
		Rectangle2D.Double rect = new Rectangle2D.Double(1, width+25, width*2+50, width+50);
		g2d.setColor(Color.BLACK);
		//g2d.draw(rect);
//		FontMetrics fm = g2d.getFontMetrics();  
//        Rectangle2D rec=fm.getStringBounds(Double.toString(width*thematicRendition.getScales()[0]), g2d); 
//	
//		String value = Double.toString(indicatorDatas[0].getValues()[i]);
//        double  stringlength = rec.getWidth();//字符串的长度
//        double stringheight = rec.getHeight();	
		for(int i=0;i<3;i++){
				double interiorGap = 0.4 - 1.0 / 3;
				double X = rect.getCenterX();
				double Y = rect.getCenterY();
				double R = width/2*(1-2*interiorGap);
//				double R1 = width/2;
				Ellipse2D.Double oval = new Ellipse2D.Double(X-R+R/4*i,Y-R+R/2*i,2*(R-R/4*i),2*(R-R/4*i));
				Line2D.Double line = new Line2D.Double(X-1.5*R, Y-R+R*i/2, X,  Y-R+R*i/2);
				double sum1 = thematicRendition.getWidth()*thematicRendition.getScales()[0]*(1-i*1.0/4);
				double tempsum = sum1;
				if(thematicRendition.getWidth()<76){
					tempsum = width/thematicRendition.getWidth()*sum1;
				}
				
				NumberFormat ddf1=NumberFormat.getNumberInstance();
			    if (tempsum<10) {
			    	ddf1.setMaximumFractionDigits(2);
				}else {
					ddf1.setMaximumFractionDigits(0);
				}
			    ddf1.setGroupingUsed(false);
			    String temp = ddf1.format(tempsum);
			
				g2d.draw(line);
				g2d.draw(oval);
				g2d.setFont(new Font("宋体",Font.PLAIN,10));
				g2d.drawString(temp,  (int) (X-1.5*R),(int) (Y-R+R*i/2));
				
				}
				Rectangle2D.Double rect1 = new Rectangle2D.Double(1, width, width*2+100, width+50);
				g2d.setFont(new Font("黑体", Font.PLAIN, 16));
				//单位
				FontMetrics fm1 = g2d.getFontMetrics(); 
				String unitStr = null;
				String[] units = thematicRendition.getFieldUnits();
				if(units[0] != null && units[0].length() != 0)
					unitStr = "单位：" + units[0];
				int unitFontWidth = 0, unitFontHeight = 0;
				if(unitStr != null)
				{
					unitFontWidth = fm1.stringWidth(unitStr);//下方单位字符串的长度和
					unitFontHeight = fm1.getHeight();//下方单位字符串的高度
				}
				String[] fieldUnits = thematicRendition.getFieldUnits();
				if ((fieldUnits[0] != null) && (!(fieldUnits[0].length() == 0))) {
					g2d.drawString("单位:"+ "（" + fieldUnits[0] + "）",(int)(rect1.getCenterX()-unitFontWidth+10),(int)(rect1.getCenterY()+rect1.getHeight()/2+unitFontHeight));
				} else {
					g2d.drawString("", (int)(rect1.getCenterX()),(int)(rect1.getCenterY()));
				}		
		
		g2d.dispose();
		image.flush();
	
		return image;	
	}
	@SuppressWarnings("deprecation")
	public JFreeChart setStruLegend(ChartStyle chartStyle,PieDataset pieDataset,String title,HashMap<String, Color> hashMap,ChartDataPara thematicRendition)  {
		TRingPlot plot = new TRingPlot(pieDataset);
		boolean SectionOutlinesVisible = chartStyle.isSectionOutlinesVisible();
		float BaseSectionOutlineStroke = chartStyle.getBaseSectionOutlineStroke();
		int BaseSectionOutlinePaint = chartStyle.getBaseSectionOutlinePaint();
		JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
				plot, false);
		ChartUtilities.applyCurrentTheme(chart);
		// 获得图表对象的引用 设置pie chart背景
		chart.setBackgroundPaint(null);
        TRingPlot plot1 = (TRingPlot) chart.getPlot();
        
        for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
    		plot1.setSectionPaint(i,new Color(thematicRendition.getFieldColor()[i]));
        }
        plot1.setDirection(Rotation.ANTICLOCKWISE);
		plot1.setBackgroundPaint(null);
		plot1.setOutlineVisible(false);
		plot1.setInteriorGap(0);
		plot1.setLabelGap(0);
		plot1.setLabelFont(new Font("黑体",Font.PLAIN,10));
		plot1.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
		plot1.setLabelLinkMargin(0);
		plot1.setLabelBackgroundPaint(Color.white);
		plot1.setLabelShadowPaint(null);
        plot1.setLabelOutlinePaint(null);
		plot1.setToolTipGenerator(null);
		plot1.setCircular(true);
		plot1.setSectionDepth(0.5);
		plot1.setShadowPaint(null);		
		if(SectionOutlinesVisible){
			BasicStroke basicStroke = new BasicStroke(
					BaseSectionOutlineStroke);
			plot1.setBaseSectionOutlineStroke(basicStroke);
			plot1.setBaseSectionOutlinePaint(new Color(BaseSectionOutlinePaint,false));
			}
		
		return chart;
	}

	public HashMap<String, Shape> generateHotArea(double x, double y,
			int width, int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		double SectionDepth = chartStyle.getSectionDepth();
		double sum = 0;
		  for(int i=0;i<indicatorDatas[0].getValues().length;i++){
	        	sum = sum + indicatorDatas[0].getValues()[i]; 
      	}
      double pieRadius = sum*1.0/width/thematicRendition.getScales()[0];
      pieRadius = Math.sqrt(pieRadius);
      double  interiorGap= 0.4 - pieRadius / 3;
      //饼图大小由整个饼图所占绘图区域的比例来决定
      double R = width/2*(1-interiorGap*2); 
      //double R1 = 
      double R1 = width/2*(1-interiorGap*2)*(1-SectionDepth);
		double ANGLE = 0;
		double ANGLE1 = 0;
		HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
		for(int i=0;i<thematicRendition.getFieldName().length;i++){				
			ANGLE = indicatorDatas[0].getValues()[i]/sum*360;	
     		if(i==0){
				int[] xPoints = {(int) (x+R1),(int) (x+R),(int) (x+R*Math.cos(ANGLE/4*Math.PI/180)),(int) (x+R*Math.cos(ANGLE/2*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*3/4*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE*3/4*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE/2*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE/4*Math.PI/180))};
				int[] yPoints = {(int) y,(int) y,(int) (y-R*Math.sin(ANGLE/4*Math.PI/180)),(int) (y-R*Math.sin(ANGLE/2*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*3/4*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE*3/4*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE/2*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE/4*Math.PI/180))};
				Polygon polygon = new Polygon();
				for(int j=0;j<xPoints.length;j++){
					polygon.addPoint(xPoints[j], yPoints[j]);	
		       	}
				String text = "";
				if (thematicRendition.getDomainAxis()[0] == null
						|| (thematicRendition.getDomainAxis()[0].length() == 0)) {
    				text = "(" + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
				}else {
					text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
				}
    		 	//String text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
    			hotAreahHashMap.put(text, polygon);
	       	}else{
     		ANGLE1 = ANGLE1 + indicatorDatas[0].getValues()[i-1]/sum*360;
     		int[] xPoints = {(int) (x+R1*Math.cos(ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180)),
     						
     						(int) (x+R*Math.cos(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE*Math.PI/180+ANGLE1*Math.PI/180)),
     						
     						(int) (x+R1*Math.cos(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180))};
			
     		int[] yPoints = {(int) (y-R1*Math.sin(ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180)),
     						
     						(int) (y-R*Math.sin(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*Math.PI/180+ANGLE1*Math.PI/180)),
     						
     						(int) (y-R1*Math.sin(ANGLE*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180))};
			
     		Polygon polygon = new Polygon();
  			for(int j=0;j<xPoints.length;j++){
  				polygon.addPoint(xPoints[j], yPoints[j]);
			       	}
  			String text = "";
  			if (thematicRendition.getDomainAxis()[0] == null
					|| (thematicRendition.getDomainAxis()[0].length() == 0)) {
				text = "(" + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
			}else {
				text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
			}
		 	//String text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
			hotAreahHashMap.put(text, polygon);
		       	}			
		}
		return hotAreahHashMap;
		
	}



}
