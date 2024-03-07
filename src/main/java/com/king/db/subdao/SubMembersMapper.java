package com.king.db.subdao;

import com.king.db.dao.MembersMapper;
import com.king.db.pojo.Members;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface SubMembersMapper extends MembersMapper {

    @Select({
            "select",
            "group_id, qq_id, qq_level, age, sex, nick_name, name_card, special_title, email, ",
            "join_date, last_speak_date, ctime, utime, enable, sign, ext_data",
            "from members",
            "where 1 = 1"
    })
    @Results({
            @Result(column="group_id", property="groupId", jdbcType= JdbcType.BIGINT, id=true),
            @Result(column="qq_id", property="qqId", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="qq_level", property="qqLevel", jdbcType=JdbcType.INTEGER),
            @Result(column="age", property="age", jdbcType=JdbcType.INTEGER),
            @Result(column="sex", property="sex", jdbcType=JdbcType.VARCHAR),
            @Result(column="nick_name", property="nickName", jdbcType=JdbcType.VARCHAR),
            @Result(column="name_card", property="nameCard", jdbcType=JdbcType.VARCHAR),
            @Result(column="special_title", property="specialTitle", jdbcType=JdbcType.VARCHAR),
            @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
            @Result(column="join_date", property="joinDate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="last_speak_date", property="lastSpeakDate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="enable", property="enable", jdbcType=JdbcType.BIT),
            @Result(column="sign", property="sign", jdbcType=JdbcType.LONGVARCHAR),
            @Result(column="ext_data", property="extData", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<Members> getAll();


    @Select({
            "select",
            "group_id, qq_id, qq_level, age, sex, nick_name, name_card, special_title, email, ",
            "join_date, last_speak_date, ctime, utime, enable, sign, ext_data",
            "from members",
            "where group_id = #{groupId,jdbcType=BIGINT}"
    })
    @Results({
            @Result(column="group_id", property="groupId", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="qq_id", property="qqId", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="qq_level", property="qqLevel", jdbcType=JdbcType.INTEGER),
            @Result(column="age", property="age", jdbcType=JdbcType.INTEGER),
            @Result(column="sex", property="sex", jdbcType=JdbcType.VARCHAR),
            @Result(column="nick_name", property="nickName", jdbcType=JdbcType.VARCHAR),
            @Result(column="name_card", property="nameCard", jdbcType=JdbcType.VARCHAR),
            @Result(column="special_title", property="specialTitle", jdbcType=JdbcType.VARCHAR),
            @Result(column="email", property="email", jdbcType=JdbcType.VARCHAR),
            @Result(column="join_date", property="joinDate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="last_speak_date", property="lastSpeakDate", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="enable", property="enable", jdbcType=JdbcType.BIT),
            @Result(column="sign", property="sign", jdbcType=JdbcType.LONGVARCHAR),
            @Result(column="ext_data", property="extData", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<Members> getByGroup(@Param("groupId") Long groupId);
}