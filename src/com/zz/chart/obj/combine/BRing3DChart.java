package com.zz.chart.obj.combine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
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
import com.zz.chart.render.pie.TRingPlot3D;

public class BRing3DChart  implements IChart {




	@SuppressWarnings("deprecation")
	public void drawChart(Graphics2D g, double x, double y, int width,
			int height, ChartDataPara thematicRendition,
			ChartStyle chartStyle, double[] maxValues, double[] minValues,
			double[] averageValues, IndicatorData[] indicatorDatas) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			//获取饼图所需要的数据
			
			DefaultPieDataset pieDataset= new DefaultPieDataset();

			for (int i = 0; i < indicatorDatas[0].getNames().length; i++) {
				pieDataset.setValue(thematicRendition.getFieldName()[i], indicatorDatas[0].getValues()[i]);
			}
			
			//创建饼图
			TRingPlot3D  plot1 = new TRingPlot3D (pieDataset);
			
			JFreeChart pieChart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT,
					plot1, false);
			ChartUtilities.applyCurrentTheme(pieChart);
			pieChart.setBackgroundPaint(null);
			
			// 获得图表对象的引用  
			TRingPlot3D plot = (TRingPlot3D) pieChart.getPlot();
			

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
	        plot.setInteriorGap(0.15);
			
			
//	        //设置饼图标签
//	        if(isLabel){
//	      	plot.setLabelGap(LabelGap);
//	      	plot.setSimpleLabels(true);
//	      	plot.setLabelBackgroundPaint(null);
//	      	plot.setLabelLinksVisible(false);
//	      	plot.setLabelOutlinePaint(null);
//	      	Font font = new Font(labelFontName, Font.PLAIN,LabelFontSize);
//	      	plot.setLabelFont(font);
//	      	plot.setLabelShadowPaint(null);
//	      	plot.setLabelLinkMargin(0);
//	      	plot.setLabelGenerator(null);
//		        }else {
//					
//				}
			 plot.setLabelGenerator(null);
	      	 plot.setSectionDepth(0.5);
		 	 plot.setOuterSeparatorExtension(0);// 不显示ring的分割线
		 	 plot.setInnerSeparatorExtension(0);
	         plot.setDepthFactor(0.175);
			//绘制饼图
	        ChartRenderingInfo info = new 
	        		ChartRenderingInfo(new StandardEntityCollection());
		
			Rectangle2D.Double symbolEnvlope =  new 
					Rectangle2D.Double(x-width/2, y-width/2/1.5, width, width/1.5); 
			//g.draw(symbolEnvlope);
			pieChart.draw(g, symbolEnvlope, info);
		
		
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
//			DefaultCategoryDataset barDataset = new DefaultCategoryDataset();// 存放绘制柱状图所用统计数据的数据集
			double maxValue = maxValues[0];
			double averageValue = averageValues[0];
			
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
//
//			// 获取符号绘制样式
//			@SuppressWarnings("unused")
//			String chartID = chartStyle.getChartID();// 符号ID
//			boolean drawBarOutline = chartStyle.isDrawBarOutLine();// 是否绘制柱子边线
//			int outLinePaint = chartStyle.getOutLinePaint();// 柱子边线颜色
//			float outLineBasicStroke = chartStyle.getOutLineBasicStroke();// 柱子边线粗细
//			double minimumBarLength = chartStyle.getMinimumBarLength();// 百分比
//			double itemMargin = chartStyle.getItemMargin();// 柱子间的距离，像素为单位
//			boolean isLable = chartStyle.isLable();// 是否绘制标签
//			String itemLabelFontName = chartStyle.getItemLabelFontName();// 标签字体名称
//			int itemLabelFontSize = chartStyle.getItemLabelFontSize();// 标签字体大小
//			int itemLabelPaint = chartStyle.getItemLabelPaint();// 标签颜色
//			int transparent = chartStyle.getTransparent();// 填充色透明度

