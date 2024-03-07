package com.king.event.group;

import com.king.event.AbstractGroupMessageHandler;
import com.king.model.MessageEventContext;
import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.MessageEvent;
import org.slf4j.MDC;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

@Slf4j
public class Bot2MessageEventHandler extends AbstractGroupMessageHandler {

    @EventHandler
    public void onMessage(Event event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理
        handlerCallback(event);
    }

    @Override
    public void handlerCallback(Event event) throws Exception {
        MessageEvent messageEvent = (MessageEvent) event;
        Group group = (Group) messageEvent.getSubject();
//        MDC.put(MDC_TRACE_ID, String.format("%s(%s)[%s]",group.getName(),group.getId(),messageEvent.getSender().getId()));
//        log.info("{}({})[{}]:{}",messageEvent.getSender().getNick(),messageEvent.getSender().getId(),messageEvent.getSenderName(),messageEvent.getMessage().serializeToMiraiCode());
//        log.info("[{}]:{}", NormalMemberUtil.getNormalMemberStr(group.get(messageEvent.getSender().getId())),messageEvent.getMessage().serializeToMiraiCode());
        if (filterId(event)){
            handler(messageEvent);
        }
        MDC.remove(MDC_TRACE_ID);
    }

    @Override
    public void handler(Event event) throws Exception {
//        MessageEvent messageEvent = (MessageEvent) event;
//        MessageEventContext messageEventContext = constructGroupContext(messageEvent);



    }
}
