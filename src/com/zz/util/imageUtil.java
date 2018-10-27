package com.zz.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017/11/22.
 */
public class imageUtil {
    public static String getImgFromUrl(String strBackUrl,String ip,String urlstr, String savepath,String printLegendFlag,String layoutID,String dpi)
    {
        //String classLegendPath = getServletContext().getRealPath("/") + "printMap" + "/" + "classLegend.png"
        //String chartLegendPath = getServletContext().getRealPath("/") + "printMap" + "/" + "chartLegend.png"

        int num = urlstr.indexOf('/',8);
        int extnum = urlstr.lastIndexOf('.');
        String u = urlstr.substring(0,num);
        String ext = urlstr.substring(extnum+1,urlstr.length());//地图格式
        //输出后的地图地址
        String printMap = null;
        try{
            //输出地图的路径
            System.out.println("进入printMap");
            String realPath = savepath + "\\"+ "printMap" +"." + ext;
            System.out.println(realPath);
            URL url = new URL(urlstr);
            System.out.println(urlstr);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("referer",u);//通过这个http头的伪装来反盗链

            BufferedImage baseImage = ImageIO.read(connection.getInputStream());
            System.out.println(baseImage.getWidth());
            FileOutputStream fout = new FileOutputStream(realPath);
            switch (dpi){
                case "300":ImageIO.write(Export300dpi(baseImage,savepath,printLegendFlag,layoutID),ext,fout);System.out.println("300dpi");
                    break;
                case "200": ImageIO.write(Export200dpi(baseImage,savepath,printLegendFlag,layoutID),ext,fout);System.out.println("200dpi");
                    break;
                case "120": ImageIO.write(Export120dpi(baseImage,savepath,printLegendFlag,layoutID),ext,fout);System.out.println("120dpi");
                    break;
                default:
                    break;
            }
            fout.flush();
            fout.close();
//            printMap = "http://223.75.52.36:28090/geoInfoHB/printMap/"+ ip +"/printMap"+"." + ext;//部署用
            printMap = strBackUrl + "/printMap/"+ ip +"/printMap"+"." + ext;//本机测试用

            System.out.println(printMap);
        }
        catch(Exception e)
        {
            System.out.print(e.getMessage().toString());
        }
        return printMap;
    }

    public static double getTitleSize(Graphics2D g2d, Font font, String text) {
        // 设置大字体
        FontRenderContext context = g2d.getFontRenderContext();
        // 获取字体的像素范围对象
        Rectangle2D stringBounds = font.getStringBounds(text, context);
        double fontWidth = stringBounds.getWidth();
        return fontWidth;
    }

    public static BufferedImage Export300dpi(BufferedImage baseImage,String savepath,String printLegendFlag,String layoutID){
        int layoutX;
        int layoutY;
        String layoutType = layoutID.split("_")[0];
        String layoutClass = layoutID.split("_")[1];
        String layoutPaper = layoutID.split("_")[2];
        try{
            if(layoutPaper.equals("A4")){
                //如果是横版
                if(layoutType.equals("Landscape")){
                    if(layoutClass.equals("01")){
                        layoutX = 180;
                        layoutY = 150;
                    }
                    else {
                        layoutX = 180;
                        layoutY = 200;
                    }
                }
                //如果是竖版
                else {
                    if(layoutClass.equals("01")){
                        layoutX = 130;
                        layoutY = 150;
                    }
                    else {
                        layoutX = 130;
                        layoutY = 200;
                    }
                }
            }
            else {
                //如果是横版
                if(layoutType.equals("Landscape")){
                    if(layoutClass.equals("01")){
                        layoutX = 300;
                        layoutY = 200;
                    }
                    else {
                        layoutX = 180;
                        layoutY = 280;
                    }
                }
                //如果是竖版
                else {
                    if(layoutClass.equals("01")){
                        layoutX = 230;
                        layoutY = 200;
                    }
                    else {
                        layoutX = 180;
                        layoutY = 280;
                    }
                }
            }
            Graphics2D g2d = baseImage.createGraphics();
            int mapWidth = baseImage.getWidth();
            int mapHeight = baseImage.getHeight();
            int positionX = mapWidth - layoutX;
            int positionY = mapHeight - layoutY;
            String chartLegendPath = savepath + "\\"+ "chartLegend.png";
            String classLegendPath = savepath + "\\"+ "classLegend.png";
            draw2LegendV(g2d,chartLegendPath,classLegendPath,printLegendFlag,positionX,positionY);
        }catch(Exception e)
        {
            System.out.print(e.getMessage().toString());
        }
        return baseImage;
    }


