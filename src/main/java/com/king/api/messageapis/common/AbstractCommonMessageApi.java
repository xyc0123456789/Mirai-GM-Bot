package com.king.api.messageapis.common;

import com.king.api.messageapis.AbstractMessageApi;
import com.king.api.messageapis.FriendMessageApi;
import com.king.api.messageapis.GroupMessageApi;

public abstract class AbstractCommonMessageApi extends AbstractMessageApi implements GroupMessageApi, FriendMessageApi {

    @Override
    public boolean commandType() {
        return true;
    }
}
