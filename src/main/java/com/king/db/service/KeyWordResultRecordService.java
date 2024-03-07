package com.king.db.service;


import com.king.db.pojo.KeyWordResultRecord;
import com.king.db.pojo.KeyWordResultRecordExample;
import com.king.db.subdao.SubKeyWordResultRecordMapper;
import com.king.util.MD5Util;
import com.king.util.MyStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
public class KeyWordResultRecordService {

    @Autowired
    private SubKeyWordResultRecordMapper subKeyWordResultRecordMapper;


    public KeyWordResultRecord selectByMd5(String md5){
        KeyWordResultRecordExample example = new KeyWordResultRecordExample();
        KeyWordResultRecordExample.Criteria criteria = example.createCriteria();
        criteria.andMd5EqualTo(md5);
        List<KeyWordResultRecord> keyWordResultRecords = subKeyWordResultRecordMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(keyWordResultRecords)){
            return null;
        }else {
            return keyWordResultRecords.get(0);
        }
    }

    public boolean add(String type, String content){
        String md5 = MD5Util.md5(content);
        if (MyStringUtil.isEmpty(md5)){
            return false;
        }
        KeyWordResultRecord keyWordResultRecord = selectByMd5(md5);
        if (keyWordResultRecord==null|| !Objects.equals(keyWordResultRecord.getType(), type)){
            KeyWordResultRecord newKeyWordRecord = new KeyWordResultRecord();
            newKeyWordRecord.setType(type);
            newKeyWordRecord.setMd5(md5);
            newKeyWordRecord.setContent(content);
            newKeyWordRecord.setCtime(new Date());
            newKeyWordRecord.setEnable(true);
            subKeyWordResultRecordMapper.insert(newKeyWordRecord);
            return true;
        }
        return false;
    }

    public String randomGetOne(String type){
        KeyWordResultRecordExample example = new KeyWordResultRecordExample();
        KeyWordResultRecordExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(type);
        List<KeyWordResultRecord> keyWordResultRecords = subKeyWordResultRecordMapper.selectByExample(example);
        String res = "";
        if (!CollectionUtils.isEmpty(keyWordResultRecords)){
            Random random = new Random();
            int i = random.nextInt(keyWordResultRecords.size());
            res = keyWordResultRecords.get(i).getContent();
        }
        log.info("sql {} result: {}", type, res);
        return res;
    }


}