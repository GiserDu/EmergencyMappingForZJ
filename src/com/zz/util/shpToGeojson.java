package com.zz.util;

import org.gdal.gdal.gdal;
import org.gdal.ogr.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class shpToGeojson {

    public static void main(String[] args) throws IOException {

//     // 注册所有的驱动
//     ogr.RegisterAll();
//     // 为了支持中文路径，请添加下面这句代码
//     gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");
//     // 为了使属性表字段支持中文，请添加下面这句
//     gdal.SetConfigOption("SHAPE_ENCODING","");
//     //shp文件所在的位置
//     String strVectorFile = "C:\\Users\\Administrator\\Desktop\\1\\t\\out\\artifacts\\WEB-INF\\upload\\12\\4\\国界1\\bou1_4p.shp";
//     //打开数据
//     DataSource ds = ogr.Open(strVectorFile,0);
//     if (ds == null)
//     {
//       System.out.println("打开文件失败！" );
//       return;
//     }
//     System.out.println("打开文件成功！" );
//     Driver dv = ogr.GetDriverByName("GeoJSON");
//     if (dv == null)
//     {
//       System.out.println("打开驱动失败！" );
//       return;
//     }
//     System.out.println("打开驱动成功！" );
//     //输出geojson的位置及文件名
//     dv.CopyDataSource(ds, "D:\\ichuan.geojson");
//     BufferedWriter out = new  BufferedWriter( new  OutputStreamWriter(
//                new  FileOutputStream("D:\\sichuan.geojson",  true )));
//     out.write("]}");
//     out.close();
//     System.out.println("转换成功！" );
   }
        public static void shpToGeojson(String  inputShpPath,String outputFilePath) throws IOException {

            // 注册所有的驱动
            ogr.RegisterAll();
            // 为了支持中文路径，请添加下面这句代码
            gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");
            // 为了使属性表字段支持中文，请添加下面这句
            gdal.SetConfigOption("SHAPE_ENCODING","");
            //shp文件所在的位置
           // String strVectorFile = "C:\\Users\\Administrator\\Desktop\\1\\t\\out\\artifacts\\WEB-INF\\upload\\1\\11\\国界\\bou1_4p.shp";
            //打开数据
            DataSource ds = ogr.Open(inputShpPath,0);
            if (ds == null)
            {
                System.out.println("打开文件失败！" );
                return;
            }
            System.out.println("打开文件成功！" );
            Driver dv = ogr.GetDriverByName("GEOJSON");
            if (dv == null)
            {
                System.out.println("打开驱动失败！" );
                return;
            }
            System.out.println("打开驱动成功！" );
            //输出geojson的位置及文件名
            dv.CopyDataSource(ds, outputFilePath);
            //gdal生成的geojson不完整，需要在结尾加   ]}
            BufferedWriter out = new  BufferedWriter( new  OutputStreamWriter(
                    new  FileOutputStream(outputFilePath,  true )));
            out.write("]}");
            out.close();
            System.out.println("转换成功！" );

        }
        public static String readShpFields(String inputShpPath) {
            String fieldsNames="";
            // 注册所有的驱动
            ogr.RegisterAll();

            // 为了支持中文路径，请添加下面这句代码
            gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
            // 为了使属性表字段支持中文，请添加下面这句
            gdal.SetConfigOption("SHAPE_ENCODING", "");

            //打开数据
            DataSource ds = ogr.Open(inputShpPath, 0);
            if (ds == null) {
                System.out.println("打开文件【" + inputShpPath + "】失败！");
                return fieldsNames;
            }

            System.out.println("打开文件【" + inputShpPath + "】成功！");

            // 获取第一个图层
            Layer oLayer = ds.GetLayerByIndex(0);
            if (oLayer == null) {
                System.out.println("获取第0个图层失败！\n");
                return fieldsNames;
            }
            // 对图层进行初始化，如果对图层进行了过滤操作，执行这句后，之前的过滤全部清空
            oLayer.ResetReading();


            System.out.println("属性表结构信息：");
            FeatureDefn oDefn = oLayer.GetLayerDefn();
            int iFieldCount = oDefn.GetFieldCount();
            for (int iAttr = 0; iAttr < iFieldCount; iAttr++) {
                FieldDefn oField = oDefn.GetFieldDefn(iAttr);
                if (iAttr==iFieldCount-1) {
                    //最后一个特殊处理，不加逗号
                    fieldsNames=fieldsNames + oField.GetNameRef();
                }
                else {
                    fieldsNames=fieldsNames + oField.GetNameRef()+ ",";
                    System.out.println(oField.GetNameRef() + ": " +
                            oField.GetFieldTypeName(oField.GetFieldType()) + "(" +
                            oField.GetWidth() + "." + oField.GetPrecision() + ")");
                }


            }
            return fieldsNames;
        }
}
