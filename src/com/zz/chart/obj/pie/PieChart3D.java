package com.zz.chart.obj.pie;

import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.render.pie.TPiePlot3D;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.Size2D;
import org.jfree.util.Rotation;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.HashMap;


public class PieChart3D implements IChart {

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
		TPiePlot3D plot1 = new TPiePlot3D(pieDataset);
		plot1.setInsets(new RectangleInsets(0.0, 5.0, 5.0, 5.0));
		
	     JFreeChart pieChart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
	             plot1, false);
	     ChartUtilities.applyCurrentTheme(pieChart);
         
		pieChart.setBackgroundPaint(null);
		
		// 获得图表对象的引用  
		TPiePlot3D plot=(TPiePlot3D)pieChart.getPlot();
		
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
		double TiltAngle = chartStyle.getTiltAngle();
		double LabelLinkMargin = chartStyle.getLabelLinkMargin();
		double DepthFactor = chartStyle.getDepthFactor();
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
        plot.setLabelGenerator(null);
        plot.setLabelGap(1);	
        
        //设置饼图标签
//        if(isLabel){
//      	plot.setLabelGap(LabelGap);
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
//				plot.setLabelGenerator(null);
//			}
    
      	 plot.setDepthFactor(DepthFactor);
		//绘制饼图
        ChartRenderingInfo info = new 
        		ChartRenderingInfo(new StandardEntityCollection());
	
		Rectangle2D.Double symbolEnvlope =  new 
				Rectangle2D.Double( x-width/2, y-width*TiltAngle/2, width, width*TiltAngle); 
