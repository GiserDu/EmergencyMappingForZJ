package com.zz.bglayer;

import java.awt.geom.*;
import java.awt.image.*;
import java.io.IOException;
import jxl.read.biff.BiffException;

public class JTest {

	// For test
	public static void main(String[] args) throws BiffException, IOException {

		MapRender mp = new MapRender();
//		mp.LoadData("F:\\Workspaces\\MyEclipse 8.5\\zzCarto\\WebRoot\\WEB-INF\\data\\BigEvent\\10");
		mp.LoadData("F:\\cc");
		Rectangle2D.Double DC = new Rectangle2D.Double(0,0,800,500);

		Rectangle2D.Double WC = mp.GetBoundary();
		System.out.println(DC);
		System.out.println(WC);
		BufferedImage bi = (BufferedImage) mp.OutputMap(WC, DC);

		mp.SaveImage("F:/me2.jpg", bi);
		System.out.println("end");

	}

}
