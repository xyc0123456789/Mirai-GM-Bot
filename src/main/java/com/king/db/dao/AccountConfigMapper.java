package com.king.db.dao;

import com.king.db.pojo.AccountConfig;
import com.king.db.pojo.AccountConfigExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

@Mapper
public interface AccountConfigMapper {
    @Delete({
        "delete from account_config",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into account_config (id, account, ",
        "password, heart_beat_strategy, ",
        "protocol, working_dir, ",
        "cache_dir_name, device_info_file_name, ",
        "sensitive_word_dir_name, ctime, ",
        "utime, enable)",
        "values (#{id,jdbcType=INTEGER}, #{account,jdbcType=BIGINT}, ",
        "#{password,jdbcType=VARCHAR}, #{heartBeatStrategy,jdbcType=VARCHAR}, ",
        "#{protocol,jdbcType=VARCHAR}, #{workingDir,jdbcType=VARCHAR}, ",
        "#{cacheDirName,jdbcType=VARCHAR}, #{deviceInfoFileName,jdbcType=VARCHAR}, ",
        "#{sensitiveWordDirName,jdbcType=VARCHAR}, #{ctime,jdbcType=TIMESTAMP}, ",
        "#{utime,jdbcType=TIMESTAMP}, #{enable,jdbcType=BIT})"
    })
    int insert(AccountConfig record);

    @InsertProvider(type=AccountConfigSqlProvider.class, method="insertSelective")
    int insertSelective(AccountConfig record);

    @SelectProvider(type=AccountConfigSqlProvider.class, method="selectByExample")
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
    List<AccountConfig> selectByExample(AccountConfigExample example);

    @Select({
        "select",
        "id, account, password, heart_beat_strategy, protocol, working_dir, cache_dir_name, ",
        "device_info_file_name, sensitive_word_dir_name, ctime, utime, enable",
        "from account_config",
        "where id = #{id,jdbcType=INTEGER}"
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
    AccountConfig selectByPrimaryKey(Integer id);

    @UpdateProvider(type=AccountConfigSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(AccountConfig record);

    @Update({
        "update account_config",
        "set account = #{account,jdbcType=BIGINT},",
          "password = #{password,jdbcType=VARCHAR},",
          "heart_beat_strategy = #{heartBeatStrategy,jdbcType=VARCHAR},",
          "protocol = #{protocol,jdbcType=VARCHAR},",
          "working_dir = #{workingDir,jdbcType=VARCHAR},",
          "cache_dir_name = #{cacheDirName,jdbcType=VARCHAR},",
          "device_info_file_name = #{deviceInfoFileName,jdbcType=VARCHAR},",
          "sensitive_word_dir_name = #{sensitiveWordDirName,jdbcType=VARCHAR},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP},",
          "enable = #{enable,jdbcType=BIT}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(AccountConfig record);
}