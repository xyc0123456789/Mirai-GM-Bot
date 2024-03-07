package com.king.api.messageapis;

import com.king.db.service.CommandPermissionService;
import com.king.model.MessageEventContext;
import com.king.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.king.config.CommonConfig.MDC_TRACE_ID;
import static com.king.db.service.CommandPermissionService.FriendMessagePermissionMap;
import static com.king.db.service.CommandPermissionService.getKeyFromMessageDefault;

@Component
@Slf4j
public class FriendMessageApiGroup {

    public final List<FriendMessageApi> friendMessageApis;

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(30);

    public FriendMessageApiGroup(List<FriendMessageApi> friendMessageApis) {
        this.friendMessageApis = friendMessageApis;
    }


    public void handler(MessageEventContext messageEventContext) {
        for (FriendMessageApi friendMessageApi : friendMessageApis) {
            scheduledExecutorService.schedule(() -> {
                try {
                    MDC.put(MDC_TRACE_ID, messageEventContext.getTraceName());
                    String keyFromMessageDefault = getKeyFromMessageDefault(friendMessageApi.commandName(), messageEventContext);
                    boolean permission = FriendMessagePermissionMap.getOrDefault(keyFromMessageDefault,false);
//                    log.info(keyFromMessageDefault+": "+permission);
                    if (permission) {
                        boolean condition = friendMessageApi.condition(messageEventContext);
                        if (condition) {
                            friendMessageApi.handler(messageEventContext);
                        }
                    }
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    MDC.remove(MDC_TRACE_ID);
                }
            }, 0, TimeUnit.SECONDS);
        }
    }

    @PostConstruct
    private void sort() {
        friendMessageApis.sort(Comparator.comparingInt(FriendMessageApi::sortedOrder));
        StringBuilder stringBuilder = new StringBuilder("friendPlainTextApi:");
        Set<String> tmp = new HashSet<>();
        ArrayList<String> repeat = new ArrayList<>();
        for (FriendMessageApi friendMessageApi : friendMessageApis){
            stringBuilder.append(friendMessageApi.getClass().getSimpleName()).append("; ");

            String commandName = friendMessageApi.commandName();
            if (MyStringUtil.isEmpty(commandName)){
                throw new RuntimeException("command name is empty");
            }
            if (tmp.contains(commandName)){
                repeat.add(commandName);
            }
            tmp.add(commandName);
        }
        if (repeat.size()>0){
            throw new RuntimeException("存在重复命令名称:"+repeat);
        }
        log.info(stringBuilder.toString());
    }

}
