package com.king.api.messageapis.common;

import com.king.api.messageapis.common.keyword.KeyWordHandler;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.DFAUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.king.config.CommonConfig.MDC_TRACE_ID;

@Slf4j
@Component
public class HttpKeyWordApi extends AbstractCommonMessageApi {

    @Autowired
    private List<KeyWordHandler> keyWordHandlerList;

    private final DFAUtil dfaUtil = new DFAUtil();


    public static final Map<String, KeyWordHandler> keyWordHandlerMap = new HashMap<>();

    @PostConstruct
    private void init(){
        Set<String> tmpSet = new HashSet<>();
        for (KeyWordHandler handler:keyWordHandlerList){
            tmpSet.add(handler.word());
            keyWordHandlerMap.put(handler.word(), handler);
        }
        dfaUtil.createDFAHashMap(tmpSet);
    }

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(30);

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        Set<String> sensitiveWordByDFAMap = dfaUtil.getSensitiveWordByDFAMap(messageEventContext.getContent(), 1);
        return !CollectionUtils.isEmpty(sensitiveWordByDFAMap);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        Set<String> sensitiveWordByDFAMap = dfaUtil.getSensitiveWordByDFAMap(content, 1);
        for (String keyWord:sensitiveWordByDFAMap){
            scheduledExecutorService.submit(()->{
                try {
                    MDC.put(MDC_TRACE_ID, messageEventContext.getTraceName());
                    keyWordHandlerMap.get(keyWord).handler(messageEventContext);
                }catch (Exception e){
                    log.error(keyWord+" handle err", e);
                }finally {
                    MDC.remove(MDC_TRACE_ID);
                }
            });
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 9;
    }

    @Override
    public String commandName() {
        return "keyword";
    }

}
