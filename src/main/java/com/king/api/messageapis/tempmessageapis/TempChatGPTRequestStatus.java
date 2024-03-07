package com.king.api.messageapis.tempmessageapis;

import com.king.api.messageapis.common.ChatGPTRequestStatus;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/9 14:50
 **/
@Slf4j
@Component
public class TempChatGPTRequestStatus extends AbstractTempGroupMessageApi{

    @Autowired
    private ChatGPTRequestStatus chatGPTRequestStatus;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return false;
//        return "#botstatus".equalsIgnoreCase(messageEventContext.getContent());
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        chatGPTRequestStatus.handler(messageEventContext);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "temp.group.chatgpt.botstatus";
    }
}
