package telecarto.data.util;

import java.util.ArrayList;





import org.json.JSONArray;
import org.json.JSONObject;

import telecarto.data.jm.dj.JingMenDJ3_1;

/**
 * 将数据库中的结果转换成JSON
 * @author Tianqin
 *
 */
public class JsonConverterUtil {

	/**
	 * 将地基3_1表转换成json格式
	 */
	public static String convertDJ3_1(ArrayList<JingMenDJ3_1> data){
		
		JSONObject resultObject = new JSONObject();
		
		JSONArray contentArr = new JSONArray();
		ArrayList<String> indicatorsTJDY = new ArrayList<>();
		ArrayList<String> indicatorsYSMC = new ArrayList<>();
		for(JingMenDJ3_1 dj:data){
			JSONObject o = new JSONObject();
			o.put("统计单元类型", dj.getTJDYLX());
			o.put("统计单元代码", dj.getTJDYDM());
			o.put("统计单元名称", dj.getTJDYMC());
			
			String tjdymc = dj.getTJDYMC();
			if(!indicatorsTJDY.contains(tjdymc)){
				indicatorsTJDY.add(tjdymc);
			}
			String ysmc = dj.getYSMC();
			if(!indicatorsYSMC.contains(ysmc)){
				indicatorsYSMC.add(ysmc);
			}			
			
			o.put("要素代码", dj.getYSDM());
			o.put("要素名称", ysmc);
			o.put("面积（平方米）", dj.getMJ());
			o.put("面积占比（%）", dj.getMJZB());
			contentArr.put(o);
		}
		
		JSONArray arrTJDY = new JSONArray();
		for(String tjdy:indicatorsTJDY){
			arrTJDY.put(tjdy);
		}
		
		JSONArray arrYSMC = new JSONArray();
		for(String ys:indicatorsYSMC){
			arrYSMC.put(ys);
		}
		
		
		JSONArray xArr= new JSONArray();
		JSONObject xArr1 = new JSONObject();
		xArr1.put("统计单元名称", arrYSMC);
		JSONObject xArr2 = new JSONObject();
		xArr2.put("要素名称", arrTJDY);
		xArr.put(xArr1);
		xArr.put(xArr2);
		
		JSONArray yArr= new JSONArray();
		yArr.put("面积（平方米）");
		yArr.put("面积占比（%）");
		
		resultObject.put("TotalNum", data.size());
		resultObject.put("Content", contentArr);
		resultObject.put("xVal", xArr);
		resultObject.put("yVal", yArr);
		
		return resultObject.toString();
	}
}
