package com.king.util;

import com.king.component.MyBot;
import com.king.db.pojo.MessageRecord;
import com.king.db.service.MessageRecordService;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @description: 消息数据分析
 * @author: xyc0123456789
 * @create: 2023/3/10 12:32
 **/
@Slf4j
@Component
public class MessageAnalysisUtil {

    private static MessageRecordService messageRecordService;

    @Autowired
    public MessageAnalysisUtil(MessageRecordService messageRecordService){
        MessageAnalysisUtil.messageRecordService = messageRecordService;
    }

    public static void analyseGroup(Long groupId){
        analyseGroup(groupId, null, new Date());
    }

    public static void analyseGroup(Long groupId, Long subContactId,Date date){
        Group group = MyBot.bot.getGroup(groupId);
        Contact contact = group;
        if (group==null){
            return;
        }
        List<MessageRecord> groupTextByDate;
        if (subContactId!=null){
            contact = group.get(subContactId);
            groupTextByDate = messageRecordService.getByGroupAndIdWithDate(groupId, subContactId, date);
        }else {
            groupTextByDate = messageRecordService.getByGroupWithDate(groupId, date);
        }
        if (contact==null){
            return;
        }

        if (CollectionUtils.isEmpty(groupTextByDate)){
            return;
        }

        Map<Integer, List<MessageRecord>> total = new HashMap<>();
        for (MessageRecord messageRecord: groupTextByDate){
            String hour = messageRecord.getDateTime().substring(6, 8);
            int index = Integer.parseInt(hour);
            List<MessageRecord> tmpCollection = total.computeIfAbsent(index, k -> new ArrayList<>());
            tmpCollection.add(messageRecord);
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        decimalFormat.setMinimumIntegerDigits(1);
//        StringBuilder stringBuilder = new StringBuilder(DateFormateUtil.formatYYYYMMDD(date));
//        stringBuilder.append(" total:").append(groupTextByDate.size()).append("\\\n");
//        for (int i=0;i<24;i++){
//            List<MessageRecord> list = total.getOrDefault(i, new ArrayList<>());
//            String percent = decimalFormat.format(list.size() * 100.0 / groupTextByDate.size());
//            stringBuilder.append(MyStringUtil.getTwoStr(i)).append(" ~ ").append(MyStringUtil.getTwoStr(i+1)).append(": ").append(MyStringUtil.getFormationStrWithBlankHTML(list.size(), 7)).append("  ···  ").append(MyStringUtil.getFormationStrWithBlankHTML(percent,7)).append("%").append("\\\n");
//        }
        String title = DateFormateUtil.formatYYYYMMDD(date) + " 发言时段分析";
        String subTitle = "总条数:" + groupTextByDate.size();
        Map<String, String> content = new TreeMap<>();
        for (int i=0;i<24;i++){
            List<MessageRecord> list = total.getOrDefault(i, new ArrayList<>());
            String percent = decimalFormat.format(list.size() * 100.0 / groupTextByDate.size());
            content.put(MyStringUtil.getTwoStr(i)+" ~ "+MyStringUtil.getTwoStr(i+1)+" "+String.format("%7s", list.size())+" 条", percent);
        }
        File pieImg = GeneratePieHtmlUtil.generatePieImg(content, title, subTitle);
        GroupMembersUtil.sendImgFromString(group, pieImg, null);
    }

}