    public static BufferedImage Export200dpi(BufferedImage baseImage,String savepath,String printLegendFlag,String layoutID){
        String layoutType = layoutID.split("_")[0];
        String layoutClass = layoutID.split("_")[1];
        String layoutPaper = layoutID.split("_")[2];
        int mapWidth = baseImage.getWidth();
        int mapHeight = baseImage.getHeight();
        int positionX;
        int positionY;
        Graphics2D g2d = baseImage.createGraphics();
        String chartLegendPath = savepath + "\\"+ "chartLegend.png";
        String classLegendPath = savepath + "\\"+ "classLegend.png";
        try{
            if(layoutPaper.equals("A4")){
                //如果是横版
                if(layoutType.equals("Landscape")){
                    if(layoutClass.equals("01")){
                        positionX = mapWidth-100;
                        positionY = mapHeight-90;
                    }
                    else {
                        positionX = mapWidth-100;
                        positionY = mapHeight-120;
                    }
                    draw2LegendH(g2d,chartLegendPath,classLegendPath,printLegendFlag,positionX,positionY);
                }
                //如果是竖版
                else {
                    if(layoutClass.equals("01")){
                        positionX = mapWidth-90;
                        positionY = mapHeight-100;
                    }
                    else {
                        positionX = mapWidth-80;
                        positionY = mapHeight-120;
                    }
                    draw2LegendV(g2d,chartLegendPath,classLegendPath,printLegendFlag,positionX,positionY);
                }
            }
            else {
                //如果是横版
                if(layoutType.equals("Landscape")){
                    if(layoutClass.equals("01")){
                        positionX = mapWidth-180;
                        positionY = mapHeight-110;
                    }
                    else {
                        positionX = mapWidth-110;
                        positionY = mapHeight-170;
                    }
                    draw2LegendV(g2d,chartLegendPath,classLegendPath,printLegendFlag,positionX,positionY);
                }
                //如果是竖版
                else {
                    if(layoutClass.equals("01")){
                        positionX = mapWidth-150;
                        positionY = mapHeight-120;
                    }
                    else {
                        positionX = mapWidth-110;
                        positionY = mapHeight-180;
                    }
                    draw2LegendV(g2d,chartLegendPath,classLegendPath,printLegendFlag,positionX,positionY);
                }
            }
        }catch(Exception e)
        {
            System.out.print(e.getMessage().toString());
        }
        return baseImage;
    }

