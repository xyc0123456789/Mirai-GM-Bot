package com.king.util;
import java.util.*;

public class TimeoutCache<K, V> {
    private final Map<K, V> cache = new HashMap<>();
    private final Map<K, Timer> timers = new HashMap<>();
    private final Map<K, TimeoutCallback<V>> callbacks = new HashMap<>();

    /**
     *
     * @param key
     * @param value
     * @param timeout 单位：毫秒
     * @param callback
     */
    public void put(K key, V value, long timeout, TimeoutCallback<V> callback) {
        synchronized (cache) {
            cache.put(key, value);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    synchronized (cache) {
                        if (cache.containsKey(key)) {
                            cache.remove(key);
                            if (callback != null) {
                                callback.onTimeout(value);
                            }
                        }
                        timers.remove(key);
                        callbacks.remove(key);
                    }
                }
            };
            Timer timer = new Timer(true);
            timer.schedule(task, timeout);
            timers.put(key, timer);
            callbacks.put(key, callback);
        }
    }

    public V get(K key) {
        synchronized (cache) {
            if (cache.containsKey(key)) {
                V value = cache.get(key);
                cache.remove(key);
                Timer timer = timers.get(key);
                if (timer != null) {
                    timer.cancel();
                }
                timers.remove(key);
                TimeoutCallback<V> callback = callbacks.get(key);
                if (callback != null) {
                    callback.onAccess(value);
                }
                callbacks.remove(key);
                return value;
            }
        }
        return null;
    }

    public V read(K key) {
        synchronized (cache) {
            return cache.get(key);
        }
    }

    public int size() {
        synchronized (cache) {
            return cache.size();
        }
    }

    public List<V> getUuids() {
        synchronized (cache){
            return new ArrayList<>(cache.values());
        }
    }

    public interface TimeoutCallback<V> {
        void onTimeout(V value);
        void onAccess(V value);
    }


    public static void main(String[] args) throws InterruptedException {
        TimeoutCache<String, Integer> cache = new TimeoutCache<>();
        TimeoutCallback timeoutCallback = new TimeoutCallback() {
            @Override
            public void onTimeout(Object value) {
                System.out.println("Key timed out with value " + value);
            }

            @Override
            public void onAccess(Object value) {
                System.out.println("Key accessed with value " + value);
            }
        };

        cache.put("key1", 123, 5000, timeoutCallback); // 存储键值对，超时时间为 5 秒，并传入回调函数对象
        cache.put("key2", 456, 10000, timeoutCallback); // 存储键值对，超时时间为 10 秒，不传入回调函数对象
        System.out.println(cache.get("key1")); // 获取键 key1 对应的值，输出 123，并从缓存中删除键值对和对应的回调函数对象和超时计时器
        Thread.sleep(6000); // 等待 6 秒，让键 key1 超时
        System.out.println(cache.get("key1")); // 获取键 key1 对应的值，输出 null
        System.out.println(cache.size()); // 获取缓存中键值对的数量，输出 1
        System.out.println(cache.get("key1")); // 获取键 key1 对应的值，输出 123，并从缓存中删除键值对
        Thread.sleep(6000); // 等待 6 秒，让键 key1 超时
        System.out.println(cache.get("key1")); // 获取键 key1 对应的值，输出 null
        System.out.println(cache.size()); // 获取缓存中键值对的数量，输出 1
        Thread.sleep(6000); // 等待 6 秒，让键 key1 超时
        System.out.println(cache.size()); // 获取缓存中键值对的数量，输出 1
    }

}