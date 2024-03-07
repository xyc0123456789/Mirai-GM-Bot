package com.king.api.messageapis.group;

import com.king.config.CommonConfig;
import com.king.db.pojo.Members;
import com.king.db.service.MembersService;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.DateFormateUtil;
import com.king.util.ExcelData;
import com.king.util.ExcelUtils;
import com.king.util.GroupMembersUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Slf4j
@Component
public class ExportMembersQuick extends AbstractGroupMessageApi {

    @Autowired
    private MembersService membersService;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        return "#exportmembers".equals(content)||"＃exportmembers".equals(content);
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
                List<Members> groupList = membersService.getGroupList(group.getId());
                ExcelData excelData = GroupMembersUtil.getMembers(groupList);
                file = ExcelUtils.generateExcel(excelData, CommonConfig.getWorkingDir() + "excel/" + group.getId()+"-db-"+ DateFormateUtil.getCurYYYYMMDDTHHMMSS() + ".xlsx");
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
        return "member.export.sql";
    }
}
