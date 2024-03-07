package com.king.event.group;

import com.king.api.recallapis.GroupMessageRecallApiGroup;
import com.king.config.CommonConfig;
import com.king.event.AbstractGroupMessageRecallEventHandler;
import com.king.model.MessageRecallEventContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.MessageRecallEvent;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/8 22:10
 **/
@Slf4j
@Component
public class MyGroupMessageRecallEventHandler extends AbstractGroupMessageRecallEventHandler {

    @Autowired
    private GroupMessageRecallApiGroup groupMessageRecallApiGroup;

    @EventHandler
    public void onMessage(Event event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理
        handlerCallback(event);
    }

    //文本处理
    public void handler(Event event){
        MessageRecallEvent.GroupRecall groupRecallEvent = (MessageRecallEvent.GroupRecall) event;
        MessageRecallEventContext messageRecallEventContext = new MessageRecallEventContext();
        messageRecallEventContext.setTraceName(MDC.get(CommonConfig.MDC_TRACE_ID));
        messageRecallEventContext.setMessageRecallEvent(groupRecallEvent);
        log.info("{}", messageRecallEventContext);
        groupMessageRecallApiGroup.handler(messageRecallEventContext);
    }
}
