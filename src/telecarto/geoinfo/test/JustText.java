package telecarto.geoinfo.test;

import com.zz.chart.data.ClassData;
import net.sf.json.JSONArray;
import telecarto.geoinfo.db.MysqlAccessBean;

import java.awt.Color;
import java.sql.ResultSet;
import java.util.ArrayList;

public class JustText {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Color c = new Color(255, 34, 0);
//		System.out.println("color: "+c.getRGB()+" r:" +c.getRed()+" g: " +c.getGreen()+" b: "+c.getBlue());
//		Color c2 = new Color(0+34*256+255*256*256);
//		System.out.println(c2.getRed()+", "+c2.getGreen()+", "+ c2.getBlue());
//
//		System.out.println(2<<4);

//		JSONArray regionDataValue=JSONArray.fromObject("['a','b']");
//		//String regionDataValues=regionDataValue.toArray().toString();
//		String[] regionData=new String[regionDataValue.size()];
//		if(regionDataValue!=null||regionDataValue.size()!=0){
//			for(int i=0;i<regionDataValue.size();i++){
//				regionData[i]=regionDataValue.get(i).toString();
//			}
//		}
//		System.out.println(regionData);

        //数据库连接测试
//		MysqlAccessBean mysql = new MysqlAccessBean();
//		String sql;
//		ResultSet resultSet2;
//		String regionClass="1";
//		//根据输入行政等级class，确立
//		sql="SELECT * FROM 	region_info WHERE	class =" + regionClass;
//
//		//sql_select = "LEFT JOIN "+ tableName +" t2 ON t1.RGN_CODE = t2.RGN_CODE WHERE t1.RGN_CODE LIKE '"+Param+"' AND t1.RGN_CODE!= '"+regionParam+"' AND t2.YEAR = '" + year + "'";
//		//sql_select = "LEFT JOIN "+ tableName +" t2 ON t1.RGN_CODE = t2.RGN_CODE WHERE t1.RGN_CLASS = '" + regionParam + "' AND t2.YEAR = '" + year + "'";
//		//sql = "SELECT t1.RGN_CODE,t1.RGN_NAME,t1.GEOMETRY,t1.REGION_X,t1.REGION_Y,t2."+ fieldName +" FROM region t1 " + sql_select;
//		// sql = "SELECT t1.RGN_CODE,t1.RGN_NAME,t1.GEOMETRY,t1.REGION_X,t1.REGION_Y,t2."+ fieldName +" FROM region t1 " + sql_select;
//		sql="";
//		try {
//			resultSet2 = mysql.query(sql);
//			ArrayList<ClassData> classList = new ArrayList<>();
//			while (resultSet2.next()) {
//				ClassData classData = new ClassData(resultSet2.getString(1),
//						resultSet2.getString(3),resultSet2.getString(5),resultSet2.getString(6),resultSet2.getString(7),resultSet2.getString(9));
//				classList.add(classData);
//			}
//
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		finally {
//			mysql.close();
//		}
        String s="总人口数（万）";
        int startIndex=s.indexOf("（");
        int endIndex=s.indexOf("）");
        String strn=s.substring(startIndex+1,endIndex);//截取数字,001,002等
        System.out.println(strn);
	}

}
