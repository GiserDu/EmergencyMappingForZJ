package telecarto.geoinfo.servlets;


import com.zz.chart.data.ClassData;
import net.sf.json.JSONArray;
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
import java.util.ArrayList;


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

		ResultSet resultSet;
		String type = request.getParameter("type");
		if(type.equals("mapInfoUpload")){
			String map_id=request.getParameter("map_id");
			if (map_id.equals("")){
				//提交新专题图
				mapInfoSubmit(request,response);
			}else {
				//提交编辑后的专题图信息
				mapInfoUpdate(request,response,map_id);
			}
		}else if(type.equals("imageUpload")){
			PrintWriter out = response.getWriter();
			out.println("已上传，服务器不作处理");
			out.flush();
			out.close();
		}else if(type.equals("mapInfoQuery")) {
				//查询已有专题图信息
				String map_id=request.getParameter("map_id");
				MysqlAccessBean mysql1 = null;
				try {
					mysql1 = new MysqlAccessBean();
					String sql = "SELECT map_name,map_tag,map_info, submit_time,edit_time,picture FROM user_map where map_id="+map_id;

					resultSet = mysql1.query(sql);
					JSONObject mapObject = new JSONObject();
					while (resultSet.next()) {
						mapObject.put("map_name",resultSet.getString("map_name"));
						mapObject.put("map_tag",resultSet.getString("map_tag"));
						mapObject.put("map_info",resultSet.getString("map_info"));
						mapObject.put("picture",resultSet.getString("picture"));
						//mapObject.put("thematicClass",resultSet.getString(6));
					}

					PrintWriter out = response.getWriter();
					out.println(mapObject);
					out.flush();
					out.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					mysql1.close();
				}
			}
		else if (type.equals("delete")){//删除专题图
			MysqlAccessBean mysql = null;
			try {
				String mapID = request.getParameter("id");
				int id = Integer.parseInt(mapID);
				int flag;
				mysql = new MysqlAccessBean();
				String sql = "UPDATE `user_map` SET `is_deleted`='1' WHERE `map_id`= "+ id +"";
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
		JSONObject message=new JSONObject();
		message.put("message","后台已上传入数据库");
		PrintWriter out = response.getWriter();
		out.print(message);
		out.flush();
		out.close();
	}
	//地图信息更新
	public void mapInfoUpdate(HttpServletRequest request, HttpServletResponse response,String map_id) throws IOException {
		int flag=-1;
		String mapTitle=request.getParameter("mapTitle");
		String mapTag=request.getParameter("mapTag");
		String mapInfo=request.getParameter("mapInfo");
		String treeNodes=request.getParameter("treeNodes");
		String userId=request.getParameter("userId");
		String picture64=request.getParameter("picture64");
		//链接数据库
		MysqlAccessBean mysql = null;
		try {
			int id = Integer.parseInt(map_id);
			Connection connection = DBManager.getConnection();
			PreparedStatement pst=connection.prepareStatement( "UPDATE `user_map` SET " +
					"map_name=?," +
					"map_tag=?," +
					"map_info=?," +
					"map_param=?," +
					"user_id=?," +
					"picture=? " +
					"WHERE `map_id`=?");
//			String sql = "UPDATE `user_map` SET " +
//					"'map_name'="+mapTitle+"," +
//					"'map_tag'="+mapTag+"," +
//					"'map_info'="+mapInfo+","+
//					"'map_param'="+treeNodes+","+
//					"'user_id'="+userId+","+
//					"'picture'="+picture64+","+
//					"WHERE `map_id`= "+ id ;
			pst.setString(1, mapTitle);
			pst.setString(2, mapTag);
			pst.setString(3, mapInfo);
			pst.setString(4, treeNodes);
			pst.setString(5, userId);
			pst.setString(6, picture64);
			pst.setInt(7, id);

			pst.executeUpdate();
			pst.close();
			connection.close();

			//flag = mysql.update(sql);
			JSONObject resultFlag=new JSONObject();
			resultFlag.put("flag",flag);
			PrintWriter out = response.getWriter();
			out.print(resultFlag);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}

