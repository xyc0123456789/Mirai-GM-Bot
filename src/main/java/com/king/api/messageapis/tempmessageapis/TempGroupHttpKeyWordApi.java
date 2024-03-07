package com.king.api.messageapis.tempmessageapis;

import com.king.api.messageapis.common.HttpKeyWordApi;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/9 13:46
 **/
@Slf4j
@Component
public class TempGroupHttpKeyWordApi extends AbstractTempGroupMessageApi{

    @Autowired
    private HttpKeyWordApi httpKeyWordApi;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return httpKeyWordApi.condition(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        httpKeyWordApi.handler(messageEventContext);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 97;
    }

    @Override
    public String commandName() {
        return "temp.group.keyword";
    }
}
