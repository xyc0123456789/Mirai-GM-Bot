package com.king.util.xunfei.spark;

import com.king.db.pojo.ChatRequestRecord;
import com.king.db.pojo.ChatUserInfo;
import com.king.db.pojo.SparkKey;
import com.king.db.service.ChatRequestRecordService;
import com.king.db.service.ChatUserInfoService;
import com.king.db.service.SparkKeyService;
import com.king.util.MyStringUtil;
import com.king.util.openai.component.OpenAIRequestContext;
import com.unfbx.chatgpt.entity.chat.Message;
import io.github.briqt.spark4j.SparkClient;
import io.github.briqt.spark4j.constant.SparkApiVersion;
import io.github.briqt.spark4j.listener.SparkSyncChatListener;
import io.github.briqt.spark4j.model.SparkMessage;
import io.github.briqt.spark4j.model.SparkSyncChatResponse;
import io.github.briqt.spark4j.model.request.SparkRequest;
import io.github.briqt.spark4j.model.response.SparkTextUsage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: spark
 * @author: xyc0123456789
 * @create: 2023/10/9 14:02
 **/
@Slf4j
@Component
public class XunFeiSparkClient {

    public static boolean initSuccess = false;

    private static final SparkClient sparkClient=new SparkClient();

    @Autowired
    private SparkKeyService sparkKeyService;

    @Autowired
    private ChatRequestRecordService chatRequestRecordService;

    @Autowired
    private ChatUserInfoService chatUserInfoService;

    public XunFeiSparkClient() {
        init();
    }

    @PostConstruct
    public void init(){
        SparkKey key = null;
        if (sparkKeyService != null) {
            key = sparkKeyService.getKey();
        }else {
            log.warn("XunFei spark client init failed, sparkKeyService is null");
        }
        init(key);
    }

    public void init(SparkKey key) {
        if (key == null){
            log.info("XunFei spark client init failed, key is null");
            return;
        }
        sparkClient.appid= key.getAppid();
        sparkClient.apiKey=key.getApikey();
        sparkClient.apiSecret=key.getApisecret();
        log.info("XunFei spark client init success");
        initSuccess = true;
    }


    public static List<SparkMessage> build(String userId, String requestNo, String query, String prompt, List<ChatRequestRecord> messages){
        List<SparkMessage> messageList = new ArrayList<>();
        for (ChatRequestRecord msg:messages){
            String role = msg.getRole();
            if (Message.Role.ASSISTANT.getName().equalsIgnoreCase(role)){
                messageList.add(SparkMessage.assistantContent(msg.getContent()));
            } else if (Message.Role.USER.getName().equalsIgnoreCase(role)) {
                messageList.add(SparkMessage.userContent(msg.getContent()));
            } else if (Message.Role.SYSTEM.getName().equalsIgnoreCase(role)) {
                if (!MyStringUtil.isEmpty(msg.getContent())) {
                    messageList.add(SparkMessage.userContent(msg.getContent()));
                }
            }
        }
        messageList.add(SparkMessage.userContent(query));
        return messageList;
    }

    public String commonRequest(String userId, String query){
        ChatUserInfo chatUserInfo = chatUserInfoService.getOrAddOne(userId);
        String lastRequestNo = chatUserInfo.getLastRequestNo();
        String prompt = chatUserInfo.getPrompt();

        List<ChatRequestRecord> chatRequestRecordList = chatRequestRecordService.getByRequestNo(lastRequestNo);
        if (chatRequestRecordList == null){
            chatRequestRecordList = new ArrayList<>();
            ChatRequestRecord chatRequestRecord = chatRequestRecordService.addPrompt(lastRequestNo, userId, prompt);
            chatRequestRecordList.add(chatRequestRecord);
        }
        List<SparkMessage> messages = build(userId, lastRequestNo, query, prompt, chatRequestRecordList);
        SparkRequest sparkRequest=SparkRequest.builder()
                .messages(messages)
                .maxTokens(2048)
                .temperature(0.5)// 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高 非必传,取值为[0,1],默认为0.5
                .apiVersion(SparkApiVersion.V1_5)// 指定请求版本，默认使用最新2.0版本
                .build();
        SparkSyncChatResponse sparkSyncChatResponse = chatSync(sparkRequest);
        chatRequestRecordService.addAns(lastRequestNo, lastRequestNo, userId, query, sparkSyncChatResponse.getContent());
        return sparkSyncChatResponse.getContent();
    }

    public SparkSyncChatResponse chatSync(SparkRequest sparkRequest) {
        SparkSyncChatResponse chatResponse = new SparkSyncChatResponse();
        SparkSyncChatListenerImpl syncChatListener = new SparkSyncChatListenerImpl(chatResponse);
        sparkClient.chatStream(sparkRequest, syncChatListener);
        while (!chatResponse.isOk()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
        }
        return chatResponse;
    }


    public static void main(String[] args) {

        SparkKey key = new SparkKey();
        key.setAppid("4505ab7a");
        key.setApikey("");
        key.setApisecret("");
        XunFeiSparkClient xunFeiSparkClient = new XunFeiSparkClient();
        xunFeiSparkClient.init(key);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 20, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20), new ThreadPoolExecutor.AbortPolicy());
        for (int i=0;i<5;i++) {
            executor.execute(()->{
                try {
                    // 消息列表，可以在此列表添加历史对话记录
                    List<SparkMessage> messages = new ArrayList<>();
                    messages.add(SparkMessage.userContent("你好"));
//        messages.add(SparkMessage.assistantContent("好的，这位同学，有什么问题需要李老师为你解答吗？"));
//        messages.add(SparkMessage.userContent("鲁迅和周树人小时候打过架吗？"));
//        messages.add(SparkMessage.assistantContent("鲁迅和周树人小时候打过架吗？"));


                    SparkRequest sparkRequest = SparkRequest.builder()
                            .messages(messages)
                            .maxTokens(2048)
                            .temperature(0.5)
                            .apiVersion(SparkApiVersion.V1_5)
                            .build();


                    SparkSyncChatResponse chatResponse = xunFeiSparkClient.chatSync(sparkRequest);
                    SparkTextUsage textUsage = chatResponse.getTextUsage();

                    System.out.println("\n回答：" + chatResponse.getContent());
//                    System.out.println("\n提问tokens：" + textUsage.getPromptTokens()
//                            + "，回答tokens：" + textUsage.getCompletionTokens()
//                            + "，总消耗tokens：" + textUsage.getTotalTokens());
                }catch (Exception e){
                    log.info("", e);
                }
            });
        }
    }

}
