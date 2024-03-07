package com.king.api.messageapis;

import com.king.api.messageapis.tempmessageapis.TempDefaultAnswerApi;
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
import java.util.concurrent.atomic.AtomicInteger;

import static com.king.config.CommonConfig.MDC_TRACE_ID;
import static com.king.db.service.CommandPermissionService.*;

@Component
@Slf4j
public class TempGroupMessageApiGroup {

    public final List<TempGroupMessageApi> tempGroupMessageApis;

    @Autowired
    private TempDefaultAnswerApi tempDefaultAnswerApi;

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(30);

    public TempGroupMessageApiGroup(List<TempGroupMessageApi> tempGroupMessageApis) {
        this.tempGroupMessageApis = tempGroupMessageApis;
    }


    public static final Set<String> globalGroupCommand = new HashSet<>();
    static {
        globalGroupCommand.add("temp.group.message.record.all");
//        globalGroupCommand.add("message.content.save");
//        globalGroupCommand.add("flood.screen");
//        globalGroupCommand.add("audio.resend");
//        globalGroupCommand.add("image.qrcode.detect");
//        globalGroupCommand.add("image.resend");
//        globalGroupCommand.add("message.record.all");
    }

    public void handler(MessageEventContext messageEventContext) {
        AtomicInteger count = new AtomicInteger(0);
        for (TempGroupMessageApi tempGroupMessageApi : tempGroupMessageApis) {
            scheduledExecutorService.schedule(() -> {
                try {
                    MDC.put(MDC_TRACE_ID, messageEventContext.getTraceName());
                    String keyFromMessageDefault = getKeyFromMessageDefault(tempGroupMessageApi.commandName(), messageEventContext);
                    boolean permission = TempGroupMessagePermissionMap.getOrDefault(keyFromMessageDefault,false);
//                    log.info(keyFromMessageDefault+": "+permission);
                    if (permission) {
                        boolean personPermission;
                        if (globalGroupCommand.contains(tempGroupMessageApi.commandName())){
                            personPermission = true;
                        }else {
                            String keyFromMessageForPerson = getKeyFromMessageForPerson(tempGroupMessageApi.commandName(), messageEventContext);
                            personPermission = TempGroupMessagePermissionMap.getOrDefault(keyFromMessageForPerson, true);
                        }
//                        log.info("{}: {}",keyFromMessageForPerson,personPermission);
                        if (personPermission) {
                            boolean condition = tempGroupMessageApi.condition(messageEventContext);
                            if (condition) {
                                if (!globalGroupCommand.contains(tempGroupMessageApi.commandName())) {
                                    count.getAndAdd(1);
                                }
                                tempGroupMessageApi.handler(messageEventContext);
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
        try {
            MDC.put(MDC_TRACE_ID, messageEventContext.getTraceName());
            Thread.sleep(1000);
            if (count.get()==0){
                tempDefaultAnswerApi.handler(messageEventContext);
            }
        } catch (Exception e) {
            log.error("", e);
        }finally {
            MDC.remove(MDC_TRACE_ID);
        }
    }

    @PostConstruct
    private void sort() {
        tempGroupMessageApis.sort(Comparator.comparingInt(TempGroupMessageApi::sortedOrder));
        StringBuilder stringBuilder = new StringBuilder("TempGroupMessageApi:");
        Set<String> tmp = new HashSet<>();
        ArrayList<String> repeat = new ArrayList<>();
        for (TempGroupMessageApi tempGroupMessageApi : tempGroupMessageApis){
            stringBuilder.append(tempGroupMessageApi.getClass().getSimpleName()).append("; ");

            String commandName = tempGroupMessageApi.commandName();
            if (MyStringUtil.isEmpty(commandName)){
                throw new RuntimeException(tempGroupMessageApi.getClass().getSimpleName()+" command name is empty");
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
