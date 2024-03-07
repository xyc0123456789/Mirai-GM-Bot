package com.king.util;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;

@Slf4j
public class FriendNotifyUtil {

    public static void notifyFriendMessage(Bot bot,Long id,String message){
        if (MyStringUtil.isEmpty(message)){
            return;
        }
        try {
            bot.getFriend(id).sendMessage(message);
        }catch (Exception e){
            log.error("", e);
        }

    }
}
