package com.zz.util;

import com.zz.bglayer.Arith;
import com.zz.bglayer.ModelPrim;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.data.JConnection;
import com.zz.chart.data.ReadRegionData;
import com.zz.chart.data.ReadThematicData;
import jxl.read.biff.BiffException;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;



public class JUtil {
	public final static String CONTENT_TYPE_IMAGE_JPEG = "image/png;charset=utf-8";
	public final static double EPS_CLOSEINTEGER = 1.0e-6;
	private final static double MMPERINCH = 25.4;
	
	private final static int GradeImgWidth = 350;
	
//	private final static int titleMargin = 0;

	
	
	public static Rectangle2D.Double StringToRect(String str) {
		 System.out.println(str);
		String[] WCStrings = str.split(",");
		double[] a = new double[4];
		for (int i = 0; i < a.length; i++) {
			a[i] = Double.parseDouble(WCStrings[i]);
		}
		Rectangle2D.Double rect = new Rectangle2D.Double(a[0], a[1], a[2]
				- a[0], a[3] - a[1]);
		return rect;
	}

	// å¯¹æ°æ®è¿è¡å¤çï¼è¿åæ°å¼å¯¹åºçä½ç½®
	public static int Indexof(double[] a, double b) {
		int index = -1;
		for (int i = 0; i < a.length; i++) {
			if (a[i] == b)
				index = i;
		}
		return index;
	}

	/**
	 * com.ny.mapç¨ è²å½©è½¬æ¢æ¹æ³,è½¬æ¢ærgbæ°ç»æ¨¡å¼
	 * 
	 * @param rgb
	 * @return int[]
	 * @throws
	 * @since 1.0
	 */
	public static int[] GetRGB(int rgb) {
		int[] retval = new int[3];
		int R = (rgb & 0xff0000) >> 16;
		if (R > 256) {
			System.out.println("Wrong color value R: " + R);
			R = 256;
		}
		retval[0] = R;
		// int g = (color % (256*256))/256;
		int G = (rgb & 0xff00) >> 8;
		if (G > 256) {
			System.out.println("Wrong color value G:" + G);
			G = 256;
		}
		retval[1] = G;
		// int r = color %(256*256)%256;
		int B = (rgb & 0xff);
		if (B > 256) {
			System.out.println("Wrong color value B: " + B);
			B = 256;
		}
		retval[2] = B;
		return retval;
	}
	//å°RGBè½¬ä¸ºint
	public int getIntColor(int r, int g, int b){
		int rgb = r+g*256+b*256*256;
		return rgb;
	}

	/**
	 * è·åWEB-INFæå¨è·¯å¾
	 * 
	 * @return
	 */
	public static String GetWebInfPath() {
		String path = new JUtil().getClass().getClassLoader().getResource("")
				.getPath();
		path = path.replace("classes","");
		path = path.replace("%20", " ");// é¤ç©ºæ ¼
		return path;
	}

