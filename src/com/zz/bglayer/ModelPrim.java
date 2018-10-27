package com.zz.bglayer;

import com.zz.util.Formula;
import com.zz.util.Sort;



public class ModelPrim
{
	/**
	 * 界限等分模型
	 * @param colData(data for grading)
	 * @param thHir
	 * @return double[](critical value)
	 */
	public static int[] modelA0(int[] a, int num)
	{
		int[] b = a.clone();
		Sort.BigToSmall(b);//从大到小排序
		int n = num;
		double dif = (double)(b[0]) / n; //最大值除以分级数
		int[] c = new int[n + 1];
		c[0] = 0;
		c[n] = b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(i * dif + 0.5);
		}
		return c;
	}
	
	public static int[] modelA0(double[] a, int num)
	{
		double[] b = a.clone();
		Sort.BigToSmall(b);
		int n = num;
		double dif = b[0] / n;
		int[] c = new int[n + 1];
		c[0] = 0;
		c[n] = (int)b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(i * dif + 0.5);
		}
		return c;
	}
	
	public static int[] modelA1(int[] a, int num)
	{
		int[] b = a.clone();		
		Sort.BigToSmall(b);
		int length = b.length;
		int n = num;
		double dif = (double)(b[0] - b[length - 1]) / n;
		int[] c = new int[n + 1];
		c[0] = b[length - 1];
		c[n] = b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(c[0] + i * dif + 0.5);
		}
		return c;		
	}
	
	public static int[] modelA1(double[] a, int num)
	{
		double[] b = a.clone();
		Sort.BigToSmall(b);
		int length = b.length;
		int n = num;
		double dif = (b[0] - b[length - 1]) / n;
		int[] c = new int[n + 1];
		c[0] = (int) b[length - 1];
		c[n] = (int) b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(c[0] + i * dif + 0.5);
		}
		return c;		
	}
	
	/**
	 * 间隔等分模型
	 * @param colData(data for grading)
	 * @param thHir
	 * @return double[](critical value)
	 */
	public static int[] modelB0(int[] a, int num)
	{
		int[] b = a.clone();
		Sort.BigToSmall(b);
		int n = num;
		double dif = (double)(b[0]) / (n * (n + 1));
		int[] c = new int[n + 1];
		c[0] = 0;
		c[n] = b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(i * (i + 1) * dif + 0.5);
		}
		return c;
	}
	
	public static int[] modelB0(double[] a, int num)
	{
		double[] b = a.clone();
		Sort.BigToSmall(b);
		int n = num;
		double dif = b[0] / (n * (n + 1));
		int[] c = new int[n + 1];
		c[0] = 0;
		c[n] = (int) b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(i * (i + 1) * dif + 0.5);
		}
		return c;
	}
	
	public static int[] modelB1(int[] a, int num)
	{
		int[] b = a.clone();
		Sort.BigToSmall(b);
		int length = b.length;
		int n = num;
		double dif = (double)(b[0] - b[length - 1]) / (n * (n + 1));
		int[] c = new int[n + 1];
		c[0] = b[length - 1];
		c[n] = b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(c[0] + i * (i + 1) * dif + 0.5);
		}
		return c;
	}

	public static int[] modelB1(double[] a, int num)
	{
		double[] b = a.clone();
		Sort.BigToSmall(b);
		int length = b.length;
		int n = num;
		double dif = (b[0] - b[length - 1]) / (n * (n + 1));
		int[] c = new int[n + 1];
		c[0] = (int) b[length - 1];
		c[n] = (int) b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(c[0] + i * (i + 1) * dif + 0.5);
		}
		return c;
	}
	
	/**
	 * 界限等比模型
	 * @param colData(data for grading)
	 * @param thHir
	 * @return double[](critical value)
	 */
	public static int[] modelC0(int[] a, int num)
	{
		int[] b = a.clone();
		Sort.BigToSmall(b);
		int length = b.length;
		int n = num;
		double ratn = (double) b[0] / b[length - 1];
		double rat = Math.pow(ratn, 1d / n);
		int[] c = new int[n + 1];
		c[0] = b[length - 1];
		c[n] = b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(c[0] * Math.pow(rat, i) + 0.5);
		}
		c[0] = 0;
		return c;
	}
	
	public static int[] modelC0(double[] a, int num)
	{
		double[] b = a.clone();
		Sort.BigToSmall(b);
		int length = b.length;
		int n = num + 1;
		double ratn = (double) b[0] / b[length - 1];
		double rat = Math.pow(ratn, 1d / n);
		int[] c = new int[n + 1];
		c[0] = (int) b[length - 1];
		c[n] = (int) b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(c[0] * Math.pow(rat, i) + 0.5);
		}
		c[0] = 0;
		return c;
	}
	
	public static int[] modelC1(int[] a, int num)
	{
		int[] b = a.clone();
		Sort.BigToSmall(b);
		int length = b.length;
		int n = num;
		double ratn = (double)(b[0] / b[length - 1]);
		double rat = Math.pow(ratn, 1d / n);
		int[] c = new int[n + 1];
		c[0] = b[length - 1];
		c[n] = b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(c[0] * Math.pow(rat, i) + 0.5);
		}
		return c;
	}
	
	public static int[] modelC1(double[] a, int num)
	{
		double[] b = a.clone();
		Sort.BigToSmall(b);
		int length = b.length;
		int n = num;
		double ratn = b[0] / b[length - 1];
		double rat = Math.pow(ratn, 1d / n);
		int[] c = new int[n + 1];
		c[0] = (int) b[length - 1];
		c[n] = (int) b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(c[0] * Math.pow(rat, i) + 0.5);
		}
		return c;
	}
	
	/**
	 * 间隔等比模型
	 * @param colData(data for grading)
	 * @param thHir
	 * @return double[](critical value)
	 */
	public static int[] modelD0(int[] a, int num)
	{
		int[] b = a.clone();
		Sort.BigToSmall(b);
		int n = num;
		double ratn = (double)b[0];
		double rat = Formula.EquationR(n, ratn);
		int[] c = new int[n + 1];
		c[0] = 0;
		c[n] = b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(Formula.PolynomialsR(i, rat) + 0.5);
		}
		return c;
	}
	
	public static int[] modelD0(double[] a, int num)
	{
		double[] b = a.clone();
		Sort.BigToSmall(b);
		int n = num;
		double ratn = (double)b[0];
		double rat = Formula.EquationR(n, ratn);
		int[] c = new int[n + 1];
		c[0] = 0;
		c[n] = (int) b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(Formula.PolynomialsR(i, rat) + 0.5);
		}
		return c;
	}
	
	public static int[] modelD1(int[] a, int num)
	{
		int[] b = a.clone();
		Sort.BigToSmall(b);
		int length = b.length;
		int n = num;
		double ratn = (double)b[0] - b[length - 1];
		double rat = Formula.EquationR(n, ratn);
		int[] c = new int[n + 1];
		c[0] = b[length - 1];
		c[n] = b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(c[0] + Formula.PolynomialsR(i, rat) + 0.5);
		}
		return c;
	}
	
	public static int[] modelD1(double[] a, int num)
	{
		double[] b = a.clone();
		Sort.BigToSmall(b);
		int length = b.length;
		int n = num;
		double ratn = (double)b[0] - b[length - 1];
		double rat = Formula.EquationR(n, ratn);
		int[] c = new int[n + 1];
		c[0] = (int) b[length - 1];
		c[n] = (int) b[0] + 1;
		for(int i = 1; i < c.length - 1; i++)
		{
			c[i] = (int)(c[0] + Formula.PolynomialsR(i, rat) + 0.5);
		}
		return c;
	}
}
