package com.king.db.subdao;

import com.king.db.dao.SensitiveWordMapper;
import com.king.db.pojo.SensitiveWord;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Component
@Mapper
public interface SubSensitiveWordMapper extends SensitiveWordMapper {

    @Select({
            "select",
            "id, mseeage, lable, ctime, utime, ext_data",
            "from sensitive_word",
            "where 1=1"
    })
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="mseeage", property="mseeage", jdbcType=JdbcType.VARCHAR),
            @Result(column="lable", property="lable", jdbcType=JdbcType.INTEGER),
            @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="ext_data", property="extData", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<SensitiveWord> getAll();
    
}