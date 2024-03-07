package com.king.api.messageapis.group;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.king.api.recallapis.GroupMessageRecallConstName.GroupMessageRecallResendMe;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/9 15:19
 **/
@Slf4j
@Component
public class GroupMessageRecallResend extends AbstractGroupMessageApi{
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return false;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        return null;
    }

    @Override
    public int sortedOrder() {
        return 0;
    }

    @Override
    public String commandName() {
        return GroupMessageRecallResendMe;
    }

    @Override
    public boolean defaultStatus() {
        return false;
    }
}
