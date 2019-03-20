package com.zz.util;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


public class CsvReader {
    public static void main(String[] args) {
        String excelPath = "C:\\Users\\Administrator\\Desktop\\国家专项测试\\测试数据\\testCSV.csv";
        BufferedReader br = null;
        CSVParser csvFileParser = null;
        List list = null;
        System.out.print(readCsvFile(excelPath,0)) ;

    }
// 读取csv文件 传参数 文件 表头 从第几行开始
        public static List readCsvFile(String filePath,  Integer num) {
            BufferedReader br = null;
            CSVParser csvFileParser = null;
            List list = null;
            // 创建CSVFormat（header mapping）
            CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader();
            try {
                // 初始化FileReader object
                br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "GBK"));//解决乱码问题
                // 初始化 CSVParser object
                csvFileParser = new CSVParser(br, csvFileFormat);
                // CSV文件records
                List<CSVRecord> csvRecords = csvFileParser.getRecords();
                List data = new ArrayList();
                list = new ArrayList();
                for (int i = num; i < csvRecords.size(); i++) {
                    CSVRecord record = csvRecords.get(i);
//                    Set<String> key=((LinkedHashMap)record.mapping).keySet();
                    list.add(record);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print("csv文件读取异常");
            } finally {
                try {
                    br.close();
                    csvFileParser.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }
 }

