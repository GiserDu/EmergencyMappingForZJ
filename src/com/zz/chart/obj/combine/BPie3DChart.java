package com.zz.chart.obj.combine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
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
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.render.pie.TPiePlot3D;
import com.zz.chart.render.pie.TRingPlot3D;


public class BPie3DChart  implements IChart {




	@SuppressWarnings("deprecation")
	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			//获取饼图所需要的数据
			
			DefaultPieDataset pieDataset= new DefaultPieDataset();

//			for (int i = 0; i < indicatorDatas[0].getNames().length; i++) {
				pieDataset.setValue(thematicRendition.getFieldName()[0], indicatorDatas[0].getValues()[0]);
//			}
			
			//创建饼图
			TPiePlot3D  plot1 = new TPiePlot3D (pieDataset);
			
			JFreeChart pieChart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
					plot1, false);
			ChartUtilities.applyCurrentTheme(pieChart);
			pieChart.setBackgroundPaint(null);
			
			// 获得图表对象的引用  
			TPiePlot3D plot = (TPiePlot3D ) pieChart.getPlot();
			

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
			//设置饼图颜色

			for(int i=0;i<thematicRendition.getFieldName().length;i++){
				plot.setSectionPaint(i, new Color(thematicRendition.getFieldColor()[i]));
			}
			 //设置饼图大小
//	        double sum = 0;
//	        for(int i=0;i<indicatorDatas[0].getValues().length;i++){
//	        	sum = sum + indicatorDatas[0].getValues()[i];
//	        }
	        double pieRadius = indicatorDatas[0].getValues()[0]/maxValues[0];
	        pieRadius = Math.sqrt(pieRadius);
	        double interiorGap = 0.4 - pieRadius / 3;
	        plot.setInteriorGap(interiorGap);
	        		
	        	
			//设置饼图样式
			plot.setBackgroundAlpha(BackgroundAlphas);
			plot.setForegroundAlpha(ForegroundAlphas);
