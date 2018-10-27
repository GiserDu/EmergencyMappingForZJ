package telecarto.data.util;

import java.awt.Color;

/**
 * 使用的颜色色系有： 黄红色系， 蓝色色系，红色色系，黄绿色系，黄棕色系，青黄色系
 * @author Tianqin
 *
 */
public class ColorUtil {
	public static String getColorScheme(int count,String colorRamp){
		System.out.println("corlor ramp:" + colorRamp);
		switch(count){
		case 1:
			return get1Color(colorRamp);
		case 2:
			return get2Color(colorRamp);
		case 3:
			return get3Color(colorRamp);
		case 4:
			return get4Color(colorRamp);
		case 5:
			return get5Color(colorRamp);
		case 6:
			return get6Color(colorRamp);
		case 7:
			break;
		case 8:
			break;
		}
		return null;
	}
	
	
	private static String get1Color(String colorRamp){
		int color;
		switch (colorRamp) {		
		case "黄红色系":
			color= rgb2int(new Color(255,250,137));
			break;
		case "蓝色色系":
			color= rgb2int(new Color(127,200,242));	
			break;
		case "红色色系":
			color= rgb2int(new Color(242,161,195));	
			break;
		case "黄绿色系":
			color= rgb2int(new Color(255,250,137));
			break;
		case "黄棕色系":
			color= rgb2int(new Color(255,251,164));
			break;
		case "青黄色系":
			color= rgb2int(new Color(237,27,36));
			break;
		default:
			return null;
		}
		return Integer.toString(color);
	}
	
	
	private static String get2Color(String colorRamp){
		String colorStr;
		switch (colorRamp) {		
		case "黄红色系":
			colorStr= rgb2Str(new Color(255,250,137));
			colorStr += ";"+rgb2Str(new Color(241,165,75));
			break;
		case "蓝色色系":
			colorStr= rgb2Str(new Color(127,200,242));	
			colorStr += ";"+rgb2Str(new Color(0,161,227));
			break;
		case "红色色系":
			colorStr= rgb2Str(new Color(242,161,195));	
			colorStr += ";"+rgb2Str(new Color(228,79,143));
			break;
		case "黄绿色系":
			colorStr= rgb2Str(new Color(255,250,137));
			colorStr += ";"+rgb2Str(new Color(143,201,93));
			break;
		case "黄棕色系":
			colorStr= rgb2Str(new Color(255,251,164));
			colorStr += ";"+rgb2Str(new Color(255,247,51));
			break;
		case "青黄色系":
			colorStr= rgb2Str(new Color(237,27,36));
			colorStr += ";"+rgb2Str(new Color(38,33,100));
			break;
		default:
			return null;
		}
		return colorStr;
	}
	
	private static String get3Color(String colorRamp){
		String colorStr;
		switch (colorRamp) {		
		case "黄红色系":
			colorStr= rgb2Str(new Color(255,253,201));
			colorStr += ";"+rgb2Str(new Color(252,228,116));
			colorStr += ";"+rgb2Str(new Color(233,129,70));
			break;
		case "蓝色色系":
			colorStr= rgb2Str(new Color(196,229,250));	
			colorStr += ";"+rgb2Str(new Color(86,187,237));
			colorStr += ";"+rgb2Str(new Color(0,145,211));
			break;
		case "红色色系":
			colorStr= rgb2Str(new Color(250,210,228));	
			colorStr += ";"+rgb2Str(new Color(237,136,178));
			colorStr += ";"+rgb2Str(new Color(212,46,125));
			break;
		case "黄绿色系":
			colorStr= rgb2Str(new Color(255,253,201));
			colorStr += ";"+rgb2Str(new Color(228,237,121));
			colorStr += ";"+rgb2Str(new Color(88,181,97));
			break;
		case "黄棕色系":
			colorStr= rgb2Str(new Color(255,253,213));
			colorStr += ";"+rgb2Str(new Color(255,250,137));
			colorStr += ";"+rgb2Str(new Color(241,232,0));
			break;
		case "青黄色系":
			colorStr= rgb2Str(new Color(237,27,36));
			colorStr += ";"+rgb2Str(new Color(38,33,100));
			colorStr += ";"+rgb2Str(new Color(241, 241,0));
			break;
		default:
			return null;
		}
		return colorStr;
	}
	
