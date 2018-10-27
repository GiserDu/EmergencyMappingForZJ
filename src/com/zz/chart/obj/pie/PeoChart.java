package com.zz.chart.obj.pie;

import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.render.pie.PeoPlot;
import com.zz.chart.render.pie.TPiePlot;
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
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.HashMap;


public class PeoChart implements IChart {


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
		PeoPlot plot1 = new PeoPlot(pieDataset);
		
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
        	if(interiorGap>0.425){
        		interiorGap = 0.425;
        	}
        plot.setInteriorGap(interiorGap);
        plot.setLabelGenerator(null);
       // x = x+width/2*(1-2*(0.4-1.0/3));
//		y = y-width/2*(1-2*interiorGap);
       
		//绘制饼图
        ChartRenderingInfo info = new 
        		ChartRenderingInfo(new StandardEntityCollection());
	
		Rectangle2D.Double symbolEnvlope =  new 
				Rectangle2D.Double( x-width/2 ,y-width/2 , width,width); 
		//g.draw(symbolEnvlope);
		
		pieChart.draw(g, symbolEnvlope, info);
		
        //设置饼图标签
        if(isLabel){
        	g.setColor(Color.black);
    		Font font = new Font(labelFontName, Font.PLAIN,LabelFontSize);
    		g.setFont(font);//设置字体
    		double stringlength = 0;
    		@SuppressWarnings("unused")
			double stringheight = 0 ; //指标对应的值得字符串长高
//    		double Rmax = width/2;
    		double R = width/2*(1-interiorGap*2);//饼图的半径 
    	 //	double Rmax = width/2*(1-interiorGap*2);
    	 	 double max = indicatorDatas[0].getValues()[0];  
    	     for(int i=0;i<indicatorDatas[0].getValues().length;i++){
    	   	if(max<indicatorDatas[0].getValues()[i]){
    	   		max = indicatorDatas[0].getValues()[i];
    	   	} 	  	  
    	     }
//    		for(int i=0;i<indicatorDatas[0].getValues().length;i++){
				FontMetrics fm = g.getFontMetrics();  
		            Rectangle2D rec=fm.getStringBounds(Float.toString((float)indicatorDatas[0].getValues()[0]), g); 
		//		Angle = indicatorDatas[0].getValues()[i]/sum*360;
		//		String value = Double.toString(indicatorDatas[0].getValues()[i]);
				stringlength = rec.getWidth();//字符串的长度
				stringheight = rec.getHeight();	
				double R1 = indicatorDatas[0].getValues()[0]/max*R;
				
					int labelx = (int)(x-stringlength*0.5);
				    int labely = (int)(y+R-2*R1);
				    g.drawString(Float.toString((float)indicatorDatas[0].getValues()[0]), labelx,(int) (labely));	
				
    	//	}
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
//		String unit = "亿元";
		

		// 绘制结构
		DefaultPieDataset pieStruDataset = new DefaultPieDataset();
		String[] seriesList = thematicRendition.getFieldName();
		for (int i = 0; i < seriesList.length; i++) 
		{
			pieStruDataset.setValue(seriesList[i],maxValues[0]);		
		}
		HashMap<String,Color> hashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldColor().length; i++) {
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
		Rectangle2D.Double rect1 = new Rectangle2D.Double(X-l/10, Y+l/10, l/5, l/2-l/10);
		Rectangle2D.Double rect2 = new Rectangle2D.Double(X-l/6, Y-l/3, l/3, l/3+l/10);
		Arc2D.Double arcDouble = new Arc2D.Double(X-l/10, Y-l/2, l/5, l/5, -75, 330, Arc2D.OPEN);
		
//		Line2D.Double line1 = new Line2D.Double(x-l/2,y-l/2,x-l/2, y+l/2);
//		Line2D.Double line2 = new Line2D.Double(x-l/2,y-l/2,x-l/4,y-l*3/8);
//		Line2D.Double line3 = new Line2D.Double(x-l/4,y-l*3/8,x-l/4,y+l*5/8);
//		Line2D.Double line = new Line2D.Double(x-l/4,y+l*5/8, x-l/2,y+l/2);
		Line2D.Double line4 = new Line2D.Double(X,Y-l/2+l/3*i,X+l/2,Y-l/2+l/3*i);
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
		g2d.draw(rect1);
		g2d.draw(rect2);
		g2d.draw(arcDouble);
	//	g2d.draw(line3);
		g2d.draw(line4);
//		g2d.setFont(new Font("宋体",Font.PLAIN,10));
//		g2d.drawString(Double.toString(sum1),  (int) (x+l/2),(int) (y-l/2+l/3*i));
	//	g2d.drawLine((int)rect.getCenterX()-thematicRendition.getWidth()/2,(int) rect.getCenterY(), (int)rect.getCenterX()+thematicRendition.getWidth(),(int)rect.getCenterY());
		}
		
