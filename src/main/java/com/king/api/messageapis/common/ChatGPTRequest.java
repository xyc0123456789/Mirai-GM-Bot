package com.king.api.messageapis.common;

import com.king.config.CommonConfig;
import com.king.db.service.BotMessageRecordService;
import com.king.model.*;
import com.king.util.CommonMarkUtil;
import com.king.util.DFAUtil;
import com.king.util.LanguageDetectUtil;
import com.king.util.MyStringUtil;
import com.king.util.xunfei.spark.XunFeiSparkClient;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

@Slf4j
@Component
public class ChatGPTRequest extends AbstractCommonMessageApi {

    @Autowired
    protected DFAUtil dfaUtil;

    @Autowired
    protected BotMessageRecordService botMessageRecordService;

    @Autowired
    private XunFeiSparkClient xunFeiSparkClient;


    @Autowired
    private CacheManager cacheManager;

    private Cache messageCache;

    private static final String MessageKeyCache = "ChatGptCache";

    @PostConstruct
    private void init() {
        messageCache = cacheManager.getCache(MessageKeyCache);
    }

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getContent().length() > 4 && messageEventContext.getContent().substring(0, 4).equalsIgnoreCase("#GPT");
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        return subhandler(messageEventContext, 4);
    }

    public CommonResponse subhandler(MessageEventContext messageEventContext, int startIndex) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        QuoteReply quoteReply = messageEventContext.getQuoteReply();

        String key = messageEvent.getSubject().getId() + "-" + messageEvent.getSource().getIds()[0];
        Cache.ValueWrapper valueWrapper = messageCache.get(key);
        if (valueWrapper == null || valueWrapper.get() == null){
            messageCache.put(key, key);
        }else {
            log.info("key={} message:{} 已处理", key, messageEventContext.getContent());
            return null;
        }

        String content = messageEventContext.getContent().trim();
        String answer = "";

        if (messageEventContext.getMessageEvent().getSubject().getId() == GroupId.Group985_2) {
            Set<String> sensitiveWordByDFAMap = dfaUtil.getSensitiveWordByDFAMap(content, 1);
            if (!CollectionUtils.isEmpty(sensitiveWordByDFAMap)) {
                answer = "请求存在敏感词，不予回复";
            }
        }
        ChatGPTResponse response = getPreProcess(content, startIndex);
        if (MyStringUtil.isEmpty(content)){
            return null;
        }

        String userId = String.valueOf(messageEvent.getSender().getId());

        if (MyStringUtil.isEmpty(answer)) {
            try {
                answer = xunFeiSparkClient.commonRequest(userId, response.getRequestContent());
                log.info("response: {}", answer);
            }catch (Exception e){
                log.error("", e);
                answer = e.getMessage();
            }
        }
        if (messageEventContext.isGroup()) {
            answer = convertSensitiveMessage(answer, dfaUtil);
        }
        if (MyStringUtil.isEmpty(answer)){
            answer = "system error";
        }

        if ("ru".equals(LanguageDetectUtil.getLanguage(answer))){
            MessageChain messages = new MessageChainBuilder().append(quoteReply).append(answer).build();
            messageEvent.getSubject().sendMessage(messages);
            return null;
        }
        if (!response.isMath()&&(!messageEventContext.isGroup() || messageEvent.getSubject().getId() == GroupId.AIQUN || messageEvent instanceof GroupTempMessageEvent)){
            MessageChain messages = new MessageChainBuilder().append(quoteReply).append(answer).build();
            messageEvent.getSubject().sendMessage(messages);
            return null;
        }

        return getCommonResponse(messageEvent, quoteReply, answer, response.isMath());
    }


    public static String convertSensitiveMessage(String str,DFAUtil dfaUtil){
        Set<String> answerSensitiveWordByDFAMap = dfaUtil.getSensitiveWordByDFAMap(str, 2);
        if (!CollectionUtils.isEmpty(answerSensitiveWordByDFAMap)) {
            ArrayList<String> arrayList = new ArrayList<>(answerSensitiveWordByDFAMap);
            arrayList.sort(Comparator.comparing(String::length));
            arrayList.sort(Comparator.reverseOrder());
            for (String toReplace: arrayList){
                str = str.replaceAll(toReplace, MyStringUtil.sensitiveMask(toReplace));
            }
        }
        return str;
    }

    public static ChatGPTResponse getPreProcess(String content, int length){
        int botNum = -1;

//        try {
//            String mayNum = content.substring(length, length+1);
//            botNum = Integer.parseInt(mayNum);
//        }catch (Exception ignored){}

        String answer = "请求异常:",req;
        boolean math= false;
        ChatGPTResponse response = new ChatGPTResponse(Response.ok());

        try {
//            if (botNum==-1){
                if (content.substring(length).startsWith("math")){
                    req = content.substring(length+4).trim();
                    math = true;
                }else {
                    req = content.substring(length).trim();
                }
                botNum = CommonConfig.botManager.getNextBot();
//            }
//            else {
//                botNum = botNum%BotNums;
//                if (content.substring(length+1).startsWith("math")){
//                    req = content.substring(length+5).trim();
//                    math = true;
//                }else {
//                    req = content.substring(length+1).trim();
//                }
//            }
            response.setRequestContent(req);
        } catch (Exception e) {
            log.error("", e);
            response.setResponse(Response.fail(answer));
        }
        response.setBotIndex(botNum);
        response.setMath(math);
        return response;
    }

    public static CommonResponse getCommonResponse(MessageEvent messageEvent, QuoteReply quoteReply, String answer) {
        return getCommonResponse(messageEvent, quoteReply, answer, false);
    }

    public static CommonResponse getCommonResponse(MessageEvent messageEvent, QuoteReply quoteReply, String answer, boolean math) {
        File tmpImg = CommonMarkUtil.createMarkdownImg(answer, math);
        MessageChain messages;
        if (tmpImg == null) {
            messages = new MessageChainBuilder().append(quoteReply).append("图片生成发生错误").build();
        } else {
            Image image = Contact.uploadImage(messageEvent.getSender(), tmpImg);
            messages = new MessageChainBuilder().append(quoteReply).append(image).build();
        }
        messageEvent.getSubject().sendMessage(messages);
        if (tmpImg != null) {
            boolean delete = tmpImg.delete();
            log.info("{} delete {}", tmpImg.getAbsolutePath(), delete);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 50;
    }

    @Override
    public String commandName() {
        return "chatgpt.request";
    }
}
