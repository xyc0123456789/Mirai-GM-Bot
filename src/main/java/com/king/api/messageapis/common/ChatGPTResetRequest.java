package com.king.api.messageapis.common;

import com.king.config.CommonConfig;
import com.king.db.pojo.ChatUserInfo;
import com.king.db.service.ChatUserInfoService;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.PythonServerRequest;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static com.king.config.CommonConfig.BotNums;
import static com.king.config.CommonConfig.botManager;

@Slf4j
@Component
public class ChatGPTResetRequest extends AbstractCommonMessageApi {

    @Autowired
    protected ChatUserInfoService chatUserInfoService;
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return "#reset".equalsIgnoreCase(messageEventContext.getContent().trim());
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        String answer="请求异常:";

        try {
            ChatUserInfo chatUserInfo = new ChatUserInfo();
            chatUserInfo.setPrompt(messageEventContext.getContent().trim().substring(6));
            chatUserInfo.setUserId(String.valueOf(messageEvent.getSender().getId()));
            chatUserInfoService.updateWithReset(chatUserInfo);
            answer = "reset success. prompt: " + chatUserInfo.getPrompt().substring(0, Math.min(100, chatUserInfo.getPrompt().length()));
            if (chatUserInfo.getPrompt().length()>100){
                answer += "...";
            }
        }catch (Exception e){
            answer += e.getMessage();
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
        return "chatgpt.reset";
    }
}
