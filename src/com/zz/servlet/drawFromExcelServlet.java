package com.zz.servlet;

import com.zz.chart.chartfactory.ChartFactory;
import com.zz.chart.chartfactory.ChartStyleFactory;
import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.ClassData;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.data.ReadRegionData;
import com.zz.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.awt.SunHints;
import sun.misc.BASE64Encoder;
import telecarto.data.util.ColorUtil;
import telecarto.geoinfo.db.DBManager;
import telecarto.geoinfo.db.MysqlAccessBean;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@WebServlet(name = "drawFromExcelServlet")
public class drawFromExcelServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/javascript;charset=UTF-8");//返回json格式的数据
        request.setCharacterEncoding("UTF-8");//设置服务器端对前端传输数据的解码方式!!!
        String inputType=request.getParameter("inputType");
        switch (inputType){
            case "chartLayerData":
                doChartLayer(request,response);
                break;
            case "classLayerData":
                doClassLayer(request,response);
                break;
            default:
                System.out.print("不支持的数据输入类型");

        }


    }
    //制作统计图表
    public void doChartLayer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ip = NetworkUtil.getIpAddr(request);

        //获得统计数据各指标
        String chartLayerNum = request.getParameter("chartLayerNum");
        JSONObject dataJson = JSONObject.fromObject(request.getParameter("allTjLayerContent"));
        JSONObject statisticdataJson=JSONObject.fromObject(dataJson.getJSONObject("statisticdata"));
        System.out.println(statisticdataJson);
//        JSONObject statisticdataJson=JSONObject.fromObject(statisticJson);
        //String tabID=statisticdataJson.getString("tabId");    //统计来源标识ID，1-本地数据库，2-API，3-Excel
        String dataAddress=statisticdataJson.getString("excelAddress");//Excel表格位置
        dataAddress=getServletContext().getRealPath("/")+dataAddress.substring(dataAddress.lastIndexOf("\\uploadFile"));
        String spatialId=statisticdataJson.getString("spatialId");
       String year = statisticdataJson.getString("yearColomn"); //根据实际情况修改

        JSONArray fieldsNameArray=statisticdataJson.getJSONArray("fieldsName");
        //String fieldsNames=statisticdataJson.getString("fieldsName");
        String[] fieldsName=new String[fieldsNameArray.size()];
        if(fieldsNameArray!=null||fieldsNameArray.size()!=0){
            for(int i=0;i<fieldsNameArray.size();i++){
                fieldsName[i]=fieldsNameArray.get(i).toString();
            }
        }

        //获得画图数据各指标
        JSONObject cartographydataJson=JSONObject.fromObject(dataJson.getJSONObject("cartographydata"));
        System.out.println(cartographydataJson);
//        String type=cartographydataJson.getString("cartographydataJson");
        String chartID0=cartographydataJson.getString("chartID");
        String chartID = chartID0.substring(1, 6);
        System.out.println(chartID);
