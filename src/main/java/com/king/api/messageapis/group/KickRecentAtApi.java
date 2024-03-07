package com.king.api.messageapis.group;

import com.king.config.CommonConfig;
import com.king.db.service.MembersService;
import com.king.model.CommonResponse;
import com.king.model.KickRequest;
import com.king.model.LastDetectResult;
import com.king.model.MessageEventContext;
import com.king.task.KickTaskImpl;
import com.king.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description: 提出上次检测的人
 * @author: xyc0123456789
 * @create: 2023/5/22 13:19
 **/
@Slf4j
@Component
public class KickRecentAtApi extends AbstractGroupMessageApi {

    @Autowired
    private KickTaskImpl kickTask;

    @Autowired
    private MembersService membersService;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        if (!"#kicklastdetect".equals(messageEventContext.getContent().trim())){
            return false;
        }
        return isManagerOrMe(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        Group group = (Group) messageEvent.getSubject();
        long groupId = group.getId();
        QuoteReply quoteReply = messageEventContext.getQuoteReply();

        LastDetectResult lastDetectResult = CommonConfig.lastDetect.get(groupId);
        List<Long> longList = lastDetectResult.getIds();
        Integer type = lastDetectResult.getType();
        if (CollectionUtils.isEmpty(longList)){
            group.sendMessage(new MessageChainBuilder()
                    .append(quoteReply).append("请先使用#detect等相关命令").build());
        }else {
//            messageEvent.getSubject().sendMessage(new MessageChainBuilder()
//                    .append(quoteReply).append("toKickList:"+ longList).build());
            Set<Long> longSet = new HashSet<>();
            if (type.equals(0)) {//新加头衔的人员需要剔除
                for (Long id : longList) {
                    NormalMember normalMember = group.get(id);
                    if (normalMember != null) {
                        if (MyStringUtil.isEmpty(normalMember.getSpecialTitle())) {
                            longSet.add(id);
                        }
                    }
                }
            }else {
                longSet.addAll(longList);
            }

            
            KickRequest kickRequest = new KickRequest(groupId, longSet, "");
            try {
                kickTask.kickAndSendResult(kickRequest, group);
            } catch (Exception e) {
                log.error("", e);
            }
        }

        return null;
    }

    @Override
    public int sortedOrder() {
        return 96;
    }

    @Override
    public String commandName() {
        return "member.kick.last.detect";
    }
}
