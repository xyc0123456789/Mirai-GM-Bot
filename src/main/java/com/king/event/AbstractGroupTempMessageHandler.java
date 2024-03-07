package com.king.event;

import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import org.slf4j.MDC;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/8 21:52
 **/
@Slf4j
public abstract class AbstractGroupTempMessageHandler extends AbstractGroupHandler{

    @Override
    public void handlerCallback(Event event) throws Exception {
        GroupTempMessageEvent messageEvent = (GroupTempMessageEvent) event;
        NormalMember normalMember = messageEvent.getSubject();
        Group group = normalMember.getGroup();
        MDC.put(MDC_TRACE_ID, String.format("<%s>%s(%s)[%s]", "temp",group.getName(),group.getId(),messageEvent.getSender().getId()));
//        log.info("{}({})[{}]:{}",messageEvent.getSender().getNick(),messageEvent.getSender().getId(),messageEvent.getSenderName(),messageEvent.getMessage().serializeToMiraiCode());
        log.info("[{}]:{}", NormalMemberUtil.getNormalMemberStr(normalMember),messageEvent.getMessage().serializeToMiraiCode());
        if (filterId(event)){
            handler(messageEvent);
        }
        MDC.remove(MDC_TRACE_ID);
    }

    @Override
    public boolean filterId(Event event) {
        GroupTempMessageEvent messageEvent = (GroupTempMessageEvent) event;
        return filterHashMap.containsKey(messageEvent.getGroup().getId());
    }

}
