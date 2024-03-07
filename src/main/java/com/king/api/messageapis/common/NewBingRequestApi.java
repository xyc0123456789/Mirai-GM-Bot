package com.king.api.messageapis.common;

import com.king.component.MyBot;
import com.king.model.*;
import com.king.util.*;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import static com.king.api.messageapis.common.ChatGPTRequest.convertSensitiveMessage;

@Slf4j
@Component
public class NewBingRequestApi extends AbstractCommonMessageApi {

    public final EdgeBingUtil edgeBingUtil = new EdgeBingUtil();
    @Autowired
    private DFAUtil dfaUtil;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getContent().length() > 5 && messageEventContext.getContent().startsWith("#bing") && !"#bingreset".equalsIgnoreCase(messageEventContext.getContent());
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        String input = messageEventContext.getContent().substring(5).trim();
        if (MyStringUtil.isEmpty(input)){
            return null;
        }
        Contact subject = messageEvent.getSubject();
        User sender = messageEvent.getSender();

        NewBingRequest newBingRequest = new NewBingRequest();
        newBingRequest.setMessage(input);
        NewBingResponse response = EdgeBingUtil.askBing(input, String.valueOf(sender.getId()));
        ForwardMessageBuilder forwardMessageBuilder = new ForwardMessageBuilder(subject);
        forwardMessageBuilder.add(messageEvent);
        forwardMessageBuilder.add(MyBot.MyBotQQId,"response", new MessageChainBuilder().append(convertSensitiveMessage(response.getResponse(), dfaUtil)).build());

        if (!CollectionUtils.isEmpty(response.getReferences())) {
            for (String reference: response.getReferences()) {
                forwardMessageBuilder.add(MyBot.MyBotQQId, "reference", new MessageChainBuilder().append(convertSensitiveMessage(reference.trim(), dfaUtil)).build());
            }
        }
        if (!CollectionUtils.isEmpty(response.getSuggestedResponses())) {
            for (String tmp : response.getSuggestedResponses()) {
                forwardMessageBuilder.add(MyBot.MyBotQQId, "suggestion", new MessageChainBuilder().append(convertSensitiveMessage(tmp, dfaUtil)).build());
            }
        }

        MessageChain messages = new MessageChainBuilder().append(forwardMessageBuilder.build()).build();
        subject.sendMessage(messages);
        return null;
    }



    @Override
    public int sortedOrder() {
        return 50;
    }

    @Override
    public String commandName() {
        return "bing.request";
    }

    @Override
    public boolean defaultStatus() {
        return false;
    }
}