	/**
	 * å¤æ­è¯¥ä¸»é¢åºååºå¾æ¯å¦æè¯¦ç»ç¨åº¦åºå« <br>
	 * ç´è¾å¸ãå°çº§å¸æ3å°4çº§çè¯¦ç»ç¨åº¦ï¼çãå¨å½æ²¡æè¯¦ç»ç¨åº¦åºå« <br>
	 * æè¯¦ç»ç¨åº¦åºå«ä¸ºtrueï¼æ²¡æè¯¦ç»ç¨åº¦åºåä¸ºfalse
	 * 
	 * @param tofPath
	 * @return
	 */
	public static boolean IsScale(String tofPath) {
		String region = tofPath.split("/")[tofPath.split("/").length - 1];
		boolean flag1 = region.indexOf("0000") >= 0;
		boolean flag2 = region.indexOf("11") >= 0 || region.indexOf("12") >= 0
				|| region.indexOf("31") >= 0 || region.indexOf("50") >= 0;// å¤æ­æ¯å¦æ¯ç´è¾å¸
		boolean flag = !flag1 || flag2;
		return flag;
	}
	/**
	 * éè¿åå¼å¤æ­è¯¥ææ æ¯å¦åºè¯¥ç¨æ´æ°è¡¨ç¤º
	 * @param values
	 * @return
	 */
	public static boolean IsIntegerOnly(double[] values)
	{
		double sum = 0;
		for (int i = 0; i < values.length; i++)
			sum = sum + Math.abs(values[i] - (int)(values[i] + 0.5));
		boolean flag = sum < EPS_CLOSEINTEGER;
		return flag;
	}
	/**
	 * éè¿åå¼å¤æ­è¯¥æ°æ¯å¦åºè¯¥ç¨æ´æ°è¡¨ç¤º
	 * @param value
	 * @return
	 */
	public static boolean IsInteger(double value)
	{
		double eps = Math.abs(value - (int)(value + 0.5));
		boolean flag = eps < EPS_CLOSEINTEGER;
		return flag;
	}
	/**
	 * è¿åèå¥å¤çåçå¼å­ç¬¦ä¸²
	 * <br>å°æ°åçåè³å°2ä½æ°å­ï¼æ´æ°åçåä¸ºæ´æ°
	 * @param tipValue éè¦è¿è¡èå¥å¤ççtipå¼
	 * @return
	 */
	public static String GetDecimalTipValueStr(double tipValue)
	{
		String str = "";
		boolean flag = false;
		double value = tipValue;
		if (value < 0) {
			value = - value;
			flag = true;
		}
		if (IsInteger(value))
			str = (int)(value + 0.5) + "";
		else
		{
			if (value >= 10)//20140821
				str = (int)(value + 0.5) + "";
			else if (value >= 1)
				str = (int)(value * 10 + 0.5) / 10d + "";
			else
				str = (int)(value * 100 + 0.5) / 100d + "";
		}
		if (flag)
			str = "-" + str;
		return str;
	}
	/**
	 * è¿åèå¥å¤çåçå¼ï¼å½å¤§äº4ä½æ°å­æ¶ï¼å4ä½æ°å­
	 * @param tipValue éè¦è¿è¡èå¥å¤ççtipå¼
	 * @return
	 */
	public static double GetDecimalTipValue(double tipValue) {
		boolean flag = false;
		double value = tipValue;
		if (value < 0) {
			value = -value;
			flag = true;
		}
		double a = 0;
		if (value >= 1000)
			a = (int) (value + 0.5);
		else if (value >= 100)
			a = (int) (value * 10 + 0.5) / 10d;
		else if (value >= 10)
			a = (int) (value * 100 + 0.5) / 100d;
		else if (value >= 1)
			a = (int) (value * 1000 + 0.5) / 1000d;
		else
			a = (int) (value * 10000 + 0.5) / 10000d;
		if (flag) {
			a = -a;
		}
		return a;
	}
	
	
	/**
	 * å é¤Regionåè¾¹ç0
	 * @param region
	 * @return
	 */
	public static String CutZero(String region)
	{
		String str = "";
		int temp = Integer.parseInt(region);
		if(temp % 1000000 == 0)
			str = "%";
		else if (temp % 10000 == 0)
			str = temp / 10000 + "%";
		else if (temp % 100 == 0)
			str = temp / 100 + "%";
		else
			str = region;
		return str;
	}
	/**
	 * è·åä¸é¢æ°æ®
	 * @param
	 * @return
	 * @throws SQLException 
	 */
	public static IndicatorData[] getIndicatorData(String thematicData,ChartDataPara chartDataPara,String regionParam,String year)
	{
		ArrayList<IndicatorData> list = new ArrayList<IndicatorData>();
		ReadThematicData rThematicData = new ReadThematicData(thematicData,regionParam,year); //读取专题数据数据的类

		ReadRegionData.doReadRegionData(regionParam);//读取区域地理定位点的类
//		if(!ReadRegionData.getRegonParam().equals(regionParam)){
//			ReadRegionData.doReadRegionData(regionParam);//读取区域地理定位点的类
//		}

//		String[] regionCodes = regionData.getRegonCode();//地理编码表的GeoCode
		String[] regionCodes = ReadRegionData.getRegonCode();

		String[] datasStrings = rThematicData.getData();//专题数据表中的指标值
		String dataName = rThematicData.getDataName();//指标名
		
//		String[] yearData = rThematicData.getYearData();//专题数据表中年份数据
		String[] regonData = rThematicData.getRegonData();//专题数据表中GeoCode

		String[] timeStrings = chartDataPara.getDomainAxis();
		
		for (int i = 0; i < regionCodes.length; i++) {
			for (int j = 0; j < regonData.length; j++) {
				String[] dataNameStrings = dataName.split(",");
				if (regionCodes[i].equals(regonData[j])) {
					String[] tempStrings = datasStrings[j].split(",");
					double[] value = new double[tempStrings.length];
					for (int j2 = 0; j2 < value.length; j2++) {
						value[j2] = Double.parseDouble(tempStrings[j2]);
					}
//					for (int k = 0; k < timeStrings.length; k++) {
//						if (timeStrings[k].equals(yearData[j])) {
//							IndicatorData indicatorData = new IndicatorData(yearData[j], dataNameStrings, value);
//							list.add(indicatorData);
//						}
//
//					}
					IndicatorData indicatorData = new IndicatorData(year, dataNameStrings, value);
					list.add(indicatorData);
				}
			}
		}		
		list.trimToSize();
		IndicatorData[] indicatorDatas = (IndicatorData[])list.toArray(new IndicatorData[1]);
		if (indicatorDatas.length!=regionCodes.length) {
			System.out.println("缺失该年份数据");
		}
		return 	indicatorDatas;
	
	}

