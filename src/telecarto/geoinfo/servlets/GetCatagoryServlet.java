package telecarto.geoinfo.servlets;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import telecarto.geoinfo.db.MysqlAccessBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

public class GetCatagoryServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public GetCatagoryServlet() {
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
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		MysqlAccessBean mysql = null;
		ResultSet resultSet = null;
//		String []thematicName = {"人口与就业","国民经济","农业","工业","人民生活","科技与教育","地形地貌","植被覆盖","水域","荒漠与裸露地表","交通网络","居民地与设施"};
		String []thematicName = {"人口与就业","国民经济","植被覆盖","水域","地形地貌","荒漠与裸露地表","交通网络","居民地与设施"};
		try {
			mysql = new MysqlAccessBean();
			String sql = "SELECT TABLE_NAME_CN,NAME_CN,ISGRADED,NAME_EN,YEARS FROM index_dictionary WHERE DISPLAY='1'";
			resultSet = mysql.query(sql);
//			int i = 0;
//			while (resultSet.next()) {
//				tableName[i] = resultSet.getString(1);
//				fieldName[i] = resultSet.getString(2);
//				i++;
//			}
			JSONArray fieldArray = new JSONArray();
			for (int j=0;j<thematicName.length;j++){
				JSONArray fields = new JSONArray();
				JSONArray grades = new JSONArray();
				JSONArray fields_en = new JSONArray();
				JSONArray fields_years = new JSONArray();
				JSONObject singleTable = new JSONObject();
				singleTable.put("tablename",thematicName[j]);
				while (resultSet.next()) {
					if (resultSet.getString(1).equals(thematicName[j])){
						fields.add(resultSet.getString(2));
						grades.add(resultSet.getString(3));
						fields_en.add(resultSet.getString(4));
						fields_years.add(resultSet.getString(5));
					}
				}
				resultSet.beforeFirst();
				singleTable.put("fields",fields);
				singleTable.put("isGraded",grades);
				singleTable.put("names",fields_en);
				singleTable.put("years",fields_years);
				fieldArray.add(singleTable);
			}

			PrintWriter out = response.getWriter();

//			System.out.println(fieldArray.toString());
			out.println(fieldArray.toString());

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

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
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
