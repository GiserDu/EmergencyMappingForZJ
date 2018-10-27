package com.zz.chart.obj.custom;

/** 
 * 静态函数类,提供常用的函数方法
 * @author lmk
 * @version 1.0 
 */
public class Util {

	public Util() {

	}

	/**
	 * 色彩转换方法,转换成rgb数组模式
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
		//int g = (color % (256*256))/256; 
		int G = (rgb & 0xff00) >> 8;
		if (G > 256) {
			System.out.println("Wrong color value G:" + G);
			G = 256;
		}
		retval[1] = G;
		//int r = color %(256*256)%256;
		int B = (rgb & 0xff);
		if (B > 256) {
			System.out.println("Wrong color value B: " + B);
			B = 256;
		}
		retval[2] = B;
		return retval;
	}
	
	private static int getTriangleHNum(int num)
	{
		int curNum = num;
		int i = 1;
		int sum = 1;
		while(sum < curNum){
			i++;
			sum = sum + i;
		}
		return i;
	}
	
	private static int getTriangleVNum(int num)
	{
		int curNum = num;
		int i = 0;
		while(curNum > 0)
		{
			curNum = curNum - getTriangleHNum(curNum);
			i++;
		}
		return i;
	}
	
	/**
	 * 三角形排列，返回每行符号个数
	 * @param num
	 * @return int[]
	 */
	public static int[] getTriangleHNums(int num)
	{
		int curNum = num;
		if(curNum == 0)
		{
			int[] temp = new int[1];
			return temp;
		}
		int h = getTriangleVNum(curNum);
		int[] hNums = new int[h];
		for(int i = 0; i < h; i++)
		{
			hNums[h - 1 - i] = getTriangleHNum(curNum);
			curNum = curNum - hNums[h - 1 -i];
		}
		return hNums;
	}
	
	private static int getRectangleVNum(int num)
	{
		int temp = 0;
		if(num >= 4)
			temp = 2;
		else
			temp = 1;
		return temp;		
	}
	
	private static int getRectangleHNum(int num)
	{
		int vNum = getRectangleVNum(num);
		int hNum = 0;
		if(vNum == 1)
			hNum = num;
		if(vNum == 2)
			hNum = (num + 1) / vNum;
		return hNum;
	}
	
	/**
	 * 矩形排列，返回每行符号个数
	 * @param num
	 * @return int[]
	 */
	public static int[] getRectangleHNums(int num)
	{
		int vNum = getRectangleVNum(num);
		int[] hNums = new int[vNum];
		if(vNum == 2)
		{
			hNums[1] = getRectangleHNum(num);
			hNums[0] = num - hNums[1];	
		}
		else
			hNums[0] = num;
		return hNums;
	}
}