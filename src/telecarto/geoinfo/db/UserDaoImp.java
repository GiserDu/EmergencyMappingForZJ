/**
 * 对用户进行各种验证。
 */
package telecarto.geoinfo.db;

import java.sql.Connection;

import telecarto.vo.user.User;

/**
 * @author Tianqin
 *
 */
public class UserDaoImp {
	
	private Connection conn;
	private String tableName;
	
	public UserDaoImp(){
		this.conn= DBManager.getConnection();
		this.tableName=""; //TODO: Fill table name here.
	}
	
	
	/**
	 * Verify the username,password and privileges here. 
	 * @param u
	 * @return return true If passed, or return false.
	 */
	public boolean verify(User u){
		return true;
	}
}
