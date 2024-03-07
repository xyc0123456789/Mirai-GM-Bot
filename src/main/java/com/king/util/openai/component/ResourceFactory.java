package com.king.util.openai.component;

/**
 * @description: 工厂类
 * @author: xyc0123456789
 * @create: 2023/5/29 22:40
 **/
public interface ResourceFactory<T> {
    T createResource();
}
