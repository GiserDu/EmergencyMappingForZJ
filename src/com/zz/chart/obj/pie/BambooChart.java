package com.zz.chart.obj.pie;

import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.render.pie.BambooPlot;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.Size2D;
import org.jfree.util.Rotation;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.HashMap;


public class BambooChart implements IChart {

	@SuppressWarnings("deprecation")
	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		
	
//		width = (new Double(width*1.3)).intValue();
		//获取饼图所需要的数据
		DefaultPieDataset pieDataset= new DefaultPieDataset();

		for (int i = 0; i < indicatorDatas[0].getNames().length; i++) {
			pieDataset.setValue(thematicRendition.getFieldName()[i], indicatorDatas[0].getValues()[i]);
		}
		
		//创建饼图
		BambooPlot plot1 = new BambooPlot(pieDataset);
		
		JFreeChart pieChart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
				plot1, false);
		ChartUtilities.applyCurrentTheme(pieChart);
		pieChart.setBackgroundPaint(null);
      
		
		
		// 获得图表对象的引用  
		BambooPlot plot = (BambooPlot) pieChart.getPlot();
		
		///获取饼图绘制样式
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
//		plot.setLabelGap(LabelGap);
		plot.setLabelGenerator(null);
//        if(isLabel){
      	
//      	plot.setSimpleLabels(true);
//      	plot.setLabelBackgroundPaint(null);
//      	plot.setLabelLinksVisible(false);
//      	plot.setLabelOutlinePaint(null);
//      	Font font = new Font(labelFontName, Font.PLAIN,LabelFontSize);
//      	plot.setLabelFont(font);
//      	plot.setLabelShadowPaint(null);
//      	plot.setLabelLinkMargin(0);
//      	plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{1}"));
//	        }else {
//				
//			}
		//  x = x+width/2*(1-2*(0.4-1.0/3));
