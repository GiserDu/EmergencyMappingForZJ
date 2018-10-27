package com.zz.chart.obj.pie;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.HashMap;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.Size2D;
import org.jfree.util.Rotation;

import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.render.pie.OverTrianglePlot;
import com.zz.chart.render.pie.WindPiePlot;


public class PartSectorChart implements IChart {

	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		
		boolean SectionOutlinesVisible = chartStyle.isSectionOutlinesVisible();
		float BaseSectionOutlineStroke = chartStyle.getBaseSectionOutlineStroke();
		int BaseSectionOutlinePaint = chartStyle.getBaseSectionOutlinePaint();
		boolean isLabel = chartStyle.isbLabel();
		int LabelFontSize = chartStyle.getLabelFontSize();
		String labelFontName = chartStyle.getLabelFontName();
		double LabelLinkMargin = chartStyle.getLabelLinkMargin();
		
		// 消除线条锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		double Rmax = width;
		
		//按指标个数平分360，获取每份指标所占的角度
		double dAngle = 360/thematicRendition.getFieldName().length;
		//取每个指标所占的角度的三分之一作为每个扇形的跨度角
		double dSweepAngle = dAngle*1.0/3;
		double StAngle = 0;	
		
		for(int i=0;i<thematicRendition.getFieldName().length;i++){
			StAngle = dAngle*i;
			//绘制一个指标的扇形（多个）
			for(int k=0;k<indicatorDatas.length;k++){
				double R = indicatorDatas[k].getValues()[i]/maxValues[0]*Rmax;			
				Rectangle2D.Double rect = new Rectangle2D.Double(x-R, y-R, 2*R, 2*R);				
				Arc2D.Double sweepArc = new Arc2D.Double(rect, StAngle+k*dSweepAngle/5*4.0, dSweepAngle, Arc2D.PIE);			
//				g.setColor(new Color(thematicRendition.getFieldColor()[i]));
//		        g.setColor(new Color(thematicRendition.getFieldColor()[i]));
				
//		        //修改渐变色的生成方式
//	     		int colorr = (thematicRendition.getFieldColor()[i] >> 16) & 0xFF;
//	     		int colorg = (thematicRendition.getFieldColor()[i] >> 8) & 0xFF;
//	     		int colorb = (thematicRendition.getFieldColor()[i]) & 0xFF;
//	     		
//	      		int c = ColorUtil.getLighterColor(colorr, colorg, colorb, 0.12 * k);
				
				g.setColor( new Color(thematicRendition.getFieldColor()[i]));
				g.fill(sweepArc);
				double HAngle = 0;//图例绘制在指标对应角度的1/2处
				double Angle1 = 0;
				if(isLabel){
					Font font = new Font(labelFontName, Font.PLAIN, LabelFontSize);
					g.setFont(font);
					g.setColor(Color.BLACK);
				 	FontMetrics fm = g.getFontMetrics();  
		            Rectangle2D rec=fm.getStringBounds(Float.toString((float)indicatorDatas[0].getValues()[0]), g); 
				    	double stringlength = rec.getWidth();	
				    	g.setStroke(new BasicStroke(1f));
						if(i==0){
							//绘制第一个值得标注，分为四种种情况，标注绘制在终边的一半上
//							Angle = indicatorDatas[0].getValues()[i]/sum*360;
							
							HAngle = dSweepAngle/2+k*dSweepAngle/5*4.0;
							int labelx = (int)(x+R/4*3*Math.cos(HAngle*Math.PI/180));
						    int labely = (int)(y-R/4*3*Math.sin(HAngle*Math.PI/180));
							if(HAngle<=90){
								g.drawString(Float.toString((float)indicatorDatas[k].getValues()[i]), 
										(int) (labelx+LabelLinkMargin*R),(int) (labely));
								g.drawLine(labelx, labely, (int) (labelx+LabelLinkMargin*R), labely);
							
							}else if (HAngle>90&&HAngle<=180) {
								g.drawString(Float.toString((float)indicatorDatas[k].getValues()[i]),  
										(int) (labelx-LabelLinkMargin*R-stringlength),(int) (labely));
								g.drawLine(labelx, labely, (int) (labelx-LabelLinkMargin*R), labely);
							}
							//绘制中间的角度
						}else if(i>0&&i<indicatorDatas[k].getValues().length-1){
//							Angle = indicatorDatas[0].getValues()[i]/sum*360;							
//							Angle1 = Angle1 + indicatorDatas[k].getValues()[i-1]/sum*360;
							Angle1 = dAngle*i;
							HAngle = Angle1 + dSweepAngle/2+k*dSweepAngle/5*4.0;
							int labelx = (int)(x+R/4*3*Math.cos(HAngle*Math.PI/180));
						    int labely = (int)(y-R/4*3*Math.sin(HAngle*Math.PI/180));
							if(HAngle<=60){
								g.drawString(Float.toString((float)indicatorDatas[k].getValues()[i]), 
										(int) (labelx+LabelLinkMargin*R),(int) (labely));
								g.drawLine(labelx, labely, (int) (labelx+LabelLinkMargin*R), labely);
							
							}else if(HAngle>60&&HAngle<90){
								g.drawString(Float.toString((float)indicatorDatas[k].getValues()[i]), 
										(int) (labelx+R/3+LabelLinkMargin*R), (int) (labely-R/3));
								g.drawLine(labelx, labely, (int) (labelx+R/3), (int) (labely-R/3));
								g.drawLine((int) (labelx+R/3), (int) (labely-R/3), (int) (labelx+LabelLinkMargin*R/3+R), (int) (labely-R/3));
							
							}else if (HAngle==90){
								g.drawString(Float.toString((float)indicatorDatas[k].getValues()[i]),  
										(int) (labelx+LabelLinkMargin*R), (int) (labely-R/2));
								g.drawLine(labelx, labely, (int) (labelx), (int) (labely-R/2));
								g.drawLine((int) (labelx), (int) (labely-R/2), (int) (labelx+LabelLinkMargin*R), (int) (labely-R/2));
					
							}else if (HAngle>90&&HAngle<=120){
								g.drawString(Float.toString((float)indicatorDatas[k].getValues()[i]),  
										(int) (labelx-R/3-LabelLinkMargin*R-stringlength), (int) (labely-R/3));
								g.drawLine(labelx, labely, (int) (labelx-R/3), (int) (labely-R/3));
								g.drawLine((int) (labelx-R/3), (int) (labely-R/3), (int) (labelx-R/3-LabelLinkMargin*R), (int) (labely-R/3));
							
							}else if (HAngle>120&&HAngle<=240){
								g.drawString(Float.toString((float)indicatorDatas[k].getValues()[i]), 
										(int) (labelx-LabelLinkMargin*R-stringlength),(int) (labely));
								g.drawLine(labelx, labely, (int) (labelx-LabelLinkMargin*R), labely);
							
							}else if (HAngle>240&&HAngle<270){
								g.drawString(Float.toString((float)indicatorDatas[k].getValues()[i]), 
										(int) (labelx-R/3-LabelLinkMargin*R-stringlength), (int) (labely+R/3));
								g.drawLine(labelx, labely, (int) (labelx-R/3), (int) (labely+R/3));
								g.drawLine((int) (labelx-R/3), (int) (labely+R/3), (int) (labelx-R/3-LabelLinkMargin*R), (int) (labely+R/3));
							
							}else if(HAngle==270){
								g.drawString(Float.toString((float)indicatorDatas[k].getValues()[i]),  
										(int) (labelx+LabelLinkMargin*R), (int) (labely+R/2));
								g.drawLine(labelx, labely, (int) (labelx), (int) (labely+R/2));
								g.drawLine((int) (labelx), (int) (labely+R/2), (int) (labelx+LabelLinkMargin*R), (int) (labely+R/2));
					
							}else if(HAngle>270&&HAngle<=360){
								g.drawString(Float.toString((float)indicatorDatas[k].getValues()[i]),  
										(int) (labelx+LabelLinkMargin*R),(int) (labely));
								g.drawLine(labelx, labely, (int) (labelx+LabelLinkMargin*R), labely);
							}
						}else{
							//绘制最后一个角度
//							Angle1 = Angle1 + indicatorDatas[0].getValues()[indicatorDatas[0].getValues().length-1]/sum*360;
							Angle1 = dAngle*i;
							HAngle = Angle1 + dSweepAngle/2+k*dSweepAngle/5*4.0;
							int labelx = (int)(x+R/4*3*Math.cos(HAngle*Math.PI/180));
						    int labely = (int)(y-R/4*3*Math.sin(HAngle*Math.PI/180));
							
							 if (HAngle>180&&HAngle<=270) {
								 g.drawString(Float.toString((float)indicatorDatas[k].getValues()[i]), 
											(int) (labelx-LabelLinkMargin*R-stringlength),(int) (labely));
									g.drawLine(labelx, labely, (int) (labelx-LabelLinkMargin*R), labely);
								
							 }else if(HAngle>270&&HAngle<360){
								 g.drawString(Float.toString((float)indicatorDatas[k].getValues()[i]), 
										(int) (labelx+LabelLinkMargin*R),(int) (labely));
								g.drawLine(labelx, labely, (int) (labelx+LabelLinkMargin*R), labely);
							}	
						}
					
					}
			
				
				if(SectionOutlinesVisible){
					BasicStroke basicStroke = new BasicStroke(
							BaseSectionOutlineStroke);
					g.setStroke(basicStroke);
					g.setColor(new Color(BaseSectionOutlinePaint));
					g.draw(sweepArc);
					
					}
				
			}	
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
		DefaultCategoryDataset pieDataset = new DefaultCategoryDataset();
		String[] value_year;
		@SuppressWarnings("unused")
		double[] tempvalue = new double[indicatorDatas[0].getValues().length];
		
		value_year = new String[indicatorDatas.length];
		String[] nameStrings = thematicRendition.getFieldName();
		
		for(int i=0;i<indicatorDatas.length;i++){
		
			value_year[i]=indicatorDatas[i].getDomainAxis();
			
		}
		for (int i = 0; i < indicatorDatas.length; i++) {
			tempvalue  = indicatorDatas[i].getValues();
			
			for (int j = 0; j < nameStrings.length; j++) {
				pieDataset.setValue(100+j*200, nameStrings[j], indicatorDatas[i].getDomainAxis());
			}
		}
		HashMap<String,Color> hashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			hashMap.put(thematicRendition.getFieldName()[i], new Color(thematicRendition.getFieldColor()[i],true));
		}
		JFreeChart pieStruChart = this.setStruLegend(pieDataset,"",hashMap, thematicRendition);
		BufferedImage pieStruLegend = pieStruChart.createBufferedImage(width*2, (int) (width),null);
		
