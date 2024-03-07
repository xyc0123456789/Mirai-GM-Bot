package com.king.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class StreamUtil {


    public static String getStringFromResources(String path){
        try {
            InputStream resourceAsStream = StreamUtil.class.getClassLoader().getResourceAsStream(path);
            ByteArrayOutputStream outputStream = StreamUtil.inputStreamToOutputStream(resourceAsStream);
            if(outputStream != null) {
                return outputStream.toString(String.valueOf(StandardCharsets.UTF_8));
            }
        }catch (Exception e){
            log.error("", e);
        }
        return null;
    }

    public static File writeToFile(String filePath, InputStream inputStream) {
        // 检查参数是否合法
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("filePath cannot be null or empty");
        }
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream cannot be null");
        }
        try {
            // 如果输入流已经关闭，抛出异常
            inputStream.available();
        } catch (IOException e) {
            throw new IllegalArgumentException("inputStream cannot be closed", e);
        }
        // 创建目标文件
        File file = new File(filePath);
        try {
            // 如果文件不存在，创建文件
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
            return null;
        }
        // 创建输出流
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            // 创建缓冲区
            byte[] buffer = new byte[1024];
            // 循环读取和写入数据
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            // 处理异常
            log.error("", e);
            return null;
        }
        // 返回结果
        return file;
    }


    public static ByteArrayOutputStream inputStreamToOutputStream(InputStream inputStream){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buff = new byte[4096];
            int len;
            while ((len = inputStream.read(buff))!=-1){
                outputStream.write(buff,0, len);
            }
            outputStream.flush();
            return outputStream;
        }catch (Exception e){
            log.error("inputStreamToOutputStream err", e);
            return null;
        }
    }

    public static InputStream outputStreamToinputStream(ByteArrayOutputStream outputStream){
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

}
