package com.zz.bglayer;
import java.awt.geom.*;

public class JGridIndex {
	Rectangle2D.Double WC;
	int rowNum;
	int lineNum;
	double[] gridSize;
	Boolean hasGrid;
	
	public JGridIndex(){
		WC = null;
		rowNum = 0;
		lineNum = 0;
		gridSize = null;
		hasGrid = false;

	}
	public JGridIndex(Rectangle2D.Double WC,int rowNum,int lineNum){
		if(rowNum == 0 && lineNum == 0){
			this.hasGrid = false;
			WC = null;
			rowNum = 0;
			lineNum = 0;
			gridSize = null;
		}else{
			this.hasGrid = true;
			this.WC = new Rectangle2D.Double();
			this.WC = WC;
			this.rowNum = rowNum;
			this.lineNum = lineNum;
			this.gridSize = new double[2];
			gridSize[0] = this.WC.getWidth()/this.lineNum;
			gridSize[1] = this.WC.getHeight()/this.rowNum;
		}
	}
	public Boolean getHasGrid() {
		return hasGrid;
	}
	
	public void setHasGrid(Boolean hasGrid) {
		this.hasGrid = hasGrid;
	}
	
	public int[] selectDrawGrid(Rectangle2D.Double curWC, Rectangle2D.Double WC) {
		int[] drawGrid = new int[4];
		double pt1X, pt1Y, pt2X, pt2Y;
		pt1X = curWC.getX();
		pt1Y = curWC.getY();
		pt2X = curWC.getX() + curWC.getWidth();
		pt2Y = curWC.getY() + curWC.getHeight();

		// drawGrid[0] = Math.max(0,(int)((pt1X-WC.getX())/gridSize[0]));
		// drawGrid[1] = Math.max(0,(int)((pt1Y-WC.getY())/gridSize[1]));
		// drawGrid[2] = Math.min(rowNum-1,(int)((pt2X-WC.getX())/gridSize[0]));
		// drawGrid[3] =
		// Math.min(lineNum-1,(int)((pt2Y-WC.getY())/gridSize[1]));

		drawGrid[0] = Math.min(lineNum - 1,
				(int) ((pt1X - WC.getX()) / gridSize[0]));
		drawGrid[1] = Math.min(rowNum - 1,
				(int) ((pt1Y - WC.getY()) / gridSize[1]));
		drawGrid[2] = Math.min(lineNum - 1,
				(int) ((pt2X - WC.getX()) / gridSize[0]));
		drawGrid[3] = Math.min(rowNum - 1,
				(int) ((pt2Y - WC.getY()) / gridSize[1]));
		for (int i = 0; i < 4; i++) {
			if (drawGrid[i] < 0)
				drawGrid[i] = 0;
		}

		// drawGrid[0] = (int) ((pt1X - WC.getX()) / gridSize[0]);
		// drawGrid[1] = (int) ((pt1Y - WC.getY()) / gridSize[1]);
		// drawGrid[2] = (int) ((pt2X - WC.getX()) / gridSize[0]);
		// drawGrid[3] = (int) ((pt2Y - WC.getY()) / gridSize[1]);
		//
		// System.out.println(drawGrid[1]+" "+drawGrid[0]+" "+drawGrid[3]+"
		// "+drawGrid[2]);

		return drawGrid;
	}

	

}
