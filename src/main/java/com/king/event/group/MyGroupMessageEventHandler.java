package com.king.event.group;

import com.king.api.messageapis.GroupMessageApiGroup;
import com.king.event.AbstractGroupMessageHandler;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.MessageEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyGroupMessageEventHandler extends AbstractGroupMessageHandler {

    private final GroupMessageApiGroup groupMessageApiGroup;

    public MyGroupMessageEventHandler(GroupMessageApiGroup groupMessageApiGroup) {
        this.groupMessageApiGroup = groupMessageApiGroup;
    }

    @EventHandler
    public void onMessage(Event event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理
        handlerCallback(event);
    }

    //文本处理
    public void handler(Event event){
        MessageEvent messageEvent = (MessageEvent) event;
        MessageEventContext messageEventContext = constructGroupContext(messageEvent);
//        log.info(String.valueOf(count));
        groupMessageApiGroup.handler(messageEventContext);
    }
}


