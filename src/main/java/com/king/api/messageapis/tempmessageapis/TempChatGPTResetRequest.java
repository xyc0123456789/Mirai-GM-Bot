package com.king.api.messageapis.tempmessageapis;

import com.king.api.messageapis.common.ChatGPTResetRequest;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/9 14:53
 **/
@Slf4j
@Component
public class TempChatGPTResetRequest extends AbstractTempGroupMessageApi{

    @Autowired
    private ChatGPTResetRequest chatGPTResetRequest;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getContent().startsWith("#reset");
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        chatGPTResetRequest.handler(messageEventContext);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "temp.group.chatgpt.reset";
    }
}
