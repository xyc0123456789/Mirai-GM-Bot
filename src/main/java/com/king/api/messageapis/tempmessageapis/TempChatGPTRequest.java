package com.king.api.messageapis.tempmessageapis;

import com.king.api.messageapis.common.ChatGPTRequest;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/9 14:15
 **/
@Slf4j
@Component
public class TempChatGPTRequest extends AbstractTempGroupMessageApi {

    @Autowired
    private ChatGPTRequest chatGPTRequest;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getContent().length() > 5 && messageEventContext.getContent().substring(0, 4).equalsIgnoreCase("#GPT");
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        chatGPTRequest.handler(messageEventContext);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "temp.group.chatgpt.request";
    }
}
