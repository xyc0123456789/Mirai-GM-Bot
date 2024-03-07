package com.king.api.recallapis;

import com.king.component.MyBot;
import com.king.db.pojo.MessageRecord;
import com.king.db.service.MessageRecordService;
import com.king.model.CommonResponse;
import com.king.model.MessageRecallEventContext;
import com.king.model.QQFriendId;
import com.king.util.MyStringUtil;
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
public class GroupRecallResendMessageToMe extends AbstractGroupMessageRecall{

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
        MessageRecord messageRecord = messageRecordService.getMessageById(messageRecallEvent.getGroup().getId(), messageIds[0]);
        if (messageRecord!=null){
            String messageMiraiCode = messageRecord.getMessageMiraiCode();
            if (MyStringUtil.isEmpty(messageMiraiCode)){
                return null;
            }
            if (messageMiraiCode.contains("[mirai:file")){
                //"[mirai:file:/013412a0-23b1-44fc-a5e9-ac1533f667f2,102,1-s2.0-S0379711218303291-main.pdf,2686598]"
                messageMiraiCode = messageMiraiCode.replaceAll("mirai:","");
            }

            Friend friend = MyBot.bot.getFriend(QQFriendId.ME);
            if (friend!=null){
                StringBuilder stringBuilder = new StringBuilder();
                Group group = MyBot.bot.getGroup(messageRecord.getGroupId());
                Member operator = messageRecallEvent.getOperator();
                if (operator!=null) {
                    stringBuilder.append(operator.getNick()).append("(").append(operator.getId()).append(")");
                }
                stringBuilder.append(messageRecord.getDateTime());
                if (group!=null) {
                    stringBuilder.append(NormalMemberUtil.getGroupStr(group));
                    NormalMember normalMember = group.get(messageRecord.getQqId());
                    if (normalMember!=null){
                        stringBuilder.append(NormalMemberUtil.getNormalMemberStr(normalMember));
                    }
                    stringBuilder.append(": ");
                }
                try {
                    MessageChainBuilder chainBuilder = new MessageChainBuilder();
                    chainBuilder.append(stringBuilder.toString());
                    chainBuilder.append(MiraiCode.deserializeMiraiCode(messageMiraiCode));
                    friend.sendMessage(chainBuilder.build());
                }catch (Exception e){
                    log.error("", e);
                    stringBuilder.append(messageMiraiCode);
                    friend.sendMessage(stringBuilder.toString());
                }
            }
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 50;
    }

    @Override
    public String commandName() {
        return GroupMessageRecallConstName.GroupMessageRecallResendMe;
    }

    @Override
    public boolean defaultStatus() {
        return true;
    }
}
