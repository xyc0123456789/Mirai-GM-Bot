package com.king.api.messageapis.group;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.WordCloudUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class WordCloudSelfApi extends AbstractGroupMessageApi{

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return "#selfwordcloud".equalsIgnoreCase(messageEventContext.getContent());
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        WordCloudUtil.sendWordCloud(messageEvent.getSubject().getId(), messageEvent.getSender().getId(), new Date());
        return null;
    }






    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "message.word.cloud.self";
    }

}
