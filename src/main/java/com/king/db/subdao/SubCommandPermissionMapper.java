package com.king.db.subdao;

import com.king.db.dao.CommandPermissionMapper;
import com.king.db.pojo.CommandPermission;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface SubCommandPermissionMapper extends CommandPermissionMapper {


    @Select({
            "select",
            "contact_type, contact_id, sub_contact_id, command_name, command_desc, remark1, ",
            "remark2, remark3, ext_data, is_open, ctime, utime, enable",
            "from command_permission",
            "where contact_type = #{contactType,jdbcType=INTEGER}",
            "and contact_id = #{contactId,jdbcType=BIGINT}",
            "and sub_contact_id = #{subContactId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="contact_type", property="contactType", jdbcType= JdbcType.INTEGER, id=true),
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
    List<CommandPermission> selectByOnePerson(@Param("contactType") Integer contactType, @Param("contactId") Long contactId, @Param("subContactId") Long subContactId);
    
}