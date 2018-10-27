package com.zz.chart.obj.pie;

import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.render.pie.SectorPlot;
import com.zz.chart.render.pie.TPiePlot;
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
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.HashMap;

public class MSectorChart implements IChart {

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
		
		SectorPlot plot1 = new SectorPlot(pieDataset);
		//绘制饼图
		JFreeChart pieChart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
				plot1, false);
		ChartUtilities.applyCurrentTheme(pieChart);
		pieChart.setBackgroundPaint(null);
		
		// 获得图表对象的引用  
		TPiePlot plot = (TPiePlot) pieChart.getPlot();
		
		//获取饼图绘制样式
		float BackgroundAlpha = chartStyle.getBackgroundAlpha();
		float ForegroundAlpha = chartStyle.getForegroundAlpha();
		boolean OutlineVisible = chartStyle.isOutlineVisble();
		boolean SectionOutlinesVisible = chartStyle.isSectionOutlinesVisible();
		float BaseSectionOutlineStroke = chartStyle.getBaseSectionOutlineStroke();
		int BaseSectionOutlinePaint = chartStyle.getBaseSectionOutlinePaint();
		boolean Circular = chartStyle.isCircular();
//		double StartAngle = chartStyle.getStartAngle();
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
		plot.setStartAngle(90);
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
        double pieRadius = sum/width/thematicRendition.getScales()[0];
        pieRadius = Math.sqrt(pieRadius);
        double interiorGap = 0.4 - pieRadius / 3;
        if(interiorGap<0.35){
        	plot.setInteriorGap(interiorGap);
        }else {
			plot.setInteriorGap(interiorGap);
		}
        plot.setLabelGenerator(null);
   
		//绘制饼图
        ChartRenderingInfo info = new 
        		ChartRenderingInfo(new StandardEntityCollection());
	
		Rectangle2D.Double symbolEnvlope =  new 
				Rectangle2D.Double( x-width/2 ,y-width/2 , width,width); 
		//g.draw(symbolEnvlope);
		pieChart.draw(g, symbolEnvlope, info);
		//设置标签
		if(isLabel){
		plot.setLabelGenerator(null);
		g.setColor(Color.black);
		Font font = new Font(labelFontName, Font.PLAIN,LabelFontSize);
		g.setFont(font);//设置字体
		double stringlength = 0;
		@SuppressWarnings("unused")
		double stringheight = 0 ; //指标对应的值得字符串长高
	   	double Rmax = width/2*(1-interiorGap*2);
		double max = indicatorDatas[0].getValues()[0];  
        for(int i=0;i<indicatorDatas[0].getValues().length;i++){
        	 if(max<indicatorDatas[0].getValues()[i]){
        		 max = indicatorDatas[0].getValues()[i];
       	} 	  	  
         }
        double R = 0;
        for(int i=0;i<indicatorDatas[0].getNames().length;i++){
        	R = indicatorDatas[0].getValues()[i]/max*Rmax;
        	FontMetrics fm = g.getFontMetrics();  
            Rectangle2D rec=fm.getStringBounds(Float.toString((float)indicatorDatas[0].getValues()[i]), g); 
		    stringlength = rec.getWidth();//字符串的长度
		    stringheight = rec.getHeight();
//			double ANGLE = 180;
//			double ANGLE1 = 180;
//			double a = 90;	
	     		if(i==0){
					g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), 
							(int)(x-R-stringlength),(int) y);		       
		       	}else{
		       		g.drawString(Float.toString((float)indicatorDatas[0].getValues()[i]), 
							(int)(x+R),(int) y);
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
		String unit = "亿元";
		

		// 绘制结构
		DefaultPieDataset pieStruDataset = new DefaultPieDataset();
		String[] seriesList = thematicRendition.getFieldName();
		for (int i = 0; i < seriesList.length; i++) 
		{
			pieStruDataset.setValue(seriesList[i],maxValues[0]);		
		}
		HashMap<String,Color> hashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			hashMap.put(thematicRendition.getFieldName()[i], new Color(thematicRendition.getFieldColor()[i],false));
		}
		JFreeChart pieStruChart = this.setStruLegend(pieStruDataset,"",hashMap, thematicRendition);
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
		
