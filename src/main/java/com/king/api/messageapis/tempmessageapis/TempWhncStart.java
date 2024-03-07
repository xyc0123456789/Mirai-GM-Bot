package com.king.api.messageapis.tempmessageapis;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.YouPlaintMeGuessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: 我画你猜命令
 * @author: xyc0123456789
 * @create: 2023/5/10 21:58
 **/
@Slf4j
@Component
public class TempWhncStart extends AbstractTempGroupMessageApi{
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return YouPlaintMeGuessUtil.status(messageEventContext)||YouPlaintMeGuessUtil.put(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        return null;
    }

    @Override
    public int sortedOrder() {
        return 97;
    }

    @Override
    public String commandName() {
        return "temp.group.whnc";
    }
}
