package com.king.resource.notifyimpl;

import com.king.component.MyBot;
import com.king.model.GroupId;
import com.king.model.QQFriendId;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@Slf4j
public class SendMessage implements NotifyApi {
    @Override
    public void handler(Map<String, String> request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        log.info(request.toString());
        String message = "cloudfare notify";
        message = request.getOrDefault("message", message);
        String idStr = request.get("idStr");
        Long id = null;
        if (idStr != null) {
            try {
                id = Long.parseLong(idStr);
            } catch (Exception ignored) {
            }
        }
        log.info("Notify:{} {}", id, message);
        Friend me = MyBot.bot.getFriend(QQFriendId.ME);
//        if (id != null) {
//            Friend friend = MyBot.bot.getFriend(id);
            Group group = MyBot.bot.getGroup(GroupId.TEST);
//            if (friend != null) {
//                friend.sendMessage(message);
//            } else
            if (group != null) {
                MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
                messageChainBuilder.append(new At(QQFriendId.ME));
                messageChainBuilder.append(message);
                group.sendMessage(messageChainBuilder.build());
            } else {
                if (me != null) {
                    me.sendMessage(message);
                }
            }
//        } else {
//            if (me != null) {
//                me.sendMessage(message);
//            }
//        }
    }

    @Override
    public String type() {
        return "cloudfare";
    }
}
