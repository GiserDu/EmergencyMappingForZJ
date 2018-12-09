package com.zz.servlet;

import com.zz.chart.data.ClassData;
import com.zz.util.Classifiter;
import com.zz.util.JUtil;
import com.zz.util.NetworkUtil;
import com.zz.util.imageUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.awt.SunHints;
import sun.misc.BASE64Encoder;
import telecarto.data.util.ChEnConverter;
import telecarto.geoinfo.db.MysqlAccessBean;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/10/29.
 */
//@WebServlet(name = "ClassLayerServlet")
public class ClassLayerServlet extends HttpServlet {
    public ClassLayerServlet() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
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

        //获取客户端的ip地址
        String ip = NetworkUtil.getIpAddr(request);

        //解析传输过来的各种数据
        //colors = [rgb(r,g,b);rgb(r,g,b),...]颜色从起始到结束
        String color = request.getParameter("colors");
        String[] colors = color.trim().split(";");
//        String endColor = request.getParameter("endColor");
        String fieldName = request.getParameter("field");
        String fieldNameCN = request.getParameter("field_cn");
        String table = request.getParameter("table");//中文表名
        String tableName = ChEnConverter.convertTableNameCH2En(table);
        int breakNum = Integer.parseInt(request.getParameter("breakNum"));

        String breakMethod = request.getParameter("breakMethod");
        String regionParam = request.getParameter("regionParam");
        String year = request.getParameter("year");

        String cnNameAndUnit = JUtil.getCnNameAndUnit(fieldName);
        String dataSource = cnNameAndUnit.split(",")[2];

        MysqlAccessBean mysql = null;
//        ResultSet resultSet1 = null;
        ResultSet resultSet2;
        String sql_select;
//        String sql_table;
        String sql;
        try {
            mysql = new MysqlAccessBean();

            if(regionParam.equals("1")){
                sql_select = "LEFT JOIN "+ tableName +" t2 ON t1.RGN_CODE = t2.RGN_CODE WHERE t1.RGN_CLASS = '" + regionParam + "' AND t2.YEAR = '" + year + "'";
                sql = "SELECT t1.RGN_CODE,t1.RGN_NAME,t1.GEOMETRY,t1.REGION_X,t1.REGION_Y,t2."+ fieldName +" FROM region t1 " + sql_select;
            }
            else {
                //String Param = regionParam.substring(0, 4) + "__";
                //sql_select = "LEFT JOIN "+ tableName +" t2 ON t1.RGN_CODE = t2.RGN_CODE WHERE t1.RGN_CODE LIKE '"+Param+"' AND t1.RGN_CODE!= '"+regionParam+"' AND t2.YEAR = '" + year + "'";
                sql_select = "LEFT JOIN "+ tableName +" t2 ON t1.RGN_CODE = t2.RGN_CODE WHERE t1.RGN_CLASS = '" + regionParam + "' AND t2.YEAR = '" + year + "'";
                sql = "SELECT t1.RGN_CODE,t1.RGN_NAME,t1.GEOMETRY,t1.REGION_X,t1.REGION_Y,t2."+ fieldName +" FROM region t1 " + sql_select;
            }
            resultSet2 = mysql.query(sql);
            ArrayList<ClassData> classList = new ArrayList<>();


//            while (resultSet2.next()) {
//                ClassData classData = new ClassData(resultSet2.getString(1),
//                        resultSet2.getString(3),resultSet2.getString(4),resultSet2.getString(5),resultSet2.getString(6));
//                classList.add(classData);
//			}
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
            int width = 165;
            int height = 21 * breakNum+45;
            BufferedImage image = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
//            image = g2d.getDeviceConfiguration().createCompatibleImage(width,
//                    height + 30, Transparency.TRANSLUCENT);
            g2d.setColor(Color.white);
//            g2d.setColor(new Color(247,247,247));
            g2d.fillRect(0,0,image.getWidth(),image.getHeight());
            g2d.dispose();

            g2d = image.createGraphics();
            //文字抗锯齿化处理
            g2d.setRenderingHint(SunHints.KEY_ANTIALIASING, SunHints.VALUE_ANTIALIAS_ON);

            Font font = new Font("黑体", Font.PLAIN, 17);
            g2d.setFont(font);
            g2d.setColor(Color.black);

            // 获取图例标题的像素范围对象
            double fontWidth = imageUtil.getTitleSize(g2d,font,fieldNameCN);
            int stringWidth = new BigDecimal(fontWidth).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            int startX = new BigDecimal((165.0 - stringWidth)/2).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            if(startX>0){
                g2d.drawString(fieldNameCN, startX, 25);
            }
            else {
                g2d.setFont(new Font("黑体", Font.PLAIN, 16));
                fontWidth = imageUtil.getTitleSize(g2d,font,fieldNameCN);
                stringWidth = new BigDecimal(fontWidth).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                startX = new BigDecimal((165 - stringWidth)/2).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                g2d.drawString(fieldNameCN, startX, 25);
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
            ImageIO.write(image, "png", baosLegend);
            byte[] bytesLegend = baosLegend.toByteArray();
            String imgStreamLegend = encoderLegend.encodeBuffer(bytesLegend).trim();
            //输出分级图图例到本地
            String tempFilePath = getServletContext().getRealPath("/") + "printMap/" + ip.replaceAll("\\.","-");
            File fileSavepath = new File(tempFilePath);
            if(!fileSavepath.exists()){
                fileSavepath.mkdir();
            }
            String imgPath = tempFilePath + "/"+"classLegend.png";
            ImageIO.write(image, "png", new File(imgPath));

            //传输JSON分级grapgics数组到前端
            classObject.put("classDataArray",classDataArray);
            classObject.put("dataSource",dataSource);
            classObject.put("classLegend",imgStreamLegend.replaceAll("[\\s*\t\n\r]", ""));
            PrintWriter out = response.getWriter();
            out.println(classObject);
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
