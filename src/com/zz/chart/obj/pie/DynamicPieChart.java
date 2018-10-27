package com.zz.chart.obj.pie;

import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.render.pie.DynamicPiePlot;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
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
import java.util.Arrays;
import java.util.HashMap;

public class DynamicPieChart implements IChart {

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
		DynamicPiePlot plot1 = new DynamicPiePlot(pieDataset);
		
		JFreeChart pieChart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
				plot1, false);
		ChartUtilities.applyCurrentTheme(pieChart);
		pieChart.setBackgroundPaint(null);
		
		// 获得图表对象的引�? 
		DynamicPiePlot plot = (DynamicPiePlot) pieChart.getPlot();
		
		
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
        plot.setNoDataMessageFont(new Font("宋体", Font.ITALIC, 20)); // 设置没有数据时显示的信息的字�?        
        plot.setNoDataMessagePaint(Color.orange); // 设置没有数据时显示的信息的颜�? 
        //设置饼图大小
        double sum = 0;
        for(int i=0;i<indicatorDatas[0].getValues().length;i++){
        	sum = sum + indicatorDatas[0].getValues()[i];
        }
        double pieRadius = sum*1.0/width/thematicRendition.getScales()[0];
        pieRadius = Math.sqrt(pieRadius);
        double interiorGap = 0.4 - pieRadius / 3;
        	if(interiorGap>0.425){
        		interiorGap = 0.425;
        	}
        plot.setInteriorGap(interiorGap);
        plot.setLabelGenerator(null);
   //     x = x+width/2*(1-2*(0.4-1.0/3));
//		y = y-width/2*(1-2*interiorGap);
       
		//绘制饼图
        ChartRenderingInfo info = new 
        		ChartRenderingInfo(new StandardEntityCollection());
	
		Rectangle2D.Double symbolEnvlope =  new 
				Rectangle2D.Double( x-width/2 ,y-width/2 , width,width); 
