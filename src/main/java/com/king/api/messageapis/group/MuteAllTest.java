package com.king.api.messageapis.group;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.task.MuteAllImpl;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MuteAllTest extends AbstractGroupMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        if (!"#mutestart".equalsIgnoreCase(messageEventContext.getContent()) && !"#mutestop".equalsIgnoreCase(messageEventContext.getContent())) {
            return false;
        }
        return isManagerOrMe(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        Group group = (Group) messageEventContext.getMessageEvent().getSubject();
        String content = messageEventContext.getContent();
        boolean muteAll;
        if (content.contains("start")) {
            muteAll = true;
        } else {
            muteAll = false;
        }
        MuteAllImpl.muteAll(group.getId(), muteAll);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 98;
    }

    @Override
    public String commandName() {
        return "mute.all.test";
    }
}
