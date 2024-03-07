package com.king.api.memberapis;

import com.king.component.MyBot;
import com.king.db.pojo.Members;
import com.king.db.service.MembersService;
import com.king.model.CommonResponse;
import com.king.model.GroupId;
import com.king.model.MemberEventContext;
import com.king.util.DateFormateUtil;
import com.king.util.JsonUtil;
import com.king.util.MyStringUtil;
import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.king.db.service.CommandPermissionService.GroupMemberPermissionMap;
import static com.king.db.service.CommandPermissionService.getKeyFromMessageDefault;
import static com.king.model.ExtDataJsonKeys.COUNT;

@Component
@Slf4j
public class MemberLeave extends AbstractMemberEventApi{

    @Autowired
    private MembersService membersService;

    @Autowired
    private MemberLeaveSendMessage memberLeaveSendMessage;

    @Override
    public boolean condition(MemberEventContext memberEventContext) {
        return memberEventContext.getGroupMemberEvent() instanceof MemberLeaveEvent;
    }

    @Override
    public CommonResponse handler(MemberEventContext memberEventContext) {
        MemberLeaveEvent memberLeaveEvent = (MemberLeaveEvent) memberEventContext.getGroupMemberEvent();
        Member member = memberLeaveEvent.getMember();
        String message;
        if (memberLeaveEvent instanceof MemberLeaveEvent.Kick){
            MemberLeaveEvent.Kick kick = (MemberLeaveEvent.Kick) memberLeaveEvent;
            NormalMember operator = kick.getOperator();
            message = NormalMemberUtil.getNormalMemberStr(member) +"被迫离开了群聊,操作者："+NormalMemberUtil.getNormalMemberStr(operator);
        }else if (memberLeaveEvent instanceof MemberLeaveEvent.Quit){
            message = NormalMemberUtil.getNormalMemberStr(member) +"主动离开了群聊";
        }else {
            throw new RuntimeException("unknow memberLeaveEvent"+ JsonUtil.toJson(memberLeaveEvent));
        }
        Members members = membersService.memberLeave(memberLeaveEvent.getGroupId(), member.getId());
        String extData = members.getExtData();
        if (!MyStringUtil.isEmpty(extData)){
            Map map = JsonUtil.fromJson(extData, Map.class);
            int count = Integer.parseInt((String) map.get(COUNT));
            if (count>1){
                String join = DateFormateUtil.formatYYYYMMDDTHHMMSS(members.getJoinDate());
                String last = DateFormateUtil.formatYYYYMMDDTHHMMSS(members.getUtime());
                message = message + ",本次入群时间:"+join+", 上次离群时间:"+last+"，入群次数:"+count;
            }
        }else {
            members.setExtData(JsonUtil.json(COUNT,"1"));
            membersService.updateByPrimaryKeySelective(members);
        }
        log.info(message);
        Group devGroup = memberLeaveEvent.getBot().getGroup(GroupId.DEV);
        if (devGroup==null) {
            devGroup = memberLeaveEvent.getBot().getGroup(GroupId.TEST);
        }
        if (devGroup!=null){
            devGroup.sendMessage(memberLeaveEvent.getGroup().getName() +"(" + memberLeaveEvent.getGroupId() + ")-" + message);
        }

        String keyFromMessageDefault = getKeyFromMessageDefault(memberLeaveSendMessage.commandName(), memberEventContext);
        boolean permission = GroupMemberPermissionMap.getOrDefault(keyFromMessageDefault,false);
        if (permission){
            memberLeaveEvent.getGroup().sendMessage(message);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 0;
    }

    @Override
    public String commandName() {
        return "member.api.leave";
    }

}
