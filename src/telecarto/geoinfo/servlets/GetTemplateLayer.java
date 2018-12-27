//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package telecarto.geoinfo.servlets;

import com.zz.util.JUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import telecarto.geoinfo.db.MysqlAccessBean;

@WebServlet(
        name = "GetTemplateLayer"
)
public class GetTemplateLayer extends HttpServlet {
    public GetTemplateLayer() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        System.out.println("112");

        try {

            String disasterType = request.getParameter("disasterType");
            System.out.println(disasterType);
            String disasterStatus = request.getParameter("disasterStatus").toString();
            System.out.println(disasterStatus);
            String templateScale = request.getParameter("templateScale").toString();
            System.out.println(templateScale);
            String templateTheme = request.getParameter("templateTheme").toString();
            System.out.println(templateTheme);
            String templateMap = request.getParameter("templateMap").toString();
            System.out.println(templateMap);
            String queryType = request.getParameter("queryType").toString();
            System.out.println(queryType);
            String userMapId = request.getParameter("mapId").toString();
            System.out.println(userMapId);
            String disasterTable = "";
            byte var8 = -1;
            switch(disasterType.hashCode()) {
                case -2123919667:
                    if (disasterType.equals("earthquake")) {
                        var8 = 5;
                    }
                    break;
                case -79018874:
                    if (disasterType.equals("geology")) {
                        var8 = 4;
                    }
                    break;
                case 3143222:
                    if (disasterType.equals("fire")) {
                        var8 = 1;
                    }
                    break;
                case 97526782:
                    if (disasterType.equals("flood")) {
                        var8 = 2;
                    }
                    break;
                case 99469088:
                    if (disasterType.equals("house")) {
                        var8 = 3;
                    }
                    break;
                case 1052964649:
                    if (disasterType.equals("transport")) {
                        var8 = 0;
                    }
            }

            switch(var8) {
                case 0:
                    disasterType = "db_transport";
                    break;
                case 1:
                    disasterType = "db_fire";
                    break;
                case 2:
                    disasterType = "db_flood";
                    break;
                case 3:
                    disasterType = "db_house";
                    break;
                case 4:
                    disasterType = "db_geology";
                    break;
                case 5:
                    disasterType = "db_earthquake";
            }

            MysqlAccessBean mysql = null;
            String configpath = JUtil.GetWebInfPath() + "/prop/dbconpara.properties";
            InputStream ips = new FileInputStream(configpath);
            Properties pro = new Properties();
            pro.load(ips);
            disasterType = pro.getProperty(disasterType);
            mysql = new MysqlAccessBean();
            String sql = "";
            if (queryType.equals("queryLayer")) {
                sql ="SELECT SIX_LZJTU_LAYER FROM "+disasterType+" WHERE SIX_LZJTU_STATUS ='"+disasterStatus+"' AND SIX_LZJTU_MAP = '"+templateMap+"' AND  SIX_LZJTU_SCALE = '"+templateScale+"' AND SIX_LZJTU_MAPGROUP = '"+templateTheme+"'";
            }
            else if (queryType.equals("userMap")){
                sql = "SELECT map_name, map_param FROM user_map WHERE map_id =" + userMapId;
            }
            else {
                String disasterGroup = request.getParameter("disasterGroup").toString();
                sql = "SELECT SIX_LZJTU_MAP,SIX_LZJTU_SCALE,SIX_LZJTU_MAPLOC FROM " + disasterType + " where SIX_LZJTU_STATUS='" + disasterStatus + "' and SIX_LZJTU_MAPGROUP='" + disasterGroup + "' ORDER BY SIX_LZJTU_MAP";
            }

            System.out.println(sql);
            ResultSet resultSet = mysql.query(sql);

            JSONObject mapObject;
            JSONArray mapArray;
            for(mapArray = new JSONArray(); resultSet.next(); mapArray.add(mapObject)) {
                mapObject = new JSONObject();
                if (queryType.equals("queryLayer")) {
                    String layercontentstr= resultSet.getString(1);
                    layercontentstr=layercontentstr.replace("，", ",");
                    mapObject.put("SIX_LZJTU_LAYER", layercontentstr);
                }
                else if (queryType.equals("userMap")){
                    String layerContentStr = resultSet.getString(2);
                    layerContentStr = layerContentStr.replace("，", ",");
                    mapObject.put("userMapName", resultSet.getString(1));
                    mapObject.put("userMapParam", layerContentStr);
                }
                else {
                    mapObject.put("SIX_LZJTU_MAP", resultSet.getString(1));
                    mapObject.put("SIX_LZJTU_SCALE", resultSet.getString(2));
                    mapObject.put("SIX_LZJTU_MAPLOC", resultSet.getString(3));
                }
            }

            PrintWriter out = response.getWriter();
            out.println(mapArray.toString());
            out.flush();
            out.close();
        } catch (Exception var15) {
            PrintWriter out = response.getWriter();
            out.println("error:" + var15.toString());
            out.flush();
            out.close();
            System.out.print(var15.toString());
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        this.doPost(request, response);
    }
}
