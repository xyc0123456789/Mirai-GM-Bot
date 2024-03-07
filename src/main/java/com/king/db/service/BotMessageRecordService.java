package com.king.db.service;


import com.king.db.pojo.BotMessageRecord;
import com.king.db.subdao.SubBotMessageRecordMapper;
import com.king.model.MessageEventContext;
import com.king.util.DateFormateUtil;
import com.king.util.JsonUtil;
import com.king.util.MyStringUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.OnlineAudio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class BotMessageRecordService {

    @Autowired
    private SubBotMessageRecordMapper subBotMessageRecordMapper;

    private static final AtomicInteger suffixIdex = new AtomicInteger(0);

    private static int getLastId(){
        int andAdd = suffixIdex.getAndAdd(1);
        if (andAdd>1000000){
            suffixIdex.getAndSet(0);
            return suffixIdex.getAndAdd(1);
        }
        return andAdd;
    }

    private static Long getsubId(Date date){
        String format =  DateFormateUtil.formatYYMMDDHHMMSSOnlyNum(date) + "%06d";
        String ans = String.format(format, getLastId());
        return Long.parseLong(ans);
    }

    public void save(MessageEventContext messageEventContext,String botName, String request, String answer){
        if (MyStringUtil.isEmpty(request)||MyStringUtil.isEmpty(answer)){
            return;
        }

        BotMessageRecord messageRecord = new BotMessageRecord();
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        Contact subject = messageEvent.getSubject();
        User sender = messageEvent.getSender();
        Date date = new Date();
        messageRecord.setGroupId(subject.getId());
        messageRecord.setQqId(sender.getId());
        messageRecord.setMessageMiraiCode(messageEvent.getMessage().serializeToMiraiCode());

        messageRecord.setBotName(botName);
        messageRecord.setQuery(request);
        messageRecord.setAnswer(answer);

        messageRecord.setSubId(getsubId(date));
        messageRecord.setDate(DateFormateUtil.formatYYYYMMDD(date));
        messageRecord.setDateTime(DateFormateUtil.formatYYMMDDHHMMSSOnlyNum(date));

        int[] ids = messageEvent.getSource().getIds();
        if (ids.length == 1){
            messageRecord.setSingleId(ids[0]);
        }else {
            messageRecord.setSingleId(null);
        }

        messageRecord.setIds(JsonUtil.toJson(ids));
        messageRecord.setCtime(date);
        messageRecord.setUtime(date);
        messageRecord.setEnable(true);
        subBotMessageRecordMapper.insert(messageRecord);

    }


}