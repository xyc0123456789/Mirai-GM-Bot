package com.king.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class RateLimiter {

    private final int time; // 时间间隔
    private final int count; // 间隔内可用次数

    private int remainCount; // 剩余可用次数
    private long lastTime; // 上一次使用时间
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private final long interval;

    public RateLimiter(int time, int count) {
        this.time = time;
        this.count = count;
        this.remainCount = count;
        this.interval = time/count;
        this.lastTime = System.currentTimeMillis();
    }

    public boolean get() {
        lock.lock();
        try {
            long now = System.currentTimeMillis();
            if (now - lastTime >= time) { // 超过时间间隔，重置可用次数和时间
                remainCount = count;
            }else {
                remainCount += (now-lastTime)/interval;
            }

            if (remainCount > 0) { // 还有可用次数
                remainCount--;
                lastTime = now;
                return true;
            } else { // 已经用完可用次数，需要等待
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean getByWait() {
        lock.lock();
        try {
            long now = System.currentTimeMillis();
            if (now - lastTime >= time) { // 超过时间间隔，重置可用次数和时间
                remainCount = count;
            }else {
                remainCount += (now-lastTime)/interval;
            }

            if (remainCount > 0) { // 还有可用次数
                remainCount--;
                lastTime = now;
            } else {
                long waitTime = interval - (now - lastTime);
                log.info("need wait time:{} ms", waitTime);
                try {Thread.sleep(waitTime);} catch (InterruptedException ignore) {}
                lastTime = System.currentTimeMillis();
            }
        } finally {
            lock.unlock();
        }
        return true;
    }


    public static void main(String[] args) throws InterruptedException {

        RateLimiter limiter = new RateLimiter(60000, 5);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 3, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executor.submit(()->{
                boolean result = limiter.get();
                System.out.println(System.currentTimeMillis()+": Request " + (finalI + 1) + ": " + result);
            });
        }

        try {Thread.sleep(13000);} catch (InterruptedException ignore) {}
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executor.submit(()->{
                boolean result = limiter.get();
                System.out.println(System.currentTimeMillis()+": Request " + (finalI + 1) + ": " + result);
            });
        }


        for (int i = 0; i < 20; i++) {
            int finalI = i;
            executor.submit(()->{
                boolean result = limiter.getByWait();
                System.out.println(System.currentTimeMillis()+": Request " + (finalI + 1) + ": " + result);
            });
        }
    }
}
