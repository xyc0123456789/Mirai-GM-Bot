package com.king.api.messageapis.tempmessageapis;

import com.king.api.messageapis.common.ChatGPTRolePlayRequest;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/9 15:08
 **/
@Slf4j
@Component
public class TempChatGPTRolePlayRequest extends AbstractTempGroupMessageApi {

    @Autowired
    private ChatGPTRolePlayRequest chatGPTRolePlayRequest;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
//        return messageEventContext.getContent().startsWith("#preset");
        return false;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        chatGPTRolePlayRequest.handler(messageEventContext);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 0;
    }

    @Override
    public String commandName() {
        return "temp.group.chatgpt.rolyplay";
    }
}
