package com.king.db.service;


import com.king.db.pojo.MessageRecord;
import com.king.db.pojo.MessageRecordExample;
import com.king.db.subdao.SubMessageRecordMapper;
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

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class MessageRecordService {

    private static final AtomicInteger suffixIdex = new AtomicInteger(0);

    private static int getLastId(){
        int andAdd = suffixIdex.getAndAdd(1);
        if (andAdd>=1000000){
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

    private static Long getDeltaSubId(Date date, int seconds){
        Date offSecond = DateFormateUtil.offSecond(date, seconds);
        String ans =  DateFormateUtil.formatYYMMDDHHMMSSOnlyNum(offSecond) + "000000";
        return Long.parseLong(ans);
    }

    @Autowired
    private SubMessageRecordMapper subMessageRecordMapper;

    public void add(MessageEventContext messageEventContext){
        MessageRecord messageRecord = new MessageRecord();
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        Contact subject = messageEvent.getSubject();
        User sender = messageEvent.getSender();
        Date date = new Date();

        messageRecord.setGroupId(subject.getId());
        messageRecord.setQqId(sender.getId());
        messageRecord.setMessageMiraiCode(messageEvent.getMessage().serializeToMiraiCode());
        if (!CollectionUtils.isEmpty(messageEventContext.getImages())){
            Map<String,String> imgMap = new HashMap<>();
            for (Image image: messageEventContext.getImages()){
                String queryUrl = Image.queryUrl(image);
                imgMap.put(image.getImageId(),queryUrl);
            }
            messageRecord.setImageUrlList(JsonUtil.toJson(imgMap));
        }

        OnlineAudio onlineAudio = messageEventContext.getOnlineAudio();
        if (onlineAudio !=null){
            messageRecord.setAudioUrl(onlineAudio.getUrlForDownload());
        }
        messageRecord.setTextContent(messageEventContext.getContent());
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
        subMessageRecordMapper.insert(messageRecord);
    }

    public List<MessageRecord> getByGroupAndIdWithDate(Long groupId,Long qqId,Date date){
        MessageRecordExample messageRecordExample = new MessageRecordExample();
        MessageRecordExample.Criteria criteria = messageRecordExample.createCriteria();
        criteria.andGroupIdEqualTo(groupId);
        criteria.andQqIdEqualTo(qqId);
        criteria.andDateEqualTo(DateFormateUtil.formatYYYYMMDD(date));
        return subMessageRecordMapper.selectByExample(messageRecordExample);
    }

    public List<MessageRecord> getByGroupWithDate(Long groupId,Date date){
        MessageRecordExample messageRecordExample = new MessageRecordExample();
        MessageRecordExample.Criteria criteria = messageRecordExample.createCriteria();
        criteria.andGroupIdEqualTo(groupId);
        criteria.andDateEqualTo(DateFormateUtil.formatYYYYMMDD(date));
        return subMessageRecordMapper.selectByExample(messageRecordExample);
    }

    public List<String> getMemberTextByDate(Long groupId,Long qqId,Date date){
        List<MessageRecord> messageRecords = getByGroupAndIdWithDate(groupId, qqId, date);
        ArrayList<String> arrayList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(messageRecords)){
            for (MessageRecord messageRecord: messageRecords){
                String textContent = messageRecord.getTextContent();
                if (!MyStringUtil.isEmpty(textContent)) {
                    arrayList.add(textContent);
                }
            }
        }
        return arrayList;
    }

    public List<String> getGroupTextByDate(Long groupId,Date date){
        List<MessageRecord> messageRecords = getByGroupWithDate(groupId, date);
        ArrayList<String> arrayList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(messageRecords)){
            for (MessageRecord messageRecord: messageRecords){
                String textContent = messageRecord.getTextContent();
                if (!MyStringUtil.isEmpty(textContent)) {
                    arrayList.add(textContent);
                }
            }
        }
        return arrayList;
    }

    public MessageRecord getMessageById(Long groupId, int id){
        MessageRecordExample messageRecordExample = new MessageRecordExample();
        MessageRecordExample.Criteria criteria = messageRecordExample.createCriteria();
        criteria.andSingleIdEqualTo(id);
        criteria.andGroupIdEqualTo(groupId);
        List<MessageRecord> messageRecords = subMessageRecordMapper.selectByExample(messageRecordExample);
        if (CollectionUtils.isEmpty(messageRecords)){
            return null;
        }
        return messageRecords.get(0);
    }

    public MessageRecord update(MessageRecord messageRecord){
        int i = subMessageRecordMapper.updateByPrimaryKeySelective(messageRecord);
        log.info("MessageRecordService update: {}", i);
        return messageRecord;
    }


    public List<MessageRecord> getOffsetMessageList(Long groupId, Long qqId, int secondsOffset){
        MessageRecordExample messageRecordExample = new MessageRecordExample();
        MessageRecordExample.Criteria criteria = messageRecordExample.createCriteria();
        criteria.andGroupIdEqualTo(groupId);
        if (qqId != null) {
            criteria.andQqIdEqualTo(qqId);
        }
        criteria.andSubIdGreaterThan(getDeltaSubId(null, secondsOffset));
        return subMessageRecordMapper.selectByExample(messageRecordExample);
    }

    public static void main(String[] args) {
        System.out.println(getDeltaSubId(null, -600));
    }
}