package telecarto.geoinfo.servlets;

import telecarto.data.util.AntZipUtil;
import telecarto.data.util.RarUtil;
import telecarto.geoinfo.db.FileOperateBean;
import telecarto.geoinfo.db.MysqlAccessBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2017/10/18.
 */
//@WebServlet("/servlet/ZipFileServlet")
@MultipartConfig(
        maxFileSize = 100*1024*1024
)
//@WebServlet(name = "ZipFileServlet")
public class ZipFileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getContentLength() > 0) {
            //注意！！在javaWeb开发中，一定要把引用的包拷贝在WEB-INF/lib中，不然无法调用！！！
            InputStream fileSource = null;
            MysqlAccessBean mysql = null;
            try {
                //直接给POIFSFileSystem传入fileSource无法识别文件头，因为包含了消息头！！！
                //request.getInputStream()字节流中包含有四行文件头,而POIFSFileSystem需要不包含文件头的正文内容
                request.setCharacterEncoding("UTF-8");
                Part zipFile = request.getPart("file"); //Part仅适用于文件对象的获取
                String mapName = request.getParameter("fileName");//获取地图名称
                String display = request.getParameter("display");//获取显示权限(1:显示,0:不显示)
                String mapClass = request.getParameter("mapClass");//获取专题图所属主题
                String description = request.getParameter("description");//获取专题图的描述
                String filename = FileOperateBean.getFilename(zipFile);//xxx.zip
                String tempFilePath = getServletContext().getRealPath("/") + "thematicMaps";
                //对应用于存储的文件夹地址为-->http://localhost:8080/thematicMaps/mapName
                fileSource = zipFile.getInputStream();
                String filePath = FileOperateBean.getZipPath(fileSource, tempFilePath, filename);//全名.zip
                String[] temp = filename.split("\\.");//转义字符
                if(temp[1].equals("rar")){
                   //压缩文件为.rar格式
                    RarUtil.unrar(filePath,tempFilePath);
                    RarUtil.deleteRar(filePath);
                }
                else {
                    //压缩文件为.zip格式
                    System.out.println(AntZipUtil.unZip(filePath, tempFilePath));
                    //处理完成后，删除本地zip文件
                    FileOperateBean.deleteZip();
                }

                String flagPath = tempFilePath + "\\" + temp[0] + "\\TileGroup0";
                File fileuploadpath = new File(flagPath);
                //如果判断出解压后的文件不是专题图的文件夹结构,则删除文件夹
                if(!fileuploadpath.exists()){
                    String deletePath = tempFilePath + "\\" + temp[0];
                    AntZipUtil.deleteFile(deletePath);
                    PrintWriter writer = response.getWriter();
                    writer.print("wrong");
                    writer.close();
                }
                else {
                    String url = "http://223.75.52.36:28090/geoInfoHB/thematicMaps/" + temp[0];
                    mysql = new MysqlAccessBean();
                    String sql = "INSERT INTO thematic_maps(MAP_NAME,MAP_URL,DATE,DISPLAY,CLASS,DESCRIPE) VALUES(?,?,?,?,?,?)";
                    mysql.update(sql,mapName,url,display,mapClass,description);

                    //返回信息
                    //response.setContentType("text/html;charset=UTF-8");
                    PrintWriter writer = response.getWriter();
                    writer.print(" { ");
                    writer.print(tempFilePath+"\\"+filename);
                    writer.print(" } ");
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                fileSource.close();
            }

        }
    }
}
