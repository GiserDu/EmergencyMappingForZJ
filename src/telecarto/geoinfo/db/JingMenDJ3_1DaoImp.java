package telecarto.geoinfo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import telecarto.data.jm.dj.JingMenDJ3_1;


/**
 * 获取荆门地基表3_1数据
 * @author Tianqin
 *
 */
public class JingMenDJ3_1DaoImp {
	private Connection conn;
	private String tableName;
	
	private String city;
	private String district;
	
	
	public JingMenDJ3_1DaoImp(){
		this.conn = DBManager.getConnection();
		this.tableName="D430802_DJ3_1";
	}
	
	public JingMenDJ3_1DaoImp(String city,String district,String tableName){
		this.conn = DBManager.getConnection();
		this.tableName=tableName;
		this.city = city;
		this.district = district;
	}
	
	public void close(){
		if(this.conn != null){
			try {
				this.conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<JingMenDJ3_1> getAllData(){
		String sql = "SELECT * FROM " + tableName;
		ArrayList<JingMenDJ3_1> results = new ArrayList<>();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				JingMenDJ3_1 dj = new JingMenDJ3_1();
				dj.setTJDYLX(rs.getString("TJDTLX")); //TODO: 数据中应该为TJDYLX
				dj.setTJDYDM(rs.getString("TJDYDM"));
				dj.setTJDYMC(rs.getString("TJDYMC"));
				dj.setYSDM(rs.getString("YSDM"));
				dj.setYSMC(rs.getString("YSMC"));
				dj.setMJ(rs.getDouble("MJ"));
				dj.setMJZB(rs.getDouble("MJZB"));
				results.add(dj);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
}
