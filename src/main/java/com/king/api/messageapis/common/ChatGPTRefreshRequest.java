package com.king.api.messageapis.common;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ChatGPTRefreshRequest extends AbstractCommonMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getContent().equalsIgnoreCase("#refresh");
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
//        MessageEvent messageEvent = messageEventContext.getMessageEvent();
//        String answer="请求异常:";
//        try {
//            answer = PythonServerRequest.chatGPT(messageEventContext.getContent());
//        }catch (Exception e){
//            log.error("", e);
//            answer+=e.getMessage();
//        }
//        QuoteReply quoteReply = messageEventContext.getQuoteReply();
//        MessageChain messages = new MessageChainBuilder().append(quoteReply).append(answer).build();
//        messageEvent.getSubject().sendMessage(messages);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 50;
    }

    @Override
    public String commandName() {
        return "chatgpt.refresh";
    }
}
