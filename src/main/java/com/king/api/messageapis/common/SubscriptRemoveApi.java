package com.king.api.messageapis.common;

import com.king.db.service.SubscriptionService;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.model.PlatformEnum;
import com.king.model.SubscriptionInfo;
import com.king.task.SubscriptionTaskImpl;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 解除订阅
 * @author: xyc0123456789
 * @create: 2023/3/25 1:27
 **/
@Slf4j
@Component
public class SubscriptRemoveApi extends AbstractCommonMessageApi{

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionTaskImpl subscriptionTask;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getContent().startsWith("#subrm");
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent().trim();
        String[] split = content.split("\\s+");
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        boolean update = subscriptionService.update(PlatformEnum.getByName(split[1]), split[2], messageEvent.getSubject().getId(), messageEvent.getSender().getId(), false);
        subscriptionTask.reload();
        QuoteReply quoteReply = messageEventContext.getQuoteReply();
        messageEvent.getSubject().sendMessage(new MessageChainBuilder().append(quoteReply).append(update?"取消订阅成功":"取消订阅失败").build());
        return null;
    }

    @Override
    public int sortedOrder() {
        return 94;
    }

    @Override
    public String commandName() {
        return "subscript.remove";
    }
}
