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

import java.util.Arrays;

/**
 * @description: 订阅
 * @author: xyc0123456789
 * @create: 2023/3/25 1:27
 **/
@Slf4j
@Component
public class SubscriptAddApi extends AbstractCommonMessageApi{
    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionTaskImpl subscriptionTask;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getContent().startsWith("#subadd");
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent().trim();
        String[] split = content.split("\\s+");
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        SubscriptionInfo subscriptionInfo = validateSubInfo(split);
        if (subscriptionInfo!=null){
            subscriptionService.add(PlatformEnum.getByName(split[1]),split[2],messageEvent.getSubject().getId(), messageEvent.getSender().getId());
            subscriptionTask.add(PlatformEnum.getByName(split[1]),split[2],subscriptionInfo.getLiveStatus()==1);
            subscriptionTask.sendNotify(subscriptionInfo, messageEvent.getSubject().getId(), false);
        }else {
            QuoteReply quoteReply = messageEventContext.getQuoteReply();
            messageEvent.getSubject().sendMessage(new MessageChainBuilder().append(quoteReply).append("格式错误或者房间号错误").build());
        }
        return null;
    }

    public static SubscriptionInfo validateSubInfo(String[] info){
        if (info.length!=3){
            return null;
        }
        PlatformEnum platformEnum = PlatformEnum.getByName(info[1]);
        if (platformEnum ==null){
            return null;
        }
        return platformEnum.api.getLiveStatus(info[2]);
    }

    @Override
    public int sortedOrder() {
        return 95;
    }

    @Override
    public String commandName() {
        return "subscript.add";
    }

    public static void main(String[] args) {
        String content = "a   b   c";
        String[] split = content.split("\\s+");
        System.out.println(Arrays.toString(split));
    }
}
