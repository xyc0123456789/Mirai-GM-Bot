package com.king.api.messageapis.group.audio;

import com.king.api.messageapis.group.AbstractGroupMessageApi;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SendBackMp3 extends AbstractGroupMessageApi {

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return false;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "audio.send.back";
    }
}