    public static BufferedImage Export120dpi(BufferedImage baseImage,String savepath,String printLegendFlag,String layoutID){
        String layoutType = layoutID.split("_")[0];
        String layoutClass = layoutID.split("_")[1];
        String layoutPaper = layoutID.split("_")[2];
        int mapWidth = baseImage.getWidth();
        int mapHeight = baseImage.getHeight();
        int positionX1;
        int positionY1;
        int positionX2;
        int positionY2;
        Graphics2D g2d = baseImage.createGraphics();
        String chartLegendPath = savepath + "\\"+ "chartLegend.png";
        String classLegendPath = savepath + "\\"+ "classLegend.png";
        try {
            if(layoutPaper.equals("A4")){
                //如果是横版
                if(layoutType.equals("Landscape")){
                    //右上角统计图 + 右下角分级图
                    if(layoutClass.equals("01")){
                        positionX1 = mapWidth-70;
                        positionY1 = mapHeight-50;
                        positionX2 = mapWidth-50;
                        positionY2 = 35;

                        switch (printLegendFlag){
                            case "chart":{
                                System.out.println("输出统计符号图例");
                                BufferedImage chartImage = ImageIO.read(new FileInputStream(chartLegendPath));
                                //把图例绘制到bufferedImage上
                                g2d.drawImage(chartImage,positionX1-chartImage.getWidth(),positionY1-chartImage.getHeight(),
                                        chartImage.getWidth(),chartImage.getHeight(),null);
                            }
                            break;
                            case "class":{
                                System.out.println("输出分级符号图例");
                                BufferedImage classImage = ImageIO.read(new FileInputStream(classLegendPath));
                                //把图例绘制到bufferedImage上
                                g2d.drawImage(classImage,positionX1-classImage.getWidth(),positionY1-classImage.getHeight(),
                                        classImage.getWidth(),classImage.getHeight(),null);
                            }
                            break;
                            case "both": {
                                System.out.println("输出两个图例");
                                BufferedImage chartImage = ImageIO.read(new FileInputStream(chartLegendPath));
                                BufferedImage classImage = ImageIO.read(new FileInputStream(classLegendPath));
                                //绘制分级图图例(右下角)
                                g2d.drawImage(classImage, positionX1 - classImage.getWidth(), positionY1 - classImage.getHeight(),
                                        classImage.getWidth(), classImage.getHeight(), null);

                                //绘制统计图图例(右上角)
                                g2d.drawImage(chartImage, positionX2 - chartImage.getWidth(), positionY2,
                                        chartImage.getWidth(), chartImage.getHeight(), null);
                            }
                            break;
                            default:break;
                        }
                        g2d.dispose();
                    }
                    else {//左上角统计图 + 右下角分级图
                        positionX1 = mapWidth-70;
                        positionY1 = mapHeight-80;
                        positionX2 = 60;
                        positionY2 = 80;
                        switch (printLegendFlag){
                            case "chart":{
                                System.out.println("输出统计符号图例");
                                BufferedImage chartImage = ImageIO.read(new FileInputStream(chartLegendPath));
                                //把图例绘制到bufferedImage上
                                g2d.drawImage(chartImage,positionX1-chartImage.getWidth(),positionY1-chartImage.getHeight(),
                                        chartImage.getWidth(),chartImage.getHeight(),null);
                            }
                            break;
                            case "class":{
                                System.out.println("输出分级符号图例");
                                BufferedImage classImage = ImageIO.read(new FileInputStream(classLegendPath));
                                //把图例绘制到bufferedImage上
                                g2d.drawImage(classImage,positionX1-classImage.getWidth(),positionY1-classImage.getHeight(),
                                        classImage.getWidth(),classImage.getHeight(),null);
                            }
                            break;
                            case "both": {
                                System.out.println("输出两个图例");
                                BufferedImage chartImage = ImageIO.read(new FileInputStream(chartLegendPath));
                                BufferedImage classImage = ImageIO.read(new FileInputStream(classLegendPath));
                                //绘制分级图图例(右下角)
                                g2d.drawImage(classImage, positionX1 - classImage.getWidth(), positionY1 - classImage.getHeight(),
                                        classImage.getWidth(), classImage.getHeight(), null);

                                //绘制统计图图例(左上角)
                                g2d.drawImage(chartImage, positionX2, positionY2,
                                        chartImage.getWidth(), chartImage.getHeight(), null);
                            }
                            break;
                            default:break;
                        }
                        g2d.dispose();
                    }
                }
                //如果是竖版
                else {
                    //两个图例垂直排列
                    if (layoutClass.equals("01")) {
                        positionX1 = mapWidth - 40;
                        positionY1 = mapHeight - 70;
                    } else {
                        positionX1 = mapWidth - 40;
                        positionY1 = mapHeight - 70;
                    }
                    draw2LegendH(g2d,chartLegendPath,classLegendPath,printLegendFlag,positionX1,positionY1);
                }
            }
            else {
                //如果是横版
                if(layoutType.equals("Landscape")){
                    //两个图例横着排列
                    if(layoutClass.equals("01")){
                        positionX1 = mapWidth-90;
                        positionY1 = mapHeight-70;
                    }
                    else {
                        positionX1 = mapWidth-60;
                        positionY1 = mapHeight-100;
                    }
                    draw2LegendH(g2d,chartLegendPath,classLegendPath,printLegendFlag,positionX1,positionY1);
                }
                //如果是竖版
                else {
                    //两个图例竖着排列
                    if(layoutClass.equals("01")){
                        positionX1 = mapWidth-90;
                        positionY1 = mapHeight-80;
                    }
                    else {
                        positionX1 = mapWidth-70;
                        positionY1 = mapHeight-120;
                    }
                    draw2LegendV(g2d,chartLegendPath,classLegendPath,printLegendFlag,positionX1,positionY1);
                }
            }
        }catch(Exception e)
        {
            System.out.print(e.getMessage().toString());
        }
        return baseImage;
    }