	private static String get4Color(String colorRamp){
		String colorStr;
		switch (colorRamp) {		
		case "黄红色系":
			colorStr= rgb2Str(new Color(255,251,177));
			colorStr += ";"+rgb2Str(new Color(252,228,116));
			colorStr += ";"+rgb2Str(new Color(241,165,75));
			colorStr += ";"+rgb2Str(new Color(228,102,66));
			break;
		case "蓝色色系":
			colorStr= rgb2Str(new Color(164,215,246));	
			colorStr += ";"+rgb2Str(new Color(86,187,237));
			colorStr += ";"+rgb2Str(new Color(0,161,227));
			colorStr += ";"+rgb2Str(new Color(0,131,186));
			break;
		case "红色色系":
			colorStr= rgb2Str(new Color(246,186,211));	
			colorStr += ";"+rgb2Str(new Color(237,136,178));
			colorStr += ";"+rgb2Str(new Color(228,79,143));
			colorStr += ";"+rgb2Str(new Color(188,47,115));
			break;
		case "黄绿色系":
			colorStr= rgb2Str(new Color(255,251,177));
			colorStr += ";"+rgb2Str(new Color(228,237,121));
			colorStr += ";"+rgb2Str(new Color(143,201,93));
			colorStr += ";"+rgb2Str(new Color(18,169,98));
			break;
		case "黄棕色系":
			colorStr= rgb2Str(new Color(255,252,189));
			colorStr += ";"+rgb2Str(new Color(255,250,137));
			colorStr += ";"+rgb2Str(new Color(255,247,51));
			colorStr += ";"+rgb2Str(new Color(209,202,0));
			break;
		case "青黄色系":
			colorStr= rgb2Str(new Color(237,27,36));
			colorStr += ";"+rgb2Str(new Color(38,33,100));
			colorStr += ";"+rgb2Str(new Color(241, 241,0));
			colorStr += ";"+rgb2Str(new Color(33,178,75));
			break;
		default:
			return null;
		}
		return colorStr;
	}
	
	private static String get5Color(String colorRamp){
		String colorStr;
		switch (colorRamp) {		
		case "黄红色系":
			colorStr= rgb2Str(new Color(255,253,201));
			colorStr += ";"+rgb2Str(new Color(255,250,137));
			colorStr += ";"+rgb2Str(new Color(247,197,95));
			colorStr += ";"+rgb2Str(new Color(233,129,70));
			colorStr += ";"+rgb2Str(new Color(201,81,61));
			break;
		case "蓝色色系":
			colorStr= rgb2Str(new Color(196,229,250));
			colorStr += ";"+rgb2Str(new Color(127,200,242));
			colorStr += ";"+rgb2Str(new Color(19,174,233));
			colorStr += ";"+rgb2Str(new Color(0,145,211));
			colorStr += ";"+rgb2Str(new Color(0,112,155));
			break;
		case "红色色系":
			colorStr= rgb2Str(new Color(250,210,228));	
			colorStr += ";"+rgb2Str(new Color(242,161,195));
			colorStr += ";"+rgb2Str(new Color(233,111,161));
			colorStr += ";"+rgb2Str(new Color(212,46,125));
			colorStr += ";"+rgb2Str(new Color(158,43,101));
			break;
		case "黄绿色系":
			colorStr= rgb2Str(new Color(255,253,201));
			colorStr += ";"+rgb2Str(new Color(255,250,137));
			colorStr += ";"+rgb2Str(new Color(186,219,107));
			colorStr += ";"+rgb2Str(new Color(88,181,97));
			colorStr += ";"+rgb2Str(new Color(0,147,91));
			break;
		case "黄棕色系":
			colorStr= rgb2Str(new Color(255,253,213));
			colorStr += ";"+rgb2Str(new Color(255,251,164));
			colorStr += ";"+rgb2Str(new Color(255,248,106));
			colorStr += ";"+rgb2Str(new Color(241,232,0));
			colorStr += ";"+rgb2Str(new Color(171,167,26));
			break;
		case "青黄色系":
			colorStr= rgb2Str(new Color(237,27,36));
			colorStr += ";"+rgb2Str(new Color(38,33,100));
			colorStr += ";"+rgb2Str(new Color(241, 241,0));
			colorStr += ";"+rgb2Str(new Color(33,178,75));
			colorStr += ";"+rgb2Str(new Color(146,39,143));
			break;
		default:
			return null;
		}
		return colorStr;
	}
	
