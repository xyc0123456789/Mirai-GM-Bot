package com.king.event.group;

import com.king.api.memberapis.MemberApiGroup;
import com.king.config.CommonConfig;
import com.king.event.AbstractGroupMemberHandler;
import com.king.model.MemberEventContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.GroupMemberEvent;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyGroupMemberEventHandler extends AbstractGroupMemberHandler {

    @Autowired
    private MemberApiGroup memberApiGroup;

    @EventHandler
    public void onMessage(Event event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理
        handlerCallback(event);
    }

    // 成员退出进入
    public void handler(Event event){
        MemberEventContext memberEventContext = new MemberEventContext();
        memberEventContext.setTraceName(MDC.get(CommonConfig.MDC_TRACE_ID));
        memberEventContext.setGroupMemberEvent((GroupMemberEvent) event);
        memberApiGroup.handler(memberEventContext);
    }
}
