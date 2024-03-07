package com.king.api.messageapis.group;

import com.king.config.CommonConfig;
import com.king.db.service.MembersService;
import com.king.model.CommonResponse;
import com.king.model.GroupId;
import com.king.model.LastDetectResult;
import com.king.model.MessageEventContext;
import com.king.task.MemberActiveImpl;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @description: 检测不活跃
 * @author: xyc0123456789
 * @create: 2023/3/30 0:19
 **/
@Slf4j
@Component
public class DetectNegativeMembers extends AbstractGroupMessageApi{

    @Autowired
    private MembersService membersService;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        if(!messageEventContext.getContent().startsWith("#detectnegative")){
            return false;
        }
        return isManagerOrMe(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        QuoteReply quoteReply = messageEventContext.getQuoteReply();
        int begin = 15;
        boolean title = content.substring(begin,begin+1).equalsIgnoreCase("t");
        if (title){
            begin+=1;
        }
        boolean at = content.substring(begin, begin+1).equalsIgnoreCase("a");
        if (at){
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
//        if (groupId!= GroupId.Group985_2){
//            active.andInMainGroup();
//        }
        active.andLastSpeak(date);
        if (title){
            active.andSpecialTitle();
        }
        active.build();
        active.sendWarnMessage(quoteReply, at);
        List<Long> longSet = new ArrayList<>(active.toKickNormalMemberIds());
        if (!longSet.isEmpty()) {
            CommonConfig.lastDetect.put(groupId, new LastDetectResult(longSet, 2));
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "member.detect.active";
    }
}
