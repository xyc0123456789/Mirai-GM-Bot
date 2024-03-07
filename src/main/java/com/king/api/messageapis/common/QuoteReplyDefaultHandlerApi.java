package com.king.api.messageapis.common;

import com.king.component.MyBot;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 引用
 * @author: xyc0123456789
 * @create: 2023/5/17 15:03
 **/
@Slf4j
@Component
public class QuoteReplyDefaultHandlerApi extends AbstractCommonMessageApi{

    @Autowired
    private ChatGPTRequest chatGPTRequest;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        QuoteReply quoteReply = messageEventContext.getMessageEvent().getMessage().get(QuoteReply.Key);
        if (quoteReply!=null){
            MessageSource source = quoteReply.getSource();
            log.info("ori fromId:{} source:{}", source.getFromId(), source);
            return source.getFromId() == MyBot.bot.getId();
        }
        return false;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        try {Thread.sleep(1000);} catch (InterruptedException ignore) {}
        chatGPTRequest.subhandler(messageEventContext, 0);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 101;
    }

    @Override
    public String commandName() {
        return "message.quote";
    }
}
