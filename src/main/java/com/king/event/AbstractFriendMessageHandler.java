package com.king.event;

import com.king.util.DFAUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

@Slf4j
public abstract class AbstractFriendMessageHandler extends AbstractFriendHandler {

    @Override
    protected void handlerCallback(Event event) throws Exception {
        MessageEvent messageEvent = (MessageEvent) event;
        MessageChain message = messageEvent.getMessage();
        MDC.put(MDC_TRACE_ID, String.format("%s(%s)",messageEvent.getSender().getNick(),messageEvent.getSender().getId()));
        log.info(message.serializeToMiraiCode());
        if (filterId(event)){
            handler(event);
        }
        MDC.remove(MDC_TRACE_ID);
    }

    @Override
    protected boolean filterId(Event event) {
        long id = ((MessageEvent) event).getSubject().getId();
        return filterHashMap.containsKey(id);
    }
}
