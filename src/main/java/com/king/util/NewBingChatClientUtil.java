package com.king.util;

import com.king.model.NewBingRequest;
import com.king.model.NewBingResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import static com.king.config.CommonConfig.MDC_TRACE_ID;
import static com.king.config.CommonConfig.botManager;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/11 21:55
 **/
@Slf4j
public class NewBingChatClientUtil {

    private String jailbreakConversationId = "";
    private String parentMessageId="";
    private String conversationSignature = "";

    public static final String RESET = "sajnfanbgiagjnas";

    private final ThreadPoolExecutor threadPoolExecutor = ThreadPoolExecutorUtil.getAQueueExecutor(10);

    public NewBingResponse reqBySeque(NewBingRequest newBingRequest){
        String mdc = MDC.get(MDC_TRACE_ID);

        try {
            Future<NewBingResponse> submit = threadPoolExecutor.submit(() -> {
                MDC.put(MDC_TRACE_ID, mdc);
                try {
                    if (RESET.equals(newBingRequest.getMessage())){
                        return this.reset();
                    }else{
                        return this.request(newBingRequest);
                    }
                } finally {
                    MDC.remove(MDC_TRACE_ID);
                }
            });
            return submit.get();
        } catch (RejectedExecutionException rejectedExecutionException) {
            NewBingResponse response = new NewBingResponse();
            response.setResponse("请求过快,请等待之前若干请求返回");
            return response;
        } catch (Exception e) {
            log.error("chatGPT execute err", e);
            NewBingResponse response = new NewBingResponse();
            response.setResponse(e.getMessage());
            return response;
        }
    }

    private NewBingResponse reset(){
        this.jailbreakConversationId="";
        this.parentMessageId = "";
        this.conversationSignature = "";
        NewBingResponse response = new NewBingResponse();
        response.setResponse("OK");
        return response;
    }

    private NewBingResponse request(NewBingRequest newBingRequest){
        String url = "http://127.0.0.1:5001/conversation";
        HttpClient httpClient = new HttpClient(url, 300000, 300000, false);

        Map<String, Object> req = new HashMap<>();
        req.put("message", newBingRequest.getMessage());
        if (!MyStringUtil.isEmpty(newBingRequest.getConversationSignature())){
            req.put("jailbreakConversationId", newBingRequest.getJailbreakConversationId());
            req.put("parentMessageId", newBingRequest.getParentMessageId());
            req.put("conversationSignature", newBingRequest.getConversationSignature());
        } else if (!MyStringUtil.isEmpty(this.jailbreakConversationId)) {
            req.put("jailbreakConversationId", this.jailbreakConversationId);
            req.put("parentMessageId", this.parentMessageId);
            req.put("conversationSignature", this.conversationSignature);
        } else {
            req.put("jailbreakConversationId", true);
        }
        NewBingResponse response = new NewBingResponse();
        int count = 10;
        while (count-->0){
            try {
                log.info("request {}:{}", 10-count,req);
                int status = httpClient.sendJson(req, null);
                String result = httpClient.getResult();
                dealResponse(result, response);
            } catch (Exception e) {
                log.error("new bing request err",e);
                response.setResponse(e.getMessage());
            }
            if (!response.getResponse().startsWith("/turing/conversation/create")){
                log.info("response:{}", response);
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {}
        }
        return response;
    }


    private NewBingResponse dealResponse(String result, NewBingResponse newBingResponse){
        if (newBingResponse==null){
            newBingResponse = new NewBingResponse();
        }
        if (MyStringUtil.isEmpty(result)){
            newBingResponse.setResponse("response is None");
            return newBingResponse;
        }
        Map<String, Object> map = JsonUtil.fromJson(result, Map.class);
        if (map.containsKey("error")){
            newBingResponse.setResponse((String) map.get("error"));
            return newBingResponse;
        }

        try {
            this.jailbreakConversationId = (String) map.get("jailbreakConversationId");
            this.conversationSignature = (String) map.get("conversationSignature");
            this.parentMessageId = (String) map.get("messageId");
            newBingResponse.setResponse((String) map.get("response"));
        }catch (Exception e){
            log.error("", e);
            newBingResponse.setResponse(e.getMessage());
        }
        try {
            Map<String,Object> details = (Map<String, Object>) map.get("details");
            if (details!=null){
                List<Map<String,Object>> suggestedResponses = (List<Map<String, Object>>) details.get("suggestedResponses");
                if (suggestedResponses!=null) {
                    List<String> responses = newBingResponse.getSuggestedResponses();
                    for (Map<String, Object> tmpMap : suggestedResponses) {
                        responses.add((String) tmpMap.getOrDefault("text", ""));
                    }
                }
            }
        }catch (Exception e){
            log.error("", e);
        }

        try {
            Map<String,Object> details = (Map<String, Object>) map.get("details");
            List<String> references = newBingResponse.getReferences();
            if (details!=null){
                List<Map<String,Object>> adaptiveCards = (List<Map<String, Object>>) details.get("adaptiveCards");
                if (adaptiveCards!=null) {
                    for (Map<String, Object> tmpMap : adaptiveCards) {
                        List<Map<String,Object>> body = (List<Map<String, Object>>) tmpMap.get("body");
                        if (body!=null){
                            for (Map<String, Object> subBody : body) {
                                references.add((String) subBody.get("text"));
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            log.error("", e);
        }

        return newBingResponse;
    }

    public static void main(String[] args) {
        NewBingChatClientUtil newBingChatClientUtil = new NewBingChatClientUtil();

        Scanner scanner = new Scanner(System.in);
        while (true){
            NewBingRequest newBingRequest = new NewBingRequest();
            log.info("请输入：");
            String nextLine = scanner.nextLine();
            newBingRequest.setMessage(nextLine);
            newBingChatClientUtil.reqBySeque(newBingRequest);
        }
    }

}
