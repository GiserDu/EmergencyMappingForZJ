package com.zz.bglayer;
import java.math.BigDecimal;

public class Arith {
	private static final int DEF_DIV_SCALE = 10;//小数位数
	private Arith() {
		
	}
	
	public static double add(double v1,double v2){//加法运算
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		return b1.add(b2).doubleValue();
	}
	
	public static double sub(double v1,double v2){//减法运算
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		return b1.subtract(b2).doubleValue();
	}
	
	public static double mul(double v1,double v2){//乘法运算
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		return b1.multiply(b2).doubleValue();
	}
	
	public static double div(double v1,double v2){//除法运算
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		return b1.divide(b2,DEF_DIV_SCALE,BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static double roundTo(double num,int newScale){//四舍五入
		return BigDecimal.valueOf(num).setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public static void main(String[] args){
//		System.out.println(Arith.add(0.05,0.01));
//		System.out.println(Arith.sub(1.0,0.42));
//		System.out.println(Arith.mul(4.015,100));
//		System.out.println(Arith.div(123.3,100));
//		System.out.println(Arith.roundTo(123.55,1));
		
	}

}
