package com.king.api.messageapis.tempmessageapis;

import com.king.api.messageapis.common.NewBingRequestResetApi;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 临时会话bing reset
 * @author: xyc0123456789
 * @create: 2023/3/9 14:15
 **/
@Slf4j
@Component
public class TempNewBingResetRequest extends AbstractTempGroupMessageApi {

    @Autowired
    private NewBingRequestResetApi newBingRequestResetApi;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getContent().length() > 8 && "#bingreset".equalsIgnoreCase(messageEventContext.getContent());
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        newBingRequestResetApi.handler(messageEventContext);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "temp.group.bing.request.reset";
    }
}
