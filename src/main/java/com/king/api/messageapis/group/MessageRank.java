package com.king.api.messageapis.group;

import com.king.component.MyBot;
import com.king.db.pojo.Members;
import com.king.db.pojo.MessageCount;
import com.king.db.service.MembersService;
import com.king.db.service.MessageCountService;
import com.king.model.CommonResponse;
import com.king.model.MessageEventContext;
import com.king.util.DateFormateUtil;
import com.king.util.GeneratePieHtmlUtil;
import com.king.util.GroupMembersUtil;
import com.king.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.NormalMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MessageRank extends AbstractGroupMessageApi{


    @Autowired
    private MessageCountService messageCountService;

    @Autowired
    private MembersService membersService;

    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return "#messagerank".equals(messageEventContext.getContent());
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        sendRank(messageEventContext.getMessageEvent().getSubject().getId());
        return null;
    }

    public void sendRank(Long groupId){
        sendRank(groupId, new Date());
    }

    public void sendRank(Long groupId, Date date){
        Group group = MyBot.bot.getGroup(groupId);
        if (group==null){
            return;
        }
        String yyyymmdd = DateFormateUtil.formatYYYYMMDD(date);
        List<MessageCount> messageCountList = messageCountService.getByGroupIdAndDate(groupId, yyyymmdd);
        List<MessageCount> result = messageCountList.stream().sorted(Comparator.comparing(MessageCount::getCount, Comparator.nullsLast(Comparator.reverseOrder()))).collect(Collectors.toList());
        long total = 0;
        for(MessageCount messageCount:result){
            total+=messageCount.getCount();
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        decimalFormat.setMinimumIntegerDigits(1);
        String title = yyyymmdd+"日发言榜";
        String subTitle = "总计"+total+"(单位:条)";
        Map<String, String> content = new TreeMap<>();

        int rankSize = Math.min(result.size(), 20);
        long subCount = 0;
        for (int i=0;i<rankSize;){
            MessageCount messageCount = result.get(i);
            Members members = membersService.selectMember(groupId, messageCount.getSubContactId());
            String nameCard,nickName,specialTitle;
            if (members==null){
                NormalMember normalMember = group.get(messageCount.getSubContactId());
                if (normalMember==null){
                    nameCard = String.valueOf(messageCount.getSubContactId());
                    nickName = "";
                    specialTitle = "";
                }else {
                    nameCard = normalMember.getNameCard();
                    nickName = normalMember.getNick();
                    specialTitle = normalMember.getSpecialTitle();
                }
            }else {
                nameCard = members.getNameCard();
                nickName = members.getNickName();
                specialTitle = members.getSpecialTitle();
            }
            subCount+=messageCount.getCount();
            String percent = decimalFormat.format(messageCount.getCount() * 100.0 / total);

            StringBuilder name = new StringBuilder();
            name.append(MyStringUtil.getTwoStr(i+1)).append(". ").append(nameCard)
                    .append("(").append(nickName).append(")")
                    .append("《").append(specialTitle).append("》")
                    .append(" ").append(messageCount.getCount()).append(" 条");
            content.put(name.toString(), percent);
            i++;
        }
        long remain = total-subCount;
        String remainPercent = decimalFormat.format(remain * 100.0 / total);
        content.put("其TA人 "+remain+" 条", remainPercent);
        File pieImg = GeneratePieHtmlUtil.generatePieImg(content, title, subTitle);
        GroupMembersUtil.sendImgFromString(group, pieImg, null);
    }



    @Override
    public int sortedOrder() {
        return 99;
    }

    @Override
    public String commandName() {
        return "member.message.rank";
    }
}