	private static String get6Color(String colorRamp){
		String colorStr;
		switch (colorRamp) {		
		case "黄红色系":
			colorStr= rgb2Str(new Color(255,254,227));
			colorStr += ";"+rgb2Str(new Color(255,253,201));
			colorStr += ";"+rgb2Str(new Color(255,250,137));
			colorStr += ";"+rgb2Str(new Color(247,197,95));
			colorStr += ";"+rgb2Str(new Color(233,129,70));
			colorStr += ";"+rgb2Str(new Color(201,81,61));
			break;
		case "蓝色色系":
			colorStr= rgb2Str(new Color(229,243,252));
			colorStr += ";"+rgb2Str(new Color(196,229,250));
			colorStr += ";"+rgb2Str(new Color(127,200,242));
			colorStr += ";"+rgb2Str(new Color(19,174,233));
			colorStr += ";"+rgb2Str(new Color(0,145,211));
			colorStr += ";"+rgb2Str(new Color(0,112,155));
			break;
		case "红色色系":
			colorStr= rgb2Str(new Color(253,235,242));	
			colorStr += ";"+rgb2Str(new Color(250,210,228));
			colorStr += ";"+rgb2Str(new Color(242,161,195));
			colorStr += ";"+rgb2Str(new Color(233,111,161));
			colorStr += ";"+rgb2Str(new Color(212,46,125));
			colorStr += ";"+rgb2Str(new Color(158,43,101));
			break;
		case "黄绿色系":
			colorStr= rgb2Str(new Color(255,254,227));
			colorStr += ";"+rgb2Str(new Color(255,253,201));
			colorStr += ";"+rgb2Str(new Color(255,250,137));
			colorStr += ";"+rgb2Str(new Color(186,219,107));
			colorStr += ";"+rgb2Str(new Color(88,181,97));
			colorStr += ";"+rgb2Str(new Color(0,147,91));
			break;
		case "黄棕色系":
			colorStr= rgb2Str(new Color(255,254,236));
			colorStr += ";"+rgb2Str(new Color(255,253,213));
			colorStr += ";"+rgb2Str(new Color(255,251,164));
			colorStr += ";"+rgb2Str(new Color(255,248,106));
			colorStr += ";"+rgb2Str(new Color(241,232,0));
			colorStr += ";"+rgb2Str(new Color(171,167,26));
			break;
		case "青黄色系":
			colorStr= rgb2Str(new Color(237,27,36));
			colorStr += ";"+rgb2Str(new Color(38,33,100));
			colorStr += ";"+rgb2Str(new Color(241, 241,0));
			colorStr += ";"+rgb2Str(new Color(33,178,75));
			colorStr += ";"+rgb2Str(new Color(146,39,143));
			colorStr += ";"+rgb2Str(new Color(241, 139, 36));
			break;
		default:
			return null;
		}
		return colorStr;
	}
	
	private static int rgb2int(Color c){
		return c.getBlue()+c.getGreen()*256+c.getRed()*256*256;
	}
	
	private static String rgb2Str(Color c){
		return Integer.toString(rgb2int(c));
	}
	
}