//		
//        int maxW1 = Math.max(wStruTitle, wUnite) ;
//        int maxW2 = Math.max(wStruTitle,w) ;
//        int maxW = Math.max(maxW1, maxW2) ;
//        
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
			//g2d.draw(rect);
			FontMetrics fm = g2d.getFontMetrics();  
	        Rectangle2D rec=fm.getStringBounds(Double.toString(width*thematicRendition.getScales()[0]), g2d); 
		
//			String value = Double.toString(indicatorDatas[0].getValues()[i]);
	        double  stringlength = rec.getWidth();//字符串的长度
	        @SuppressWarnings("unused")
			double stringheight = rec.getHeight();	
			for(int i=0;i<3;i++){
				double interiorGap1 = 0.4 - 1.0 / 3;
				double X1 = rect.getCenterX();
				double Y1 = rect.getCenterY();
				double R1 = width/2*(1-2*interiorGap1);
			Arc2D.Double arc = new Arc2D.Double(X1-R1, Y1-R1+R1*i/4, 2*(R1-R1*i/4), 2*(R1-R1*i/4), 0, 180,Arc2D.OPEN );
			//Ellipse2D.Double oval = new Ellipse2D.Double(rect.getCenterX()-thematicRendition.getWidth()/2+thematicRendition.getWidth()*i/8, rect.getCenterY()-thematicRendition.getWidth()/2+thematicRendition.getWidth()*i/4, thematicRendition.getWidth()-thematicRendition.getWidth()*i/4, thematicRendition.getWidth()-thematicRendition.getWidth()*i/4);
			Line2D.Double line = new Line2D.Double(X1-R1-stringlength/8, Y1-R1+R1*i/4, X1,  Y1-R1+R1*i/4);
			double sum1 = width*thematicRendition.getScales()[0]*(1-i*1.0/4);
			NumberFormat ddf1=NumberFormat.getNumberInstance();
		    if (sum1<10) {
		    	ddf1.setMaximumFractionDigits(2);
			}else {
				ddf1.setMaximumFractionDigits(0);
			}
		    ddf1.setGroupingUsed(false);
		    String temp = ddf1.format(sum1);
			g2d.draw(line);
			g2d.draw(arc);
			g2d.setFont(new Font("宋体",Font.PLAIN,9));
			g2d.drawString(temp,  (int) (X1-R1-stringlength*0.25),(int) (Y1-R1+R1*2*i/8));
			g2d.drawLine((int)(X1-R1),(int) Y1, (int) ((int)X1+R1*2),(int)Y1);
			}
	//	g2d.drawRect(x, y, symbolWidth, symbolHeight)
			Rectangle2D.Double rect1 = new Rectangle2D.Double(1, width, width+100, width+50);
			g2d.setFont(new Font("黑体", Font.PLAIN, 16));
		
			String[] fieldUnits = thematicRendition.getFieldUnits();
			if ((fieldUnits[0] != null) && (!(fieldUnits[0].length() == 0))) {
				g2d.drawString("单位:"+ "（" + fieldUnits[0] + "）",(int)(rect1.getCenterX()-rect1.getWidth()/4),(int)(rect1.getCenterY()+rect1.getHeight()/3));
			} else {
				g2d.drawString("", (int)(rect1.getCenterX()),(int)(rect1.getCenterY()));
			}
		
		g2d.dispose();
		image.flush();
	
		return image;	
	}
	@SuppressWarnings("deprecation")
	public JFreeChart setStruLegend(PieDataset pieDataset,String title,HashMap<String, Color> hashMap,ChartDataPara thematicRendition)  {
		SectorPlot plot =  new SectorPlot(pieDataset);

		JFreeChart pieChart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
				plot, false);
		ChartUtilities.applyCurrentTheme(pieChart);
		// 获得图表对象的引用 设置pie chart背景
		pieChart.setBackgroundPaint(null);
