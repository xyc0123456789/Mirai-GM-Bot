package com.king.event;

import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.slf4j.MDC;

import java.util.Arrays;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

@Slf4j
public abstract class AbstractGroupMessageHandler extends AbstractGroupHandler {

    @Override
    public void handlerCallback(Event event) throws Exception {
        MessageEvent messageEvent = (MessageEvent) event;
        Group group = (Group) messageEvent.getSubject();
        MDC.put(MDC_TRACE_ID, String.format("%s(%s)[%s][%s]",group.getName(),group.getId(),messageEvent.getSender().getId(), Arrays.toString(messageEvent.getSource().getIds())));
//        log.info("{}({})[{}]:{}",messageEvent.getSender().getNick(),messageEvent.getSender().getId(),messageEvent.getSenderName(),messageEvent.getMessage().serializeToMiraiCode());
        log.info("[{}]:{}", NormalMemberUtil.getNormalMemberStr(group.get(messageEvent.getSender().getId())),messageEvent.getMessage().serializeToMiraiCode());
        if (filterId(event)){
            handler(messageEvent);
        }
        MDC.remove(MDC_TRACE_ID);
    }

    @Override
    public boolean filterId(Event event) {
        GroupMessageEvent messageEvent = (GroupMessageEvent) event;
        return filterHashMap.containsKey(messageEvent.getSubject().getId());
    }
}
