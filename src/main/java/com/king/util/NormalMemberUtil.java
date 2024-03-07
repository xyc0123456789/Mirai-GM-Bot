package com.king.util;

import com.king.db.pojo.Members;
import com.king.db.service.MembersService;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class NormalMemberUtil {

    private static MembersService membersService;

    public NormalMemberUtil(MembersService membersService) {
        NormalMemberUtil.membersService = membersService;
    }

    public static String getNormalMemberStr(MessageEvent messageEvent) {
        Members normalMember = membersService.selectMember(messageEvent.getSubject().getId(), messageEvent.getSender().getId());
        return getNormalMemberStr(normalMember);
    }

    public static String getNormalMemberStr(Long groupId, Long qqId) {
        Members normalMember = membersService.selectMember(groupId, qqId);
        return getNormalMemberStr(normalMember);
    }

    public static String getNormalMemberStr(Members members){
        if (members == null) {
            return "";
        }
        return members.getNameCard() + "[" + members.getNickName() + "](" + members.getQqId() + ")<" + members.getSpecialTitle() + ">";
    }

    public static String getNormalMemberStr(Member member) {
        if (member == null) {
            return "";
        }
        return member.getNameCard() + "[" + member.getNick() + "](" + member.getId() + ")<" + member.getSpecialTitle() + ">";
    }

    public static String getGroupStr(Group group) {
        return "[" + group.getName() + "(" + group.getId() + ")]";
    }

    public static String getStrFromMessageEvent(MessageEvent messageEvent){
        Contact contact = messageEvent.getSubject();
        if (contact instanceof Group){
            return getNormalMemberStr(messageEvent);
        }else {
            return String.valueOf(contact.getId());
        }
    }


    public static String getGroupMemberInfo(Long groupId, Set<Long> ids, MembersService membersService){
        StringBuilder stringBuilder = new StringBuilder();
        for (Long id: ids){
            Members members = membersService.selectMember(groupId, id);
            if (members!=null){
                stringBuilder.append(members.getQqId())
                        .append("(").append(members.getNickName()).append(")")
                        .append("[").append(members.getNameCard()).append("]")
                        .append("<").append(members.getSpecialTitle()).append(">")
                        .append(" ").append(members.getQqLevel()).append("; ");
            }
        }
        return stringBuilder.toString();
    }

    public static void sendMessageForIds(List<Long> longs, String messageStr, Contact contact){
        sendMessageForIds(longs, messageStr, contact, true);
    }

    public static void sendMessageForIds(List<Long> longs, String messageStr, Contact contact, boolean isAt){
        int pageSize = 30;
        List<List<Long>> lists = ListUtil.paginateList(longs, pageSize);
        int page = 0,total = lists.size();
        for (List<Long> items: lists){
            page++;
            MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
            for (Long id:items){
                if (isAt) {
                    messageChainBuilder.append(new At(id));
                }else {
                    messageChainBuilder.append(getNormalMemberStr(contact.getId(), id)+", ");
                }
            }
            messageChainBuilder.append("\n").append("current/pageSize: ").append(items.size()+"/"+pageSize).append(" page: ").append(String.valueOf(page)).append("/").append(String.valueOf(total));
            messageChainBuilder.append(" ").append(messageStr);
            contact.sendMessage(messageChainBuilder.build());
            try {Thread.sleep(1000);} catch (InterruptedException ignore) {}
        }
    }

}
