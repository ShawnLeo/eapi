package com.shawn.generator.tools;

import java.io.*;
import java.util.zip.*;

public class ZipFileUtil {

    public static void compressFiles2Zip(String zipFileName) throws IOException {
        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName + ".zip"));

        //创建缓冲输出流
        BufferedOutputStream bos = new BufferedOutputStream(out);

        File sourceFile = new File(zipFileName);

        //调用函数
        compress(out, bos, sourceFile, sourceFile.getName());

        bos.close();
        out.close();

    }

    public static void  compress(ZipOutputStream out, BufferedOutputStream bos, File sourceFile, String base) throws IOException {
        //如果路径为目录（文件夹）
        if (sourceFile.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();

            if (flist.length == 0) {//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                out.putNextEntry(new ZipEntry(base + "/"));
            } else { //如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                for (int i = 0; i < flist.length; i++) {
                    compress(out, bos, flist[i], base + "/" + flist[i].getName());
                }
            }
        } else { //如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
            out.putNextEntry(new ZipEntry(base));
            FileInputStream fos = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int tag;
//            System.out.println(base);
            //将源文件写入到zip文件中
            while ((tag = bis.read()) != -1) {
                out.write(tag);
            }
            bis.close();
            fos.close();

        }
    }
}


//public class TestZip {
//
//    public static void main(String[] args) {
//        ZipCompress zipCom = new ZipCompress("/Users/shawn/Documents/code/eapi/code_gen_temp/aaa.zip", "/Users/shawn/Documents/code/eapi/code_gen_temp");
//        try {
//            zipCom.zip();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
