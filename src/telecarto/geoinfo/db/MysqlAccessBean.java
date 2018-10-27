package telecarto.geoinfo.db;

import com.zz.chart.data.JConnection;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MysqlAccessBean {
	 	public static Connection con = null;//定义连接  
	    public static Statement st = null;//定义statement  
	    public static PreparedStatement pStmt = null; 
	    public ResultSet result = null;//定义结果集   
	    public static String url;//定义URL
	    public static String user;//定义用户名     
	    public static String password;//定义密码       
	    
	    //建立与数据库连接的函数    
	    public Connection getConn(){
			//mysql参数设置
			JConnection jConnection = new JConnection();
			Connection con = jConnection.getConnection();
			return con;
	     }  
	    
	     //构造函数
	     public MysqlAccessBean()  
	     {
	         con=this.getConn();  
	         //构造函数，默认加裁配置文件为jdbc.driver  
	     }  
		          
		          
		 // 执行数据库查询并返回查询结果
		 public ResultSet query(String sql)
		 {
			 //con=this.getConn();
			 try{
				 pStmt = con.prepareStatement(sql);
				 con.setAutoCommit(false);
				 result = pStmt.executeQuery();
	//	        	 st = con.createStatement();//获取Statement
	//	        	 result = st.executeQuery(sql);//执行查询，返回结果集
			 } catch (Exception e)  {
				 e.printStackTrace();
			 }
			 return result;
		 }
	       
	       
	     //执行数据库更新    
		public void update(String sql,String detail,String geojson)
		{
			//con=this.getConn();
			try{
				//st = con.createStatement();//获取Statement
				pStmt = con.prepareStatement(sql);
				con.setAutoCommit(false);

				Date time= new Date(new java.util.Date().getTime());
				DateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   //创建一个格式化日期对象
				String punchTime = simpleDateFormat.format(time);   //格式化后的时间

				pStmt.setString(1,punchTime);
				pStmt.setString(2,detail);
				pStmt.setString(3,geojson);

				int j = pStmt.executeUpdate();
				//结束当前事务，释放资源
				con.commit();
				if(j!=0) System.out.println("写入信息成功！");
				else System.out.println("写入信息失败！");
			} catch (Exception e)  {
				e.printStackTrace();
			}
		}

		public void update(String sql,String mapName,String mapURL,String display,String mapClass,String description)
		{
			try{
				//st = con.createStatement();//获取Statement
				pStmt = con.prepareStatement(sql);
				con.setAutoCommit(false);

				Date time= new Date(new java.util.Date().getTime());
				DateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   //创建一个格式化日期对象
				String punchTime = simpleDateFormat.format(time);   //格式化后的时间

				pStmt.setString(1,mapName);
				pStmt.setString(2,mapURL);
				pStmt.setString(3,punchTime);
				pStmt.setString(4,display);
				pStmt.setString(5,mapClass);
				pStmt.setString(6,description);

				int j = pStmt.executeUpdate();
				//结束当前事务，释放资源
				con.commit();
				if(j!=0) System.out.println("写入信息成功！");
				else System.out.println("写入信息失败！");
			} catch (Exception e)  {
				e.printStackTrace();
			}
		}


		public int update(String sql)
		{
			 int j=0;
			 try{
//				 st = con.createStatement();//获取Statement
//				 st.executeUpdate(sql);//执行查询，返回结果集
				 pStmt = con.prepareStatement(sql);
				 con.setAutoCommit(false);
				 j = pStmt.executeUpdate();
				 //结束当前事务，释放资源
				 con.commit();
				 if(j!=0) System.out.println("写入信息成功！");
				 else System.out.println("写入信息失败！");
			 } catch (Exception e)  {
				 e.printStackTrace();
			 }
			 finally {
				 return j;
			 }
		}


		//关闭数据库连接
		public void close(){
			try{
				if (result != null)result.close();
				if (st != null)st.close();
				if (pStmt != null)pStmt.close();
				if (con != null)con.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
}