//		g.draw(symbolEnvlope);
		pieChart.draw(g, symbolEnvlope, info);

		
		//绘制标签
		if(isLabel){
		g.setColor(Color.black);
		Font font = new Font(labelFontName, Font.PLAIN,LabelFontSize);
		g.setFont(font);//设置字体
		double stringlength = 0;
		@SuppressWarnings("unused")
		double stringheight = 0 ; //指标对应的值得字符串长高
//		double Rmax = width/2;
		double R = width/2*TiltAngle*(1-interiorGap*2);//饼图的半径 
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

//		int symbolWidth = (int)(1079* 1+width);
//		int symbolHeight = (int)(679* 1.25+height);

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
//	
//		String value = Double.toString(indicatorDatas[0].getValues()[i]);
//        double  stringlength = rec.getWidth();//字符串的长度
//        double stringheight = rec.getHeight();	
        for(int i=0;i<3;i++){
    		double interiorGap = 0.4 - 1.0 / 3;
    		double X = rect.getCenterX();
    		double Y = rect.getCenterY();
    		double R = width/2*(1-2*interiorGap);
//    		double R1 = width/2;
    		Ellipse2D.Double oval = new Ellipse2D.Double(X-R+R/4*i,Y-R+R/2*i,2*(R-R/4*i),2*(R-R/4*i));
    		Line2D.Double line = new Line2D.Double(X-1.3*R, Y-R+R*i/2, X,  Y-R+R*i/2);
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
    		g2d.draw(oval);
    		g2d.setFont(new Font("宋体",Font.PLAIN,10));
    		g2d.drawString(temp,  (int) (X-1.8*R),(int) (Y-R+R*i/2+3));
    		
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
	public JFreeChart setStruLegend(PieDataset pieDataset,String title,HashMap<String, Color> hashMap,ChartDataPara thematicRendition)  {

		JFreeChart chart = ChartFactory.createPieChart3D
		(
            title,				// 图表标题
            pieDataset,             // 数据集
            false,                   // 定义图例
            true,
            false
        );
		ChartUtilities.applyCurrentTheme(chart);
		// 获得图表对象的引用 设置pie chart背景
		chart.setBackgroundPaint(null);
//		TextTitle title = pieChart.getTitle();
//      title.setFont(new Font("黑体",Font.PLAIN,15));
		
        PiePlot3D plot1 = (PiePlot3D) chart.getPlot();
        
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
//		plot1.setSectionDepth(0.5);
		plot1.setShadowPaint(null);
		
		plot1.setSectionOutlinesVisible(false);
		return chart;
	
	}



	public HashMap<String, Shape> generateHotArea(double x, double y,
			int width, int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		double TiltAngle = chartStyle.getTiltAngle();
		 double DepthFactor = chartStyle.getDepthFactor();
//		double sum = 0;
//		  for(int i=0;i<indicatorDatas[0].getValues().length;i++){
//	        	sum = sum + indicatorDatas[0].getValues()[i]; 
//      	}
//      double pieRadius = sum*1.0/width/thematicRendition.getScales()[0];
//      pieRadius = Math.sqrt(pieRadius);
//      double  interiorGap= 0.4 - pieRadius / 3;
//      //饼图大小由整个饼图所占绘图区域的比例来决定
//      double R = width*2/2.75/2*(1-interiorGap*2); 
//		double ANGLE = 0;
//		double ANGLE1 = 0;
//		HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
//		//新建多边形,添加多边形的顶点：一指标对应一个扇形，选取该指标所对应扇形的圆心、两个顶点、以及
//		//圆弧上面三个等分点来构成多边形的顶点。
//	
//			for(int i=0;i<thematicRendition.getFieldName().length;i++){				
//				ANGLE = indicatorDatas[0].getValues()[i]/sum*360;	
//	       		if(i==0){
//					int[] xPoints = {(int) x,(int) (x+R),(int) (x+R*Math.cos(ANGLE/4*Math.PI/180)),(int) (x+R*Math.cos(ANGLE/2*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*3/4*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*Math.PI/180))};
//					int[] yPoints = {(int) y,(int) y,(int) (y-R*Math.sin(ANGLE/4*Math.PI/180)),(int) (y-R*Math.sin(ANGLE/2*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*3/4*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*Math.PI/180))};
//					Polygon polygon = new Polygon();
//					for(int j=0;j<xPoints.length;j++){
//						polygon.addPoint(xPoints[j], yPoints[j]);	
//					}
//					String text = "";
//					if (thematicRendition.getDomainAxis()[0] == null||thematicRendition.getDomainAxis()[0]=="") {
//	    				text = "(" + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
//					}else {
//						text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
//					}
//	    		 	//String text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
//	    			hotAreahHashMap.put(text, polygon);
//	       	}else{
//	       		ANGLE1 = ANGLE1 + indicatorDatas[0].getValues()[i-1]/sum*360;
//       			int[] xPoints = {(int) x,(int) (x+R*Math.cos(ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*Math.PI/180+ANGLE1*Math.PI/180))};      	    
//    			int[] yPoints = {(int) y,(int) (y-R*Math.sin(ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*Math.PI/180+ANGLE1*Math.PI/180))}; 		
//    		 	Polygon polygon = new Polygon();
//    			for(int j=0;j<xPoints.length;j++){
//    				polygon.addPoint(xPoints[j], yPoints[j]);
//			       	}
//    			String text = "";
//				if (thematicRendition.getDomainAxis()[0] == null||thematicRendition.getDomainAxis()[0]=="") {
//    				text = "(" + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
//				}else {
//					text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
//				}
//    		 	//String text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
//    			hotAreahHashMap.put(text, polygon);
//		       	}			
//		}
//		return hotAreahHashMap;
//	}
//
		  double sum = 0;
	        for(int i=0;i<indicatorDatas[0].getValues().length;i++){
	        	sum = sum + indicatorDatas[0].getValues()[i];
	        }
	      double pieRadius = sum*1.0/width/thematicRendition.getScales()[0];
	      pieRadius = Math.sqrt(pieRadius);
	      double  interiorGap= 0.4 - pieRadius / 3;
//			  double R = width/2*(1-interiorGap*2); 
		      //double R1 = 
		    //  double R1 = width/2*(1-0.15*2)*0.5;
		      System.out.println(width);
				double ANGLE = 0;
				double ANGLE1 = 0;
//				Rectangle2D.Double symbolEnvlope1 =  new 
//				Rectangle2D.Double(x-width*0.7/2, y-width*2*0.7/2.75/2, width*0.7, width*2*0.7/2.75); 
//				Rectangle2D.Double symbolEnvlope2 =  new 
//				Rectangle2D.Double(x-width/4, y-width/2/2.75, width/2, width/2.75); 
				Rectangle2D.Double symbolEnvlope =  new 
				Rectangle2D.Double( x-width/2*(1-interiorGap*2), y-width*TiltAngle/2*(1-interiorGap*2)*(1-DepthFactor/4), width*(1-interiorGap*2), width*TiltAngle*(1-interiorGap*2)*(1-DepthFactor)); 
//				Rectangle2D.Double(x-width/4, y-width/4/1.5, width/2, width/2/1.5); 
				HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
				//新建多边形,添加多边形的顶点：一指标对应一个扇形，选取该指标所对应扇形的圆心、两个顶点、以及
				//圆弧上面三个等分点来构成多边形的顶点。
//				 Rectangle2D.Double rectangleDouble = new Rectangle2D.Double();
					
					
				for(int i=0;i<thematicRendition.getFieldName().length;i++){				
					Arc2D.Double arc1 = new Arc2D.Double();
//					Arc2D.Double arc2 = new Arc2D.Double();
					
						
						//饼
					 ANGLE = indicatorDatas[0].getValues()[i]/sum*360;							
					 arc1 = new Arc2D.Double(symbolEnvlope, ANGLE1, ANGLE, 2);
//					 arc2 = new Arc2D.Double(symbolEnvlope2, ANGLE1, ANGLE, 2);
					 //	Area areaArc1 = new Area(arc1);							
//						Area areaArc2 = new Area(arc2);
//						areaArc1.subtract(areaArc2);
					ANGLE1 += ANGLE;

//						Area area1 = new Area(areaArc1);
//						Area area2 = new Area(rectangleDouble);
//						area1.subtract(area2);
						//g.draw(area1);
						String text = "";
						if (thematicRendition.getDomainAxis()[0] == null
								|| (thematicRendition.getDomainAxis()[0].length() == 0)) {
		    				text = "(" + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
						}else {
							text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
						}
		    			hotAreahHashMap.put(text, arc1);
		}
		return hotAreahHashMap;

	}


}