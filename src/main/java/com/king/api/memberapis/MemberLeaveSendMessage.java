package com.king.api.memberapis;

import com.king.db.pojo.Members;
import com.king.db.service.MembersService;
import com.king.model.CommonResponse;
import com.king.model.MemberEventContext;
import com.king.util.DateFormateUtil;
import com.king.util.JsonUtil;
import com.king.util.MyStringUtil;
import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.king.model.ExtDataJsonKeys.COUNT;

@Component
@Slf4j
public class MemberLeaveSendMessage extends AbstractMemberEventApi{


    @Override
    public boolean condition(MemberEventContext memberEventContext) {
        return false;
    }

    @Override
    public CommonResponse handler(MemberEventContext memberEventContext) {
        return null;
    }

    @Override
    public int sortedOrder() {
        return 90;
    }

    @Override
    public String commandName() {
        return "member.api.leave.send.message";
    }

    @Override
    public boolean defaultStatus() {
        return false;
    }
}
