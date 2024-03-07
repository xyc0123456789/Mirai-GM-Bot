package com.king.util.openai.component.impl;

import com.king.util.openai.component.ResourceFactory;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.function.KeyStrategyFunction;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description: client工厂类
 * @author: xyc0123456789
 * @create: 2023/5/29 22:50
 **/
@Slf4j
public class OpenAiStreamClientFactory implements ResourceFactory<OpenAiStreamClient> {

    private final List<String> apiKeys;

    public OpenAiStreamClientFactory(List<String> apiKeys) {
        this.apiKeys = apiKeys;
    }

    @Override
    public OpenAiStreamClient createResource() {
        Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1", 7078));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        //！！！生产或者测试环境建议设置为这三种级别：NONE,BASIC,HEADERS,！！！
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .proxy(proxy)
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        return OpenAiStreamClient.builder()
                .apiKey(apiKeys)
                //自定义key的获取策略：默认KeyRandomStrategy
//                .keyStrategy(new KeyRandomStrategy())
                .keyStrategy((KeyStrategyFunction<List<String>, String>) keys -> keys.get(0))
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传（(关注公众号回复：openai ，获取免费的测试代理地址)）
//                .apiHost("https://自己代理的服务器地址/")
                .build();
    }
}
