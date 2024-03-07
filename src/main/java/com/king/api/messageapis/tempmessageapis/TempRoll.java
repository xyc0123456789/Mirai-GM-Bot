package com.king.api.messageapis.tempmessageapis;

import com.king.api.messageapis.common.Roll;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/9 13:40
 **/
@Slf4j
@Component
public class TempRoll extends AbstractTempGroupMessageApi{

    @Autowired
    private Roll roll;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        return content.startsWith("roll")||content.startsWith("#roll");
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        roll.handler(messageEventContext);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 98;
    }

    @Override
    public String commandName() {
        return "temp.group.roll";
    }
}
