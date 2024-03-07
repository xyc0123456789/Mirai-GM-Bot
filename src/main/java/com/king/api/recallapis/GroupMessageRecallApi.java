package com.king.api.recallapis;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.model.MessageRecallEventContext;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/9 15:26
 **/
public interface GroupMessageRecallApi {

    boolean condition(MessageRecallEventContext messageRecallEventContext);

    CommonResponse handler(MessageRecallEventContext messageRecallEventContext);

    int sortedOrder();

    String commandName();

    boolean defaultStatus();//默认是否开启

}