//		g.draw(symbolEnvlope);
		
		pieChart.draw(g, symbolEnvlope, info);
		
        //设置饼图标签
        if(isLabel){
        	g.setColor(Color.black);
    		Font font = new Font(labelFontName, Font.PLAIN,LabelFontSize);
    		g.setFont(font);//设置字体
    		double stringlength = 0;
    		@SuppressWarnings("unused")
			double stringheight = 0 ; //指标对应的值得字符串长�?
//    		double Rmax = width/2;
    		double R = width/2*(1-interiorGap*2);//饼图的半�?
    	 //	double Rmax = width/2*(1-interiorGap*2);
    	 	 double max = indicatorDatas[0].getValues()[0];  
    	     for(int i=0;i<indicatorDatas[0].getValues().length;i++){
    	   	if(max<indicatorDatas[0].getValues()[i]){
    	   		max = indicatorDatas[0].getValues()[i];
    	   	} 	  	  
    	     }
    		for(int i=0;i<indicatorDatas[0].getValues().length;i++){
				FontMetrics fm = g.getFontMetrics();  
		            Rectangle2D rec=fm.getStringBounds(Float.toString((float)indicatorDatas[0].getValues()[i]), g); 
		//		Angle = indicatorDatas[0].getValues()[i]/sum*360;
		//		String value = Double.toString(indicatorDatas[0].getValues()[i]);
				stringlength = rec.getWidth();//字符串的长度
				stringheight = rec.getHeight();	
				double R1 = indicatorDatas[0].getValues()[i]/max*R;
				
					int labelx = (int)(x-stringlength*0.5);
				    int labely = (int)(y+R-2*R1);
				    g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), labelx,(int) (labely));	
				
    		}
	        }else {
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
			pieStruDataset.setValue(seriesList[i],indicatorDatas[0].getValues()[i]);		
		}
		HashMap<String,Color> hashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			hashMap.put(thematicRendition.getFieldName()[i], new Color(thematicRendition.getFieldColor()[i],false));
		}
		
		JFreeChart pieStruChart = this.setStruLegend(pieStruDataset,"",hashMap, thematicRendition);
		  if (width<76) {
				width = 76;
			}
		
		BufferedImage pieStruLegend = pieStruChart.createBufferedImage(width*2, (int) (width),null);
		
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
        
      
		BufferedImage image = new BufferedImage((int) (width+100),hStruTitle+maxH+width+30, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.createGraphics();


//		g2d.setBackground(Color.WHITE);
//		g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		struTitle.draw(g2d, new Rectangle2D.Double((image.getWidth()-wStruTitle)/2,0,wStruTitle,hStruTitle));
		g2d.drawImage(pieStruLegend,(image.getWidth()-pieStruLegend.getWidth())/2,hStruTitle,pieStruLegend.getWidth(),pieStruLegend.getHeight(), null);
//		amountTitle.draw(g2d, new Rectangle2D.Double((image.getWidth()-wAmountTitle)/2,hStruTitle+pieStruLegend.getHeight(),wAmountTitle,hAmountTitle));
//		g2d.drawImage(bubbleSymbol,(image.getWidth()-bubbleSymbol.getWidth())/2,hStruTitle+pieStruLegend.getHeight()+hAmountTitle,bubbleSymbol.getWidth(),bubbleSymbol.getHeight(), null);	
//		g2d.drawRect(0,0, image.getWidth()-1, image.getHeight()-1);
		
		Font font = new Font("宋体", Font.PLAIN,12);
		g2d.setFont(font);//设置字体
		double stringlength1 = 0;
		@SuppressWarnings("unused")
		double stringheight1 = 0 ; //指标对应的值得字符串长�?

		Rectangle2D.Double rect = new Rectangle2D.Double(1, width+20, width+150, width+50);
		g2d.setColor(Color.BLACK);
		//g2d.draw(rect);
		FontMetrics fm = g2d.getFontMetrics();  
        Rectangle2D rec=fm.getStringBounds(Double.toString(width*thematicRendition.getScales()[0]), g2d); 
	
//		String value = Double.toString(indicatorDatas[0].getValues()[i]);
//        double  stringlength = rec.getWidth();//字符串的长度
        @SuppressWarnings("unused")
		double stringheight = rec.getHeight();	
		for(int i=0;i<3;i++){
			
			double interiorGap = 0.4 - 1.0 / 3;
			double X = rect.getCenterX();
			double Y = rect.getCenterY();
			double R = width/2*(1-2*interiorGap);
//			double R1 = width/2;
			Ellipse2D.Double oval = new Ellipse2D.Double(X-R+R/4*i,Y-R+R/2*i,2*(R-R/4*i),2*(R-R/4*i));
			Line2D.Double line = new Line2D.Double(X-R*1.3, Y-R+R*i/2, X,  Y-R+R*i/2);
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
			
			g2d.drawString(temp,  (int) (X-R*1.375),(int) (Y-R+R*i/2));
			
		}

		Rectangle2D.Double rect1 = new Rectangle2D.Double(1, 1, width+150, width+80);
		g2d.setColor(Color.BLACK);
		int tempValue = thematicRendition.getFieldName().length;
		for(int i=0;i<tempValue;i++){
			FontMetrics fm1 = g2d.getFontMetrics();  
            Rectangle2D rec1=fm1.getStringBounds(indicatorDatas[0].getNames()[i], g2d); 
            
		stringlength1 = rec1.getWidth();//字符串的长度
		stringheight1 = rec1.getHeight();
		Ellipse2D.Double oval = new Ellipse2D.Double(rect1.getCenterX()-width/2+width*i/tempValue/2, 
				rect1.getCenterY()-width/2+width*i/tempValue,width-width*i/tempValue, width-width*i/tempValue);
		
		Line2D.Double line = new Line2D.Double(rect1.getCenterX()-width/2-stringlength1/8, 
				rect1.getCenterY()-width/2+width*i/tempValue+width/2/tempValue, 
				rect1.getCenterX(),rect1.getCenterY()-width/2+width*i/tempValue+width/2/tempValue);
		
		g2d.setColor(new Color(thematicRendition.getFieldColor()[i]));
		g2d.fill(oval);
		g2d.setColor(Color.black);
		g2d.draw(line);
		g2d.setFont(new Font("宋体",Font.PLAIN,10));
		g2d.drawString(thematicRendition.getFieldName()[i],  
				(int) (rect1.getCenterX()-width/2-stringlength1),
				(int) rect1.getCenterY()-width/2+width*i/tempValue+width/2/tempValue);

		}
		Rectangle2D.Double rect11 = new Rectangle2D.Double(1, width, width+100, width+50);
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
			g2d.drawString("单位:"+ "（" + fieldUnits[0] + "）",(int)(rect11.getCenterX()-unitFontWidth/2),(int)(rect11.getCenterY()+rect11.getHeight()/2+unitFontHeight));
		} else {
			g2d.drawString("", (int)(rect11.getCenterX()),(int)(rect11.getCenterY()));
		}
	//	g2d.drawRect(x, y, symbolWidth, symbolHeight)
		
		
		g2d.dispose();
		image.flush();
	
		return image;	
	}
	
	@SuppressWarnings("deprecation")
	public JFreeChart setStruLegend(PieDataset pieDataset,String title,HashMap<String, Color> hashMap,ChartDataPara thematicRendition)  {
		DynamicPiePlot plot = new DynamicPiePlot(pieDataset);
		JFreeChart pieChart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
				plot, false);
		ChartUtilities.applyCurrentTheme(pieChart);
		// 获得图表对象的引�?设置pie chart背景
		pieChart.setBackgroundPaint(null);
