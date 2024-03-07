package com.king.api.messageapis.group;

import com.king.config.CommonConfig;
import com.king.db.service.MembersService;
import com.king.model.CommonResponse;
import com.king.model.GroupId;
import com.king.model.LastDetectResult;
import com.king.model.MessageEventContext;
import com.king.task.MemberActiveImpl;
import com.king.util.MyStringUtil;
import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description: 检测指定头衔
 * @author: xyc0123456789
 * @create: 2023/3/30 0:19
 **/
@Slf4j
@Component
public class DetectSpecialTitleMembers extends AbstractGroupMessageApi{

    @Autowired
    private MembersService membersService;

    private static final String command = "#detectspecialtitle";

    private static final int commandLen = command.length();

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        if(!messageEventContext.getContent().startsWith(command)){
            return false;
        }
        return isManagerOrMe(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        QuoteReply quoteReply = messageEventContext.getQuoteReply();

        Set<String> keys = new HashSet<>();
        String[] split = content.substring(commandLen).split("\\s+");
        for (String key:split){
            if (MyStringUtil.isEmpty(key)){
                continue;
            }
            keys.add(key.trim());
        }

        Group group = (Group) messageEvent.getSubject();
        List<Long> toKick = new ArrayList<>();
        for (NormalMember normalMember: group.getMembers()){
            String specialTitle = normalMember.getSpecialTitle();
            if (MyStringUtil.isEmpty(specialTitle)){
                continue;
            }
            for (String key: keys){
                if (specialTitle.contains(key)){
                    toKick.add(normalMember.getId());
                    break;
                }
            }
        }
        if (CollectionUtils.isEmpty(toKick)){
            String messageInfo = "关键词: " + keys + " 未能匹配到结果";
            group.sendMessage(new MessageChainBuilder().append(quoteReply).append(messageInfo).build());
        }else {
            CommonConfig.lastDetect.put(group.getId(), new LastDetectResult(toKick, 1));
            String messageInfo = "关键词: " + keys + " 匹配结果如上";
            NormalMemberUtil.sendMessageForIds(toKick, messageInfo, group, false);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "member.detect.keyword.special.title";
    }
}
