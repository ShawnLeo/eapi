package com.meimeitech.generator.tools;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class ZipFileUtil {
    /**
     * 把文件压缩成zip格式
     *
     * @param dir         压缩文件目录
     * @param zipFilePath 压缩后的zip文件路径   ,如"D:/test/aa.zip";
     */
    public static String compressFiles2Zip(String dir) throws IOException {
        File dirFile = new File(dir);
        if (dirFile.isFile()) {
            throw new IllegalArgumentException("参数dir:不是目录");
        }
        String parent = dirFile.getParent();
        String zipFilePath = dir+".zip";

        Collection<File> files = FileUtils.listFilesAndDirs(dirFile, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        if (files != null && files.size() > 0) {
            if (isEndsWithZip(zipFilePath)) {
                ZipArchiveOutputStream zaos = null;
                try {
                    File zipFile = new File(zipFilePath);
                    zaos = new ZipArchiveOutputStream(zipFile);
                    zaos.setUseZip64(Zip64Mode.AsNeeded);
                    for (File file : files) {
                        if (file != null) {
                            ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getAbsolutePath().replace(parent, ""));
                            zaos.putArchiveEntry(zipArchiveEntry);
                            if (file.isFile()) {
                                IOUtils.write(FileUtils.readFileToByteArray(file), zaos);
                            }
                            zaos.closeArchiveEntry();
                        }
                    }
                    zaos.finish();
                } finally {
                    if (zaos != null) {
                        zaos.close();
                    }
                }
            }
        }
        return new File(zipFilePath).getPath();
    }

    /**
     * 判断文件名是否以.zip为后缀
     *
     * @param fileName 需要判断的文件名
     * @return 是zip文件返回true, 否则返回false
     */
    public static boolean isEndsWithZip(String fileName) {
        boolean flag = false;
        if (fileName != null && !"".equals(fileName.trim())) {
            if (fileName.endsWith(".ZIP") || fileName.endsWith(".zip")) {
                flag = true;
            }
        }
        return flag;
    }

}