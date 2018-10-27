package com.zz.chart.render.cunstom;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class FlatChartRenderer
{
	private final Color OUTLINE = Color.WHITE;
	private final int ANGLE1 = 43;
	private final int ANGLE2 = 172;
	private final int ANGLE3 = 270;
	private final double PI = 3.141592653;
	private final double RADIAN1 = ANGLE1 * PI / 180;
	private final double RADIAN2 = ANGLE2 * PI / 180;
	private final double RADIAN3 = ANGLE3 * PI / 180;
	private final double RATIOLEFTBLACK = 7.5 / 66;
	private final double RATIOCENTERBLACK_LR = 4.7 / 66;
	private final double RATIORIGHTBLACK = 8.3 / 66;
	private final double RATIOCENTERWHITE_LR = 13.6 / 66;
	private final double RATIOFRONTBLACK = 6.6 / 34;
	private final double RATIOCENTERBLACK_FB = 2.0 / 34;
	private final double RATIOBACKBLACK = 5.2 / 34;
	private final double RATIOCENTERWHITE_FB = 10.1 / 34;
	private final double RATIOUPBLACK = 11.4 / 166;
	private final double RATIOCENTERBLACK_UD = 6.8 / 166;
	private final double RATIODOWNBLACK = 8.4 / 166;
	private final double RATIOCENTERWHITE_UD = 8.5 / 166;//建筑物绘制参数
	private Color colorFill;//地图定义文件
	private int perValue;
	private int perLength;
	private int perWidth;
	private int perHeight;
	private double ratioLengthGap;
	private double ratioWidthGap;
	private boolean isLabel;//符号描述文件
	private double value;
	private double MaxValue;
	private double MinValue;//专题数据文件提供
	private double x, y;
	private double width, height;
	private Font font;
	private Color colorFont;//绘制时候提供
	private int numFront;
	private int numBack;
	private double leftValueFront = 0;
	private double leftValueBack = 0;
	private double perperValue;
	private double realWidth;
	private double realHeight;
	private Point2D.Double FrontPt1 = new Point2D.Double();
	private Point2D.Double BackPt1 = new Point2D.Double();//顶,左下方
	private Area area = new Area();

	private final int NONE = 0;
	private final int SINGLE = 1;
	private final int NORMAL = 2;
	private int TYPE = NORMAL;
	
	public void draw(Graphics2D g2D)
	{
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		validPerValue();
		getNumAndLeft();
		getRealWidthAndHeight();
		if(realWidth > width || realHeight > height)
		{
			System.out.println("绘图区域不够，请重新设置绘图区域大小！！！");
//			return;
		}
		//lmk 2014.2.10
		if(realWidth < 1.0e-3 || realHeight < 1.0e-3)
		{
			System.out.println("该区域指标值太小，无法表示！！！");
			return;
		}
		else
		{
			getFrontAndBackPt1();//求前、后顶面的左下方点
			for(int i = 0; i < numBack; i++)
			{
				Point2D.Double temPt = new Point2D.Double();
				temPt.x = BackPt1.x + (ratioLengthGap + 1) * perLength * Math.cos(PI - RADIAN2) * i;
				temPt.y = BackPt1.y;
				if(i == numBack - 1 && numBack > numFront)
					if(leftValueBack == 0 && leftValueFront == 0)
					{		
						Point2D.Double temPt1 = new Point2D.Double();
						Point2D.Double temPt2 = new Point2D.Double();
						temPt1.x = FrontPt1.x + (ratioLengthGap + 1) * perLength * Math.cos(PI - RADIAN2) * i;
						temPt1.y = FrontPt1.y;
						temPt2.x = BackPt1.x + (ratioLengthGap + 1) * perLength * Math.cos(PI - RADIAN2) * i;
						temPt2.y = BackPt1.y;
						temPt.x = (temPt1.x + temPt2.x) / 2;
						temPt.y = (temPt1.y + temPt2.y) / 2;
					}
				drawPer(g2D, temPt);
			}
			for(int i = 0; i < numFront; i++)
			{
				Point2D.Double temPt = new Point2D.Double();
				temPt.x = FrontPt1.x + (ratioLengthGap + 1) * perLength * Math.cos(PI - RADIAN2) * i;
				temPt.y = FrontPt1.y;
				drawPer(g2D, temPt);
			}
			if(leftValueFront > 0)
			{
				int number = (int) (leftValueFront / perperValue + 0.5);
				Point2D.Double temPt = new Point2D.Double();
				temPt.x = FrontPt1.x +(ratioLengthGap + 1) * perLength * Math.cos(PI - RADIAN2) * numFront;
				temPt.y = FrontPt1.y + (10 - number) * (RATIOCENTERWHITE_UD + RATIOCENTERBLACK_UD) * perHeight;//正确
				drawPer(g2D, temPt, number);
			}
			if(leftValueBack > 0)
			{
				int number = (int) (leftValueBack / perperValue + 0.5);
				Point2D.Double temPt = new Point2D.Double();
				if(TYPE == NORMAL)
				{
					Point2D.Double temPt1 = new Point2D.Double();
					Point2D.Double temPt2 = new Point2D.Double();
					temPt1.x = FrontPt1.x + (ratioLengthGap + 1) * perLength * Math.cos(PI - RADIAN2) * numFront;
					temPt1.y = FrontPt1.y + (10 - number) * (RATIOCENTERWHITE_UD + RATIOCENTERBLACK_UD) * perHeight;
					temPt2.x = BackPt1.x +(ratioLengthGap + 1) * perLength * Math.cos(PI - RADIAN2) * numBack;
					temPt2.y = BackPt1.y + (10 - number) * (RATIOCENTERWHITE_UD + RATIOCENTERBLACK_UD) * perHeight;
					temPt.x = (temPt1.x + temPt2.x) / 2;
					temPt.y = (temPt1.y + temPt2.y) / 2;
				}
				if(TYPE == SINGLE)
				{
					temPt.x = BackPt1.x;
					temPt.y = BackPt1.y;
				}
				drawPer(g2D, temPt, number);
			}
			if(isLabel)
			{
				Color colorOriginal = g2D.getColor();
				g2D.setFont(font);
				g2D.setColor(colorFont);
				String str = "" + value;
				FontMetrics fm = g2D.getFontMetrics();
				int stringWidth = fm.stringWidth(str);
//				int stringAscent = fm.getAscent();
				int stringDecent = fm.getDescent();
				int stringX = (int) (x - stringWidth / 2d);
				int stringY = (int) (y - realHeight / 2d - stringDecent);//标签写在上方正中处
				g2D.drawString(str, stringX, stringY);
				g2D.setColor(colorOriginal);
			}
		}
	}
	public void loadArea()
	{
		validPerValue();
		getNumAndLeft();
		getRealWidthAndHeight();
		if(realWidth > width || realHeight > height)
		{
			System.out.println("绘图区域不够，请重新设置绘图区域大小！！！");
//			return;
		}
		//lmk 2014.2.10
		if(realWidth < 1.0e-5 || realHeight < 1.0e-5)
		{
			System.out.println("该区域指标值太小，无法表示！！！");
			return;
		}
		else
		{
			getFrontAndBackPt1();//求前、后顶面的左下方点
			for(int i = 0; i < numBack; i++)
			{
				Point2D.Double temPt = new Point2D.Double();
				temPt.x = BackPt1.x + (ratioLengthGap + 1) * perLength * Math.cos(PI - RADIAN2) * i;
				temPt.y = BackPt1.y;
				if(i == numBack - 1 && numBack > numFront)
					if(leftValueBack == 0 && leftValueFront == 0)
					{		
						Point2D.Double temPt1 = new Point2D.Double();
						Point2D.Double temPt2 = new Point2D.Double();
						temPt1.x = FrontPt1.x + (ratioLengthGap + 1) * perLength * Math.cos(PI - RADIAN2) * i;
						temPt1.y = FrontPt1.y;
						temPt2.x = BackPt1.x + (ratioLengthGap + 1) * perLength * Math.cos(PI - RADIAN2) * i;
						temPt2.y = BackPt1.y;
						temPt.x = (temPt1.x + temPt2.x) / 2;
						temPt.y = (temPt1.y + temPt2.y) / 2;
					}
				loadPerArea(temPt);
			}
			for(int i = 0; i < numFront; i++)
			{
				Point2D.Double temPt = new Point2D.Double();
				temPt.x = FrontPt1.x + (ratioLengthGap + 1) * perLength * Math.cos(PI - RADIAN2) * i;
				temPt.y = FrontPt1.y;
				loadPerArea(temPt);
			}
			if(leftValueFront > 0)
			{
				int number = (int) (leftValueFront / perperValue + 0.5);
				Point2D.Double temPt = new Point2D.Double();
				temPt.x = FrontPt1.x +(ratioLengthGap + 1) * perLength * Math.cos(PI - RADIAN2) * numFront;
				temPt.y = FrontPt1.y + (10 - number) * (RATIOCENTERWHITE_UD + RATIOCENTERBLACK_UD) * perHeight;
				loadPerArea(temPt, number);
			}
			if(leftValueBack > 0)
			{
				int number = (int) (leftValueBack / perperValue + 0.5);
				Point2D.Double temPt = new Point2D.Double();
				if(TYPE == NORMAL)
				{
					Point2D.Double temPt1 = new Point2D.Double();
					Point2D.Double temPt2 = new Point2D.Double();
					temPt1.x = FrontPt1.x + (ratioLengthGap + 1) * perLength * Math.cos(PI - RADIAN2) * numFront;
					temPt1.y = FrontPt1.y + (10 - number) * (RATIOCENTERWHITE_UD + RATIOCENTERBLACK_UD) * perHeight;
					temPt2.x = BackPt1.x +(ratioLengthGap + 1) * perLength * Math.cos(PI - RADIAN2) * numBack;
					temPt2.y = BackPt1.y + (10 - number) * (RATIOCENTERWHITE_UD + RATIOCENTERBLACK_UD) * perHeight;
					temPt.x = (temPt1.x + temPt2.x) / 2;
					temPt.y = (temPt1.y + temPt2.y) / 2;
				}
				if(TYPE == SINGLE)
				{
					temPt.x = BackPt1.x;
					temPt.y = BackPt1.y;
				}
				loadPerArea(temPt, number);
			}
		}
	}
	private void validPerValue()
	{
		if(this.perValue == 0)
			this.perValue = (int) (MaxValue / 10 + 0.5);
	}
	private void getNumAndLeft()
	{
		perperValue = perValue / 10.0;
		int num = (int) ((value / perperValue + 0.5) / 10);
		numFront = num / 2;
		numBack = num - numFront;
		double leftValue = value - perValue * num;
		if(leftValue > perperValue / 2)
		{
			if(numBack > numFront)
				leftValueFront = leftValue;
			else
				leftValueBack = leftValue;
		}
	}
	private void getRealWidthAndHeight()
	{
		int num = numBack;
		//lmk 2014.2.20
		int num2 = numFront;
		if(leftValueBack > 0)
			num++;
		if(leftValueFront > 0)
			num2++;
		//lmk 2014.2.20
		realWidth = (num + (num - 1) * ratioLengthGap) * perLength * Math.cos(PI - RADIAN2) + perWidth * Math.sin(RADIAN1);
		if(num2 > 0)
			realHeight = perLength * Math.sin(PI - RADIAN2) + perHeight + (2 + ratioWidthGap) * perWidth * Math.sin(RADIAN1);
		else if(num > 0)
		{
			//lmk 2014.2.20
			TYPE = this.SINGLE;
			int num3 = (int) (leftValueBack / perperValue + 0.5);
			num3 = (10 - num3) % 10;
			double tempPerFrontWindowHeight = (RATIOCENTERWHITE_UD + RATIOCENTERBLACK_UD) * perHeight;//计算前面小"窗户"高
			realHeight = perLength * Math.sin(PI - RADIAN2) + perHeight + perWidth * Math.sin(RADIAN1) - num3 * tempPerFrontWindowHeight;
		}
		else
		{
			TYPE = this.NONE;
			return;
		}
	}
	private void getFrontAndBackPt1()
	{
		if(TYPE == this.NORMAL)
		{
			FrontPt1.x = x - realWidth / 2;
			FrontPt1.y = y + realHeight / 2 - perHeight - perLength * Math.sin(PI - RADIAN2);
			BackPt1.x = FrontPt1.x + (1 + ratioWidthGap) * perWidth * Math.cos(RADIAN1);
			BackPt1.y = FrontPt1.y - (1 + ratioWidthGap) * perWidth * Math.sin(RADIAN1);
		}
		//2014.2.21
		if(TYPE == this.SINGLE)
		{
			TYPE = this.SINGLE;
			FrontPt1.x = x - realWidth / 2;
			FrontPt1.y = y + realHeight / 2 - (realHeight - perWidth * Math.sin(RADIAN1));
			BackPt1.x = x - realWidth / 2;
			BackPt1.y = y + realHeight / 2 - (realHeight - perWidth * Math.sin(RADIAN1));
		}
		
	}
	//pt为顶面左下方点
	//number为层数
	private void drawPer(Graphics2D g2D, Point2D.Double pt, int number)
	{
		if(number > 10)
		{
			System.out.println("数据有误！！！");
			return;
		}
		double curPerHeight = perHeight - (10 - number) * (RATIOCENTERWHITE_UD + RATIOCENTERBLACK_UD) * perHeight;
		Color colorOriginal = g2D.getColor();
		Stroke strokeOriginal = g2D.getStroke();
		Point2D.Double[] ptUps = new Point2D.Double[4];
		for (int i = 0; i < ptUps.length; i++)
			ptUps[i] = new Point2D.Double();
		ptUps[0] = pt;
		ptUps[1].x = ptUps[0].x + perLength * Math.cos(PI - RADIAN2);
		ptUps[1].y = ptUps[0].y + perLength * Math.sin(PI - RADIAN2);
		ptUps[2].x = ptUps[1].x + perWidth * Math.cos(RADIAN1);
		ptUps[2].y = ptUps[1].y - perWidth * Math.sin(RADIAN1);
		ptUps[3].x = ptUps[0].x + ptUps[2].x - ptUps[1].x;
		ptUps[3].y = ptUps[0].y + ptUps[2].y - ptUps[1].y;
		GeneralPath gpUp = new GeneralPath();
		gpUp.moveTo(ptUps[0].x, ptUps[0].y);
		for(int i = 1; i < 4; i++)
			gpUp.lineTo(ptUps[i].x, ptUps[i].y);
		Area areaUp = new Area(gpUp);//上方区域
		Point2D.Double[] ptFronts = new Point2D.Double[4];
		for (int i = 0; i < ptFronts.length; i++)
			ptFronts[i] = new Point2D.Double();
		ptFronts[0] = pt;//右下方为起点，逆时针
		ptFronts[1].x = ptFronts[0].x;
		ptFronts[1].y = ptFronts[0].y + curPerHeight;
		ptFronts[3] = ptUps[1];
		ptFronts[2].x = ptFronts[1].x + ptFronts[3].x - ptFronts[0].x;
		ptFronts[2].y = ptFronts[1].y + ptFronts[3].y - ptFronts[0].y;
		GeneralPath gpFront = new GeneralPath();
		gpFront.moveTo(ptFronts[0].x, ptFronts[0].y);
		for(int i = 1; i < 4; i++)
			gpFront.lineTo(ptFronts[i].x, ptFronts[i].y);
		Area areaFront = new Area(gpFront);//前方区域
		Point2D.Double[] ptRights = new Point2D.Double[4];
		for (int i = 0; i < ptRights.length; i++)
			ptRights[i] = new Point2D.Double();
		ptRights[0] = ptUps[1];
		ptRights[1] = ptFronts[2];
		ptRights[3] = ptUps[2];
		ptRights[2].x = ptRights[1].x + ptRights[3].x - ptRights[0].x;
		ptRights[2].y = ptRights[1].y + ptRights[3].y - ptRights[0].y;
		GeneralPath gpRight = new GeneralPath();
		gpRight.moveTo(ptRights[0].x, ptRights[0].y);
		for(int i = 1; i < 4; i++)
			gpRight.lineTo(ptRights[i].x, ptRights[i].y);
		Area areaRight = new Area(gpRight);//右方区域
		g2D.setColor(Color.BLACK);//设填充色
		g2D.fill(areaUp);
		g2D.fill(areaFront);
		g2D.fill(areaRight);
		g2D.setColor(Color.WHITE);//设边线色
		g2D.setStroke(new BasicStroke(4));
		g2D.draw(areaUp);//画边线
		g2D.draw(areaFront);
		g2D.draw(areaRight);//以上实现画最外面的轮廓性的东西，下面填"窗户"
		g2D.setStroke(strokeOriginal);
		Area[] areaFrontWindow = new Area[3 * number];
		double perFrontWindowLength = RATIOCENTERWHITE_LR * perLength;
		double perFrontWindowHeight = RATIOCENTERWHITE_UD * perHeight;//计算前面小"窗户"长、高
		Point2D.Double[] ptFrontWindow0s = new Point2D.Double[4];
		for (int i = 0; i < ptFrontWindow0s.length; i++)
			ptFrontWindow0s[i] = new Point2D.Double();
		ptFrontWindow0s[0].x = pt.x + RATIOLEFTBLACK * perLength * Math.cos(PI - RADIAN2);
		ptFrontWindow0s[0].y = pt.y + RATIOUPBLACK * perHeight;//左上方为起点，逆时针
		ptFrontWindow0s[1].x = ptFrontWindow0s[0].x;
		ptFrontWindow0s[1].y = ptFrontWindow0s[0].y + perFrontWindowHeight;
		ptFrontWindow0s[2].x = ptFrontWindow0s[1].x + perFrontWindowLength * Math.cos(PI - RADIAN2);
		ptFrontWindow0s[2].y = ptFrontWindow0s[1].y + perFrontWindowLength * Math.sin(PI - RADIAN2);
		ptFrontWindow0s[3].x = ptFrontWindow0s[0].x + ptFrontWindow0s[2].x - ptFrontWindow0s[1].x;
		ptFrontWindow0s[3].y = ptFrontWindow0s[0].y + ptFrontWindow0s[2].y - ptFrontWindow0s[1].y;
		GeneralPath gpFrontWindow0 = new GeneralPath();
		gpFrontWindow0.moveTo(ptFrontWindow0s[0].x, ptFrontWindow0s[0].y);
		for(int i = 1; i < 4; i++)
			gpFrontWindow0.lineTo(ptFrontWindow0s[i].x, ptFrontWindow0s[i].y);
		Area areaFrontWindow0 = new Area(gpFrontWindow0);
		for(int i = 0; i < areaFrontWindow.length; i++)
		{
			areaFrontWindow[i] = (Area) areaFrontWindow0.clone();
			AffineTransform at = new AffineTransform();
			double tempLength = (RATIOCENTERWHITE_LR + RATIOCENTERBLACK_LR) * perLength * (i % 3);
			double tempHeight = (RATIOCENTERWHITE_UD + RATIOCENTERBLACK_UD) * perHeight * (i / 3);
			double tx = tempLength * Math.cos(PI - RADIAN2);
			double ty = tempLength * Math.sin(PI - RADIAN2) + tempHeight;
			at.translate(tx, ty);
			areaFrontWindow[i].transform(at);
		}//小"窗户"赋区域值
		Area[] areaRightWindow = new Area[2 * number];
		double perRightWindowWidth = RATIOCENTERWHITE_FB * perWidth;
		double perRightWindowHeight = RATIOCENTERWHITE_UD * perHeight;//计算右边小"窗户"宽、高
		Point2D.Double[] ptRightWindow0s = new Point2D.Double[4];
		for (int i = 0; i < ptRightWindow0s.length; i++)
			ptRightWindow0s[i] = new Point2D.Double();
		ptRightWindow0s[0].x = ptUps[1].x + RATIOFRONTBLACK * perWidth * Math.cos(RADIAN1);
		ptRightWindow0s[0].y = ptUps[1].y + RATIOUPBLACK * perHeight - RATIOFRONTBLACK * perWidth * Math.sin(RADIAN1);
		ptRightWindow0s[1].x = ptRightWindow0s[0].x;
		ptRightWindow0s[1].y = ptRightWindow0s[0].y + perRightWindowHeight;
		ptRightWindow0s[2].x = ptRightWindow0s[1].x + perRightWindowWidth * Math.cos(RADIAN1);
		ptRightWindow0s[2].y = ptRightWindow0s[1].y - perRightWindowWidth * Math.sin(RADIAN1);
		ptRightWindow0s[3].x = ptRightWindow0s[0].x + ptRightWindow0s[2].x - ptRightWindow0s[1].x;
		ptRightWindow0s[3].y = ptRightWindow0s[0].y + ptRightWindow0s[2].y - ptRightWindow0s[1].y;
		GeneralPath gpRightWindow0 = new GeneralPath();
		gpRightWindow0.moveTo(ptRightWindow0s[0].x, ptRightWindow0s[0].y);
		for(int i = 1; i < 4; i++)
			gpRightWindow0.lineTo(ptRightWindow0s[i].x, ptRightWindow0s[i].y);
		Area areaRightWindow0 = new Area(gpRightWindow0);
		for(int i = 0; i < areaRightWindow.length; i++)
		{
			areaRightWindow[i] = (Area) areaRightWindow0.clone();
			AffineTransform at = new AffineTransform();
			double tempWidth = (RATIOCENTERWHITE_FB + RATIOCENTERBLACK_FB) * perWidth * (i % 2);
			double tempHeight = (RATIOCENTERWHITE_UD + RATIOCENTERBLACK_UD) * perHeight * (i / 2);
			double tx = tempWidth * Math.cos(RADIAN1);
			double ty = - tempWidth * Math.sin(RADIAN1) + tempHeight;
			at.translate(tx, ty);
			areaRightWindow[i].transform(at);
		}//小"窗户"赋区域值
		g2D.setColor(Color.WHITE);
		for(int i = 0; i < areaFrontWindow.length; i++)
			g2D.fill(areaFrontWindow[i]);
		for(int i = 0; i < areaRightWindow.length; i++)
			g2D.fill(areaRightWindow[i]);
		g2D.setColor(colorOriginal);//颜色恢复到初始状态
		g2D.setStroke(strokeOriginal);
	}
	
	private void drawPer(Graphics2D g2D, Point2D.Double pt)
	{
		Color colorOriginal = g2D.getColor();
		Stroke strokeOriginal = g2D.getStroke();
		Point2D.Double[] ptUps = new Point2D.Double[4];
		for (int i = 0; i < ptUps.length; i++)
			ptUps[i] = new Point2D.Double();
		ptUps[0] = pt;
		ptUps[1].x = ptUps[0].x + perLength * Math.cos(PI - RADIAN2);
		ptUps[1].y = ptUps[0].y + perLength * Math.sin(PI - RADIAN2);
		ptUps[2].x = ptUps[1].x + perWidth * Math.cos(RADIAN1);
		ptUps[2].y = ptUps[1].y - perWidth * Math.sin(RADIAN1);
		ptUps[3].x = ptUps[0].x + ptUps[2].x - ptUps[1].x;
		ptUps[3].y = ptUps[0].y + ptUps[2].y - ptUps[1].y;
		GeneralPath gpUp = new GeneralPath();
		gpUp.moveTo(ptUps[0].x, ptUps[0].y);
		for(int i = 1; i < 4; i++)
			gpUp.lineTo(ptUps[i].x, ptUps[i].y);
		Area areaUp = new Area(gpUp);//上方区域
		Point2D.Double[] ptFronts = new Point2D.Double[4];
		for (int i = 0; i < ptFronts.length; i++)
			ptFronts[i] = new Point2D.Double();
		ptFronts[0] = pt;//右下方为起点，逆时针
		ptFronts[1].x = ptFronts[0].x;
		ptFronts[1].y = ptFronts[0].y + perHeight;
		ptFronts[3] = ptUps[1];
		ptFronts[2].x = ptFronts[1].x + ptFronts[3].x - ptFronts[0].x;
		ptFronts[2].y = ptFronts[1].y + ptFronts[3].y - ptFronts[0].y;
		GeneralPath gpFront = new GeneralPath();
		gpFront.moveTo(ptFronts[0].x, ptFronts[0].y);
		for(int i = 1; i < 4; i++)
			gpFront.lineTo(ptFronts[i].x, ptFronts[i].y);
		Area areaFront = new Area(gpFront);//前方区域
		Point2D.Double[] ptRights = new Point2D.Double[4];
		for (int i = 0; i < ptRights.length; i++)
			ptRights[i] = new Point2D.Double();
		ptRights[0] = ptUps[1];
		ptRights[1] = ptFronts[2];
		ptRights[3] = ptUps[2];
		ptRights[2].x = ptRights[1].x + ptRights[3].x - ptRights[0].x;
		ptRights[2].y = ptRights[1].y + ptRights[3].y - ptRights[0].y;
		GeneralPath gpRight = new GeneralPath();
		gpRight.moveTo(ptRights[0].x, ptRights[0].y);
		for(int i = 1; i < 4; i++)
			gpRight.lineTo(ptRights[i].x, ptRights[i].y);
		Area areaRight = new Area(gpRight);//右方区域
	
		g2D.setStroke(strokeOriginal);
		g2D.setColor(Color.BLACK);//设填充色
		g2D.fill(areaUp);
		g2D.fill(areaFront);
		g2D.fill(areaRight);//以上实现画最外面的轮廓性的东西，下面填"窗户"
		g2D.setColor(Color.WHITE);//设边线色
		g2D.setStroke(new BasicStroke(4));
		g2D.draw(areaUp);
		g2D.draw(areaFront);
		g2D.draw(areaRight);//画边线
		Area[] areaFrontWindow = new Area[30];
		double perFrontWindowLength = RATIOCENTERWHITE_LR * perLength;
		double perFrontWindowHeight = RATIOCENTERWHITE_UD * perHeight;//计算前面小"窗户"长、高
		Point2D.Double[] ptFrontWindow0s = new Point2D.Double[4];
		for (int i = 0; i < ptFrontWindow0s.length; i++)
			ptFrontWindow0s[i] = new Point2D.Double();
		ptFrontWindow0s[0].x = pt.x + RATIOLEFTBLACK * perLength * Math.cos(PI - RADIAN2);
		ptFrontWindow0s[0].y = pt.y + RATIOUPBLACK * perHeight;//左上方为起点，逆时针
		ptFrontWindow0s[1].x = ptFrontWindow0s[0].x;
		ptFrontWindow0s[1].y = ptFrontWindow0s[0].y + perFrontWindowHeight;
		ptFrontWindow0s[2].x = ptFrontWindow0s[1].x + perFrontWindowLength * Math.cos(PI - RADIAN2);
		ptFrontWindow0s[2].y = ptFrontWindow0s[1].y + perFrontWindowLength * Math.sin(PI - RADIAN2);
		ptFrontWindow0s[3].x = ptFrontWindow0s[0].x + ptFrontWindow0s[2].x - ptFrontWindow0s[1].x;
		ptFrontWindow0s[3].y = ptFrontWindow0s[0].y + ptFrontWindow0s[2].y - ptFrontWindow0s[1].y;
		GeneralPath gpFrontWindow0 = new GeneralPath();
		gpFrontWindow0.moveTo(ptFrontWindow0s[0].x, ptFrontWindow0s[0].y);
		for(int i = 1; i < 4; i++)
			gpFrontWindow0.lineTo(ptFrontWindow0s[i].x, ptFrontWindow0s[i].y);
		Area areaFrontWindow0 = new Area(gpFrontWindow0);
		for(int i = 0; i < areaFrontWindow.length; i++)
		{
			areaFrontWindow[i] = (Area) areaFrontWindow0.clone();
			AffineTransform at = new AffineTransform();
			double tempLength = (RATIOCENTERWHITE_LR + RATIOCENTERBLACK_LR) * perLength * (i % 3);
			double tempHeight = (RATIOCENTERWHITE_UD + RATIOCENTERBLACK_UD) * perHeight * (i / 3);
			double tx = tempLength * Math.cos(PI - RADIAN2);
			double ty = tempLength * Math.sin(PI - RADIAN2) + tempHeight;
			at.translate(tx, ty);
			areaFrontWindow[i].transform(at);
		}//小"窗户"赋区域值
		Area[] areaRightWindow = new Area[20];
		double perRightWindowWidth = RATIOCENTERWHITE_FB * perWidth;
		double perRightWindowHeight = RATIOCENTERWHITE_UD * perHeight;//计算右边小"窗户"宽、高
		Point2D.Double[] ptRightWindow0s = new Point2D.Double[4];
		for (int i = 0; i < ptRightWindow0s.length; i++)
			ptRightWindow0s[i] = new Point2D.Double();
		ptRightWindow0s[0].x = ptUps[1].x + RATIOFRONTBLACK * perWidth * Math.cos(RADIAN1);
		ptRightWindow0s[0].y = ptUps[1].y + RATIOUPBLACK * perHeight - RATIOFRONTBLACK * perWidth * Math.sin(RADIAN1);
		ptRightWindow0s[1].x = ptRightWindow0s[0].x;
		ptRightWindow0s[1].y = ptRightWindow0s[0].y + perRightWindowHeight;
		ptRightWindow0s[2].x = ptRightWindow0s[1].x + perRightWindowWidth * Math.cos(RADIAN1);
		ptRightWindow0s[2].y = ptRightWindow0s[1].y - perRightWindowWidth * Math.sin(RADIAN1);
		ptRightWindow0s[3].x = ptRightWindow0s[0].x + ptRightWindow0s[2].x - ptRightWindow0s[1].x;
		ptRightWindow0s[3].y = ptRightWindow0s[0].y + ptRightWindow0s[2].y - ptRightWindow0s[1].y;
		GeneralPath gpRightWindow0 = new GeneralPath();
		gpRightWindow0.moveTo(ptRightWindow0s[0].x, ptRightWindow0s[0].y);
		for(int i = 1; i < 4; i++)
			gpRightWindow0.lineTo(ptRightWindow0s[i].x, ptRightWindow0s[i].y);
		Area areaRightWindow0 = new Area(gpRightWindow0);
		for(int i = 0; i < areaRightWindow.length; i++)
		{
			areaRightWindow[i] = (Area) areaRightWindow0.clone();
			AffineTransform at = new AffineTransform();
			double tempWidth = (RATIOCENTERWHITE_FB + RATIOCENTERBLACK_FB) * perWidth * (i % 2);
			double tempHeight = (RATIOCENTERWHITE_UD + RATIOCENTERBLACK_UD) * perHeight * (i / 2);
			double tx = tempWidth * Math.cos(RADIAN1);
			double ty = - tempWidth * Math.sin(RADIAN1) + tempHeight;
			at.translate(tx, ty);
			areaRightWindow[i].transform(at);
		}//小"窗户"赋区域值
		g2D.setColor(Color.WHITE);
		for(int i = 0; i < areaFrontWindow.length; i++)
			g2D.fill(areaFrontWindow[i]);
		for(int i = 0; i < areaRightWindow.length; i++)
			g2D.fill(areaRightWindow[i]);
		g2D.setColor(colorOriginal);//颜色恢复到初始状态
		g2D.setStroke(strokeOriginal);
	}

	private void loadPerArea(Point2D.Double pt, int number)
	{
		double curPerHeight = perHeight - (10 - number) * (RATIOCENTERWHITE_UD + RATIOCENTERBLACK_UD) * perHeight;
		Point2D.Double[] ptUps = new Point2D.Double[4];
		for (int i = 0; i < ptUps.length; i++)
			ptUps[i] = new Point2D.Double();
		ptUps[0] = pt;
		ptUps[1].x = ptUps[0].x + perLength * Math.cos(PI - RADIAN2);
		ptUps[1].y = ptUps[0].y + perLength * Math.sin(PI - RADIAN2);
		ptUps[2].x = ptUps[1].x + perWidth * Math.cos(RADIAN1);
		ptUps[2].y = ptUps[1].y - perWidth * Math.sin(RADIAN1);
		ptUps[3].x = ptUps[0].x + ptUps[2].x - ptUps[1].x;
		ptUps[3].y = ptUps[0].y + ptUps[2].y - ptUps[1].y;
		GeneralPath gpUp = new GeneralPath();
		gpUp.moveTo(ptUps[0].x, ptUps[0].y);
		for(int i = 1; i < 4; i++)
			gpUp.lineTo(ptUps[i].x, ptUps[i].y);
		Area areaUp = new Area(gpUp);//上方区域
		Point2D.Double[] ptFronts = new Point2D.Double[4];
		for (int i = 0; i < ptFronts.length; i++)
			ptFronts[i] = new Point2D.Double();
		ptFronts[0] = pt;//右下方为起点，逆时针
		ptFronts[1].x = ptFronts[0].x;
		ptFronts[1].y = ptFronts[0].y + curPerHeight;
		ptFronts[3] = ptUps[1];
		ptFronts[2].x = ptFronts[1].x + ptFronts[3].x - ptFronts[0].x;
		ptFronts[2].y = ptFronts[1].y + ptFronts[3].y - ptFronts[0].y;
		GeneralPath gpFront = new GeneralPath();
		gpFront.moveTo(ptFronts[0].x, ptFronts[0].y);
		for(int i = 1; i < 4; i++)
			gpFront.lineTo(ptFronts[i].x, ptFronts[i].y);
		Area areaFront = new Area(gpFront);//前方区域
		Point2D.Double[] ptRights = new Point2D.Double[4];
		for (int i = 0; i < ptRights.length; i++)
			ptRights[i] = new Point2D.Double();
		ptRights[0] = ptUps[1];
		ptRights[1] = ptFronts[2];
		ptRights[3] = ptUps[2];
		ptRights[2].x = ptRights[1].x + ptRights[3].x - ptRights[0].x;
		ptRights[2].y = ptRights[1].y + ptRights[3].y - ptRights[0].y;
		GeneralPath gpRight = new GeneralPath();
		gpRight.moveTo(ptRights[0].x, ptRights[0].y);
		for(int i = 1; i < 4; i++)
			gpRight.lineTo(ptRights[i].x, ptRights[i].y);
		Area areaRight = new Area(gpRight);//右方区域
		area.add(areaUp);
		area.add(areaFront);
		area.add(areaRight);
	}
	
	private void loadPerArea(Point2D.Double pt)
	{
		Point2D.Double[] ptUps = new Point2D.Double[4];
		for (int i = 0; i < ptUps.length; i++)
			ptUps[i] = new Point2D.Double();
		ptUps[0] = pt;
		ptUps[1].x = ptUps[0].x + perLength * Math.cos(PI - RADIAN2);
		ptUps[1].y = ptUps[0].y + perLength * Math.sin(PI - RADIAN2);
		ptUps[2].x = ptUps[1].x + perWidth * Math.cos(RADIAN1);
		ptUps[2].y = ptUps[1].y - perWidth * Math.sin(RADIAN1);
		ptUps[3].x = ptUps[0].x + ptUps[2].x - ptUps[1].x;
		ptUps[3].y = ptUps[0].y + ptUps[2].y - ptUps[1].y;
		GeneralPath gpUp = new GeneralPath();
		gpUp.moveTo(ptUps[0].x, ptUps[0].y);
		for(int i = 1; i < 4; i++)
			gpUp.lineTo(ptUps[i].x, ptUps[i].y);
		Area areaUp = new Area(gpUp);//上方区域
		Point2D.Double[] ptFronts = new Point2D.Double[4];
		for (int i = 0; i < ptFronts.length; i++)
			ptFronts[i] = new Point2D.Double();
		ptFronts[0] = pt;//右下方为起点，逆时针
		ptFronts[1].x = ptFronts[0].x;
		ptFronts[1].y = ptFronts[0].y + perHeight;
		ptFronts[3] = ptUps[1];
		ptFronts[2].x = ptFronts[1].x + ptFronts[3].x - ptFronts[0].x;
		ptFronts[2].y = ptFronts[1].y + ptFronts[3].y - ptFronts[0].y;
		GeneralPath gpFront = new GeneralPath();
		gpFront.moveTo(ptFronts[0].x, ptFronts[0].y);
		for(int i = 1; i < 4; i++)
			gpFront.lineTo(ptFronts[i].x, ptFronts[i].y);
		Area areaFront = new Area(gpFront);//前方区域
		Point2D.Double[] ptRights = new Point2D.Double[4];
		for (int i = 0; i < ptRights.length; i++)
			ptRights[i] = new Point2D.Double();
		ptRights[0] = ptUps[1];
		ptRights[1] = ptFronts[2];
		ptRights[3] = ptUps[2];
		ptRights[2].x = ptRights[1].x + ptRights[3].x - ptRights[0].x;
		ptRights[2].y = ptRights[1].y + ptRights[3].y - ptRights[0].y;
		GeneralPath gpRight = new GeneralPath();
		gpRight.moveTo(ptRights[0].x, ptRights[0].y);
		for(int i = 1; i < 4; i++)
			gpRight.lineTo(ptRights[i].x, ptRights[i].y);
		Area areaRight = new Area(gpRight);//右方区域
		area.add(areaUp);
		area.add(areaFront);
		area.add(areaRight);
	}
	public Color getColorFill() {
		return colorFill;
	}
	public void setColorFill(Color colorFill) {
		this.colorFill = colorFill;
	}
	public int getPerValue() {
		return perValue;
	}
	public void setPerValue(int perValue) {
		this.perValue = perValue;
	}
	public int getPerLength() {
		return perLength;
	}
	public void setPerLength(int perLength) {
		this.perLength = perLength;
	}

	public int getPerHeight() {
		return perHeight;
	}
	public void setPerHeight(int perHeight) {
		this.perHeight = perHeight;
	}
	public boolean isLabel() {
		return isLabel;
	}
	public void setLabel(boolean isLabel) {
		this.isLabel = isLabel;
	}
	
	public double getMaxValue() {
		return MaxValue;
	}
	public void setMaxValue(double maxValue) {
		MaxValue = maxValue;
	}
	public double getMinValue() {
		return MinValue;
	}
	public void setMinValue(double minValue) {
		MinValue = minValue;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public Color getColorFont() {
		return colorFont;
	}
	public void setColorFont(Color colorFont) {
		this.colorFont = colorFont;
	}
	public Color getOUTLINE() {
		return OUTLINE;
	}
	public int getANGLE1() {
		return ANGLE1;
	}
	public int getANGLE2() {
		return ANGLE2;
	}
	public int getANGLE3() {
		return ANGLE3;
	}
	public double getPI() {
		return PI;
	}
	public double getRADIAN1() {
		return RADIAN1;
	}
	public double getRADIAN2() {
		return RADIAN2;
	}
	public double getRADIAN3() {
		return RADIAN3;
	}
	public double getRATIOLEFTBLACK() {
		return RATIOLEFTBLACK;
	}
	public double getRATIOCENTERBLACK_LR() {
		return RATIOCENTERBLACK_LR;
	}
	public double getRATIORIGHTBLACK() {
		return RATIORIGHTBLACK;
	}
	public double getRATIOCENTERWHITE_LR() {
		return RATIOCENTERWHITE_LR;
	}
	public double getRATIOFRONTBLACK() {
		return RATIOFRONTBLACK;
	}
	public double getRATIOCENTERBLACK_FB() {
		return RATIOCENTERBLACK_FB;
	}
	public double getRATIOBACKBLACK() {
		return RATIOBACKBLACK;
	}
	public double getRATIOCENTERWHITE_FB() {
		return RATIOCENTERWHITE_FB;
	}
	public double getRATIOUPBLACK() {
		return RATIOUPBLACK;
	}
	public double getRATIOCENTERBLACK_UD() {
		return RATIOCENTERBLACK_UD;
	}
	public double getRATIODOWNBLACK() {
		return RATIODOWNBLACK;
	}
	public double getRATIOCENTERWHITE_UD() {
		return RATIOCENTERWHITE_UD;
	}

	public double getRatioLengthGap() {
		return ratioLengthGap;
	}

	public void setRatioLengthGap(double ratioLengthGap) {
		this.ratioLengthGap = ratioLengthGap;
	}

	public double getRatioWidthGap() {
		return ratioWidthGap;
	}

	public void setRatioWidthGap(double ratioWidthGap) {
		this.ratioWidthGap = ratioWidthGap;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public int getPerWidth() {
		return perWidth;
	}
	public void setPerWidth(int perWidth) {
		this.perWidth = perWidth;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
}