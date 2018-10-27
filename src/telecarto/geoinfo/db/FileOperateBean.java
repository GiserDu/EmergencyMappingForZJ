package telecarto.geoinfo.db;

import javax.servlet.http.Part;
import java.io.*;

public class FileOperateBean {
    private static String excelPath;
    private static String zipPath;

    public static String getExcelPath(InputStream fileSource,String tempFilePath,String disFileName) throws IOException{
        FileOutputStream outputStream = null;
        RandomAccessFile randomAccessFile = null;
        RandomAccessFile randomFile = null;
        File tempFile = null;
        File saveFile = null;
        String filename = null;
        try{
            File fileuploadpath = new File(tempFilePath);
            if(!fileuploadpath.exists()){
                fileuploadpath.mkdir();
            }

            //tempFile指向临时文件  ,将excel文件读取并写入临时文件
            tempFile = new File(tempFilePath,disFileName);
            outputStream = new FileOutputStream(tempFile);

            byte b[] = new byte[2 * 1024];
            int n;
            while(( n = fileSource.read(b)) != -1){
                outputStream.write(b, 0, n);
            }

            //获取上传文件的名称 filename
            randomFile = new RandomAccessFile(tempFile,"r");
            randomFile.readLine();
            String str = randomFile.readLine();
            System.out.println(str);

            int endIndex = str.lastIndexOf("\"");
            String beginStr = str.substring(0,endIndex);
            int beginIndex = beginStr.lastIndexOf("\"") +1;
            filename = beginStr.substring(beginIndex, beginStr.length());

            //针对IE浏览器，处理文件名
            int flag = filename.indexOf("\\");
            if(flag != -1){
                filename = filename.substring(filename.lastIndexOf("\\")+1);
            }

            System.out.println("filename:" + filename);

            //重新定位文件指针到文件头
            randomFile.seek(0);
            long startPosition = 0;
            int i = 1;

            //获取文件内容开始位置 startPosition（零时文件前四行不是正式内容）
            while(( n = randomFile.readByte()) != -1 && i <=4){
                if(n == '\n'){
                    startPosition = randomFile.getFilePointer();
                    i ++;
                }
            }
            startPosition = randomFile.getFilePointer() -1;

            //获取文件内容结束位置endPosition
            randomFile.seek(randomFile.length());
            long endPosition = randomFile.getFilePointer();
            int j = 1;
            while(endPosition >=0 && j<=2){
                endPosition--;
                randomFile.seek(endPosition);
                if(randomFile.readByte() == '\n'){
                    j++;
                }
            }
            endPosition = endPosition -1;

            //设置保存上传文件的路径
            String realPath = tempFilePath;
//            String realPath = "D:/FileTemp";
            File fileupload = new File(realPath);
            if(!fileupload.exists()){
                fileupload.mkdir();
            }
            saveFile = new File(realPath,filename);
            randomAccessFile = new RandomAccessFile(saveFile,"rw");

            //从临时文件当中读取文件内容（根据起止位置获取）
            randomFile.seek(startPosition);
            while(startPosition < endPosition){
                randomAccessFile.write(randomFile.readByte());
                startPosition = randomFile.getFilePointer();
            }

            excelPath = saveFile.toString();

        } catch (IOException e) {
            e.printStackTrace();
            //request.getRequestDispatcher("/fail.jsp").forward(request, response);
        } finally {
            outputStream.close();
            fileSource.close();
            randomAccessFile.close();
            randomFile.close();
            tempFile.delete();
        }
        return excelPath;

    }


    public static String getZipPath(InputStream fileSource,String tempFilePath,String disFileName) throws IOException{
        FileOutputStream outputStream = null;
        File tempFile = null;
        try{
            File fileuploadpath = new File(tempFilePath);
            if(!fileuploadpath.exists()){
                fileuploadpath.mkdir();
            }

            //tempFile指向临时文件  ,将excel文件读取并写入临时文件
            tempFile = new File(tempFilePath,disFileName);
            outputStream = new FileOutputStream(tempFile);

            byte b[] = new byte[2 * 1024];
            int n;
            while(( n = fileSource.read(b)) != -1){
                outputStream.write(b, 0, n);
            }

            zipPath = tempFile.toString();
            System.out.println("filename:" + zipPath);

        } catch (IOException e) {
            e.printStackTrace();
            //request.getRequestDispatcher("/fail.jsp").forward(request, response);
        } finally {
            outputStream.close();
            fileSource.close();
        }
        return zipPath;

    }

    public static void deleteExcel(){
        File file = new File(excelPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    public static void deleteZip(){
        File file = new File(zipPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }


    public static String getFilename(Part part){
        if (part == null) {
            return null;
        }
        String fileName = part.getHeader("content-disposition");
        if (isBlank(fileName)) {
            return null;
        }
        return substringBetween(fileName, "filename=\"", "\"");
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0)
            return true;
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null)
            return null;
        int start = str.indexOf(open);
        if (start != -1) {
            int end = str.indexOf(close, start + open.length());
            if (end != -1)
                return str.substring(start + open.length(), end);
        }
        return null;
    }

}
