package com.king.api.messageapis.tempmessageapis;

import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.king.db.service.CommandPermissionService.*;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/13 9:39
 **/
@Slf4j
@Component
public class TempDefaultAnswerApi extends AbstractTempGroupMessageApi {

    @Autowired
    private TempMenu tempMenu;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return false;
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        String keyFromMessageDefault = getKeyFromMessageDefault(this.commandName(), messageEventContext);
        boolean permission = TempGroupMessagePermissionMap.getOrDefault(keyFromMessageDefault,false);
        if (permission){
            MessageEvent messageEvent = messageEventContext.getMessageEvent();
            String keyFromMessageForPerson = getKeyFromMessageForPerson(this.commandName(), messageEventContext);
            Boolean personPermission = TempGroupMessagePermissionMap.getOrDefault(keyFromMessageForPerson, true);
            if (personPermission){
                messageEvent.getSubject().sendMessage("命令匹配失败，请认真查看说明。证明请发送给群主或其他管理，谢谢配合~");
                tempMenu.handler(messageEventContext);
            }else {
                messageEvent.getSubject().sendMessage("你已被禁止使用bot的临时会话");
            }
        }



        return null;
    }

    @Override
    public int sortedOrder() {
        return 100;
    }

    @Override
    public String commandName() {
        return "temp.group.default.answer";
    }
}
