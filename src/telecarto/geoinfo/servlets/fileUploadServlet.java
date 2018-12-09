package telecarto.geoinfo.servlets;

import com.zz.chart.data.ClassData;
import com.zz.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import sun.awt.SunHints;
import sun.misc.BASE64Encoder;
import telecarto.data.util.ReadGeojson;
import telecarto.geoinfo.db.MysqlAccessBean;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "fileUploadServlet")
public class fileUploadServlet extends HttpServlet {
    String saveFilePath="";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/javascript;charset=UTF-8");//返回json格式的数据
        request.setCharacterEncoding("UTF-8");//设置服务器端对前端传输数据的解码方式!!!

        //得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
        String savePath = this.getServletContext().getRealPath("/uploadFile");
        File tmpSaveFile = new File(savePath);
        if (!tmpSaveFile.exists()) {
            //创建临时目录
            tmpSaveFile.mkdir();
        }
        //上传时生成的临时文件保存目录
        String tempPath = this.getServletContext().getRealPath("/WEB-INF/temp");
        File tmpFile = new File(tempPath);
        if (!tmpFile.exists()) {
            //创建临时目录
            tmpFile.mkdir();
        }

        //消息提示
        JSONObject message = new JSONObject();

        try{
            //使用Apache文件上传组件处理文件上传步骤：
            //1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //设置工厂的缓冲区的大小，当上传的文件大小超过缓冲区的大小时，就会生成一个临时文件存放到指定的临时目录当中。
            factory.setSizeThreshold(1024*100);//设置缓冲区的大小为100KB，如果不指定，那么缓冲区的大小默认是10KB
            //设置上传时生成的临时文件的保存目录
            factory.setRepository(tmpFile);
            //2、创建一个文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
            //监听文件上传进度
            upload.setProgressListener(new ProgressListener(){
                public void update(long pBytesRead, long pContentLength, int arg2) {
                    System.out.println("文件大小为：" + pContentLength + ",当前已处理：" + pBytesRead);
                    /**
                     * 文件大小为：14608,当前已处理：4096
                     文件大小为：14608,当前已处理：14608
                     */
                }
            });
            //解决上传文件名的中文乱码
            upload.setHeaderEncoding("UTF-8");
            //3、判断提交上来的数据是否是上传表单的数据
            if(!ServletFileUpload.isMultipartContent(request)){
                //按照传统方式获取数据，根据输入inputType判断上传数据方式
                String inputType = request.getParameter("inputType");
                switch (inputType){
                    case "localDatabase":
                        System.out.println(inputType);
                        message.put("dataType","正常表单数据");
                        String dataEx="[{name: '表格1'},{name: '表格2'},{name: '表格3'}]";

                        message.put("dataEx",dataEx);
                        break;
                    case "APIData":
                        String apiUrl = request.getParameter("apiUrl");

                        ReadGeojson.doReadGeojson(apiUrl);
                        System.out.println(ReadGeojson.getPropertiesName());
                        message.put("apiCallbackData",ReadGeojson.getPropertiesName());
                        //获取API地址
                        break;
                    default:
                        System.out.println("其他");
                        MysqlAccessBean mysql = new MysqlAccessBean();
                        String sql;
                        ResultSet resultSet2;
                        String regionClass="1";
                        String dataFieldName="总人口数（万）";
                        String fieldNameCN="总人口数（万）";
                        int breakNum=5;
                        String breakMethod="界限等分模型";
                        String ip="127.0.0";
                        String  colors[]= {"rgb(20,0,0)","rgb(60,0,0)","rgb(100,0,0)","rgb(140,0,0)","rgb(180,0,0)"};
                        //根据输入行政等级class，确立
                        sql="SELECT\n" +
                                "\t*\n" +
                                "FROM\n" +
                                "\tregion_info\n" +
                                "LEFT JOIN\tpopular\n" +
                                "ON region_info.citycode=popular.`code`\n" +
                                "WHERE\n" +
                                "\tregion_info.class = " + regionClass;

                        //sql_select = "LEFT JOIN "+ tableName +" t2 ON t1.RGN_CODE = t2.RGN_CODE WHERE t1.RGN_CODE LIKE '"+Param+"' AND t1.RGN_CODE!= '"+regionParam+"' AND t2.YEAR = '" + year + "'";
                        //sql_select = "LEFT JOIN "+ tableName +" t2 ON t1.RGN_CODE = t2.RGN_CODE WHERE t1.RGN_CLASS = '" + regionParam + "' AND t2.YEAR = '" + year + "'";
                        //sql = "SELECT t1.RGN_CODE,t1.RGN_NAME,t1.GEOMETRY,t1.REGION_X,t1.REGION_Y,t2."+ fieldName +" FROM region t1 " + sql_select;
                        // sql = "SELECT t1.RGN_CODE,t1.RGN_NAME,t1.GEOMETRY,t1.REGION_X,t1.REGION_Y,t2."+ fieldName +" FROM region t1 " + sql_select;

                        try {
                            resultSet2 = mysql.query(sql);
                            ArrayList<ClassData> classList = new ArrayList<>();
                            while (resultSet2.next()) {
                                ClassData classData = new ClassData(resultSet2.getString(1),resultSet2.getString(3),
                                        resultSet2.getString(5),resultSet2.getString(6),resultSet2.getString(7),resultSet2.getString(dataFieldName));
                                classList.add(classData);
                            }
                            double maxValue =  Double.parseDouble(classList.get(0).getData());
                            double minValue = Double.parseDouble(classList.get(0).getData());
                            for (int i=0;i<classList.size();i++){
                                if(Double.parseDouble(classList.get(i).getData())>maxValue){
                                    maxValue = Double.parseDouble(classList.get(i).getData());
                                }
                                if(Double.parseDouble(classList.get(i).getData())<minValue){
                                    minValue = Double.parseDouble(classList.get(i).getData());
                                }
                            }
                            //采用对应的分类方法,根据各区域的数据值进行分类,并赋予颜色
                            Classifiter classifiter = new Classifiter();
                            JSONArray classDataArray = classifiter.getClassIntervalJson(minValue,maxValue,breakNum,classList,colors,breakMethod);
                            JSONObject classObject = new JSONObject();
                            //绘制分级图图例
                            double []classInterval = classifiter.getIntervals(minValue,maxValue,breakNum,breakMethod);
                            int width = 165;
                            int height = 21 * breakNum+45;
                            BufferedImage image = new BufferedImage(width, height,
                                    BufferedImage.TYPE_INT_RGB);
                            Graphics2D g2d = image.createGraphics();

                            g2d.setColor(Color.white);

                            g2d.fillRect(0,0,image.getWidth(),image.getHeight());
                            g2d.dispose();

                            g2d = image.createGraphics();
                            //文字抗锯齿化处理
                            g2d.setRenderingHint(SunHints.KEY_ANTIALIASING, SunHints.VALUE_ANTIALIAS_ON);

                            Font font = new Font("黑体", Font.PLAIN, 17);
                            g2d.setFont(font);
                            g2d.setColor(Color.black);

                            // 获取图例标题的像素范围对象
                            double fontWidth = imageUtil.getTitleSize(g2d,font,fieldNameCN);
                            int stringWidth = new BigDecimal(fontWidth).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                            int startX = new BigDecimal((165.0 - stringWidth)/2).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                            if(startX>0){
                                g2d.drawString(fieldNameCN, startX, 25);
                            }
                            else {
                                g2d.setFont(new Font("黑体", Font.PLAIN, 16));
                                fontWidth = imageUtil.getTitleSize(g2d,font,fieldNameCN);
                                stringWidth = new BigDecimal(fontWidth).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                                startX = new BigDecimal((165 - stringWidth)/2).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                                g2d.drawString(fieldNameCN, startX, 25);
                            }

//            g2d.drawString("分级图图例", 40, 25);
                            font = new Font("", Font.PLAIN, 13);
                            g2d.setFont(font);

                            for (int i=0;i<colors.length;i++){
                                String temp = colors[i].substring(colors[i].indexOf("(") + 1, colors[i].indexOf(")"));
                                String[] rgbTemp = temp.trim().split(",");
                                //绘制空心矩形(色块边线)
                                g2d.setColor(Color.black);
                                g2d.drawRect(20,i*21+37,28,16);
                                //设置颜色-->色块
                                g2d.setColor(new Color(Integer.parseInt(rgbTemp[0].trim()), Integer
                                        .parseInt(rgbTemp[1].trim()), Integer.parseInt(rgbTemp[2].trim())));
                                //绘制实心矩形色块
                                g2d.fillRect(21,i*21+37+1,27,15);
                                //设置颜色-->文字
                                g2d.setColor(Color.black);
                                if(i == 0){
                                    String legendStr = "< " + classInterval[0];
                                    g2d.drawString(legendStr, 55, i*21+39+12);
                                }
                                else if(i==colors.length-1){
                                    String legendStr = ">= " + classInterval[i-1];
                                    g2d.drawString(legendStr, 55, i*21+39+12);
                                }
                                else {
                                    String legendStr;
                                    legendStr = classInterval[i-1] + " ~ " + classInterval[i];
                                    g2d.drawString(legendStr, 55, i*21+39+12);
                                }
                            }
                            g2d.dispose();
                            //图例图片转码为base64
                            BASE64Encoder encoderLegend = new sun.misc.BASE64Encoder();
                            ByteArrayOutputStream baosLegend = new ByteArrayOutputStream();
                            ImageIO.write(image, "png", baosLegend);
                            byte[] bytesLegend = baosLegend.toByteArray();
                            String imgStreamLegend = encoderLegend.encodeBuffer(bytesLegend).trim();
                            //输出分级图图例到本地
                            String tempFilePath = getServletContext().getRealPath("/") + "printMap/" + ip.replaceAll("\\.","-");
                            File fileSavepath = new File(tempFilePath);
                            if(!fileSavepath.exists()){
                                fileSavepath.mkdirs();
                            }
                            String imgPath = tempFilePath + "/"+"classLegend.png";
                            ImageIO.write(image, "png", new File(imgPath));

                            //传输JSON分级grapgics数组到前端
                            classObject.put("classDataArray",classDataArray);
                            //classObject.put("dataSource",dataSource);
                            classObject.put("classLegend",imgStreamLegend.replaceAll("[\\s*\t\n\r]", ""));
                            PrintWriter out = response.getWriter();
                            out.print(classObject);
                            out.flush();
                            out.close();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally {
                            mysql.close();
                        }

                }

                PrintWriter writer = response.getWriter();
                writer.print(message);
                writer.close();
                return;
            }

            //设置上传单个文件的大小的最大值，目前是设置为1024*1024字节，也就是5MB
            upload.setFileSizeMax(1024*1024*5);
            //设置上传文件总量的最大值，最大值=同时上传的多个文件的大小的最大值的和，目前设置为15MB
            upload.setSizeMax(1024*1024*15);
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = upload.parseRequest(request);
            for(FileItem item : list){
                //如果fileitem中封装的是普通输入项的数据
                if(item.isFormField()){
                    String name = item.getFieldName();
                    //解决普通输入项的数据的中文乱码问题
                    String value = item.getString("UTF-8");
                    //value = new String(value.getBytes("iso8859-1"),"UTF-8");
                    System.out.println(name + "=" + value);
                }else{//如果fileitem中封装的是上传文件
                    //得到上传的文件名称，
                    String filename = item.getName();
                    System.out.println(filename);
                    if(filename==null || filename.trim().equals("")){
                        continue;
                    }
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    filename = filename.substring(filename.lastIndexOf("\\")+1);
                    //得到上传文件的扩展名
                    String fileExtName = filename.substring(filename.lastIndexOf(".")+1);
                    //如果需要限制上传的文件类型，那么可以通过文件的扩展名来判断上传的文件类型是否合法
                    System.out.println("上传的文件的扩展名是："+fileExtName);
                    //获取item中的上传文件的输入流

                    InputStream in = item.getInputStream();
                    //得到文件保存的唯一名称
                    String saveFilename = makeFileName(filename);
                    //得到文件的保存目录
                    String realSavePath = makePath(saveFilename, savePath);
                    saveFilePath = realSavePath+"\\"+filename;
                    //创建一个文件输出流
                    FileOutputStream out = new FileOutputStream(saveFilePath);
                    //创建一个缓冲区
                    byte buffer[] = new byte[1024];
                    //判断输入流中的数据是否已经读完的标识
                    int len = 0;
                    //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                    while((len=in.read(buffer))>0){
                        //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                        out.write(buffer, 0, len);
                    }
                    //关闭输入流
                    in.close();
                    //关闭输出流
                    out.close();
                    //删除处理文件上传时生成的临时文件
                    item.delete();
                    String relativeJsonPath = "http://" + request.getServerName() //服务器地址
                            + ":"
                            + request.getServerPort()		 //端口号
                            + request.getContextPath()     //项目名称
                            + saveFilePath.substring(saveFilePath.lastIndexOf("\\uploadFile"));

                    message.put("message","文件上传成功！") ;
                    message.put("saveFilePath",relativeJsonPath);
                }
            }
        }catch (FileUploadBase.FileSizeLimitExceededException e) {
            e.printStackTrace();
            request.setAttribute("message", "单个文件超出最大值！！！");

            return;
        }catch (FileUploadBase.SizeLimitExceededException e) {
            e.printStackTrace();
            message.put("message","上传文件的总的大小超出限制的最大值！！！！") ;

            return;
        }catch (Exception e) {
            message.put("message","文件上传失败！") ;
            e.printStackTrace();
        }

