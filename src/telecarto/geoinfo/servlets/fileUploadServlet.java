package telecarto.geoinfo.servlets;

import com.zz.chart.chartfactory.ChartFactory;
import com.zz.chart.chartfactory.ChartStyleFactory;
import com.zz.chart.chartfactory.IChart;
import com.zz.chart.chartstyle.ChartDataPara;
import com.zz.chart.chartstyle.ChartStyle;
import com.zz.chart.data.ClassData;
import com.zz.chart.data.IndicatorData;
import com.zz.chart.data.ReadRegionData;
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
import telecarto.data.util.ColorUtil;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.Rectangle2D;
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
                        MysqlAccessBean mysql1 = new MysqlAccessBean();
                        //查询本地数据库中的表名
                        String sql1 = "SELECT TABLE_NAME FROM information_schema.views WHERE TABLE_SCHEMA LIKE 'zj_mapping'";
                        StringBuffer dataEx1 = new StringBuffer();
                        try{
                            ResultSet resultSet3 = mysql1.query(sql1);

                            while (resultSet3.next()){
                                dataEx1.append("{name: '"+resultSet3.getString(1)+"'},");
                            }
                            dataEx1.deleteCharAt(dataEx1.length()-1);
                            dataEx1.insert(0, "[");
                            dataEx1.insert(dataEx1.length(), "]");

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally {
                            mysql1.close();
                        }
//                        String dataEx2="[{name: '表格1'},{name: '表格2'},{name: '表格3'}]";
                        String dataEx = dataEx1.toString();
                        message.put("dataEx",dataEx);
                        break;
                    case "column":
                        String tableName = request.getParameter("tableName");
                        MysqlAccessBean mysql2 = new MysqlAccessBean();
                        String sql2 = "select COLUMN_NAME from information_schema.COLUMNS WHERE TABLE_NAME LIKE '" + tableName + "' AND COLUMN_NAME NOT LIKE '年份'";
                        StringBuffer colomnName1 = new StringBuffer();
                        try{
                            ResultSet resultSet4 = mysql2.query(sql2);
                            int i = 0;
                            while (resultSet4.next()){
                                colomnName1.append("\"" + i + "\": \"" + resultSet4.getString(1) + "\",");
                                i++;
                            }
                            colomnName1.deleteCharAt(colomnName1.length()-1);
                            colomnName1.insert(0, "{");
                            colomnName1.insert(colomnName1.length(), "}");
                            String colomnName = colomnName1.toString();
                            message.put("tableField", colomnName);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally {
                            mysql2.close();
                        }
                        break;
                    case "APIData":
                        String apiUrl = request.getParameter("apiUrl");

                        ReadGeojson.doReadGeojson(apiUrl);
                        System.out.println(ReadGeojson.getPropertiesName());
                        message.put("apiCallbackData",ReadGeojson.getPropertiesName());
                        //获取API地址
                        break;
                    case "chartLayerData":
                        doChartLayer(request,response);
                        break;
                    default:
                        System.out.println("其他");
                        MysqlAccessBean mysql = new MysqlAccessBean();
                        String sql = "";
                        ResultSet resultSet2;
                        JSONObject dataJson = JSONObject.fromObject(request.getParameter("allTjLayerContent"));
                        JSONObject statisticdataJson=JSONObject.fromObject(dataJson.getJSONObject("statisticdata"));
                        String classTableName = statisticdataJson.getString("tableName");
                        String spatialId = statisticdataJson.getString("spatialId");
                        JSONArray fieldsNameArray=statisticdataJson.getJSONArray("fieldsName");
                        String fieldsNames=statisticdataJson.getString("fieldsName");
                        String year = statisticdataJson.getString("timeId");
//                        String year = "2016";
                        StringBuffer fieldsNamesBuffer = new StringBuffer(fieldsNames);
                        fieldsNamesBuffer.delete(0, 2);
                        fieldsNamesBuffer.delete(fieldsNamesBuffer.length()-2, fieldsNamesBuffer.length());
                        System.out.println(fieldsNamesBuffer);
//                        String regionClass="1";
                        String dataFieldName=fieldsNamesBuffer.toString();
                        String fieldNameCN=dataFieldName;

                        JSONObject cartographydataJson=JSONObject.fromObject(dataJson.getJSONObject("cartographydata"));

                        int breakNum = Integer.parseInt(cartographydataJson.getString("classNumSliderValue"));
                        String breakMethod=cartographydataJson.getString("modelName");
                        String ip = NetworkUtil.getIpAddr(request);
                        String regionParam = request.getParameter("regionParam");

                        String color = cartographydataJson.getString("colors");
                        String  colors[]= color.trim().split(";");
                        System.out.println(colors);
                        //根据输入行政等级class，确立
                        if (regionParam.equals("1")){
                            sql="SELECT\n" +
                                    "\tregion_info_copy1.citycode, region_info_copy1.name, region_info_copy1.x, region_info_copy1.y, region_info_copy1.json, " + classTableName + ".`" + dataFieldName +"`" +
                                    "\tFROM\n" +
                                    "\tregion_info_copy1\n" +
                                    "LEFT JOIN\t"+ classTableName +"\n" +
                                    "ON region_info_copy1.citycode="+ classTableName +".`"+ spatialId +"`\n" +
                                    "WHERE\n" +
                                    "\tregion_info_copy1.class = " + regionParam +" AND "+ classTableName +".`年份` LIKE '" + year +"'";
                        }
                        else if (regionParam.equals("2")){
                            sql="SELECT\n" +
                                    "\tregion_info_copy1.coutcode, region_info_copy1.name, region_info_copy1.x, region_info_copy1.y, region_info_copy1.json, " + classTableName + ".`" + dataFieldName +"`" +
                                    "\tFROM\n" +
                                    "\tregion_info_copy1\n" +
                                    "LEFT JOIN\t"+ classTableName +"\n" +
                                    "ON region_info_copy1.coutcode="+ classTableName +".`"+ spatialId +"`\n" +
                                    "WHERE\n" +
                                    "\tregion_info_copy1.class = " + regionParam +" AND "+ classTableName +".`年份` LIKE '" + year +"'";
                        }

                        //sql_select = "LEFT JOIN "+ tableName +" t2 ON t1.RGN_CODE = t2.RGN_CODE WHERE t1.RGN_CODE LIKE '"+Param+"' AND t1.RGN_CODE!= '"+regionParam+"' AND t2.YEAR = '" + year + "'";
                        //sql_select = "LEFT JOIN "+ tableName +" t2 ON t1.RGN_CODE = t2.RGN_CODE WHERE t1.RGN_CLASS = '" + regionParam + "' AND t2.YEAR = '" + year + "'";
                        //sql = "SELECT t1.RGN_CODE,t1.RGN_NAME,t1.GEOMETRY,t1.REGION_X,t1.REGION_Y,t2."+ fieldName +" FROM region t1 " + sql_select;
                        // sql = "SELECT t1.RGN_CODE,t1.RGN_NAME,t1.GEOMETRY,t1.REGION_X,t1.REGION_Y,t2."+ fieldName +" FROM region t1 " + sql_select;

                        try {
                            resultSet2 = mysql.query(sql);
                            ArrayList<ClassData> classList = new ArrayList<>();
                            while (resultSet2.next()) {
                                ClassData classData = new ClassData(resultSet2.getString(1),resultSet2.getString(2),
                                        resultSet2.getString(3),resultSet2.getString(4),resultSet2.getString(5),resultSet2.getString(6));
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
                            int width = 225;
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
                            int startX = new BigDecimal((225.0 - stringWidth)/2).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                            if(startX>0){
                                g2d.drawString(fieldNameCN, startX, 25);
                            }
                            else {
                                g2d.setFont(new Font("黑体", Font.PLAIN, 16));
                                fontWidth = imageUtil.getTitleSize(g2d,font,fieldNameCN);
                                stringWidth = new BigDecimal(fontWidth).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                                startX = new BigDecimal((225 - stringWidth)/2).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
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
                            ImageIO.setUseCache(false);
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

    //制作统计图表
    public void doChartLayer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ip = NetworkUtil.getIpAddr(request);
    String statisticJson="{\n" +
            "\t  \"nav\": \"nav2\",\n" +
            "\t  \"tabId\": \"1\",\n" +
            "\t  \"dataAddress\": \"\",\n" +
            "\t  \"tableName\": \"population\",\n" +
            "\t  \"spatialId\": \"id\",\n" +
            "\t  \"fieldsName\": [\n" +
            "\t\t\n" +
            "\t\t\"总人口数（万）\",\n" +
            "\t\t\"男性人口数（万）\",\n" +
            "\t\t\"女性人口数（万）\",\n" +
            "\t  ],\n" +
            "\t  \"fieldsNum\": 5\n" +
            "\t}";
        //获得空间数据各个指标
//        JSONObject spatialdataJson=JSONObject.fromObject(request.getParameter("spatialdata"));//空间数据json对象
//
//        String tabID_spatialdata=spatialdataJson.getString("tabID");    //空间数据来源标识ID，1-行政区划，2-上传shp
//        JSONArray regionDataValue=spatialdataJson.getJSONArray("regionDataValue");  //区域标识编码数组
//        String fileName=spatialdataJson.getString("fileName");


        //获得统计数据各指标
        JSONObject dataJson = JSONObject.fromObject(request.getParameter("allTjLayerContent"));
        JSONObject statisticdataJson=JSONObject.fromObject(dataJson.getJSONObject("statisticdata"));
        System.out.println(statisticdataJson);
//        JSONObject statisticdataJson=JSONObject.fromObject(statisticJson);
        String tabID_statisticdata=statisticdataJson.getString("tabId");    //空间数据来源标识ID，1-行政区划，2-上传shp
        String dataAddress=statisticdataJson.getString("dataAddress");
        String tableName=statisticdataJson.getString("tableName");
        String spatialId=statisticdataJson.getString("spatialId");
        String year = statisticdataJson.getString("timeId"); //根据实际情况修改
//        String year = "2016";
        JSONArray fieldsNameArray=statisticdataJson.getJSONArray("fieldsName");
        String fieldsNames=statisticdataJson.getString("fieldsName");
        String[] fieldsName=new String[fieldsNameArray.size()];
		if(fieldsNameArray!=null||fieldsNameArray.size()!=0){
			for(int i=0;i<fieldsNameArray.size();i++){
                fieldsName[i]=fieldsNameArray.get(i).toString();
			}
		}
        int fieldsNum=statisticdataJson.getInt("fieldsNum");

        //获得画图数据各指标
        JSONObject cartographydataJson=JSONObject.fromObject(dataJson.getJSONObject("cartographydata"));
        System.out.println(cartographydataJson);
//        String type=cartographydataJson.getString("cartographydataJson");
        String chartID0=cartographydataJson.getString("chartID");
        String chartID = chartID0.substring(1, 6);
        System.out.println(chartID);
//        String chartID="10101";
//        JSONArray colorArray=cartographydataJson.getJSONArray("colors");
//        int symbolSize=cartographydataJson.getInt("symbolSizeSliderValue");
        String tempSource="本地数据库";

        // String wcString = request.getParameter("wc");
        //String dcString = request.getParameter("dc");
        //String chartid = request.getParameter("chartID");// 专题符号id
        int width = cartographydataJson.getInt("symbolSizeSliderValue");// 符号长宽
//        int width=80;
//        int height=80;
        int height= cartographydataJson.getInt("symbolSizeSliderValue");
//        String regionParam = "1";

//		String islabelString = request.getParameter("ISLABEL");
        // String islabelString = "false";
        // String yearString = request.getParameter("year");

        //String chartData = request.getParameter("CHARTDATA");
        //System.out.println("chart data: "+chartData);
        StringBuffer fieldsNamesStr = new StringBuffer(fieldsNames);
        fieldsNamesStr.deleteCharAt(0);

        String[] arr = fieldsNames.split(",");
//        tableName= statisticdataJson.getString("tableName");
        String thematicData = tableName;

        //根据所选指标的数目进行不同的色彩配置(有几个指标配置几个色彩渐变)
        String colorRampSchema1 = cartographydataJson.getString("colorName");
//        String colorRampSchema1 = "青黄色系";
//		System.out.println(colorRampSchema1);
//        String colorString="15538980;2498916;15855872";
        String colorString;
        colorString = ColorUtil.getColorScheme(arr.length,colorRampSchema1);
        String[] colors = colorString.split(";");
        int[] fieldColors = new int[colors.length]; // 专题符号颜色
        for (int i = 0; i < fieldColors.length; i++) {
            fieldColors[i] = Integer.parseInt(colors[i]);
        }
        String regionParam = request.getParameter("regionParam");
        String dataTabID = statisticdataJson.getString("tabId");
        String indiSource = "";
        switch (dataTabID){
            case "1":
                indiSource = "平台数据库";
                break;
            case "2":
                indiSource = "API数据";
                break;
            case "3":
                indiSource = "上传EXCEL文件";
                break;
        }

        //Rectangle2D.Double DC = JUtil.StringToRect(dcString);// DC
        ChartStyleFactory chartStyleFactory = new ChartStyleFactory();
        ChartStyle chartStyle = chartStyleFactory.createcChartStyle(chartID);// 通过工厂模式实例化符号样式类
        //设置符号宽高
//        int width = Integer.parseInt(widthstring);
//        int height = Integer.parseInt(heightstring);

        //根据符号ID加载符号样式
        String dir = "assets/";
        String chartPath = dir + chartID + ".xml";
        chartStyle.Load(chartPath);
        //初始化符号参数
        //统计符号的chartDataPara
        ChartDataPara chartDataPara = new ChartDataPara();

        chartDataPara.initial_ZJ(fieldsNames,indiSource);// 初始化专题符号层参数

        chartDataPara.setFieldColor(fieldColors);
        chartDataPara.setWidth(width);
        chartDataPara.setHeight(height);


//        //输入apiURL返回数据resultString
//        //解析API返回的数据resultString，
//        String URL="";
//        String resultString=JUtil.getResultStrFromAPI("");
//        IndicatorData[] indicatorDatas = JUtil.getIndicatorDataFromAPi(resultString);
//        double[][] coordinatesXY=JUtil.getXYFromAPi(resultString);
////        String[] xStrings = ReadRegionData.getRegonX();
////        String[] yStrings = ReadRegionData.getRegonY();
//        chartDataPara.initialAsAPI(indicatorDatas);// 初始化专题符号层参数
        //
        //ArrayList<String > regionData=new ArrayList();
        IndicatorData[] indicatorDatas = JUtil.getIndicatorData_ZJ(tableName, fieldsName, regionParam, spatialId, year);
        ReadRegionData.doReadRegionData_ZJ(regionParam);
        double[] maxValues = JUtil.maxValues(indicatorDatas);
        double[] minValues = JUtil.minValues(indicatorDatas);
        double[] averageValues = JUtil.averageValues(indicatorDatas);
        double[] scales = JUtil.scales(indicatorDatas, width);
        chartDataPara.setScales(scales);
        String[] xStrings = ReadRegionData.getRegonX();
        String[] yStrings = ReadRegionData.getRegonY();
        String[] nameStrings = ReadRegionData.getRegonName();//regionCodes为区域代码数组

        //生成图例
        ChartFactory chartFactoryLegend = new ChartFactory();
        IChart chartLegend = chartFactoryLegend.createcChart(chartID);
        IndicatorData[] indicatorDataLegend = new IndicatorData[1];
        indicatorDataLegend[0] = indicatorDatas[0];
        BufferedImage bi = chartLegend.drawLegend(80, 160, chartDataPara, chartStyle, maxValues, minValues, averageValues, indicatorDataLegend);
        BufferedImage bufferedImage = new BufferedImage(bi.getWidth(),bi.getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
//			g2d.setColor(new Color(247,247,247));
        g2d.setColor(Color.white);
        g2d.fillRect(0,0,bi.getWidth(),bi.getHeight());
        //把图例bi绘制到bufferedImage上
        g2d.drawImage(bi,0,0,bi.getWidth(),bi.getHeight(),null);
        g2d.dispose();
        //图例图片转码为base64
        BASE64Encoder encoderLegend = new BASE64Encoder();
        ByteArrayOutputStream baosLegend = new ByteArrayOutputStream();
        ImageIO.setUseCache(false);
        ImageIO.write(bufferedImage, "png", baosLegend);
        byte[] bytesLegend = baosLegend.toByteArray();
        String imgStreamLegend = encoderLegend.encodeBuffer(bytesLegend).trim();
        //输出统计图图例到本地(底色透明)
        String tempPath = getServletContext().getRealPath("/") + "printMap";
        String tempFilePath = getServletContext().getRealPath("/") + "printMap\\"+ ip.replaceAll("\\.","-");
        File fileSavepath = new File(tempPath);
        File fileSavepathMap = new File(tempFilePath);
        if(!fileSavepath.exists()){
            fileSavepath.mkdirs();
        }
        if(!fileSavepathMap.exists()){
            fileSavepathMap.mkdirs();
        }
        String imgPath = fileSavepathMap + "/"+"chartLegend.png";
        ImageIO.write(bi, "png", new File(imgPath));




        /*单张图绘制,并进行最小闭包处理*/
        int indiNum = indicatorDatas[0].getNames().length;//指标数目
        String[] indiNames = new String[indiNum];
        String[] indiUnits = new String[indiNum];

        for (int p=0;p<indiNum;p++) {
            indiNames[p] = indicatorDatas[0].getNames()[p];
            indiUnits[p] ="";//指标单位未获得，默认为空
        }
        JSONObject chartsObject = new JSONObject();
        JSONArray chartArray = new JSONArray();
        ArrayList<BufferedImage> biList = new ArrayList<>();

        for (int i = 0; i < indicatorDatas.length; i++) {
            IndicatorData[] indicatorData = new IndicatorData[1];
            indicatorData[0] = indicatorDatas[i];
            int biWidth = (new Double(width*1.3)).intValue();
//				int biHeight = (new Double(height*1.2)).intValue();
            BufferedImage bi2 = new BufferedImage(biWidth,height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2D2 = bi2.createGraphics();
            ChartFactory chartFactory = new ChartFactory();
            IChart chart = chartFactory.createcChart(chartID);
            /*width/2, width/2,为绘制专题符号的中心点位置,这里width/2可以将符号绘制在正中心*/
            chart.drawChart(g2D2, width/2, width/2, width, height, chartDataPara,
                    chartStyle, maxValues, minValues, averageValues,
                    indicatorData);

            int imgWidth = bi2.getWidth();
            int imgHeight = bi2.getHeight();
            ConvexHull convex = new ConvexHull();
            int [] scanerY = convex.getScanerY(imgWidth,imgHeight,bi2);
            int [] scanerX = convex.getScanerX(imgWidth,imgHeight,bi2);
            int convexWidth = scanerX[1] - scanerX[0] +1;
            int convexHeight = scanerY[1] - scanerY[0] +1;
            int [] imageArry = new int[convexWidth*convexHeight];
            imageArry = bi2.getRGB(scanerX[0],scanerY[0],convexWidth,convexHeight,imageArry,0,convexWidth);


            //convexBI为新生成的最小闭包图片
            BufferedImage convexBI = new BufferedImage(convexWidth,convexHeight, BufferedImage.TYPE_INT_ARGB);
            convexBI.setRGB(0,0,convexWidth,convexHeight,imageArry,0,convexWidth);
            biList.add(convexBI);
            g2D2.dispose();

            //将生产的图片转换为base64编码的字符串imgStream
            BASE64Encoder encoder = new BASE64Encoder();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(biList.get(i), "png", baos);
            byte[] bytes = baos.toByteArray();
            String imgStream = encoder.encodeBuffer(bytes).trim();

            JSONObject singleChart = new JSONObject();
            JSONObject singleChartAttr = new JSONObject();

//            singleChartAttr.put("rng_code", regionCodes[i]);
            singleChartAttr.put("rng_name", indicatorData[0].getDomainAxis());

            singleChart.put("point_x", xStrings[i]);
            singleChart.put("point_y", yStrings[i]);
            for (int r=0;r<indiNum;r++){
                String indi = "indi" + r;
                String valueName = "value" + r;
                String value = indicatorDatas[i].getValues()[r] + indiUnits[r];
                singleChartAttr.put(indi, indiNames[r]);
                singleChartAttr.put(valueName, value);
            }
            singleChartAttr.put("indiNum", indiNum);
//				singleChart.put("indiNum",indiNum);
//				singleChart.put("unit", indiUnits[0]);
            singleChart.put("img",imgStream);
            singleChart.put("imgWidth",convexWidth);
            singleChart.put("imgHeight",convexHeight);
            singleChart.put("attributes",singleChartAttr);
            chartArray.add(singleChart);
        }
        chartsObject.put("charts",chartArray);
        //String timeData = yearString + "年";
        //chartsObject.put("year",timeData);
        chartsObject.put("source",indiSource);
        chartsObject.put("chartLegend",imgStreamLegend.replaceAll("[\\s*\t\n\r]", ""));
        chartsObject.put("type","chartLayer");
        PrintWriter writer = response.getWriter();
        writer.print(chartsObject);
        writer.close();

    }

}
