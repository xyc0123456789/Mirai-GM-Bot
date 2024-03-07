package com.king.api.messageapis.group;

import com.king.config.CommonConfig;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.*;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class ExportMembers extends AbstractGroupMessageApi {
    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        return "#导出群成员".equals(content)||"＃导出群成员".equals(content);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        try {
            MessageEvent messageEvent = messageEventContext.getMessageEvent();
            Group group = (Group) messageEvent.getSubject();
            QuoteReply quoteReply = messageEventContext.getQuoteReply();
            MessageChain messages = new MessageChainBuilder().append(quoteReply).append("正在导出，请稍后...").build();
            messageEvent.getSubject().sendMessage(messages);
            File file=null;
            try {
                ExcelData excelData = GroupMembersUtil.getMembers(group);
                file = ExcelUtils.generateExcel(excelData, CommonConfig.getWorkingDir() + "excel/" + group.getId()+"-"+ DateFormateUtil.getCurYYYYMMDDTHHMMSS() + ".xlsx");
            }catch (Exception e){
                log.error("生成excel失败", e);
            }
            if (file!=null && file.exists()){
                group.getFiles().uploadNewFile(file.getName(), ExternalResource.create(file).toAutoCloseable());
            }else {
                messages = new MessageChainBuilder().append(quoteReply).append("导出失败，请联系管理员").build();
                group.sendMessage(messages);
            }
        }catch (Exception e){
            log.error("", e);
        }
        return null;
    }

    @Override
    public int sortedOrder() {
        return 11;
    }

    @Override
    public String commandName() {
        return "member.export";
    }
}