        //解压文件
        //根据类型，进行相应的解压缩，默认保存到当前目录
        String type = saveFilePath.substring(saveFilePath.lastIndexOf(".")+1);
        if(type.equals("zip")){
            ZipAndRarUtil.unZip(saveFilePath);
            compressProcess(message,request);
        }else if(type.equals("rar")){
            ZipAndRarUtil.unRar(saveFilePath);
            compressProcess(message,request);
        }else if (type.equals("xls") || type.equals("xlsx")){

            ExcelProcess.doReadExcel(saveFilePath);
            message.put("tableFields",ExcelProcess.getFieldNames());
//            JSONArray jsonObject = JSONArray.fromObject(ExcelProcess.getFieldNames());
//            String jsonStr = jsonObject.toString();
//            message.put("tableFields",jsonStr);
        }else {
            System.out.print("不支持的数据格式");
        }

//        String fileName = "";
//        String shpFilePath="";
//        String fileDic=saveFilePath.substring(0, saveFilePath.lastIndexOf('.'));
//        File file = new File(fileDic);
//        File[] tempList = file.listFiles();
//        //解压输出文件路径，遍历寻找shp文件
//        for (int i = 0; i < tempList.length; i++) {
//            if (tempList[i].isFile()) {
//                System.out.println("文     件："+tempList[i]);
//                fileName = tempList[i].getName();
//                if(fileName.substring(fileName.lastIndexOf('.')+1).equals("shp")){
//                    shpFilePath=tempList[i].getPath();
//                }
//                System.out.println("文件名："+fileName);
//        }
//            if (tempList[i].isDirectory()) {
//                System.out.println("文件夹："+tempList[i]);
//            }
//        }
//
//        //gdal实现shp转geojson
//        shpToGeojson.shpToGeojson(shpFilePath,shpFilePath.replace(".shp",".json"));
//        String fieldsName= shpToGeojson.readShpFields(shpFilePath);
//        message.put("fieldsName",fieldsName);
//
//        String outJsonPath=shpFilePath.replace(".shp",".json");
//
//        String relativeJsonPath = "http://" + request.getServerName() //服务器地址
//                + ":"
//                + request.getServerPort()		 //端口号
//                + request.getContextPath()     //项目名称
//                + outJsonPath.substring(outJsonPath.lastIndexOf("\\uploadFile"));
//        message.put("geoJsonURL",relativeJsonPath);

