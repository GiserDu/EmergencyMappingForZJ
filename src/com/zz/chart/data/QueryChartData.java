package com.zz.chart.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class QueryChartData {
	private String[] data;
	private String dataName;
	private String rowNum;
	
	public String[] getData(String str, String year){
//		System.out.println("QueryChart serviced");
		String[] temp = str.split(",");
//		System.out.println(temp.length);
		String table = temp[0];//表的名称
		dataName = "";//获取指标名称 
		for (int i = 1; i < temp.length; i++) {
			dataName +=temp[i] + ",";
		}
			dataName = dataName.substring(0,dataName.length()-1);
			
			
		String selectString =  dataName;	
		String sqlString = "SELECT " ;
		String sqlString1 = " FROM ";
		String sqlString2 = " WHERE YEAR = ";
		
		String sql = sqlString + selectString + sqlString1 +table+ sqlString2 + year;
		System.out.println(sql);
		String sql1 = "SELECT COUNT(*) FROM zhzq ";	
		
//		ConnOrcl connOrcl = new ConnOrcl();
//		Statement stam = connOrcl.getStmt();
//		ResultSet resultSet = null;
		JConnection jConnection = new JConnection();
		
		Connection connection = jConnection.getConnection();
		PreparedStatement pst = null;
		ResultSet resultSet = null;
		
		try {
			pst = connection.prepareStatement(sql1);
			resultSet = pst.executeQuery();
			while (resultSet.next()) {
				rowNum = resultSet.getString(1);
			}
			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int row = Integer.parseInt(rowNum);
		
		data = new String[row];
		int i = 0;
		
		PreparedStatement pst2 = null;
		try {
			pst2 = connection.prepareStatement(sql);
			resultSet = pst2.executeQuery();
			
			while (resultSet.next()) {
				for (int j = 1; j < temp.length; j++) {
					if (j==1) {
						data[i] = resultSet.getString(j);
					} else {
						data[i] += ","+resultSet.getString(j);
					}			
				}
				i++;
			}
			
			pst2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}			
		
		return data;
		
	}

}
