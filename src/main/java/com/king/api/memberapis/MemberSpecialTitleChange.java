package com.king.api.memberapis;

import com.king.db.pojo.Members;
import com.king.db.service.MembersService;
import com.king.model.CommonResponse;
import com.king.model.MemberEventContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.MemberSpecialTitleChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class MemberSpecialTitleChange extends AbstractMemberEventApi{

    @Autowired
    private MembersService membersService;

    @Override
    public boolean condition(MemberEventContext memberEventContext) {
        return memberEventContext.getGroupMemberEvent() instanceof MemberSpecialTitleChangeEvent;
    }

    @Override
    public CommonResponse handler(MemberEventContext memberEventContext) {
        MemberSpecialTitleChangeEvent memberSpecialTitleChangeEvent = (MemberSpecialTitleChangeEvent) memberEventContext.getGroupMemberEvent();
        String newSpecialTitle = memberSpecialTitleChangeEvent.getNew();
        NormalMember member = memberSpecialTitleChangeEvent.getMember();
        Members members = new Members();
        members.setSpecialTitle(newSpecialTitle);
        membersService.updateMember(member,members);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 0;
    }

    @Override
    public String commandName() {
        return "member.api.special.title.change";
    }
}
