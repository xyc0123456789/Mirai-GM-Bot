package com.king.util;

import com.king.config.CommonConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: EmojiKitchen
 * @author: xyc0123456789
 * @create: 2023/3/27 23:30
 **/
@Slf4j
public class EmojiKitchenUtil {

    private static Map<String, String> urls;

    private static final String REG = "\u2764\ufe0f\u200d\ud83e\ude79|\ud83d\ude2e\u200d\ud83d\udca8|\ud83d\ude36\u200d\ud83c\udf2b\ufe0f|[\u2601-\u2b50]\ufe0f?|[\ud83c\udc04-\ud83c\udff9]\ufe0f?|[\ud83d\udc0c-\ud83d\udefc]\ufe0f?|[\ud83e\udd0d-\ud83e\udee7]";


    static {
        try {
            InputStream resourceAsStream = EmojiKitchenUtil.class.getClassLoader().getResourceAsStream("static/image_urls.json");
            urls = JsonUtil.fromJson(StreamUtil.inputStreamToOutputStream(resourceAsStream).toString(), Map.class);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static File cook(String message){
        Pattern pattern = Pattern.compile(REG);
        int count = 0;
        String first=null,second=null;
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()){
            if (count==0) {
                first=matcher.group();
            }else {
                second=matcher.group();
                break;
            }
            count++;
        }
        if (first!=null&&second!=null&&message.startsWith(first+second)){
            File cook = cook(first, second);
            if (cook==null){
                return cook(second, first);
            }else {
                return cook;
            }
        }
        return null;
    }

    public static boolean find(String message){
        Pattern pattern = Pattern.compile(REG);
        int count = 0;
        String first=null,second=null;
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()){
            if (count==0) {
                first=matcher.group();
            }else {
                second=matcher.group();
                break;
            }
            count++;
        }
        return first!=null&&second!=null&&message.startsWith(first+second);
    }


    public static File cook(String first, String second) {
//        log.info(first+"\t"+second);
        String filename = toUnicode(first)+"_"+toUnicode(second)+".png";
        File file = new File(CommonConfig.getWorkingDir() + "download/image/emoji/" + filename);
        log.info(file.getAbsolutePath());
        if (file.exists()) {
            return file;
        }
        String url = urls.get(filename);
        log.info(filename+" "+url);
        if (url!=null) {
            return FileUtil.downLoadImage(url, "download/image/emoji/" + filename);
        }else {
            return null;
        }
    }


    public static String toUnicode(String str){
        return "u"+Integer.toHexString(str.codePointAt(0));
    }


}
