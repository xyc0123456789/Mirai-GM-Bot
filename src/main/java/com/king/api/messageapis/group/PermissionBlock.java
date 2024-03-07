package com.king.api.messageapis.group;

import com.king.db.pojo.CommandPermission;
import com.king.db.pojo.GroupSpecialList;
import com.king.db.service.CommandPermissionService;
import com.king.db.service.GroupSpecialListService;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/2 17:26
 **/
@Slf4j
@Component
public class PermissionBlock extends AbstractGroupMessageApi{

    @Autowired
    @Lazy
    private CommandPermissionService commandPermissionService;

    @Autowired
    private GroupSpecialListService groupSpecialListService;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        if (!messageEventContext.getContent().trim().startsWith("#permissionblock")){
            return false;
        }
        return isManagerOrMe(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        QuoteReply quoteReply = messageEventContext.getQuoteReply();
        String content = messageEventContext.getContent().trim().substring(16);
        String[] splitIds = content.split(" ");
        ArrayList<At> atList = messageEventContext.getAtList();
        Set<Long> ids = new HashSet<>();
        long groupId = messageEvent.getSubject().getId();
        for (String str: splitIds){
            if (MyStringUtil.isEmpty(str)){
                continue;
            }
            try {
                Long id = Long.parseLong(str);
                ids.add(id);
            }catch (Exception ignored){}
        }
        for (At at: atList){
            ids.add(at.getTarget());
        }

        List<Long> success = new ArrayList<>();
        for (Long id:ids){
            GroupSpecialList specialList = groupSpecialListService.getAndUpdateOrAddOne(groupId, id, 0L);
            if (specialList!=null){
                List<CommandPermission> list = commandPermissionService.getList(1, groupId, id);
                if (!CollectionUtils.isEmpty(list)){//update否则自动生成
                    for (CommandPermission commandPermission: list){
                        commandPermission.setIsOpen(false);
                    }
                    commandPermissionService.update(list);
                }
                success.add(id);
            }
        }
        commandPermissionService.reloadPermission(false);
        MessageChain messageChain = new MessageChainBuilder().append(quoteReply).append("#permissionblock success ids:").append(success.toString()).build();
        messageEvent.getSubject().sendMessage(messageChain);

        return null;
    }

    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "message.give.permission";
    }
}