		// 绘制构成标题
		TextTitle struTitle = pieStruChart.getTitle();
//		struTitle.setText("图例");
		struTitle.setFont(new Font("黑体", Font.PLAIN, 25));		
		struTitle.setMargin(1, 1, 1, 1);
		BufferedImage imgStruTitle = new BufferedImage(1, 1,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2StruTitle = imgStruTitle.createGraphics();
        Size2D sizeStruTitle = struTitle.arrange(g2StruTitle);
        g2StruTitle.dispose();
        int wStruTitle = (int) Math.rint(sizeStruTitle.width);
        int hStruTitle = (int) Math.rint(sizeStruTitle.height);
      //绘制单位
		TextTitle unitTitle = new TextTitle();
//		unitTitle.setText("单位"+unit);
		unitTitle.setFont(new Font("黑体", Font.PLAIN, 20));
		unitTitle.setMargin(1, 1, 1, 1);
		BufferedImage imgUnit = new BufferedImage(1, 1,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2Unit = imgUnit.createGraphics();
//        Size2D sizeUnit = unitTitle.arrange(g2Unit);
        g2Unit.dispose();
//        int wUnite = (int) Math.rint(sizeUnit.width);
//        int hUnit = (int) Math.rint(sizeUnit.height);
 
        
        int maxH = hStruTitle + pieStruLegend.getHeight();
        
		BufferedImage image = new BufferedImage((int) (width+100),hStruTitle+maxH+width, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);		
		struTitle.draw(g2d, new Rectangle2D.Double((image.getWidth()-wStruTitle)/2,0,wStruTitle,hStruTitle));
		g2d.drawImage(pieStruLegend,(image.getWidth()-pieStruLegend.getWidth())/2,hStruTitle,pieStruLegend.getWidth(),pieStruLegend.getHeight(), null);
//		g2d.drawRect(0,0, image.getWidth()-1, image.getHeight()-1);	
		Rectangle2D.Double rect = new Rectangle2D.Double(1, width, width+100, width+50);
		g2d.setColor(Color.BLACK);
        double interiorGap = 0.4 - 1.0 / 3;
		double X = rect.getCenterX();
		double Y = rect.getCenterY();
		double l = width*(1-2*interiorGap);
		for(int i=0;i<3;i++){
			Arc2D.Double arcDouble = new Arc2D.Double(X-l/2+i*l/6, Y-l/2+i*l/6, l-i*l/3, l-i*l/3, 90, 30, Arc2D.PIE);
			Line2D.Double line4 = new Line2D.Double(X,Y-l/2+l/6*i,X+l/2,Y-l/2+l/6*i);
			double sum1 = maxValues[0]*(1-i*1.0/3);
			NumberFormat ddf1=NumberFormat.getNumberInstance();
			    if (sum1<10) {
			    	ddf1.setMaximumFractionDigits(2);
				}else {
					ddf1.setMaximumFractionDigits(0);
				}
			    ddf1.setGroupingUsed(false);
			    String temp = ddf1.format(sum1);
		    g2d.setFont(new Font("宋体",Font.PLAIN,10));
			g2d.drawString(temp,  (int) (X+l/2),(int) (Y-l/2+l/6*i));
			g2d.draw(arcDouble);
			g2d.draw(line4);
		}
		Rectangle2D.Double rect1 = new Rectangle2D.Double(1, width, width+100, width+50);
		g2d.setFont(new Font("黑体", Font.PLAIN, 20));
	
		String[] fieldUnits = thematicRendition.getFieldUnits();
		if ((fieldUnits[0] != null) && (!(fieldUnits[0].length() == 0))) {
			g2d.drawString("单位:"+ "（" + fieldUnits[0] + "）",(int)(rect1.getCenterX()-rect1.getWidth()/4),(int)(rect1.getCenterY()+rect1.getHeight()/2));
		} else {
			g2d.drawString("", (int)(rect1.getCenterX()),(int)(rect1.getCenterY()));
		}

		g2d.dispose();
		image.flush();
	
		return image;	
	}
	
	public JFreeChart setStruLegend(CategoryDataset pieStruDataset,String title,HashMap<String, Color> hashMap,ChartDataPara thematicRendition)  {
		WindPiePlot plot = new WindPiePlot(pieStruDataset);
		
		JFreeChart pieChart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
				plot, false);
		ChartUtilities.applyCurrentTheme(pieChart);
		// 获得图表对象的引�?设置pie chart背景
		pieChart.setBackgroundPaint(null);
        WindPiePlot plot1 = (WindPiePlot) pieChart.getPlot();
        
//        for (int i = 0; i < thematicRendition.getDomainAxis().length; i++) {
//   		 for(int j=0;j<thematicRendition.getFieldName().length;j++){
//   			plot.setSectionPaint(j,i, new Color(thematicRendition.getFieldColor()[j]+(255-50*i)*256*256*256,true ));
//   		 }
//   		}
        
        //修改渐变色的生成方式
//        for (int i = 0; i < thematicRendition.getDomainAxis().length; i++) {
//   		 for(int j=0;j<thematicRendition.getFieldName().length;j++){
//     		int r = (thematicRendition.getFieldColor()[j] >> 16) & 0xFF;
//     		int g = (thematicRendition.getFieldColor()[j] >> 8) & 0xFF;
//     		int b = (thematicRendition.getFieldColor()[j]) & 0xFF;
//     		
//      		int c = ColorUtil.getLighterColor(r, g, b, 0.12 * i);
//   			plot.setSectionPaint(j,i, new Color(c));
//   		 }
//   		}
  		for(int j=0;j<thematicRendition.getFieldName().length;j++) {
    		plot.setSectionPaint(j,0, hashMap.get(thematicRendition.getFieldName()[j]));
    	}
        plot1.setDirection(Rotation.ANTICLOCKWISE);
		plot1.setBackgroundPaint(null);
		plot1.setOutlineVisible(false);
		plot1.setInteriorGap(0.2);
		// 设置饼图标签的绘制字�?，label
		plot1.setStartAngle(0);
		plot1.setLabelGap(1);
		plot1.setShadowPaint(null);
      	plot1.setSimpleLabels(true);
      	plot1.setLabelBackgroundPaint(null);
      	plot1.setLabelLinksVisible(false);
      	plot1.setLabelOutlinePaint(null);
      	Font font = new Font("宋体", Font.PLAIN,10);
      	plot1.setLabelFont(font);
      	plot1.setLabelShadowPaint(null);
      	plot1.setLabelLinkMargin(0);
      	plot1.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
	return pieChart;
	}
	
