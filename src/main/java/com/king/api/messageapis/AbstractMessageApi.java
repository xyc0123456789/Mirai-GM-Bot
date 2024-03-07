package com.king.api.messageapis;

import com.king.config.CommonConfig;
import com.king.model.MessageEventContext;
import com.king.model.QQFriendId;
import com.king.util.JsonUtil;
import com.king.util.MyStringUtil;
import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageSource;

import java.util.Map;

@Slf4j
public abstract class AbstractMessageApi implements MessageApi {

    protected boolean isManagerOrMe(MessageEventContext messageEventContext){
        return permissionLevelOrMe(messageEventContext, 0);
    }

    protected boolean isOwnerOrMe(MessageEventContext messageEventContext){
        return permissionLevelOrMe(messageEventContext, 1);
    }

    protected boolean permissionLevelOrMe(MessageEventContext messageEventContext, int level){
        Contact subject = messageEventContext.getMessageEvent().getSubject();
        if (subject instanceof Group) {
            Group group = (Group) subject;
            long id = messageEventContext.getMessageEvent().getSender().getId();
            NormalMember normalMember = group.get(id);
            if (normalMember == null) {
                log.error("获取权限失败{}【{}】", group.getId(), id);
                return false;
            }
            MemberPermission permission = normalMember.getPermission();
            return permission.getLevel() > level || normalMember.getId() == QQFriendId.ME;
        }else {
            return subject.getId() == QQFriendId.ME;
        }
    }



    protected String updateJsonField(String oriString, Object... kv){
        if (MyStringUtil.isEmpty(oriString)){
            return JsonUtil.json(kv);
        }
        if(kv == null || kv.length % 2 != 0) {
            throw new RuntimeException("参数长度错误");
        }
        Map map = JsonUtil.fromJson(oriString, Map.class);
        for(int i = 0; i < kv.length / 2; i++) {
            map.put(kv[i * 2], kv[i * 2 +1]);
        }
        return JsonUtil.toJson(map);
    }


    public static void kick(MessageEvent messageEvent){
        Contact subject = messageEvent.getSubject();
        if (subject instanceof Group){
            Group group = (Group) subject;
            NormalMember normalMember = group.get(messageEvent.getSender().getId());
            if (normalMember!=null&& !CommonConfig.kickWhiteList.contains(normalMember.getId())){
                MemberPermission botPermission = group.getBotPermission();
                if (botPermission.getLevel()>0) {
                    group.sendMessage(NormalMemberUtil.getNormalMemberStr(normalMember)+"将因刷屏被踢出群聊");
                    normalMember.kick("刷屏");
                }
            }
        }
    }

    public static void recall(MessageEventContext messageEventContext){
        Contact subject = messageEventContext.getMessageEvent().getSubject();
        if (subject instanceof Group){
            Group group = (Group) subject;
            MemberPermission botPermission = group.getBotPermission();
            if (botPermission.getLevel()>0) {
                try {
                    MessageSource.recall(messageEventContext.getMessageSource());
                }catch (Exception ignored){}
            }
        }
    }


    public boolean commandType() {
        return false;
    }

    public boolean defaultStatus() {
        return true;
    }
}
