package com.king.task;

import com.king.component.MyBot;
import com.king.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.GroupSettings;

@Slf4j
public class MuteAllImpl {

    public static void muteAll(Long groupId, boolean muteTargetStatus) {
        muteAll(groupId, muteTargetStatus, true);
    }
    public static void muteAll(Long groupId, boolean muteTargetStatus, boolean sendMessage) {
        Group group = MyBot.bot.getGroup(groupId);
        String switchWord = muteTargetStatus ? "开启" : "关闭";
        if (group == null) {
            log.error("groupId:{} {}全员禁言失败", groupId, switchWord);
            return;
        }
        if (group.getBotPermission().getLevel() == 0) {
            log.error("groupId:{} {}全员禁言失败, bot权限不足", groupId, switchWord);
            return;
        }
        GroupSettings settings = group.getSettings();
        boolean muteAll = settings.isMuteAll();
        String message;
        if (muteAll != muteTargetStatus) {
            try {
                settings.setMuteAll(muteTargetStatus);
                message = switchWord + "全员禁言成功";
            } catch (Exception e) {
                log.error("muteAll err", e);
                message = switchWord + "全员禁言失败: " + e.getMessage();
            }
            log.info(message);
        } else {
            log.warn("已" + switchWord + "全员禁言");
            message = null;
        }
        if (sendMessage && !MyStringUtil.isEmpty(message)) {
            group.sendMessage(message);
        }
    }

}
