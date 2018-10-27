package com.zz.chart.obj.combine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.HashMap;

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

import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.render.pie.TRingPlot;

public class BRing2DChart  implements IChart {

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
			float BackgroundAlphas = chartStyle.getBackgroundAlphas();
			float ForegroundAlphas = chartStyle.getForegroundAlphas();
			boolean OutlinesVisible = chartStyle.isOutlinesVisble();
			boolean Outlines = chartStyle.isOutLines();// 是否绘制柱子边线
			int outLinePaints = chartStyle.getOutLinePaints();// 柱子边线颜色
			float outLineBasicStrokes = chartStyle.getOutLineBasicStrokes();// 柱子边线粗细
//			double minimumBarLength = chartStyle.getMinimumBarLength();// 百分比
			boolean isLables = chartStyle.isLables();// 是否绘制标签
			String itemLabelFontNames = chartStyle.getItemLabelFontNames();// 标签字体名称
			int itemLabelFontSizes = chartStyle.getItemLabelFontSizes();// 标签字体大小
			int itemLabelPaints = chartStyle.getItemLabelPaints();// 标签颜色
//			int transparent = chartStyle.getTransparent();// 填充色透明度
			//设置饼图颜色

			for(int i=0;i<thematicRendition.getFieldName().length;i++){
				plot.setSectionPaint(i, new Color(thematicRendition.getFieldColor()[i]));
			}
			//设置饼图样式
			plot.setBackgroundAlpha(BackgroundAlphas);
			plot.setForegroundAlpha(ForegroundAlphas);
//			plot.setSectionOutlinesVisible(OutlinesVisible);
			plot.setOutlineVisible(OutlinesVisible);
			if(Outlines){
			BasicStroke basicStroke = new BasicStroke(
					outLineBasicStrokes);
			plot.setBaseSectionOutlineStroke(basicStroke);
			plot.setBaseSectionOutlinePaint(new Color(outLinePaints,false));
			}
			plot.setShadowPaint(null);
			plot.setCircular(false);
			plot.setDirection(Rotation.ANTICLOCKWISE);
			plot.setStartAngle(0);
			plot.setIgnoreNullValues(true);
			plot.setIgnoreZeroValues(true);
			plot.setNoDataMessage("");          
	        plot.setNoDataMessageFont(new Font("宋体", Font.ITALIC, 20)); // 设置没有数据时显示的信息的字体         
	        plot.setNoDataMessagePaint(Color.orange); // 设置没有数据时显示的信息的颜色  
	        
//	        //设置饼图大小
	        double sum = 0;
	        for(int i=0;i<indicatorDatas[0].getValues().length;i++){
	        	sum = sum + indicatorDatas[0].getValues()[i];
	        }
//	        double pieRadius = sum*1.0/width/thematicRendition.getScale();
//	        pieRadius = Math.sqrt(pieRadius);
//	        double interiorGap = 0.4 - pieRadius / 3;
//	        if(interiorGap<0.425){
//	        	plot.setInteriorGap(interiorGap);
//	        }else {
				plot.setInteriorGap(0.1);
//			}
			
	        //环状饼图特
	        plot.setSectionDepth(0.5);
	        plot.setLabelGenerator(null);
			//绘制饼图
	        ChartRenderingInfo info = new 
	        		ChartRenderingInfo(new StandardEntityCollection());
		
			Rectangle2D.Double symbolEnvlope =  new 
					Rectangle2D.Double( x-width/2 ,y-width/2 , width,width); 

			pieChart.draw(g, symbolEnvlope, info);
		
		
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

//			DefaultCategoryDataset barDataset = new DefaultCategoryDataset();// 存放绘制柱状图所用统计数据的数据集
			double maxValue = maxValues[1];
			double averageValue = averageValues[1];
			
			double[] values = new double[1];// 存放单变量多指标的值，例如存放2012年男女指标的人口数

			if (indicatorDatas[1] ==null) {
				System.out.println("绘制专题符号图层时发现专题数据中区域数据不完整！");
				return;
			}
			int group = Integer.parseInt(thematicRendition.getDomainAxis()[0]);
			values[0] = indicatorDatas[0].getValues()[group];// 因为普通柱状图只适合单变量多指标这样的数据结构，其余的不适用
			
			if (maxValue > (averageValue * 4)) {
				maxValue = averageValue * 4 * (1 + 1.0/9);
			}else {
				maxValue = maxValue * (1 + 1.0/9);
			}
//			
//			if (null == values) {
//				System.out.println("区域数据为空！");
//				return;
//			} else {
//				// 将数据放入柱状图数据集中
//				for (int i = 0; i < values.length; i++) {
//					barDataset.addValue(values[i], indicatorDatas[0].getNames()[i],
//							indicatorDatas[0].getDomainAxis());
//				}
//			}
		

