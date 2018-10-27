package com.zz.chart.chartfactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.zz.util.JUtil;

/**
 * 符号类名获取类
 * 
 * @author lmk
 */
public class JClassName {
	public String getChartStyleClassName(String chartID) {
		return load(JUtil.GetWebInfPath() + "/prop/chartstyle.properties",
				chartID);
	}

	public String getChartClassName(String chartID) {
		return load(JUtil.GetWebInfPath() + "/prop/chart.properties", chartID);
	}

	private String load(String pathprop, String chartID) {
		Properties prop = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(pathprop);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			prop.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop.getProperty(chartID);
	}
}