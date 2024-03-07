package com.king.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/18 15:51
 **/
@Slf4j
public class POEClient {

    private static String conversationId="";
    private static String parentMessageId="";

    private static final ThreadPoolExecutor SSEQueue = new ThreadPoolExecutor(1, 1000, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), new ThreadPoolExecutor.AbortPolicy());;

    public static String getResponse(String message) throws Exception {
        String url = "http://127.0.0.1:8090/conversation";
        HttpClient httpClient = new HttpClient(url, 60000, 60000, false);
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("type", "conversation");
        stringObjectMap.put("conversation_id", conversationId);
        stringObjectMap.put("parent_message_id", parentMessageId);
        stringObjectMap.put("text", message);
        int status = httpClient.sendJson(stringObjectMap,null);
        String result = httpClient.getResult();
        Map<String,Object> map = JsonUtil.fromJson(result, Map.class);
        int code = (int) map.get("code");
        String msg = (String) map.get("msg");
        if (status==200&&code==200&&"ok".equalsIgnoreCase(msg)){
            String response = POEStreamResultUtil.readEvents();
            if (MyStringUtil.isEmpty(response)) {
                return "";
            }
            Map json = JsonUtil.fromJson(response, Map.class);
            Map<String,Object> data = (Map<String, Object>) json.get("data");
            return (String) data.get("text");
        }
        return "";
    }

}
