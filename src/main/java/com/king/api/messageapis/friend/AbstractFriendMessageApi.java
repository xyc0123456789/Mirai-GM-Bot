package com.king.api.messageapis.friend;

import com.king.api.messageapis.AbstractMessageApi;
import com.king.api.messageapis.FriendMessageApi;
import com.king.db.service.MembersService;
import com.king.model.MessageEventContext;
import com.king.model.QQFriendId;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractFriendMessageApi extends AbstractMessageApi implements FriendMessageApi {

    protected boolean isMe(MessageEventContext messageEventContext){
        return messageEventContext.getMessageEvent().getSender().getId()== QQFriendId.ME;
    }

    @Autowired
    protected MembersService membersService;
}
