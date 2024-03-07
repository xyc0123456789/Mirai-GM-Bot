package com.king.event;

import com.king.db.service.ContactListenListService;
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
public abstract class AbstractGroupHandler extends AbstractHandler {

    public ConcurrentHashMap<Long,String> filterHashMap = new ConcurrentHashMap<>();

    @Autowired
    protected ContactListenListService contactListenListService;


    @PostConstruct
    public void reload(){
        Map<Long, String> linsteningGroups = contactListenListService.getLinsteningGroups();
        filterHashMap.clear();
        filterHashMap.putAll(linsteningGroups);
        log.info(this.getClass().getSimpleName()+" reload group filterHashMap:{}", filterHashMap);
    }

    @Override
    public void handleException(CoroutineContext context, Throwable exception){
        // 处理事件处理时抛出的异常
        exception.printStackTrace();
        MDC.remove(MDC_TRACE_ID);
    }

    protected abstract void handlerCallback(Event event) throws Exception;

    public abstract boolean filterId(Event event);

    public abstract void handler(Event event) throws Exception;
}
