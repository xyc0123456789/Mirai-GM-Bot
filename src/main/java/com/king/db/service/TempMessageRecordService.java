package com.king.db.service;


import com.king.db.pojo.TempMessageRecord;
import com.king.db.pojo.TempMessageRecordExample;
import com.king.db.subdao.SubTempMessageRecordMapper;
import com.king.model.MessageEventContext;
import com.king.util.DateFormateUtil;
import com.king.util.JsonUtil;
import com.king.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.GroupTempMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.OnlineAudio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class TempMessageRecordService {

    @Autowired
    private SubTempMessageRecordMapper subTempMessageRecordMapper;


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

    public void add(MessageEventContext messageEventContext){
        TempMessageRecord messageRecord = new TempMessageRecord();
        GroupTempMessageEvent messageEvent = (GroupTempMessageEvent) messageEventContext.getMessageEvent();
        NormalMember normalMember = messageEvent.getSubject();
        User sender = messageEvent.getSender();
        Date date = new Date();

        messageRecord.setGroupId(normalMember.getGroup().getId());
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
        subTempMessageRecordMapper.insert(messageRecord);
    }

    public List<TempMessageRecord> getByGroupAndIdWithDate(Long groupId, Long qqId, Date date){
        TempMessageRecordExample messageRecordExample = new TempMessageRecordExample();
        TempMessageRecordExample.Criteria criteria = messageRecordExample.createCriteria();
        criteria.andGroupIdEqualTo(groupId);
        criteria.andQqIdEqualTo(qqId);
        criteria.andDateEqualTo(DateFormateUtil.formatYYYYMMDD(date));
        return subTempMessageRecordMapper.selectByExample(messageRecordExample);
    }

    public List<TempMessageRecord> getByGroupWithDate(Long groupId,Date date){
        TempMessageRecordExample messageRecordExample = new TempMessageRecordExample();
        TempMessageRecordExample.Criteria criteria = messageRecordExample.createCriteria();
        criteria.andGroupIdEqualTo(groupId);
        criteria.andDateEqualTo(DateFormateUtil.formatYYYYMMDD(date));
        return subTempMessageRecordMapper.selectByExample(messageRecordExample);
    }

    public List<String> getMemberTextByDate(Long groupId,Long qqId,Date date){
        List<TempMessageRecord> messageRecords = getByGroupAndIdWithDate(groupId, qqId, date);
        ArrayList<String> arrayList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(messageRecords)){
            for (TempMessageRecord messageRecord: messageRecords){
                String textContent = messageRecord.getTextContent();
                if (!MyStringUtil.isEmpty(textContent)) {
                    arrayList.add(textContent);
                }
            }
        }
        return arrayList;
    }

    public List<String> getGroupTextByDate(Long groupId,Date date){
        List<TempMessageRecord> messageRecords = getByGroupWithDate(groupId, date);
        ArrayList<String> arrayList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(messageRecords)){
            for (TempMessageRecord messageRecord: messageRecords){
                String textContent = messageRecord.getTextContent();
                if (!MyStringUtil.isEmpty(textContent)) {
                    arrayList.add(textContent);
                }
            }
        }
        return arrayList;
    }

    public TempMessageRecord getMessageById(int id){
        TempMessageRecordExample messageRecordExample = new TempMessageRecordExample();
        TempMessageRecordExample.Criteria criteria = messageRecordExample.createCriteria();
        criteria.andSingleIdEqualTo(id);
        List<TempMessageRecord> messageRecords = subTempMessageRecordMapper.selectByExample(messageRecordExample);
        if (CollectionUtils.isEmpty(messageRecords)){
            return null;
        }
        return messageRecords.get(0);
    }

}