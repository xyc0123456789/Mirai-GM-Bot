package com.king.api.messageapis.group;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.DFAUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class Recall extends AbstractGroupMessageApi {

    @Autowired
    private DFAUtil dfaUtil;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return true;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        try {
            String content = messageEventContext.getContent();
            MessageEvent messageEvent = messageEventContext.getMessageEvent();
            Set<String> sensitiveWordByDFAMap = dfaUtil.getSensitiveWordByDFAMap(content, 1);
            if (sensitiveWordByDFAMap.size()>0){
                log.error("{{}}被撤回了,触发词汇{}", content, sensitiveWordByDFAMap);
                DFAUtil.notifyFriend(messageEvent,messageEvent.getMessage().serializeToMiraiCode(),sensitiveWordByDFAMap);
                MessageSource.recall(messageEventContext.getMessageSource());
            }
        }catch (Exception e){
            log.error("recall", e);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 98;
    }

    @Override
    public String commandName() {
        return "message.recall";
    }
}
