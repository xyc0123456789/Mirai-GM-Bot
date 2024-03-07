package com.king.db.dao;

import com.king.db.pojo.ChatRequestRecord;
import com.king.db.pojo.ChatRequestRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;

@Mapper
public interface ChatRequestRecordMapper {
    @Insert({
        "insert into chat_request_record (request_no, user_id, ",
        "message_id, request_date, ",
        "request_time, role, ",
        "content)",
        "values (#{requestNo,jdbcType=VARCHAR}, #{userId,jdbcType=VARCHAR}, ",
        "#{messageId,jdbcType=VARCHAR}, #{requestDate,jdbcType=VARCHAR}, ",
        "#{requestTime,jdbcType=VARCHAR}, #{role,jdbcType=VARCHAR}, ",
        "#{content,jdbcType=VARCHAR})"
    })
    int insert(ChatRequestRecord record);

    @InsertProvider(type=ChatRequestRecordSqlProvider.class, method="insertSelective")
    int insertSelective(ChatRequestRecord record);

    @SelectProvider(type=ChatRequestRecordSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="request_no", property="requestNo", jdbcType=JdbcType.VARCHAR),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.VARCHAR),
        @Result(column="message_id", property="messageId", jdbcType=JdbcType.VARCHAR),
        @Result(column="request_date", property="requestDate", jdbcType=JdbcType.VARCHAR),
        @Result(column="request_time", property="requestTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="role", property="role", jdbcType=JdbcType.VARCHAR),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR)
    })
    List<ChatRequestRecord> selectByExample(ChatRequestRecordExample example);
}