//			HashMap<String, Color> fieldNameAndColorHashMap = new HashMap<String, Color>();
//			for (int i = 0; i < thematicRendition.getFieldColor().length; i++) {
//				fieldNameAndColorHashMap.put(thematicRendition.getFieldName()[i],
//						new Color(transparent * 256 * 256 * 256
//								+ thematicRendition.getFieldColor()[i], true));
//			}

			//int xOffSet = 5;//水平方向时用
			int yOffSet = 5;//垂直方向用

			// 处理极端数据
		//	double realItemMargin = itemMargin * (values.length - 1) * 1.0 / width;// 根据设置的柱子间的间距，求出所有间距之和占据绘图区域宽度的百分比
		//	double barWidth = (width * (1 - realItemMargin) / values.length);
			double barWidth = width/2*(1-0.25*2);
			if (barWidth > width * 0.51) {
				barWidth = width * 0.51;
			}

			for (int i = 0; i < 1; i++) {
				if (values[0] > (averageValue * 4)) {
					int barX0 = (int) x - (int) (barWidth / 2);
//					if (barWidth < width * 0.51) {
//						barX0 = (int) x - (int) (barWidth / 2)
//								;
//					} else {
//						barX0 = (int) (x - width * 0.51 / 2);
//					}
					int barHeight = (int) (0.8 * height);// 断裂前80%
					int innerHeight = (int) (0.02 * height);// 锯齿高度
					int innerWidth = (int) (barWidth / 3);// 锯齿宽度
					int barY0 = (int) (y);

//					GradientPaint gradientPaint = new GradientPaint((float) barX0,
//							(float) (barY0), fieldNameAndColorHashMap
//									.get(thematicRendition.getFieldName()[i]),
//							(float) (barX0 + barWidth / 2), (float) (barY0),
//							new Color(255, 255, 255, 255), true);

					barHeight = (int) (0.8 * height);// 断裂前80%
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
					Arc2D arcDown = new Arc2D.Double(barX0, barY0 - yOffSet * 1.0
							/ 2, barWidth, yOffSet, 180, 180, Arc2D.OPEN);
					polygonDown.append(arcDown, true);
					polygonDown.lineTo((float) xpointsDown[4],
							(float) ypointsDown[4]);
					polygonDown.lineTo((float) xpointsDown[3],
							(float) ypointsDown[3]);
					polygonDown.lineTo((float) xpointsDown[2],
							(float) ypointsDown[2]);
					polygonDown.closePath();

					barHeight = barHeight + innerHeight;
					int[] xpointsUp = { barX0, barX0, barX0 + innerWidth,
							barX0 + 2 * innerWidth, barX0 + 3 * innerWidth,
							barX0 + 3 * innerWidth };
					int[] ypointsUp = { barY0 - (int) (0.9 * height),
							barY0 - barHeight, barY0 - barHeight + innerHeight,
							barY0 - barHeight, barY0 - barHeight + innerHeight,
							barY0 - (int) (0.9 * height) };

					polygonUp.moveTo((float) xpointsUp[1], (float) ypointsUp[1]);
					polygonUp.lineTo((float) xpointsUp[0], (float) ypointsUp[0]);
					Arc2D arcUp = new Arc2D.Double(barX0, barY0
							- (int) (0.9 * height) - yOffSet * 1.0 / 2, barWidth,
							yOffSet, 180, -180, Arc2D.OPEN);
					polygonUp.append(arcUp, true);
					polygonUp.lineTo((float) xpointsUp[4], (float) ypointsUp[4]);
					polygonUp.lineTo((float) xpointsUp[3], (float) ypointsUp[3]);
					polygonUp.lineTo((float) xpointsUp[2], (float) ypointsUp[2]);
					polygonUp.closePath();

					Shape top = new Ellipse2D.Double(barX0, barY0
							- (int) (0.9 * height) - yOffSet * 1.0 / 2, barWidth,
							yOffSet);
					//g.setPaint(gradientPaint);
					g.setColor(new Color(thematicRendition.getFieldColor()[1],false));
					g.fill(polygonUp);
					g.fill(polygonDown);
					
					g.fill(top);
					// 断裂处理的必须标上标签
					Font font = new Font(itemLabelFontNames, Font.PLAIN,
							itemLabelFontSizes);
					g.setColor(new Color(itemLabelPaints, false));
					g.setFont(font);
					g.drawString(Float.toString((float) values[0]), (int) barX0,
							(int) barY0 - (int) (0.9 * height));

					if (Outlines) {

						g.setColor(new Color(outLinePaints, false));
						BasicStroke basicStroke = new BasicStroke(
								outLineBasicStrokes);
						g.setStroke(basicStroke);
						g.draw(polygonUp);
						g.draw(polygonDown);
						g.draw(top);
					}

				} else {
					int barX0 = (int) x - (int) (barWidth / 2);
//					if (barWidth < width * 0.51) {
//						barX0 = (int) x - (int) (width / 2)
//								+ (int) (barWidth + itemMargin) * i;
//					} else {
//						barX0 = (int) (x - width * 0.51 / 2);
//					}
					int barY0 = (int) (y);
					double barHeight = values[0] / maxValue * height;
//					if (barHeight < minimumBarLength) {
//						barHeight = minimumBarLength;
//					}

//					GradientPaint gradientPaint = new GradientPaint((float) barX0,
//							(float) (barY0), fieldNameAndColorHashMap
//									.get(thematicRendition.getFieldName()[i]),
//							(float) (barX0 + barWidth / 2), (float) (barY0),
//							new Color(255, 255, 255, 255), true);

					int[] xpointsDown = { barX0, barX0, (int) (barX0 + barWidth),
							(int) (barX0 + barWidth) };
					int[] ypointsDown = { barY0, barY0 - (int) barHeight,
							barY0 - (int) barHeight, barY0 };

					GeneralPath polygon = new GeneralPath();

					polygon.moveTo((float) xpointsDown[1], (float) ypointsDown[1]);
					polygon.lineTo((float) xpointsDown[0], (float) ypointsDown[0]);
					Arc2D arcDown = new Arc2D.Double(barX0, barY0 - yOffSet * 1.0
							/ 2, barWidth, yOffSet, 180, 180, Arc2D.OPEN);
					polygon.append(arcDown, true);
					polygon.lineTo((float) xpointsDown[2], (float) ypointsDown[2]);
					Arc2D arcUp = new Arc2D.Double(barX0, barY0 - barHeight
							- yOffSet * 1.0 / 2, barWidth, yOffSet, 0, 180,
							Arc2D.OPEN);
					polygon.append(arcUp, true);
					polygon.closePath();
					Shape top = new Ellipse2D.Double(barX0, barY0 - barHeight
							- yOffSet * 1.0 / 2, barWidth, yOffSet);
					
					g.setColor(new Color(thematicRendition.getFieldColor()[group],false));
					g.fill(polygon);
//					g.setColor(fieldNameAndColorHashMap.get(
//							thematicRendition.getFieldName()[group]).brighter());
					g.fill(top);

					if (Outlines) {

						g.setColor(new Color(outLinePaints, false));
						BasicStroke basicStroke = new BasicStroke(
								outLineBasicStrokes);
						g.setStroke(basicStroke);
						g.draw(polygon);
						g.draw(top);
					}
					if (isLables) {
						Font font = new Font(itemLabelFontNames, Font.PLAIN,
								itemLabelFontSizes);
						g.setColor(new Color(itemLabelPaints, false));
						g.setFont(font);
						g.drawString(Float.toString((float) values[0]), barX0,
								(int) (barY0 - barHeight));
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
			   double sum = 0;
			   for(int i=0;i<indicatorDatas[0].getValues().length-1;i++){
		      	   sum = sum + indicatorDatas[0].getValues()[i]; 
		  	      }
			    double pieRadius = sum*1.0/width/thematicRendition.getScales()[0];
			    pieRadius = Math.sqrt(pieRadius);
//			    double interiorGap = 0.5 - pieRadius / 2;
//			    	if(interiorGap>0.425){
//			    		interiorGap = 0.425;
//			    	}
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
			for (int i = 0; i < seriesList.length-1; i++) 
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
//			@SuppressWarnings("unused")
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
							/ 2 + (barWidth) * i, domainAxis.getY2(),
							domainAxis.getX1() + barWidth / 2
									+ (barWidth ) * i, domainAxis
									.getY2() + 2);
				} else {
					line2dDomain = new Line2D.Double(
							domainAxis.getX1() + width / 2, domainAxis.getY2(),
							domainAxis.getX1() + width / 2, domainAxis.getY2() + 2);

				}
				g2d.draw(line2dDomain);
				g2d.drawString(thematicRendition.getFieldName()[group],
						(int) line2dDomain.getX1() - 10,
						(int) line2dDomain.getY2() + 2 + 10);
			}

			// 图
			double x = X + barWidth * 1.0 ;
			double y = Y+height/2;

			int yOffSet = (int) (barWidth / 4);
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

			int[] xpointsDown = { barX0, barX0, (int) (barX0 + barWidth),
					(int) (barX0 + barWidth) };
			int[] ypointsDown = { barY0, barY0 - (int) height,
					barY0 - (int) height, barY0 };

			GeneralPath polygon = new GeneralPath();

			polygon.moveTo((float) xpointsDown[1], (float) ypointsDown[1]);
			polygon.lineTo((float) xpointsDown[0], (float) ypointsDown[0]);
			Arc2D arcDown = new Arc2D.Double(barX0, barY0 - yOffSet * 1.0
					/ 2, barWidth, yOffSet, 180, 180, Arc2D.OPEN);
			polygon.append(arcDown, true);
			polygon.lineTo((float) xpointsDown[2], (float) ypointsDown[2]);
			Arc2D arcUp = new Arc2D.Double(barX0, barY0 - height
					- yOffSet * 1.0 / 2, barWidth, yOffSet, 0, 180,
					Arc2D.OPEN);
			polygon.append(arcUp, true);
			polygon.closePath();
			Shape top = new Ellipse2D.Double(barX0, barY0 - height
					- yOffSet * 1.0 / 2, barWidth, yOffSet);
			g2d.setColor(new Color(thematicRendition.getFieldColor()[group],false));
			g2d.fill(polygon);