	public HashMap<String, Shape> generateHotArea(double x, double y,
			int width, int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
		    double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		  double sum = 0;
	        for(int i=0;i<indicatorDatas[0].getValues().length;i++){
	        	sum = sum + indicatorDatas[0].getValues()[i];
	        }
	      double pieRadius = sum*1.0/width/thematicRendition.getScales()[0];
	      pieRadius = Math.sqrt(pieRadius);
	      double  interiorGap= 0.4 - pieRadius / 3;
		  double ANGLE = 0;
		  double ANGLE1 = 0;

				Rectangle2D.Double symbolEnvlope =  new 
				Rectangle2D.Double( x-width/2*(1-interiorGap*2), y-width/2*(1-interiorGap*2), 
						 width*(1-interiorGap*2), width*(1-interiorGap*2)); 
				HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
				for(int i=0;i<thematicRendition.getFieldName().length;i++){				
					Arc2D.Double arc1 = new Arc2D.Double();		
					ANGLE = indicatorDatas[0].getValues()[i]/sum*90;							
				    arc1 = new Arc2D.Double(symbolEnvlope, ANGLE1, ANGLE, 2);
					
					 ANGLE1 += ANGLE;

						String text = "";
						if (thematicRendition.getDomainAxis()[0] == null
								|| (thematicRendition.getDomainAxis()[0].length() == 0))  {
		    				text = "(" + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
						}else {
							text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
						}
		    			hotAreahHashMap.put(text, arc1);
		}
		  
		return hotAreahHashMap;

	}




}
