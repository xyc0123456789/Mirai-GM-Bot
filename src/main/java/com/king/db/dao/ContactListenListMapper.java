package com.king.db.dao;

import com.king.db.pojo.ContactListenList;
import com.king.db.pojo.ContactListenListExample;
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
public interface ContactListenListMapper {
    @Delete({
        "delete from contact_listen_list",
        "where contact_type = #{contactType,jdbcType=INTEGER}",
          "and contact_id = #{contactId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(@Param("contactType") Integer contactType, @Param("contactId") Long contactId);

    @Insert({
        "insert into contact_listen_list (contact_type, contact_id, ",
        "remark, ctime, ",
        "utime, enable, permission)",
        "values (#{contactType,jdbcType=INTEGER}, #{contactId,jdbcType=BIGINT}, ",
        "#{remark,jdbcType=VARCHAR}, #{ctime,jdbcType=TIMESTAMP}, ",
        "#{utime,jdbcType=TIMESTAMP}, #{enable,jdbcType=BIT}, #{permission,jdbcType=LONGVARCHAR})"
    })
    int insert(ContactListenList record);

    @InsertProvider(type=ContactListenListSqlProvider.class, method="insertSelective")
    int insertSelective(ContactListenList record);

    @SelectProvider(type=ContactListenListSqlProvider.class, method="selectByExampleWithBLOBs")
    @Results({
        @Result(column="contact_type", property="contactType", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="contact_id", property="contactId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT),
        @Result(column="permission", property="permission", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<ContactListenList> selectByExampleWithBLOBs(ContactListenListExample example);

    @SelectProvider(type=ContactListenListSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="contact_type", property="contactType", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="contact_id", property="contactId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT)
    })
    List<ContactListenList> selectByExample(ContactListenListExample example);

    @Select({
        "select",
        "contact_type, contact_id, remark, ctime, utime, enable, permission",
        "from contact_listen_list",
        "where contact_type = #{contactType,jdbcType=INTEGER}",
          "and contact_id = #{contactId,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="contact_type", property="contactType", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="contact_id", property="contactId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT),
        @Result(column="permission", property="permission", jdbcType=JdbcType.LONGVARCHAR)
    })
    ContactListenList selectByPrimaryKey(@Param("contactType") Integer contactType, @Param("contactId") Long contactId);

    @UpdateProvider(type=ContactListenListSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ContactListenList record);

    @Update({
        "update contact_listen_list",
        "set remark = #{remark,jdbcType=VARCHAR},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP},",
          "enable = #{enable,jdbcType=BIT},",
          "permission = #{permission,jdbcType=LONGVARCHAR}",
        "where contact_type = #{contactType,jdbcType=INTEGER}",
          "and contact_id = #{contactId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKeyWithBLOBs(ContactListenList record);

    @Update({
        "update contact_listen_list",
        "set remark = #{remark,jdbcType=VARCHAR},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP},",
          "enable = #{enable,jdbcType=BIT}",
        "where contact_type = #{contactType,jdbcType=INTEGER}",
          "and contact_id = #{contactId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(ContactListenList record);
}