package com.king.api.messageapis.common;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.At;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Slf4j
@Component
public class AtDefaultHandlerApi extends AbstractCommonMessageApi {

    @Autowired
    private ChatGPTRequest chatGPTRequest;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        if (CollectionUtils.isEmpty(messageEventContext.getAtList())){
            return false;
        }
        for (At at: messageEventContext.getAtList()){
            return at.getTarget()==messageEventContext.getMessageEvent().getBot().getId()&& !MyStringUtil.isEmpty(messageEventContext.getContent());
        }
        return false;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        try {Thread.sleep(500);} catch (InterruptedException ignore) {}
        chatGPTRequest.subhandler(messageEventContext, 0);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 20;
    }

    @Override
    public String commandName() {
        return "message.at";
    }
}
