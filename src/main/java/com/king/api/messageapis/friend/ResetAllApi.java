package com.king.api.messageapis.friend;

import com.king.db.pojo.ChatUserInfo;
import com.king.db.service.ChatUserInfoService;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 重置所有人的上下文
 * @author: xyc0123456789
 * @create: 2023/7/26 12:54
 **/
@Slf4j
@Component
public class ResetAllApi extends AbstractFriendMessageApi{

    @Autowired
    protected ChatUserInfoService chatUserInfoService;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        if("#resetall".equalsIgnoreCase(messageEventContext.getContent().trim())){
            return isMe(messageEventContext);
        }
        return false;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        String answer="OK";

        try {
            chatUserInfoService.updateWithResetAll();
        }catch (Exception e){
            answer = "请求异常:" + e.getMessage();
            log.error("", e);
        }
        QuoteReply quoteReply = messageEventContext.getQuoteReply();
        MessageChain messages = new MessageChainBuilder().append(quoteReply).append(answer).build();
        messageEvent.getSubject().sendMessage(messages);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 50;
    }

    @Override
    public String commandName() {
        return "chatgpt.reset.all";
    }
}
