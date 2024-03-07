package com.king.api.messageapis.tempmessageapis;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.WordCloudUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/9 13:38
 **/
@Slf4j
@Component
public class TempWordcloudApi extends AbstractTempGroupMessageApi{
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return "#wordcloud".equalsIgnoreCase(messageEventContext.getContent());
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        GroupTempMessageEvent groupTempMessageEvent = (GroupTempMessageEvent) messageEvent;
        NormalMember normalMember = groupTempMessageEvent.getSubject();
        Date date = new Date();
        WordCloudUtil.sendWordCloud(normalMember.getGroup().getId(), normalMember.getId(), date);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "temp.group.word.cloud";
    }
}