    public static void draw2LegendV(Graphics2D g2d,String chartLegendPath,String classLegendPath,String printLegendFlag,int positionX,int positionY){
        try{
            switch (printLegendFlag){
                case "chart":{
                    System.out.println("输出统计符号图例");
                    BufferedImage chartImage = ImageIO.read(new FileInputStream(chartLegendPath));
                    //把图例绘制到bufferedImage上
                    g2d.drawImage(chartImage,positionX-chartImage.getWidth(),positionY-chartImage.getHeight(),
                            chartImage.getWidth(),chartImage.getHeight(),null);
                }
                break;
                case "class":{
                    System.out.println("输出分级符号图例");
                    BufferedImage classImage = ImageIO.read(new FileInputStream(classLegendPath));
                    //把图例绘制到bufferedImage上
                    g2d.drawImage(classImage,positionX-classImage.getWidth(),positionY-classImage.getHeight(),
                            classImage.getWidth(),classImage.getHeight(),null);
                }
                break;
                case "both": {
                    System.out.println("输出两个图例");
                    BufferedImage chartImage = ImageIO.read(new FileInputStream(chartLegendPath));
                    BufferedImage classImage = ImageIO.read(new FileInputStream(classLegendPath));
                    int maxWidth;
                    int maxHeight = classImage.getHeight() + chartImage.getHeight() + 20;
                    if (classImage.getWidth() < chartImage.getWidth()) {
                        maxWidth = chartImage.getWidth();
                        //把图例绘制到bufferedImage上
                        g2d.drawImage(classImage, positionX - classImage.getWidth(), positionY - classImage.getHeight(),
                                classImage.getWidth(), classImage.getHeight(), null);
                        int deviation = (chartImage.getWidth() - classImage.getWidth()) / 2;
                        g2d.drawImage(chartImage, positionX - classImage.getWidth() - deviation,
                                positionY - classImage.getHeight() - chartImage.getHeight() - 20,
                                chartImage.getWidth(), chartImage.getHeight(), null);
                    } else {
                        maxWidth = classImage.getWidth();
                        //把图例绘制到bufferedImage上
                        g2d.drawImage(classImage, positionX - classImage.getWidth(), positionY - classImage.getHeight(),
                                classImage.getWidth(), classImage.getHeight(), null);
                        int deviation = (classImage.getWidth() - chartImage.getWidth()) / 2;
                        g2d.drawImage(chartImage, positionX - classImage.getWidth() + deviation,
                                positionY - classImage.getHeight() - chartImage.getHeight() - 20,
                                chartImage.getWidth(), chartImage.getHeight(), null);
                    }
                }
                break;
                default:break;
            }
            g2d.dispose();
        }
        catch(Exception e)
        {
            System.out.print(e.getMessage().toString());
        }
    }

    public static void draw2LegendH(Graphics2D g2d,String chartLegendPath,String classLegendPath,String printLegendFlag,int positionX,int positionY){
        try{
            switch (printLegendFlag){
                case "chart":{
                    System.out.println("输出统计符号图例");
                    BufferedImage chartImage = ImageIO.read(new FileInputStream(chartLegendPath));
                    //把图例绘制到bufferedImage上
                    g2d.drawImage(chartImage,positionX-chartImage.getWidth(),positionY-chartImage.getHeight(),
                            chartImage.getWidth(),chartImage.getHeight(),null);
                }
                    break;
                case "class":{
                    System.out.println("输出分级符号图例");
                    BufferedImage classImage = ImageIO.read(new FileInputStream(classLegendPath));
                    //把图例绘制到bufferedImage上
                    g2d.drawImage(classImage,positionX-classImage.getWidth(),positionY-classImage.getHeight(),
                            classImage.getWidth(),classImage.getHeight(),null);
                }
                    break;
                case "both": {
                    System.out.println("输出两个图例");
                    BufferedImage chartImage = ImageIO.read(new FileInputStream(chartLegendPath));
                    BufferedImage classImage = ImageIO.read(new FileInputStream(classLegendPath));
                    //先画统计图图例
                    g2d.drawImage(chartImage, positionX - chartImage.getWidth(), positionY - chartImage.getHeight(),
                            chartImage.getWidth(), chartImage.getHeight(), null);

                    //再画分级图图例
                    g2d.drawImage(classImage, positionX - chartImage.getWidth()-classImage.getWidth(),
                            positionY - classImage.getHeight(), classImage.getWidth(), classImage.getHeight(), null);
                }
                    break;
                default:break;
            }
            g2d.dispose();
        }
        catch(Exception e)
        {
            System.out.print(e.getMessage().toString());
        }
    }
}
