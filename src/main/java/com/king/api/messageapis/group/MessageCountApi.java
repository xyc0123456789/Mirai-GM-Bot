package com.king.api.messageapis.group;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.king.config.ApplicationConfig;
import com.king.db.service.MessageCountService;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.ThreadPoolExecutorUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 记录所有message到Redis，保存时间24h，并用线程池向数据库修改计数
 *
 */
@Slf4j
@Component
public class MessageCountApi extends AbstractGroupMessageApi{

    @Autowired
    private MessageCountService messageCountService;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return true;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        String date = DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT);
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        long groupId = messageEvent.getSubject().getId();
        long senderId = messageEvent.getSender().getId();

        messageCountService.addCount(groupId, senderId, date);
        return null;
    }


    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "message.count";
    }
}
