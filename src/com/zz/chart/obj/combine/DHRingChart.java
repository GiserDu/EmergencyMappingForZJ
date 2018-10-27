package com.zz.chart.obj.combine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.HashMap;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.Size2D;
import org.jfree.util.Rotation;

import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.render.pie.TPiePlot;

public class DHRingChart implements IChart {

	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		//获取饼图绘制样式
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		//获取饼图绘制样式
//		float BackgroundAlphas = chartStyle.getBackgroundAlphas();
//		float ForegroundAlphas = chartStyle.getForegroundAlphas();
//		boolean OutlinesVisible = chartStyle.isOutlinesVisble();
		boolean Outlines = chartStyle.isOutLines();// 是否绘制柱子边线
		int outLinePaints = chartStyle.getOutLinePaints();// 柱子边线颜色
		float outLineBasicStrokes = chartStyle.getOutLineBasicStrokes();// 柱子边线粗细
//		double minimumBarLength = chartStyle.getMinimumBarLength();// 百分比
		boolean isLables = chartStyle.isLables();// 是否绘制标签
		String itemLabelFontNames = chartStyle.getItemLabelFontNames();// 标签字体名称
		int itemLabelFontSizes = chartStyle.getItemLabelFontSizes();// 标签字体大小
		int itemLabelPaints = chartStyle.getItemLabelPaints();// 标签颜色
		
		//饼的大小
//        double sum = 0;
//        for(int i=0;i<indicatorDatas[0].getValues().length;i++){
//        	sum = sum + indicatorDatas[0].getValues()[i];
//        }

        double pieRadius1 = indicatorDatas[0].getValues()[0]*1.0/maxValues[0]; 
        pieRadius1 = Math.sqrt(pieRadius1);
        double interiorGap1 = 0.4 - pieRadius1 / 3;
        if(pieRadius1==0){
        	interiorGap1=0.5;
        }
        
        
        double pieRadius2 = indicatorDatas[0].getValues()[1]*1.0/maxValues[0]; 
        pieRadius2 = Math.sqrt(pieRadius2);  
        double interiorGap2 = 0.4 - pieRadius2 / 3;	
        if(pieRadius2==0){
        	interiorGap2=0.5;
        }
//        double max = 0;
//        if(maxValues[1]>maxValues[0]){
//        	max = maxValues[1];
//        }else {
//			max = maxValues[0];
//		}
        
      

//    	double R = width/2*(1-2*interiorGap);//饼图的半径  
//	 	double max = indicatorDatas[0].getValues()[0];  
//	    for(int i=0;i<indicatorDatas[0].getValues().length;i++){
//		   	if(max<indicatorDatas[0].getValues()[i]){
//		   		max = indicatorDatas[0].getValues()[i];
//		   	  } 	  	  
//	    }
	    double R1 =width/2*(1-2*interiorGap1);
	    double R2=width/2*(1-2*interiorGap2);
        Arc2D.Double RingUP = new Arc2D.Double();
        Arc2D.Double RingUPinner = new Arc2D.Double();
        Rectangle2D.Double RectUP = new Rectangle2D.Double(x-R1, y-R1, 2*R1,2*R1);
        Rectangle2D.Double RectUPinner = new Rectangle2D.Double(x-R1/2, y-R1/2, R1, R1);
        Line2D.Double LineUP = new Line2D.Double();
        Arc2D.Double RingDOWN = new Arc2D.Double();
        Arc2D.Double RingDOWNinner = new Arc2D.Double();
        Rectangle2D.Double RectDOWN = new Rectangle2D.Double(x-R2, y-R2, R2*2, R2*2);
        Rectangle2D.Double RectDOWNinner = new Rectangle2D.Double(x-R2/2, y-R2/2, R2, R2);
        Line2D.Double LineDOWN = new Line2D.Double();
        
        double startAngle = 0;
        double arcAngle = 0;
        double Rout = width/2;
        double Rin = width/4;

