package telecarto.data.util;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/11/25.
 */
public class RarUtil {
    /**
     * 根据原始rar路径，解压到指定文件夹下.
     * @param srcRarPath 原始rar路径
     * @param dstDirectoryPath 解压到的文件夹
     */
    public static boolean unrar(String srcRarPath,String dstDirectoryPath)throws Exception {
        Archive archive = null;
        FileOutputStream fos = null;
        boolean flag = false;
//        System.out.println("Starting...");
        File sourceRar = new File(srcRarPath);
        File destDir = new File(dstDirectoryPath);
        try {
            archive = new Archive(sourceRar);
            FileHeader fh = archive.nextFileHeader();
            int count = 0;
            File destFileName;
            while (fh != null) {
                String compressFileName;
                System.out.println(fh.isUnicode());
                // 判断文件路径是否有中文
                if(existZH(fh.getFileNameW())){
//                    System.out.println((++count) + ") "+ fh.getFileNameW());
                    compressFileName =fh.getFileNameW().trim();
                }else{
//                    System.out.println((++count) + ") "+ fh.getFileNameString());
                    compressFileName =fh.getFileNameString().trim();
                }
                destFileName = new File(destDir.getAbsolutePath() + "/" +compressFileName);
                if (fh.isDirectory()) {
                    if (!destFileName.exists()){
                        destFileName.mkdirs();
                    }
                    fh =archive.nextFileHeader();
                    continue;
                }
                if (!destFileName.getParentFile().exists()) {
                    destFileName.getParentFile().mkdirs();
                }
                fos = new FileOutputStream(destFileName);
                archive.extractFile(fh, fos);
                fos.close();
                fos = null;
                fh = archive.nextFileHeader();
            }
            archive.close();
            archive = null;
//            System.out.println("Finished !");
            flag = true;
        }catch (Exception e) {
            throw e;
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
            if (archive != null) {
                try {
                    archive.close();
                } catch (Exception e) {
                    //ignore
                }
            }
        }
        return flag;
    }

     /*
     * 判断是否是中文
     */
    public static boolean existZH(String str){
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            return true;
        }
        return false;
    }

    public static void deleteRar(String filePath){
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }


    public static void main(String[] temp) throws Exception {
        //解压
        RarUtil.unrar("C:/Users/Administrator/Desktop/上传测试数据/制图5-1-1.rar", "C:/Users/Administrator/Desktop");
    }
}
