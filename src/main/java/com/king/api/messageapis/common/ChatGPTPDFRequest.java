package com.king.api.messageapis.common;

import com.king.config.ApplicationConfig;
import com.king.config.CommonConfig;
import com.king.model.CommonResponse;
import com.king.model.MessageCacheValueDTO;
import com.king.model.MessageEventContext;
import com.king.util.FileUtil;
import com.king.util.MyStringUtil;
import com.king.util.PDFUtil;
import com.king.util.openai.component.OpenAIRequestContext;
import com.king.util.openai.component.impl.OpenAiApiClient;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.file.AbsoluteFile;
import net.mamoe.mirai.contact.file.AbsoluteFolder;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.LinkedList;

import static com.king.config.CommonConfig.redisFlag;

/**
 * @description: 直接与pdf交流
 * @author: xyc0123456789
 * @create: 2023/6/26 17:09
 **/
@Slf4j
@Component
public class ChatGPTPDFRequest extends AbstractCommonMessageApi {

    @Autowired
    private OpenAiApiClient openAiApiClient;

    @Autowired
    private CacheManager cacheManager;

    private Cache messageCache;

    @PostConstruct
    private void init() {
        messageCache = cacheManager.getCache(ApplicationConfig.PDF_CONTENT_CACHE);
    }


    public static boolean validateString(String input) {
        if (MyStringUtil.isEmpty(input)) {
            return false;
        }
        String pattern = ".+\\.pdf\\s+.+";
        boolean match = input.matches(pattern);
        if (match) {
            int i = input.indexOf(".pdf");
            String substring = input.substring(0, i + 4);
            if (substring.equals(input.trim())) {
                return false;
            }
        }
        return match;
    }

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return validateString(messageEventContext.getContent().trim());
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent().trim();
        int index = content.indexOf(".pdf");
        String pdfName = content.substring(0, index + 4).trim();
        String query = content.substring(index + 4).trim();

        MessageEvent messageEvent = messageEventContext.getMessageEvent();

        String parentDir = CommonConfig.getWorkingDir() + "download/pdf/chat/";
        File pdfFile;
        String pdfContent;

        //检查cache
        Cache.ValueWrapper valueWrapper=null;
        try {
            valueWrapper = messageCache.get(pdfName);
        }catch (RedisConnectionFailureException e){
            log.error("RedisConnectionFailureException err: "+e.getMessage());
            redisFlag.getAndSet(false);
        }

        if (valueWrapper != null && valueWrapper.get() != null) {
            log.info("read {} from redis cache", pdfName);
            pdfContent = (String) valueWrapper.get();
        } else {
            //需要下载或者读取
            if (pdfName.startsWith("http")) {
                pdfFile = FileUtil.downLoadImage(pdfName, "download/pdf/chat/" + FileUtil.getFileNameFromUrl(pdfName));
            } else {
                //1、检查文件是否存在
                pdfFile = new File(parentDir + pdfName);
                if (!pdfFile.exists()) {
                    //2、如果不存在检查当前群聊文件是否存在
                    if (messageEventContext.isGroup()) {
                        Group group = (Group) messageEvent.getSubject();
                        AbsoluteFolder root = group.getFiles().getRoot();
                        AbsoluteFile result = root.filesStream()
                                .filter(file -> file.getName().equals(pdfName))
                                .findFirst()
                                .orElse(null);

                        //2.1、如果存在就下载
                        if (result != null) {
                            pdfFile = FileUtil.downLoadImage(result.getUrl(), "download/pdf/chat/" + pdfName);
                        } else {
                            pdfFile = null;
                        }
                    } else {
                        pdfFile = null;
                    }
                }
            }

            //2.2、如果不存在，返回失败信息
            if (pdfFile == null || !pdfFile.exists()) {
                messageEvent.getSubject().sendMessage(new MessageChainBuilder().append(messageEventContext.getQuoteReply()).append("error: " + pdfName + " 文件不存在/文件下载失败").build());
                return null;
            }
            //3、如果文件存在就请求，不存在返回失败信息

            try {
                pdfContent = PDFUtil.readPdf(pdfFile).replaceAll("\n", " ");
            } catch (RuntimeException e) {
                messageEvent.getSubject().sendMessage(new MessageChainBuilder().append(messageEventContext.getQuoteReply()).append("error: " + pdfName + " 文件读取失败").build());
                return null;
            }
            if (!MyStringUtil.isEmpty(pdfContent)) {
                messageCache.put(pdfName, pdfContent);
            }
        }
        OpenAIRequestContext openAIRequestContext = new OpenAIRequestContext();
        openAIRequestContext.setMdcTrace(MDC.get(CommonConfig.MDC_TRACE_ID));
        openAIRequestContext.setTruncateLimit(16000);
        openAIRequestContext.setTemperature(0);
        LinkedList<Message> requestMessageList = openAIRequestContext.getRequestMessageList();
        requestMessageList.add(Message.builder().role(Message.Role.SYSTEM).content("请先根据<content></content>中的内容回答我的问题。以下是内容:<content>" + pdfContent + "</content>").build());
        requestMessageList.add(Message.builder().role(Message.Role.USER).content(query).build());
        openAIRequestContext.setRequestMessage(query);
        openAiApiClient.request(openAIRequestContext);
        openAIRequestContext.waitToFinish();
        String answer;
        if (openAIRequestContext.getFinishReason() == null) {
            answer = openAIRequestContext.getResponseMessage().toString();
        } else {
            answer = openAIRequestContext.getFinishReason();
        }
        messageEvent.getSubject().sendMessage(new MessageChainBuilder().append(messageEventContext.getQuoteReply()).append(answer).build());
        return null;
    }

    @Override
    public int sortedOrder() {
        return 51;
    }

    @Override
    public String commandName() {
        return "chatgpt.pdf.request";
    }

    public static void main(String[] args) {
        String str1 = "example.pdf some text";
        String str2 = "example.pdf   ";
        String str3 = "example.txt";

        System.out.println(validateString(str1)); // true
        System.out.println(validateString(str2)); // false
        System.out.println(validateString(str3)); // false
    }
}
