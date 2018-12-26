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

/**
 * 获取地基数据
 * @author Tianqin
 *
 */
public class GetThematicMapServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public GetThematicMapServlet() {
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
		String type = request.getParameter("type");
		String user_id= request.getParameter("user_id");
		MysqlAccessBean mysql = null;
		ResultSet resultSet;
		//初始化后台管理页面(按照前端的格式进行组织)
		if(type.equals("initManage")){
			try {
				mysql = new MysqlAccessBean();
				String sql = "SELECT map_id,map_name,map_tag,map_info, submit_time,edit_time,picture FROM user_map where user_id=\'"+user_id+"\'";

				resultSet = mysql.query(sql);
				JSONArray mapArray = new JSONArray();
				while (resultSet.next()) {
					JSONObject mapObject = new JSONObject();
					mapObject.put("map_id",resultSet.getString("map_id"));
					mapObject.put("map_name",resultSet.getString("map_name"));
					mapObject.put("map_tag",resultSet.getString("map_tag"));
					mapObject.put("map_info",resultSet.getString("map_info"));
					mapObject.put("submit_time",resultSet.getString("submit_time"));
					mapObject.put("edit_time",resultSet.getString("edit_time"));
					mapObject.put("picture",resultSet.getString("picture"));
					//mapObject.put("thematicClass",resultSet.getString(6));
					mapArray.add(mapObject);
				}

				PrintWriter out = response.getWriter();
				out.println(mapArray.toString());

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
		//初始化产品展示页面
		else {
			try {
				String [] className = {"样例专题1","样例专题2"};
				mysql = new MysqlAccessBean();
				String sql = "SELECT CLASS,MAP_NAME,MAP_URL FROM thematic_maps WHERE DISPLAY='1'";
				resultSet = mysql.query(sql);
				JSONArray mapArray = new JSONArray();
				for (int j=0;j<className.length;j++){
					JSONArray names = new JSONArray();
					JSONArray srcs = new JSONArray();
					JSONObject singleClass = new JSONObject();
					singleClass.put("toClass",className[j]);
					while (resultSet.next()) {
						if (resultSet.getString(1).equals(className[j])){
							names.add(resultSet.getString(2));
							String src = resultSet.getString(3)+"/TileGroup0/0-0-0.jpg";
							srcs.add(src);
						}
					}
					resultSet.beforeFirst();
					singleClass.put("name",names);
					singleClass.put("src",srcs);
					mapArray.add(singleClass);
				}

				PrintWriter out = response.getWriter();
				out.println(mapArray.toString());

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
			doGet(request,response);
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
