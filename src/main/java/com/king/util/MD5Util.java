package com.king.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description: md5
 * @author: xyc0123456789
 * @create: 2023/3/17 22:19
 **/
public class MD5Util {

    public static String md5(String input) {
        try {
            // 获取MD5摘要算法实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 将输入字符串转换为字节数组并进行哈希计算
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            // 将哈希值转换为十六进制字符串并返回
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // 如果发生异常，则返回空字符串
            return "";
        }
    }

    public static String md5File(String filePath) {
        try {
            // 获取MD5摘要算法实例
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 创建文件输入流并包装为摘要输入流
            try (DigestInputStream dis = new DigestInputStream(Files.newInputStream(Paths.get(filePath)), md)) {
                // 读取文件
                while (dis.read() != -1) {}
            }
            // 获取哈希值并转换为十六进制字符串
            byte[] hash = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            // 如果发生异常，则返回空字符串
            return "";
        }
    }

    public static void main(String[] args) {
        String hello = md5("hellohellohellohellohello");
        System.out.println(hello);
    }

}
