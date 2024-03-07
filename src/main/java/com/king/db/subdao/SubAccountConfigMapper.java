package com.king.db.subdao;

import com.king.db.dao.AccountConfigMapper;
import com.king.db.pojo.AccountConfig;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Component
@Mapper
public interface SubAccountConfigMapper extends AccountConfigMapper {


    @Select({
            "select",
            "*",
            "from account_config",
            "where enable = 1 order by id desc"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="account", property="account", jdbcType=JdbcType.BIGINT),
            @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
            @Result(column="heart_beat_strategy", property="heartBeatStrategy", jdbcType=JdbcType.VARCHAR),
            @Result(column="protocol", property="protocol", jdbcType=JdbcType.VARCHAR),
            @Result(column="working_dir", property="workingDir", jdbcType=JdbcType.VARCHAR),
            @Result(column="cache_dir_name", property="cacheDirName", jdbcType=JdbcType.VARCHAR),
            @Result(column="device_info_file_name", property="deviceInfoFileName", jdbcType=JdbcType.VARCHAR),
            @Result(column="sensitive_word_dir_name", property="sensitiveWordDirName", jdbcType=JdbcType.VARCHAR),
            @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="enable", property="enable", jdbcType=JdbcType.BIT)
    })
    List<AccountConfig> getOne();
}