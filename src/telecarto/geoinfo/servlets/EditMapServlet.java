package telecarto.geoinfo.servlets;


import net.sf.json.JSONObject;
import telecarto.geoinfo.db.DBManager;
import telecarto.geoinfo.db.MysqlAccessBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class EditMapServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public EditMapServlet() {
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
			throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		String description;
		String name;
		String display;
		String mapClass;
		String type = request.getParameter("type");
		if(type.equals("mapInfoUpload")){
			mapInfoSubmit(request,response);
		}else if(type.equals("imageUpload")){
			PrintWriter out = response.getWriter();
			out.println("已上传，服务器不作处理");
			out.flush();
			out.close();
		}
		String mapID = request.getParameter("id");
		int id = Integer.parseInt(mapID);

		MysqlAccessBean mysql = null;
		if(type.equals("edit")){//编辑专题图
			description = request.getParameter("description");
			name = request.getParameter("name");
			display = request.getParameter("display");
			mapClass = request.getParameter("mapClass");
			try{
				int flag;
				mysql = new MysqlAccessBean();
				Date time= new Date(new java.util.Date().getTime());
				DateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   //创建一个格式化日期对象
				String punchTime = simpleDateFormat.format(time);   //格式化后的时间

				String sql = "UPDATE thematic_maps SET MAP_NAME = '" + name + "', DISPLAY = '" + display +
						"',DESCRIPE = '" + description + "',DATE='"+punchTime+"',CLASS='"+mapClass+"' WHERE ID = "+ id +"";
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
		else {//删除专题图
			try {
				int flag;
				mysql = new MysqlAccessBean();
				String sql = "DELETE FROM thematic_maps WHERE ID = "+ id +"";
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
	//地图信息提交
	public void mapInfoSubmit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String mapTitle=request.getParameter("mapTitle");
		String mapTag=request.getParameter("mapTag");
		String mapInfo=request.getParameter("mapInfo");
		String treeNodes=request.getParameter("treeNodes");
		String userId=request.getParameter("userId");
		String picture64=request.getParameter("picture64");
		//链接数据库
		ResultSet resultSet;
		Connection connection = DBManager.getConnection();
		PreparedStatement pst= null;
		try {
			pst = connection.prepareStatement( "INSERT INTO user_map (map_name,map_tag,map_info,map_param,user_id, picture)" +
                    " VALUES (?, ?, ?,?,?,?)");

		pst.setString(1, mapTitle);
		pst.setString(2, mapTag);
		pst.setString(3, mapInfo);
		pst.setString(4, treeNodes);
		pst.setString(5, userId);
		pst.setString(6, picture64);

		pst.execute();
		pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PrintWriter out = response.getWriter();
		out.println("112");
		out.flush();
		out.close();
	}
}

