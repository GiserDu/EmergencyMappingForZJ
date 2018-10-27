package com.zz.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Sort
{
	private static double DISTINCT_EXP = 0.005;
	//从大到小排序
	public static int[] BigToSmall(int[] a)
	{
		for(int i = 0; i < a.length; i++)
		{
			for(int j = i + 1; j < a.length; j++)
				if(a[i] < a[j])
				{
					int temp = a[j];
					a[j] = a[i];
					a[i] = temp;
				}
		}
		return a;
	}
	
	public static double[] BigToSmall(double[] a)
	{
		for(int i = 0; i < a.length; i++)
		{
			for(int j = i + 1; j < a.length; j++)
				if(a[i] < a[j])
				{
					double temp = a[j];
					a[j] = a[i];
					a[i] = temp;
				}
		}
		return a;
	}

	//从小到大排序
	public static int[] SmallToBig(int[] a)
	{
		for(int i = 0; i < a.length; i++)
		{
			for(int j = i + 1; j < a.length; j++)
				if(a[i] > a[j])
				{
					int temp = a[j];
					a[j] = a[i];
					a[i] = temp;
				}
		}
		return a;
	}
	
	public static double[] SmallToBig(double[] a)
	{
		for(int i = 0; i < a.length; i++)
		{
			for(int j = i + 1; j < a.length; j++)
				if(a[i] > a[j])
				{
					double temp = a[j];
					a[j] = a[i];
					a[i] = temp;
				}
		}
		return a;
	}
	
	//返回差的数组（前 - 后）
	public static int[] Minus1(int[] a)
	{
		int[] b = a.clone();
		int c[] = new int[b.length - 1];
		for(int i = 0; i < c.length; i++)
		{
			c[i] = b[i] - b[i + 1];
		}
		return c;
	}
	
	public static double[] Minus1(double[] a)
	{
		double[] b = a.clone();
		double c[] = new double[b.length - 1];
		for(int i = 0; i < c.length; i++)
		{
			c[i] = b[i] - b[i + 1];
		}
		return c;
	}
	
	//返回差的数组（后 - 前）
	public static int[] Minus2(int[] a)
	{
		int[] b = a.clone();
		int c[] = new int[b.length - 1];
		for(int i = 0; i < c.length; i++)
		{
			c[i] = b[i + 1] - b[i];
		}
		return c;
	}
	
	public static double[] Minus2(double[] a)
	{
		double[] b = a.clone();
		double c[] = new double[b.length - 1];
		for(int i = 0; i < c.length; i++)
		{
			c[i] = b[i + 1] - b[i];
		}
		return c;
	}
	
	//返回商的数组（前 / 后）
	public static double[] Divide1(double[] a)
	{
		double[] b = a.clone();
		double c[] = new double[b.length - 1];
		for(int i = 0; i < c.length; i++)
		{
			c[i] = b[i] / b[i + 1];
		}
		return c;
	}

	public static double[] Divide1(int[] a)
	{
		int[] b = a.clone();
		double[] c = new double[b.length - 1];
		for(int i = 0; i < c.length; i++)
		{
			c[i] = (double)b[i] / b[i + 1];
		}
		return c;
	}
	
	//返回商的数组（后 / 前）
	public static double[] Divide2(double[] a)
	{
		double[] b = a.clone();
		double c[] = new double[b.length - 1];
		for(int i = 0; i < c.length; i++)
		{
			c[i] = b[i + 1] / b[i];
		}
		return c;
	}

	public static double[] Divide2(int[] a)
	{
		int[] b = a.clone();
		double[] c = new double[b.length - 1];
		for(int i = 0; i < c.length; i++)
		{
			c[i] = (double)b[i + 1] / b[i];
		}
		return c;
	}
	
	//对数据进行处理，相近的数字仅保留一个，返回被剔除数值
	public static double[] Distinct1(double[] a)
	{
		double[] b = a.clone();
		ArrayList<Double> list = new ArrayList<Double>();
		for(int i = 1; i < b.length; i++)
		{
			double temp = (b[i] - b[i - 1]) / b[i - 1];
			if(temp < DISTINCT_EXP)
				list.add(b[i]);
		}
		list.trimToSize();
		double[] c = Transform.List2Double(list);
		return c;
	}
	
	public static int[] Distinct1(int[] a)
	{
		int[] b = a.clone();
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 1; i < b.length; i++)
		{
			double temp = (double)(b[i] - b[i - 1]) / b[i - 1];
			if(temp < DISTINCT_EXP)
				list.add(b[i]);
		}
		list.trimToSize();
		int[] c = Transform.List2Int(list);
		return c;
	}
	
	//对数据进行处理，相近的数字仅保留一个，返回剩余数值
	public static double[] Distinct2(double[] a)
	{
		double[] b = a.clone();
		ArrayList<Double> list = new ArrayList<Double>();
		list.add(b[0]);
		for(int i = 1; i < b.length; i++)
		{
			double temp = (b[i] - b[i - 1]) / b[i - 1];
			if(temp >= DISTINCT_EXP)
				list.add(b[i]);
		}
		list.trimToSize();
		double[] c = Transform.List2Double(list);
		return c;
	}
	
	public static int[] Distinct2(int[] a)
	{
		int[] b = a.clone();
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i = 1; i < b.length; i++)
		{
			double temp = (double)(b[i] - b[i - 1]) / b[i - 1];
			if(temp >= DISTINCT_EXP)
				list.add(b[i]);
		}
		list.trimToSize();
		int[] c = Transform.List2Int(list);
		return c;
	}
	
	//对数据进行处理，返回数值对应的位置
	public static int Indexof(double[] a, double b)
	{
		int index = 99999;
		for(int i = 0; i < a.length; i++)
		{
			if(a[i] == b)
				index = i;
		}
		return index;
	}
	
	//对点进行排序，依据X值， 从小到大
	public static ArrayList<Point2D.Double> SmallToBigByX(ArrayList<Point2D.Double> a)
	{
		for(int i = 0; i < a.size(); i++)
		{
			for(int j = i; j < a.size(); j++)
				if(a.get(j).x < a.get(i).x)
				{
					Point2D.Double temPt = a.get(i);
					a.set(i, a.get(j));
					a.set(j, temPt);
				}
		}
		return a;
	}
	
	//对点进行排序，依据X值， 从大到小
	public static ArrayList<Point2D.Double> BigToSmallByX(ArrayList<Point2D.Double> a)
	{
		for(int i = 0; i < a.size(); i++)
		{
			for(int j = i; j < a.size(); j++)
				if(a.get(j).x > a.get(i).x)
				{
					Point2D.Double temPt = a.get(i);
					a.set(i, a.get(j));
					a.set(j, temPt);
				}
		}
		return a;
	}
	
	//对点进行排序，依据X值， 从小到大
	public static ArrayList<Point2D.Double> SmallToBigByY(ArrayList<Point2D.Double> a)
	{
		for(int i = 0; i < a.size(); i++)
		{
			for(int j = i; j < a.size(); j++)
				if(a.get(j).y < a.get(i).y)
				{
					Point2D.Double temPt = a.get(i);
					a.set(i, a.get(j));
					a.set(j, temPt);
				}
		}
		return a;
	}
	
	//对点进行排序，依据X值， 从大到小
	public static ArrayList<Point2D.Double> BigToSmallByY(ArrayList<Point2D.Double> a)
	{
		for(int i = 0; i < a.size(); i++)
		{
			for(int j = i; j < a.size(); j++)
				if(a.get(j).y > a.get(i).y)
				{
					Point2D.Double temPt = a.get(i);
					a.set(i, a.get(j));
					a.set(j, temPt);
				}
		}
		return a;
	}
}
