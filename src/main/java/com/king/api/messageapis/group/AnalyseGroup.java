package com.king.api.messageapis.group;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.MessageAnalysisUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import org.springframework.stereotype.Component;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/10 13:39
 **/
@Slf4j
@Component
public class AnalyseGroup extends AbstractGroupMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return "#analysegroup".equalsIgnoreCase(messageEventContext.getContent());
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        long groupId = messageEvent.getSubject().getId();
        MessageAnalysisUtil.analyseGroup(groupId);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 97;
    }

    @Override
    public String commandName() {
        return "message.analyse.group";
    }
}
