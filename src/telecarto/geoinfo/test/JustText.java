package telecarto.geoinfo.test;

import java.awt.Color;

public class JustText {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Color c = new Color(255, 34, 0);
		System.out.println("color: "+c.getRGB()+" r:" +c.getRed()+" g: " +c.getGreen()+" b: "+c.getBlue());
		Color c2 = new Color(0+34*256+255*256*256);
		System.out.println(c2.getRed()+", "+c2.getGreen()+", "+ c2.getBlue());
		
		System.out.println(2<<4);
	}

}
