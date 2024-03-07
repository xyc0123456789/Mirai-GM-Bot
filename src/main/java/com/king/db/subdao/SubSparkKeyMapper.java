package com.king.db.subdao;

import com.king.db.dao.SparkKeyMapper;
import org.springframework.stereotype.Component;
import org.apache.ibatis.annotations.Mapper;

@Component
@Mapper
public interface SubSparkKeyMapper extends SparkKeyMapper {
    
}