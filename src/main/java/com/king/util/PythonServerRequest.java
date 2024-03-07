package com.king.util;

import com.king.config.CommonConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.king.config.CommonConfig.MDC_TRACE_ID;
import static com.king.config.CommonConfig.botManager;

@Slf4j
public class PythonServerRequest {


    //    private static final ThreadPoolExecutor gptExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.AbortPolicy());
    public static Lock lock = new ReentrantLock(true);

    public static String textGenerator(String input, String length) throws Exception {
        String url = "http://127.0.0.1:5000";
        HttpClient httpClient = new HttpClient(url, 30000, 30000, false);
        Map<String, String> stringObjectMap = new HashMap<>();
        stringObjectMap.put("input", input);
        stringObjectMap.put("length", length);
        int status = httpClient.sendPost(stringObjectMap);
        String result = httpClient.getResult();
        log.info("textGenerator request input:{},status:{},result:{}", input, status, result);
        return result;
    }

    public static String chatGPT(String input) throws Exception {
        return chatGPT(input, null);
    }

    public static String chatGPT(String input, String id) throws Exception {
        return chatGPT(input, id, 0);
    }

    public static String chatGPT(String input, String id, int bot_id) throws Exception {
        try {
            botManager.addBotCount(bot_id);
            Future<String> stringFuture = chatGPTSubmit(input, id, bot_id);
            return stringFuture.get();
        } catch (RejectedExecutionException rejectedExecutionException) {
            return "请求失败: 请求过快,请等待之前若干请求返回";
        } catch (Exception e) {
            log.error("chatGPT execute err", e);
            return "请求失败: 系统错误-" + e.getMessage();
        } finally {
            botManager.decBotCount(bot_id);
        }
    }

    public static Future<String> chatGPTSubmit(String input, String id, int bot_id) throws Exception {
        String mdc = MDC.get(MDC_TRACE_ID);
        return botManager.getPoolExe(bot_id).submit(() -> {
            try {
                MDC.put(MDC_TRACE_ID, mdc);
                String url = "http://127.0.0.1:5000/chatgpt";
                HttpClient httpClient = new HttpClient(url, 300000, 300000, false);
                Map<String, String> stringObjectMap = new HashMap<>();
                stringObjectMap.put("input", input);
                stringObjectMap.put("id", id);
                stringObjectMap.put("bot_index", String.valueOf(bot_id));
                log.info("gpt request:{} {}", bot_id, input);
                CommonConfig.gptKeyLimiter.getByWait();
                int status = httpClient.sendPost(stringObjectMap);
                String result = httpClient.getResult();
                int indexOf = result.indexOf("\\\n");
                if (result.contains("<!doctype html>") && result.contains("<title>500 Internal Server Error</title>")) {
                    log.info("gpt request input:{},status:{}", input, status);
                    throw new RuntimeException("网络错误，请重试");
                }else if (result.contains("Too many requests in 1 hour")){
                    throw new RuntimeException("Too many requests in 1 hour");
                } else if (indexOf !=-1&&"'str' object has no attribute 'get'".equals(result.substring(indexOf +2))) {
                    throw new RuntimeException("other err");
                } else if (result.trim().startsWith("HTTPSConnectionPool")){
//                    try {Thread.sleep(15000);} catch (InterruptedException ignore) {}
                    throw new RuntimeException("网络代理连接异常，请稍后再试");
                }
//                    log.info("gpt request input:{},status:{},result:{}", input, status, result);
                return result;
            } catch (Exception e) {
                log.error("gpt request err", e);
                if (botManager.getBotStatusDTOList().get(bot_id).mayAccessable()) {
                    botManager.setFail(bot_id);
                }
//                try {Thread.sleep(60000);} catch (InterruptedException ignore) {}
                return "请求失败: " + e.getMessage();
            } finally {
                MDC.remove(MDC_TRACE_ID);
            }
        });
    }
    public static Future<String> chatGPTSubmitPOE(String input, String id, int bot_id) throws Exception {
        String mdc = MDC.get(MDC_TRACE_ID);
        return botManager.getPoolExe(0).submit(() -> {
            try {
                MDC.put(MDC_TRACE_ID, mdc);
                Thread.sleep(1000);
                int count = 3;
                while (count-->0){
                    String response = POEClient.getResponse(input);
                    if (!MyStringUtil.isEmpty(response)){
                        return response;
                    }
                }
                throw new RuntimeException("read time out");
            } catch (Exception e) {
                log.error("gpt request err", e);
                return "请求失败: " + e.getMessage();
            } finally {
                MDC.remove(MDC_TRACE_ID);
            }
        });
    }

    public static String antiOffensive(String input) throws Exception {
        String url = "http://127.0.0.1:5000/anti_offensive";
        HttpClient httpClient = new HttpClient(url, 30000, 30000, false);
        Map<String, String> stringObjectMap = new HashMap<>();
        stringObjectMap.put("input", input);
        int status = httpClient.sendPost(stringObjectMap);
        String result = httpClient.getResult();
        log.info("antiOffensive request input:[{}],status:[{}],result:[{}]", input, status, result);
        return result;
    }

    public static String filterSenti(String input) throws Exception {
        String url = "http://127.0.0.1:5000/filter_senti";
        HttpClient httpClient = new HttpClient(url, 30000, 30000, false);
        Map<String, String> stringObjectMap = new HashMap<>();
        stringObjectMap.put("input", input);
        int status = httpClient.sendPost(stringObjectMap);
        String result = httpClient.getResult();
        log.info("filterSenti request input:[{}],status:[{}],result:[{}]", input, status, result);
        return result;
    }
}
