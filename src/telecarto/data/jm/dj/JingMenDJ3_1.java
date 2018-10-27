package telecarto.data.jm.dj;

import java.io.Serializable;

/**
 * 东门地基表3_1
 * 测试用
 * @author Tianqin
 *
 */
public class JingMenDJ3_1 implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String TJDYLX;  //统计单元类型
	private String TJDYDM;  //统计单元代码
	private String TJDYMC;  //统计单元名称
	private String YSDM;   //要素代码
	private String YSMC;   //要素名称
	private double MJ;     //面积
	private double MJZB;   //面积占比
	
	public String getTJDYLX() {
		return TJDYLX;
	}
	public void setTJDYLX(String tJDYLX) {
		TJDYLX = tJDYLX;
	}
	public String getTJDYDM() {
		return TJDYDM;
	}
	public void setTJDYDM(String tJDYDM) {
		TJDYDM = tJDYDM;
	}
	public String getTJDYMC() {
		return TJDYMC;
	}
	public void setTJDYMC(String tJDYMC) {
		TJDYMC = tJDYMC;
	}
	public String getYSDM() {
		return YSDM;
	}
	public void setYSDM(String ySDM) {
		YSDM = ySDM;
	}
	public double getMJ() {
		return MJ;
	}
	public void setMJ(double mJ) {
		MJ = mJ;
	}
	public double getMJZB() {
		return MJZB;
	}
	public void setMJZB(double mJZB) {
		MJZB = mJZB;
	}
	public String getYSMC() {
		return YSMC;
	}
	public void setYSMC(String ySMC) {
		YSMC = ySMC;
	}
	@Override
	public String toString() {
		return "统计单元类型： "+this.TJDYLX +"统计单元代码： "+this.TJDYDM+"统计单元名称 ："+this.TJDYMC+
				"要素代码： "+this.YSDM+"要素名称： "+this.YSMC+" 面积平方米： "+this.MJ+" 面积占比： "+this.MJZB;
	}
	
	
	
}