        	arcAngle = 360/2;
        	//注意圆弧的起始点，为后面连成半环调整
        	RingUP = new Arc2D.Double(RectUP, startAngle, arcAngle, Arc2D.OPEN);   	
        	RingUPinner = new Arc2D.Double(RectUPinner,arcAngle+startAngle, -arcAngle, Arc2D.OPEN);
        	LineUP =  new Line2D.Double(x-R1, y, x+R1, y);
        	g.setColor(new Color(thematicRendition.getFieldColor()[0]));

        	double xPoints[]={x+Rout*Math.cos(startAngle*Math.PI/180),x+Rout*Math.cos((startAngle+arcAngle)*Math.PI/180),x+Rin*Math.cos((startAngle+arcAngle)*Math.PI/180),x+Rin*Math.cos(startAngle*Math.PI/180)};
        	double yPoints[]={y-Rout*Math.sin(startAngle*Math.PI/180),y-Rout*Math.sin((startAngle+arcAngle)*Math.PI/180),y-Rin*Math.sin((startAngle+arcAngle)*Math.PI/180),y-Rin*Math.sin(startAngle*Math.PI/180)};
        	
        	GeneralPath HRingUP = new GeneralPath();
        	HRingUP.moveTo(xPoints[2], yPoints[2]);
        	HRingUP.append(RingUPinner, true);	
        	HRingUP.lineTo(xPoints[0], yPoints[0]);
        	HRingUP.append(RingUP, true);
        	
        	//HRingUP.lineTo(xPoints[0], yPoints[0]);
        	HRingUP.closePath();
        	
        	g.fill(HRingUP);
//        	g.setColor(Color.BLACK);
        	//g.draw(HRingUP);
//        	g.draw(RingUP);
//        	g.draw(RingUPinner);
//        	g.draw(LineUP);
        	if(Outlines){
        		g.setColor(new Color(outLinePaints,false));
        		BasicStroke basicStroke = new BasicStroke(outLineBasicStrokes);
        		g.setStroke(basicStroke);
        		g.draw(RingUP);
            	g.draw(RingUPinner);
            	g.draw(LineUP);
        	 
        	}
        	if (isLables) {
				Font font = new Font(itemLabelFontNames, Font.PLAIN,
						itemLabelFontSizes);
				g.setColor(new Color(itemLabelPaints, false));
				g.setFont(font);
				
				g.drawString(Float.toString((float) indicatorDatas[0].getValues()[0]), (int)(x+R1),
						(int) (y-R1/4*3));
				g.setColor(Color.BLACK);
        		BasicStroke basicStroke = new BasicStroke(1f);
        		g.setStroke(basicStroke);
        		g.drawLine((int)x, (int)(y-R1/4*3), (int)(x+R1), (int)(y-R1/4*3));
			}
        

        	arcAngle = 360/2;
        	//注意圆弧的起始点，为后面连成半环调整
        	RingDOWN = new Arc2D.Double(RectDOWN, startAngle+180, arcAngle, Arc2D.OPEN);   	
        	RingDOWNinner = new Arc2D.Double(RectDOWNinner,(180+arcAngle+startAngle), -arcAngle, Arc2D.OPEN);
        	LineDOWN =  new Line2D.Double(x-R2, y, x+R2, y);
        	g.setColor(new Color(thematicRendition.getFieldColor()[1]));     	
        	double xPoints1[]={x+Rout*Math.cos((180+startAngle)*Math.PI/180),x+Rout*Math.cos((180+startAngle+arcAngle)*Math.PI/180),x+Rin*Math.cos((180+startAngle+arcAngle)*Math.PI/180),x+Rin*Math.cos((180+startAngle)*Math.PI/180)};
        	double yPoints1[]={y-Rout*Math.sin((180+startAngle)*Math.PI/180),y-Rout*Math.sin((180+startAngle+arcAngle)*Math.PI/180),y-Rin*Math.sin((180+startAngle+arcAngle)*Math.PI/180),y-Rin*Math.sin((180+startAngle)*Math.PI/180)};
        	//startAngle += arcAngle;
        	GeneralPath HRingDOWN = new GeneralPath();
        	HRingDOWN.moveTo(xPoints1[2], yPoints1[2]);
        	HRingDOWN.append(RingDOWNinner, true);	
        	HRingDOWN.lineTo(xPoints1[0], yPoints1[0]);
        	HRingDOWN.append(RingDOWN, true);
        	//HRingUP.lineTo(xPoints[0], yPoints[0]);
        	HRingDOWN.closePath();	
        	g.fill(HRingDOWN);

