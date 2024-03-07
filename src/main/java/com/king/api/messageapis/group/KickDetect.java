package com.king.api.messageapis.group;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.model.QQFriendId;
import com.king.task.KickTaskImpl;
import com.king.util.DateFormateUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.NormalMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KickDetect extends AbstractGroupMessageApi {

    @Autowired
    private KickTaskImpl kickTask;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        if (!"#kickdetect".equalsIgnoreCase(content)){
            return false;
        }
        return isManagerOrMe(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        try {
            kickTask.kick(messageEventContext.getMessageEvent().getSubject().getId(), DateFormateUtil.getOffsetDate000000(1));
        }catch (Exception e){
            log.error("kickdetect",e);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 96;
    }

    @Override
    public String commandName() {
        return "member.kick.detect";
    }
}
