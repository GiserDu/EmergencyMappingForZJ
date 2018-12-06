package com.zz.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;


//处理并解析excel
public class ExcelProcess {
    private static String regionFieldName;//行政编码字段：默认为第一列
    private static String[] fieldNames;//所有属性字段

    public static void main(String[] args){


   }
   private static void doReadExcel(String excelPath){
       try {
           //String encoding = "GBK";
           //String excelPath="C:\\Users\\Administrator\\Desktop\\国家专项测试\\测试数据\\test.xlsx";
           File excel = new File(excelPath);
           if (excel.isFile() && excel.exists()) {   //判断文件是否存在

               String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
               Workbook wb;
               //根据文件后缀（xls/xlsx）进行判断
               if ( "xls".equals(split[1])){
                   FileInputStream fis = new FileInputStream(excel);   //文件流对象
                   wb = new HSSFWorkbook(fis);
               }else if ("xlsx".equals(split[1])){
                   wb = new XSSFWorkbook(excel);
               }else {
                   System.out.println("文件类型错误!");
                   return;
               }

               //开始解析
               Sheet sheet = wb.getSheetAt(0);     //读取sheet 0
               //获得第一行第一列的值作为行政单位
               regionFieldName=sheet.getRow(0).getCell(0).toString();
//               int firstRowIndex = sheet.getFirstRowNum()+1;   //第一行是列名，所以不读
//               int lastRowIndex = sheet.getLastRowNum();
//               System.out.println("firstRowIndex: "+firstRowIndex);
//               System.out.println("lastRowIndex: "+lastRowIndex);
               //获取第一行值并存入字符串数组
               Row firstRow=sheet.getRow(0);
               fieldNames=new String[firstRow.getLastCellNum()+1];
               for(int cIndex=0;cIndex<firstRow.getLastCellNum();cIndex++){
                   fieldNames[cIndex]=firstRow.getCell(cIndex).toString();
               }
               System.out.print(fieldNames);
//               for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
//                   System.out.println("rIndex: " + rIndex);
//                   Row row = sheet.getRow(rIndex);
//
//                   if (row != null) {
//                       for (int cIndex = 0; cIndex < row.getRowNum(); cIndex++) {   //遍历列
//                           Cell cell = row.getCell(cIndex);
//
//                           if (cell != null) {
//                               System.out.println(cell.toString());
//                           }
//                       }
//                   }
//               }
           } else {
               System.out.println("找不到指定的文件");
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
   }

}

