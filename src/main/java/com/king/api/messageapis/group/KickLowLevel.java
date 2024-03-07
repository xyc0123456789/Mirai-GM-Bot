package com.king.api.messageapis.group;

import com.king.config.CommonConfig;
import com.king.db.service.MembersService;
import com.king.model.CommonResponse;
import com.king.model.KickRequest;
import com.king.model.MessageEventContext;
import com.king.task.KickTaskImpl;
import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Component
public class KickLowLevel extends AbstractGroupMessageApi {

    private final KickTaskImpl kickTask;

    private final MembersService membersService;

    public KickLowLevel(KickTaskImpl kickTask, MembersService membersService) {
        this.kickTask = kickTask;
        this.membersService = membersService;
    }

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        if (!messageEventContext.getContent().startsWith("#kicklowlevel")){
            return false;
        }
        return isManagerOrMe(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        QuoteReply quoteReply = messageEventContext.getQuoteReply();
        String content = messageEventContext.getContent();
        int low = -1;
        String level = content.substring(13).trim();
        try {
            low = Integer.parseInt(level);
        }catch (Exception ignored){}

        if (low == -1){
            MessageChain messages = new MessageChainBuilder().append(quoteReply).append("格式错误，示例: #kicklowlevel 99").build();
            messageEvent.getSubject().sendMessage(messages);
            return null;
        }

        try {
            Set<Long> lowLevelSet = DetectLowLevelMembers.detectLowLevel(membersService, messageEvent, low);
            lowLevelSet.removeAll(CommonConfig.lowLevelWhiteList);
            int total = lowLevelSet.size();
            KickRequest kickRequest = new KickRequest(messageEvent.getSubject().getId(), lowLevelSet, "qq level<=" + low);
            ScheduledFuture<String> ans = kickTask.normalKick(kickRequest);
            String sansStr = ans.get();
            MessageChain messages = new MessageChainBuilder().append(quoteReply).append("QQ level <= ")
                    .append(String.valueOf(low)).append("; total ").append(String.valueOf(total))
                    .append("; kick result:").append(sansStr).append("\n")
                    .append("success ").append(String.valueOf(kickRequest.getKickSuccessIds().size())).append(":").append(NormalMemberUtil.getGroupMemberInfo(kickRequest.getGroupId(), kickRequest.getKickSuccessIds(), membersService)).append("\n")
                    .append("fail ").append(String.valueOf(kickRequest.getKickFailIds().size())).append(":").append(NormalMemberUtil.getGroupMemberInfo(kickRequest.getGroupId(), kickRequest.getKickFailIds(), membersService)).build();
            messageEvent.getSubject().sendMessage(messages);
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
        return "member.kick.low.level";
    }
}
