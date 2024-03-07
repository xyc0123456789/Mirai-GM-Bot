package com.king.util;

import com.king.config.CommonConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class FileUtil {

//    public static Account getAccountInfo(File file){
//        String account = readFile(file);
//        String[] split = account.split("\\s*\n\\s*", -1);
//        Map<String,String> map = new HashMap<>();
//        for (String row:split){
//            if (MyStringUtil.isEmpty(row)){
//                continue;
//            }
//            if (row.startsWith("#")){
//                continue;
//            }
//            String[] keyValue = row.split("=");
//            if (keyValue.length!=2){
//                continue;
//            }
//            map.put(keyValue[0],keyValue[1]);
//        }
//        return BeanUtil.toBeanIgnoreError(map, Account.class);
//    }

    public static String readFile(File file){
        ByteArrayOutputStream outputStream = null;
        BufferedInputStream inputStream = null;
        byte[] buffer = new byte[1024];
        try {
            outputStream = new ByteArrayOutputStream();
            inputStream = new BufferedInputStream(Files.newInputStream(file.toPath()));
            int len;
            while ((len = inputStream.read(buffer))!=-1){
                outputStream.write(buffer,0,len);
            }
        }catch (Exception e){
            log.error("ioException:",e);
            return null;
        }finally {
            try {
                if (outputStream!=null){
                    outputStream.flush();
                    outputStream.close();
                }
            }catch (Exception e){
                log.error("",e);
            }
            try {
                if (inputStream!=null){
                    inputStream.close();
                }
            }catch (Exception e){
                log.error("",e);
            }
        }
        return outputStream.toString();
    }
    public static File downLoadImage(String urlStr, String savePath){
        return downLoadImage(urlStr, savePath, false);
    }

    public static File downLoadImage(String urlStr, String savePath, boolean isLocal){
        InputStream is=null;
        OutputStream os=null;
        File sf=null;
        try {
            if (savePath==null||savePath.trim().length()==0){
                savePath = getFileNameFromUrl(urlStr);
            }
            // 构造URL
            URL url = new URL(urlStr);
            // 打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5*1000);
            // 输入流
            is = con.getInputStream();

            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            if (isLocal){
                sf = new File(savePath);
            }else {
                sf = new File(CommonConfig.getWorkingDir() + savePath);
            }
            if(sf.getParentFile()!=null && !sf.getParentFile().exists()){
                sf.getParentFile().mkdirs();
            }

            os = Files.newOutputStream(Paths.get(sf.getPath()));
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.flush();
        }catch (Exception e){
            log.error("下载失败", e);
            sf=null;
        }finally {
            if (os!=null) {
                try {
                    os.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (is!=null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return sf;
    }

    public static void outPutToFile(String content,File target){
        outPutToFile(content.getBytes(StandardCharsets.UTF_8), target);
    }
    public static void outPutToFile(byte[] content,File target){
        BufferedOutputStream outputStream = null;
        BufferedInputStream inputStream = null;
        byte[] buffer = new byte[1024];
        try {
            outputStream = new BufferedOutputStream(Files.newOutputStream(target.toPath()));
            inputStream = new BufferedInputStream(new ByteArrayInputStream(content));
            int len;
            while ((len = inputStream.read(buffer))!=-1){
                outputStream.write(buffer,0,len);
            }
        }catch (Exception e){
            log.error("ioException:",e);
        }finally {
            try {
                if (outputStream!=null){
                    outputStream.flush();
                    outputStream.close();
                }
            }catch (Exception e){
                log.error("",e);
            }
            try {
                if (inputStream!=null){
                    inputStream.close();
                }
            }catch (Exception e){
                log.error("",e);
            }
        }
    }

    public static void appendToFile(String content,File target){
        appendToFile(content.getBytes(StandardCharsets.UTF_8), target);
    }

    public static void appendToFile(byte[] content, File target){
        BufferedOutputStream outputStream = null;
        BufferedInputStream inputStream = null;
        byte[] buffer = new byte[1024];
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(target.getAbsolutePath(), true));
            inputStream = new BufferedInputStream(new ByteArrayInputStream(content));
            int len;
            while ((len = inputStream.read(buffer))!=-1){
                outputStream.write(buffer,0,len);
            }
        }catch (Exception e){
            log.error("ioException:",e);
        }finally {
            try {
                if (outputStream!=null){
                    outputStream.flush();
                    outputStream.close();
                }
            }catch (Exception e){
                log.error("",e);
            }
            try {
                if (inputStream!=null){
                    inputStream.close();
                }
            }catch (Exception e){
                log.error("",e);
            }
        }


    }

    public static String getFileNameFromUrl(String url){
        int start=0;
        for (int j=url.length()-1;j>=0;j--){
            if (url.charAt(j)=='/'){
                start=j+1;
                break;
            }
        }
        return url.substring(start).replaceAll("[\\\\/:*?\"<>|]","");
    }

    public static void deleteFile(File file){
        if (file!=null && file.exists()){
            boolean delete = file.delete();
            if (!delete){
                log.error(file.getAbsolutePath()+" delete failed");
            }else {
                log.error(file.getAbsolutePath()+" delete success");
            }
        }
    }
}
