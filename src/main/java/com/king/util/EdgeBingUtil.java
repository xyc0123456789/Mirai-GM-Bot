package com.king.util;

import com.king.component.MyBot;
import com.king.model.NewBingResponse;
import com.king.model.QQFriendId;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Friend;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/5/25 14:12
 **/

@Slf4j
public class EdgeBingUtil {

    private static final ThreadPoolExecutor threadPoolExecutor = ThreadPoolExecutorUtil.getAQueueExecutor(10);

    public static NewBingResponse askBing(String input, String id){
        String mdc = MDC.get(MDC_TRACE_ID);
        String resp = "";
        NewBingResponse response = new NewBingResponse();
        try {
            Future<NewBingResponse> stringFuture = threadPoolExecutor.submit(() -> {
                try {
                    String result = ask(input, mdc, id);

                    Map map = JsonUtil.fromJson(result, Map.class);
                    String message = (String) map.get("message");

                    response.setResponse(message);
                    if (message!=null && !message.startsWith("error")){
                        List<String> refer = (List<String>) map.get("refer");
                        List<String> suggestions = (List<String>) map.get("suggestions");
                        response.setReferences(refer);
                        response.setSuggestedResponses(suggestions);
                    }
                    return response;
                } finally {
                    MDC.remove(MDC_TRACE_ID);
                }
            });
            return stringFuture.get();
        } catch (RejectedExecutionException rejectedExecutionException) {
            resp = "被请求过快";
        } catch (Exception e) {
            log.error("chatGPT execute err", e);
            resp = e.getMessage();
        }
        Friend friend = MyBot.bot.getFriend(QQFriendId.ME);
        if (friend!=null){
            friend.sendMessage(resp);
        }
        response.setResponse(resp);
        return response;
    }


    private static String ask(String input, String mdc, String id) {
        int bot_id = 0;
        try {
            MDC.put(MDC_TRACE_ID, mdc);
            String url = "http://127.0.0.1:5000/bing";
            HttpClient httpClient = new HttpClient(url, 300000, 300000, false);
            Map<String, String> stringObjectMap = new HashMap<>();
            stringObjectMap.put("input", input);
            stringObjectMap.put("id", id);
            stringObjectMap.put("bot_index", String.valueOf(bot_id));
            log.info("edgeBing request:{} {}", bot_id, input);
            int status = httpClient.sendPost(stringObjectMap);
            String result = httpClient.getResult();
            if (result.contains("<!doctype html>") && result.contains("<title>500 Internal Server Error</title>")) {
                log.info("input:{},status:{}", input, status);
                throw new RuntimeException("网络错误，请重试");
            }else if (result.trim().startsWith("HTTPSConnectionPool")){
                throw new RuntimeException("网络代理连接异常，请稍后再试");
            }
            return result;
        } catch (Exception e) {
            if (e instanceof RuntimeException){
                log.error(e.getMessage());
            }else {
                log.error("", e);
            }
            try {Thread.sleep(60000);} catch (InterruptedException ignore) {}
            return "";
        } finally {
            MDC.remove(MDC_TRACE_ID);
        }
    }

    public static void main(String[] args) {
        System.out.println(askBing("hello, what's data today?", ""));
    }
}
