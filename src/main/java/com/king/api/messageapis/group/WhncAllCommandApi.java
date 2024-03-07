package com.king.api.messageapis.group;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.YouPlaintMeGuessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: 我画你猜命令
 * @author: xyc0123456789
 * @create: 2023/5/10 22:00
 **/
@Slf4j
@Component
public class WhncAllCommandApi extends AbstractGroupMessageApi{
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return true;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        YouPlaintMeGuessUtil.put(messageEventContext);
        YouPlaintMeGuessUtil.guess(messageEventContext);
        YouPlaintMeGuessUtil.stop(messageEventContext);
        YouPlaintMeGuessUtil.status(messageEventContext);
        return null;
    }

    @Override
    public int sortedOrder() {
        return 68;
    }

    @Override
    public String commandName() {
        return "group.whnc.all";
    }
}
