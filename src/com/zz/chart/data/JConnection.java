package com.zz.chart.data;
import com.zz.util.JUtil;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * MySQL连接
 * @author 501
 *
 */
public class JConnection {
 
  /**
	 * 获取一个连接.
	 */
	
	private Connection conn = null;
	private Statement stmt = null;
	
	public JConnection() {
		
	    try
	    {	String configpath = JUtil.GetWebInfPath()+"/prop/dbconpara.properties";
	    	InputStream ips = new FileInputStream(configpath);
	    	Properties pro = new Properties();
	    	pro.load(ips);
	    	String url = pro.getProperty("url");
	    	String user = pro.getProperty("user");
	    	String pwd = pro.getProperty("pwd");
//			driver="com.mysql.jdbc.Driver";
	    	Class.forName("com.mysql.jdbc.Driver").newInstance();
	    	conn=DriverManager.getConnection(url,user,pwd);
	    }
	    catch(Exception e)
	    {
	    	e.toString();
	    }
		try 
		{
			conn.setAutoCommit(true);
		} 
		catch(SQLException e)
		{
			e.toString();
		}
	}
	

	
	
	public Connection getConnection() {
		return conn;
	}
	
	/**
	 * 释放一个连接
	 */
	
	public void close() {
		try {

			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void freeConnection(Connection conn) {
    	try {
    		//关闭连接
	        if (conn!=null) conn.close();
    	}
    	catch (Exception e) {
    		e.getMessage();
    	}
	}
	
	public static void main(String[] args)
	  {
		 //		
		  	ResultSet rs = null;
		  	PreparedStatement stmt = null;
		  	JConnection jConnection= new JConnection();
			Connection conn = jConnection.getConnection();

			 String sql="select * from datadictionary where CN_NAME=?";
			  try{
			   stmt=conn.prepareStatement(sql);
			   stmt.setString(1, "重复2次以上");
			   rs=stmt.executeQuery();
			   while(rs.next())
			   {
				   String geocode=rs.getString(1);
				//   data.GEOCODE.put(geocode, cityname);
				   System.out.println("编号："+geocode);
				   
			   }
			  }
			  catch(Exception e)
			  {
				  e.getMessage();
			  }
			   }
}
