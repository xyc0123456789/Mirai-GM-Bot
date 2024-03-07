package com.king.db.service;


import com.king.db.pojo.SparkKey;
import com.king.db.pojo.SparkKeyExample;
import com.king.db.subdao.SubSparkKeyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class SparkKeyService {

    @Autowired
    private SubSparkKeyMapper subSparkKeyMapper;

    public SparkKey getKey(){
        SparkKeyExample example = new SparkKeyExample();

        List<SparkKey> sparkKeys = subSparkKeyMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(sparkKeys)){
            return null;
        }
        return sparkKeys.get(0);
    }

}