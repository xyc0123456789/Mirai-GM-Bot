package com.king.api.messageapis.common;

import com.king.component.MyBot;
import com.king.db.pojo.Subscription;
import com.king.db.service.SubscriptionService;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 查询
 * @author: xyc0123456789
 * @create: 2023/3/25 1:27
 **/
@Slf4j
@Component
public class SubscriptListApi extends AbstractCommonMessageApi{
    @Autowired
    private SubscriptionService subscriptionService;
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getContent().equalsIgnoreCase("#sublist");
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {

        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        Contact subject = messageEvent.getSubject();
        List<Subscription> all = subscriptionService.getByGroup(subject.getId());
        Map<Long, List<String>> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(all)) {
            for (Subscription subscription : all) {
                if (map.containsKey(subscription.getQqId())) {
                    List<String> strings = map.get(subscription.getQqId());
                    strings.add(subscription.getPlatform() + "-" + subscription.getRoomId());
                } else {
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add(subscription.getPlatform() + "-" + subscription.getRoomId());
                    map.put(subscription.getQqId(), tmp);
                }
            }
        }

        ForwardMessageBuilder forwardMessageBuilder = new ForwardMessageBuilder(subject);
        boolean isGroup = messageEventContext.isGroup();
        if (isGroup){
            for (Long id:map.keySet()){
                forwardMessageBuilder.add(id, NormalMemberUtil.getNormalMemberStr(subject.getId(),id),new MessageChainBuilder().append(map.get(id).toString()).build());
            }
        }else {
            forwardMessageBuilder.add(messageEvent.getSender(),new MessageChainBuilder().append(map.get(messageEvent.getSender().getId()).toString()).build());
        }
        subject.sendMessage(forwardMessageBuilder.build());
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "subscript.list";
    }
}
