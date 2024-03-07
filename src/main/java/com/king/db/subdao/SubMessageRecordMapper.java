package com.king.db.subdao;

import com.king.db.dao.MessageRecordMapper;
import org.springframework.stereotype.Component;
import org.apache.ibatis.annotations.Mapper;

@Component
@Mapper
public interface SubMessageRecordMapper extends MessageRecordMapper {
    
}