//		TextTitle title = pieChart.getTitle();
//      title.setFont(new Font("黑体",Font.PLAIN,15));
		
        TPiePlot plot1 = (TPiePlot) pieChart.getPlot();
        
        for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
    		plot.setSectionPaint(i,new Color(thematicRendition.getFieldColor()[i]));
        }
        plot1.setDirection(Rotation.ANTICLOCKWISE);
		plot1.setBackgroundPaint(null);
		plot1.setOutlineVisible(false);
		plot1.setInteriorGap(0);
		// 设置饼图标签的绘制字体 ，label
		plot1.setLabelGap(0);
		plot1.setLabelFont(new Font("黑体",Font.PLAIN,10));
		plot1.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
		plot1.setLabelLinkMargin(0);
		plot1.setLabelBackgroundPaint(Color.white);
		plot1.setLabelShadowPaint(null);
        plot1.setLabelOutlinePaint(null);
		plot1.setToolTipGenerator(null);
		plot1.setCircular(true);

		plot1.setShadowPaint(null);
		plot1.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
		plot1.setSectionOutlinesVisible(false);
		plot1.setDirection(Rotation.ANTICLOCKWISE);
		plot1.setStartAngle(90);
		return pieChart;
	
	}



	public HashMap<String, Shape> generateHotArea(double x, double y,
			int width, int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		double sum = 0;
		  for(int i=0;i<indicatorDatas[0].getValues().length;i++){
	        	sum = sum + indicatorDatas[0].getValues()[i]; 
      	}
      double pieRadius = sum*1.0/width/thematicRendition.getScales()[0];//数据改变地图定义文件的scale的值也应变化才能保证比例正确
      pieRadius = Math.sqrt(pieRadius);
      double  interiorGap= 0.4 - pieRadius / 3;
      //饼图大小由整个饼图所占绘图区域的比例来决定
      //同一绘图区域所有指标的最大值为最大半径
      double Rmax = width/2*(1-interiorGap*2);
    	double max = indicatorDatas[0].getValues()[0];  
      for(int i=0;i<indicatorDatas[0].getValues().length;i++){
      	 if(max<indicatorDatas[0].getValues()[i]){
      		 max = indicatorDatas[0].getValues()[i];
     	} 	  	  
       }
    //	double R = width/2*(1-interiorGap*2); 
		double ANGLE = 180;
//		double ANGLE1 = 180;
		double a = 90;
		HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
		//新建多边形,添加多边形的顶点：一指标对应一个扇形，选取该指标所对应扇形的圆心、两个顶点、以及
		//圆弧上面三个等分点来构成多边形的顶点。
		for(int i=0;i<thematicRendition.getFieldName().length;i++){				
			double R = indicatorDatas[0].getValues()[i]/max*Rmax;
		
	
		//	ANGLE = indicatorDatas[0].getValues()[i]/sum*360;	
			if(i==0){
				int[] xPoints = {(int) x,(int) x,(int) (x+R*Math.cos(ANGLE/4*Math.PI/180+a*Math.PI/180)),(int) (x+R*Math.cos(ANGLE/2*Math.PI/180+a*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*3/4*Math.PI/180+a*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*Math.PI/180+a*Math.PI/180))};
				int[] yPoints = {(int) y,(int) (y-R),(int) (y-R*Math.sin(ANGLE/4*Math.PI/180+a*Math.PI/180)),(int) (y-R*Math.sin(ANGLE/2*Math.PI/180+a*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*3/4*Math.PI/180+a*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*Math.PI/180+a*Math.PI/180))};
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
     	//	ANGLE1 = ANGLE1 + indicatorDatas[0].getValues()[i-1]/sum*360;
	       		int[] xPoints = {(int) x,(int) (x),(int) (x+R*Math.cos(315*Math.PI/180)),(int) (x+R),(int) (x+R*Math.cos(45*Math.PI/180)),(int) (x+R*Math.cos(90*Math.PI/180))};      	    
     			int[] yPoints = {(int) y,(int) (y+R),(int) (y-R*Math.sin(315*Math.PI/180)),(int) (y),(int) (y-R*Math.sin(45*Math.PI/180)),(int) (y-R*Math.sin(90*Math.PI/180))}; 		
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
