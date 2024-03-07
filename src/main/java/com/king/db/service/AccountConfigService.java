package com.king.db.service;


import com.king.db.pojo.AccountConfig;
import com.king.db.subdao.SubAccountConfigMapper;
import com.king.util.JsonUtil;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class AccountConfigService {

    @Autowired
    private SubAccountConfigMapper subAccountConfigMapper;


    public AccountConfig getOne(){
        List<AccountConfig> one = subAccountConfigMapper.getOne();
        log.info(JsonUtil.toJson(one));
        if (!CollectionUtils.isEmpty(one)){
            return one.get(0);
        }
        return null;
    }

    public AccountConfig getById(int id){
        return subAccountConfigMapper.selectByPrimaryKey(id);
    }

}