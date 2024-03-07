package com.king.api.messageapis.group;

import com.king.api.messageapis.AbstractMessageApi;
import com.king.api.messageapis.GroupMessageApi;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractGroupMessageApi extends AbstractMessageApi implements GroupMessageApi {
    @Override
    public boolean commandType() {
        return false;
    }
}