//			plot.setSectionOutlinesVisible(SectionOutlinesVisible);
			plot.setOutlineVisible(OutlinesVisible);
			if(Outlines){
			BasicStroke basicStroke = new BasicStroke(
					outLineBasicStrokes);
			plot.setBaseSectionOutlineStroke(basicStroke);
			plot.setBaseSectionOutlinePaint(new Color(outLinePaints));
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
	        plot.setLabelGenerator(null);
	        plot.setDepthFactor(0.175);
	        //设置饼图标签
	        if(isLables){
			FontMetrics fm = g.getFontMetrics();  
	        Rectangle2D rec=fm.getStringBounds(Float.toString((float) indicatorDatas[0].getValues()[0]), g); 		
//			String value = Double.toString(indicatorDatas[0].getValues()[i]);
	        double  stringlength = rec.getWidth();//字符串的长度
	        double stringheight = rec.getHeight();	
	        g.drawString(Float.toString((float) indicatorDatas[0].getValues()[0]), (int)(x-stringlength/2), (int) (y+width/2*(1-2*interiorGap)+stringheight/2));
	        }
			//绘制饼图
	        ChartRenderingInfo info = new 
	        		ChartRenderingInfo(new StandardEntityCollection());
		
			Rectangle2D.Double symbolEnvlope =  new 
					Rectangle2D.Double(x-width/2, y-width/2/1.5, width, width/1.5); 
			//g.draw(symbolEnvlope);
			pieChart.draw(g, symbolEnvlope, info);
		
		
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			double maxValue = maxValues[1];
			double averageValue = averageValues[1];
			double[] values;// 存放单变量多指标的值，例如存放2012年男女指标的人口数
			if (indicatorDatas[1] ==null) {
				System.out.println("绘制专题符号图层时发现专题数据中区域数据不完整！");
				return;
			}
			values = indicatorDatas[1].getValues();

			if (maxValue > (averageValue * 4)) {
				maxValue = averageValue * 4 * (1 + 1.0/9);
			}else {
				maxValue = maxValue * (1 + 1.0/9);
			}


//			int[] color;
//
//			int[] crgb = thematicRendition.getFieldColor();
//			if (crgb.length != values.length) {
//				color = new int[values.length];
//				for (int i = 0; i < color.length; i++) {
//					color[i] = (((int) (255 * Math.random()) * 256) + (int) (255 * Math
//							.random()))
//							* 256 + (int) (255 * Math.random());
//				}
//			} else {
//				color = crgb;
//			}
			
			// 处理极端数据

//			double realItemMargin = itemMargin * (values.length - 1) * 1.0 / width;// 根据设置的柱子间的间距，求出所有间距之和占据绘图区域宽度的百分比
//			double barWidth = (width * (1 - realItemMargin) / values.length);
			double barWidth = width/2*(1-0.25*2);
			if (barWidth > width * 0.51) {// 设置柱子最大宽度为绘图区域的51%
				barWidth = width * 0.51;
			}

			double bigvalue = 5 * maxValue / 24;// 规定最多有4个大的矩形，4个小的矩形
			double smallvalue = bigvalue / 5;
			double bigbarHeight = 0.0;
			bigbarHeight = bigvalue / (maxValue * (1 + 1 / 24)) * height;// 设定max值刚好达到最大长度，每个小矩形间的空隙为smallvalue矩形的1/3,而小矩形加空隙为bigvalue矩形的1/5，假设空隙宽度为1/4height，则整个为30height，所以其中1/29是根据5/24/（1+x）=
			// 5/25
			double smallbarHeight = 0.0;
			smallbarHeight = bigbarHeight / 5 * 2 / 4;
			double spaceHeight = 0.0;
			spaceHeight = bigbarHeight / 10;
			double radius = barWidth / 4;

			BasicStroke basicStroke = new BasicStroke(outLineBasicStrokes);
			g.setStroke(basicStroke);

			for (int i = 0; i < values.length; i++) {
				if (values[i] > (averageValue * 4)) {
					int bigint = 4;
//					int barX0 = 0;
//					barX0 = (int)(x - barWidth * values.length * 1.0 / 2-itemMargin*(values.length-1)/2 + i
//							* (barWidth ));
					int barX0 = (int) x - (int) (barWidth / 2);
//					GradientPaint tGradientPaint = new GradientPaint(
//							(float) (barX0), (float) y, new Color(color[i], true),
//							(float) (barX0 + 0.5 * barWidth), (float) y, new Color(
//									255, 255, 255), true);
//					GradientPaint tGradientPaintbrighter = new GradientPaint(
//							(float) (barX0), (float) y, new Color(color[i], true).brighter(),
//							(float) (barX0 + 0.5 * barWidth), (float) y, new Color(
//									255, 255, 255), true);

					for (int j = 0; j < bigint; j++) {

						double rx = barX0;
						double ry = y - bigbarHeight * (1 + j) - spaceHeight * j;
						double rw = barWidth;
						double rh = bigbarHeight;
						Ellipse2D ellipse2DDOWN = new Ellipse2D.Double(rx, ry - 0.5
								* radius + bigbarHeight, rw, radius);

						Rectangle2D.Double tRectangle2D = new Rectangle2D.Double(
								rx, ry, rw, rh);
						Ellipse2D ellipse2DUP = new Ellipse2D.Double(rx, ry - 0.5
								* radius, rw, radius);

//						g.setPaint(tGradientPaint);
						g.setColor(new Color(thematicRendition.getFieldColor()[1],false));
						g.fill(tRectangle2D);
						g.fill(ellipse2DDOWN);
//						g.setPaint(tGradientPaintbrighter);
						g.setColor(new Color(thematicRendition.getFieldColor()[1],false).darker());
						g.fill(ellipse2DUP);

						if (Outlines) {
							g.setColor(new Color(outLinePaints, false));
							Area areaEllipse2DDOWN = new Area(ellipse2DDOWN);
							Area areaRectangle2D = new Area(tRectangle2D);
							Area areaEllipse2DUP = new Area(ellipse2DUP);
							areaEllipse2DDOWN.add(areaRectangle2D);
							areaEllipse2DDOWN.add(areaEllipse2DUP);

							g.draw(areaEllipse2DDOWN);
							g.draw(ellipse2DUP);
						}
					}
					int innerHeight = (int) (0.02 * height);// 锯齿高度
					int barHeight = (int) ((bigbarHeight - innerHeight) / 2);// 共90个像素，断裂前80
					int innerWidth = (int) (barWidth / 3);// 锯齿宽度
					int barY0 = (int) (y - (bigbarHeight + spaceHeight) * 4);

					int[] xpointsDown = { barX0, barX0, barX0 + innerWidth,
							barX0 + 2 * innerWidth, barX0 + 3 * innerWidth,
							barX0 + 3 * innerWidth };
					int[] ypointsDown = { barY0, barY0 - barHeight,
							barY0 - barHeight + innerHeight, barY0 - barHeight,
							barY0 - barHeight + innerHeight, barY0 };

					GeneralPath polygonDown = new GeneralPath();
					GeneralPath polygonUp = new GeneralPath();

					polygonDown.moveTo((float) xpointsDown[1],
							(float) ypointsDown[1]);
					polygonDown.lineTo((float) xpointsDown[0],
							(float) ypointsDown[0]);
					Arc2D arcDown = new Arc2D.Double(barX0, barY0 - radius * 1.0
							/ 2, barWidth, radius, 180, 180, Arc2D.OPEN);
					polygonDown.append(arcDown, true);
					polygonDown.lineTo((float) xpointsDown[4],
							(float) ypointsDown[4]);
					polygonDown.lineTo((float) xpointsDown[3],
							(float) ypointsDown[3]);
					polygonDown.lineTo((float) xpointsDown[2],
							(float) ypointsDown[2]);
					polygonDown.closePath();

					barHeight = barHeight + 2;
					int[] xpointsUp = { barX0, barX0, barX0 + innerWidth,
							barX0 + 2 * innerWidth, barX0 + 3 * innerWidth,
							barX0 + 3 * innerWidth };
					int[] ypointsUp = { barY0 - (int) bigbarHeight,
							barY0 - barHeight, barY0 - barHeight + innerHeight,
							barY0 - barHeight, barY0 - barHeight + innerHeight,
							barY0 - (int) bigbarHeight };

					polygonUp.moveTo((float) xpointsUp[1], (float) ypointsUp[1]);
					polygonUp.lineTo((float) xpointsUp[0], (float) ypointsUp[0]);
					Arc2D arcUp = new Arc2D.Double(barX0, barY0
							- (int) bigbarHeight - radius * 1.0 / 2, barWidth,
							radius, 180, -180, Arc2D.OPEN);
					polygonUp.append(arcUp, true);
					polygonUp.lineTo((float) xpointsUp[4], (float) ypointsUp[4]);
					polygonUp.lineTo((float) xpointsUp[3], (float) ypointsUp[3]);
					polygonUp.lineTo((float) xpointsUp[2], (float) ypointsUp[2]);
					polygonUp.closePath();

					Shape top = new Ellipse2D.Double(barX0, barY0
							- (int) bigbarHeight - radius * 1.0 / 2, barWidth,
							radius);
					g.setColor(new Color(thematicRendition.getFieldColor()[1],false));
					g.fill(polygonUp);
					g.fill(polygonDown);
					g.setColor(new Color(thematicRendition.getFieldColor()[1], false).darker());
					g.fill(top);

					// 断裂处理的必须标上标签
					Font font = new Font(itemLabelFontNames, Font.PLAIN,
							itemLabelFontSizes);
					g.setColor(new Color(itemLabelPaints, false));
					g.setFont(font);
					g.drawString(Float.toString((float) values[i]), (int) barX0,
							(int) barY0 - (int) (bigbarHeight + spaceHeight));

					if (Outlines) {

						g.setColor(new Color(outLinePaints, false));
						g.setStroke(basicStroke);
						g.draw(polygonUp);
						g.draw(polygonDown);
						g.draw(top);
					}

				} else {
					int bigint = (int) (values[i] / bigvalue);
					int smallint = (int) ((values[i] % bigvalue) / smallvalue);
					int N = 0;
					int M = 0;
//					double barX0 = x - barWidth * values.length * 1.0 / 2-itemMargin*(values.length-1)/2 + i
//					* (barWidth + itemMargin);
					int barX0 = (int) x - (int) (barWidth / 2);
//					GradientPaint tGradientPaint = new GradientPaint((float) (barX0), (float) y, new Color(
//							color[i]), (float) (barX0 + 0.5 * barWidth),
//							(float) y, new Color(255, 255, 255), true);
//					GradientPaint tGradientPaintbrighter = new GradientPaint(
//							(float) (barX0), (float) y,
//							new Color(color[i]).brighter(), (float) (barX0 + 0.5 * barWidth),
//							(float) y, new Color(255, 255, 255), true);

					for (int j = 0; j < bigint; j++) {

						double rx = barX0;
						double ry = y - bigbarHeight * (1 + j) - spaceHeight * j;
						double rw = barWidth;
						double rh = bigbarHeight;
						Ellipse2D ellipse2DDOWN = new Ellipse2D.Double(rx, ry - 0.5
								* radius + bigbarHeight, rw, radius);

						Rectangle2D.Double tRectangle2D = new Rectangle2D.Double(
								rx, ry, rw, rh);
						Ellipse2D ellipse2DUP = new Ellipse2D.Double(rx, ry - 0.5
								* radius, rw, radius);

						g.setColor(new Color(thematicRendition.getFieldColor()[1],false));
						g.fill(tRectangle2D);
						g.fill(ellipse2DDOWN);
						g.setColor(new Color(thematicRendition.getFieldColor()[1],false).darker());
						g.fill(ellipse2DUP);

						if (Outlines) {
							g.setColor(new Color(outLinePaints, false));
							Area areaEllipse2DDOWN = new Area(ellipse2DDOWN);
							Area areaRectangle2D = new Area(tRectangle2D);
							Area areaEllipse2DUP = new Area(ellipse2DUP);
							areaEllipse2DDOWN.add(areaRectangle2D);
							areaEllipse2DDOWN.add(areaEllipse2DUP);

							g.draw(areaEllipse2DDOWN);
							g.draw(ellipse2DUP);
						}

						N = j + 1;
					}
					for (int j = 0; j < smallint; j++) {
						if (N == 0) {
							double rx = barX0;
							double ry = y - smallbarHeight * (1 + j) - spaceHeight
									* j;
							double rw = barWidth;
							double rh = smallbarHeight;
							Ellipse2D ellipse2DDOWN = new Ellipse2D.Double(rx, ry
									- 0.5 * radius + smallbarHeight, rw, radius);
							Rectangle2D.Double tRectangle2D = new Rectangle2D.Double(
									rx, ry, rw, rh);
							Ellipse2D ellipse2DUP = new Ellipse2D.Double(rx, ry
									- 0.5 * radius, rw, radius);

							g.setColor(new Color(thematicRendition.getFieldColor()[1],false));
							g.fill(tRectangle2D);
							g.fill(ellipse2DDOWN);
							g.setColor(new Color(thematicRendition.getFieldColor()[1],false).darker());
							g.fill(ellipse2DUP);

							if (Outlines) {
								g.setColor(new Color(outLinePaints, false));
								Area areaEllipse2DDOWN = new Area(ellipse2DDOWN);
								Area areaRectangle2D = new Area(tRectangle2D);
								Area areaEllipse2DUP = new Area(ellipse2DUP);
								areaEllipse2DDOWN.add(areaRectangle2D);
								areaEllipse2DDOWN.add(areaEllipse2DUP);

								g.draw(areaEllipse2DDOWN);
								g.draw(ellipse2DUP);
							}
							M++;
						} else {
							double rx = barX0;
							double ry = y - bigbarHeight * N - spaceHeight * N
									- smallbarHeight * (1 + j) - spaceHeight * j;
							double rw = barWidth;
							double rh = smallbarHeight;
							Ellipse2D ellipse2DDOWN = new Ellipse2D.Double(rx, ry
									- 0.5 * radius + smallbarHeight, rw, radius);
							Rectangle2D.Double tRectangle2D = new Rectangle2D.Double(
									rx, ry, rw, rh);
							Ellipse2D ellipse2DUP = new Ellipse2D.Double(rx, ry
									- 0.5 * radius, rw, radius);

//							g.setPaint(tGradientPaint);
							g.setColor(new Color(thematicRendition.getFieldColor()[1],false));
							g.fill(tRectangle2D);
							g.fill(ellipse2DDOWN);
							g.setColor(new Color(thematicRendition.getFieldColor()[1],false).darker());
							g.fill(ellipse2DUP);

							if (Outlines) {
								g.setColor(new Color(outLinePaints, false));
								Area areaEllipse2DDOWN = new Area(ellipse2DDOWN);
								Area areaRectangle2D = new Area(tRectangle2D);
								Area areaEllipse2DUP = new Area(ellipse2DUP);
								areaEllipse2DDOWN.add(areaRectangle2D);
								areaEllipse2DDOWN.add(areaEllipse2DUP);

								g.draw(areaEllipse2DDOWN);
								g.draw(ellipse2DUP);
							}
							M++;
						}

					}
					if (isLables && (M != 0 || N != 0)) {
						g.setFont(new Font(itemLabelFontNames, Font.PLAIN,
								itemLabelFontSizes));
						g.setColor(new Color(itemLabelPaints, false));
						g.drawString(Float.toString((float) values[i]), (int) (barX0),
								(int) (y - bigbarHeight * N - spaceHeight * N
										- smallbarHeight * (1 + M) - spaceHeight
										* M));
					}

				}
			}
			g.setStroke(new BasicStroke(1f));
			g.setColor(Color.black);
			//实验热区
//	        //设置饼图大小
	        double sum = 0;
	        for(int i=0;i<indicatorDatas[0].getValues().length;i++){
	        	sum = sum + indicatorDatas[0].getValues()[i];
	        }
			 //shiyanrequ
//			  double R = width/2*(1-0.15*2); 
//		      //double R1 = 
//		      double R1 = width/2*(1-0.15*2)*0.5;
//		      System.out.println(width);
				double ANGLE = 0;
				double ANGLE1 = 0;
//				Rectangle2D.Double symbolEnvlope1 =  new 
//				Rectangle2D.Double(x-width*0.7/2, y-width*2*0.7/2.75/2, width*0.7, width*2*0.7/2.75); 
//				Rectangle2D.Double symbolEnvlope2 =  new 
//				Rectangle2D.Double(x-width/4, y-width/2/2.75, width/2, width/2.75); 
				Rectangle2D.Double symbolEnvlope1 =  new 
				Rectangle2D.Double(x-width/2*0.7, y-width/2/1.5*0.7, width*0.7, width/1.5*0.7); 
				Rectangle2D.Double symbolEnvlope2 =  new 
				Rectangle2D.Double(x-width/4, y-width/4/1.5, width/2, width/2/1.5); 
//				HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
				//新建多边形,添加多边形的顶点：一指标对应一个扇形，选取该指标所对应扇形的圆心、两个顶点、以及
				//圆弧上面三个等分点来构成多边形的顶点。
				for(int i=0;i<thematicRendition.getFieldName().length;i++){				
					Arc2D.Double arc1 = new Arc2D.Double();
					Arc2D.Double arc2 = new Arc2D.Double();
					 Rectangle2D.Double rectangleDouble = new Rectangle2D.Double();
					
						//柱子
//						double[] values1;// 存放单变量多指标的值，例如存放2012年男女指标的人口数
//
//						values1 = indicatorDatas[0].getValues();// 因为普通柱状图只适合单变量多指标这样的数据结构，其余的不适用
////						if (null == values) {
////							System.out.println("区域数据为空！");
////							return null;
////						}
//						
//						if (maxValue > (averageValue * 4)) {
//							maxValue = averageValue * 4 * (1 + 1.0/9);
//						}else {
//							maxValue = maxValue * (1 + 1.0/9);
//						}
						int barX0 = (int) x - (int) (barWidth / 2);
							
							if (values[0] > (averageValue * 4)) {
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
							//	g.draw(rectangleDouble);
							}
						//	g.draw(rectangleDouble);
						//饼
					 ANGLE = indicatorDatas[0].getValues()[i]/sum*360;	
//		     		if(i==0){
						
					 arc1 = new Arc2D.Double(symbolEnvlope1, ANGLE1, ANGLE, 2);
					 arc2 = new Arc2D.Double(symbolEnvlope2, ANGLE1, ANGLE, 2);
					//	g.draw(arc2);
					 	
					 	Area areaArc1 = new Area(arc1);
						
						Area areaArc2 = new Area(arc2);
						areaArc1.subtract(areaArc2);
					//	g.draw(areaArc1);
						//g.fill(areaArc1);
					ANGLE1 += ANGLE;
//			       	}else{
//		     		ANGLE1 = ANGLE1 + indicatorDatas[0].getValues()[i-1]/sum*360;
//		    		 arc1 = new Arc2D.Double(symbolEnvlope1, ANGLE1, ANGLE, Arc2D.PIE);
//					 arc2 = new Arc2D.Double(symbolEnvlope2, ANGLE1, ANGLE, Arc2D.PIE);
//						
//			       	}
						Area area1 = new Area(areaArc1);
						Area area2 = new Area(rectangleDouble);
						area1.subtract(area2);
						//g.draw(area1);
						
				}
				
		}
		

	public BufferedImage drawLegend(int width, int height,
			ChartDataPara thematicRendition, ChartStyle chartStyle,
			double[] maxValues, double[] minValues, double[] averageValues,
			IndicatorData[] indicatorDatas) {
		double maxValue = maxValues[0];
		double averageValue = averageValues[0];
		if (maxValue > (averageValue * 4)) {
			maxValue = averageValue * 4 * (1 + 1.0/9);
		}else {
			maxValue = maxValue * (1 + 1.0/9);
		}

		double bigvalue = 5 * maxValue / 24;// 规定最多有4个大的矩形，4个小的矩形
		double smallvalue = bigvalue / 5;
		String[] yearList = new String[indicatorDatas.length];
		for (int i = 0; i < yearList.length; i++) {
			yearList[i] = indicatorDatas[i].getDomainAxis();
		}
//		String domainAxisUnit = thematicRendition.getDomainAxisUnit();
		String[] fieldUnits = thematicRendition.getFieldUnits();

		String[] seriesList = thematicRendition.getFieldName();

		String[] domainAxisList = new String[indicatorDatas.length];
		for (int i = 0; i < domainAxisList.length; i++) {
			domainAxisList[i] = indicatorDatas[i].getDomainAxis();
		}
		int[] color;

		int[] crgb = thematicRendition.getFieldColor();
		if (crgb.length != seriesList.length) {
			color = new int[seriesList.length];
			for (int i = 0; i < color.length; i++) {
				color[i] = (((int) (255 * Math.random()) * 256) + (int) (255 * Math
						.random()))
						* 256 + (int) (255 * Math.random());
			}
		} else {
			color = crgb;
		}

		double barWidth = width * (1.0 - 0.1 * seriesList.length)
				/ seriesList.length;
		double maxbarWidth = 0.3 * width;
		if (barWidth > maxbarWidth) {
			barWidth = maxbarWidth;
		}
		double bigbarHeight = 0.0;
		bigbarHeight = bigvalue / (maxValue * (1 + 1 / 24)) * height;// 设定max值刚好达到最大长度，每个小矩形间的空隙为smallvalue矩形的1/3,而小矩形加空隙为bigvalue矩形的1/5，假设空隙宽度为1/4height，则整个为30height，所以其中1/29是根据5/24/（1+x）=
		// 5/25
		double smallbarHeight = 0.0;
		smallbarHeight = bigbarHeight / 5 * 2 / 4;
		double radius = barWidth / 4;

		int symbolWidth = 337;
		int symbolHeight = (int) (66 + bigbarHeight + 33 + 21 * seriesList.length);

		BufferedImage image = new BufferedImage(symbolWidth, symbolHeight+width+50,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
//		g.setBackground(Color.WHITE);
//		g.clearRect(0, 0, symbolWidth, symbolHeight);
		g.setColor(Color.BLACK);
		g.draw(new Rectangle(1, 1, symbolWidth - 2, symbolHeight+width+50 - 2));

		g.setFont(new Font("黑体", Font.PLAIN, 25));
		g.drawString("图例", 142, 30);
		g.setFont(new Font("黑体", Font.PLAIN, 20));
		if ((fieldUnits[0]!=null)&&(!(fieldUnits[0].length()==0))) {
			g.drawString("单位：（" + fieldUnits[0] + "）", 92, 55);
		}
		g.setFont(new Font("黑体", Font.PLAIN, 15));

		double rx = 72;
		double ry = 66 + 0.5 * radius;
		double rw = barWidth;
		//double rh = bigbarHeight;
		
		GeneralPath polygonBig = new GeneralPath();
		GeneralPath polygonSmall = new GeneralPath();
		polygonBig.moveTo(rx, ry);
		polygonBig.lineTo(rx, ry + bigbarHeight);
		Arc2D arcDown = new Arc2D.Double(rx, ry + bigbarHeight - radius * 0.5
				, barWidth, radius, 180, 180, Arc2D.OPEN);
		polygonBig.append(arcDown, true);
		polygonBig.lineTo(rx + barWidth, ry);
		Arc2D arcUp = new Arc2D.Double(rx, ry - radius * 0.5
				, barWidth, radius, 0, 180, Arc2D.OPEN);
		polygonBig.append(arcUp, true);
		polygonBig.closePath();
//		Ellipse2D bigellipse2DDOWN = new Ellipse2D.Double(rx, ry - 0.5 * radius
//				+ bigbarHeight, rw, radius);
//
//		Rectangle2D.Double bigtRectangle2D = new Rectangle2D.Double(rx, ry, rw,
//				rh);
		Ellipse2D bigEllipse2DUP = new Ellipse2D.Double(rx, ry - 0.5 * radius,
				rw, radius);

		rx = 172;
		ry = 66 + bigbarHeight - smallbarHeight + 0.5 * radius;
		rw = barWidth;
		//rh = smallbarHeight;
		polygonSmall.moveTo(rx, ry);
		polygonSmall.lineTo(rx, ry + smallbarHeight);
		arcDown = new Arc2D.Double(rx, ry + smallbarHeight - radius * 0.5
				, barWidth, radius, 180, 180, Arc2D.OPEN);
		polygonSmall.append(arcDown, true);
		polygonSmall.lineTo(rx + barWidth, ry);
		arcUp = new Arc2D.Double(rx, ry - radius * 0.5
				, barWidth, radius, 0, 180, Arc2D.OPEN);
		polygonSmall.append(arcUp, true);
		polygonSmall.closePath();
//		Ellipse2D smallellipse2DDOWN = new Ellipse2D.Double(rx, ry - 0.5
//				* radius + smallbarHeight, rw, radius);
//
//		Rectangle2D.Double smalltRectangle2D = new Rectangle2D.Double(rx, ry,
//				rw, rh);
		Ellipse2D smallEllipse2DUP = new Ellipse2D.Double(rx,
				ry - 0.5 * radius, rw, radius);

		g.setColor(Color.BLACK);
		g.draw(polygonBig);
		g.draw(bigEllipse2DUP);
		g.draw(smallEllipse2DUP);
		g.draw(polygonSmall);
//		g.fill(smalltRectangle2D);
//		g.fill(smallellipse2DDOWN);
//		g.fill(bigellipse2DDOWN);
//		g.fill(bigtRectangle2D);
//		g.setColor(Color.GRAY);
//		g.fill(bigellipse2DUP);
//		g.fill(smallellipse2DUP);

		g.setColor(Color.BLACK);
		g.drawString(Integer.toString((int) bigvalue), 72,
				66 + (int) bigbarHeight + 5 + 15);
		g.drawString(Integer.toString((int) smallvalue), 172,
				66 + (int) bigbarHeight + 5 + 15);

//		if (domainAxisUnit != null) {
//			g.setColor(Color.BLACK);
//			g.drawString(domainAxisList[0] + domainAxisUnit, 72, 66
//					+ (int) bigbarHeight + 5 + 15 + 5 + 21
//					* (seriesList.length / 2 + 1));
//		}


		for (int i = 0; i < seriesList.length; i++) {
		
				double xx = 92;
			
			Rectangle2D.Double fieldRect = new Rectangle2D.Double(xx, 66
					+ (int) bigbarHeight + 5 + 15 + 5 + 21 * (i + 1) - 10, 7, 7);
			g.setColor(new Color(color[i],false));
			g.fill(fieldRect);
			g.setColor(Color.BLACK);
			g.drawString(seriesList[i], (int) (xx+13), 66 + (int) bigbarHeight + 5 + 15
					+ 5 + 21 * (i + 1));
		}
		Rectangle2D.Double rect = new Rectangle2D.Double(1, symbolHeight, symbolWidth, width+50);
		g.setColor(Color.BLACK);
		//g2d.draw(rect);
//		FontMetrics fm = g2d.getFontMetrics();  
//        Rectangle2D rec=fm.getStringBounds(Double.toString(width*thematicRendition.getScales()[0]), g2d); 
	
//		String value = Double.toString(indicatorDatas[0].getValues()[i]);
//        double  stringlength = rec.getWidth();//字符串的长度
//        double stringheight = rec.getHeight();	
		for(int i=0;i<3;i++){
		double interiorGap = 0.4 - 1.0 / 3;
		double X = rect.getCenterX();
		double Y = rect.getCenterY();
		double R = width/2*(1-2*interiorGap);
//		double R1 = width/2;
		Ellipse2D.Double oval = new Ellipse2D.Double(X-R+R/4*i,Y-R+R/2*i,2*(R-R/4*i),2*(R-R/4*i));
		Line2D.Double line = new Line2D.Double(X-1.3*R, Y-R+R*i/2, X,  Y-R+R*i/2);
		double sum1 = maxValues[0]*(1-i*1.0/4);
		NumberFormat ddf1=NumberFormat.getNumberInstance();
	    if (sum1<10) {
	    	ddf1.setMaximumFractionDigits(2);
		}else {
			ddf1.setMaximumFractionDigits(0);
		}
	    ddf1.setGroupingUsed(false);
	    String temp = ddf1.format(sum1);
		g.draw(line);
		g.draw(oval);
		g.setFont(new Font("宋体",Font.PLAIN,10));
		g.drawString(temp,  (int) (X-1.5*R),(int) (Y-R+R*i/2));
		
		}
		g.dispose();

		return image;
	}

		
		@SuppressWarnings("deprecation")
		public JFreeChart setStruLegend(PieDataset pieDataset,String title,HashMap<String, Color> hashMap,ChartDataPara thematicRendition)  {
			TRingPlot3D  plot = new TRingPlot3D (pieDataset);

			JFreeChart pieChart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT,
					plot, false);
			ChartUtilities.applyCurrentTheme(pieChart);
			// 获得图表对象的引用 设置pie chart背景
			pieChart.setBackgroundPaint(null);
//			TextTitle title = pieChart.getTitle();
//	      title.setFont(new Font("黑体",Font.PLAIN,15));
			
			TRingPlot3D plot1 = (TRingPlot3D) pieChart.getPlot();
	        
	        for (int i = 0; i < thematicRendition.getFieldName().length-1; i++) {
	    		plot.setSectionPaint(i,new Color(thematicRendition.getFieldColor()[i]));
	        }
	        plot.setSectionDepth(0.5);
			plot1.setBackgroundPaint(null);
			plot1.setOutlineVisible(false);
			plot1.setInteriorGap(0.001);
			// 设置饼图标签的绘制字体 ，label
			plot1.setLabelGap(0);
			plot1.setLabelFont(new Font("黑体",Font.PLAIN,15));
			plot1.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}"));
			plot1.setLabelLinkMargin(0.2);
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
		     double pieRadius = indicatorDatas[0].getValues()[0]/maxValues[0];
		        pieRadius = Math.sqrt(pieRadius);
		        double interiorGap = 0.4 - pieRadius / 3;
		        
		    	double barWidth = width/2*(1-0.25*2);
