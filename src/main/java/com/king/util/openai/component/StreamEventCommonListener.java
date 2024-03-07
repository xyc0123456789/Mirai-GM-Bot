package com.king.util.openai.component;

import com.king.util.JsonUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.slf4j.MDC;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

/**
 * @description: sse
 * @author: xyc0123456789
 * @create: 2023/5/28 23:22
 **/
@Slf4j
public class StreamEventCommonListener extends EventSourceListener {

    private OpenAIRequestContext response;

    public StreamEventCommonListener() {
        this.response = new OpenAIRequestContext();
    }

    public StreamEventCommonListener(OpenAIRequestContext response) {
        this.response = response;
    }

    public OpenAIRequestContext getResponse() {
        return response;
    }

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        MDC.put(MDC_TRACE_ID, this.response.getMdcTrace());
        log.info("OpenAI建立sse连接...");
    }

    //{"id":"chatcmpl-7LCKbog4ReJ8LHvUZihohddESaBf1","object":"chat.completion.chunk","created":1685286645,"model":"gpt-3.5-turbo-0301","choices":[{"delta":{"role":"assistant"},"index":0,"finish_reason":null}]}
    //{"id":"chatcmpl-7LCKbog4ReJ8LHvUZihohddESaBf1","object":"chat.completion.chunk","created":1685286645,"model":"gpt-3.5-turbo-0301","choices":[{"delta":{"content":"Hello"},"index":0,"finish_reason":null}]}
    //...
    //{"id":"chatcmpl-7LCKbog4ReJ8LHvUZihohddESaBf1","object":"chat.completion.chunk","created":1685286645,"model":"gpt-3.5-turbo-0301","choices":[{"delta":{},"index":0,"finish_reason":"stop"}]}
    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
//        log.info("OpenAI返回数据：{}", data);
        if ("[DONE]".equals(data)) {
            log.info("OpenAI返回数据结束了");
            return;
        }
        try {
            Map map = JsonUtil.fromJson(data, Map.class);
            response.setMessageId((String) map.get("id"));
            List<Map> choices = (List<Map>) map.get("choices");
            Map choice = choices.get(0);
            Map delta = (Map) choice.get("delta");
            if (delta.containsKey("role")) {
                response.setResponseRole((String) delta.get("role"));
            } else if (delta.containsKey("content")) {
                String content = (String) delta.getOrDefault("content", "");
                response.getResponseMessage().append(content);
                response.getResponseList().add(content);
            }
            if (choice.get("finish_reason") != null) {
                log.info("finish_reason: " + choice.get("finish_reason"));
            }
        } catch (Exception e) {
            log.error("", e);
        }

    }

    @Override
    public void onClosed(EventSource eventSource) {
        log.info("OpenAI关闭sse连接...");
        response.stop();
        MDC.remove(MDC_TRACE_ID);
    }

    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        MDC.put(MDC_TRACE_ID, this.response.getMdcTrace());
        this.response.stop();
        this.response.setFinishReason("connection_err, please retry");
        if (Objects.isNull(response)) {
            this.response.setFinishReason(t.getMessage());
            if (t.getMessage().contains("Connection reset")){
                log.error("OpenAI  sse连接异常:{}", t.getMessage());
            }else {
                log.error("OpenAI  sse连接异常:", t);
            }
            eventSource.cancel();
            MDC.remove(MDC_TRACE_ID);
            return;
        }
        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            log.error("OpenAI  sse连接异常coda: {} data: {} 异常: {}", response.code(), body.string(), t);
        } else {
            log.error("OpenAI  sse连接异常code: {} data: {} 异常: {}", response.code(), response, t);
        }
        eventSource.cancel();
        MDC.remove(MDC_TRACE_ID);
    }
}
