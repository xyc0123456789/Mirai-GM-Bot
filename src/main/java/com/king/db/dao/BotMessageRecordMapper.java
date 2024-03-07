package com.king.db.dao;

import com.king.db.pojo.BotMessageRecord;
import com.king.db.pojo.BotMessageRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

@Mapper
public interface BotMessageRecordMapper {
    @Delete({
        "delete from bot_message_record",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and qq_id = #{qqId,jdbcType=BIGINT}",
          "and sub_id = #{subId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(@Param("groupId") Long groupId, @Param("qqId") Long qqId, @Param("subId") Long subId);

    @Insert({
        "insert into bot_message_record (group_id, qq_id, ",
        "sub_id, single_id, ",
        "ids, date, date_time, ",
        "message_mirai_code, bot_name, ",
        "ctime, utime, ",
        "enable, query, answer)",
        "values (#{groupId,jdbcType=BIGINT}, #{qqId,jdbcType=BIGINT}, ",
        "#{subId,jdbcType=BIGINT}, #{singleId,jdbcType=INTEGER}, ",
        "#{ids,jdbcType=VARCHAR}, #{date,jdbcType=VARCHAR}, #{dateTime,jdbcType=VARCHAR}, ",
        "#{messageMiraiCode,jdbcType=VARCHAR}, #{botName,jdbcType=VARCHAR}, ",
        "#{ctime,jdbcType=TIMESTAMP}, #{utime,jdbcType=TIMESTAMP}, ",
        "#{enable,jdbcType=BIT}, #{query,jdbcType=LONGVARCHAR}, #{answer,jdbcType=LONGVARCHAR})"
    })
    int insert(BotMessageRecord record);

    @InsertProvider(type=BotMessageRecordSqlProvider.class, method="insertSelective")
    int insertSelective(BotMessageRecord record);

    @SelectProvider(type=BotMessageRecordSqlProvider.class, method="selectByExampleWithBLOBs")
    @Results({
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="qq_id", property="qqId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="sub_id", property="subId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="single_id", property="singleId", jdbcType=JdbcType.INTEGER),
        @Result(column="ids", property="ids", jdbcType=JdbcType.VARCHAR),
        @Result(column="date", property="date", jdbcType=JdbcType.VARCHAR),
        @Result(column="date_time", property="dateTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="message_mirai_code", property="messageMiraiCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="bot_name", property="botName", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT),
        @Result(column="query", property="query", jdbcType=JdbcType.LONGVARCHAR),
        @Result(column="answer", property="answer", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<BotMessageRecord> selectByExampleWithBLOBs(BotMessageRecordExample example);

    @SelectProvider(type=BotMessageRecordSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="qq_id", property="qqId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="sub_id", property="subId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="single_id", property="singleId", jdbcType=JdbcType.INTEGER),
        @Result(column="ids", property="ids", jdbcType=JdbcType.VARCHAR),
        @Result(column="date", property="date", jdbcType=JdbcType.VARCHAR),
        @Result(column="date_time", property="dateTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="message_mirai_code", property="messageMiraiCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="bot_name", property="botName", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT)
    })
    List<BotMessageRecord> selectByExample(BotMessageRecordExample example);

    @Select({
        "select",
        "group_id, qq_id, sub_id, single_id, ids, date, date_time, message_mirai_code, ",
        "bot_name, ctime, utime, enable, query, answer",
        "from bot_message_record",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and qq_id = #{qqId,jdbcType=BIGINT}",
          "and sub_id = #{subId,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="qq_id", property="qqId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="sub_id", property="subId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="single_id", property="singleId", jdbcType=JdbcType.INTEGER),
        @Result(column="ids", property="ids", jdbcType=JdbcType.VARCHAR),
        @Result(column="date", property="date", jdbcType=JdbcType.VARCHAR),
        @Result(column="date_time", property="dateTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="message_mirai_code", property="messageMiraiCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="bot_name", property="botName", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT),
        @Result(column="query", property="query", jdbcType=JdbcType.LONGVARCHAR),
        @Result(column="answer", property="answer", jdbcType=JdbcType.LONGVARCHAR)
    })
    BotMessageRecord selectByPrimaryKey(@Param("groupId") Long groupId, @Param("qqId") Long qqId, @Param("subId") Long subId);

    @UpdateProvider(type=BotMessageRecordSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(BotMessageRecord record);

    @Update({
        "update bot_message_record",
        "set single_id = #{singleId,jdbcType=INTEGER},",
          "ids = #{ids,jdbcType=VARCHAR},",
          "date = #{date,jdbcType=VARCHAR},",
          "date_time = #{dateTime,jdbcType=VARCHAR},",
          "message_mirai_code = #{messageMiraiCode,jdbcType=VARCHAR},",
          "bot_name = #{botName,jdbcType=VARCHAR},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP},",
          "enable = #{enable,jdbcType=BIT},",
          "query = #{query,jdbcType=LONGVARCHAR},",
          "answer = #{answer,jdbcType=LONGVARCHAR}",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and qq_id = #{qqId,jdbcType=BIGINT}",
          "and sub_id = #{subId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKeyWithBLOBs(BotMessageRecord record);

    @Update({
        "update bot_message_record",
        "set single_id = #{singleId,jdbcType=INTEGER},",
          "ids = #{ids,jdbcType=VARCHAR},",
          "date = #{date,jdbcType=VARCHAR},",
          "date_time = #{dateTime,jdbcType=VARCHAR},",
          "message_mirai_code = #{messageMiraiCode,jdbcType=VARCHAR},",
          "bot_name = #{botName,jdbcType=VARCHAR},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP},",
          "enable = #{enable,jdbcType=BIT}",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and qq_id = #{qqId,jdbcType=BIGINT}",
          "and sub_id = #{subId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(BotMessageRecord record);
}