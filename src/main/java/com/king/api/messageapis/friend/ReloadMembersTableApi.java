package com.king.api.messageapis.friend;

import com.king.db.service.CommandPermissionService;
import com.king.event.friend.MyFriendEventMessageHandler;
import com.king.event.group.MyGroupMemberEventHandler;
import com.king.event.group.MyGroupMessageEventHandler;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.openai.component.impl.OpenAiApiClient;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReloadMembersTableApi extends AbstractFriendMessageApi {


    private final MyFriendEventMessageHandler myFriendEventMessageHandler;

    private final MyGroupMemberEventHandler myGroupMemberEventHandler;

    private final MyGroupMessageEventHandler myGroupMessageEventHandler;

    private final CommandPermissionService commandPermissionService;

    private final OpenAiApiClient openAiApiClient;


    @Lazy
    public ReloadMembersTableApi(MyFriendEventMessageHandler myFriendEventMessageHandler, MyGroupMemberEventHandler myGroupMemberEventHandler, MyGroupMessageEventHandler myGroupMessageEventHandler, CommandPermissionService commandPermissionService, OpenAiApiClient openAiApiClient) {
        this.myFriendEventMessageHandler = myFriendEventMessageHandler;
        this.myGroupMemberEventHandler = myGroupMemberEventHandler;
        this.myGroupMessageEventHandler = myGroupMessageEventHandler;
        this.commandPermissionService = commandPermissionService;
        this.openAiApiClient = openAiApiClient;
    }

    private void reloadAll(){
        myFriendEventMessageHandler.reload();
        myGroupMessageEventHandler.reload();
        myGroupMemberEventHandler.reload();
        commandPermissionService.reloadPermission();
        openAiApiClient.init();
    }

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return "#reload".equalsIgnoreCase(messageEventContext.getContent())&&isMe(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        reloadAll();
        messageEvent.getSender().sendMessage("ok");
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "friend.member.reload";
    }
}
