package com.king.event.group;

import com.king.api.messageapis.TempGroupMessageApiGroup;
import com.king.event.AbstractGroupTempMessageHandler;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/8 21:45
 **/
@Slf4j
@Component
public class MyGroupTempMessageEventHandler extends AbstractGroupTempMessageHandler {

    @Autowired
    private TempGroupMessageApiGroup tempGroupMessageApiGroup;

    @EventHandler
    public void onMessage(Event event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理
        handlerCallback(event);
    }

    //文本处理
    public void handler(Event event){
        GroupTempMessageEvent groupTempMessageEvent = (GroupTempMessageEvent)event;
        MessageEventContext messageEventContext = constructGroupContext(groupTempMessageEvent);
//        log.info("{}", messageEventContext);
        tempGroupMessageApiGroup.handler(messageEventContext);
    }
}
