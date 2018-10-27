package telecarto.geoinfo.servlets;

import com.zz.util.JUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import telecarto.geoinfo.db.MysqlAccessBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;

@WebServlet(name = "GetTemplateContent")
public class GetTemplateContent extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        try
        {
            String disasterType=request.getParameter("disasterType");
            System.out.println(disasterType);
            String disasterStatus=request.getParameter("disasterStatus").toString();
            System.out.println(disasterStatus);
            String  queryType=request.getParameter("queryType").toString();
            System.out.println(queryType);
            String disasterTable="";
            switch(disasterType){
                case "transport":
                    disasterType="db_transport";
                    break;
                case "fire":
                    disasterType="db_fire";
                    break;
                case "flood":
                    disasterType="db_flood";
                    break;
                case "house":
                    disasterType="db_house";
                    break;
                case "geology":
                    disasterType="db_geology";
                    break;
                case "earthquake":
                    disasterType="db_earthquake";
                    break;
            }
            MysqlAccessBean mysql = null;
            ResultSet resultSet;
            String configpath = JUtil.GetWebInfPath()+"/prop/dbconpara.properties";
            InputStream ips = new FileInputStream(configpath);
            Properties pro = new Properties();
            pro.load(ips);
            disasterType = pro.getProperty(disasterType);
            mysql = new MysqlAccessBean();
            String sql = "";
            if(queryType.equals("queryTheme")){
                sql = "SELECT distinct SIX_LZJTU_MAPGROUP FROM "+disasterType+" where SIX_LZJTU_STATUS='"+disasterStatus+"'";
            }
            else if(queryType.equals("queryMaps")){
                String disasterGroup=request.getParameter("disasterGroup").toString();
                sql = "SELECT SIX_LZJTU_MAP,SIX_LZJTU_SCALE,SIX_LZJTU_MAPLOC FROM "+disasterType+" where SIX_LZJTU_STATUS='"+disasterStatus+"' and SIX_LZJTU_MAPGROUP='"+disasterGroup+"' ORDER BY SIX_LZJTU_MAP";
            }
            else{
                String disasterName = request.getParameter("disasterName").toString();
                sql = "SELECT SIX_LZJTU_MAP,SIX_LZJTU_SCALE,SIX_LZJTU_MAPLOC FROM "+disasterType+" where SIX_LZJTU_STATUS='"+disasterStatus+"' and SIX_LZJTU_MAP LIKE '%"+disasterName+"%' ORDER BY SIX_LZJTU_MAP";
            }
            System.out.println(sql);
            resultSet = mysql.query(sql);
            JSONArray mapArray = new JSONArray();
            while (resultSet.next()) {
                JSONObject mapObject = new JSONObject();
                if(queryType.equals("queryTheme")){
                    mapObject.put("SIX_LZJTU_MAPGROUP",resultSet.getString(1));
                }
                else {
                    mapObject.put("SIX_LZJTU_MAP",resultSet.getString(1));
                    mapObject.put("SIX_LZJTU_SCALE",resultSet.getString(2));
                    mapObject.put("SIX_LZJTU_MAPLOC",resultSet.getString(3));

                }
                mapArray.add(mapObject);
            }
            PrintWriter out = response.getWriter();
            out.println(mapArray.toString());

            out.flush();
            out.close();
        }
        catch(Exception e)
        {
            PrintWriter out = response.getWriter();
            out.println("error:"+e.toString());
            out.flush();
            out.close();
            System.out.print(e.toString());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        this.doPost(request,response);
    }
}
