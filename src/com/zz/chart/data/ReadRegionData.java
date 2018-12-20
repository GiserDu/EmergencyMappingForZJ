package com.zz.chart.data;

import telecarto.geoinfo.db.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ReadRegionData {

	private static String[] regonCode;
	private static String[] regonX;
	private static String[] regonY;
	private static String[] regonClass;
	private static String[] regonName;
	private static String rowNum;
	private static String ParamTemp = "0";


	public String getRowNum() {
		return rowNum;
	}

	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}

	//后面可以按照用户所选区域进行参数传递,获取对应区域的数据
	public  ReadRegionData() {

	}
	//浙江本地数据库数据源查询，按行政区域类别查询
	public static void doReadRegionDataByRegionParam(String regionParam){
		//regionParam=1时,调取第一级行政区(17个地州市)
		ParamTemp = regionParam;
		JConnection jConnection = new JConnection();
		String sql_select;
		String sql;
		String sql1;

		if(regionParam.equals("1")){
			sql_select = "WHERE class = '" + regionParam + "'";
			sql = "SELECT citycode,x,y,class,name FROM region_info_copy1 " + sql_select + "ORDER BY citycode";
			sql1 = "SELECT COUNT(citycode) FROM region_info_copy1 " +sql_select;
		}
		else {
			//String Param = regionParam.substring(0, 4) + "__";
			//String Param =  "1000_";
			//sql_select = "WHERE RGN_CODE LIKE '" + Param + "' AND RGN_CODE!= '" + regionParam + "'";
			sql_select = "WHERE class = '" + regionParam + "'";
			sql = "SELECT coutcode,x,y,class,name FROM region_info_copy1 " + sql_select + "ORDER BY coutcode";
//			sql = "SELECT RGN_CODE,NAME_CN,NAME_EN,POP_TOTAL,POP_PERM FROM theme_pop " + sql_select;
			sql1 = "SELECT COUNT(coutcode) FROM region_info_copy1 " + sql_select;
		}
		try {
			Connection connection = DBManager.getConnection();
			PreparedStatement pst;
			PreparedStatement pst2;
			ResultSet resultSet;

			pst = connection.prepareStatement(sql1);
			resultSet = pst.executeQuery();
			while (resultSet.next()) {
				rowNum = resultSet.getString(1);
			}
			pst.close();
			int row = Integer.parseInt(rowNum);
			regonCode = new String[row];
			regonX = new String[row];
			regonY = new String[row];
			regonClass = new String[row];
			regonName = new String[row];
			int i =0;

			pst2 = connection.prepareStatement(sql);
			resultSet = pst2.executeQuery();
			while (resultSet.next()) {
				regonCode[i] = resultSet.getString(1);
				regonX[i] = resultSet.getString(2);
				regonY[i] = resultSet.getString(3);
				regonClass[i] = resultSet.getString(4);
				regonName[i] = resultSet.getString(5);
				i++;
			}
			pst2.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			jConnection.close();
		}


	}
	//按照行政区域单个查询
	public static void doReadRegionDataByEachRegion(String[] regionNames){

		JConnection jConnection = new JConnection();

		ArrayList<String > regonCodeList=new ArrayList<>();
		ArrayList<String > regonXList=new ArrayList<>();
		ArrayList<String > regonYList=new ArrayList<>();
		ArrayList<String > regonClassList=new ArrayList<>();
		ArrayList<String > regonNameList=new ArrayList<>();

		//sql_select = "WHERE name = '" + regionName + "'";
		//sql = "SELECT citycode,x,y,class,name FROM region_info_copy1 " + sql_select + "ORDER BY citycode";
		//sql1 = "SELECT COUNT(citycode) FROM region_info_copy1 " +sql_select;
		try {
			Connection connection = DBManager.getConnection();

			ResultSet resultSet;

			PreparedStatement pst = connection.prepareStatement( "SELECT citycode,x,y,class,name FROM region_info_copy1 "  +
					" WHERE name =  ? ");
			for(int i=0;i<regionNames.length;i++){
				pst.setString(1, regionNames[i]);
				resultSet=pst.executeQuery();
				while (resultSet.next()) {
					regonCodeList.add( resultSet.getString(1)) ;
					regonXList.add(resultSet.getString(2)) ;
					regonYList.add(resultSet.getString(3)) ;
					regonClassList.add( resultSet.getString(4));
					regonNameList.add(resultSet.getString(5));
				}
			}
			pst.close();
			//list转Array
			regonCode= regonCodeList.toArray(new String[regonCodeList.size()]);
			regonX= regonXList.toArray(new String[regonXList.size()]);
			regonY= regonYList.toArray(new String[regonYList.size()]);
			regonClass= regonClassList.toArray(new String[regonClassList.size()]);
			regonName= regonNameList.toArray(new String[regonNameList.size()]);

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			jConnection.close();
		}


	}

	public static void doReadRegionData(String regionParam){
		//regionParam=1时,调取第一级行政区(17个地州市)
		ParamTemp = regionParam;
		JConnection jConnection = new JConnection();
		String sql_select;
		String sql;
		String sql1;

		if(regionParam.equals("1")){
			sql_select = "WHERE RGN_CLASS = '" + regionParam + "'";
			sql = "SELECT RGN_CODE,REGION_X,REGION_Y,RGN_CLASS,RGN_NAME FROM region " + sql_select;
			sql1 = "SELECT COUNT(RGN_CODE) FROM region " +sql_select;
		}
		else {
			//String Param = regionParam.substring(0, 4) + "__";
			//String Param =  "1000_";
			//sql_select = "WHERE RGN_CODE LIKE '" + Param + "' AND RGN_CODE!= '" + regionParam + "'";
			sql_select = "WHERE RGN_CLASS = '" + regionParam + "'";
			sql = "SELECT RGN_CODE,REGION_X,REGION_Y,RGN_CLASS,RGN_NAME FROM region " + sql_select;
//			sql = "SELECT RGN_CODE,NAME_CN,NAME_EN,POP_TOTAL,POP_PERM FROM theme_pop " + sql_select;
			sql1 = "SELECT COUNT(RGN_CODE) FROM region " + sql_select;
		}
		try {
			Connection connection = DBManager.getConnection();
			PreparedStatement pst;
			PreparedStatement pst2;
			ResultSet resultSet;

			pst = connection.prepareStatement(sql1);
			resultSet = pst.executeQuery();
			while (resultSet.next()) {
				rowNum = resultSet.getString(1);
			}
			pst.close();
			int row = Integer.parseInt(rowNum);
			regonCode = new String[row];
			regonX = new String[row];
			regonY = new String[row];
			regonClass = new String[row];
			regonName = new String[row];
			int i =0;

			pst2 = connection.prepareStatement(sql);
			resultSet = pst2.executeQuery();
			while (resultSet.next()) {
				regonCode[i] = resultSet.getString(1);
				regonX[i] = resultSet.getString(2);
				regonY[i] = resultSet.getString(3);
				regonClass[i] = resultSet.getString(4);
				regonName[i] = resultSet.getString(5);
				i++;
			}
			pst2.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			jConnection.close();
		}


	}
	public static String[] getRegonCode() {return regonCode;}
	public static void setRegonCode(String[] regonCode) {regonCode = regonCode;}
	public static String[] getRegonX() {
		return regonX;
	}
	public static void setRegonX(String[] regonX) {
		regonX = regonX;
	}
	public static String[] getRegonY() {
		return regonY;
	}
	public static void setRegonY(String[] regonY) {
		regonY = regonY;
	}
	public static String[] getRegonClass() {
		return regonClass;
	}
	public static String[] getRegonName() {
		return regonName;
	}
	public static String getRegonParam() {
		return ParamTemp;
	}
	//test
//	public static void main(String[] args){
//		ReadRegionData rrd = new ReadRegionData();
//		String[] regioncodeStrings = rrd.getRegonCode();
//		System.out.println(regioncodeStrings[2]);
//
//	}
}
