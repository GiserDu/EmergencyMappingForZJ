package telecarto.geoinfo.servlets;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import telecarto.geoinfo.db.MysqlAccessBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet(name = "GetAdministrativeRegion")
public class GetAdministrativeRegion extends HttpServlet {

//从服务接口获取json，主要为了从地理空间数据云中爬取数据
        public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }
                String jsonText = sb.toString();
                JSONObject json = JSONObject.fromObject(jsonText);
                return json;
            } finally {
                is.close();
            }
        }

    /**
     * JSON字符串特殊字符处理，比如：“\A1;1300”
     * @param s
     * @return String
     */
    public static String stringToJson(String s,boolean isJSjson){
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<s.length(); i++){
            char c =s.charAt(i);
            switch(c){
                case'\'': if(isJSjson) {sb.append("\\\'");}else{sb.append("\'");} break;
                case'\"': if(!isJSjson) {sb.append("\\\"");}else{sb.append("\"");} break;
                case'\\':sb.append("\\\\"); break; //如果不处理单引号，可以释放此段代码，若结合StringDanYinToJSON()处理单引号就必须注释掉该段代码
                case'/': sb.append("\\/");break;
                case'\b':sb.append("\\b");break;//退格
                case'\f':sb.append("\\f");break;//走纸换页
                case'\n':sb.append("\\n");break;//换行
                case'\r':sb.append("\\r");break;//回车
                case'\t':sb.append("\\t");break;//横向跳格
                default: sb.append(c);
            }}
        return sb.toString();
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        String type=request.getParameter("type");
        String regionName="";
        String regionCode="";
        System.out.println("*11311111");
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
                            sql = "SELECT gson from regionboundarys_compress WHERE  citycode = '' and coutcode ='' and name='";
                        }
                        else if(code.equals("cityCode")){
                            sql = "SELECT gson from regionboundarys_compress WHERE  coutcode ='' and citycode !='' and name='";
                        }
                        else if(code.equals("coutCode")){
                            sql = "SELECT gson from regionboundarys_compress WHERE coutcode !='' and name='";
                        }
                        sql=sql+selectedRegion[i]+"'";

                       /* if(code.equals("proCode")){
                            sql = "SELECT coutcode from regionboundarys_copy WHERE coutcode !=''";
                        }*/

                        resultSet = mysql.query(sql);
                        while (resultSet.next()) {
                            /*String currentCode=resultSet.getString(1);
                            JSONObject jsons= readJsonFromUrl("http://www.gscloud.cn/statenames/coutjson/"+currentCode);
                            String updateSQL="update regionboundarys_copy set gson='"+jsons+"' where  coutcode ='"+currentCode+"'";
                            mysql.update(updateSQL);*/
                            regionName=resultSet.getString(1);
                            regionName=stringToJson(regionName,true);
                        }
                    }
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
                    String sql = "SELECT name,procode from regionboundarys_compress WHERE citycode = '' and coutcode =''";
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
                    String sql = "SELECT name,citycode from regionboundarys_compress WHERE procode = '"+proCode+"' and coutcode ='' and citycode !=''";
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
                    String sql = "SELECT name,coutcode from regionboundarys_compress WHERE procode = '"+proCode+"' and cityCode ='"+cityCode+"' and coutcode !=''";
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
