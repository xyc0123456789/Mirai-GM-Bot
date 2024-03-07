package com.king.api.messageapis.common;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.RollUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Roll extends AbstractCommonMessageApi {


    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        return content.startsWith("roll")||content.startsWith("#roll");
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        try {
            MessageEvent messageEvent = messageEventContext.getMessageEvent();
            QuoteReply quoteReply = messageEventContext.getQuoteReply();
            String random = RollUtil.roll(messageEventContext.getContent());
            log.info("#roll "+quoteReply.getSource().getFromId() +":"+ random);
            MessageChain messages = new MessageChainBuilder().append(quoteReply).append(random).build();
            messageEvent.getSubject().sendMessage(messages);
        }catch (Exception e){
            log.error("roll",e);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 20;
    }

    @Override
    public String commandName() {
        return "message.roll";
    }
}
