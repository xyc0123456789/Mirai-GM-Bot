package com.king.event.friend;

import com.king.api.messageapis.FriendMessageApiGroup;
import com.king.api.messageapis.common.GenerateText;
import com.king.db.service.MembersService;
import com.king.event.AbstractFriendMessageHandler;
import com.king.model.*;
import com.king.util.*;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

@Slf4j
@Component
public class MyFriendEventMessageHandler extends AbstractFriendMessageHandler {

    private final FriendMessageApiGroup friendMessageApiGroup;

    public MyFriendEventMessageHandler(FriendMessageApiGroup friendMessageApiGroup) {
        this.friendMessageApiGroup = friendMessageApiGroup;
    }

    @EventHandler
    protected void onMessage(MessageEvent event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理
        handlerCallback(event);
    }

    @Override
    protected void handler(Event event) throws Exception {
        MessageEvent messageEvent = (MessageEvent) event;
        MessageEventContext messageEventContext = constructFriendContext(messageEvent);
        friendMessageApiGroup.handler(messageEventContext);
    }

}
