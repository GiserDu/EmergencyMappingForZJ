package com.zz.chart.obj.pie;

import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.render.pie.TPiePlot;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.Size2D;
import org.jfree.util.Rotation;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class BrickChart implements IChart {

	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub	
//		boolean SectionOutlinesVisible = chartStyle.isSectionOutlinesVisible();
		boolean SectionOutlinesVisible =true;
		float BaseSectionOutlineStroke = chartStyle.getBaseSectionOutlineStroke();
//		int BaseSectionOutlinePaint = chartStyle.getBaseSectionOutlinePaint();
		int BaseSectionOutlinePaint = 1;
		boolean isLabel = chartStyle.isbLabel();
		int LabelFontSize = chartStyle.getLabelFontSize();
		String labelFontName = chartStyle.getLabelFontName();
		// 消除线条锯齿
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		
		int a=width/20;	//最大值对应小方块边长
		double b=90;	//斜角
		
//		int transparent = chartStyle.getTransparent();// 填充色透明度
		double[] indicators;
		indicators = indicatorDatas[0].getValues();
		Color [] colors=new Color[thematicRendition.getFieldColor().length];
		for (int i = 0; i < thematicRendition.getFieldColor().length; i++) {
			colors[i]=new Color( thematicRendition.getFieldColor()[i], false);
		}
		
		
			double sum = 0;
			for (int i = 0; i < indicatorDatas[0].getValues().length; i++) {
				sum = sum + indicatorDatas[0].getValues()[i];
	
			}
		 double pieRadius = sum*1.0/width/thematicRendition.getScales()[0];
	        pieRadius = Math.sqrt(pieRadius);
	        double interiorGap = 0.1 - pieRadius / 3;
//		a=sum*1.0/(width*thematicRendition.getScales()[0])*a;
//		a = sum/maxValues[0]*a;
		
	     a= (int) ((int)a*(1-2*interiorGap));  
		int[] normaindi=new int[indicators.length];//使数据转化为小方块的个数
    	for (int i = 0; i < normaindi.length; i++) {
			normaindi[i]=(int) (indicators[i]/sum*100);
//			System.out.println(normaindi[i]);
		}
		
		int[] fillnumber=new int[normaindi.length+1];//填色序号端点
    	for (int i = 0; i < fillnumber.length; i++) {
    		if (i==0) {
				fillnumber[i]=0;
			}else {
				fillnumber[i]=fillnumber[i-1]+normaindi[i-1];
//				System.out.println(fillnumber[i]);
			}
			
		}

		x = x+width/5;
		y = y+height/5;
    	ArrayList<Polygon> bricks = bricks(x, y, a, b);
    	ArrayList<Polygon> brectangles =brectangles(x, y, a,b);
    	ArrayList<Polygon> lrectangles = lrectangles(x, y, a, b);
    	
    	HashMap<Polygon, Color>has=new HashMap<Polygon, Color>();
    	
    	//涂最后一个指标之前的颜色
    	for (int i = 0; i < fillnumber.length-2; i++) {
			for (int j = fillnumber[i]; j < fillnumber[i+1]; j++) {
				g.setColor(colors[i]);
				g.fillPolygon(bricks.get(j));
				has.put(bricks.get(j), colors[i]);
			}
		}
    	//涂最后一个指标的颜色
    	for (int i = fillnumber[fillnumber.length-2]; i < 100; i++) {
			g.setColor(colors[colors.length-1]);
			g.fillPolygon(bricks.get(i));
			has.put(bricks.get(i), colors[colors.length-1]);
		}
    	
    	//涂上部颜色
//    	for (int i = 0; i < 10; i++) {
//    		int j=90+i;
//			Color color = has.get(bricks.get(j));
//			g.setColor(Color.gray);
//			g.fillPolygon(brectangles.get(i));
//			g.setColor(Color.white);
//			g.drawPolygon(brectangles.get(i));
//		}
    	
    	//涂侧边颜色
    	for (int i = 0; i <10; i++) {
			g.setColor(Color.GRAY);
			g.fillPolygon(lrectangles.get(i));
			g.fillPolygon(brectangles.get(i));
    	}
    	
    	if(SectionOutlinesVisible){
    		BasicStroke basicStroke = new BasicStroke(BaseSectionOutlineStroke);
    		g.setStroke(basicStroke);
	    	g.setColor(new Color(BaseSectionOutlinePaint));
	    	for (int i = 0; i < 100; i++) {
				g.drawPolygon(bricks.get(i));
	    	}
    	}
    	if(isLabel){
    		g.setColor(Color.black);
    		Font font = new Font(labelFontName, Font.PLAIN,LabelFontSize);
    		g.setFont(font);
    		g.drawString(Float.toString((float)sum),(int) (x-a*10), (int)(y-a*11));
    	}
    		
    }
    


	public Polygon brick(double x ,double y, double a, double b){
//		int height = (int)(a*Math.sin(Math.PI*b/180));		
    	int[] X={(int) x,(int)(x-a),(int) (x-a),(int) (x)};
    	int[] Y={(int) y,(int) y,(int) (y-a),(int) (y-a)};
    	Polygon brick =new Polygon(X,Y,4);
    	return brick;
     }
    
	//100个方块
	public ArrayList<Polygon> bricks(double x,double y,double a,double b){
		ArrayList<Polygon>bricks = new ArrayList<Polygon>();
		for (int i = 0; i < 10; i++) {
    		for (int j = 0; j < 10; j++) {
    			Polygon brick = brick(x, y, a, b);
				x=(double) (x-a);
				bricks.add(brick);
			}
    		double angle = Math.PI*b/180;
    		x= (int)(x+a*10+a*Math.cos(angle));
    		y= y-(int)(a*Math.sin(angle));
		}
		return bricks;
	}
	//上部方块
	public ArrayList<Polygon> brectangles(double x ,double y, double a,double b){
		ArrayList<Polygon>bricks=bricks(x, y, a, b);
		ArrayList<Polygon> brectangles=new ArrayList<Polygon>();
		for (int i = 0; i < 10; i++) {
			int j = 90+i;
			int x1=bricks.get(j).xpoints[3];
			int x2=bricks.get(j).xpoints[2];
			int x3=(int) (x2+a*Math.cos(Math.PI*45/180));
			int x4=(int) (x1+a*Math.cos(Math.PI*45/180));
			int y1=bricks.get(j).ypoints[3];
			int y2=y1;
			int y3=(int) (bricks.get(j).ypoints[2]-a*Math.sin(Math.PI*45/180));
			int y4=y3;
			int[] X = {x1,x2,x3,x4};
			int[] Y = {y1,y2,y3,y4};
			Polygon polygon = new Polygon(X, Y, 4);
			x=(int)(x-a);
			brectangles.add(polygon);
    	}
		return brectangles; 
	}
    
	//侧边方块
	public ArrayList<Polygon> lrectangles(double x ,double y, double a,double b) {
		ArrayList<Polygon>bricks=bricks(x, y, a, b);
		ArrayList<Polygon>lrectangles = new ArrayList<Polygon>();
    	for (int i = 0; i < 10; i++) {
    		int j=i*10;
    		int x1=bricks.get(j).xpoints[0];
    		int x2=bricks.get(j).xpoints[3];
    		int x3=(int) (x2+a*Math.cos(Math.PI*45/180));
    		int x4=x3;
    		int y1=bricks.get(j).ypoints[0];
    		int y2=bricks.get(j).ypoints[3];
    		int y3=(int) (y2-a*Math.sin(Math.PI*45/180));
    		int y4=(int) (y3+a);
			int[] X = {x1,x2,x3,x4};
			int[] Y = {y1,y2,y3,y4};
			Polygon polygon = new Polygon(X,Y,4);
			lrectangles.add(polygon);
		}
    	return lrectangles;

	}


	public BufferedImage drawLegend(int width, int height,
			ChartDataPara thematicRendition, ChartStyle chartStyle,
			double[] maxValues, double[] minValues, double[] averageValues,
			IndicatorData[] indicatorDatas) {
		// TODO Auto-generated method stub
		// 绘制结构
		DefaultPieDataset pieStruDataset = new DefaultPieDataset();
		String[] seriesList = thematicRendition.getFieldName();
		for (int i = 0; i < seriesList.length; i++) 
		{
			pieStruDataset.setValue(seriesList[i],0);		
		}
		HashMap<String,Color> hashMap = new HashMap<String, Color>();
		for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
			hashMap.put(thematicRendition.getFieldName()[i], new Color(thematicRendition.getFieldColor()[i],true));
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
//		unitTitle.setText("单位："+"（"+unit+"）");
		unitTitle.setFont(new Font("黑体", Font.PLAIN, 20));
		unitTitle.setMargin(1, 1, 1, 1);
		BufferedImage imgUnit = new BufferedImage(1, 1,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2Unit = imgUnit.createGraphics();
//        Size2D sizeUnit = unitTitle.arrange(g2Unit);
        g2Unit.dispose();
//        int wUnite = (int) Math.rint(sizeUnit.width);
//        int hUnit = (int) Math.rint(sizeUnit.height);

        
        int maxH = hStruTitle + pieStruLegend.getHeight();
        
		BufferedImage image = new BufferedImage((int) (width*1.2+100),(int) (hStruTitle+maxH+width/4*thematicRendition.getFieldColor().length+20), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) image.createGraphics();

//		g2d.setBackground(Color.WHITE);
//		g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
		struTitle.draw(g2d, new Rectangle2D.Double((image.getWidth()-wStruTitle)/2,0,wStruTitle,hStruTitle));
		g2d.drawImage(pieStruLegend,(image.getWidth()-pieStruLegend.getWidth())/2,hStruTitle,pieStruLegend.getWidth(),pieStruLegend.getHeight(), null);
//		amountTitle.draw(g2d, new Rectangle2D.Double((image.getWidth()-wAmountTitle)/2,hStruTitle+pieStruLegend.getHeight(),wAmountTitle,hAmountTitle));
//		g2d.drawImage(bubbleSymbol,(image.getWidth()-bubbleSymbol.getWidth())/2,hStruTitle+pieStruLegend.getHeight()+hAmountTitle,bubbleSymbol.getWidth(),bubbleSymbol.getHeight(), null);	
//		g2d.drawRect(0,0, image.getWidth()-1, image.getHeight()-1);
		
		Rectangle2D.Double rect = new Rectangle2D.Double(1, 3, width+100, width+50);
		g2d.setColor(Color.BLACK);
		//g2d.draw(rect);
//		FontMetrics fm = g2d.getFontMetrics();  
//        Rectangle2D rec=fm.getStringBounds(Double.toString(width*thematicRendition.getScales()[0]), g2d); 
        double interiorGap = 0.4 - 1.0 / 3;
    	double X = rect.getCenterX();
		double Y = rect.getCenterY();
        double l = width*(1-2*interiorGap);
		for(int i=0;i<3;i++){
		//Arc2D.Double arc = new Arc2D.Double(rect.getCenterX()-thematicRendition.getWidth()/2, rect.getCenterY()-thematicRendition.getWidth()/2+thematicRendition.getWidth()*i/8, thematicRendition.getWidth()-thematicRendition.getWidth()*i/4, thematicRendition.getWidth()-thematicRendition.getWidth()*i/4, 0, 180,Arc2D.OPEN );
		//Ellipse2D.Double oval = new Ellipse2D.Double(rect.getCenterX()-thematicRendition.getWidth()/2+thematicRendition.getWidth()*i/8, rect.getCenterY()-thematicRendition.getWidth()/2+thematicRendition.getWidth()*i/4, thematicRendition.getWidth()-thematicRendition.getWidth()*i/4, thematicRendition.getWidth()-thematicRendition.getWidth()*i/4);		
		Rectangle2D.Double rect1 = new Rectangle2D.Double(X-l/2+i*l/8, Y-l/2+i*l/4, l-i*l/4, l-i*l/4);
		Line2D.Double line4 = new Line2D.Double(X,Y-l/2+l/4*i,X+l*2/3,Y-l/2+l/4*i);
		NumberFormat ddf1 = NumberFormat.getNumberInstance();
		
		double sum1 = maxValues[0]*(1-i*1.0/4);
		if (sum1 < 100) {
			ddf1.setMaximumFractionDigits(2);
		} else {
			ddf1.setMaximumFractionDigits(0);
		}
		String temp = ddf1.format(sum1);
		g2d.draw(rect1);
		//g2d.draw(rect2);
		//g2d.draw(arcDouble);
	//	g2d.draw(line3);
		g2d.draw(line4);
		g2d.setFont(new Font("宋体",Font.PLAIN,10));
		g2d.drawString(temp,  (int) (X+l*2/3),(int) (Y-l/2+l/4*i));
	//	g2d.drawLine((int)rect.getCenterX()-thematicRendition.getWidth()/2,(int) rect.getCenterY(), (int)rect.getCenterX()+thematicRendition.getWidth(),(int)rect.getCenterY());
		}
	
		for (int j = 0; j < thematicRendition.getFieldColor().length; j++) {
			
		

		Rectangle2D.Double rect2 =new Rectangle2D.Double(X-width/4, Y+width/2+width/4*j, width/6, width/6);

		

		g2d.setColor(new Color(thematicRendition.getFieldColor()[j]));
		g2d.fill(rect2);
		g2d.setColor(Color.BLACK);
		g2d.drawString(thematicRendition.getFieldName()[j], (int)X, (int)Y+width/2+width/4*(j+1)-width/7);
		
        }
		
		
		Rectangle2D.Double rect1 = new Rectangle2D.Double(1, width+hStruTitle, width, height);
		g2d.setFont(new Font("黑体", Font.PLAIN, 16));
	
		String[] fieldUnits = thematicRendition.getFieldUnits();
		if ((fieldUnits[0] != null) && (!(fieldUnits[0].length() == 0))) {
			g2d.drawString("单位:"+ "（" + fieldUnits[0] + "）",(int)(rect1.getCenterX()+ rect1.getWidth()/10),(int)(rect1.getCenterX()+rect1.getHeight()*0.85));
//			g2d.setColor(Color.red);
//			g2d.draw(rect1);
////			g2d.setColor(Color.blue);
//			g2d.draw(rect);
////			g2d.setColor(Color.yellow);
//			g2d.drawRect(0, 0, width, height);
		} else {
			g2d.drawString("", (int)(rect1.getCenterX()),(int)(rect1.getCenterY()));
		}
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
		plot1.setInteriorGap(0);
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
        double pieRadius = sum*1.0/width/thematicRendition.getScales()[0];
        pieRadius = Math.sqrt(pieRadius);
        double  interiorGap= 0.4 - pieRadius / 3;
        //饼图大小由整个饼图所占绘图区域的比例来决定
        double R = width/2*(1-interiorGap*2); 
		double ANGLE = 0;
		double ANGLE1 = 0;
		HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
		//新建多边形,添加多边形的顶点：一指标对应一个扇形，选取该指标所对应扇形的圆心、两个顶点、以及
		//圆弧上面三个等分点来构成多边形的顶点。
		for(int i=0;i<thematicRendition.getFieldName().length;i++){				
			ANGLE = indicatorDatas[0].getValues()[i]/sum*360;	
       		if(i==0){
				int[] xPoints = {(int) x,(int) (x+R),(int) (x+R*Math.cos(ANGLE/4*Math.PI/180)),(int) (x+R*Math.cos(ANGLE/2*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*3/4*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*Math.PI/180))};
				int[] yPoints = {(int) y,(int) y,(int) (y-R*Math.sin(ANGLE/4*Math.PI/180)),(int) (y-R*Math.sin(ANGLE/2*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*3/4*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*Math.PI/180))};
				Polygon polygon = new Polygon();
				for(int j=0;j<xPoints.length;j++){
					polygon.addPoint(xPoints[j], yPoints[j]);	
		       	}
				String text = "";

				if (thematicRendition.getDomainAxis()[0] == null
						|| (thematicRendition.getDomainAxis()[0].length() == 0)) {
					text = "("+indicatorDatas[0].getNames()[i] + ")="
					+ indicatorDatas[0].getValues()[i];
				}else {
					text = "(" + thematicRendition.getDomainAxis()[0] + ","
					+ indicatorDatas[0].getNames()[i] + ")="
					+ indicatorDatas[0].getValues()[i];
				}
				hotAreahHashMap.put(text, polygon);

	       	}else{
       		ANGLE1 = ANGLE1 + indicatorDatas[0].getValues()[i-1]/sum*360;
       			int[] xPoints = {(int) x,(int) (x+R*Math.cos(ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*Math.PI/180+ANGLE1*Math.PI/180))};      	    
    			int[] yPoints = {(int) y,(int) (y-R*Math.sin(ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*Math.PI/180+ANGLE1*Math.PI/180))}; 		
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
