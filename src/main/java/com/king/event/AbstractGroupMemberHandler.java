package com.king.event;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.events.GroupMemberEvent;
import org.slf4j.MDC;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

@Slf4j
public abstract class AbstractGroupMemberHandler extends AbstractGroupHandler {


    @Override
    public void handlerCallback(Event event) throws Exception {
        GroupMemberEvent groupMemberEvent = (GroupMemberEvent) event;
        Group group = groupMemberEvent.getGroup();
        MDC.put(MDC_TRACE_ID, String.format("%s(%s)",group.getName(),group.getId()));
        log.info(groupMemberEvent.toString());
        if (filterId(event)){
            handler(event);
        }
        MDC.remove(MDC_TRACE_ID);
    }

    @Override
    public boolean filterId(Event event) {
        GroupMemberEvent groupMemberEvent = (GroupMemberEvent) event;
        return filterHashMap.containsKey(groupMemberEvent.getGroup().getId());
    }
}
