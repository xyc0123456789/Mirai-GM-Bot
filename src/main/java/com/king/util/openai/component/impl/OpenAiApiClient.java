package com.king.util.openai.component.impl;

import com.king.db.pojo.ChatRequestRecord;
import com.king.db.pojo.ChatUserInfo;
import com.king.db.service.ChatRequestRecordService;
import com.king.db.service.ChatUserInfoService;
import com.king.db.service.OpenAIApiKeyService;
import com.king.util.FileUtil;
import com.king.util.MyStringUtil;
import com.king.util.openai.component.OpenAIRequestContext;
import com.king.util.openai.component.ResourceManager;
import com.king.util.openai.component.StreamEventCommonListener;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

/**
 * @description: 使用线程与相同数量的client去构建请求并发起请求
 * @author: xyc0123456789
 * @create: 2023/5/29 22:54
 **/
@Slf4j
@Component
public class OpenAiApiClient {

    @Autowired
    private ChatRequestRecordService chatRequestRecordService;

    @Autowired
    private ChatUserInfoService chatUserInfoService;

    @Autowired
    private OpenAIApiKeyService openAIApiKeyService;

    private static final int CLIENT_NUMS = 10;

    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CLIENT_NUMS, CLIENT_NUMS, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(CLIENT_NUMS * 100), new ThreadPoolExecutor.AbortPolicy());

    private ResourceManager<OpenAiStreamClient> resourceManager;


    public void init(List<String> apiKeys) {
        resourceManager = new ResourceManager<>(CLIENT_NUMS, new OpenAiStreamClientFactory(apiKeys));
    }

    @PostConstruct
    public void init(){
        List<String> keys = openAIApiKeyService.getKeys();
        log.info("api keys: {}, get(0):{}", keys.size(), CollectionUtils.isEmpty(keys)?null: keys.get(0).substring(0,6));
        init(keys);
    }

    public void request(OpenAIRequestContext context) {
        String mdc = MDC.get(MDC_TRACE_ID);
        try {
            THREAD_POOL_EXECUTOR.submit(() -> {
                OpenAiStreamClient openAiStreamClient = null;
                try {
                    MDC.put(MDC_TRACE_ID, mdc);

                    openAiStreamClient = resourceManager.acquire();
                    context.setClient(openAiStreamClient);
                    context.setResourceManager(resourceManager);
                    StreamEventCommonListener eventSourceListener = new StreamEventCommonListener(context);
                    ChatCompletion chatCompletion = context.buildChatCompletion();
                    openAiStreamClient.streamChatCompletion(chatCompletion, eventSourceListener);
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    MDC.remove(MDC_TRACE_ID);
                }
            });
        } catch (RejectedExecutionException rejectedExecutionException) {
            log.error("系统过载");
        } catch (Exception e) {
            log.error("chatGPT execute err", e);
        }

    }




    public static OpenAIRequestContext constructContext(String userId, String requestNo, String query, String prompt, List<ChatRequestRecord> messages){
        OpenAIRequestContext context = new OpenAIRequestContext();
        context.setMdcTrace(MDC.get(MDC_TRACE_ID));
        context.setUserId(userId);
        context.setRequestNo(requestNo);
        context.setRequestMessage(query);
        if (!MyStringUtil.isEmpty(prompt)){
            context.setPrompt(prompt);
        }
        LinkedList<Message> messageList = context.getRequestMessageList();
        for (ChatRequestRecord msg:messages){
            Message.Role role = OpenAIRequestContext.getFromName(msg.getRole());
            if (role==null){
                continue;
            }
            messageList.add(Message.builder().role(role).content(msg.getContent()).build());
        }
        messageList.add(Message.builder().role(Message.Role.USER).content(query).build());
        return context;
    }

    public OpenAIRequestContext commonRequest(String userId, String query){
        //1、查询并创建chatuserinfo表
        //2、查看requestNo和prompt
        //3、根据requestNo查询Message表,会得到 只有一条，一条都没有，有很多条
        //4、请求openai
        //5、保存请求和响应信息
        ChatUserInfo chatUserInfo = chatUserInfoService.getOrAddOne(userId);
        String lastRequestNo = chatUserInfo.getLastRequestNo();
        String prompt = chatUserInfo.getPrompt();

        List<ChatRequestRecord> chatRequestRecordList = chatRequestRecordService.getByRequestNo(lastRequestNo);
        if (chatRequestRecordList == null){
            chatRequestRecordList = new ArrayList<>();
            ChatRequestRecord chatRequestRecord = chatRequestRecordService.addPrompt(lastRequestNo, userId, prompt);
            chatRequestRecordList.add(chatRequestRecord);
        }
        OpenAIRequestContext openAIRequestContext = constructContext(userId, lastRequestNo, query, prompt, chatRequestRecordList);
        request(openAIRequestContext);
        openAIRequestContext.waitToFinish();
        chatRequestRecordService.addAns(openAIRequestContext);
        return openAIRequestContext;
    }

    public static void main(String[] args) {
        OpenAiApiClient client = new OpenAiApiClient();
        client.init(new ArrayList<>(Arrays.asList(FileUtil.readFile(new File("key.txt")).trim())));
        OpenAIRequestContext context = new OpenAIRequestContext();
        context.setMdcTrace("1");
        context.setModel(ChatCompletion.Model.GPT_3_5_TURBO_16K.getName());
        context.setMaxTokens(16000);
        LinkedList<Message> requestMessageList = context.getRequestMessageList();
        requestMessageList.add(Message.builder().role(Message.Role.SYSTEM).content(context.getPrompt()).build());
        requestMessageList.add(Message.builder().role(Message.Role.USER).content("hello").build());
        context.setRequestMessage("hello");
        client.request(context);
        context.waitToFinish();
        log.info(context.getRequestMessage());
        log.info(context.getResponseMessage().toString());
    }
}
