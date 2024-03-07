package com.king.event;

import com.king.db.service.ContactListenListService;
import com.king.event.AbstractHandler;
import kotlin.coroutines.CoroutineContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.Event;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

@Slf4j
public abstract class AbstractFriendHandler extends AbstractHandler {

    public ConcurrentHashMap<Long,String> filterHashMap = new ConcurrentHashMap<>();

    @Autowired
    protected ContactListenListService contactListenListService;


    @PostConstruct
    public void reload(){
        Map<Long, String> linsteningFriends = contactListenListService.getLinsteningFriends();
        filterHashMap.clear();
        filterHashMap.putAll(linsteningFriends);
        log.info("reload friend filterHashMap:{}", filterHashMap);
    }

    @Override
    public void handleException(CoroutineContext context, Throwable exception){
        // 处理事件处理时抛出的异常
        log.error("on message handleException", exception);
        MDC.remove(MDC_TRACE_ID);
    }
    protected abstract void handlerCallback(Event event) throws Exception;
    protected abstract void handler(Event event) throws Exception;
    protected abstract boolean filterId(Event event);

}
