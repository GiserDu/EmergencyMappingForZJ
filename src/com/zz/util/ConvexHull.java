package com.zz.util;

import java.awt.image.BufferedImage;

/**
 * Created by Administrator on 2017/10/9.
 */
public class ConvexHull {
    public ConvexHull(){

    }
    //纵向扫描,获取到闭包的上下范围
    public int[] getScanerY(int imgWidth, int imgHeight, BufferedImage bi){
        boolean isAllFilled = true;
        int [] tempRow = new int[imgWidth];
        int [] indicatorRow = new int[imgHeight];
        int [] scanerY = new int[2];
        for(int j=0;j<imgHeight;j++){
            tempRow = bi.getRGB(0,j,imgWidth,1,tempRow,0,imgWidth);
            indicatorRow[j] = 0;
            for (int temp:tempRow) {
                if(temp!=0){
                    indicatorRow[j] = 1;
                    break;
                }
            }
        }
        for (int temp:indicatorRow) {
            if(temp==0){
                isAllFilled = false;
                break;
            }
        }
        if(indicatorRow[0]==1 && indicatorRow[imgHeight-1]==1){
            isAllFilled = true;
        }

        if(isAllFilled){
            scanerY[0] = 0;
            scanerY[1] = imgHeight-1;
        }
        else {
            for(int i=0;i<indicatorRow.length-1;i++){
                if(indicatorRow[i]==0 && indicatorRow[i+1]==1){
                    if(scanerY[0]==0){
                        scanerY[0] = i+1;
                    }
                }
                if(indicatorRow[i]==1 && indicatorRow[i+1]==0){
                    scanerY[1] = i;
                }
            }
            if(scanerY[1]==0){
                scanerY[1]=imgHeight-1;
            }
        }
        if (indicatorRow[0]==1){
            scanerY[0] = 0;
        }

        return scanerY;
    }

    //横向扫描,获取到闭包的左右范围
    public int[] getScanerX(int imgWidth, int imgHeight, BufferedImage bi) {
        boolean isAllFilled = true;
        int [] tempCol = new int[imgHeight];
        int [] indicatorCol = new int[imgWidth];
        int [] scanerX = new int[2];
        for(int m=0;m<imgWidth;m++){
            for(int k=0;k<imgHeight;k++) {
                tempCol[k] = bi.getRGB(m,k);
            }
            indicatorCol[m] = 0;
            for (int temp:tempCol) {
                if(temp!=0){
                    indicatorCol[m] = 1;
                    break;
                }
            }
        }
        for (int temp:indicatorCol) {
            if(temp==0){
                isAllFilled = false;
                break;
            }
        }
        if(indicatorCol[0]==1 && indicatorCol[imgWidth-1]==1){
            isAllFilled = true;
        }
        if(isAllFilled){
            scanerX[0] = 0;
            scanerX[1] = imgWidth-1;
        }
        else {
            for(int i=0;i<indicatorCol.length-1;i++){
                if(indicatorCol[i]==0 && indicatorCol[i+1]==1){
                    if(scanerX[0]==0){
                        scanerX[0] = i+1;
                    }
                }
                if(indicatorCol[i]==1 && indicatorCol[i+1]==0){
                    scanerX[1] = i;
//                    break;
                }
            }

            if(scanerX[1]==0){
                scanerX[1]=imgWidth-1;
            }
        }
        if (indicatorCol[0]==1){
            scanerX[0] = 0;
        }
        if (indicatorCol[imgWidth-1]==1){
            scanerX[1] = imgWidth-1;
        }
        return scanerX;
    }
}
