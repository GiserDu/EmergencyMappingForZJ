package com.zz.servlet;


import com.zz.util.NetworkUtil;
import com.zz.util.imageUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class printMapServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public printMapServlet() {
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
//		response.setContentType("image/png;charset=utf-8");
		response.setContentType("text/javascript;charset=UTF-8");//返回json格式的数据
		request.setCharacterEncoding("UTF-8");//设置服务器端对前端传输数据的解码方式!!!

		//获取客户端的ip地址
		String ip = NetworkUtil.getIpAddr(request);

		String imgURL = request.getParameter("img");
		String layout = request.getParameter("layoutID");
		String dpi = request.getParameter("dpi");
		String printLegendFlag = request.getParameter("flag");//判断当前图例的状态

		String strBackUrl = "http://" + request.getServerName() //服务器地址
				+ ":"
				+ request.getServerPort()		 //端口号
				+ request.getContextPath();      //项目名称

		String ipContext = ip.replaceAll("\\.","-");
		String printSavePath = getServletContext().getRealPath("/") + "printMap/"+ ipContext ;
		String printMapURL = imageUtil.getImgFromUrl(strBackUrl,ipContext,imgURL,printSavePath,printLegendFlag,layout,dpi);
		PrintWriter out = response.getWriter();
		out.println(printMapURL);
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet.
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