        	if(Outlines){
        		g.setColor(new Color(outLinePaints,false));
        		BasicStroke basicStroke = new BasicStroke(outLineBasicStrokes);
        		g.setStroke(basicStroke);
        	   	g.draw(RingDOWNinner);
            	g.draw(RingDOWN);
            	g.draw(LineDOWN);
        	}
        	if (isLables) {
				Font font = new Font(itemLabelFontNames, Font.PLAIN,
						itemLabelFontSizes);
				g.setColor(new Color(itemLabelPaints, false));
				g.setFont(font);
				g.drawString(Float.toString((float) indicatorDatas[0].getValues()[1]), (int)(x+R1),
						(int) (y+R2/4*3));
				g.setColor(Color.BLACK);
        		BasicStroke basicStroke = new BasicStroke(1f);
        		g.setStroke(basicStroke);
        		g.drawLine((int)x, (int)(y+R1/4*3), (int)(x+R1), (int)(y+R1/4*3));
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
		for (int i = 0; i < thematicRendition.getFieldColor().length; i++) {
			hashMap.put(thematicRendition.getFieldName()[i], new Color(thematicRendition.getFieldColor()[i],false));
		}
		JFreeChart pieStruChart = this.setStruLegend(pieStruDataset,"",hashMap, thematicRendition);
		BufferedImage pieStruLegend = pieStruChart.createBufferedImage(width*2, (int) (width),null);
	
		// 绘制构成标题
		TextTitle struTitle = pieStruChart.getTitle();
		struTitle.setText("图例");
		struTitle.setFont(new Font("黑体", Font.PLAIN, 25));		
		struTitle.setMargin(1, 1, 1, 1);
		BufferedImage imgStruTitle = new BufferedImage(1, 1,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2StruTitle = imgStruTitle.createGraphics();
        Size2D sizeStruTitle = struTitle.arrange(g2StruTitle);
        g2StruTitle.dispose();
        int wStruTitle = (int) Math.rint(sizeStruTitle.width);
        int hStruTitle = (int) Math.rint(sizeStruTitle.height);
  
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

        
        int maxH = hStruTitle + pieStruLegend.getHeight();
        
		BufferedImage image = new BufferedImage((int) (width+100),maxH+width+hStruTitle, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.createGraphics();
		

//		g2d.setBackground(Color.WHITE);
//		g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
//		
		struTitle.draw(g2d, new Rectangle2D.Double((image.getWidth()-wStruTitle)/2,0,wStruTitle,hStruTitle));
		g2d.drawImage(pieStruLegend,(image.getWidth()-pieStruLegend.getWidth())/2,hStruTitle,pieStruLegend.getWidth(),pieStruLegend.getHeight(), null);
//		amountTitle.draw(g2d, new Rectangle2D.Double((image.getWidth()-wAmountTitle)/2,hStruTitle+pieStruLegend.getHeight(),wAmountTitle,hAmountTitle));
//		g2d.drawImage(bubbleSymbol,(image.getWidth()-bubbleSymbol.getWidth())/2,hStruTitle+pieStruLegend.getHeight()+hAmountTitle,bubbleSymbol.getWidth(),bubbleSymbol.getHeight(), null);	
		g2d.drawRect(0,0, image.getWidth()-1, image.getHeight()-1);
		
		Rectangle2D.Double rect1 = new Rectangle2D.Double(1, hStruTitle, width+100, width);
//		g2d.draw(rect1);
		boolean Outlines = chartStyle.isOutLines();// 是否绘制柱子边线
		int outLinePaints = chartStyle.getOutLinePaints();// 柱子边线颜色
		float outLineBasicStrokes = chartStyle.getOutLineBasicStrokes();// 柱子边线粗细
//		double minimumBarLength = chartStyle.getMinimumBarLength();// 百分比
	
		String itemLabelFontNames = chartStyle.getItemLabelFontNames();// 标签字体名称
		int itemLabelFontSizes = chartStyle.getItemLabelFontSizes();// 标签字体大小
		int itemLabelPaints = chartStyle.getItemLabelPaints();// 标签颜色
		double x = rect1.getCenterX();
		double y = rect1.getCenterY();
		 double sum = 0;
	        for(int i=0;i<indicatorDatas[0].getValues().length;i++){
	        	sum = sum + indicatorDatas[0].getValues()[i];
	        }
	        double pieRadius = sum*1.0/width/thematicRendition.getScales()[0];
	        pieRadius = Math.sqrt(pieRadius);
	        double interiorGap = 0.4 - 1.0 / 3;
	        	
//	        double max = 0;
//	        if(maxValues[1]>maxValues[0]){
//	        	max = maxValues[1];
//	        }else {
//				max = maxValues[0];
//			}
	        
	      
//	        double Rmax =width/2;
	    	double R = width/2*(1-2*interiorGap);//饼图的半径  
		 	double max = indicatorDatas[0].getValues()[0];  
		    for(int i=0;i<indicatorDatas[0].getValues().length;i++){
			   	if(max<indicatorDatas[0].getValues()[i]){
			   		max = indicatorDatas[0].getValues()[i];
			   	  } 	  	  
		    }
		    double R1 = R;
		    double R2 = R;
	        Arc2D.Double RingUP = new Arc2D.Double();
	        Arc2D.Double RingUPinner = new Arc2D.Double();
	        Rectangle2D.Double RectUP = new Rectangle2D.Double(x-R1, y-R1, 2*R1,2*R1);
	        Rectangle2D.Double RectUPinner = new Rectangle2D.Double(x-R1/2, y-R1/2, R1, R1);
	        Line2D.Double LineUP = new Line2D.Double();
	        Arc2D.Double RingDOWN = new Arc2D.Double();
	        Arc2D.Double RingDOWNinner = new Arc2D.Double();
	        Rectangle2D.Double RectDOWN = new Rectangle2D.Double(x-R2, y-R2, R2*2, R2*2);
	        Rectangle2D.Double RectDOWNinner = new Rectangle2D.Double(x-R2/2, y-R2/2, R2, R2);
	        Line2D.Double LineDOWN = new Line2D.Double();
	        
	        double startAngle = 0;
	        double arcAngle = 0;
	        double Rout = width/2;
	        double Rin = width/4;

	        	arcAngle = 360/2;
	        	//注意圆弧的起始点，为后面连成半环调整
	        	RingUP = new Arc2D.Double(RectUP, startAngle, arcAngle, Arc2D.OPEN);   	
	        	RingUPinner = new Arc2D.Double(RectUPinner,arcAngle+startAngle, -arcAngle, Arc2D.OPEN);
	        	LineUP =  new Line2D.Double(x-R1/2, y, x+R1/2, y);
	        	g2d.setColor(new Color(thematicRendition.getFieldColor()[0]));

	        	double xPoints[]={x+Rout*Math.cos(startAngle*Math.PI/180),x+Rout*Math.cos((startAngle+arcAngle)*Math.PI/180),x+Rin*Math.cos((startAngle+arcAngle)*Math.PI/180),x+Rin*Math.cos(startAngle*Math.PI/180)};
	        	double yPoints[]={y-Rout*Math.sin(startAngle*Math.PI/180),y-Rout*Math.sin((startAngle+arcAngle)*Math.PI/180),y-Rin*Math.sin((startAngle+arcAngle)*Math.PI/180),y-Rin*Math.sin(startAngle*Math.PI/180)};
	        	
	        	GeneralPath HRingUP = new GeneralPath();
	        	HRingUP.moveTo(xPoints[2], yPoints[2]);
	        	HRingUP.append(RingUPinner, true);	
	        	HRingUP.lineTo(xPoints[0], yPoints[0]);
	        	HRingUP.append(RingUP, true);
	        	
	        	//HRingUP.lineTo(xPoints[0], yPoints[0]);
	        	HRingUP.closePath();
	        	
	        	g2d.fill(HRingUP);
//	        	g.setColor(Color.BLACK);
	        	//g.draw(HRingUP);
//	        	g.draw(RingUP);
//	        	g.draw(RingUPinner);
//	        	g.draw(LineUP);
	        	if(Outlines){
	        		g2d.setColor(new Color(outLinePaints,false));
	        		BasicStroke basicStroke = new BasicStroke(outLineBasicStrokes);
	        		g2d.setStroke(basicStroke);
	        		g2d.draw(RingUP);
	            	g2d.draw(RingUPinner);
	            	g2d.draw(LineUP);
	        	 
	        	}
//	        	if (isLables) {
					Font font = new Font(itemLabelFontNames, Font.PLAIN,
							itemLabelFontSizes);
					g2d.setColor(new Color(itemLabelPaints, false));
					g2d.setFont(font);
					
					g2d.drawString(indicatorDatas[0].getNames()[0], (int)(x+R1),
							(int) (y-R1/4*3));
					g2d.setColor(Color.BLACK);
	        		BasicStroke basicStroke = new BasicStroke(1f);
	        		g2d.setStroke(basicStroke);
	        		g2d.drawLine((int)x, (int)(y-R1/4*3), (int)(x+R1), (int)(y-R1/4*3));
//				}
	        

	        	arcAngle = 360/2;
	        	//注意圆弧的起始点，为后面连成半环调整
	        	RingDOWN = new Arc2D.Double(RectDOWN, startAngle+180, arcAngle, Arc2D.OPEN);   	
	        	RingDOWNinner = new Arc2D.Double(RectDOWNinner,(180+arcAngle+startAngle), -arcAngle, Arc2D.OPEN);
	        	LineDOWN =  new Line2D.Double(x-R2/2, y, x+R2/2, y);
	        	g2d.setColor(new Color(thematicRendition.getFieldColor()[1]));     	
	        	double xPoints1[]={x+Rout*Math.cos((180+startAngle)*Math.PI/180),x+Rout*Math.cos((180+startAngle+arcAngle)*Math.PI/180),x+Rin*Math.cos((180+startAngle+arcAngle)*Math.PI/180),x+Rin*Math.cos((180+startAngle)*Math.PI/180)};
	        	double yPoints1[]={y-Rout*Math.sin((180+startAngle)*Math.PI/180),y-Rout*Math.sin((180+startAngle+arcAngle)*Math.PI/180),y-Rin*Math.sin((180+startAngle+arcAngle)*Math.PI/180),y-Rin*Math.sin((180+startAngle)*Math.PI/180)};
	        	//startAngle += arcAngle;
	        	GeneralPath HRingDOWN = new GeneralPath();
	        	HRingDOWN.moveTo(xPoints1[2], yPoints1[2]);
	        	HRingDOWN.append(RingDOWNinner, true);	
	        	HRingDOWN.lineTo(xPoints1[0], yPoints1[0]);
	        	HRingDOWN.append(RingDOWN, true);
	        	//HRingUP.lineTo(xPoints[0], yPoints[0]);
	        	HRingDOWN.closePath();	
	        	g2d.fill(HRingDOWN);

	        	if(Outlines){
	        		g2d.setColor(new Color(outLinePaints,false));
	        		BasicStroke basicStroke1 = new BasicStroke(outLineBasicStrokes);
	        		g2d.setStroke(basicStroke1);
	        	   	g2d.draw(RingDOWNinner);
	            	g2d.draw(RingDOWN);
	            	g2d.draw(LineDOWN);
	        	}
//	        	if (isLables) {
					Font font1 = new Font(itemLabelFontNames, Font.PLAIN,
							itemLabelFontSizes);
					g2d.setColor(new Color(itemLabelPaints, false));
					g2d.setFont(font1);
					g2d.drawString(indicatorDatas[0].getNames()[1], (int)(x+R1),
							(int) (y+R2/4*3));
					g2d.setColor(Color.BLACK);
	        		BasicStroke basicStroke1 = new BasicStroke(1f);
	        		g2d.setStroke(basicStroke1);
	        		g2d.drawLine((int)x, (int)(y+R1/4*3), (int)(x+R1), (int)(y+R1/4*3));
//				}
		//		Rectangle2D.Double pierect = new Rectangle2D.Double((image.getWidth()-pieStruLegend.getWidth())/2,hStruTitle,pieStruLegend.getWidth(),pieStruLegend.getHeight());
		//g2d.draw(pierect);
		Rectangle2D.Double rect = new Rectangle2D.Double(1, width+hStruTitle, width+100, width+50);
		g2d.setColor(Color.BLACK);
//		g2d.draw(rect);
//		FontMetrics fm = g2d.getFontMetrics();  
//        Rectangle2D rec=fm.getStringBounds(Double.toString(width*thematicRendition.getScales()[0]), g2d); 
	
//		String value = Double.toString(indicatorDatas[0].getValues()[i]);
//        double  stringlength = rec.getWidth();//字符串的长度
//        double stringheight = rec.getHeight();	
		for(int i=0;i<3;i++){
//		double interiorGap = 0.4 - 1.0 / 3;
		double X = rect.getCenterX();
		double Y = rect.getCenterY();
//		double R = width/2*(1-2*interiorGap);
//		double R1 = width/2;
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
		g2d.drawString(temp,  (int) (X-1.5*R),(int) (Y-R+R*i/2));
		
		}
		
	//	g2d.drawRect(x, y, symbolWidth, symbolHeight)
		
		
		g2d.dispose();
		image.flush();
	
		return image;	
	}
	
	@SuppressWarnings({ "deprecation" })
	public JFreeChart setStruLegend(PieDataset pieDataset,String title,HashMap<String, Color> hashMap,ChartDataPara thematicRendition)  {
		TPiePlot plot = new TPiePlot(pieDataset);
		JFreeChart pieChart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
				plot, false);
		ChartUtilities.applyCurrentTheme(pieChart);
	
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
		plot1.setInteriorGap(0.5);
		// 设置饼图标签的绘制字体 ，label
		plot1.setLabelGap(0);
		plot1.setLabelFont(new Font("黑体",Font.PLAIN,10));
		//plot1.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
	//	plot1.setLabelLinkMargin(0.05);
		plot1.setLabelBackgroundPaint(Color.white);
		plot1.setLabelShadowPaint(null);
        plot1.setLabelOutlinePaint(null);
		plot1.setToolTipGenerator(null);
		plot1.setCircular(true);		
		plot1.setShadowPaint(null);	
		plot1.setSectionOutlinesVisible(false);
		plot1.setLabelGenerator(null);
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
		double ANGLE1 = 180;
//		double a =0;
		HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
		//新建多边形,添加多边形的顶点：一指标对应一个扇形，选取该指标所对应扇形的圆心、两个顶点、以及
		//圆弧上面三个等分点来构成多边形的顶点。
			for(int i=0;i<indicatorDatas[0].getNames().length;i++)	{
			double R = indicatorDatas[0].getValues()[i]/max*Rmax;
			 double R1 = R/2;
	
		//	ANGLE = indicatorDatas[0].getValues()[i]/sum*360;	
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
  				text = "(" + indicatorDatas[0].getNames()[0]+")="+indicatorDatas[0].getValues()[0];
				}else {
					text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[0]+")="+indicatorDatas[0].getValues()[0];
				}
  		 	//String text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
  			hotAreahHashMap.put(text, polygon); 			
			}
			if(i==1){
//				ANGLE1 = ANGLE1 + indicatorDatas[0].getValues()[i-1]/sum*360;
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
	  			hotAreahHashMap.put(text, polygon); 
			}
			}	
	return hotAreahHashMap;
		
	}


}
