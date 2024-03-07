package com.king.api.messageapis.common;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.EmojiKitchenUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @description: EmojiKitchen
 * @author: xyc0123456789
 * @create: 2023/3/20 22:06
 **/
@Slf4j
@Component
public class EmojiKitchen extends AbstractCommonMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return true;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        String msg = messageEventContext.getContent();
        File cook = EmojiKitchenUtil.cook(msg);
        if (cook!=null){
            Image image = Contact.uploadImage(messageEvent.getSender(), cook);
            MessageChain messages = new MessageChainBuilder().append(image).build();
            messageEvent.getSubject().sendMessage(messages);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "message.emoji.kitchen";
    }
}
