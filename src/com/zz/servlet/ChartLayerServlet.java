package com.zz.servlet;

import com.zz.chart.chartfactory.ChartFactory;
import com.zz.chart.chartfactory.ChartStyleFactory;
import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.data.ReadRegionData;
import com.zz.util.ConvexHull;
import com.zz.util.JUtil;
import com.zz.util.NetworkUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;
import telecarto.data.util.ChEnConverter;
import telecarto.data.util.ColorUtil;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ChartLayerServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */

	public ChartLayerServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

//		response.setContentType("image/png;charset=UTF-8");
		response.setContentType("text/javascript;charset=UTF-8");//返回json格式的数据
		request.setCharacterEncoding("UTF-8");//设置服务器端对前端传输数据的解码方式!!!

		//获取客户端的ip地址
		String ip = NetworkUtil.getIpAddr(request);

		String wcString = request.getParameter("wc");
		String dcString = request.getParameter("dc");
		String chartid = request.getParameter("CHARTID");// 专题符号id
		String widthstring = request.getParameter("WIDTH");// 符号长宽
		String heightstring = request.getParameter("WIDTH");
		String regionParam = request.getParameter("regionParam");

//		String islabelString = request.getParameter("ISLABEL");
		String islabelString = "false";
		String yearString = request.getParameter("year");

		String chartData = request.getParameter("CHARTDATA");
		System.out.println("chart data: "+chartData);
		String[] arr = chartData.split(",");
		System.out.println(arr);
		String thematicData = arr[0];
		//将中文表名转换为数据库中对应的表名
		thematicData = ChEnConverter.convertTableNameCH2En(thematicData);

		//将所选中文指标名转换为数据库中对应字段名
		for (int i = 1; i < arr.length; i++) {
//			thematicData += "," + ChEnConverter.covertColNameCh2En(arr[i]);
			thematicData += "," + arr[i];
		}
		System.out.println(thematicData);

		//根据所选指标的数目进行不同的色彩配置(有几个指标配置几个色彩渐变)
		String colorRampSchema1 = request.getParameter("colorRampSchema");
//		System.out.println(colorRampSchema1);
		String colorString;
		colorString = ColorUtil.getColorScheme(arr.length - 1,colorRampSchema1);
//		System.out.println("color: " + colorString);


		Rectangle2D.Double WC = JUtil.StringToRect(wcString);// WC
		Rectangle2D.Double DC = JUtil.StringToRect(dcString);// DC

		ChartStyleFactory chartStyleFactory = new ChartStyleFactory();
		ChartStyle chartStyle = chartStyleFactory.createcChartStyle(chartid);// 通过工厂模式实例化符号样式类

		int width = Integer.parseInt(widthstring);
		int height = Integer.parseInt(heightstring);

		/*
		 * width = (int) (1000000*width / WC.width); height = (int)
		 * (1600000*height / WC.height);//width,height
		 */
		String dir = "assets/";
		String chartPath = dir + chartid + ".xml";
		chartStyle.Load(chartPath);

		chartStyle.setLabels(Boolean.parseBoolean(islabelString));
		// chartStyle.setLabels(false);
		chartStyle.setLabelsFontSize(width / 10);

		//统计符号的chartDataPara
		ChartDataPara chartDataPara = new ChartDataPara();
		chartDataPara.initial(thematicData, yearString);// 初始化专题符号层参数

		String[] colors = colorString.split(";");
		int[] fieldColors = new int[colors.length]; // 专题符号颜色
		for (int i = 0; i < fieldColors.length; i++) {
			fieldColors[i] = Integer.parseInt(colors[i]);
		}
		chartDataPara.setFieldColor(fieldColors);
		chartDataPara.setWidth(width);
		chartDataPara.setHeight(height);

		//图例的chartDataPara
		ChartDataPara chartDataParaLegend = new ChartDataPara();
		chartDataParaLegend.setFieldColor(fieldColors);
		int widthLegend = 80;
		int heightLegend = 160;
		chartDataParaLegend.setWidth(widthLegend);
		chartDataParaLegend.setHeight(heightLegend);


		IndicatorData[] indicatorDatas = JUtil.getIndicatorData(thematicData, chartDataPara,regionParam,yearString);
		//		ReadRegionData regionData = new ReadRegionData();