        PrintWriter writer = response.getWriter();
        writer.print(message);
        writer.close();



    }
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }
    /**
     * @Method: makeFileName
     * @Description: 生成上传文件的文件名，文件名以：uuid+"_"+文件的原始名称
     * @Anthor:孤傲苍狼
     * @param filename 文件的原始名称
     * @return uuid+"_"+文件的原始名称
     */
    private String makeFileName(String filename){  //2.jpg
        //为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
        return UUID.randomUUID().toString() + "_" + filename;
    }

    /**
     * 为防止一个目录下面出现太多文件，要使用hash算法打散存储
     * @Method: makePath
     * @Description:
     * @Anthor:孤傲苍狼
     *
     * @param filename 文件名，要根据文件名生成存储目录
     * @param savePath 文件存储路径
     * @return 新的存储目录
     */
    private String makePath(String filename,String savePath){
        //得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
        int hashcode = filename.hashCode();
        int dir1 = hashcode&0xf;  //0--15
        int dir2 = (hashcode&0xf0)>>4;  //0-15
        //构造新的保存目录
        String dir = savePath + "\\" + dir1 + "\\" + dir2;  //upload\2\3  upload\3\5
        //File既可以代表文件也可以代表目录
        File file = new File(dir);
        //如果目录不存在
        if(!file.exists()){
            //创建目录
            file.mkdirs();
        }
        return dir;
    }
    private void compressProcess(JSONObject message, HttpServletRequest request) throws IOException {

        String fileName = "";
        String shpFilePath="";
        String fileDic=saveFilePath.substring(0, saveFilePath.lastIndexOf('.'));
        File file = new File(fileDic);
        File[] tempList = file.listFiles();
        //解压输出文件路径，遍历寻找shp文件
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                System.out.println("文     件："+tempList[i]);
                fileName = tempList[i].getName();
                if(fileName.substring(fileName.lastIndexOf('.')+1).equals("shp")){
                    shpFilePath=tempList[i].getPath();
                }
                System.out.println("文件名："+fileName);
            }
            if (tempList[i].isDirectory()) {
                System.out.println("文件夹："+tempList[i]);
            }
        }

        //gdal实现shp转geojson
        shpToGeojson.shpToGeojson(shpFilePath,shpFilePath.replace(".shp",".json"));
        String fieldsName= shpToGeojson.readShpFields(shpFilePath);
        message.put("fieldsName",fieldsName);

        String outJsonPath=shpFilePath.replace(".shp",".json");

        String relativeJsonPath = "http://" + request.getServerName() //服务器地址
                + ":"
                + request.getServerPort()		 //端口号
                + request.getContextPath()     //项目名称
                + outJsonPath.substring(outJsonPath.lastIndexOf("\\uploadFile"));
        message.put("geoJsonURL",relativeJsonPath);

    }

}
