package com.king.api.messageapis.common;

import com.king.api.messageapis.common.AbstractCommonMessageApi;
import com.king.db.service.MessageRecordService;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 记录所有message到数据库
 *
 */
@Slf4j
@Component
public class MessageRecordApi extends AbstractCommonMessageApi {

    @Autowired
    private MessageRecordService messageRecordService;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return true;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        messageRecordService.add(messageEventContext);
        return null;
    }




    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "message.record.all";
    }
}