//			g2d.setColor(fieldNameAndColorHashMap.get(
//					thematicRendition.getFieldName()[group]).brighter());
			g2d.fill(top);


//						if (drawBarOutline) {

							g2d.setColor(Color.black);
							BasicStroke basicStroke = new BasicStroke(0.5f);
							g2d.setStroke(basicStroke);
							g2d.draw(polygon);
							g2d.draw(top);
//						}
						

			// 图例
			FontMetrics fm1 = g2d.getFontMetrics();
			Rectangle2D rec1 = fm1.getStringBounds(fieldList[0], g2);
			Font font2 = new Font("宋体", Font.PLAIN, 15);
			int xx = symbolWidth / 2 - 6 - (int) (rec1.getWidth() / 2);
			for (int i = 0; i < fieldList.length; i++) {

				Rectangle2D.Double fieldRect = new Rectangle2D.Double(xx,
						domainAxis.getY1() + 4 + 10 + 5 + 15 - 7 + 21 * i, 7, 7);
//				g2d.setColor(fieldNameAndColorHashMap.get(fieldList[group]));
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
			  double sum = 0;
		        for(int i=0;i<indicatorDatas[0].getValues().length-1;i++){
		        	sum = sum + indicatorDatas[0].getValues()[i];
		        }
		    	double barWidth = width/2*(1-0.25*2);
//				  double R = width/2*(1-0.15*2); 
//			      //double R1 = 
//			      double R1 = width/2*(1-0.15*2)*0.5;
			      System.out.println(width);
					double ANGLE = 0;
					double ANGLE1 = 0;
//					Rectangle2D.Double symbolEnvlope1 =  new 
//					Rectangle2D.Double(x-width*0.7/2, y-width*2*0.7/2.75/2, width*0.7, width*2*0.7/2.75); 
//					Rectangle2D.Double symbolEnvlope2 =  new 
//					Rectangle2D.Double(x-width/4, y-width/2/2.75, width/2, width/2.75); 
					Rectangle2D.Double symbolEnvlope1 =  new 
					Rectangle2D.Double(x-width/2*0.7, y-width/2/1.5*0.7, width*0.7, width/1.5*0.7); 
					Rectangle2D.Double symbolEnvlope2 =  new 
					Rectangle2D.Double(x-width/4, y-width/4/1.5, width/2, width/2/1.5); 
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
//		    			hotAreahHashMap.put(text, rectangleDouble);
					for(int i=0;i<thematicRendition.getFieldName().length-1;i++){				
						Arc2D.Double arc1 = new Arc2D.Double();
						Arc2D.Double arc2 = new Arc2D.Double();
						
							
							//饼
						 ANGLE = indicatorDatas[0].getValues()[i]/sum*360;							
						 arc1 = new Arc2D.Double(symbolEnvlope1, ANGLE1, ANGLE, 2);
						 arc2 = new Arc2D.Double(symbolEnvlope2, 0, 360, 2);
						 	Area areaArc1 = new Area(arc1);							
							Area areaArc2 = new Area(arc2);
							areaArc1.subtract(areaArc2);
						ANGLE1 += ANGLE;

							Area area1 = new Area(areaArc1);
							Area area2 = new Area(rectangleDouble);
							area1.subtract(area2);
							//g.draw(area1);
							
							if (thematicRendition.getDomainAxis()[0] == null
									|| (thematicRendition.getDomainAxis()[0].length() == 0)) {
			    				text = "(" + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
							}else {
								text = "("+thematicRendition.getDomainAxis()[0] + "," + indicatorDatas[0].getNames()[i]+")="+indicatorDatas[0].getValues()[i];
							}
			    			hotAreahHashMap.put(text, area1);
			}
			return hotAreahHashMap;

		}

	}