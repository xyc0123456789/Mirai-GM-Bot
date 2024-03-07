package com.king.util;

import com.king.model.MessageEventContext;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyStringUtil {

    public static String getThreeStr(int integer){
        return getFormationStr(integer, 3);
    }
    public static String getTwoStr(int integer){
        return getFormationStr(integer, 2);
    }
    public static String getFormationStr(int integer, int maxLength){
        return String.format("%0"+maxLength+"d",integer);
    }

    public static String getFormationStrWithBlank(int integer, int maxLength){
        return String.format("%"+maxLength+"s", integer);
    }

    public static String getFormationStrWithBlankHTML(int integer, int maxLength){
        return String.format("%"+maxLength+"s", integer).replaceAll(" ","&ensp;");
    }

    public static String getFormationStrWithBlankHTML(String integer, int maxLength){
        return String.format("%"+maxLength+"s", integer).replaceAll(" ","&ensp;");
    }

    public static boolean containSensitiveStr(String key, Set<String> senWord) {
        for (String senKey : senWord) {
            //启用正则
            if (senKey.startsWith("~")) {
                Pattern pattern = Pattern.compile(senKey.substring(1));
                Matcher matcher = pattern.matcher(key);
                if (matcher.find()) {
                    return true;
                }
            } else {
                //普通匹配
                if (senKey.equalsIgnoreCase(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * ■★
     * @param oriStr
     * @return
     */
    public static String sensitiveMask(String oriStr){
        if (isEmpty(oriStr)){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<oriStr.length();i++){
            stringBuilder.append("■");
        }
        return stringBuilder.toString();
    }

    public static boolean isEmpty(String string){
        return string==null||string.trim().length()==0;
    }

    public static boolean isEquel(Long[] longs,long num){
        for (Long i:longs){
            if (i.equals(num)){
                return true;
            }
        }
        return false;
    }

    public static String getSubString(MessageEventContext messageEventContext, int commandLen){
        String content = messageEventContext.getContent();
        if (MyStringUtil.isEmpty(content)){
            return null;
        }
        if (commandLen<=0){
            return content.trim();
        }
        if (content.length()<=commandLen){
            return null;
        }

        return content.substring(commandLen).trim();
    }

}
