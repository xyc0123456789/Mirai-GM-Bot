package com.king.db.dao;

import com.king.db.pojo.MessageCount;
import com.king.db.pojo.MessageCountExample;
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
public interface MessageCountMapper {
    @Delete({
        "delete from message_count",
        "where contact_type = #{contactType,jdbcType=INTEGER}",
          "and contact_id = #{contactId,jdbcType=BIGINT}",
          "and sub_contact_id = #{subContactId,jdbcType=BIGINT}",
          "and date = #{date,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(@Param("contactType") Integer contactType, @Param("contactId") Long contactId, @Param("subContactId") Long subContactId, @Param("date") String date);

    @Insert({
        "insert into message_count (contact_type, contact_id, ",
        "sub_contact_id, date, ",
        "count, remark, permission, ",
        "ctime, utime, ",
        "enable)",
        "values (#{contactType,jdbcType=INTEGER}, #{contactId,jdbcType=BIGINT}, ",
        "#{subContactId,jdbcType=BIGINT}, #{date,jdbcType=VARCHAR}, ",
        "#{count,jdbcType=BIGINT}, #{remark,jdbcType=VARCHAR}, #{permission,jdbcType=VARCHAR}, ",
        "#{ctime,jdbcType=TIMESTAMP}, #{utime,jdbcType=TIMESTAMP}, ",
        "#{enable,jdbcType=BIT})"
    })
    int insert(MessageCount record);

    @InsertProvider(type=MessageCountSqlProvider.class, method="insertSelective")
    int insertSelective(MessageCount record);

    @SelectProvider(type=MessageCountSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="contact_type", property="contactType", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="contact_id", property="contactId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="sub_contact_id", property="subContactId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="date", property="date", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="count", property="count", jdbcType=JdbcType.BIGINT),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="permission", property="permission", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT)
    })
    List<MessageCount> selectByExample(MessageCountExample example);

    @Select({
        "select",
        "contact_type, contact_id, sub_contact_id, date, count, remark, permission, ctime, ",
        "utime, enable",
        "from message_count",
        "where contact_type = #{contactType,jdbcType=INTEGER}",
          "and contact_id = #{contactId,jdbcType=BIGINT}",
          "and sub_contact_id = #{subContactId,jdbcType=BIGINT}",
          "and date = #{date,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="contact_type", property="contactType", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="contact_id", property="contactId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="sub_contact_id", property="subContactId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="date", property="date", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="count", property="count", jdbcType=JdbcType.BIGINT),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="permission", property="permission", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT)
    })
    MessageCount selectByPrimaryKey(@Param("contactType") Integer contactType, @Param("contactId") Long contactId, @Param("subContactId") Long subContactId, @Param("date") String date);

    @UpdateProvider(type=MessageCountSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(MessageCount record);

    @Update({
        "update message_count",
        "set count = #{count,jdbcType=BIGINT},",
          "remark = #{remark,jdbcType=VARCHAR},",
          "permission = #{permission,jdbcType=VARCHAR},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP},",
          "enable = #{enable,jdbcType=BIT}",
        "where contact_type = #{contactType,jdbcType=INTEGER}",
          "and contact_id = #{contactId,jdbcType=BIGINT}",
          "and sub_contact_id = #{subContactId,jdbcType=BIGINT}",
          "and date = #{date,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(MessageCount record);
}