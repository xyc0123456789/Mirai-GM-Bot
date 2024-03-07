package com.king.db.subdao;

import com.king.db.dao.OpenAIApiKeyMapper;
import org.springframework.stereotype.Component;
import org.apache.ibatis.annotations.Mapper;

@Component
@Mapper
public interface SubOpenAIApiKeyMapper extends OpenAIApiKeyMapper {
    
}