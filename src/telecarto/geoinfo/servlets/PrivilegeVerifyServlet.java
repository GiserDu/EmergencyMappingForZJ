package telecarto.geoinfo.servlets;

import telecarto.geoinfo.db.UserDaoImp;
import telecarto.vo.user.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PrivilegeVerifyServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public PrivilegeVerifyServlet() {
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

		System.out.println("privilege verify.");
		response.setContentType("text/html");
		
		System.out.println("context path: "+request.getContextPath());
		//PrintWriter out = response.getWriter();
		//response.sendRedirect(request.getContextPath()+"/dataManage/DataView.jsp");
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("./dataManage/DataView.jsp");
		dispatcher.forward(request, response);
		
		/*
		//Purpose
		Object purposeO = request.getParameter("purpose");
		System.out.println(purposeO);
		if(purposeO != null){
			String purpose = purposeO.toString();
			switch(purpose){
			case "DATA_VIEW":
				if(!verifyDataView(request)){
					System.out.println("data view succeed.");
					response.sendRedirect(request.getContextPath()+"/dataManage/DataView.jsp");
					System.out.println("send redirect ends.");
					//request.getRequestDispatcher("http://localhost:8080/geoInfoHB/dataManage/DataView.jsp").forward(request, response);
				}else{
					System.out.println("back to index page.");
					response.sendRedirect("../");
				}
				break;
				default:
					return;
			}
		}else{
			return;
		}
		*/
		//out.flush();
		//out.close();
	}
	
	
	private boolean verifyDataView(HttpServletRequest request){
		User u = verifyUserExistence(request);
		if(u != null){
			//TODO: below has to be modified when take  privileges into account.
			UserDaoImp uimp = new UserDaoImp();
			if(uimp.verify(u)){
				return true;			}
		}
		return false;
	}
	
	/**
	 * Verify whether the user exists.
	 * @param request
	 * @return
	 */
	private User verifyUserExistence(HttpServletRequest request){
		
		Object unameO = request.getParameter("username");
		Object pwdO = request.getParameter("password");
		
		if(unameO != null && pwdO != null){
			return new User(unameO.toString(), pwdO.toString());
		}else{
			return null;
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
