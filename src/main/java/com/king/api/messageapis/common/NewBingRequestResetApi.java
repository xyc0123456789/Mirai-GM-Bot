package com.king.api.messageapis.common;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.model.NewBingRequest;
import com.king.model.NewBingResponse;
import com.king.util.NewBingChatClientUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NewBingRequestResetApi extends AbstractCommonMessageApi {

    @Autowired
    private NewBingRequestApi newBingRequestApi;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
//        return messageEventContext.getContent().length() > 5 && "#bingreset".equalsIgnoreCase(messageEventContext.getContent());
        return false;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        QuoteReply quoteReply = messageEventContext.getQuoteReply();


        NewBingRequest newBingRequest = new NewBingRequest();
        newBingRequest.setMessage(NewBingChatClientUtil.RESET);
//        NewBingResponse response = newBingRequestApi.newBingChatClientUtil.reqBySeque(newBingRequest);
        String stringBuilder = "OK";
        Contact subject = messageEvent.getSubject();
        MessageChain messages = new MessageChainBuilder().append(quoteReply).append(stringBuilder).build();
        subject.sendMessage(messages);
        return null;
    }



    @Override
    public int sortedOrder() {
        return 50;
    }

    @Override
    public String commandName() {
        return "bing.request.reset";
    }

    @Override
    public boolean defaultStatus() {
        return false;
    }
}
