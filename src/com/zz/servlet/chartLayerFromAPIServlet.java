package com.zz.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.et.mvc.util.Json;
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
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.PrintWriter;
import java.util.ArrayList;



@WebServlet(name = "chartLayerFromAPIServlet")
public class chartLayerFromAPIServlet extends HttpServlet {
    public chartLayerFromAPIServlet() {
        super();
    }
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/javascript;charset=UTF-8");//返回json格式的数据
        request.setCharacterEncoding("UTF-8");//设置服务器端对前端传输数据的解码方式!!!

        String ip = NetworkUtil.getIpAddr(request);
        //获得空间数据各个指标
        JSONObject spatialdataJson=JSONObject.fromObject(request.getParameter("spatialdata"));//空间数据json对象
        String tabID_spatialdata=spatialdataJson.getString("tabID");    //空间数据来源标识ID，1-行政区划，2-上传shp
        JSONArray regionDataValue=spatialdataJson.getJSONArray("regionDataValue");  //区域标识编码数组
        String fileName=spatialdataJson.getString("fileName");
        //获得统计数据各指标
        JSONObject statisticdataJson=JSONObject.fromObject(request.getParameter("statisticdata"));//空间数据json对象
        String tabID_statisticdata=statisticdataJson.getString("tabID");    //空间数据来源标识ID，1-行政区划，2-上传shp
        String dataAddress=statisticdataJson.getString("dataAddress");
        String tableName=statisticdataJson.getString("tableName");
        String spatialId=statisticdataJson.getString("spatialId");
        JSONArray fieldsName=statisticdataJson.getJSONArray("fieldsName");
        int fieldsNum=statisticdataJson.getInt("fieldsNum");
        
        //获得画图数据各指标
        JSONObject cartographydataJson= JSONObject.fromObject(request.getParameter("cartographydata"));
        String type=cartographydataJson.getString("cartographydataJson");
        String chartID=cartographydataJson.getString("chartID");
        JSONArray colorArray=cartographydataJson.getJSONArray("colors");
        int symbolSize=cartographydataJson.getInt("symbolSizeSliderValue");

        // String wcString = request.getParameter("wc");
        String dcString = request.getParameter("dc");
        String chartid = request.getParameter("chartID");// 专题符号id
        String widthstring = request.getParameter("symbolSizeSliderValue");// 符号长宽
        String heightstring = request.getParameter("WIDTH");
        String regionParam = request.getParameter("regionParam");

//		String islabelString = request.getParameter("ISLABEL");
       // String islabelString = "false";
        // String yearString = request.getParameter("year");

        String chartData = request.getParameter("CHARTDATA");
        System.out.println("chart data: "+chartData);
        String[] arr = chartData.split(",");
        System.out.println(arr);
        String thematicData = arr[0];

        //根据所选指标的数目进行不同的色彩配置(有几个指标配置几个色彩渐变)
        //String colorRampSchema1 = request.getParameter("colorRampSchema");

//		System.out.println(colorRampSchema1);
        String colorString="15538980;2498916;15855872";
        //colorString = ColorUtil.getColorScheme(arr.length - 1,colorRampSchema1);
        String[] colors = colorString.split(";");
        int[] fieldColors = new int[colors.length]; // 专题符号颜色
        for (int i = 0; i < fieldColors.length; i++) {
            fieldColors[i] = Integer.parseInt(colors[i]);
        }

        Rectangle2D.Double DC = JUtil.StringToRect(dcString);// DC
        ChartStyleFactory chartStyleFactory = new ChartStyleFactory();
        ChartStyle chartStyle = chartStyleFactory.createcChartStyle(chartid);// 通过工厂模式实例化符号样式类
        //设置符号宽高
        int width = Integer.parseInt(widthstring);
        int height = Integer.parseInt(heightstring);
        //根据符号ID加载符号样式
        String dir = "assets/";
        String chartPath = dir + chartid + ".xml";
        chartStyle.Load(chartPath);
        //初始化符号参数
        //统计符号的chartDataPara
        ChartDataPara chartDataPara = new ChartDataPara();
        String yearString="";


        chartDataPara.setFieldColor(fieldColors);
        chartDataPara.setWidth(width);
        chartDataPara.setHeight(height);


        //输入apiURL返回数据resultString
        //解析API返回的数据resultString，
        String URL="";
        String resultString=JUtil.getResultStrFromAPI("");
        IndicatorData[] indicatorDatas = JUtil.getIndicatorDataFromAPi(resultString);
        double[][] coordinatesXY=JUtil.getXYFromAPi(resultString);
//        String[] xStrings = ReadRegionData.getRegonX();
//        String[] yStrings = ReadRegionData.getRegonY();
        chartDataPara.initialAsAPI(indicatorDatas);// 初始化专题符号层参数
        //
        double[] maxValues = JUtil.maxValues(indicatorDatas);
        double[] minValues = JUtil.minValues(indicatorDatas);
        double[] averageValues = JUtil.averageValues(indicatorDatas);
        double[] scales = JUtil.scales(indicatorDatas, width);
        chartDataPara.setScales(scales);


        //生成图例
        ChartFactory chartFactoryLegend = new ChartFactory();
        IChart chartLegend = chartFactoryLegend.createcChart(chartid);
        IndicatorData[] indicatorDataLegend = new IndicatorData[1];
        indicatorDataLegend[0] = indicatorDatas[0];
        BufferedImage bi = chartLegend.drawLegend(80, 160, chartDataPara, chartStyle, maxValues, minValues, averageValues, indicatorDataLegend);
        BufferedImage bufferedImage = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
//			g2d.setColor(new Color(247,247,247));
        g2d.setColor(Color.white);
        g2d.fillRect(0,0,bi.getWidth(),bi.getHeight());
        //把图例bi绘制到bufferedImage上
        g2d.drawImage(bi,0,0,bi.getWidth(),bi.getHeight(),null);
        g2d.dispose();
        //图例图片转码为base64
        BASE64Encoder encoderLegend = new BASE64Encoder();
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
        String indiSource = URL;

        for (int p=0;p<indiNum;p++) {
            indiNames[p] = indicatorDatas[0].getNames()[p];
            indiUnits[p] ="";//指标单位未获得，默认为空
        }
        JSONObject chartsObject = new JSONObject();
        JSONArray chartArray = new JSONArray();
        ArrayList<BufferedImage> biList = new ArrayList<>();

        for (int i = 0; i < indicatorDatas.length; i++) {
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

            //将生产的图片转换为base64编码的字符串imgStream
            BASE64Encoder encoder = new BASE64Encoder();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(biList.get(i), "png", baos);
            byte[] bytes = baos.toByteArray();
            String imgStream = encoder.encodeBuffer(bytes).trim();

            JSONObject singleChart = new JSONObject();
            JSONObject singleChartAttr = new JSONObject();

//            singleChartAttr.put("rng_code", regionCodes[i]);
            singleChartAttr.put("rng_name", indicatorData[0].getDomainAxis());
            singleChart.put("point_x", coordinatesXY[i][0]);
            singleChart.put("point_y", coordinatesXY[i][1]);
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




        PrintWriter writer = response.getWriter();
        writer.print(chartsObject);
        writer.close();

//        String[] nameStrings = ReadRegionData.getRegonName();//regionCodes为区域代码数组

    }


    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException {
        // Put your code here
    }
}