			// 处理极端数据
//
//			HashMap<String, Color> fieldNameAndColorHashMap = new HashMap<String, Color>();
//			for (int i = 0; i < thematicRendition.getFieldColor().length; i++) {
//				fieldNameAndColorHashMap.put(thematicRendition.getFieldName()[i],
//						new Color(transparent * 256 * 256 * 256
//								+ thematicRendition.getFieldColor()[i], true));
//			}

	//		double realItemMargin = itemMargin * (values.length - 1) * 1.0 / width;// 根据设置的柱子间的间距，求出所有间距之和占据绘图区域宽度的百分比
	//		double barWidth = (width * (1 - realItemMargin) / values.length);
			double barWidth = width/2*(1-0.275*2);
			if (barWidth > width * 0.51) {
				barWidth = width * 0.51;
			}

			for (int i = 0; i < values.length; i++) {
				if (values[0] > (averageValue * 4)) {
					int barX0 = (int) x - (int) (barWidth / 2);
//					if (barWidth < width * 0.51) {
//						barX0 = (int) x - (int) (width / 2)
//								+ (int) (barWidth + itemMargin) * i;
//					} else {
//						barX0 = (int) (x - width * 0.51 / 2);
//					}
					int barHeight = (int) (0.8 * height);// 断裂前80%
					int innerHeight = (int) (0.02 * height);// 锯齿高度
					int innerWidth = (int) (barWidth / 3);// 锯齿宽度
					int barY0 = (int) y;

					int[] xpointsDown = { barX0, barX0, barX0 + innerWidth,
							barX0 + 2 * innerWidth, barX0 + (int) barWidth,
							barX0 + (int) barWidth };
					int[] ypointsDown = { barY0, barY0 - barHeight,
							barY0 - barHeight + innerHeight, barY0 - barHeight,
							barY0 - barHeight + innerHeight, barY0 };
					int npointsDown = xpointsDown.length;
					Polygon polygonDown = new Polygon(xpointsDown, ypointsDown,
							npointsDown);

					barHeight = barHeight + innerHeight;
					int[] xpointsUp = { barX0, barX0, barX0 + innerWidth,
							barX0 + 2 * innerWidth, barX0 + (int) barWidth,
							barX0 + (int) barWidth };
					int[] ypointsUp = { barY0 - (int) (0.9 * height),
							barY0 - barHeight, barY0 - barHeight + innerHeight,
							barY0 - barHeight, barY0 - barHeight + innerHeight,
							barY0 - (int) (0.9 * height) };
					int npointsUp = xpointsUp.length;
					Polygon polygonUP = new Polygon(xpointsUp, ypointsUp, npointsUp);
//
//					GradientPaint gradientPaint = new GradientPaint((float) barX0,
//							(float) (barY0), fieldNameAndColorHashMap
//									.get(thematicRendition.getFieldName()[i]),
//							(float) (barX0 + barWidth / 2), (float) (barY0),
//							new Color(255, 255, 255, 255), true);
					//g.setPaint(gradientPaint);
					g.setColor(new Color(thematicRendition.getFieldColor()[group],false));
					g.fill(polygonUP);
					g.fill(polygonDown);

					// 断裂处理的必须标上标签
					Font font = new Font(itemLabelFontNames, Font.PLAIN,
							itemLabelFontSizes);
					g.setColor(new Color(itemLabelPaints, false));
					g.setFont(font);
					g.drawString(Float.toString((float) values[i]), (int) barX0,
							(int) barY0 - (int) (0.9 * height));

					if (Outlines) {

						g.setColor(new Color(outLinePaints, false));
						BasicStroke basicStroke1 = new BasicStroke(
								outLineBasicStrokes);
						g.setStroke(basicStroke1);
						g.draw(polygonUP);
						g.draw(polygonDown);
					}

				} else {
					//int barX0 = 0;
					int barX0 = (int) x - (int) (barWidth / 2);
//					if (barWidth < width * 0.51) {
//						barX0 = (int) x - (int) (width / 2)
//								+ (int) (barWidth + itemMargin) * i;
//					} else {
//						barX0 = (int) (x - width * 0.51 / 2);
//					}
					double barY0 = y;
					double barHeight = values[0] / maxValue * height;
//					if (barHeight < minimumBarLength) {
//						barHeight = minimumBarLength;
//					}
					Rectangle2D.Double bar = new Rectangle2D.Double(barX0, barY0
							- barHeight, barWidth, barHeight);
//					GradientPaint gradientPaint = new GradientPaint((float) barX0,
//							(float) (barY0), fieldNameAndColorHashMap
//									.get(thematicRendition.getFieldName()[i]),
//							(float) (barX0 + barWidth / 2), (float) (barY0),
//							new Color(255, 255, 255, 255), true);
					//g.setPaint(gradientPaint);
					g.setColor(new Color(thematicRendition.getFieldColor()[group],false));
					g.fill(bar);
					if (isLables) {
						Font font = new Font(itemLabelFontNames, Font.PLAIN,
								itemLabelFontSizes);
						g.setColor(new Color(itemLabelPaints, false));
						g.setFont(font);
						g.drawString(Float.toString((float) values[i]), barX0,
								(int) (barY0 - barHeight));
					}
					if (Outlines) {
						g.setColor(new Color(outLinePaints, false));
						BasicStroke basicStroke1 = new BasicStroke(
								outLineBasicStrokes);
						g.setStroke(basicStroke1);
						g.draw(bar);
					}
				}
			}
			//实验热区
			 //shiyanrequ
			  double R = width/2*(1-0.1*2); 
		      //double R1 = 
		      double R1 = width/2*(1-0.1*2)*0.5;
				double ANGLE = 0;
				double ANGLE1 = 0;
			
//				HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
				//新建多边形,添加多边形的顶点：一指标对应一个扇形，选取该指标所对应扇形的圆心、两个顶点、以及
				//圆弧上面三个等分点来构成多边形的顶点。
				for(int i=0;i<thematicRendition.getFieldName().length;i++){				
					Polygon polygon1 = new Polygon();
					 Polygon polygon2 = new Polygon();
					 Rectangle2D.Double rectangleDouble = new Rectangle2D.Double();
					
						//柱子
//						double[] values1;// 存放单变量多指标的值，例如存放2012年男女指标的人口数

//						values1 = indicatorDatas[0].getValues();// 因为普通柱状图只适合单变量多指标这样的数据结构，其余的不适用
//						if (null == values) {
//							System.out.println("区域数据为空！");
//							return null;
//						}
						
						if (maxValue > (averageValue * 4)) {
							maxValue = averageValue * 4 * (1 + 1.0/9);
						}else {
							maxValue = maxValue * (1 + 1.0/9);
						}
						int barX0 = (int) x - (int) (barWidth / 2);
							
							if (indicatorDatas[0].getValues()[1] > (averageValue * 4)) {
								int barHeight = (int) (0.9 * height);// 断裂前80%
//								int innerHeight = (int) (0.02 * height);// 锯齿高度
//							
//								int barY0 = (int) y;
								//barHeight = barHeight + innerHeight;
								rectangleDouble = new Rectangle2D.Double(barX0, y
										- barHeight, barWidth,barHeight);
								//g.draw(rectangleDouble);
							} else {
								double barHeight = values[0] / maxValue * height;
								rectangleDouble = new Rectangle2D.Double(barX0, y
										- barHeight,
										barWidth, barHeight);
								//g.draw(rectangleDouble);
							}
						//	g.draw(rectangleDouble);
						//饼
					 ANGLE = indicatorDatas[0].getValues()[i]/sum*360;	
		     		if(i==0){
						int[] xPoints = {(int) (x+R1),(int) (x+R),(int) (x+R*Math.cos(ANGLE/4*Math.PI/180)),(int) (x+R*Math.cos(ANGLE/2*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*3/4*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE*3/4*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE/2*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE/4*Math.PI/180))};
						int[] yPoints = {(int) y,(int) y,(int) (y-R*Math.sin(ANGLE/4*Math.PI/180)),(int) (y-R*Math.sin(ANGLE/2*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*3/4*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE*3/4*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE/2*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE/4*Math.PI/180))};
						
						for(int j=0;j<xPoints.length;j++){
							polygon1.addPoint(xPoints[j], yPoints[j]);	
							g.setColor(Color.red);
							
				       	}
					//	g.draw(polygon1);
						 Area area1 = new Area(polygon1);
							
							Area area3 = new Area(rectangleDouble);
							area1.subtract(area3);
					//		g.draw(area1);
			       	}else{
		     		ANGLE1 = ANGLE1 + indicatorDatas[0].getValues()[i-1]/sum*360;
		    		int[] xPoints = {(int) (x+R1*Math.cos(ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180)),
	 						
	 						(int) (x+R*Math.cos(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE*Math.PI/180+ANGLE1*Math.PI/180)),
	 						
	 						(int) (x+R1*Math.cos(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180))};
			
		    		int[] yPoints = {(int) (y-R1*Math.sin(ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180)),
	 						
	 						(int) (y-R*Math.sin(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*Math.PI/180+ANGLE1*Math.PI/180)),
	 						
	 						(int) (y-R1*Math.sin(ANGLE*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180))};
	      
		  			for(int j=0;j<xPoints.length;j++){
		  				polygon2.addPoint(xPoints[j], yPoints[j]);
		  				g.setColor(Color.blue);
		  			}
		  			 Area area2 = new Area(polygon2);
						
						Area area3 = new Area(rectangleDouble);
//						area1.subtract(area3);
//						g.draw(area1);
		  			area2.subtract(area3);
		  		//	g.draw(area2);
			       	}

				}
		}
			

	public BufferedImage drawLegend(int width, int height,
			ChartDataPara thematicRendition, ChartStyle chartStyle,
			double[] maxValues, double[] minValues, double[] averageValues,
			IndicatorData[] indicatorDatas) {
			   double sum = 0;
			   for(int i=0;i<indicatorDatas[0].getValues().length;i++){
		      	   sum = sum + indicatorDatas[0].getValues()[i]; 
		  	      }
			    double pieRadius = sum*1.0/width/thematicRendition.getScales()[0];
			    pieRadius = Math.sqrt(pieRadius);
			    double interiorGap = 0.5 - pieRadius / 2;
			    	if(interiorGap>0.425){
			    		interiorGap = 0.425;
			    	}
			    	double maxValue = maxValues[1];
					double averageValue = averageValues[1];
					
					double[] values = new double[1];// 存放单变量多指标的值，例如存放2012年男女指标的人口数

//					if (indicatorDatas[1] ==null) {
//						System.out.println("绘制专题符号图层时发现专题数据中区域数据不完整！");
//						return;
//					}
					int group = Integer.parseInt(thematicRendition.getDomainAxis()[0]);
					values[0] = indicatorDatas[0].getValues()[group];// 因为普通柱状图只适合单变量多指标这样的数据结构，其余的不适用
					
					if (maxValue > (averageValue * 4)) {
						maxValue = averageValue * 4 * (1 + 1.0/9);
					}else {
						maxValue = maxValue * (1 + 1.0/9);
					}
			
			int symbolWidth = (int)(1079* 1);
			int symbolHeight = (int)(679* 1.25);
//			int symbolWidth = width + 120;
//			int symbolHeight = (int) (66 + height + 33 + 21 );
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
			for (int i = 0; i < thematicRendition.getFieldColor().length; i++) {
				hashMap.put(thematicRendition.getFieldName()[i], new Color(thematicRendition.getFieldColor()[i],false));
			}
			JFreeChart pieStruChart = this.setStruLegend(pieStruDataset,"",hashMap, thematicRendition);
			BufferedImage pieStruLegend = pieStruChart.createBufferedImage(symbolWidth*1/5+30, (int)symbolHeight*1/8+30,null);
		
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
	      //绘制单位
			TextTitle unitTitle = new TextTitle();
			unitTitle.setText("单位："+"（"+unit+"）");
			unitTitle.setFont(new Font("黑体", Font.PLAIN, 20));
			unitTitle.setMargin(1, 1, 1, 1);
			BufferedImage imgUnit = new BufferedImage(1, 1,BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2Unit = imgUnit.createGraphics();
	        Size2D sizeUnit = unitTitle.arrange(g2Unit);
	        g2Unit.dispose();
	        int wUnite = (int) Math.rint(sizeUnit.width);
	        int hUnit = (int) Math.rint(sizeUnit.height);
	      //绘制图例
			LegendTitle legend = new LegendTitle(pieStruChart.getPlot());
			legend.setItemFont(new Font("黑体", Font.PLAIN, 12));
	        legend.setMargin(1, 1, 1, 1);
	        legend.setPosition(RectangleEdge.RIGHT);
	        legend.setHorizontalAlignment(HorizontalAlignment.RIGHT);
	        BufferedImage img = new BufferedImage(1, 1,
	                BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2 = img.createGraphics();
	        Size2D size = legend.arrange(g2);
	        g2.dispose();
	        int w = (int) Math.rint(size.width);
	        int h = (int) Math.rint(size.height);
			
			
	        int maxW1 = Math.max(wStruTitle, wUnite) ;
	        int maxW2 = Math.max(wStruTitle,w) ;
	        int maxW = Math.max(maxW1, maxW2) +100;
	        
	        int maxH = hStruTitle + hUnit + pieStruLegend.getHeight() + h+70;
	        
			BufferedImage image = new BufferedImage(maxW,maxH, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = (Graphics2D) image.createGraphics();

			g2d.setBackground(Color.WHITE);
			g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			
			struTitle.draw(g2d, new Rectangle2D.Double((image.getWidth()-wStruTitle)/2,0,wStruTitle,hStruTitle));
			g2d.setColor(Color.black);
			g2d.drawImage(pieStruLegend,(image.getWidth()-pieStruLegend.getWidth())/4,hStruTitle,pieStruLegend.getWidth(),pieStruLegend.getHeight(), null);
//			amountTitle.draw(g2d, new Rectangle2D.Double((image.getWidth()-wAmountTitle)/2,hStruTitle+pieStruLegend.getHeight(),wAmountTitle,hAmountTitle));
//			g2d.drawImage(bubbleSymbol,(image.getWidth()-bubbleSymbol.getWidth())/2,hStruTitle+pieStruLegend.getHeight()+hAmountTitle,bubbleSymbol.getWidth(),bubbleSymbol.getHeight(), null);	
			g2d.drawRect(0,0, image.getWidth()-1, image.getHeight()-1);
			
			
			Rectangle2D.Double rect = new Rectangle2D.Double(10, 150, 220, 160);
			g2d.setColor(Color.BLACK);
//			g2d.draw(rect);
//			FontMetrics fm = g2d.getFontMetrics();  
//	        Rectangle2D rec=fm.getStringBounds(Double.toString(width*thematicRendition.getScale()), g2d); 
//		
////			String value = Double.toString(indicatorDatas[0].getValues()[i]);
//	        double  stringlength = rec.getWidth();//字符串的长度
//	        double stringheight = rec.getHeight();	
//			for(int i=0;i<3;i++){
//				
//			Ellipse2D.Double oval = new Ellipse2D.Double(rect.getCenterX()-thematicRendition.getWidth()/2+thematicRendition.getWidth()*i/8, rect.getCenterY()-thematicRendition.getWidth()/2+thematicRendition.getWidth()*i/4, thematicRendition.getWidth()-thematicRendition.getWidth()*i/4, thematicRendition.getWidth()-thematicRendition.getWidth()*i/4);
//			Line2D.Double line = new Line2D.Double(rect.getCenterX()-thematicRendition.getWidth()/2-stringlength/8, rect.getCenterY()-thematicRendition.getWidth()/2+thematicRendition.getWidth()*i/4, rect.getCenterX(),  rect.getCenterY()-thematicRendition.getWidth()/2+thematicRendition.getWidth()*i/4);
//			double sum1 = width*thematicRendition.getScale()*(1-i*1.0/4);
//			g2d.draw(line);
//			g2d.draw(oval);
//			g2d.setFont(new Font("宋体",Font.PLAIN,10));
//			g2d.drawString(Double.toString(sum1),  (int) (rect.getCenterX()-thematicRendition.getWidth()/2-stringlength/2),(int) rect.getCenterY()-thematicRendition.getWidth()/2+thematicRendition.getWidth()*i/4);
//			
//			}
			
		//	g2d.drawRect(x, y, symbolWidth, symbolHeight)
		

//			String fieldUnits = thematicRendition.getFieldUnit();

			String[] fieldList = thematicRendition.getFieldName();

			// 获取符号绘制样式
		
//			String chartID = chartStyle.getChartID();// 符号ID
//			boolean drawBarOutline = chartStyle.isDrawBarOutLine();// 是否绘制柱子边线
//			int outLinePaint = chartStyle.getOutLinePaint();// 柱子边线颜色
//			float outLineBasicStroke = chartStyle.getOutLineBasicStroke();// 柱子边线粗细
//			double itemMargin = chartStyle.getItemMargin();// 柱子间的距离，像素为单位
//			int transparent = chartStyle.getTransparent();// 填充色透明度

//			HashMap<String, Color> fieldNameAndColorHashMap = new HashMap<String, Color>();
//			for (int i = 0; i < thematicRendition.getFieldColor().length; i++) {
//				fieldNameAndColorHashMap.put(thematicRendition.getFieldName()[i],
//						new Color(transparent * 256 * 256 * 256
//								+ thematicRendition.getFieldColor()[i], true));
//			}
//			double realItemMargin = itemMargin
//					* (thematicRendition.getDomainAxis().length - 1) * 1.0
//					/ thematicRendition.getWidth();// 根据设置的柱子间的间距，求出所有间距之和占据绘图区域宽度的百分比
			double barWidth = width/2*(1-0.25*2);
//			double barHeight = values[0] / maxValue * height;
			//double maxBarWidth = 0.51 * height;
//			if (barWidth > maxBarWidth) {
//				barWidth = maxBarWidth;
//			}
	//		maxValue = thematicRendition.getScale() * width;
			

//			BufferedImage image1 = new BufferedImage(symbolWidth, symbolHeight,
//					BufferedImage.TYPE_INT_ARGB);
//			Graphics2D g = (Graphics2D) image1.getGraphics();
//			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//					RenderingHints.VALUE_ANTIALIAS_ON);
//			g.setBackground(Color.WHITE);
//			g.clearRect(0, 0, symbolWidth, symbolHeight);
//			g2d.setColor(Color.BLACK);
//			g2d.draw(new Rectangle(1, 1, symbolWidth - 2, symbolHeight - 2));
//
//			g2d.setFont(new Font("黑体", Font.PLAIN, 25));
//			g2d.drawString("图例", symbolWidth / 2 - 25, 30);
			g2d.setFont(new Font("黑体", Font.PLAIN, 12));
//			if ((fieldUnits != null) && (!(fieldUnits.length() == 0))) {
//				g2
//						.drawString("单位：（" + fieldUnits + "）",
//								symbolWidth / 2 - 60, 55);
//			}
			double X = rect.getCenterX();
			double Y = rect.getCenterY()-height/4;
			
			// axis
			Line2D rangeAxis = new Line2D.Double(rect.getCenterX(),
					Y+height/2, X, Y-height/2);
			Line2D domainAxis = new Line2D.Double(X,
					Y+height/2, X+width/2,
					Y+height/2);

			g2d.draw(domainAxis);
			g2d.draw(rangeAxis);
//
//			if (barWidth > maxBarWidth) {
//				barWidth = maxBarWidth;
//			}

			// 刻度
			Font font = new Font("宋体", Font.PLAIN, 12);
			g2.setFont(font);
			g2.setColor(Color.BLACK);
			for (int i = 0; i < thematicRendition.getFieldName().length + 1; i++) {
				Line2D line2dRange = new Line2D.Double(domainAxis.getX1() - 2,
						domainAxis.getY2() - height
								/ thematicRendition.getFieldName().length * i,
						domainAxis.getX1(), domainAxis.getY2() - height
								/ thematicRendition.getFieldName().length * i);
				g2d.draw(line2dRange);
				FontMetrics fm1 = g2.getFontMetrics();
				NumberFormat ddf1 = NumberFormat.getNumberInstance();
				if (maxValue < 10) {
					ddf1.setMaximumFractionDigits(2);
				} else {
					ddf1.setMaximumFractionDigits(0);
				}
				  ddf1.setGroupingUsed(false);
				String tempString = ddf1.format(maxValue
						/ thematicRendition.getFieldName().length * i);
				Rectangle2D rec1 = fm1.getStringBounds(tempString, g2);

				g2d.drawString(tempString, (int) (domainAxis.getX1() - 2
						- rec1.getWidth() - 5), (int) (domainAxis.getY2() - height
						/ thematicRendition.getFieldName().length * i));
			}
			for (int i = 0; i < 1; i++) {
				Line2D line2dDomain;
				if (barWidth < width * 0.5) {
					line2dDomain = new Line2D.Double(domainAxis.getX1() + barWidth
							/ 2 + (barWidth ) * i, domainAxis.getY2(),
							domainAxis.getX1() + barWidth / 2
									+ (barWidth ) * i, domainAxis
									.getY2() + 2);
				} else {
					line2dDomain = new Line2D.Double(
							domainAxis.getX1() + width / 2, domainAxis.getY2(),
							domainAxis.getX1() + width / 2, domainAxis.getY2() + 2);

				}
				g2d.draw(line2dDomain);
				g2d.drawString(thematicRendition.getFieldName()[1],
						(int) line2dDomain.getX1() - 10,
						(int) line2dDomain.getY2() + 2 + 10);
			}

			// 图
			double x = X + barWidth * 1.0 ;
			double y = Y+height/2;

//			int yOffSet = (int) (barWidth / 4);
			//int xOffSet = (int) (barWidth / 4);

			int barX0 = (int) x - (int) (barWidth / 2);
//			if (barWidth < width * 0.51) {
//				barX0 = (int) x - (int) (width / 2)
//						+ (int) (barWidth + itemMargin) * i;
//			} else {
//				barX0 = (int) (x - width * 0.51 / 2);
//			}
			int barY0 = (int) (y);
//			double barHeight = values / maxValue * height;
//			if (barHeight < minimumBarLength) {
//				barHeight = minimumBarLength;
//			}

//			GradientPaint gradientPaint = new GradientPaint((float) barX0,
//					(float) (barY0), fieldNameAndColorHashMap
//							.get(thematicRendition.getFieldName()[i]),
//					(float) (barX0 + barWidth / 2), (float) (barY0),
//					new Color(255, 255, 255, 255), true);

//			double barHeight = values[i] / maxValue * height;
//			if (barHeight < minimumBarLength) {
//				barHeight = minimumBarLength;
//			}
			Rectangle2D.Double bar = new Rectangle2D.Double(barX0, barY0
					- height, barWidth, height);
//			GradientPaint gradientPaint = new GradientPaint((float) barX0,
//					(float) (barY0), fieldNameAndColorHashMap
//							.get(thematicRendition.getFieldName()[1]),
//					(float) (barX0 + barWidth / 2), (float) (barY0),
//					new Color(255, 255, 255, 255), true);
			g2d.setColor(new Color(thematicRendition.getFieldColor()[group],true));
			g2d.fill(bar);
//			if (isLable) {
//				Font font = new Font(itemLabelFontName, Font.PLAIN,
//						itemLabelFontSize);
//				g.setColor(new Color(itemLabelPaint, true));
//				g.setFont(font);
//				g.drawString(java.lang.Double.toString(values[i]), barX0,
//						(int) (barY0 - barHeight));
//			}
//			if (drawBarOutline) {
				g2d.setColor(Color.black);
				BasicStroke basicStroke = new BasicStroke(
						0.5f);
				g2d.setStroke(basicStroke);
				g2d.draw(bar);
//			}


					
				
				

			

			// 图例
			FontMetrics fm1 = g2d.getFontMetrics();
			Rectangle2D rec1 = fm1.getStringBounds(fieldList[0], g2);
			Font font2 = new Font("宋体", Font.PLAIN, 15);
			int xx = symbolWidth / 2 - 6 - (int) (rec1.getWidth() / 2);
			for (int i = 0; i < fieldList.length; i++) {

				Rectangle2D.Double fieldRect = new Rectangle2D.Double(xx,
						domainAxis.getY1() + 4 + 10 + 5 + 15 - 7 + 21 * i, 7, 7);
//				g2d.setColor(fieldNameAndColorHashMap.get(fieldList[i]));
				g2d.fill(fieldRect);
				g2d.setColor(Color.BLACK);
				g2d.setFont(font2);
				g2d.drawString(fieldList[i], xx + 13, (int) (domainAxis.getY1() + 4
						+ 10 + 5 + 15 + 21 * i));
			}
		
			
			
			
		
			return image;	
		}
		
		@SuppressWarnings("deprecation")
		public JFreeChart setStruLegend(PieDataset pieDataset,String title,HashMap<String, Color> hashMap,ChartDataPara thematicRendition)  {
			TRingPlot  plot = new TRingPlot (pieDataset);

			JFreeChart pieChart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
					plot, false);
			ChartUtilities.applyCurrentTheme(pieChart);
			// 获得图表对象的引用 设置pie chart背景
		//	pieChart.setBackgroundPaint(null);
//			TextTitle title = pieChart.getTitle();
//	      title.setFont(new Font("黑体",Font.PLAIN,15));
			
			TRingPlot plot1 = (TRingPlot) pieChart.getPlot();
	        
	        for (int i = 0; i < thematicRendition.getFieldName().length; i++) {
	    		plot.setSectionPaint(i,new Color(thematicRendition.getFieldColor()[i]));
	        }
	        plot.setSectionDepth(0.5);
			plot1.setBackgroundPaint(null);
			plot1.setOutlineVisible(false);
			plot1.setInteriorGap(0.005);
			// 设置饼图标签的绘制字体 ，label
			plot1.setLabelGap(0);
			plot1.setLabelFont(new Font("黑体",Font.PLAIN,10));
			plot1.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
			plot1.setLabelLinkMargin(0.15);
			plot1.setLabelBackgroundPaint(Color.white);
			plot1.setLabelShadowPaint(null);
	        plot1.setLabelOutlinePaint(null);

			plot1.setToolTipGenerator(null);
			plot1.setCircular(true);

			plot1.setShadowPaint(null);
//			plot1.setDepthFactor(0.5);
			plot1.setSectionOutlinesVisible(false);
		
			return pieChart;
		
		}


		public HashMap<String, Shape> generateHotArea(double x, double y,
				int width, int height, ChartDataPara thematicRendition,
				ChartStyle chartStyle, double[] maxValues, double[] minValues,
				double[] averageValues, IndicatorData[] indicatorDatas) {
			// TODO Auto-generated method stub
			
			double sum = 0;
			  for(int i=0;i<indicatorDatas[0].getValues().length-1;i++){
		        	sum = sum + indicatorDatas[0].getValues()[i]; 
	    	}
			  double R = width/2*(1-0.1*2); 
		      //double R1 = 
		      double R1 = width/2*(1-0.1*2)*0.5;
				double ANGLE = 0;
				double ANGLE1 = 0;
				double barWidth = width/2*(1-0.275*2);
				HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
				//新建多边形,添加多边形的顶点：一指标对应一个扇形，选取该指标所对应扇形的圆心、两个顶点、以及
				//圆弧上面三个等分点来构成多边形的顶点。
				 Rectangle2D.Double rectangleDouble = new Rectangle2D.Double();
					
					//柱子
				double maxValue = maxValues[1];
				double averageValue = averageValues[1];
				
				double[] values = new double[1];// 存放单变量多指标的值，例如存放2012年男女指标的人口数

//				if (indicatorDatas[1] ==null) {
//					System.out.println("绘制专题符号图层时发现专题数据中区域数据不完整！");
//					return;
//				}
				int group = Integer.parseInt(thematicRendition.getDomainAxis()[0]);
				values[0] = indicatorDatas[0].getValues()[group];// 因为普通柱状图只适合单变量多指标这样的数据结构，其余的不适用
				
				if (maxValue > (averageValue * 4)) {
					maxValue = averageValue * 4 * (1 + 1.0/9);
				}else {
					maxValue = maxValue * (1 + 1.0/9);
				}
				int barX0 = (int) x - (int) (barWidth / 2);
					
					if (values[0] > (averageValue * 4)) {
						int barHeight = (int) (0.9 * height);// 断裂前80%
//						int innerHeight = (int) (0.02 * height);// 锯齿高度
//					
//						int barY0 = (int) y;
						//barHeight = barHeight + innerHeight;
						rectangleDouble = new Rectangle2D.Double(barX0, y
								- barHeight, barWidth,barHeight);
						//g.draw(rectangleDouble);
					} else {
						double barHeight = values[0] / maxValue * height;
						rectangleDouble = new Rectangle2D.Double(barX0, y
								- barHeight,
								barWidth, barHeight);
						//g.draw(rectangleDouble);
					}
					//g.draw(rectangleDouble);
					String text = "";
					if (thematicRendition.getDomainAxis()[0] == null
							|| (thematicRendition.getDomainAxis()[0].length() == 0)) {
	    				text = "(" + indicatorDatas[0].getNames()[group]+")="+indicatorDatas[0].getValues()[group];
					}else {
						text = "("+thematicRendition.getDomainAxis()[group] + "," + indicatorDatas[0].getNames()[group]+")="+indicatorDatas[0].getValues()[group];
					}
	    			hotAreahHashMap.put(text, rectangleDouble);
				for(int i=0;i<thematicRendition.getFieldName().length-1;i++){				
					Polygon polygon1 = new Polygon();
					 Polygon polygon2 = new Polygon();
					 
						//饼
					 ANGLE = indicatorDatas[0].getValues()[i]/sum*360;	
		     		if(i==0){
						int[] xPoints = {(int) (x+R1),(int) (x+R),(int) (x+R*Math.cos(ANGLE/4*Math.PI/180)),(int) (x+R*Math.cos(ANGLE/2*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*3/4*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE*3/4*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE/2*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE/4*Math.PI/180))};
						int[] yPoints = {(int) y,(int) y,(int) (y-R*Math.sin(ANGLE/4*Math.PI/180)),(int) (y-R*Math.sin(ANGLE/2*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*3/4*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE*3/4*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE/2*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE/4*Math.PI/180))};
						
						for(int j=0;j<xPoints.length;j++){
							polygon1.addPoint(xPoints[j], yPoints[j]);	
						//	g.setColor(Color.red);
							
				       	}
					//	g.draw(polygon1);
						 Area area1 = new Area(polygon1);
							
							Area area3 = new Area(rectangleDouble);
							area1.subtract(area3);
			//				g.draw(area1);
							
							if (thematicRendition.getDomainAxis()[0] == null
									|| (thematicRendition.getDomainAxis()[0].length() == 0)) {
			    				text = "(" + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
							}else {
								text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
							}
			    			hotAreahHashMap.put(text, area1);
			       	}else{
		     		ANGLE1 = ANGLE1 + indicatorDatas[0].getValues()[i-1]/sum*360;
		    		int[] xPoints = {(int) (x+R1*Math.cos(ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180)),
	 						
	 						(int) (x+R*Math.cos(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R*Math.cos(ANGLE*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE*Math.PI/180+ANGLE1*Math.PI/180)),
	 						
	 						(int) (x+R1*Math.cos(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (x+R1*Math.cos(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180))};
			
		    		int[] yPoints = {(int) (y-R1*Math.sin(ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180)),
	 						
	 						(int) (y-R*Math.sin(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R*Math.sin(ANGLE*Math.PI/180+ANGLE1*Math.PI/180)),
	 						
	 						(int) (y-R1*Math.sin(ANGLE*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE*3/4*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE/2*Math.PI/180+ANGLE1*Math.PI/180)),(int) (y-R1*Math.sin(ANGLE/4*Math.PI/180+ANGLE1*Math.PI/180))};
	 				
		  				
					      
		  			for(int j=0;j<xPoints.length;j++){
		  				polygon2.addPoint(xPoints[j], yPoints[j]);
//		  				g.setColor(Color.blue);
		  			}
		  			 Area area2 = new Area(polygon2);					
						Area area3 = new Area(rectangleDouble);
//						area1.subtract(area3);
//						g.draw(area1);
		  			area2.subtract(area3);
		  		//	g.draw(area2);
		  			
		  			if (thematicRendition.getDomainAxis()[0] == null
							|| (thematicRendition.getDomainAxis()[0].length() == 0)) {
	    				text = "(" + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
					}else {
						text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
					}
	    			hotAreahHashMap.put(text, area2);
			       	}
				}
			return hotAreahHashMap;

		}

	}