//		String[] regionCodes = regionData.getRegonCode();
		String[] regionCodes = ReadRegionData.getRegonCode();

		if (indicatorDatas.length != regionCodes.length) {
			System.out.println("无该年份数据");
			BufferedImage bi = new BufferedImage((int) DC.getWidth(),
					(int) DC.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2D = bi.createGraphics();
			g2D.setColor(new Color(255, 255, 255, 100));
			g2D.fillRect(0, 0, (int) DC.getWidth(), (int) DC.getHeight());
			g2D.setColor(Color.black);
			g2D.setFont(new Font("黑体", Font.PLAIN, 24));
			g2D.drawString("无该年份数据", (int) DC.getWidth() / 2 - 72,
					(int) DC.getHeight() / 2 - 12);
			ServletOutputStream sos = response.getOutputStream();

			ImageIO.write(bi, "PNG", sos);
			//ImageIO.write(bi, "png", new File("D:\\testChartTest.png"));
			sos.close();
		} else {
			double[] maxValues = JUtil.maxValues(indicatorDatas);
			double[] minValues = JUtil.minValues(indicatorDatas);
			double[] averageValues = JUtil.averageValues(indicatorDatas);
			double[] scales = JUtil.scales(indicatorDatas, width);

			chartDataPara.setScales(scales);
//			String[] xStrings = regionData.getRegonX();
//			String[] yStrings = regionData.getRegonY();
//			String[] nameStrings = regionData.getRegonName();
 			String[] xStrings = ReadRegionData.getRegonX();
			String[] yStrings = ReadRegionData.getRegonY();
			String[] nameStrings = ReadRegionData.getRegonName();//regionCodes为区域代码数组

			//一整张图绘制部分如下:如果进行单张图绘制则不需要进行仿射变换处理
//			JAffine affine = new JAffine(regionCodes, xStrings, yStrings, WC, DC);
//			double[] X = affine.getX();
//			double[] Y = affine.getY();
//			BufferedImage bi = new BufferedImage((int) DC.getWidth(),
//					(int) DC.getHeight(), BufferedImage.TYPE_INT_ARGB);
//			Graphics2D g2D = bi.createGraphics();
//			//一张图具体的绘制过程如下,chart.drawChart
//			for (int i = 0; i < regionCodes.length; i++) {
//				IndicatorData[] indicatorData = new IndicatorData[1];
//				indicatorData[0] = indicatorDatas[i];
//				ChartFactory chartFactory = new ChartFactory();
//				IChart chart = chartFactory.createcChart(chartid);
//				chart.drawChart(g2D, X[i], Y[i], width, height, chartDataPara,
//						chartStyle, maxValues, minValues, averageValues,
//						indicatorData);
//				// chart.drawChart(graphics2d, 100, 100, 100, 100,
//				// chartDataPara, chartStyle, maxValues, minValues,
//				// averageValues, indicatorData);
//			}
//			ServletOutputStream sos = response.getOutputStream();
//			ImageIO.write(bi, "PNG", sos);

			//绘制图例
			ChartFactory chartFactoryLegend = new ChartFactory();
			IChart chartLegend = chartFactoryLegend.createcChart(chartid);
			IndicatorData[] indicatorDataLegend = new IndicatorData[1];
			indicatorDataLegend[0] = indicatorDatas[0];
			BufferedImage bi = chartLegend.drawLegend(80, 160, chartDataPara, chartStyle, maxValues, minValues, averageValues, indicatorDataLegend);

//		ServletOutputStream sos = response.getOutputStream();
			//绘制一个白色填充的矩形为图例打底
		/*  不带透明色的BufferedImage对象:TYPE_INT_RGB
			带透明色的BufferedImage对象:TYPE_INT_ARGB
		*/
			BufferedImage bufferedImage = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = bufferedImage.createGraphics();
//			g2d.setColor(new Color(247,247,247));
			g2d.setColor(Color.white);
			g2d.fillRect(0,0,bi.getWidth(),bi.getHeight());
			//把图例bi绘制到bufferedImage上
			g2d.drawImage(bi,0,0,bi.getWidth(),bi.getHeight(),null);
			g2d.dispose();

			//图例图片转码为base64
			BASE64Encoder encoderLegend = new sun.misc.BASE64Encoder();
			ByteArrayOutputStream baosLegend = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", baosLegend);
			byte[] bytesLegend = baosLegend.toByteArray();
			String imgStreamLegend = encoderLegend.encodeBuffer(bytesLegend).trim();

			//输出统计图图例到本地(底色透明)
			String tempPath = getServletContext().getRealPath("/") + "printMap";
			String tempFilePath = getServletContext().getRealPath("/") + "printMap\\"+ ip.replaceAll("\\.","-");
			File fileSavepath = new File(tempPath);
			File fileSavepathMap = new File(tempFilePath);
			if(!fileSavepath.exists()){
				fileSavepath.mkdir();
			}
			if(!fileSavepathMap.exists()){
				fileSavepathMap.mkdir();
			}
			String imgPath = fileSavepathMap + "/"+"chartLegend.png";
			ImageIO.write(bi, "png", new File(imgPath));

			/*单张图绘制,并进行最小闭包处理*/
			int indiNum = indicatorDatas[0].getNames().length;//指标数目
			String[] indiNames = new String[indiNum];
			String[] indiUnits = new String[indiNum];
			String indiSource = chartDataPara.getFieldSource();
			for (int p=0;p<indiNum;p++) {
				indiNames[p] = indicatorDatas[0].getNames()[p];
				indiUnits[p] = chartDataPara.getFieldUnits()[p];
			}
			JSONObject chartsObject = new JSONObject();
			JSONArray chartArray = new JSONArray();
			ArrayList<BufferedImage> biList = new ArrayList<>();

			for (int i = 0; i < regionCodes.length; i++) {
				IndicatorData[] indicatorData = new IndicatorData[1];
				indicatorData[0] = indicatorDatas[i];
				int biWidth = (new Double(width*1.3)).intValue();
//				int biHeight = (new Double(height*1.2)).intValue();
				BufferedImage bi2 = new BufferedImage(biWidth,height, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2D2 = bi2.createGraphics();
				ChartFactory chartFactory = new ChartFactory();
				IChart chart = chartFactory.createcChart(chartid);
				/*width/2, width/2,为绘制专题符号的中心点位置,这里width/2可以将符号绘制在正中心*/
				chart.drawChart(g2D2, width/2, width/2, width, height, chartDataPara,
						chartStyle, maxValues, minValues, averageValues,
						indicatorData);

				int imgWidth = bi2.getWidth();
				int imgHeight = bi2.getHeight();
                ConvexHull convex = new ConvexHull();
                int [] scanerY = convex.getScanerY(imgWidth,imgHeight,bi2);
                int [] scanerX = convex.getScanerX(imgWidth,imgHeight,bi2);
                int convexWidth = scanerX[1] - scanerX[0] +1;
                int convexHeight = scanerY[1] - scanerY[0] +1;
                int [] imageArry = new int[convexWidth*convexHeight];
                imageArry = bi2.getRGB(scanerX[0],scanerY[0],convexWidth,convexHeight,imageArry,0,convexWidth);

                //convexBI为新生成的最小闭包图片
                BufferedImage convexBI = new BufferedImage(convexWidth,convexHeight, BufferedImage.TYPE_INT_ARGB);
                convexBI.setRGB(0,0,convexWidth,convexHeight,imageArry,0,convexWidth);
                biList.add(convexBI);
				g2D2.dispose();

				//将生产的图片转换为base64编码的字符串
				BASE64Encoder encoder = new sun.misc.BASE64Encoder();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(biList.get(i), "png", baos);
				byte[] bytes = baos.toByteArray();
				String imgStream = encoder.encodeBuffer(bytes).trim();

				JSONObject singleChart = new JSONObject();
				JSONObject singleChartAttr = new JSONObject();
				singleChartAttr.put("rng_code", regionCodes[i]);
				singleChartAttr.put("rng_name", nameStrings[i]);
				singleChart.put("point_x", xStrings[i]);
				singleChart.put("point_y", yStrings[i]);
				for (int r=0;r<indiNum;r++){
					String indi = "indi" + r;
					String valueName = "value" + r;
					String value = indicatorDatas[i].getValues()[r] + indiUnits[r];
					singleChartAttr.put(indi, indiNames[r]);
					singleChartAttr.put(valueName, value);
				}
				singleChartAttr.put("indiNum", indiNum);
//				singleChart.put("indiNum",indiNum);
//				singleChart.put("unit", indiUnits[0]);
				singleChart.put("img",imgStream);
				singleChart.put("imgWidth",convexWidth);
				singleChart.put("imgHeight",convexHeight);
				singleChart.put("attributes",singleChartAttr);
				chartArray.add(singleChart);
			}
			chartsObject.put("charts",chartArray);
			String timeData = yearString + "年";
			chartsObject.put("year",timeData);
			chartsObject.put("source",indiSource);
			chartsObject.put("chartLegend",imgStreamLegend.replaceAll("[\\s*\t\n\r]", ""));
//			ServletOutputStream sos = response.getOutputStream();
//			sos.close();
//			System.out.println(chartsObject.toString());
			PrintWriter writer = response.getWriter();
			writer.print(chartsObject);
			writer.close();
		}
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
