package com.king.api.messageapis.common;

import com.king.db.pojo.MessageRecord;
import com.king.db.service.BotMessageRecordService;
import com.king.db.service.MessageRecordService;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.ListUtil;
import com.king.util.MyStringUtil;
import com.king.util.NewBotPoeXiaoMei;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @description: 小美
 * @author: xyc0123456789
 * @create: 2023/5/14 0:37
 **/
@Slf4j
@Component
public class XiaoHongBotRequest extends AbstractCommonMessageApi {

    @Autowired
    private BotMessageRecordService botMessageRecordService;

    @Autowired
    private MessageRecordService messageRecordService;

    @Autowired
    private CacheManager cacheManager;

    private Cache messageCache;

    private static final String MessageKeyCache = "xiaoMeiCache";

    @PostConstruct
    private void init() {
        messageCache = cacheManager.getCache(MessageKeyCache);
    }

    private String constructKey(MessageRecord record) {
        Long groupId = record.getGroupId();
        Integer singleId = record.getSingleId();
        Long msgId;
        if (singleId == null) {
            msgId = record.getSubId();
            log.error("singleId==null,subId={}", msgId);
        } else {
            msgId = Long.valueOf(singleId);
        }
        return groupId + "-" + msgId;

    }

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return false;
//        return messageEventContext.getContent().contains("小美") && !messageEventContext.getContent().startsWith("#");
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        try {Thread.sleep(1000);} catch (InterruptedException ignore) {}
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        String query;
        List<MessageRecord> offsetMessageList = messageRecordService.getOffsetMessageList(messageEvent.getSubject().getId(), null, -600);
        log.info("offsetMessageList: {}", offsetMessageList.size());

        if (offsetMessageList.size() > 10) {
            List<MessageRecord> sublist = offsetMessageList.subList(offsetMessageList.size() - 10, offsetMessageList.size()); // get first 5 elements
            offsetMessageList = new ArrayList<>(sublist);
        }

        MessageRecord messageRecord = offsetMessageList.get(offsetMessageList.size() - 1);
        String key = constructKey(messageRecord);
        Cache.ValueWrapper valueWrapper = messageCache.get(key);
        if (valueWrapper == null || valueWrapper.get() == null){
            messageCache.put(key, key);
        }else {
            log.info("key={} message:{} 已处理", key, messageRecord.getTextContent());
            return null;
        }

        query = NewBotPoeXiaoMei.constructRequest(offsetMessageList);
        if (MyStringUtil.isEmpty(query)) {
            log.error("query is empty");
            return null;
        }

        String ask = NewBotPoeXiaoMei.constructOneLineMessage(messageEventContext);

        String newQuery = "<前情提要>\n" + query + "</前情提要>\n<toResponse>\n" + ask + "\n</toResponse>";

        String answer = NewBotPoeXiaoMei.askXiaoMei(newQuery, String.valueOf(messageEvent.getSender().getId()));
        if ("Response timed out.".equalsIgnoreCase(answer.trim()) || "SendMessageMutation failed too many times.".equalsIgnoreCase(answer.trim())) {
            NewBotPoeXiaoMei.askXiaoMei("#reset", String.valueOf(messageEvent.getSender().getId()));
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignore) {
            }
            answer = NewBotPoeXiaoMei.askXiaoMei(newQuery, String.valueOf(messageEvent.getSender().getId()));
        }
        if (!MyStringUtil.isEmpty(answer)) {
            botMessageRecordService.save(messageEventContext, this.commandName(), newQuery, answer);
            if (answer.contains("[2469104787(小美)]")) {
                int i = answer.indexOf(":");
                if (i != -1) {
                    answer = answer.substring(i + 1);
                } else {
                    answer = answer.substring(16);
                }
            }else if (answer.startsWith("小美：")){
                answer = answer.substring(3);
            }

            Set<Long> ids = ListUtil.extractNumbers(answer);
            answer = answer.replaceAll("@(\\d+)\\s*", "");
            MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
            messageChainBuilder.append(messageEventContext.getQuoteReply()).append(answer.trim());
            for (Long id:ids){
                messageChainBuilder.append(new At(id));
            }
            messageEvent.getSubject().sendMessage(messageChainBuilder.build());
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 100;
    }

    @Override
    public String commandName() {
        return "bot.request.xiaomei";
    }
}