	//	g2d.drawRect(x, y, symbolWidth, symbolHeight)
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
		PeoPlot plot = new PeoPlot(pieDataset);
		JFreeChart pieChart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
				plot, false);
		ChartUtilities.applyCurrentTheme(pieChart);
		boolean SectionOutlinesVisible = chartStyle.isSectionOutlinesVisible();
		float BaseSectionOutlineStroke = chartStyle.getBaseSectionOutlineStroke();
		int BaseSectionOutlinePaint = chartStyle.getBaseSectionOutlinePaint();
		// 获得图表对象的引用 设置pie chart背景
		pieChart.setBackgroundPaint(null);
//		TextTitle title1 = pieChart.getTitle();
//		title1.setFont(new Font("黑体",Font.PLAIN,15));
        TPiePlot plot1 = (TPiePlot) pieChart.getPlot();
        
        for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
    		plot1.setSectionPaint(i,new Color(thematicRendition.getFieldColor()[i]));
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
		plot1.setSectionOutlinesVisible(chartStyle.isSectionOutlinesVisible());
		if(SectionOutlinesVisible){
			BasicStroke basicStroke = new BasicStroke(
					BaseSectionOutlineStroke);
			plot1.setBaseSectionOutlineStroke(basicStroke);
			plot1.setBaseSectionOutlinePaint(new Color(BaseSectionOutlinePaint,false));
			}
		return pieChart;
	
	}





	public HashMap<String, Shape> generateHotArea(double x, double y,
			int width, int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
	    // x = x+width/2*(1-2*(0.4-1.0/3));
		
		double sum = 0;
		  for(int i=0;i<indicatorDatas[0].getValues().length;i++){
	        	sum = sum + indicatorDatas[0].getValues()[i]; 
		  	}
	  double pieRadius = sum*1.0/width/thematicRendition.getScales()[0];
	  pieRadius = Math.sqrt(pieRadius);
	  double  interiorGap= 0.4 - pieRadius / 3;
	  y = y-width/2*(1-2*interiorGap);
	  double max = indicatorDatas[0].getValues()[0];  
	  for(int i=0;i<indicatorDatas[0].getValues().length;i++){
		if(max<indicatorDatas[0].getValues()[i]){
			max = indicatorDatas[0].getValues()[i];
		} 	  	  
	  }
	  //饼图大小由整个饼图所占绘图区域的比例来决定
	  	double Rmax = width/2*(1-interiorGap*2);
	
		@SuppressWarnings("unused")
		double ANGLE = 0;
//		double ANGLE1 = 0;
		HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
		//新建多边形,添加多边形的顶点：一指标对应一个扇形，选取该指标所对应扇形的圆心、两个顶点、以及
		//圆弧上面三个等分点来构成多边形的顶点。
		for(int i=0;i<thematicRendition.getFieldName().length;i++){				
			double R = indicatorDatas[0].getValues()[i]/max*Rmax;
			ANGLE = indicatorDatas[0].getValues()[i]/sum*360;	
			
				int[] xPoints = {(int) (x+R/5),(int) (x+R/5),(int) (x-R/5),(int)(x-R/5)};
				int[] yPoints = {(int) (y+R),(int) (y-R),(int) (y-R),(int) (y+R)};
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

		

		return hotAreahHashMap;
		
	}



}

