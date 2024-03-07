package com.king.api.memberapis;

import com.king.db.pojo.Members;
import com.king.db.service.MembersService;
import com.king.model.CommonResponse;
import com.king.model.MemberEventContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.MemberCardChangeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class MemberCardChange extends AbstractMemberEventApi{

    @Autowired
    private MembersService membersService;

    @Override
    public boolean condition(MemberEventContext memberEventContext) {
        return memberEventContext.getGroupMemberEvent() instanceof MemberCardChangeEvent;
    }

    @Override
    public CommonResponse handler(MemberEventContext memberEventContext) {
        MemberCardChangeEvent memberCardChangeEvent = (MemberCardChangeEvent) memberEventContext.getGroupMemberEvent();
        String newName = memberCardChangeEvent.getNew();
        NormalMember member = memberCardChangeEvent.getMember();
        Members members = new Members();
        members.setNameCard(newName);
        membersService.updateMember(member,members);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 0;
    }

    @Override
    public String commandName() {
        return "member.api.card.change";
    }
}
