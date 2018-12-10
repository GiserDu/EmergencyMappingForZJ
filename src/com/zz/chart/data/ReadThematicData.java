package com.zz.chart.data;
import com.zz.util.JUtil;
import telecarto.geoinfo.db.MysqlAccessBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;


public class ReadThematicData {
	private String[] regonData;//地区编码
	private String[] data;//制图数据
	private String[] yearData;//年份
	private double max;//最大值
	private double min;//最小值
	private String regonName;
	private String dataName="";
	private String rowNum;
	private HashMap<String, String> dataMap;
	private double[] maxValues;
	private double[] minValues;

	public ReadThematicData(String thematicData,String regionParam,String year){

		String[] temp = thematicData.split(",");//数据库 表的名称 以及要查询的指标的名称
		String table = temp[0];//表的名称
		String fieldName = "";//获取指标名称
		for (int i = 1; i < temp.length; i++) {
			String cnnameAndUnit = null;
			cnnameAndUnit = JUtil.getCnNameAndUnit(temp[i]);
			String cnname = cnnameAndUnit.split(",")[0];
			fieldName+=temp[i]+",";
			dataName +=cnname + ",";
		}
		fieldName = fieldName.substring(0,fieldName.length()-1);//英文名
		dataName = dataName.substring(0,dataName.length()-1);//中文名
		String selectString = "NAME_CN,RGN_CODE," + fieldName;

		String sqlString = "SELECT " ;
		String sqlString1 = " FROM ";
		String sql = sqlString + selectString + sqlString1 +table;
		String sql1;

		if(regionParam.equals("1")){
			String query1 = "%00";
			String query2 = "42900_";
			String query3 = "429021";
			String select = " WHERE (RGN_CODE LIKE '" + query1 + "' OR RGN_CODE LIKE '" + query2 + "' OR RGN_CODE='"+query3+"') AND YEAR='"+year+"'";
			sql = sql + select;
			sql1 = "SELECT COUNT(RGN_CODE) FROM "+table + select;
		}
		else {
			//String query = regionParam.substring(0, 4) + "__";
			String query = "100%";
			String select = " WHERE (RGN_CODE LIKE '" + query + "' AND RGN_CODE!= '" + regionParam + "') AND YEAR='"+year+"'";
			sql = sql + select;
			sql1 = "SELECT COUNT(RGN_CODE) FROM "+table + select;
		}

		JConnection jConnection = new JConnection();
		Connection connection = jConnection.getConnection();
		PreparedStatement pst;
		ResultSet resultSet;
		try {
			pst = connection.prepareStatement(sql1);
			resultSet = pst.executeQuery();
			while (resultSet.next()) {
				rowNum = resultSet.getString(1);
				System.out.println(rowNum);
			}
			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		int row = Integer.parseInt(rowNum);
		regonData = new String[row];
		data = new String[row];
//		yearData = new String[row];
		dataMap = new HashMap<String, String>();
		regonName = "";

		PreparedStatement pst2;
		int i = 0;
		try {
			pst2 = connection.prepareStatement(sql);
			resultSet = pst2.executeQuery();
			while (resultSet.next()) {
				regonName += resultSet.getString(1) + ",";
				regonData[i] = resultSet.getString(2);
//				yearData[i] = resultSet.getString(3);
				String tempString = "";
				for (int j = 0; j < temp.length-1; j++) {
					String tempData = resultSet.getString(j+3);
					if (tempData.length()==0) {
						tempData = "0";
						tempString+=tempData+",";
					}else {
						tempString+=tempData+",";
					}
				}
				data[i] = tempString.substring(0, tempString.length()-1);//去掉末尾逗号

				dataMap.put(regonData[i], data[i]);
				i++;
			}
			max = 0;
			System.out.print(data.length);
			for (int k = 0; k < data.length; k++) {
				String[] tempStrings;
				if(data[k].contains(",")){
					tempStrings = data[k].split(",");
//					for(String str: tempStrings){
//						System.out.print(str+",");
//					}
					for (int j = 0; j < tempStrings.length; j++) {
						double tempint = Double.parseDouble(tempStrings[j]);
						if (tempint > max) {
							max = tempint;
						}
					}
				}
				else {
					double tempint = Double.parseDouble(data[k]);
					if (tempint > max) {
						max = tempint;
					}
				}
			}
			maxValues = new double[2];
			maxValues[0] = max;
			maxValues[1] = max;
			min = 0;
			if(data[0].contains(",")){
				min = Double.parseDouble(data[0].split(",")[0]);
			}
			else {
				min = Double.parseDouble(data[0]);
			}
			for (int k = 0; k < data.length; k++) {
				String[] tempStrings;
				if(data[k].contains(",")){
					tempStrings = data[k].split(",");
					for (int j = 0; j < tempStrings.length; j++) {
						double tempint = Double.parseDouble(tempStrings[j]);
						if (tempint < min) {
							min = tempint;
						}
					}
				}
				else {
					double tempint = Double.parseDouble(data[k]);
					if (tempint < min) {
						min = tempint;
					}
				}
			}
			minValues = new double[2];
			minValues[0] = min;
			minValues[1] = min;

			pst2.close();
		} catch (Exception e) {
			System.out.println(i);
			e.printStackTrace();
		}

		jConnection.close();
	}

//	//浙江专属
//public ReadThematicData(String tableName,String[] fieldsName,String regionParam) {
//	//fieldsName 指标名称数组
//	;//数据库 表的名称 以及要查询的指标的名称
//	//返回fields 数组,对应的值数组
//
//	String sql = "SELECT" +
//			"*" +
//			"FROM\n" +
//			"\tregion_info\n" +
//			"LEFT JOIN " + tableName +
//			" ON region_info.citycode=" + tableName + ".`code`" +
//			"WHERE" +
//			"\tregion_info.class =" + regionParam;
//	ResultSet resultSet2;
//	MysqlAccessBean mysql = new MysqlAccessBean();
//	try {
//		resultSet2 = mysql.query(sql);
//
//		ArrayList<IndicatorData> indicatorList = new ArrayList<IndicatorData>();
//		ArrayList<String> regionCodeList = new ArrayList<String>();
//		//遍历每一行的查询结果
//		while (resultSet2.next()) {
//			//获取区域编码
//			regionCodeList.add(resultSet2.getString("code"));
//			//循环遍历指标数组，获得查询后某一行的值
//			ArrayList<String> fieldDatas = new ArrayList<>();
//			for (int i = 0; i < fieldsName.length; i++) {
//				fieldDatas.add(resultSet2.getString(fieldsName[i]));
//			}
//			//list转数组
//			String[] valueArray = fieldDatas.toArray(new String[fieldDatas.size()]);
//			//构建indicatorData
//			IndicatorData indicatorData = new IndicatorData(fieldsName, StringToDoule(valueArray));
//			indicatorList.add(indicatorData);
//
//		}
//
//
//	} catch (Exception e) {
//		e.printStackTrace();
//	} finally {
//		mysql.close();
//	}
//
//}

	public String[] getRegonData() {
		return regonData;
	}

	public void setRegonData(String[] regonData) {
		this.regonData = regonData;
	}

	public String[] getData() {
		return data;
	}

	public void setData(String[] data) {
		this.data = data;
	}

	public String[] getYearData() {
		return yearData;
	}


	//	public static String[] getUniqYears(String thematicData,String regionParam){
//
//		ReadThematicData rtData = new ReadThematicData(thematicData,regionParam);
//	     TreeSet<String> treeSet = new TreeSet<String>();
//	     String[] yeardatas = rtData.getYearData();
//	     for (int i = 0; i < yeardatas.length; i++) {
//			treeSet.add(yeardatas[i]);
//		}
//	     String[] yearStrings = new String[treeSet.size()];
//	       for (int j = 0; j < yearStrings.length; j++) {
//			yearStrings[j] = treeSet.pollFirst();
//		}
//	       System.out.println(yearStrings[0]);
//	       return yearStrings;
//
//	}
	public double[] StringToDoule(String[] arrs){

		double[] dbls=new double[arrs.length];
		for (int i=0;i<dbls.length;i++){
			dbls[i]=Double.parseDouble(arrs[i]);
		}
		return  dbls;
	}
	public void setYearData(String[] yearData) {
		this.yearData = yearData;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public String getRegonName() {
		return regonName;
	}

	public void setRegonName(String regonName) {
		this.regonName = regonName;
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public HashMap<String, String> getDataMap() {
		return dataMap;
	}

	public void setDataMap(HashMap<String, String> dataMap) {
		this.dataMap = dataMap;
	}
	public double[] getMaxValues() {
		return maxValues;
	}
	public void setMaxValues(double[] maxValues) {
		this.maxValues = maxValues;
	}
	public double[] getMinValues() {
		return minValues;
	}
	public void setMinValues(double[] minValues) {
		this.minValues = minValues;
	}

//	public static void main(String[] args){
//		String thematicData = "dz,RK_HSZ,RK_SW,RK_SZ,RK_ZY";
//
//		String[] years = getUniqYears(thematicData);
//		for (int i = 0; i < years.length; i++) {
//			System.out.println(years[i]);
//		}
//
//	}
}
