package com.king.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @description: roll
 * @author: xyc0123456789
 * @create: 2023/4/8 11:31
 **/
@Slf4j
public class RollUtil {

    public static String roll(String str) {
        if (MyStringUtil.isEmpty(str)) {
            return String.valueOf((int) (Math.random() * 101));
        }
        str = str.trim();
        if (str.startsWith("#roll") || str.startsWith("roll")) {
            int roll = str.indexOf("roll");
            str = str.substring(roll + 4).trim();
        }
        String[] split = str.split("\\s+");
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (String s : split) {
            if (!MyStringUtil.isEmpty(s)) {
                stringArrayList.add(s);
            }
        }
        double target = ThreadLocalRandom.current().nextDouble();
        if (stringArrayList.size() == 0) {
            return String.valueOf((int) (target * 101));
        }
        double[] doubles = create(stringArrayList.size());
        int i = binarySearch(doubles, target);
        log.info("choose:{} {} target:{} doubles:{}", i, stringArrayList.get(i), target, doubles);
        return stringArrayList.get(i);
    }


    private static double[] create(int n) {
        double[] arr = new double[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (i + 1) * 1.0 / n;
        }
        return arr;
    }


    public static int binarySearch(double[] arr, double target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            System.out.println(roll("#roll "));
        }
    }

}
