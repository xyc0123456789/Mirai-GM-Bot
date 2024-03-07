package com.king.db.dao;

import com.king.db.pojo.CommandPermission;
import com.king.db.pojo.CommandPermissionExample;
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
public interface CommandPermissionMapper {
    @Delete({
        "delete from command_permission",
        "where contact_type = #{contactType,jdbcType=INTEGER}",
          "and contact_id = #{contactId,jdbcType=BIGINT}",
          "and sub_contact_id = #{subContactId,jdbcType=BIGINT}",
          "and command_name = #{commandName,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(@Param("contactType") Integer contactType, @Param("contactId") Long contactId, @Param("subContactId") Long subContactId, @Param("commandName") String commandName);

    @Insert({
        "insert into command_permission (contact_type, contact_id, ",
        "sub_contact_id, command_name, ",
        "command_desc, remark1, ",
        "remark2, remark3, ",
        "ext_data, is_open, ctime, ",
        "utime, enable)",
        "values (#{contactType,jdbcType=INTEGER}, #{contactId,jdbcType=BIGINT}, ",
        "#{subContactId,jdbcType=BIGINT}, #{commandName,jdbcType=VARCHAR}, ",
        "#{commandDesc,jdbcType=VARCHAR}, #{remark1,jdbcType=VARCHAR}, ",
        "#{remark2,jdbcType=VARCHAR}, #{remark3,jdbcType=VARCHAR}, ",
        "#{extData,jdbcType=VARCHAR}, #{isOpen,jdbcType=BIT}, #{ctime,jdbcType=TIMESTAMP}, ",
        "#{utime,jdbcType=TIMESTAMP}, #{enable,jdbcType=BIT})"
    })
    int insert(CommandPermission record);

    @InsertProvider(type=CommandPermissionSqlProvider.class, method="insertSelective")
    int insertSelective(CommandPermission record);

    @SelectProvider(type=CommandPermissionSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="contact_type", property="contactType", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="contact_id", property="contactId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="sub_contact_id", property="subContactId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="command_name", property="commandName", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="command_desc", property="commandDesc", jdbcType=JdbcType.VARCHAR),
        @Result(column="remark1", property="remark1", jdbcType=JdbcType.VARCHAR),
        @Result(column="remark2", property="remark2", jdbcType=JdbcType.VARCHAR),
        @Result(column="remark3", property="remark3", jdbcType=JdbcType.VARCHAR),
        @Result(column="ext_data", property="extData", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_open", property="isOpen", jdbcType=JdbcType.BIT),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT)
    })
    List<CommandPermission> selectByExample(CommandPermissionExample example);

    @Select({
        "select",
        "contact_type, contact_id, sub_contact_id, command_name, command_desc, remark1, ",
        "remark2, remark3, ext_data, is_open, ctime, utime, enable",
        "from command_permission",
        "where contact_type = #{contactType,jdbcType=INTEGER}",
          "and contact_id = #{contactId,jdbcType=BIGINT}",
          "and sub_contact_id = #{subContactId,jdbcType=BIGINT}",
          "and command_name = #{commandName,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="contact_type", property="contactType", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="contact_id", property="contactId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="sub_contact_id", property="subContactId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="command_name", property="commandName", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="command_desc", property="commandDesc", jdbcType=JdbcType.VARCHAR),
        @Result(column="remark1", property="remark1", jdbcType=JdbcType.VARCHAR),
        @Result(column="remark2", property="remark2", jdbcType=JdbcType.VARCHAR),
        @Result(column="remark3", property="remark3", jdbcType=JdbcType.VARCHAR),
        @Result(column="ext_data", property="extData", jdbcType=JdbcType.VARCHAR),
        @Result(column="is_open", property="isOpen", jdbcType=JdbcType.BIT),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT)
    })
    CommandPermission selectByPrimaryKey(@Param("contactType") Integer contactType, @Param("contactId") Long contactId, @Param("subContactId") Long subContactId, @Param("commandName") String commandName);

    @UpdateProvider(type=CommandPermissionSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(CommandPermission record);

    @Update({
        "update command_permission",
        "set command_desc = #{commandDesc,jdbcType=VARCHAR},",
          "remark1 = #{remark1,jdbcType=VARCHAR},",
          "remark2 = #{remark2,jdbcType=VARCHAR},",
          "remark3 = #{remark3,jdbcType=VARCHAR},",
          "ext_data = #{extData,jdbcType=VARCHAR},",
          "is_open = #{isOpen,jdbcType=BIT},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP},",
          "enable = #{enable,jdbcType=BIT}",
        "where contact_type = #{contactType,jdbcType=INTEGER}",
          "and contact_id = #{contactId,jdbcType=BIGINT}",
          "and sub_contact_id = #{subContactId,jdbcType=BIGINT}",
          "and command_name = #{commandName,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(CommandPermission record);
}