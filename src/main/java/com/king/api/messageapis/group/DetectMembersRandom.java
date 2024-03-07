package com.king.api.messageapis.group;


import com.king.db.service.MembersService;
import com.king.model.CommonResponse;
import com.king.model.GroupId;
import com.king.model.MessageEventContext;
import com.king.util.DateFormateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DetectMembersRandom extends AbstractGroupMessageApi {

    @Autowired
    private MembersService membersService;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        if(!"#detectr".equalsIgnoreCase(messageEventContext.getContent())){
            return false;
        }
        return isManagerOrMe(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        if (messageEventContext.getMessageEvent().getSubject().getId()== GroupId.DEV){
            membersService.detectTest(DateFormateUtil.getOffsetDate000000(1));
            membersService.detectTest(null);
        }else {
            membersService.detect(messageEventContext.getMessageEvent().getSubject().getId(), DateFormateUtil.getOffsetDate000000(1), 20);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 95;
    }

    @Override
    public String commandName() {
        return "members.detect.random";
    }
}