//		TextTitle title = pieChart.getTitle();
//      title.setFont(new Font("黑体",Font.PLAIN,15));
		
        DynamicPiePlot plot1 = (DynamicPiePlot) pieChart.getPlot();
        
        for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
    		plot.setSectionPaint(i,new Color(thematicRendition.getFieldColor()[i]));
        }
		
        plot.setBackgroundAlpha(0);
		plot.setForegroundAlpha(1f);
		plot.setSectionOutlinesVisible(false);
		plot.setOutlineVisible(false);
	
		plot.setShadowPaint(null);
		plot.setCircular(true);
		plot.setDirection(Rotation.ANTICLOCKWISE);
		plot.setStartAngle(0);
		plot.setIgnoreNullValues(true);
		plot.setIgnoreZeroValues(true);
		plot.setNoDataMessage("");          
        plot.setNoDataMessageFont(new Font("宋体", Font.ITALIC, 20)); // 设置没有数据时显示的信息的字�?        
        plot.setNoDataMessagePaint(Color.orange); // 设置没有数据时显示的信息的颜�? 

     

        plot.setInteriorGap(0.5);
      	plot1.setLabelGenerator(null);
      
		return pieChart;
		
	

	}
	
	






	public HashMap<String, Shape> generateHotArea(double x, double y,
			int width, int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		  //     x = x+width/2*(1-2*(0.4-1.0/3));
		
		double sum = 0;
		  for(int i=0;i<indicatorDatas[0].getValues().length;i++){
	        	sum = sum + indicatorDatas[0].getValues()[i]; 
		  	}
	  double pieRadius = sum*1.0/width/thematicRendition.getScales()[0];
	  pieRadius = Math.sqrt(pieRadius);
	  double  interiorGap= 0.4 - pieRadius / 3;
	 
	
	  y = y-width/2*(1-2*interiorGap);
	
//	    double Rmax = width/2;
		double R = width/2*(1-interiorGap*2);//饼图的半�?
	 //	double Rmax = width/2*(1-interiorGap*2);
	 	 double max = indicatorDatas[0].getValues()[0];  
	     for(int i=0;i<indicatorDatas[0].getValues().length;i++){
	   	if(max<indicatorDatas[0].getValues()[i]){
	   		max = indicatorDatas[0].getValues()[i];
	   	  } 	  	  
	     }
	     double[] temp = indicatorDatas[0].getValues();
	     Arrays.sort(temp);
	     HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
		for(int i=0;i<indicatorDatas[0].getValues().length;i++){	
			double R1 = temp[i]/max*R;
			if(0==i){
				Polygon polygon = new Polygon();
				for(int j=0;j<8;j++){
					int[] xPoints = {(int)(x+R1*Math.cos(j*45*Math.PI/180)),};
					int[] yPoints = {(int)(y+R-R1-R1*Math.sin(j*45*Math.PI/180))};
					for(int k=0;k<xPoints.length;k++){
						polygon.addPoint(xPoints[k], yPoints[k]);						
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
//				g.setColor(Color.black);
//				g.setStroke(new BasicStroke(2f));
//				//g.draw(polygon);
			}
			if(i>0){
				double R2 = temp[i-1]/max*R;
				Polygon polygon = new Polygon();
				for(int j=0;j<8;j++){
					int[] xPoints = {(int)(x+R1*Math.cos(270*Math.PI/180)),(int)(x+R1*Math.cos(315*Math.PI/180)),
							         (int)(x+R1*Math.cos(0*Math.PI/180)),(int)(x+R1*Math.cos(45*Math.PI/180)),
							         (int)(x+R1*Math.cos(90*Math.PI/180)),(int)(x+R1*Math.cos(135*Math.PI/180)),
							         (int)(x+R1*Math.cos(180*Math.PI/180)),(int)(x+R1*Math.cos(225*Math.PI/180)),
							         (int)(x+R1*Math.cos(270*Math.PI/180)),
							        
							         (int)(x+R2*Math.cos(270*Math.PI/180)), (int)(x+R2*Math.cos(315*Math.PI/180))
									 ,(int)(x+R2*Math.cos(0*Math.PI/180)),(int)(x+R2*Math.cos(45*Math.PI/180))
									 ,(int)(x+R2*Math.cos(90*Math.PI/180)),(int)(x+R2*Math.cos(135*Math.PI/180))
									 ,(int)(x+R2*Math.cos(180*Math.PI/180)),(int)(x+R2*Math.cos(225*Math.PI/180))
									 ,(int)(x+R2*Math.cos(270*Math.PI/180))
					  				};
					int[] yPoints = {(int)(y+R-R1-R1*Math.sin(270*Math.PI/180)),(int)(y+R-R1-R1*Math.sin(315*Math.PI/180)),
							 		(int)(y+R-R1-R1*Math.sin(0*Math.PI/180)),(int)(y+R-R1-R1*Math.sin(45*Math.PI/180)),
							 		(int)(y+R-R1-R1*Math.sin(90*Math.PI/180)),(int)(y+R-R1-R1*Math.sin(135*Math.PI/180)),
							 		(int)(y+R-R1-R1*Math.sin(180*Math.PI/180)),(int)(y+R-R1-R1*Math.sin(225*Math.PI/180)),(int)(y+R-R1-R1*Math.sin(270*Math.PI/180)),
							 		
							 		(int)(y+R-R2-R2*Math.sin(270*Math.PI/180)),(int)(y+R-R2-R2*Math.sin(315*Math.PI/180))							 		
							 		,(int)(y+R-R2-R2*Math.sin(0*Math.PI/180)),(int)(y+R-R2-R2*Math.sin(45*Math.PI/180))
							 		,(int)(y+R-R2-R2*Math.sin(90*Math.PI/180)),(int)(y+R-R2-R2*Math.sin(135*Math.PI/180))
							 		,(int)(y+R-R2-R2*Math.sin(180*Math.PI/180)),(int)(y+R-R2-R2*Math.sin(225*Math.PI/180))
							 		,(int)(y+R-R2-R2*Math.sin(270*Math.PI/180))
					};
					for(int k=0;k<xPoints.length;k++){
						polygon.addPoint(xPoints[k], yPoints[k]);						
			        }
					String text = "";
					if (thematicRendition.getDomainAxis()[0] == null||thematicRendition.getDomainAxis()[0]=="") {
	    				text = "(" + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
					}else {
						text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
					}
	    		 	//String text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
	    			hotAreahHashMap.put(text, polygon);
				}
//				g.setColor(Color.black);
//				//g.setStroke(new BasicStroke(2f));
//				g.draw(polygon);
			}
		}
		
		return hotAreahHashMap;
		
	}


}
