package com.king.api.memberapis;

import com.king.db.pojo.ContactListenList;
import com.king.db.pojo.Members;
import com.king.db.service.ContactListenListService;
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
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.king.model.ExtDataJsonKeys.COUNT;

@Component
@Slf4j
public class MemberJoin extends AbstractMemberEventApi {

    @Autowired
    private MembersService membersService;

    @Autowired
    private ContactListenListService contactListenListService;

    @Override
    public boolean condition(MemberEventContext memberEventContext) {
        return memberEventContext.getGroupMemberEvent() instanceof MemberJoinEvent;
    }

    @Override
    public CommonResponse handler(MemberEventContext memberEventContext) {
        MemberJoinEvent memberJoinEvent = (MemberJoinEvent) memberEventContext.getGroupMemberEvent();
        NormalMember member = memberJoinEvent.getMember();
        Group group = memberJoinEvent.getGroup();

        String message;
        if (memberJoinEvent instanceof MemberJoinEvent.Invite){
            message = member.getNick() + "(" + member.getId() + ")被邀请加入了群聊";
        }else if (memberJoinEvent instanceof MemberJoinEvent.Active){
            message = member.getNick() + "(" + member.getId() + ")主动加入了群聊";
        }else {
            throw new RuntimeException("unknow memberJoinEvent"+ JsonUtil.toJson(memberJoinEvent));
        }
        Members members = membersService.addMember(group, member);
        String extData = members.getExtData();
        if (!MyStringUtil.isEmpty(extData)){
            Map map = JsonUtil.fromJson(extData, Map.class);
            int count = Integer.parseInt((String) map.get(COUNT));
            if (count>1){
                String first = DateFormateUtil.formatYYYYMMDDTHHMMSS(members.getCtime());
                String last = DateFormateUtil.formatYYYYMMDDTHHMMSS(members.getUtime());
                message = message + ",第一次入群时间:"+first+", 上次离群时间:"+last+"，入群次数:"+count;
                group.sendMessage(message);
            }
        }
        log.info(message);

        //查询之前的头衔
        String specialTitle=null;
        Members oriMember = membersService.selectMember(GroupId.GROUP985, member.getId());
        if (oriMember!=null){
            specialTitle = oriMember.getSpecialTitle();
        }else {
            oriMember = membersService.selectMember(GroupId.Group985_2, member.getId());
            if (oriMember!=null) {
                specialTitle = oriMember.getSpecialTitle();
            }
        }
        ContactListenList groupByPrimaryKey = contactListenListService.getGroupByPrimaryKey(group.getId());
        String permission = groupByPrimaryKey.getPermission();
        if (!MyStringUtil.isEmpty(permission)){
            if (!MyStringUtil.isEmpty(specialTitle)){
                int index = permission.lastIndexOf("\n");
                String newPermission;
                if (index!=-1){
                    newPermission = permission.substring(0,index)+"\n原头衔: "+specialTitle+permission.substring(index);
                }else {
                    newPermission = permission+"\n原头衔: "+specialTitle;
                }
                permission = newPermission;
            }
            log.info(permission);
            At at = new At(member.getId());
            MessageChain messageChain = new MessageChainBuilder().append(at).append(MiraiCode.deserializeMiraiCode(permission)).build();
            group.sendMessage(messageChain);
        }else if (!MyStringUtil.isEmpty(specialTitle)){
            group.sendMessage(NormalMemberUtil.getNormalMemberStr(member) + " 头衔:" + specialTitle);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 0;
    }

    @Override
    public String commandName() {
        return "member.api.join";
    }
}
