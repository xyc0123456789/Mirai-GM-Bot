package com.king.api.memberapis;

import com.king.model.MemberEventContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.king.config.CommonConfig.MDC_TRACE_ID;
import static com.king.db.service.CommandPermissionService.*;

@Slf4j
@Component
public class MemberApiGroup {

    @Autowired
    public List<MemberApi> memberApiList;


    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(30);


    public void handler(MemberEventContext memberEventContext) {
        for (MemberApi memberApi : memberApiList) {
            scheduledExecutorService.schedule(() -> {
                try {
                    MDC.put(MDC_TRACE_ID, memberEventContext.getTraceName());
                    String keyFromMessageDefault = getKeyFromMessageDefault(memberApi.commandName(), memberEventContext);
                    boolean permission = GroupMemberPermissionMap.getOrDefault(keyFromMessageDefault,false);
//                    log.info(keyFromMessageDefault+": "+permission);
                    if (permission) {
                        boolean condition = memberApi.condition(memberEventContext);
                        if (condition) {
                            memberApi.handler(memberEventContext);
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
        memberApiList.sort(Comparator.comparingInt(MemberApi::sortedOrder));
    }

}
