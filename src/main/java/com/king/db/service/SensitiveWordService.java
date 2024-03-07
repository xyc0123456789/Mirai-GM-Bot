package com.king.db.service;


import com.king.db.pojo.SensitiveWord;
import com.king.db.subdao.SubSensitiveWordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SensitiveWordService {

    @Autowired
    private SubSensitiveWordMapper subSensitiveWordMapper;

    public List<SensitiveWord> getAll(){
        return subSensitiveWordMapper.getAll();
    }

    public void deleteByWord(Long id){
        subSensitiveWordMapper.deleteByPrimaryKey(id);
    }

    public Long addOneWord(String word){
        return (long) subSensitiveWordMapper.insertSelective(new SensitiveWord(null, word, null, new Date(), null, null));
    }

}