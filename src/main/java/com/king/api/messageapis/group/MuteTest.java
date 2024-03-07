package com.king.api.messageapis.group;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.MessageEvent;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class MuteTest extends AbstractGroupMessageApi {

    private static final Set<String> muteWord = new HashSet<String>(){{
        add("test");
    }};
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        return MyStringUtil.containSensitiveStr(content,muteWord);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        try {
            MessageEvent messageEvent = messageEventContext.getMessageEvent();
            Member sender = (Member) messageEvent.getSender();
            log.error("==={} 被禁言10min===", sender.getId());
            sender.mute(10*60);
        }catch (Exception e){
            log.error("", e);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 98;
    }

    @Override
    public String commandName() {
        return "member.mute";
    }
}
