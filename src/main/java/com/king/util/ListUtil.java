package com.king.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 分页工具
 * @author: xyc0123456789
 * @create: 2023/5/9 22:16
 **/
public class ListUtil {



    public static <T> List<List<T>> paginateList(List<T> list, int pageSize) {
        if (list == null || list.isEmpty() || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid input parameters");
        }

        List<List<T>> paginatedList = new ArrayList<>();
        int totalItems = list.size();
        int totalPages = (totalItems + pageSize - 1) / pageSize;

        for (int i = 0; i < totalPages; i++) {
            int startIndex = i * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalItems);
            List<T> page = new ArrayList<>(list.subList(startIndex, endIndex));
            paginatedList.add(page);
        }
        return paginatedList;
    }


    public static <T> List<T> randomList(List<T> list, int randomNum) {
        List<T> result = new ArrayList<>();
        if (list == null || list.isEmpty() || randomNum <= 0) {
            return result;
        }
        int listSize = list.size();
        if (randomNum >= listSize) {
            return new ArrayList<>(list);
        }
        Random random = new Random();
        for (int i = 0; i < randomNum; i++) {
            int randomIndex = random.nextInt(listSize - i);
            result.add(list.get(randomIndex));
            Collections.swap(list, randomIndex, listSize - i - 1);
        }
        return result;
    }

    public static Set<Long> extractNumbers(String input) {
        Set<Long> numbers = new HashSet<>();
        Pattern pattern = Pattern.compile("@(\\d+)");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            Long number = Long.parseLong(matcher.group(1));
            numbers.add(number);
        }
        return numbers;
    }

    public static String replaceNumbers(String input, Set<Long> numbers, String replacement) {
        String regex = "@(\\d+)";
        for (Long number : numbers) {
            regex += "|" + "@" + number + "";
        }
        return input.replaceAll(regex, replacement);
    }

    public static void main(String[] args) {
//        List<Integer> numbers = new ArrayList<>();
//        for (int i = 1; i <= 19; i++) {
//            numbers.add(i);
//        }
//
//        int pageSize = 30;
//        List<List<Integer>> paginatedNumbers = paginateList(numbers, pageSize);
//        System.out.println("Paginated numbers: " + paginatedNumbers);
//        List<Integer> random = randomList(numbers, 5);
//        System.out.println("random numbers: " + random);
        String input = "The numbers are @123 and @456";
        System.out.println(input.replaceAll("@(\\d+)\\s*", ""));
    }


}
