package com.king.api.recallapis;

import com.king.component.MyBot;
import com.king.db.pojo.MessageRecord;
import com.king.db.service.MessageRecordService;
import com.king.model.CommonResponse;
import com.king.model.MessageRecallEventContext;
import com.king.model.QQFriendId;
import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.MessageRecallEvent;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/9 16:31
 **/
@Slf4j
@Component
public class GroupRecallHookUpdateMessage extends AbstractGroupMessageRecall{

    @Autowired
    private MessageRecordService messageRecordService;

    @Override
    public boolean condition(MessageRecallEventContext messageRecallEventContext) {
        return true;
    }

    @Override
    public CommonResponse handler(MessageRecallEventContext messageRecallEventContext) {
        MessageRecallEvent.GroupRecall messageRecallEvent = (MessageRecallEvent.GroupRecall) messageRecallEventContext.getMessageRecallEvent();
        int[] messageIds = messageRecallEvent.getMessageIds();
        MessageRecord messageRecord = messageRecordService.getMessageById(messageRecallEvent.getGroup().getId(),messageIds[0]);
        if (messageRecord!=null){
            Member operator = messageRecallEvent.getOperator();
            if (operator!=null) {
                messageRecord.setOperatorId(operator.getId());
                messageRecord.setOperatorName(operator.getNick());
                messageRecord.setEnable(false);
                messageRecordService.update(messageRecord);
            }
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return GroupMessageRecallConstName.GroupMessageHookUpdate;
    }

    @Override
    public boolean defaultStatus() {
        return true;
    }
}
