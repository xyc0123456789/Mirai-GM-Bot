package com.king.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipUtil {


    public static File sourceToZip(String sourcePath, String targetPath) throws IOException {

        File sourceFile = new File(sourcePath);

        File targetFile = new File(targetPath);

        if (!sourceFile.exists()) throw new RuntimeException("目标文件不存在");

        if (!targetFile.isDirectory()) throw new RuntimeException("目标路径不是一个文件夹");

        File targetZipFile = null;

        if (!sourceFile.isDirectory()) {
            targetZipFile = new File(targetFile, sourceFile.getName().substring(0, sourceFile.getName().indexOf(".")) + ".zip");
        } else {
            targetZipFile = new File(targetFile, sourceFile.getName() + ".zip");
        }

        ZipOutputStream zip = null;
        try {
            zip = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(targetZipFile.toPath())));

            if (sourceFile.isFile()) {
                toZip(sourceFile, zip, "");
            } else {
                fileToZip(sourceFile, zip, "");
            }
        } catch (IOException e) {
            log.error("", e);
        } finally {
            if (zip != null) {
                zip.close();
            }
        }
        return targetZipFile;
    }

    //文件压缩
    private static void toZip(File file, ZipOutputStream zip, String targetPath) throws IOException {

        ZipEntry zipEntry = new ZipEntry(targetPath + file.getName());
        zip.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        InputStream read = Files.newInputStream(file.toPath());
        int ch = -1;
        while ((ch = read.read(bytes)) != -1) {
            zip.write(bytes, 0, ch);
        }

        read.close();
        zip.closeEntry();
    }

    //文件夹压缩
    private static void fileToZip(File file, ZipOutputStream zip, String targetPath) throws IOException {
        targetPath += file.getName() + File.separator;
        ZipEntry entry = new ZipEntry(targetPath);
        zip.putNextEntry(entry);
        zip.closeEntry();
        File[] listFiles = file.listFiles();
        if (listFiles == null || listFiles.length == 0) return;

        for (File file1 : listFiles) {
            if (!file1.isDirectory()) {
                toZip(file1, zip, targetPath);
            } else {
                fileToZip(file1, zip, targetPath);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        sourceToZip("D:\\home\\appuser\\mirai_workplace\\pdfdownload\\20230117T231301", "D:\\home\\appuser\\mirai_workplace\\pdfdownload");
    }


}