//				  double R = width/2*(1-0.15*2); 
//			      //double R1 = 
//			      double R1 = width/2*(1-0.15*2)*0.5;
//			      System.out.println(width);
//					double ANGLE = 0;
//					double ANGLE1 = 0;
//					Rectangle2D.Double symbolEnvlope1 =  new 
//					Rectangle2D.Double(x-width*0.7/2, y-width*2*0.7/2.75/2, width*0.7, width*2*0.7/2.75); 
//					Rectangle2D.Double symbolEnvlope2 =  new 
//					Rectangle2D.Double(x-width/4, y-width/2/2.75, width/2, width/2.75); 
					Rectangle2D.Double symbolEnvlope1 =  new 
//					Rectangle2D.Double(x-width/2*0.7, y-width/2/1.5*0.7, width*0.7, width/1.5*0.7); 
					Rectangle2D.Double( x-width/2*(1-interiorGap*2), y-width*0.75/2*(1-interiorGap*2)*(1-0.175/4), width*(1-interiorGap*2), width*0.75*(1-interiorGap*2)*(1-0.175)); 
					//					Rectangle2D.Double symbolEnvlope2 =  new 
//					Rectangle2D.Double(x-width/4, y-width/4/1.5, width/2, width/2/1.5); 
					HashMap<String, Shape> hotAreahHashMap = new HashMap<String, Shape>();
					//新建多边形,添加多边形的顶点：一指标对应一个扇形，选取该指标所对应扇形的圆心、两个顶点、以及
					//圆弧上面三个等分点来构成多边形的顶点。
					 Rectangle2D.Double rectangleDouble = new Rectangle2D.Double();
						
						//柱子
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
					int barX0 = (int) x - (int) (barWidth / 2);
						
						if (values[0] > (averageValue * 4)) {
							int barHeight = (int) (0.9 * height);// 断裂前80%
//							int innerHeight = (int) (0.02 * height);// 锯齿高度
//						
//							int barY0 = (int) y;
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
//					for(int i=0;i<thematicRendition.getFieldName().length-1;i++){				
						Arc2D.Double arc1 = new Arc2D.Double();
//						Arc2D.Double arc2 = new Arc2D.Double();
						
							
							//饼
										
						 arc1 = new Arc2D.Double(symbolEnvlope1, 0, 360, 2);
//						 arc2 = new Arc2D.Double(symbolEnvlope2, ANGLE1, ANGLE, 2);
						 	Area areaArc1 = new Area(arc1);							
//							Area areaArc2 = new Area(arc2);
//							areaArc1.subtract(areaArc2);
						

							Area area1 = new Area(areaArc1);
							Area area2 = new Area(rectangleDouble);
							area1.subtract(area2);
							//g.draw(area1);
							
							if (thematicRendition.getDomainAxis()[0] == null
									|| (thematicRendition.getDomainAxis()[0].length() == 0)) {
			    				text = "(" + indicatorDatas[0].getNames()[0]+")="+indicatorDatas[0].getValues()[0];
							}else {
								text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[0]+")="+indicatorDatas[0].getValues()[0];
							}
			    			hotAreahHashMap.put(text, area1);
//			}
			return hotAreahHashMap;

		}

	}