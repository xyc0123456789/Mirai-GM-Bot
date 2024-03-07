package com.king.util.openai;

import com.king.util.openai.component.StreamEventCommonListener;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.billing.BillingUsage;
import com.unfbx.chatgpt.entity.billing.Subscription;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.function.KeyStrategyFunction;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description: openai api
 * @author: xyc0123456789
 * @create: 2023/5/28 22:20
 **/
@Slf4j
public class OpenAIApiTest {

    public static OpenAiStreamClient client;

    public static final Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1", 7078));

    public OpenAIApiTest(String[] keys) {
        init(Arrays.asList(keys));
    }

    public static void init(List<String> apiKeys) {
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
        client = OpenAiStreamClient.builder()
                .apiKey(apiKeys)
                //自定义key的获取策略：默认KeyRandomStrategy
//                .keyStrategy(new KeyRandomStrategy())
                .keyStrategy((KeyStrategyFunction<List<String>, String>) keys -> keys.get(0))
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传（(关注公众号回复：openai ，获取免费的测试代理地址)）
//                .apiHost("https://自己代理的服务器地址/")
                .build();
    }


    public void subscription() {
        Subscription subscription = client.subscription();
        log.info("用户名：{}", subscription.getAccountName());
        log.info("用户总余额（美元）：{}", subscription.getHardLimitUsd());
//        log.info("更多信息看Subscription类");
    }


    public void billingUsage() {
        LocalDate start = LocalDate.of(2023, 3, 1);
        BillingUsage billingUsage = client.billingUsage(start, LocalDate.now());
        log.info("总使用金额（美分）：{}", billingUsage.getTotalUsage());
        log.info("更多信息看BillingUsage类");
    }

//    public void creditGrants() {
//        CreditGrantsResponse creditGrantsResponse = client.creditGrants();
//        log.info("账户总余额（美元）：{}", creditGrantsResponse.getTotalGranted());
//        log.info("账户总使用金额（美元）：{}", creditGrantsResponse.getTotalUsed());
//        log.info("账户总剩余金额（美元）：{}", creditGrantsResponse.getTotalAvailable());
//    }

    /**
     * 异步的，需要使用eventSourceListener.getResponse().getCountDownLatch().await();来同步
     * @param messageList
     * @return
     */
    public static StreamEventCommonListener request(List<Message> messageList) {
        return request(messageList, ChatCompletion.Model.GPT_3_5_TURBO.getName(), 0.7, 4000);
    }

    /**
     * 异步的，需要使用eventSourceListener.getResponse().getCountDownLatch().await();来同步
     * @param messageList
     * @param model
     * @param temperature
     * @param maxTokens
     * @return
     */
    public static StreamEventCommonListener request(List<Message> messageList, String model, double temperature, int maxTokens) {
        StreamEventCommonListener eventSourceListener = new StreamEventCommonListener();
        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .model(model)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .messages(messageList)
                .stream(true)
                .build();
        chatCompletion.setMaxTokens((int) (maxTokens-chatCompletion.tokens()));
        client.streamChatCompletion(chatCompletion, eventSourceListener);
        return eventSourceListener;
    }


//    public StreamEventCommonListener chatCompletions() {
//        StreamEventCommonListener eventSourceListener = new StreamEventCommonListener();
//        Message message = Message.builder().role(Message.Role.USER).content("hello").build();
//        ChatCompletion chatCompletion = ChatCompletion
//                .builder()
//                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
//                .temperature(0.7)
//                .maxTokens(4000)
//                .messages(Arrays.asList(message))
//                .stream(true)
//                .build();
//        client.streamChatCompletion(chatCompletion, eventSourceListener);
//        try {
//            eventSourceListener.getResponse().getCountDownLatch().await();
//        }catch (InterruptedException e){
//            log.error("", e);
//        }
//        log.info(eventSourceListener.getResponse().toString());
//        return eventSourceListener;
//    }
//
//
//    public void completions() {
//        ConsoleEventSourceListener eventSourceListener = new ConsoleEventSourceListener();
//        Completion q = Completion.builder()
//                .prompt("我想申请转专业，从计算机专业转到会计学专业，帮我完成一份两百字左右的申请书")
//                .stream(true)
//                .build();
//        client.streamCompletions(q, eventSourceListener);
//        CountDownLatch countDownLatch = new CountDownLatch(1);
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        OpenAIApiTest openAIApiTest = new OpenAIApiTest(new String[]{"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"});
//        openAIApi.subscription();
//        openAIApi.billingUsage();
//        openAIApi.chatCompletions();
    }

}
