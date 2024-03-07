package com.king.db.subdao;

import com.king.db.dao.ContactListenListMapper;
import com.king.db.pojo.ContactListenList;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface SubContactListenListMapper extends ContactListenListMapper {


    @Select({
            "select",
            "contact_type, contact_id, remark, ctime, utime, enable, permission",
            "from contact_listen_list",
            "where contact_type = #{contactType,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="contact_type", property="contactType", jdbcType= JdbcType.INTEGER, id=true),
            @Result(column="contact_id", property="contactId", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
            @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="enable", property="enable", jdbcType=JdbcType.BIT),
            @Result(column="permission", property="permission", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<ContactListenList> selectByContactType(@Param("contactType") Integer contactType);
}