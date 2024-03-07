package com.king.db.service;


import com.king.db.pojo.OpenAIApiKey;
import com.king.db.pojo.OpenAIApiKeyExample;
import com.king.db.subdao.SubOpenAIApiKeyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OpenAIApiKeyService {

    @Autowired
    private SubOpenAIApiKeyMapper subOpenAIApiKeyMapper;

    public List<String> getKeys(){
        OpenAIApiKeyExample example = new OpenAIApiKeyExample();
//        OpenAIApiKeyExample.Criteria criteria = example.createCriteria();
//        criteria.andKeyIsNotNull();
        List<OpenAIApiKey> openAIApiKeys = subOpenAIApiKeyMapper.selectByExample(example);
        List<String> keys = new ArrayList<>();
        for (OpenAIApiKey openAIApiKey: openAIApiKeys){
            keys.add(openAIApiKey.getApiKey());
        }
        return keys;
    }

}