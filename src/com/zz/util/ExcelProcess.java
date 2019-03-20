package com.zz.util;

import com.zz.chart.data.IndicatorData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;


//处理并解析excel
public class ExcelProcess {

    private static String regionFieldName;//行政编码字段：默认为第一列
    private static String[] fieldNames;//所有属性字段
    private static String[] spatialNames;//空间字段所有值
    private static String[] timeNames;//所有时间值，只列可选的


    public static String[] getTimeNames() {
        return timeNames;
    }

    public static void setTimeNames(String[] timeNames) {
        ExcelProcess.timeNames = timeNames;
    }



    public static String[] getSpatialNames() {
        return spatialNames;
    }

    public static void setSpatialNames(String[] spatialNames) {
        ExcelProcess.spatialNames = spatialNames;
    }


    public static IndicatorData[] getIndicatorDatas() {
        return indicatorDatas;
    }

    public static void setIndicatorDatas(IndicatorData[] indicatorDatas) {
        ExcelProcess.indicatorDatas = indicatorDatas;
    }

    private static IndicatorData[] indicatorDatas;

    public static String getRegionFieldName() {
        return regionFieldName;
    }

    public static void setRegionFieldName(String regionFieldName) {
        ExcelProcess.regionFieldName = regionFieldName;
    }

    public static String[] getFieldNames() {
        return fieldNames;
    }

    public static void setFieldNames(String[] fieldNames) {
        ExcelProcess.fieldNames = fieldNames;
    }

