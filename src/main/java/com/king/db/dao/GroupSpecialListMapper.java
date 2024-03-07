package com.king.db.dao;

import com.king.db.pojo.GroupSpecialList;
import com.king.db.pojo.GroupSpecialListExample;
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
public interface GroupSpecialListMapper {
    @Delete({
        "delete from group_special_list",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and contact_id = #{contactId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(@Param("groupId") Long groupId, @Param("contactId") Long contactId);

    @Insert({
        "insert into group_special_list (group_id, contact_id, ",
        "nick_name, card_name, ",
        "special_title, permission_level, ",
        "ctime, utime, ",
        "enable, ext_data)",
        "values (#{groupId,jdbcType=BIGINT}, #{contactId,jdbcType=BIGINT}, ",
        "#{nickName,jdbcType=VARCHAR}, #{cardName,jdbcType=VARCHAR}, ",
        "#{specialTitle,jdbcType=VARCHAR}, #{permissionLevel,jdbcType=BIGINT}, ",
        "#{ctime,jdbcType=TIMESTAMP}, #{utime,jdbcType=TIMESTAMP}, ",
        "#{enable,jdbcType=BIT}, #{extData,jdbcType=LONGVARCHAR})"
    })
    int insert(GroupSpecialList record);

    @InsertProvider(type=GroupSpecialListSqlProvider.class, method="insertSelective")
    int insertSelective(GroupSpecialList record);

    @SelectProvider(type=GroupSpecialListSqlProvider.class, method="selectByExampleWithBLOBs")
    @Results({
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="contact_id", property="contactId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="nick_name", property="nickName", jdbcType=JdbcType.VARCHAR),
        @Result(column="card_name", property="cardName", jdbcType=JdbcType.VARCHAR),
        @Result(column="special_title", property="specialTitle", jdbcType=JdbcType.VARCHAR),
        @Result(column="permission_level", property="permissionLevel", jdbcType=JdbcType.BIGINT),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT),
        @Result(column="ext_data", property="extData", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<GroupSpecialList> selectByExampleWithBLOBs(GroupSpecialListExample example);

    @SelectProvider(type=GroupSpecialListSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="contact_id", property="contactId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="nick_name", property="nickName", jdbcType=JdbcType.VARCHAR),
        @Result(column="card_name", property="cardName", jdbcType=JdbcType.VARCHAR),
        @Result(column="special_title", property="specialTitle", jdbcType=JdbcType.VARCHAR),
        @Result(column="permission_level", property="permissionLevel", jdbcType=JdbcType.BIGINT),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT)
    })
    List<GroupSpecialList> selectByExample(GroupSpecialListExample example);

    @Select({
        "select",
        "group_id, contact_id, nick_name, card_name, special_title, permission_level, ",
        "ctime, utime, enable, ext_data",
        "from group_special_list",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and contact_id = #{contactId,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="contact_id", property="contactId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="nick_name", property="nickName", jdbcType=JdbcType.VARCHAR),
        @Result(column="card_name", property="cardName", jdbcType=JdbcType.VARCHAR),
        @Result(column="special_title", property="specialTitle", jdbcType=JdbcType.VARCHAR),
        @Result(column="permission_level", property="permissionLevel", jdbcType=JdbcType.BIGINT),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT),
        @Result(column="ext_data", property="extData", jdbcType=JdbcType.LONGVARCHAR)
    })
    GroupSpecialList selectByPrimaryKey(@Param("groupId") Long groupId, @Param("contactId") Long contactId);

    @UpdateProvider(type=GroupSpecialListSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(GroupSpecialList record);

    @Update({
        "update group_special_list",
        "set nick_name = #{nickName,jdbcType=VARCHAR},",
          "card_name = #{cardName,jdbcType=VARCHAR},",
          "special_title = #{specialTitle,jdbcType=VARCHAR},",
          "permission_level = #{permissionLevel,jdbcType=BIGINT},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP},",
          "enable = #{enable,jdbcType=BIT},",
          "ext_data = #{extData,jdbcType=LONGVARCHAR}",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and contact_id = #{contactId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKeyWithBLOBs(GroupSpecialList record);

    @Update({
        "update group_special_list",
        "set nick_name = #{nickName,jdbcType=VARCHAR},",
          "card_name = #{cardName,jdbcType=VARCHAR},",
          "special_title = #{specialTitle,jdbcType=VARCHAR},",
          "permission_level = #{permissionLevel,jdbcType=BIGINT},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP},",
          "enable = #{enable,jdbcType=BIT}",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and contact_id = #{contactId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(GroupSpecialList record);
}