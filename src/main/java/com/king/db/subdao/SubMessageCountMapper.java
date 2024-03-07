package com.king.db.subdao;

import com.king.db.dao.MessageCountMapper;
import com.king.db.pojo.MessageCount;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface SubMessageCountMapper extends MessageCountMapper {


    @Select({
            "select",
            "contact_type, contact_id, sub_contact_id, date, count, remark, permission, ctime, ",
            "utime, enable",
            "from message_count",
            "where 1=1"
    })
    @Results({
            @Result(column="contact_type", property="contactType", jdbcType= JdbcType.INTEGER, id=true),
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
    List<MessageCount> findAll();


    @Select({
            "select",
            "contact_type, contact_id, sub_contact_id, date, count, remark, permission, ctime, ",
            "utime, enable",
            "from message_count",
            "where date = #{date,jdbcType=VARCHAR}"
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
    List<MessageCount> selectByDate(@Param("date") String date);


    @Select({
            "select",
            "contact_type, contact_id, sub_contact_id, date, count, remark, permission, ctime, ",
            "utime, enable",
            "from message_count",
            "where contact_type = #{contactType,jdbcType=INTEGER}",
            "and contact_id = #{contactId,jdbcType=BIGINT}",
            "and sub_contact_id = #{subContactId,jdbcType=BIGINT}"
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
    List<MessageCount> selectByGroupPerson(@Param("contactType") Integer contactType, @Param("contactId") Long contactId, @Param("subContactId") Long subContactId);

    @Select({
            "select",
            "contact_type, contact_id, sub_contact_id, date, count, remark, permission, ctime, ",
            "utime, enable",
            "from message_count",
            "where contact_type = #{contactType,jdbcType=INTEGER}",
            "and contact_id = #{contactId,jdbcType=BIGINT}",
            "and sub_contact_id = #{subContactId,jdbcType=BIGINT}",
            "and (date between #{startDate,jdbcType=VARCHAR} and #{endDate,jdbcType=VARCHAR})",
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
    List<MessageCount> selectByGroupPersonBetween(@Param("contactType") Integer contactType, @Param("contactId") Long contactId, @Param("subContactId") Long subContactId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Select({
            "select",
            "contact_type, contact_id, sub_contact_id, date, count, remark, permission, ctime, ",
            "utime, enable",
            "from message_count",
            "where contact_type = #{contactType,jdbcType=INTEGER}",
            "and contact_id = #{contactId,jdbcType=BIGINT}",
            "and date = #{date,jdbcType=VARCHAR}",
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
    List<MessageCount> selectByGroupIdAndDate(@Param("contactType") Integer contactType, @Param("contactId") Long contactId, @Param("date") String date);

}