	//获取指标的中文名和单位
	public static String getCnNameAndUnit(String enName) {
		String cnName = null; //中文名
		String unit = null;  //单位
		String source = null;  //数据来源
		String sqlString = "SELECT NAME_CN,UNIT,SOURCE FROM index_dictionary WHERE NAME_EN = '" +enName +"'";
		
		JConnection jConnection = new JConnection();
		Connection connection = jConnection.getConnection();
		ResultSet resultSet = null;
		try {
			PreparedStatement pps = connection.prepareStatement(sqlString);
			resultSet = pps.executeQuery();
			while (resultSet.next()) {
				cnName = resultSet.getString(1);
				unit = resultSet.getString(2);
				source = resultSet.getString(3);
			}
			pps.close();
//			jConnection.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			jConnection.close();
		}
		
		String cnNameAndUnit = cnName+","+unit+","+source;
		
		return cnNameAndUnit;
		
	}
	
	public static String getCnname(String chartDataString){
		String[] temp = chartDataString.split(",");
		String[] enNames = new String[temp.length];
		for (int i = 0; i < temp.length; i++) {
			enNames[i] = temp[i];
		}
		String cnNames ="";
		for (int i = 0; i < enNames.length; i++) {
			cnNames += getCnNameAndUnit(enNames[i]).split(",")[0]+";";
		}
		cnNames = cnNames.substring(0,cnNames.length()-1);
		return cnNames;
	}
	
	
	public static double[] maxValues(IndicatorData[] indicatorDatas)
	{
		double[] maxValues = new double[indicatorDatas[0].getValues().length];
		for(int i = 0; i < indicatorDatas.length; i++)
		{
			double[] values = indicatorDatas[i].getValues();
			for(int j = 0; j < values.length; j++)
			{
				if(maxValues[j] < values[j])
					maxValues[j] = values[j];
			}
		}
		return maxValues;
	}
	
	public static double[] minValues(IndicatorData[] indicatorDatas)
	{
		double[] minValues = new double[indicatorDatas[0].getValues().length];
		for(int i = 0; i < indicatorDatas.length; i++)
		{
			double[] values = indicatorDatas[i].getValues();
			for(int j = 0; j < values.length; j++)
			{
				if(minValues[j] > values[j])
					minValues[j] = values[j];
			}
		}
		return minValues;
	}
	
	public static double[] averageValues(IndicatorData[] indicatorDatas)
	{
		double[] averageValues = new double[indicatorDatas[0].getValues().length];
		for(int i = 0; i < indicatorDatas.length; i++)
		{
			double[] values = indicatorDatas[i].getValues();
			for(int j = 0; j < values.length; j++)
				averageValues[j] = averageValues[j] + values[j];
		}
		for(int i = 0; i < averageValues.length; i++)
			averageValues[i] = averageValues[i] / indicatorDatas.length;
		return averageValues;
	}
	
