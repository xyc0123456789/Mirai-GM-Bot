package com.king.util;

import cn.hutool.core.date.DateUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateFormateUtil {

    public static String getCurYYYYMMDDTHHMMSS(){
        return DateUtil.format(new Date(), "yyyyMMdd'T'HHmmss");
    }


    public static Date praseCurYYYYMMDDTHHMMSS(String formatDate){
        return DateUtil.parse(formatDate, "yyyyMMdd'T'HHmmss");
    }

    public static String formatYYYYMMDDTHHMMSS(Date date){
        return DateUtil.format(date, "yyyy-MM-dd'T'HH:mm:ss");
    }

    public static String formatYYYYMMDDHHMMSS(Date date){
        return DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatYYMMDDHHMMSSOnlyNum(Date date){
        return DateUtil.format(date, "yyMMddHHmmss");
    }

    public static String formatYYYYMMDD(Date date){
        return DateUtil.format(date, "yyyyMMdd");
    }

    public static String formatHHMMSS(Date date){
        return DateUtil.format(date, "HHmmss");
    }


    public static Long deltaDateWithMS(Date leftDate,Date rightDate){
        return leftDate.getTime()-rightDate.getTime();
    }

    public static Date offDate(int offset){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //获取下一天的时间
        calendar.add(Calendar.DAY_OF_YEAR,offset);
        return calendar.getTime();
    }

    public static Date offSecond(int offset){
        return offSecond(new Date(), offset);
    }

    public static Date offSecond(Date date, int offset){
        if (date == null){
            date = new Date();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //获取下一天的时间
        calendar.add(Calendar.SECOND, offset);
        return calendar.getTime();
    }

    public static Date getOffsetDate000000(int offset){
        return praseCurYYYYMMDDTHHMMSS(formatYYYYMMDD(offDate(offset)) + "T000000");
    }

    public static boolean checkDate(){
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 定义起始日期
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        // 计算当前日期与起始日期之间的天数差
        long days = ChronoUnit.DAYS.between(startDate, currentDate);
        // 计算余数
        int remainder = (int) (days % 3);
        System.out.println("days delta:" + days + " remainder: " + remainder);
        return remainder == 0;
    }

    public static void main(String[] args) {

//        System.out.println(formatYYYYMMDDTHHMMSS(null));
        checkDate();
    }

}
