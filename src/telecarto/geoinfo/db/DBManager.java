package telecarto.geoinfo.db;

import com.zz.util.JUtil;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DBManager {
//	private static String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
//	private static String url = "jdbc:oracle:thin:@10.5.201.200:1521:orcl";
//	private static String user= "hbmap";
//	private static String password="hbmap";

	public static Connection getConnection(){
		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			String configPath = JUtil.GetWebInfPath()+"/prop/dbconpara.properties";
			InputStream ips = new FileInputStream(configPath);
			Properties pro = new Properties();
			pro.load(ips);
			String url = pro.getProperty("url");
			String user = pro.getProperty("user");
			String pwd = pro.getProperty("pwd");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
//			conn=DriverManager.getConnection(url,user,pwd);
			return DriverManager.getConnection(url, user, pwd);


		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e)
		{
			e.toString();
		}
		return null;
		
	}
}