	public static double[] scales(IndicatorData[] indicatorDatas, double width)
	{
		double[] scales = new double[1];
		scales[0] = 0;//åå§å
		for(int i = 0; i < indicatorDatas.length; i++)
		{
			double[] values = indicatorDatas[i].getValues();
			double max = 0;
			for(int j = 0; j < values.length; j++)
				max = max + values[j];
			if(max > scales[0])
				scales[0] = max;
		}
		scales[0] = scales[0] / width;
		return scales;
	}

	public static Rectangle2D.Double transRecFloatToDouble(Rectangle2D.Float rec1){
		Rectangle2D.Double rec2 = new Rectangle2D.Double();
		
		float x,y,w,h;
		x = rec1.x;
		y = rec1.y;
		w = rec1.width;
		h = rec1.height;
		
		Float X,Y,W,H;
		X = new Float(x);
		Y = new Float(y);
		W = new Float(w);
		H = new Float(h);
		
		rec2.x = X.doubleValue();
		rec2.y = Y.doubleValue();
		rec2.width = W.doubleValue();
		rec2.height = H.doubleValue();
		
		return rec2;
	}

	public static Rectangle2D.Double CalDCSize(String paperSize, int dpi) {
		Rectangle2D.Double DC;
		double width = 0.0;
		double height = 0.0;
		if (paperSize.equals("A4")) {
			width = Arith.roundTo(297 * dpi / MMPERINCH, 0);
			height = Arith.roundTo(210 * dpi / MMPERINCH, 0);
		}
		DC = new Rectangle2D.Double(0, 0, width, height);
		return DC;
	}

	public static HashMap<String, Color> getColorMap(int num,Color[] modelColor, 
			int modelGrade, int b,String gradeDataString) throws BiffException, IOException	
	{
		HashMap<String, Color> hashMap = new HashMap<String, Color>();
		ReadRegionData regionData = new ReadRegionData();
		String[] geoCode = null;
		double[] values = null;
		String[] tempCV = getCodeAndValue(gradeDataString);
		
		int row = tempCV.length;
		geoCode = new String[row];
		values = new double[row];
		
		for (int i = 0; i < tempCV.length; i++) {
			geoCode[i] = tempCV[i].split(",")[0];
			values[i] = Double.parseDouble(tempCV[i].split(",")[1]);
		}
		Color[] fillColor = getColor(values, modelColor, modelGrade, num, b);

		for (int i = 0; i < fillColor.length; i++) {
			hashMap.put(geoCode[i], fillColor[i]);
//			System.out.println(geoCode[i]);
		}
		return hashMap;
	}
	
