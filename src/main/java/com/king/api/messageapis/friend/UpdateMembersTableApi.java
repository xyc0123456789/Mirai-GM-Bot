package com.king.api.messageapis.friend;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.model.Response;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UpdateMembersTableApi extends AbstractFriendMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return "#update".equalsIgnoreCase(messageEventContext.getContent())&&isMe(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        Response response = membersService.updateGroups();
        log.info("membersService.updateGroups:{}", response);
        messageEvent.getSender().sendMessage(response.getCode().name()+"-"+response.getMessage());
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "friend.member.update";
    }
}
