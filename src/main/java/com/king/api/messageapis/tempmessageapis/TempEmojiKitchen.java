package com.king.api.messageapis.tempmessageapis;

import com.king.api.messageapis.common.EmojiKitchen;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.EmojiKitchenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: EmojiKitchen
 * @author: xyc0123456789
 * @create: 2023/3/28 0:33
 **/
@Slf4j
@Component
public class TempEmojiKitchen extends AbstractTempGroupMessageApi{
    @Autowired
    private EmojiKitchen emojiKitchen;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return EmojiKitchenUtil.find(messageEventContext.getContent());
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        emojiKitchen.handler(messageEventContext);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "temp.group.message.emoji.kitchen";
    }
}
