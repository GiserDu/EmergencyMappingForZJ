package com.zz.bglayer;

import com.zz.util.JUtil;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
/** 
 * RegionLayer地理目标图层类
 * @author L J
 * @version 1.0 
 */
public class JRegionLayer {
	private ArrayList<JGeoRegion> arrObjects;

	//add by LJ 3月16
	private String regLayerName;
	public String getRegLayerName() {
		return regLayerName;
	}

	public void setRegLayerName(String regLayerName) {
		this.regLayerName = regLayerName;
	}

	public JRegionLayer() {
		arrObjects = new ArrayList<JGeoRegion>();
	}

    /**
     * RegionLayer绘图函数
     * @param g2D Java绘图对象
     * @param WC AI范围
     * @param DC 像素范围
     * @param affTrans 仿射变换对象
     * @param regNameMapColor HashMap映射对象
     * @return void
     * @throws 
     * @since 1.0
     */
	public void Draw(Graphics2D g2D, Rectangle2D.Double WC,
			Rectangle2D.Double DC, Affine affTrans,HashMap regNameMapColor) {
		final Color DEFAULT_COLOR = new Color(255,255,255);//当无数据时，默认颜色值为白色
		for (int i = 0; i < arrObjects.size(); i++) {
			if (arrObjects.get(i).getRect().intersects(WC)) {
				if(regNameMapColor.containsKey(arrObjects.get(i).getRegGeoCode())){
					Color fillColor = (Color)regNameMapColor.get(arrObjects.get(i).getRegGeoCode());
					arrObjects.get(i).Draw(g2D, WC, DC, affTrans, fillColor);
				}else{//当无数据时，默认为白色颜色值
					arrObjects.get(i).Draw(g2D, WC, DC, affTrans, DEFAULT_COLOR);
				}
			} else {
				continue;
			}
		}
	}

    /**
     * 读取RegionLayer数据
     * @param regionLayerPath RegionLayer文件路径
     * @return void
     * @throws 
     * @since 1.0
     */
	public void ReadLayerInfoBin(String regionLayerPath) throws IOException {
		String begin;
		int objNum;
		FileInputStream fis = new FileInputStream(regionLayerPath);
		DataInputStream dis = new DataInputStream(fis);
		begin = dis.readUTF();
		if (!begin.equals("TOFGEOLYAER")) {
			System.out.println("Region GLF文件出错！");
			return;
		}
		objNum = dis.readInt();
		for (int i = 0; i < objNum; i++) {
			String objStyle = dis.readUTF();
			if (!objStyle.equals("M")) {
				System.out.println("Region GLF文件目标类型出错！");
				return;
			}
			JGeoRegion jgr = new JGeoRegion();
			jgr.ReadObjectInfoBin(dis);
			
			
			//将相同的地理目标合并，针对于飞地的情况，修改于09年09月21日
			int k = 0;//计数器作用,0代表不是飞地的情况，1代表是飞地的情况
			for(int j=0;j<arrObjects.size();j++){
				//将飞地合并
				if(jgr.getRegGeoCode().equals(arrObjects.get(j).getRegGeoCode())){
					arrObjects.get(j).getPath().append(jgr.getPath(), false);
					
					//Path().getBounds2D()取出的是Rectangle2D.Float ,因此需要转换为Rectangle2D.Double
					Rectangle2D.Double arrRect;
					arrRect = JUtil.transRecFloatToDouble((Rectangle2D.Float)arrObjects.get(j).getPath().getBounds2D());
					arrObjects.get(j).setRect(arrRect);
					                                                                                                                                      
					k = 1;
					break;
				} 
			}
			if(k == 0){
				arrObjects.add(jgr);
			}
//			else if(k == 1){
//				//这种情况仅放一个空的Region
//				JGeoRegion jgr1 = new JGeoRegion();
//				arrObjects.add(i, jgr1);
//			}
			
			
			
//			arrObjects.add(i, jgr);
		}
		arrObjects.trimToSize();

	}

	public ArrayList<JGeoRegion> getArrObjects() {
		return arrObjects;
	}

	public void setArrObjects(ArrayList<JGeoRegion> arrObjects) {
		this.arrObjects = arrObjects;
	}

}
