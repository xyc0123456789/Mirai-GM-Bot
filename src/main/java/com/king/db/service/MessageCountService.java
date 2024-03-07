package com.king.db.service;


import com.king.db.pojo.MessageCount;
import com.king.db.subdao.SubMessageCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class MessageCountService {

    @Autowired
    private SubMessageCountMapper subMessageCountMapper;


    public MessageCount selectGroupPerson(Long groupId,Long qq_id,String date){
        return subMessageCountMapper.selectByPrimaryKey(1,groupId,qq_id,date);
    }

    public MessageCount insertGroupPerson(Long groupId,Long qq_id,String date){
        MessageCount messageCount = new MessageCount();
        messageCount.setContactType(1);
        messageCount.setContactId(groupId);
        messageCount.setSubContactId(qq_id);
        messageCount.setDate(date);
        messageCount.setCount(0L);
        messageCount.setCtime(new Date());
        messageCount.setEnable(true);
        subMessageCountMapper.insert(messageCount);
        return messageCount;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public MessageCount addCount(Long groupId, Long qq_id, String date){
        MessageCount messageCount = selectGroupPerson(groupId, qq_id, date);
        if (messageCount==null){
            messageCount = insertGroupPerson(groupId, qq_id, date);
        }
        long count = messageCount.getCount()+1;
        messageCount.setCount(count);
        subMessageCountMapper.updateByPrimaryKey(messageCount);
        return messageCount;
    }

    public List<MessageCount> getByGroupIdAndDate(Long groupId, String date){
        return subMessageCountMapper.selectByGroupIdAndDate(1,groupId,date);
    }

}