//			y = y-width/2*(1-2*interiorGap);
    
		//绘制饼图
        ChartRenderingInfo info = new 
        		ChartRenderingInfo(new StandardEntityCollection());
	
		Rectangle2D.Double symbolEnvlope =  new 
				Rectangle2D.Double( x-width/2 ,y-width/2 ,width*1.4,width);
	//	g.draw(symbolEnvlope);
		
		pieChart.draw(g, symbolEnvlope, info);
		 //设置饼图标签
		 if(isLabel){
	        	g.setColor(Color.black);
	    		Font font = new Font(labelFontName, Font.PLAIN,LabelFontSize);
	    		g.setFont(font);//设置字体
	    		double stringlength = 0;
	    		@SuppressWarnings("unused")
				double stringheight = 0 ; //指标对应的值得字符串长高
//	    		double Rmax = width/2;
	    		double R = width/2*(1-interiorGap*2);//饼图的半径 
	   
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
					if(i==0){
						int labelx = (int)(x-stringlength*1.1);
					    int labely = (int)(y+R-2*R1);
					    g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), labelx,(int) (labely));	
					}else {
						int labelx = (int)(x+stringlength*0.1);
					    int labely = (int)(y+R-2*R1);
					    g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), labelx,(int) (labely));	
					}	
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
		String unit = "亿元";
		

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
      //绘制单位
		TextTitle unitTitle = new TextTitle();
		unitTitle.setText("单位："+"（"+unit+"）");
		unitTitle.setFont(new Font("黑体", Font.PLAIN, 20));
		unitTitle.setMargin(1, 1, 1, 1);
		BufferedImage imgUnit = new BufferedImage(1, 1,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2Unit = imgUnit.createGraphics();
//        Size2D sizeUnit = unitTitle.arrange(g2Unit);
        g2Unit.dispose();
//        int wUnite = (int) Math.rint(sizeUnit.width);
//        int hUnit = (int) Math.rint(sizeUnit.height);
      //绘制图例
		LegendTitle legend = new LegendTitle(pieStruChart.getPlot());
		legend.setItemFont(new Font("黑体", Font.PLAIN, 12));
        legend.setMargin(1, 1, 1, 1);
        legend.setPosition(RectangleEdge.RIGHT);
        legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        BufferedImage img = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
//        Size2D size = legend.arrange(g2);
        g2.dispose();
//        int w = (int) Math.rint(size.width);
//        int h = (int) Math.rint(size.height);
		
//        int maxW1 = Math.max(wStruTitle, wUnite) ;
//        int maxW2 = Math.max(wStruTitle,w) ;
//        int maxW = Math.max(maxW1, maxW2) ;
        
        int maxH = hStruTitle + pieStruLegend.getHeight();
        
		BufferedImage image = new BufferedImage((int) (width+100),hStruTitle+maxH+width, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.createGraphics();

//		g2d.setBackground(Color.WHITE);
//		g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		struTitle.draw(g2d, new Rectangle2D.Double((image.getWidth()-wStruTitle)/2,0,wStruTitle,hStruTitle));
		g2d.drawImage(pieStruLegend,(image.getWidth()-pieStruLegend.getWidth())/2,hStruTitle,pieStruLegend.getWidth(),pieStruLegend.getHeight(), null);
//		amountTitle.draw(g2d, new Rectangle2D.Double((image.getWidth()-wAmountTitle)/2,hStruTitle+pieStruLegend.getHeight(),wAmountTitle,hAmountTitle));
//		g2d.drawImage(bubbleSymbol,(image.getWidth()-bubbleSymbol.getWidth())/2,hStruTitle+pieStruLegend.getHeight()+hAmountTitle,bubbleSymbol.getWidth(),bubbleSymbol.getHeight(), null);	
//		g2d.drawRect(0,0, image.getWidth()-1, image.getHeight()-1);
	
		Rectangle2D.Double rect = new Rectangle2D.Double(1, width, width+100, width+50);
		g2d.setColor(Color.BLACK);
	//	g2d.draw(rect);
//		FontMetrics fm = g2d.getFontMetrics();  
//        Rectangle2D rec=fm.getStringBounds(Double.toString(width*thematicRendition.getScales()[0]), g2d); 
        double interiorGap = 0.4 - 1.0 / 3;
		double X = rect.getCenterX();
		double Y = rect.getCenterY();
		double l = width*(1-2*interiorGap);
//		String value = Double.toString(indicatorDatas[0].getValues()[i]);
//        double  stringlength = rec.getWidth();//字符串的长度
//        double stringheight = rec.getHeight();	
		for(int i=0;i<3;i++){
		//Arc2D.Double arc = new Arc2D.Double(rect.getCenterX()-thematicRendition.getWidth()/2, rect.getCenterY()-thematicRendition.getWidth()/2+thematicRendition.getWidth()*i/8, thematicRendition.getWidth()-thematicRendition.getWidth()*i/4, thematicRendition.getWidth()-thematicRendition.getWidth()*i/4, 0, 180,Arc2D.OPEN );
		//Ellipse2D.Double oval = new Ellipse2D.Double(rect.getCenterX()-thematicRendition.getWidth()/2+thematicRendition.getWidth()*i/8, rect.getCenterY()-thematicRendition.getWidth()/2+thematicRendition.getWidth()*i/4, thematicRendition.getWidth()-thematicRendition.getWidth()*i/4, thematicRendition.getWidth()-thematicRendition.getWidth()*i/4);		
		Line2D.Double line1 = new Line2D.Double(X-l/2,Y-l/2,X-l/2, Y+l/2);
		Line2D.Double line2 = new Line2D.Double(X-l/2,Y-l/2,X-l/4,Y-l/2);
		Line2D.Double line3 = new Line2D.Double(X-l/4,Y-l/2,X-l/16*7,Y+l/2);
		Line2D.Double line = new Line2D.Double(X-l/16*7,Y+l/2, X-l/2,Y+l/2);
		Line2D.Double line4 = new Line2D.Double(X-l/2,Y-l/2+l/3*i,X+l/2,Y-l/2+l/3*i);
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
		g2d.drawString(temp,  (int) (X+l/2),(int) (Y-l/2+l/3*i));
		g2d.draw(line);
		g2d.draw(line1);
		g2d.draw(line2);
		g2d.draw(line3);
		g2d.draw(line4);
		
	//	g2d.drawLine((int)rect.getCenterX()-thematicRendition.getWidth()/2,(int) rect.getCenterY(), (int)rect.getCenterX()+thematicRendition.getWidth(),(int)rect.getCenterY());
		}
		Rectangle2D.Double rect1 = new Rectangle2D.Double(1, width, width+100, width+50);
		g2d.setFont(new Font("黑体", Font.PLAIN, 16));
	
		String[] fieldUnits = thematicRendition.getFieldUnits();
		if ((fieldUnits[0] != null) && (!(fieldUnits[0].length() == 0))) {
			g2d.drawString("单位:"+ "（" + fieldUnits[0] + "）",(int)(rect1.getCenterX()-rect1.getWidth()/4),(int)(rect1.getCenterY()+rect1.getHeight()/3+10));
		} else {
			g2d.drawString("", (int)(rect1.getCenterX()),(int)(rect1.getCenterY()));
		}
		
		g2d.dispose();
		image.flush();
	
		return image;	
	}
	
	@SuppressWarnings("deprecation")
	public JFreeChart setStruLegend(ChartStyle chartStyle,PieDataset pieDataset,String title,HashMap<String, Color> hashMap,ChartDataPara thematicRendition)  {
		BambooPlot plot = new BambooPlot(pieDataset);

		JFreeChart pieChart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
				plot, false);
		ChartUtilities.applyCurrentTheme(pieChart);
		// 获得图表对象的引用 设置pie chart背景
		pieChart.setBackgroundPaint(null);
//		TextTitle title = pieChart.getTitle();
//      title.setFont(new Font("黑体",Font.PLAIN,15));
		
        BambooPlot plot1 = (BambooPlot) pieChart.getPlot();
        
        for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
    		plot.setSectionPaint(i,new Color(thematicRendition.getFieldColor()[i]));
        }
        plot1.setDirection(Rotation.ANTICLOCKWISE);
		plot1.setBackgroundPaint(null);
		plot1.setOutlineVisible(false);
		plot1.setInteriorGap(0);
		// 设置饼图标签的绘制字体 ，label
		plot1.setLabelGap(1);
		plot1.setLabelFont(new Font("黑体",Font.PLAIN,10));
		plot1.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
		plot1.setLabelLinkMargin(0.1);
		plot1.setLabelBackgroundPaint(Color.white);
		plot1.setLabelShadowPaint(null);
        plot1.setLabelOutlinePaint(null);
        plot1.setInteriorGap(0.3);
		plot1.setToolTipGenerator(null);
		plot1.setCircular(true);

		plot1.setShadowPaint(null);
		
		plot1.setSectionOutlinesVisible(chartStyle.isSectionOutlinesVisible());
	 	
      	plot.setSimpleLabels(true);
      	plot.setLabelBackgroundPaint(null);
      	plot.setLabelLinksVisible(false);
      	plot.setLabelOutlinePaint(null);
      	
      	plot.setLabelShadowPaint(null);
    
		return pieChart;
	
	}


	public HashMap<String, Shape> generateHotArea(double x, double y,
			int width, int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
	//  x = x+width/2*(1-2*(0.4-1.0/3));
	
		double sum = 0;
		  for(int i=0;i<indicatorDatas[0].getValues().length;i++){
	        	sum = sum + indicatorDatas[0].getValues()[i]; 
		  	}
	  double pieRadius = sum*1.0/width/thematicRendition.getScales()[0];
	  pieRadius = Math.sqrt(pieRadius);
	  double  interiorGap= 0.5 - pieRadius / 2;
		y = y-width/2*(1-2*interiorGap);
		double Rmax = width/2*(1-interiorGap*2);
		double R =0;
		  double max = indicatorDatas[0].getValues()[0];  
		  for(int i=0;i<indicatorDatas[0].getValues().length;i++){
			if(max<indicatorDatas[0].getValues()[i]){
				max = indicatorDatas[0].getValues()[i];
			} 	  	  
		  }
		  HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
			for(int i=0;i<thematicRendition.getFieldName().length;i++){
				R = indicatorDatas[0].getValues()[i]/max*Rmax;	
					if(i==0){
						int[] xPoints = {(int) (x-width/20),(int) (x-width/20),(int) (x-width/10-2*R*Math.sin(15*Math.PI/180)),(int)(x-width/10)};
						int[] yPoints = {(int) (y+Rmax),(int) (y-(2*R-Rmax)),(int) (y-(2*R-Rmax)),(int) (y+Rmax)};
						Polygon polygon = new Polygon();
						for(int j=0;j<xPoints.length;j++){
							polygon.addPoint(xPoints[j], yPoints[j]);
						}
						String text = "";
						if (thematicRendition.getDomainAxis()[0] == null||thematicRendition.getDomainAxis()[0]=="") {
		    				text = "(" + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
						}else {
							text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
						}
		    		 	//String text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
		    			hotAreahHashMap.put(text, polygon);
						
				       	}else{
				       		int[] xPoints = {(int) (x),(int) (x),(int) (x+width/10+2*R*Math.sin(15*Math.PI/180)),(int)(x+width/10)};
							int[] yPoints = {(int) (y+Rmax),(int) (y-(2*R-Rmax)),(int) (y-(2*R-Rmax)),(int) (y+Rmax)};
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
				       	}}
	
		return hotAreahHashMap;
	}


	





		
	}


