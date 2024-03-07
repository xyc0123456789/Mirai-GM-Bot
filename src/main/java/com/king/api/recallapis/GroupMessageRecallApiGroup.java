package com.king.api.recallapis;

import com.king.api.messageapis.GroupMessageApi;
import com.king.api.messageapis.TempGroupMessageApi;
import com.king.model.MessageRecallEventContext;
import com.king.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.king.api.recallapis.GroupMessageRecallConstName.GroupMessageHookUpdate;
import static com.king.api.recallapis.GroupMessageRecallConstName.GroupMessageRecallResendMe;
import static com.king.config.CommonConfig.MDC_TRACE_ID;
import static com.king.db.service.CommandPermissionService.*;
import static com.king.db.service.CommandPermissionService.GroupMessagePermissionMap;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/9 15:26
 **/
@Slf4j
@Component
public class GroupMessageRecallApiGroup {

    public final List<GroupMessageRecallApi> groupMessageRecallApis;

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    public GroupMessageRecallApiGroup(List<GroupMessageRecallApi> groupMessageRecallApiList) {
        this.groupMessageRecallApis = groupMessageRecallApiList;
    }

    public static final Set<String> globalGroupCommand = new HashSet<>();

    static {
        globalGroupCommand.add(GroupMessageHookUpdate);
        globalGroupCommand.add(GroupMessageRecallResendMe);
    }

    public void handler(MessageRecallEventContext messageRecallEventContext){
        for (GroupMessageRecallApi groupMessageRecallApi : groupMessageRecallApis) {
            scheduledExecutorService.schedule(() -> {
                try {
                    MDC.put(MDC_TRACE_ID, messageRecallEventContext.getTraceName());
                    String keyFromMessageDefault = getKeyFromMessageRecallDefault(groupMessageRecallApi.commandName(), messageRecallEventContext, false);
                    boolean permission = GroupMessagePermissionMap.getOrDefault(keyFromMessageDefault,false);
//                    log.info(keyFromMessageDefault+": "+permission);
                    if (permission) {
                        boolean personPermission;
                        if (globalGroupCommand.contains(groupMessageRecallApi.commandName())){
                            personPermission = true;
                        }else {
                            String keyFromMessageForPerson = getKeyFromMessageRecallDefault(groupMessageRecallApi.commandName(), messageRecallEventContext, true);
                            personPermission = GroupMessagePermissionMap.getOrDefault(keyFromMessageForPerson, true);
                        }
//                        log.info("{}: {}",keyFromMessageForPerson,personPermission);
                        if (personPermission) {
                            boolean condition = groupMessageRecallApi.condition(messageRecallEventContext);
                            if (condition) {
                                groupMessageRecallApi.handler(messageRecallEventContext);
                            }
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
        groupMessageRecallApis.sort(Comparator.comparingInt(GroupMessageRecallApi::sortedOrder));
        StringBuilder stringBuilder = new StringBuilder("groupMessageRecallApis:");
        Set<String> tmp = new HashSet<>();
        ArrayList<String> repeat = new ArrayList<>();
        for (GroupMessageRecallApi groupMessageRecallApi : groupMessageRecallApis){
            stringBuilder.append(groupMessageRecallApi.getClass().getSimpleName()).append("; ");

            String commandName = groupMessageRecallApi.commandName();
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
