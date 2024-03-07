package com.king.api.messageapis.group;

import com.king.config.CommonConfig;
import com.king.db.pojo.Members;
import com.king.db.service.MembersService;
import com.king.model.CommonResponse;
import com.king.model.LastDetectResult;
import com.king.model.MessageEventContext;
import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.data.UserProfile;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@Slf4j
public class DetectLowLevelMembers extends AbstractGroupMessageApi {

    private final MembersService membersService;

    public DetectLowLevelMembers(MembersService membersService) {
        this.membersService = membersService;
    }

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        if(!messageEventContext.getContent().startsWith("#detectlowlevel")){
            return false;
        }
        return isManagerOrMe(messageEventContext);
    }

    public static Set<Long> detectLowLevel(MembersService membersService, MessageEvent messageEvent, int low){
        Group group = (Group)messageEvent.getSubject();
        long groupId = group.getId();
        ContactList<NormalMember> members = group.getMembers();
        Set<Long> lowLevelSet = new HashSet<>();
        for (NormalMember normalMember:members){
            Members sqlMember = membersService.selectMember(groupId, normalMember.getId());
            int qLevel;
            if (sqlMember==null||sqlMember.getQqLevel()==null) {
                UserProfile userProfile = normalMember.queryProfile();
                qLevel = userProfile.getQLevel();
            }else {
                qLevel = sqlMember.getQqLevel();
            }
            if (qLevel<=low) {
                lowLevelSet.add(normalMember.getId());
            }
        }
        return lowLevelSet;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        QuoteReply quoteReply = messageEventContext.getQuoteReply();
        String content = messageEventContext.getContent();
        log.info("#detectlowlevel :" +content);
        int low = -1;
        String level = content.substring(15).trim();
        try {
            low = Integer.parseInt(level);
        }catch (Exception ignored){}

        if (low == -1){
            MessageChain messages = new MessageChainBuilder().append(quoteReply).append("格式错误，示例：#detectlowlevel 99").build();
            messageEvent.getSubject().sendMessage(messages);
            return null;
        }
        log.info("#detectlowlevel :" + low);
        try {
            Group group = (Group)messageEvent.getSubject();
            long groupId = group.getId();
            Set<Long> lowLevelSet = DetectLowLevelMembers.detectLowLevel(membersService, messageEvent, low);
            lowLevelSet.removeAll(CommonConfig.lowLevelWhiteList);
            MessageChain messages = new MessageChainBuilder().append(quoteReply).append("QQ level <= ")
                    .append(String.valueOf(low)).append(" total ").append(String.valueOf(lowLevelSet.size()))
                    .append(": ").append(NormalMemberUtil.getGroupMemberInfo(groupId, lowLevelSet, membersService)).build();
            messageEvent.getSubject().sendMessage(messages);

            List<Long> longSet = new ArrayList<>(lowLevelSet);
            if (!longSet.isEmpty()) {
                CommonConfig.lastDetect.put(groupId, new LastDetectResult(longSet, 3));
            }
        }catch (Exception e){
            MessageChain messages = new MessageChainBuilder().append(quoteReply).append(e.getMessage()).build();
            messageEvent.getSubject().sendMessage(messages);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "members.detect.low.level";
    }
}
