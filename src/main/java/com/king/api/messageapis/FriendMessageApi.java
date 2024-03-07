package com.king.api.messageapis;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;

public interface FriendMessageApi {
    boolean condition(MessageEventContext messageEventContext);

    CommonResponse handler(MessageEventContext messageEventContext);

    int sortedOrder();

    String commandName();

    boolean commandType();//好友是否可用

    boolean defaultStatus();//默认是否开启
}
