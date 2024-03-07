package com.king.api.messageapis.group;

import com.king.db.service.MembersService;
import com.king.model.CommonResponse;
import com.king.model.GroupId;
import com.king.model.KickRequest;
import com.king.model.MessageEventContext;
import com.king.task.KickTaskImpl;
import com.king.task.MemberActiveImpl;
import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ScheduledFuture;

/**
 * @description: 踢出不活跃
 * @author: xyc0123456789
 * @create: 2023/3/30 0:20
 **/
@Slf4j
@Component
public class KickNegativeMembers extends AbstractGroupMessageApi{

    @Autowired
    private KickTaskImpl kickTask;

    @Autowired
    private MembersService membersService;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        if(!messageEventContext.getContent().startsWith("#kicknegative")){
            return false;
        }
        return isManagerOrMe(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        QuoteReply quoteReply = messageEventContext.getQuoteReply();
        int begin = 13;
        boolean title = content.substring(begin,begin+1).equalsIgnoreCase("t");
        if (title){
            begin+=1;
        }
        int date=-1;
        try {
            date = Integer.parseInt(content.substring(begin).trim());
        }catch (Exception e){}
        if (date==-1){
            MessageChain messages = new MessageChainBuilder().append(quoteReply).append("格式错误").build();
            messageEvent.getSubject().sendMessage(messages);
            return null;
        }

        long groupId = messageEvent.getSubject().getId();
        MemberActiveImpl active = new MemberActiveImpl(membersService, groupId);
        if (groupId!= GroupId.Group985_2){
            active.andInMainGroup();
        }
        active.andLastSpeak(date);
        if (title){
            active.andSpecialTitle();
        }
        active.build();
        Set<Long> longSet = active.toKickNormalMemberIds();
        int total = longSet.size();
        try {
            KickRequest kickRequest = new KickRequest(groupId, longSet, active.getWarnMessage().toString());
            ScheduledFuture<String> ans = kickTask.normalKick(kickRequest);
            String sansStr = ans.get();
            MessageChain messages = new MessageChainBuilder().append(quoteReply).append("total ").append(String.valueOf(total))
                    .append("; kick result:").append(sansStr).append("\n")
                    .append("success ").append(String.valueOf(kickRequest.getKickSuccessIds().size())).append(":").append(NormalMemberUtil.getGroupMemberInfo(kickRequest.getGroupId(), kickRequest.getKickSuccessIds(), membersService)).append("\n")
                    .append("fail ").append(String.valueOf(kickRequest.getKickFailIds().size())).append(":").append(NormalMemberUtil.getGroupMemberInfo(kickRequest.getGroupId(), kickRequest.getKickFailIds(), membersService)).build();
            messageEvent.getSubject().sendMessage(messages);
        }catch (Exception e){
            log.error("", e);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "member.kick.negative";
    }
}
