package com.king.api.messageapis.group;

import com.king.db.pojo.ContactListenList;
import com.king.db.service.ContactListenListService;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class JoinMessageEdit extends AbstractGroupMessageApi {

    @Autowired
    private ContactListenListService contactListenListService;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        if (!content.startsWith("#进群通知")){
            return false;
        }
        return isManagerOrMe(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        String message = messageEventContext.getMessageEvent().getMessage().serializeToMiraiCode().substring(5);
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        if (messageEvent.getSubject() instanceof Group){
            Group group = (Group) messageEvent.getSubject();
            if (MyStringUtil.isEmpty(message)){
                ContactListenList contactListenList = contactListenListService.getGroupByPrimaryKey(group.getId());
                contactListenList.setPermission(null);
                contactListenListService.update(contactListenList);
                return null;
            }
            if (message.charAt(0)==' '){
                message = message.substring(1);
            }
            NormalMember normalMember = group.get(messageEvent.getSender().getId());
            String name = normalMember.getNameCard();
            if (MyStringUtil.isEmpty(name)){
                name = normalMember.getNick();
            }
            message = message+"\n——"+name+"["+normalMember.getId()+"]编辑";

            ContactListenList contactListenList = contactListenListService.getGroupByPrimaryKey(group.getId());
            contactListenList.setPermission(message);
            contactListenListService.update(contactListenList);
            QuoteReply quote = messageEventContext.getQuoteReply();
            group.sendMessage(new MessageChainBuilder().append(quote).append("进群通知设置成功").build());
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 90;
    }

    @Override
    public String commandName() {
        return "join.message.edit";
    }
}
