package com.king.model;

import lombok.Data;
import net.mamoe.mirai.event.events.MessageRecallEvent;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/9 15:36
 **/
@Data
public class MessageRecallEventContext extends CommonEventContext {
    private MessageRecallEvent messageRecallEvent;
    private boolean isGroup = true;
}
