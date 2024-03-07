package com.king.util.openai.component;

import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @description: openai公共响应
 * @author: xyc0123456789
 * @create: 2023/5/28 23:25
 **/
@Data
@Slf4j
public class OpenAIRequestContext {

    private String mdcTrace="";

    private String model = ChatCompletion.Model.GPT_3_5_TURBO_16K.getName();

    private double temperature = 0.7;

    private int maxTokens = 16000;

    private int truncateLimit = 14000;

    private String prompt = "You are ChatGPT, a large language model trained by OpenAI. Respond conversationally";

    private LinkedList<Message> requestMessageList = new LinkedList<>();

    private String requestMessage;

    private String requestNo;

    private String userId;



    private String messageId;

    private StringBuilder responseMessage=new StringBuilder();
    private List<String> responseList = new ArrayList<>();
    private Message.Role responseRole;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private String finishReason=null;

    private OpenAiStreamClient client;

    private ResourceManager<OpenAiStreamClient> resourceManager;

    private static Map<String, Message.Role> roleMap = new HashMap<>();

    static {
        for (Message.Role r: Message.Role.values()){
            roleMap.put(r.getName(), r);
        }
    }

    public static Message.Role getFromName(String name){
        return roleMap.get(name);
    }

    public void setResponseRole(String roleName) {
        this.responseRole = OpenAIRequestContext.getFromName(roleName);
    }

    public void waitToFinish(){
        try {
            boolean await = countDownLatch.await(10, TimeUnit.MINUTES);
            if (!await){
                this.finishReason = "wait_time_out";
            }
        }catch (InterruptedException e){
            log.error("", e);
            this.finishReason = "interrupted";
        }
    }

    public void stop(){
        countDownLatch.countDown();
        if (resourceManager!=null&&client!=null){
            resourceManager.release(client);
        }
    }

    @Override
    public String toString() {
        return "OpenAICommonResponse{" +
                "responseMessage=" + responseMessage +
                ", role=" + responseRole +
                ", countDownLatch=" + countDownLatch.getCount() +
                '}';
    }


    public ChatCompletion buildChatCompletion(){
        ChatCompletion chatCompletion;
        while(true){
            chatCompletion = ChatCompletion
                    .builder()
                    .model(getModel())
                    .temperature(getTemperature())
                    .maxTokens(getMaxTokens())
                    .messages(getRequestMessageList())
                    .stream(true)
                    .build();
            long tokens = chatCompletion.tokens();
            if (tokens > truncateLimit && requestMessageList.size()>1){
                requestMessageList.remove(1);
            }else {
                chatCompletion.setMaxTokens((int) (getMaxTokens() - tokens));
                break;
            }
        }
        return chatCompletion;
    }

}
