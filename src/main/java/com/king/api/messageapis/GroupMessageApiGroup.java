package com.king.api.messageapis;

import com.king.api.recallapis.GroupMessageRecallConstName;
import com.king.model.MessageEventContext;
import com.king.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.king.config.CommonConfig.MDC_TRACE_ID;
import static com.king.db.service.CommandPermissionService.*;

@Component
@Slf4j
public class GroupMessageApiGroup {

    public final List<GroupMessageApi> groupMessageApis;

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(30);

    public GroupMessageApiGroup(List<GroupMessageApi> groupMessageApis) {
        this.groupMessageApis = groupMessageApis;
    }


    public static final Set<String> globalGroupCommand = new HashSet<>();
    static {
        globalGroupCommand.add("message.recall");
        globalGroupCommand.add("message.count");
        globalGroupCommand.add("message.content.save");
        globalGroupCommand.add("flood.screen");
        globalGroupCommand.add("audio.resend");
        globalGroupCommand.add("image.qrcode.detect");
        globalGroupCommand.add("image.resend");
        globalGroupCommand.add("message.record.all");
        globalGroupCommand.add(GroupMessageRecallConstName.GroupMessageRecallResendMe);
    }

    public void handler(MessageEventContext messageEventContext) {
        for (GroupMessageApi groupMessageApi : groupMessageApis) {
            scheduledExecutorService.schedule(() -> {
                try {
                    MDC.put(MDC_TRACE_ID, messageEventContext.getTraceName());
                    String keyFromMessageDefault = getKeyFromMessageDefault(groupMessageApi.commandName(), messageEventContext);
                    boolean permission = GroupMessagePermissionMap.getOrDefault(keyFromMessageDefault,false);
//                    log.info(keyFromMessageDefault+": "+permission);
                    if (permission) {
                        boolean personPermission;
                        if (globalGroupCommand.contains(groupMessageApi.commandName())){
                            personPermission = true;
                        }else {
                            String keyFromMessageForPerson = getKeyFromMessageForPerson(groupMessageApi.commandName(), messageEventContext);
                            personPermission = GroupMessagePermissionMap.getOrDefault(keyFromMessageForPerson, true);
                        }
//                        log.info("{}: {}",keyFromMessageForPerson,personPermission);
                        if (personPermission) {
                            boolean condition = groupMessageApi.condition(messageEventContext);
                            if (condition) {
                                groupMessageApi.handler(messageEventContext);
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
        groupMessageApis.sort(Comparator.comparingInt(GroupMessageApi::sortedOrder));
        StringBuilder stringBuilder = new StringBuilder("groupPlainTextApi:");
        Set<String> tmp = new HashSet<>();
        ArrayList<String> repeat = new ArrayList<>();
        for (GroupMessageApi groupMessageApi : groupMessageApis){
            stringBuilder.append(groupMessageApi.getClass().getSimpleName()).append("; ");

            String commandName = groupMessageApi.commandName();
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
