package telecarto.geoinfo.servlets;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import telecarto.geoinfo.db.MysqlAccessBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "GetAdministrativeRegion")
public class GetAdministrativeRegion extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String type=request.getParameter("type");
        String regionName="";
        String regionCode="";
        MysqlAccessBean mysql = null;
        ResultSet resultSet = null;
        String proCode="";
        String cityCode="";
        String [] selectedRegion;
        switch (type){
            case "boundary":
                try {
                    String code=request.getParameter("selectedRegion").split("s-p-l")[0].trim();
                    selectedRegion=request.getParameter("selectedRegion").split("s-p-l")[1].trim().split(",");
                    mysql = new MysqlAccessBean();
                    String sql = "";
                    JSONArray jsonarray = new JSONArray();
                    for(int i =0;i<selectedRegion.length;i++){
                        if(code.equals("proCode")){
                            sql = "SELECT gson from regionboundary1 WHERE  citycode = '' and coutcode ='' and "+code+"='";
                        }
                        else if(code.equals("cityCode")){
                            sql = "SELECT gson from regionboundary1 WHERE  coutcode ='' and citycode !='' and "+code+"='";
                        }
                        else if(code.equals("coutCode")){
                            sql = "SELECT gson from regionboundary1 WHERE coutcode !='' and "+code+"='";
                        }
                        sql=sql+selectedRegion[i]+"'";
                        resultSet = mysql.query(sql);
                        while (resultSet.next()) {
                            regionName=resultSet.getString(1);
                            /*switch (x = x.replaceAll("\\\\", "@@@@@")) {
                            }*/

                            /*JSONObject json=JSONObject.fromObject(x);
                            jsonarray.add(json);*/
                            //regionName=regionName+resultSet.getString(1)+"s-p-ll";

                        }
                    }

                    // regionName=regionName.substring(0,regionName.length()-6).trim();
                    PrintWriter out = response.getWriter();

                    out.println(regionName);

                    out.flush();
                    out.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "pro":
                try {
                    mysql = new MysqlAccessBean();
                    String sql = "SELECT name,procode from regionboundary1 WHERE citycode = '' and coutcode =''";
                    resultSet = mysql.query(sql);
                    while (resultSet.next()) {
                        regionName=regionName+resultSet.getString(1)+",";
                        regionCode=regionCode+resultSet.getString(2)+",";
                    }
                    regionName=regionName.substring(0,regionName.length()-1);
                    regionCode=regionCode.substring(0,regionCode.length()-1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "city":
                proCode=request.getParameter("proCode");
                try {
                    mysql = new MysqlAccessBean();
                    String sql = "SELECT name,citycode from regionboundary1 WHERE procode = '"+proCode+"' and coutcode ='' and citycode !=''";
                    resultSet = mysql.query(sql);
                    while (resultSet.next()) {
                        regionName=regionName+resultSet.getString(1)+",";
                        regionCode=regionCode+resultSet.getString(2)+",";
                    }
                    regionName=regionName.substring(0,regionName.length()-1);
                    regionCode=regionCode.substring(0,regionCode.length()-1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case "cout":
                proCode=request.getParameter("proCode");
                cityCode=request.getParameter("cityCode");
                try {
                    mysql = new MysqlAccessBean();
                    String sql = "SELECT name,coutcode from regionboundary1 WHERE procode = '"+proCode+"' and cityCode ='"+cityCode+"' and coutcode !=''";
                    resultSet = mysql.query(sql);
                    while (resultSet.next()) {
                        regionName=regionName+resultSet.getString(1)+",";
                        regionCode=regionCode+resultSet.getString(2)+",";
                    }
                    regionName=regionName.substring(0,regionName.length()-1);
                    regionCode=regionCode.substring(1,regionCode.length()-1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
        PrintWriter out = response.getWriter();

        out.println(regionName+"s-p-l"+regionCode);

        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }
}
