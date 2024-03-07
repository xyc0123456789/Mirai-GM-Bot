package com.king.event.group;

import com.king.api.memberapis.MemberApiGroup;
import com.king.config.CommonConfig;
import com.king.event.AbstractGroupMemberHandler;
import com.king.model.MemberEventContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.GroupMemberEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

@Slf4j
@Component
public class Bot2MemberEventHandler extends AbstractGroupMemberHandler {

    @EventHandler
    public void onMessage(Event event) throws Exception { // 可以抛出任何异常, 将在 handleException 处理
        handlerCallback(event);
    }

    @Override
    public void handlerCallback(Event event) throws Exception {
        GroupMemberEvent groupMemberEvent = (GroupMemberEvent) event;
        Group group = groupMemberEvent.getGroup();
        MDC.put(MDC_TRACE_ID, String.format("[BOT2]%s(%s)",group.getName(),group.getId()));
        log.info(groupMemberEvent.toString());
//        if (filterId(event)){
        handler(event);
//        }
        MDC.remove(MDC_TRACE_ID);
    }

    private static final String[] messages = new String[]{
            "欢迎新朋友加入我们的大家庭，让我们一起愉快地聊天吧！" ,
            "热烈欢迎新成员的加入，希望我们可以成为好朋友！" ,
            "欢迎来到我们的群聊，让我们一起分享生活、快乐和梦想！" ,
            "欢迎新朋友加入，我们的群聊将因你的加入变得更加丰富多彩！" ,
            "欢迎新成员的到来，让我们一起交流、分享、成长！" ,
            "欢迎加入我们的大家庭，我们期待着和你一起创造美好的回忆！" ,
            "欢迎新成员，让我们一起畅所欲言、共同成长！",
            "欢迎来到我们的群组，我们很高兴有你的加入！",
            "欢迎加入我们的大家庭，希望你能在这里找到快乐和归属感。",
            "欢迎来到我们的小天地，希望你能和大家一起分享生活、交流经验。",
            "欢迎加入我们的圈子，期待你能为我们带来更多的创意和想法。",
            "欢迎加入我们的行列，让我们一起学习、成长、进步。",
            "热烈欢迎你的加入，希望你能在这里结交更多的朋友，分享更多的经验。",
            "欢迎来到我们的大家庭，让我们一起团结、协作、共同进步。",
            "欢迎加入我们的群组，希望你能在这里找到属于你的舞台，展示你的才华。",
            "欢迎你的到来，让我们一起愉快地度过每一天，创造更加美好的未来。",
            "欢迎来到我们的世界，让我们一起分享快乐，克服困难，共同成长。"
    };

    // 成员退出进入
    public void handler(Event event){
        GroupMemberEvent memberEvent = (GroupMemberEvent) event;
        if (memberEvent instanceof MemberJoinEvent){
            Group group = memberEvent.getGroup();
            MemberJoinEvent memberJoinEvent = (MemberJoinEvent) memberEvent;
            NormalMember member = memberJoinEvent.getMember();
            At at = new At(member.getId());

            Random random = new Random();
            try {Thread.sleep(random.nextInt(4)*1000+2000);} catch (InterruptedException ignore) {}

            String message = messages[random.nextInt(messages.length)];
            MessageChain messageChain = new MessageChainBuilder().append(at).append(message).build();
            log.info(messageChain.serializeToMiraiCode());
            group.sendMessage(messageChain);
        }
    }
}
