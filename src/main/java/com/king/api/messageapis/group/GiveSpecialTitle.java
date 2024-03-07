package com.king.api.messageapis.group;

import com.king.db.pojo.Members;
import com.king.db.service.MembersService;
import com.king.model.CommonResponse;
import com.king.model.GroupId;
import com.king.model.MessageEventContext;
import com.king.util.MyStringUtil;
import com.king.util.NormalMemberUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.QuoteReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GiveSpecialTitle extends AbstractGroupMessageApi {

    @Autowired
    private MembersService membersService;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        if (!content.startsWith("#givetitle")){
            return false;
        }

        return isManagerOrMe(messageEventContext);
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        String content = messageEventContext.getContent();
        content = content.substring(10);
        Group group = (Group) messageEventContext.getMessageEvent().getSubject();
        QuoteReply quoteReply = MessageSource.quote(messageEventContext.getMessageEvent().getMessage());
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder().append(quoteReply);
        if (MyStringUtil.isEmpty(content)){
            group.sendMessage(messageChainBuilder.append("请输入参数qq_id或者“all”,例如#givetitle 123、#givetitle all").build());
            return null;
        }
        if ("all".equalsIgnoreCase(content.trim())){
            boolean flag = true;
            for (NormalMember normalMember:group.getMembers()){
                String specialTitle = getSpecialTitle(normalMember.getId());
                if (group.getBotPermission().getLevel()==2){
                    try {
                        if (!MyStringUtil.isEmpty(specialTitle)&&MyStringUtil.isEmpty(normalMember.getSpecialTitle())){
                            log.info("set {} specialTitle:{}", NormalMemberUtil.getNormalMemberStr(normalMember), specialTitle);
                            normalMember.setSpecialTitle(specialTitle);
                        }
                    }catch (Exception e){
                        log.error("normalMember.setSpecialTitle err", e);
                        group.sendMessage(messageChainBuilder.append("normalMember.setSpecialTitle err").build());
                        flag=false;
                        break;
                    }
                }else {
                    flag=false;
                    break;
                }
            }
            if (flag){
                group.sendMessage(messageChainBuilder.append("success").build());
            }
        }else {
            long id;
            try {
                id = Long.parseLong(content.trim());
            }catch (Exception e){
                group.sendMessage(messageChainBuilder.append("qq_id格式错误").build());
                return null;
            }

            String specialTitle = getSpecialTitle(id);
            NormalMember normalMember = group.get(id);
            if (!MyStringUtil.isEmpty(specialTitle)&&normalMember!=null&&MyStringUtil.isEmpty(normalMember.getSpecialTitle())){
                if (group.getBotPermission().getLevel()==2) {
                    try {
                        log.info("set {} specialTitle:{}", NormalMemberUtil.getNormalMemberStr(normalMember), specialTitle);
                        normalMember.setSpecialTitle(specialTitle);
                        group.sendMessage(messageChainBuilder.append("set ").append(specialTitle).append(" success").build());
                    } catch (Exception e) {
                        log.error("normalMember.setSpecialTitle err", e);
                        group.sendMessage(messageChainBuilder.append(specialTitle).build());
                    }
                } else {
                    group.sendMessage(messageChainBuilder.append(specialTitle).build());
                }
            }else {
                if (MyStringUtil.isEmpty(specialTitle)){
                    group.sendMessage(messageChainBuilder.append("查询失败").build());
                }else {
                    group.sendMessage(messageChainBuilder.append(specialTitle).build());
                }
            }
        }
        return null;
    }

    private String getSpecialTitle(long qq_id){
        Members members = membersService.selectMember(GroupId.GROUP985, qq_id);
        if (members==null){
            members = membersService.selectMember(GroupId.Group985_2, qq_id);
            if (members==null) {
                return "";
            }
        }
        return members.getSpecialTitle();
    }


    @Override
    public int sortedOrder() {
        return 98;
    }

    @Override
    public String commandName() {
        return "give.special.title";
    }

    @Override
    public boolean defaultStatus() {
        return true;
    }
}