	private static String[] getCodeAndValue(String gradeDataString) 
	{
		String[] values = null;
		String[] code = null;
		String[] codeAndValue = null;
		String rownum = null;
		JConnection jConnection  = new JConnection();
		Connection connection = jConnection.getConnection();
		
//		ConnOrcl connOrcl = new ConnOrcl();
//		Statement statement = connOrcl.getStmt();
		ResultSet resultSet = null;
		String[] dataStrings = gradeDataString.split(",");
		String tableString = dataStrings[0];
		String colString = dataStrings[1];
		String yearString = dataStrings[2];
//		String rcString = "REGION_CODE";
		String sql1 = "SELECT COUNT(*) FROM " + tableString+" WHERE YEAR = "+yearString;
		String sql2 = "SELECT REGION_CODE," +colString+ " FROM " + tableString+" WHERE YEAR = "+yearString;
		PreparedStatement pps1;
		try {
			pps1 = connection.prepareStatement(sql1);
			resultSet = pps1.executeQuery();
			while (resultSet.next()) {
				rownum = resultSet.getString(1);
			}
			
			pps1.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int row = Integer.parseInt(rownum);
		values = new String[row];
		code = new String[row];
		codeAndValue = new String[row];
		PreparedStatement pps2;
		try {
			pps2= connection.prepareStatement(sql2);
			resultSet = pps2.executeQuery();
			int i = 0;
			while (resultSet.next()) {
				code[i] = resultSet.getString(1);
				values[i] = resultSet.getString(2);

				if (values[i].length()==0) {
					values[i]="0";
				}
				codeAndValue[i] = code[i]+","+values[i];
				i++;
				
			}
			
			pps2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		jConnection.close();
		
		return codeAndValue;
		
		
		
	}
	
	private static double[] getValue(String gradeDataString){
	
		String[] codeAndValue = getCodeAndValue(gradeDataString);
		double[] values = new double[codeAndValue.length];
		for (int i = 0; i < codeAndValue.length; i++) {
			values[i] = Double.parseDouble(codeAndValue[i].split(",")[1]);
		}
		return values;
	}
	
	public static Color[] getColor(double[] value, Color[] modelColor, int modelGrade, int num,int b)
	{
		int[] boundary = null;
		if(modelGrade == 1 || modelGrade > 8)
			boundary = ModelPrim.modelA0(value, num);
		if(modelGrade == 2)
			boundary = ModelPrim.modelA1(value, num);
		if(modelGrade == 3)
			boundary = ModelPrim.modelB0(value, num);
		if(modelGrade == 4)
			boundary = ModelPrim.modelB1(value, num);
		if(modelGrade == 5)
			boundary = ModelPrim.modelC0(value, num);
		if(modelGrade == 6)
			boundary = ModelPrim.modelC1(value, num);
		if(modelGrade == 7)
			boundary = ModelPrim.modelD0(value, num);
		if(modelGrade == 8)
			boundary = ModelPrim.modelD1(value, num);
		
//		String bo = "";
//		for (int i = 0; i < boundary.length; i++) {
//			bo += boundary[i];
//		}
//		System.out.println(bo);
		
		Color[] fillColor = new Color[value.length];
		for(int i = 0; i < value.length; i++)
		{
			for(int j = 0; j < boundary.length - 1; j++)
			{
				if(value[i] >= boundary[j] && value[i] < boundary[j + 1])
					if (b==1) {
						fillColor[i] = modelColor[(int) (j * 10d / num)];
//						System.out.println("value="+value[i]+"&"+"boundary = "+boundary[j]);
					} else {
						fillColor[i] = modelColor[(int) (1.0 * j * modelColor.length / (boundary.length-1) + 0.5)];//å¡«åè²
//						System.out.println("value="+value[i]+"&"+"boundary = "+boundary[j]);
					}			
			}
		}
		return fillColor;
	}
	
	public static ArrayList<LegendType> getLegendData(int num,Color[] modelColor, 
			int modelGrade, int b,String gradeDataString) throws BiffException, IOException
	{
		ArrayList<LegendType> legendList = new ArrayList<LegendType>(); 
		double[] value = getValue(gradeDataString);
//		Color[] fillColor = getColor(value, modelColor, modelGrade, num, b);
		int[] boundary = null;
		switch(modelGrade)
		{
		case 1:
			boundary = ModelPrim.modelA0(value, num);
		case 2:
			boundary = ModelPrim.modelA1(value, num);
		case 3:
			boundary = ModelPrim.modelB0(value, num);
		case 4:
			boundary = ModelPrim.modelB1(value, num);
		case 5:
			boundary = ModelPrim.modelC0(value, num);
		case 6:
			boundary = ModelPrim.modelC1(value, num);
		case 7:
			boundary = ModelPrim.modelD0(value, num);
		case 8:
			boundary = ModelPrim.modelD1(value, num);
		default:
			boundary = ModelPrim.modelA1(value, num);	
		}
		
		Color[] fillColor = new Color[boundary.length - 1];
		for(int i = 0; i < boundary.length - 1; i++)
		{
			int j =0;
			if(num <= 4){
				 j = (int)(i * 4d / num);
			}else {
				 j = (int)(i * 10d / num);
			}
			
			fillColor[i] = modelColor[j];
		}

		
//		Color[] fillColor = new Color[value.length];
//		for(int i = 0; i < value.length; i++)
//		{
//			for(int j = 0; j < boundary.length-1 ; j++)
//			{
//				if(value[i] >= boundary[j] && value[i] < boundary[j + 1])
//					if (b==1) {
//						fillColor[i] = modelColor[(int) (j * 10d / num)];
//					} else {
//						fillColor[i] = modelColor[(int) (1.0 * j * modelColor.length / (boundary.length-1) + 0.5)];//å¡«åè²
//					}			
//			}
//		}
		
		for(int i = 0; i < boundary.length-1; i++)
		{
			LegendType temp = new LegendType();
			temp.startBound = boundary[i];
			temp.endBound = boundary[i + 1];
			temp.color = fillColor[i];
			legendList.add(temp);
		}
		return legendList;
	}
	public static BufferedImage getLegendMap(int num,Color[] modelColor, 
			int modelGrade, int b,String gradeDataString, int imageWidth, int imageHeight) throws BiffException, IOException
	{
		imageWidth=GradeImgWidth;
//		imageHeight=200;
		if (imageHeight<200) {
			imageHeight = 200;
		}
		System.out.println("GradeImgHeigt:"+imageHeight);
		String[] gradeDatas = gradeDataString.split(",");
		String tableString = gradeDatas[0];
		String colString = gradeDatas[1];
		String yearString = gradeDatas[2];
		
		String zhKind = getCnname(tableString);
		String nameAndUnit = getCnNameAndUnit(colString);
		String nameString = nameAndUnit.split(",")[0];
		String unitString = nameAndUnit.split(",")[1];
		
		String titleString = yearString+"年"+zhKind+nameString; //eg:2011年旱灾受灾人口占本省人口的比例
		String titleUnit = "单位："+unitString;//eg：单位：%
		
		
		ArrayList<LegendType> legendList = getLegendData(num, modelColor, modelGrade,b,gradeDataString);
		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.createGraphics();
		Graphics2D g2d = (Graphics2D)g;
		
		//背景设为白色
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, imageWidth, imageHeight);
		
		//当前字体的字体规格
		g2d.setColor(Color.BLACK);
		FontMetrics fm = g2d.getFontMetrics();
		int fontWidth, fontAscent,fontHeight;	
		g2d.setFont(new Font("黑体", Font.PLAIN, 15));
		fontWidth = fm.stringWidth(titleString);
		fontHeight = fm.getHeight();
//		System.out.println("字宽："+fontWidth);
		g2d.drawString(titleString, 0, fontHeight);
		g2d.drawString(titleUnit, fontWidth/2, fontHeight*2);
		
				
		g2d.setFont(new Font("楷体", Font.PLAIN, 10));
		g2d.setColor(Color.BLACK);
		
		int recx = 5;
		int recy = fontHeight*2;
		
		int gap = 10;
		int recWidth = imageWidth/6*3;
		int recHeight = imageHeight/legendList.size()/2;
		
		
		double det = (double)imageHeight / legendList.size()/2;
		int det2 = imageWidth * 2 / 5;
		for(int i = 0; i < legendList.size(); i++)
		{
			g2d.setColor(legendList.get(i).color);
//			g2d.fillRect((int)(det2 / 6d), (int)(det * i + det / 6), (int)(det2 * 2 / 3d), (int)(det));
			g2d.fillRect(recx, recy+gap+(gap+recHeight)*i, recWidth, recHeight);
			g2d.setColor(Color.BLACK);
			String temp = legendList.get(i).startBound + "~" + legendList.get(i).endBound;
			fontWidth = fm.stringWidth(temp);
			fontAscent = fm.getAscent();
//			g2d.drawString(temp, (det2 * 3 / 2 - fontWidth) / 2 + det2, (int)((i + 0.5) * det + fontAscent / 2));
			g2d.drawString(temp, recWidth+gap*2, recy+gap+(gap+recHeight)*i + fontAscent);
		}
		return image;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException, SQLException{
//		Color color1 = new Color(64, 163, 220,1);
//		Color color2 = new Color(146, 174, 69,1);
//		Color color3 = new Color(167, 156, 203,1);
//		Color color4 = new Color(193, 203, 129,1);
//		Color color5 = new Color(239, 141, 106,1);
//		Color color6 = new Color(179,155, 119,1);
//		Color colors[] = {color1,color2,color3,color4,color5,color6};
//		String colorsString = "";
//		for (int i = 0; i < colors.length; i++) {
//			int rgb = colors[i].getRGB();
//			colorsString+=rgb+",";
//		}
//		System.out.println(colorsString);
//		System.out.println("ok");
		String testString = JUtil.getCnname("HL,RK_HSZ,RK_SW,RK_SZ,RK_ZY");
		System.out.println(testString);
	}
}