    public static void main(String[] args){
        //String encoding = "GBK";
        String excelPath="C:\\Users\\Administrator\\Desktop\\国家专项测试\\测试数据\\test.xls";
        doReadExcelForFiledsName(excelPath);
        System.out.print("测试");
   }
   //读取excel获取字段名列表，以及时间字段所有值（默认时间字段为最后一行）
   public static void doReadExcelForFiledsName(String excelPath){
       try {
           File excel = new File(excelPath);
           if (excel.isFile() && excel.exists()) {   //判断文件是否存在
               String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义
               Workbook wb;
               //根据文件后缀（xls/xlsx）进行判断
               if ( "xls".equals(split[1])){
                   FileInputStream fis = new FileInputStream(excel);   //文件流对象
                   wb = new HSSFWorkbook(fis);
               }else if ("xlsx".equals(split[1])){
                   wb = new XSSFWorkbook(excel);
               }else {
                   System.out.println("表格文件类型错误!");
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
               fieldNames=new String[firstRow.getLastCellNum()];
               for(int cIndex=0;cIndex<firstRow.getLastCellNum();cIndex++){
                   fieldNames[cIndex]=firstRow.getCell(cIndex).toString();
               }
               System.out.print(fieldNames);
               //读取最后一列时间
               int firstRowIndex = sheet.getFirstRowNum()+1;   //第一行是列名，所以不读
               int lastRowIndex = sheet.getLastRowNum();

              ArrayList<String> timeList=new ArrayList<>();
               for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                   Row row = sheet.getRow(rIndex);
                   //获得最后一列
                    String eachTimeField= row.getCell(row.getLastCellNum()-1).toString()  ;
                    if(!timeList.contains(eachTimeField)){
                        timeList.add(eachTimeField);
                    }
               }
               timeNames=timeList.toArray(new String[timeList.size()]);

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

   //读取excel获取数据列表
    public  static void doReadExcelForAllData(String excelPath,String[] inputFieldsName,String spatialID){
        try {
            File excel = new File(excelPath);
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在
                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义
                Workbook wb;
                //根据文件后缀（xls/xlsx）进行判断
                if ( "xls".equals(split[1])){
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                }else if ("xlsx".equals(split[1])){
                    wb = new XSSFWorkbook(excel);
                }else {
                    System.out.println("表格文件类型错误!");
                    return;
                }

                //开始解析，默认解析第一个表格
                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0
                //获得第一行第一列的值作为行政单位
                //regionFieldName=sheet.getRow(0).getCell(0).toString();

                //获取第一行值并遍历输入字段名数组，找到对应字段的列号,存入inputFieldIndexArr数组
                Row firstRow=sheet.getRow(0);
                int[] inputFieldIndexArr=new int[inputFieldsName.length];
                int spatialIDIndex=0;//默认为第一行
                //fieldNames=new String[firstRow.getLastCellNum()];
                for(int i=0;i<inputFieldsName.length;i++){
                    String eachInputFieldName=inputFieldsName[i];
                    for(int cIndex=0;cIndex<firstRow.getLastCellNum();cIndex++){
                        //fieldNames[cIndex]=firstRow.getCell(cIndex).toString();
                        String eachFirstRowCell=firstRow.getCell(cIndex).toString();
                        if(eachFirstRowCell.equals(eachInputFieldName)){
                            inputFieldIndexArr[i]=cIndex;
                        }
                        if(eachFirstRowCell.equals(spatialID)){
                            spatialIDIndex=cIndex;
                        }
                    }
                }
                //按行读取,根据输入列索引查值
                ArrayList<IndicatorData> indicatorDataList=new ArrayList<>();
                int firstRowIndex = sheet.getFirstRowNum()+1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();

                String[] regionFieldNames=new String[lastRowIndex-firstRowIndex+1];
                for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                    System.out.println("rIndex: " + rIndex);

                    double[] valueStrs=new double[inputFieldIndexArr.length];
                    Row row = sheet.getRow(rIndex);
                    String spatialFiledName=row.getCell(spatialIDIndex).toString();
                    regionFieldNames[rIndex-1]=spatialFiledName;
                    if (row != null) {
                        //按照输入字段索引查指标值
                        for(int j=0;j<inputFieldIndexArr.length;j++){
                            valueStrs[j]=Double.parseDouble(row.getCell(inputFieldIndexArr[j]).toString());
                        }
//                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
//
//                            Cell cell = row.getCell(cIndex);
//                            if (cell != null) {
//                                System.out.println(cell.toString());
//                            }
//                        }
                    }
                    IndicatorData indicatorData=new IndicatorData(spatialFiledName,inputFieldsName,valueStrs);
                    indicatorDataList.add(indicatorData);

                }
                indicatorDatas = (IndicatorData[])indicatorDataList.toArray(new IndicatorData[1]);
                spatialNames=regionFieldNames;
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
                System.out.println("找不到指定的Excel文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //读取指定列的数据
    public static String[] getSpFieldData(String excelPath,String fieldName){
        try {
            File excel = new File(excelPath);
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在
                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义
                Workbook wb;
                //根据文件后缀（xls/xlsx）进行判断
                if ( "xls".equals(split[1])){
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                }else if ("xlsx".equals(split[1])){
                    wb = new XSSFWorkbook(excel);
                }else {
                    System.out.println("表格文件类型错误!");
                   String[] d={};
                   return d;
                }

                //开始解析，默认解析第一个表格
                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0
                //获得第一行第一列的值作为行政单位
                //regionFieldName=sheet.getRow(0).getCell(0).toString();

                //获取第一行值并遍历输入字段名数组，找到对应字段的列号,存入inputFieldIndexArr数组
                Row firstRow=sheet.getRow(0);


                int inputFieldIndex=0;

                for(int cIndex=0;cIndex<firstRow.getLastCellNum();cIndex++){
                    String eachFirstRowCell=firstRow.getCell(cIndex).toString();
                    if(eachFirstRowCell.equals(fieldName)){
                        inputFieldIndex=cIndex;
                        break;
                    }
                }

                int firstRowIndex = sheet.getFirstRowNum()+1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum();

                String[] fieldData=new String [lastRowIndex-firstRowIndex+1];
                for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {
                    //遍历行读取指定指标数据
                    System.out.println("rIndex: " + rIndex);
                    Row row = sheet.getRow(rIndex);
                    //String spatialFiledName=row.getCell(spatialIDIndex).toString();
                    if (row != null) {
                        //按照输入字段索引查指标值
                        fieldData[rIndex-1]=row.getCell(inputFieldIndex).toString();
//                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
//
//                            Cell cell = row.getCell(cIndex);
//                            if (cell != null) {
//                                System.out.println(cell.toString());
//                            }
//                        }
                    }
                }
                return fieldData;
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
                System.out.println("找不到指定的Excel文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] d={};
        return d;
    }
}

