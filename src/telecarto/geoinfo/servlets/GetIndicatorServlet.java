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

public class GetIndicatorServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public GetIndicatorServlet() {
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
		String id;
		String display;
		String type = request.getParameter("type");
		MysqlAccessBean mysql = null;
		//编辑指标的可见性
		if(type.equals("edit")){
			id = request.getParameter("id");
			int indexID = Integer.parseInt(id);
			display = request.getParameter("display");
			try{
				int flag;
				mysql = new MysqlAccessBean();
				String sql = "UPDATE index_dictionary SET DISPLAY = '" + display + "' WHERE ID = "+ indexID +"";
				flag = mysql.update(sql);
				PrintWriter out = response.getWriter();
				out.println(flag);
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
		else {
			ResultSet resultSet;
			try {
				mysql = new MysqlAccessBean();
				String sql = "SELECT ID,NAME_CN,TABLE_NAME_CN,DISPLAY,UNIT FROM index_dictionary";
				resultSet = mysql.query(sql);
				JSONArray tbArray = new JSONArray();
				while (resultSet.next()) {
					JSONObject tbObject = new JSONObject();
					tbObject.put("序号",resultSet.getInt(1));
					tbObject.put("指标名称",resultSet.getString(2));
					tbObject.put("所属主题",resultSet.getString(3));
					String isDisplay = resultSet.getString(4);
					if(isDisplay.equals("1")){
						tbObject.put("显示状态","可见");
					}
					else {
						tbObject.put("显示状态","不可见");
					}
					tbObject.put("单位",resultSet.getString(5));
					tbArray.add(tbObject);
				}

				PrintWriter out = response.getWriter();
				out.println(tbArray.toString());

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
