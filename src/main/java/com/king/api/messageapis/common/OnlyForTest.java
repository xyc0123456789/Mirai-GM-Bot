package com.king.api.messageapis.common;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import net.mamoe.mirai.message.data.XmlMessageBuilder;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/20 22:06
 **/
public class OnlyForTest extends AbstractCommonMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return messageEventContext.getContent().startsWith("test");
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        XmlMessageBuilder xmlMessageBuilder = new XmlMessageBuilder();

        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "only.for.test";
    }

    @Override
    public boolean defaultStatus() {
        return false;
    }
}
