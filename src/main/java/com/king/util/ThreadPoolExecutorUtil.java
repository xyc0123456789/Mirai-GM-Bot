package com.king.util;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorUtil {

    private static final Map<String, ThreadPoolExecutor> threadPoolCache = new ConcurrentHashMap<>();

    public static final ThreadPoolExecutor GPTThreadPool = getAQueueExecutor(10);

    public static ThreadPoolExecutor getAThreadExecutor(String key){
        if (!threadPoolCache.containsKey(key)) {
            threadPoolCache.put(key, getAQueueExecutor(10));
        }
        return threadPoolCache.get(key);
    }


    public static ThreadPoolExecutor getAQueueExecutor(int capacity){
        return new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(capacity), new ThreadPoolExecutor.AbortPolicy());
    }

}
