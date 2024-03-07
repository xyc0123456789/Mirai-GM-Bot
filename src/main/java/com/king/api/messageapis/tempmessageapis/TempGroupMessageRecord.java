package com.king.api.messageapis.tempmessageapis;

import com.king.db.service.TempMessageRecordService;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/9 17:18
 **/
@Slf4j
@Component
public class TempGroupMessageRecord extends AbstractTempGroupMessageApi{

    @Autowired
    private TempMessageRecordService tempMessageRecordService;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return true;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        tempMessageRecordService.add(messageEventContext);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "temp.group.message.record.all";
    }
}
