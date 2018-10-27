package com.zz.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Transform
{
	private static double EPS_DISTANCE = 1e-7;
	//把int[] 数组转为 double[] 数组
	public static double[] Int2Double(int[] a)
	{
		double[] b = new double[a.length];
		for(int i = 0; i < b.length; i++)
		{
			b[i] = (double)a[i];
		}
		return b;
	}
	//把float[] 数组转为double[] 数组
	public static double[] Float2Double(float[] a)
	{
		double[] b = new double[a.length];
		for(int i = 0; i < b.length; i++)
		{
			b[i] = (double)a[i];
		}
		return b;
	}
	//把int[] 数组转为float[] 数组
	public static float[] Int2Float(int[] a)
	{
		float[] b = new float[a.length];
		for(int i = 0; i < b.length; i++)
		{
			b[i] = (float)a[i];
		}
		return b;
	}
	//把ArrayList<Integer> 转为 int[]
	@SuppressWarnings("unchecked")
	public static int[] List2Int(ArrayList<Integer> list)
	{
		int[] a = new int[list.size()];
		for(int i = 0; i < a.length; i++)
		{
			a[i] = ((ArrayList<Integer>)list.clone()).get(i);
		}
		return a;
	}
	
	//把ArrayList<Float> 转为 float[]
	@SuppressWarnings("unchecked")
	public static float[] List2Float(ArrayList<Float> list)
	{
		float[] a = new float[list.size()];
		for(int i = 0; i < a.length; i++)
		{
			a[i] = ((ArrayList<Float>)list.clone()).get(i);
		}
		return a;
	}
	
	//把ArrayList<Double> 转为 double[]
	@SuppressWarnings("unchecked")
	public static double[] List2Double(ArrayList<Double> list)
	{
		double[] a = new double[list.size()];
		for(int i = 0; i < a.length; i++)
		{
			a[i] = ((ArrayList<Double>)list.clone()).get(i);
		}
		return a;
	}
	
	//把ArrayList<Point2D.Double> 转为Point2D.Double[]
	public static Point2D.Double[] list2Point2D(ArrayList<Point2D.Double> list)
	{
		Point2D.Double[] point = new Point2D.Double[list.size()];
		for(int i = 0; i < list.size(); i++)
		{
			point[i] = list.get(i);
		}
		return point;
	}
	
	//把int[] 转为 ArrayList<Integer>
	public static ArrayList<Integer> Int2List(int[] a)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 0; i < a.length; i++)
		{
			list.add(a[i]);
		}
		return list;
	}
	
	//把float[] 转为 ArrayList<Float>
	public static ArrayList<Float> Float2List(float[] a)
	{
		ArrayList<Float> list = new ArrayList<Float>();
		for(int i = 0; i < a.length; i++)
		{
			list.add(a[i]);
		}
		return list;
	}
	
	//把double[] 转为 ArrayList<Double>
	public static ArrayList<Double> Double2List(double[] a)
	{
		ArrayList<Double> list = new ArrayList<Double>();
		for(int i = 0; i < a.length; i++)
		{
			list.add(a[i]);
		}
		return list;
	}
	
	//取数组前面num个数值组成另外一个数组
	public static double[] CutPrevious(double[] a, int num)
	{
		double[] b = new double[num];
		for(int i = 0; i < num; i++)
			b[i] = a[i];
		return b;
	}
	
	//取数组后面num个数值组成另外一个数组
	public static double[] CutBehind(double[] a, int num)
	{
		double[] b = new double[num];
		for(int i = 0; i < num; i++)
			b[i] = a[a.length - i - 1];
		return b;
	}
	
//	//返回仿射变换矩阵
//	//wc(用户坐标系)
//	//dc(设备坐标系)
//	public static double[][] TAffineM(double[][] from, double[][] to)
//	{
//		double[][] m = new double[2][3];
//		m[0][0] = (to[0][1] - to[0][0]) / (from[0][1] - from[0][0]);
//		m[1][1] = -(to[1][1] - to[1][0]) / (from[1][1] - from[1][0]);
//		m[0][1] = 0;
//		m[1][0] = 0;
//		m[0][2] = to[0][0] - m[0][0] * from[0][0];
//		m[1][2] = to[1][0] + m[1][1] * from[1][0];
//		return m;
//	}
//	
//	//返回放射变换后的坐标
//	public static Point2D.Double[] TAffineR(Point2D.Double[] from, double[][] m)
//	{
//		Point2D.Double[] to = new Point2D.Double[from.length];
//		for(int i = 0; i < to.length; i++)
//		{
//			to[i].x = m[0][0] * from[i].x + m[0][1] * from[i].y + m[0][2];
//			to[i].y = m[1][0] * from[i].x + m[1][1] * from[i].y + m[1][2];
//		}
//		return to;
//	}
	
	//对已排序数组进行删重处理
	public static ArrayList<Point2D.Double>  RemoveSamePoint(ArrayList<Point2D.Double> a)
	{
		ArrayList<Point2D.Double> b = new ArrayList<Point2D.Double>();
		for(int i = 1 ; i < a.size(); i++)
		{
			double dx = a.get(i).x - a.get(i - 1).x;
			double dy = a.get(i).y - a.get(i - 1).y;
			double d = Math.sqrt(dx * dx + dy * dy);
			if(d < EPS_DISTANCE)
			{
				b.add(a.get(i));
			}
		}
		for(int i = 0; i < b.size(); i++)
		{
			a.remove(b.get(i));
		}
		b.clear();
		return a;
	}	
}