//        String chartID="10101";

        String dataSource="Excel文件";

        int width = cartographydataJson.getInt("symbolSizeSliderValue");// 符号长宽

        int height= cartographydataJson.getInt("symbolSizeSliderValue");

        //String[] arr = fieldsNames.split(",");


        //根据所选指标的数目进行不同的色彩配置(有几个指标配置几个色彩渐变)
        String colorRampSchema1 = cartographydataJson.getString("colorName");

        String colorString = ColorUtil.getColorScheme(fieldsName.length,colorRampSchema1);
        String[] colors = colorString.split(";");
        int[] fieldColors = new int[colors.length]; // 专题符号颜色
        for (int i = 0; i < fieldColors.length; i++) {
            fieldColors[i] = Integer.parseInt(colors[i]);
        }
        String regionParam = request.getParameter("regionParam");


        //Rectangle2D.Double DC = JUtil.StringToRect(dcString);// DC
        ChartStyleFactory chartStyleFactory = new ChartStyleFactory();
        ChartStyle chartStyle = chartStyleFactory.createcChartStyle(chartID);// 通过工厂模式实例化符号样式类



        //根据符号ID加载符号样式
        String dir = "assets/";
        String chartPath = dir + chartID + ".xml";
        chartStyle.Load(chartPath);
        //初始化符号参数
        //统计符号的chartDataPara
        ChartDataPara chartDataPara = new ChartDataPara();

        chartDataPara.initial_ZJExcel(fieldsName,dataSource);// 初始化专题符号层参数

        chartDataPara.setFieldColor(fieldColors);
        chartDataPara.setWidth(width);
        chartDataPara.setHeight(height);

        ExcelProcess.doReadExcelForAllData(dataAddress,fieldsName,spatialId);
        IndicatorData[] indicatorDatas=ExcelProcess.getIndicatorDatas();

        ReadRegionData.doReadRegionDataByEachRegion(ExcelProcess.getSpatialNames());
        double[] maxValues = JUtil.maxValues(indicatorDatas);
        double[] minValues = JUtil.minValues(indicatorDatas);
        double[] averageValues = JUtil.averageValues(indicatorDatas);
        double[] scales = JUtil.scales(indicatorDatas, width);
        chartDataPara.setScales(scales);
        String[] xStrings = ReadRegionData.getRegonX();
        String[] yStrings = ReadRegionData.getRegonY();
        //String[] nameStrings = ReadRegionData.getRegonName();//regionCodes为区域代码数组

        //生成图例
        ChartFactory chartFactoryLegend = new ChartFactory();
        IChart chartLegend = chartFactoryLegend.createcChart(chartID);
        IndicatorData[] indicatorDataLegend = new IndicatorData[1];
        indicatorDataLegend[0] = indicatorDatas[0];
        BufferedImage bi = chartLegend.drawLegend(80, 160, chartDataPara, chartStyle, maxValues, minValues, averageValues, indicatorDataLegend);
        BufferedImage bufferedImage = null;
        String tempPath = getServletContext().getRealPath("/") + "printMap";
        String tempFilePath = getServletContext().getRealPath("/") + "printMap\\"+ ip.replaceAll("\\.","-");

        if(chartLayerNum.equals("1")){
            bufferedImage = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bufferedImage.createGraphics();
            //			g2d.setColor(new Color(247,247,247));
            g2d.setColor(Color.white);
            g2d.fillRect(0,0,bi.getWidth(),bi.getHeight());
            //把图例bi绘制到bufferedImage上
            g2d.drawImage(bi,0,0,bi.getWidth(),bi.getHeight(),null);
            g2d.dispose();
        }else {
            String formerChartLegend = getServletContext().getRealPath("/") + "printMap\\"+ ip.replaceAll("\\.","-") +"\\"+ "chartLegend.png";
            BufferedImage chartImage = ImageIO.read(new FileInputStream(formerChartLegend));

            bufferedImage = new BufferedImage(bi.getWidth(),bi.getHeight()+chartImage.getHeight(),BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.setColor(Color.white);
            g2d.fillRect(0,0,bufferedImage.getWidth(),bufferedImage.getHeight());

            //把两个图例绘制到bufferedImage上
            g2d.drawImage(bi,0,0,bi.getWidth(),bi.getHeight(),null);
            g2d.drawImage(chartImage,0,bi.getHeight(),chartImage.getWidth(),chartImage.getHeight(),null);
            g2d.dispose();

        }

        //图例图片转码为base64
        BASE64Encoder encoderLegend = new BASE64Encoder();
        ByteArrayOutputStream baosLegend = new ByteArrayOutputStream();
        ImageIO.setUseCache(false);
        ImageIO.write(bufferedImage, "png", baosLegend);
        byte[] bytesLegend = baosLegend.toByteArray();
        String imgStreamLegend = encoderLegend.encodeBuffer(bytesLegend).trim();
        //输出统计图图例到本地(底色透明)
//        String tempPath = getServletContext().getRealPath("/") + "printMap";
//        String tempFilePath = getServletContext().getRealPath("/") + "printMap\\"+ ip.replaceAll("\\.","-");
        File fileSavepath = new File(tempPath);
        File fileSavepathMap = new File(tempFilePath);
        if(!fileSavepath.exists()){
            fileSavepath.mkdirs();
        }
        if(!fileSavepathMap.exists()){
            fileSavepathMap.mkdirs();
        }
        String imgPath = fileSavepathMap + "/"+"chartLegend.png";
        ImageIO.write(bufferedImage, "png", new File(imgPath));



        /*单张图绘制,并进行最小闭包处理*/
        int indiNum = indicatorDatas[0].getNames().length;//指标数目
        String[] indiNames = new String[indiNum];
        String[] indiUnits = new String[indiNum];

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
            IChart chart = chartFactory.createcChart(chartID);
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
        //String timeData = yearString + "年";
        //chartsObject.put("year",timeData);
        chartsObject.put("source",dataSource);
        chartsObject.put("chartLegend",imgStreamLegend.replaceAll("[\\s*\t\n\r]", ""));
        chartsObject.put("type","chartLayer");
        PrintWriter writer = response.getWriter();
        writer.print(chartsObject);
        writer.close();

    }
    //制作分级地图
    public void doClassLayer(HttpServletRequest request, HttpServletResponse response){
        MysqlAccessBean mysql = new MysqlAccessBean();

        JSONObject dataJson = JSONObject.fromObject(request.getParameter("allTjLayerContent"));
        JSONObject statisticdataJson=JSONObject.fromObject(dataJson.getJSONObject("statisticdata"));

        String spatialId = statisticdataJson.getString("spatialId");

        JSONArray fieldsNameArray=statisticdataJson.getJSONArray("fieldsName");
        //String fieldsNames=statisticdataJson.getString("fieldsName");
        String[] fieldsName=new String[fieldsNameArray.size()];
        if(fieldsNameArray!=null||fieldsNameArray.size()!=0){
            for(int i=0;i<fieldsNameArray.size();i++){
                fieldsName[i]=fieldsNameArray.get(i).toString();
            }
        }
        String dataFieldName=fieldsName[0];
        String year = statisticdataJson.getString("yearColomn");

        JSONObject cartographydataJson=JSONObject.fromObject(dataJson.getJSONObject("cartographydata"));

        int breakNum = Integer.parseInt(cartographydataJson.getString("classNumSliderValue"));
        String breakMethod=cartographydataJson.getString("modelName");
        String ip = NetworkUtil.getIpAddr(request);

        String dataAddress=statisticdataJson.getString("excelAddress");//Excel表格位置
        dataAddress=getServletContext().getRealPath("/")+dataAddress.substring(dataAddress.lastIndexOf("\\uploadFile"));

        String color = cartographydataJson.getString("colors");
        String  colors[]= color.trim().split(";");
        System.out.println(colors);
        //根据输入行政等级class，确立
//        if (regionParam.equals("1")){
//            sql="SELECT\n" +
//                    "\tregion_info_copy1.citycode, region_info_copy1.name, region_info_copy1.x, region_info_copy1.y, region_info_copy1.json, " + classTableName + ".`" + dataFieldName +"`" +
//                    "\tFROM\n" +
//                    "\tregion_info_copy1\n" +
//                    "LEFT JOIN\t"+ classTableName +"\n" +
//                    "ON region_info_copy1.citycode="+ classTableName +".`"+ spatialId +"`\n" +
//                    "WHERE\n" +
//                    "\tregion_info_copy1.class = " + regionParam +" AND "+ classTableName +".`年份` LIKE '" + year +"'";
//        }
//        else if (regionParam.equals("2")){
//            sql="SELECT\n" +
//                    "\tregion_info_copy1.coutcode, region_info_copy1.name, region_info_copy1.x, region_info_copy1.y, region_info_copy1.json, " + classTableName + ".`" + dataFieldName +"`" +
//                    "\tFROM\n" +
//                    "\tregion_info_copy1\n" +
//                    "LEFT JOIN\t"+ classTableName +"\n" +
//                    "ON region_info_copy1.coutcode="+ classTableName +".`"+ spatialId +"`\n" +
//                    "WHERE\n" +
//                    "\tregion_info_copy1.class = " + regionParam +" AND "+ classTableName +".`年份` LIKE '" + year +"'";
//        }
        ExcelProcess.doReadExcelForAllData(dataAddress,fieldsName,spatialId);
        String[] regionNames=ExcelProcess.getSpatialNames();//获取空间字段值
        String[] spData=ExcelProcess.getSpFieldData(dataAddress,dataFieldName);
        try {

            Connection connection = DBManager.getConnection();
            ArrayList<ClassData> classList=new ArrayList<>();
            ResultSet resultSet;
            PreparedStatement pst=connection.prepareStatement( "SELECT name,citycode,x,y,json FROM region_info_copy1 "  +
                    " WHERE name =  ? ");
            for(int i=0;i<regionNames.length;i++){
                pst.setString(1, regionNames[i]);
                resultSet=pst.executeQuery();
                while (resultSet.next()) {
                    ClassData classData = new ClassData(resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            spData[i],
                            dataFieldName);
                    classList.add(classData);
                }
            }
            pst.close();
//            }
            double maxValue =  Double.parseDouble(classList.get(0).getData());
            double minValue = Double.parseDouble(classList.get(0).getData());
            for (int i=0;i<classList.size();i++){
                if(Double.parseDouble(classList.get(i).getData())>maxValue){
                    maxValue = Double.parseDouble(classList.get(i).getData());
                }
                if(Double.parseDouble(classList.get(i).getData())<minValue){
                    minValue = Double.parseDouble(classList.get(i).getData());
                }
            }
            //采用对应的分类方法,根据各区域的数据值进行分类,并赋予颜色
            Classifiter classifiter = new Classifiter();
            JSONArray classDataArray = classifiter.getClassIntervalJson(minValue,maxValue,breakNum,classList,colors,breakMethod);
            JSONObject classObject = new JSONObject();
            //绘制分级图图例
            double []classInterval = classifiter.getIntervals(minValue,maxValue,breakNum,breakMethod);
            int width = 225;
            int height = 21 * breakNum+45;
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();

            g2d.setColor(Color.white);

            g2d.fillRect(0,0,image.getWidth(),image.getHeight());
            g2d.dispose();

            g2d = image.createGraphics();
            //文字抗锯齿化处理
            g2d.setRenderingHint(SunHints.KEY_ANTIALIASING, SunHints.VALUE_ANTIALIAS_ON);

            Font font = new Font("黑体", Font.PLAIN, 17);
            g2d.setFont(font);
            g2d.setColor(Color.black);

            // 获取图例标题的像素范围对象
            double fontWidth = imageUtil.getTitleSize(g2d,font,dataFieldName);
            int stringWidth = new BigDecimal(fontWidth).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            int startX = new BigDecimal((225.0 - stringWidth)/2).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            if(startX>0){
                g2d.drawString(dataFieldName, startX, 25);
            }
            else {
                g2d.setFont(new Font("黑体", Font.PLAIN, 16));
                fontWidth = imageUtil.getTitleSize(g2d,font,dataFieldName);
                stringWidth = new BigDecimal(fontWidth).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                startX = new BigDecimal((225 - stringWidth)/2).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                g2d.drawString(dataFieldName, startX, 25);
            }

//            g2d.drawString("分级图图例", 40, 25);
            font = new Font("", Font.PLAIN, 13);
            g2d.setFont(font);

            for (int i=0;i<colors.length;i++){
                String temp = colors[i].substring(colors[i].indexOf("(") + 1, colors[i].indexOf(")"));
                String[] rgbTemp = temp.trim().split(",");
                //绘制空心矩形(色块边线)
                g2d.setColor(Color.black);
                g2d.drawRect(20,i*21+37,28,16);
                //设置颜色-->色块
                g2d.setColor(new Color(Integer.parseInt(rgbTemp[0].trim()), Integer
                        .parseInt(rgbTemp[1].trim()), Integer.parseInt(rgbTemp[2].trim())));
                //绘制实心矩形色块
                g2d.fillRect(21,i*21+37+1,27,15);
                //设置颜色-->文字
                g2d.setColor(Color.black);
                if(i == 0){
                    String legendStr = "< " + classInterval[0];
                    g2d.drawString(legendStr, 55, i*21+39+12);
                }
                else if(i==colors.length-1){
                    String legendStr = ">= " + classInterval[i-1];
                    g2d.drawString(legendStr, 55, i*21+39+12);
                }
                else {
                    String legendStr;
                    legendStr = classInterval[i-1] + " ~ " + classInterval[i];
                    g2d.drawString(legendStr, 55, i*21+39+12);
                }
            }
            g2d.dispose();
            //图例图片转码为base64
            BASE64Encoder encoderLegend = new sun.misc.BASE64Encoder();
            ByteArrayOutputStream baosLegend = new ByteArrayOutputStream();
            ImageIO.setUseCache(false);
            ImageIO.write(image, "png", baosLegend);
            byte[] bytesLegend = baosLegend.toByteArray();
            String imgStreamLegend = encoderLegend.encodeBuffer(bytesLegend).trim();
            //输出分级图图例到本地
            String tempFilePath = getServletContext().getRealPath("/") + "printMap/" + ip.replaceAll("\\.","-");
            File fileSavepath = new File(tempFilePath);
            if(!fileSavepath.exists()){
                fileSavepath.mkdirs();
            }
            String imgPath = tempFilePath + "/"+"classLegend.png";
            ImageIO.write(image, "png", new File(imgPath));

            //传输JSON分级grapgics数组到前端
            classObject.put("classDataArray",classDataArray);
            //classObject.put("dataSource",dataSource);
            classObject.put("classLegend",imgStreamLegend.replaceAll("[\\s*\t\n\r]", ""));
            PrintWriter out = response.getWriter();
            out.print(classObject);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            mysql.close();
        }
    }
}
