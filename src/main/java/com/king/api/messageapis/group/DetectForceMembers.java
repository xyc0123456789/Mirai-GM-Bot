package com.king.api.messageapis.group;


import com.king.db.service.MembersService;
import com.king.model.CommonResponse;
import com.king.model.GroupId;
import com.king.model.MessageEventContext;
import com.king.model.QQFriendId;
import com.king.util.DateFormateUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.NormalMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DetectForceMembers extends AbstractGroupMessageApi {

    @Autowired
    private MembersService membersService;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        if (!"#detectforce".equalsIgnoreCase(messageEventContext.getContent())){
            return false;
        }
        return isManagerOrMe(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        if (messageEventContext.getMessageEvent().getSubject().getId()== GroupId.DEV){
            membersService.detectTest(DateFormateUtil.getOffsetDate000000(1000));
            membersService.detectTest(null);
        }else {
            membersService.detect(messageEventContext.getMessageEvent().getSubject().getId(), DateFormateUtil.getOffsetDate000000(1000));
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 95;
    }

    @Override
    public String commandName() {
        return "member.detect.force";
    }
}
