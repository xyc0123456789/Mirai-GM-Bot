package com.king.event;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.MessageRecallEvent;
import org.slf4j.MDC;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/8 22:06
 **/
@Slf4j
public abstract class AbstractGroupMessageRecallEventHandler extends AbstractGroupHandler{
    @Override
    protected void handlerCallback(Event event) throws Exception {
        MessageRecallEvent.GroupRecall groupRecallEvent = (MessageRecallEvent.GroupRecall) event;
        Group group = groupRecallEvent.getGroup();
        MDC.put(MDC_TRACE_ID, String.format("%s(%s)",group.getName(),group.getId()));
//        log.info("{}({})[{}]:{}",messageEvent.getSender().getNick(),messageEvent.getSender().getId(),messageEvent.getSenderName(),messageEvent.getMessage().serializeToMiraiCode());
        if (filterId(event)){
            handler(groupRecallEvent);
        }
        MDC.remove(MDC_TRACE_ID);
    }

    @Override
    public boolean filterId(Event event) {
        MessageRecallEvent.GroupRecall messageEvent = (MessageRecallEvent.GroupRecall) event;
        return filterHashMap.containsKey(messageEvent.getGroup().getId());
    }


}
