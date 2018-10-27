package com.scheme;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.zz.util.JUtil;



public class ChartColorScheme {
	private HashMap<Integer, Color[]> hashMapColor = new HashMap<Integer, Color[]>();
	private HashMap<Integer, String> hashMapName = new HashMap<Integer, String>();
	private final String PATH = "prop/ChartColorScheme.xml";

	@SuppressWarnings("unchecked")
	private void read(String path) {
		SAXReader saxReader = new SAXReader();
		Document doc;
		String dir = JUtil.GetWebInfPath();// 当前目录
		InputStream is = null;
		try {
			is = new FileInputStream(new File(dir + path));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			doc = saxReader.read(is);
			Element root = doc.getRootElement();
			Iterator<Element> colorIterator = root.elementIterator("color");
			while (colorIterator.hasNext()) {
				Element tempElement = colorIterator.next();
				int id = Integer.parseInt(tempElement.attributeValue("id"));
				String name = tempElement.attributeValue("name");
				hashMapName.put(id, name);
				hashMapName.put(id + 10, name);// 此处修改
				Iterator<Element> valueIterator = tempElement
						.elementIterator("value");
				if (id < 10000)
					System.out.println(id);
				int length = id / 10000;
				Color[] tempColors = new Color[length];
				Color[] tempColors2 = new Color[length];
				int j = 0;
				while (valueIterator.hasNext()) {
					Element temp2Element = valueIterator.next();
					String tempString = temp2Element.getStringValue();
					Color tempColor = stringToColor(tempString);
					if (tempColor == null)
						System.out.println(id);
					// System.out.println(id);
					tempColors[j] = tempColor;
					tempColors2[length - 1 - j] = tempColor;
					j++;
				}
				hashMapColor.put(id, tempColors);
				hashMapColor.put(id + 10, tempColors2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Color[] getColors(int key) {
		Color[] colors = hashMapColor.get(key);
		return colors;
	}

	/**
	 * @param key
	 * @return 供ChartStyle使用的SectionColorMap
	 */
	public HashMap<Integer, Color> getSectionColorMap(int key) {
		HashMap<Integer, Color> sectionColorMap = new HashMap<Integer, Color>();
		Color[] colors = hashMapColor.get(key);
		for (int i = 0; i < colors.length; i++)
			sectionColorMap.put(i, colors[i]);
		return sectionColorMap;
	}

	private Color stringToColor(String colorString) {
		String[] curStrings = colorString.split(",");
		if (curStrings.length != 3) {
			System.out.println(colorString);
			return null;
		}
		int r = Integer.parseInt(curStrings[0]);
		int g = Integer.parseInt(curStrings[1]);
		int b = Integer.parseInt(curStrings[2]);
		if (r > 255 || g > 255 || b > 255 || r * g * b < 0) {
			System.out.println(colorString);
			return null;
		}
		Color curColor = new Color(r, g, b);
		return curColor;
	}

	public ChartColorScheme() {
		read(PATH);
	}

	public ChartColorScheme(String path) {
		read(path);
	}

	public String getPATH() {
		return PATH;
	}

	public HashMap<Integer, Color[]> getHashMapColor() {
		return hashMapColor;
	}

	public void setHashMapColor(HashMap<Integer, Color[]> hashMapColor) {
		this.hashMapColor = hashMapColor;
	}

	public HashMap<Integer, String> getHashMapName() {
		return hashMapName;
	}

	public void setHashMapName(HashMap<Integer, String> hashMapName) {
		this.hashMapName = hashMapName;
	}
}