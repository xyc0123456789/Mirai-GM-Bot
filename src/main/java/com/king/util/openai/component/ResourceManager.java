package com.king.util.openai.component;

import com.king.util.openai.component.ResourceFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 资源管理器
 * @author: xyc0123456789
 * @create: 2023/5/29 22:36
 **/
@Slf4j
public class ResourceManager<T> {
    private final T[] resources;
    private final boolean[] isResourceFree;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public ResourceManager(int resourceCount, ResourceFactory<T> factory) {
        resources = (T[]) new Object[resourceCount];
        isResourceFree = new boolean[resourceCount];
        for (int i = 0; i < resourceCount; i++) {
            resources[i] = factory.createResource(); // create new resource
            isResourceFree[i] = true;
        }
    }

    public T acquire() throws InterruptedException {
        lock.lock();
        try {
            while (true) {
                for (int i = 0; i < resources.length; i++) {
                    if (isResourceFree[i]) {
                        log.info("取到client: {}", i);
                        isResourceFree[i] = false;
                        return resources[i];
                    }
                }
                condition.await(); // waituntil a resource is available
            }
        } finally {
            lock.unlock();
        }
    }

    public void release(T resource) {
        lock.lock();
        try {
            for (int i = 0; i < resources.length; i++) {
                if (resources[i] == resource) {
                    log.info("放回client: {}", i);
                    isResourceFree[i] = true;
                    condition.signal(); // signal waiting threads that a resource is available
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
