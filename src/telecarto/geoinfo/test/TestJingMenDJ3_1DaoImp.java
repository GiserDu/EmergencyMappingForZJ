package telecarto.geoinfo.test;

import telecarto.data.jm.dj.JingMenDJ3_1;
import telecarto.geoinfo.db.JingMenDJ3_1DaoImp;

import java.util.ArrayList;

//import org.junit.Test;

public class TestJingMenDJ3_1DaoImp {

//	@Test
	public void testGetAllData() {
		
		JingMenDJ3_1DaoImp jmimp = new JingMenDJ3_1DaoImp();
		ArrayList<JingMenDJ3_1> results =jmimp.getAllData();
		for(JingMenDJ3_1 j:results){
			System.out.println(" j: "+j);
		}
	}

}
