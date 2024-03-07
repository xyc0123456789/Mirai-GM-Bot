package com.king.db.dao;

import com.king.db.pojo.MessageRecord;
import com.king.db.pojo.MessageRecordExample;
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
public interface MessageRecordMapper {
    @Delete({
        "delete from message_record",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and qq_id = #{qqId,jdbcType=BIGINT}",
          "and sub_id = #{subId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(@Param("groupId") Long groupId, @Param("qqId") Long qqId, @Param("subId") Long subId);

    @Insert({
        "insert into message_record (group_id, qq_id, ",
        "sub_id, single_id, ",
        "ids, date, date_time, ",
        "message_mirai_code, text_content, ",
        "image_url_list, audio_url, ",
        "operator_id, operator_name, ",
        "ctime, utime, ",
        "enable)",
        "values (#{groupId,jdbcType=BIGINT}, #{qqId,jdbcType=BIGINT}, ",
        "#{subId,jdbcType=BIGINT}, #{singleId,jdbcType=INTEGER}, ",
        "#{ids,jdbcType=VARCHAR}, #{date,jdbcType=VARCHAR}, #{dateTime,jdbcType=VARCHAR}, ",
        "#{messageMiraiCode,jdbcType=VARCHAR}, #{textContent,jdbcType=VARCHAR}, ",
        "#{imageUrlList,jdbcType=VARCHAR}, #{audioUrl,jdbcType=VARCHAR}, ",
        "#{operatorId,jdbcType=BIGINT}, #{operatorName,jdbcType=VARCHAR}, ",
        "#{ctime,jdbcType=TIMESTAMP}, #{utime,jdbcType=TIMESTAMP}, ",
        "#{enable,jdbcType=BIT})"
    })
    int insert(MessageRecord record);

    @InsertProvider(type=MessageRecordSqlProvider.class, method="insertSelective")
    int insertSelective(MessageRecord record);

    @SelectProvider(type=MessageRecordSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="qq_id", property="qqId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="sub_id", property="subId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="single_id", property="singleId", jdbcType=JdbcType.INTEGER),
        @Result(column="ids", property="ids", jdbcType=JdbcType.VARCHAR),
        @Result(column="date", property="date", jdbcType=JdbcType.VARCHAR),
        @Result(column="date_time", property="dateTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="message_mirai_code", property="messageMiraiCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="text_content", property="textContent", jdbcType=JdbcType.VARCHAR),
        @Result(column="image_url_list", property="imageUrlList", jdbcType=JdbcType.VARCHAR),
        @Result(column="audio_url", property="audioUrl", jdbcType=JdbcType.VARCHAR),
        @Result(column="operator_id", property="operatorId", jdbcType=JdbcType.BIGINT),
        @Result(column="operator_name", property="operatorName", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT)
    })
    List<MessageRecord> selectByExample(MessageRecordExample example);

    @Select({
        "select",
        "group_id, qq_id, sub_id, single_id, ids, date, date_time, message_mirai_code, ",
        "text_content, image_url_list, audio_url, operator_id, operator_name, ctime, ",
        "utime, enable",
        "from message_record",
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
        @Result(column="text_content", property="textContent", jdbcType=JdbcType.VARCHAR),
        @Result(column="image_url_list", property="imageUrlList", jdbcType=JdbcType.VARCHAR),
        @Result(column="audio_url", property="audioUrl", jdbcType=JdbcType.VARCHAR),
        @Result(column="operator_id", property="operatorId", jdbcType=JdbcType.BIGINT),
        @Result(column="operator_name", property="operatorName", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT)
    })
    MessageRecord selectByPrimaryKey(@Param("groupId") Long groupId, @Param("qqId") Long qqId, @Param("subId") Long subId);

    @UpdateProvider(type=MessageRecordSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(MessageRecord record);

    @Update({
        "update message_record",
        "set single_id = #{singleId,jdbcType=INTEGER},",
          "ids = #{ids,jdbcType=VARCHAR},",
          "date = #{date,jdbcType=VARCHAR},",
          "date_time = #{dateTime,jdbcType=VARCHAR},",
          "message_mirai_code = #{messageMiraiCode,jdbcType=VARCHAR},",
          "text_content = #{textContent,jdbcType=VARCHAR},",
          "image_url_list = #{imageUrlList,jdbcType=VARCHAR},",
          "audio_url = #{audioUrl,jdbcType=VARCHAR},",
          "operator_id = #{operatorId,jdbcType=BIGINT},",
          "operator_name = #{operatorName,jdbcType=VARCHAR},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP},",
          "enable = #{enable,jdbcType=BIT}",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and qq_id = #{qqId,jdbcType=BIGINT}",
          "and sub_id = #{subId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(MessageRecord record);
}