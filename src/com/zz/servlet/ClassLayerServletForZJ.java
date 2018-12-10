package com.zz.servlet;

import com.zz.chart.data.ClassData;
import com.zz.util.NetworkUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import telecarto.data.util.ColorUtil;
import telecarto.geoinfo.db.MysqlAccessBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

@WebServlet(name = "ClassLayerServletForZJ")
public class ClassLayerServletForZJ extends HttpServlet {

    public ClassLayerServletForZJ() {
        super();
    }/**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet( request,  response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/javascript;charset=UTF-8");//返回json格式的数据
        request.setCharacterEncoding("UTF-8");//设置服务器端对前端传输数据的解码方式!!!

        String ip = NetworkUtil.getIpAddr(request);
        String layerName=request.getParameter("name");
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
        //String chartID=cartographydataJson.getString("chartID");
        //JSONArray colorArray=cartographydataJson.getJSONArray("colors");
        int classNum=cartographydataJson.getInt("classNumSliderValue");
        String  modelName=cartographydataJson.getString("modelName");
        String colorInit=cartographydataJson.getString("color1");
        String colorEnd=cartographydataJson.getString("color2");


        MysqlAccessBean mysql = new MysqlAccessBean();
        String sql_select;
        String sql;
        ResultSet resultSet2;


        String regionClass="1";
        //根据输入行政等级class，确立
        sql="SELECT * FROM 	region_info WHERE	class ="+regionClass;

        //sql_select = "LEFT JOIN "+ tableName +" t2 ON t1.RGN_CODE = t2.RGN_CODE WHERE t1.RGN_CODE LIKE '"+Param+"' AND t1.RGN_CODE!= '"+regionParam+"' AND t2.YEAR = '" + year + "'";
        //sql_select = "LEFT JOIN "+ tableName +" t2 ON t1.RGN_CODE = t2.RGN_CODE WHERE t1.RGN_CLASS = '" + regionParam + "' AND t2.YEAR = '" + year + "'";
        //sql = "SELECT t1.RGN_CODE,t1.RGN_NAME,t1.GEOMETRY,t1.REGION_X,t1.REGION_Y,t2."+ fieldName +" FROM region t1 " + sql_select;
       // sql = "SELECT t1.RGN_CODE,t1.RGN_NAME,t1.GEOMETRY,t1.REGION_X,t1.REGION_Y,t2."+ fieldName +" FROM region t1 " + sql_select;
        sql="";
        try {
            resultSet2 = mysql.query(sql);
            ArrayList<ClassData> classList = new ArrayList<>();
//            while (resultSet2.next()) {
//                ClassData classData = new ClassData(resultSet2.getString(1),
//                        resultSet2.getString(3),resultSet2.getString(4),resultSet2.getString(5),resultSet2.getString(6));
//                classList.add(classData);
//            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            mysql.close();
        }



    }
}
