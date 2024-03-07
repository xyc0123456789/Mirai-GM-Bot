package com.king.db.dao;

import com.king.db.pojo.Members;
import com.king.db.pojo.MembersExample;
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
public interface MembersMapper {
    @Delete({
        "delete from members",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and qq_id = #{qqId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(@Param("groupId") Long groupId, @Param("qqId") Long qqId);

    @Insert({
        "insert into members (group_id, qq_id, ",
        "qq_level, age, sex, ",
        "nick_name, name_card, ",
        "special_title, email, ",
        "join_date, last_speak_date, ",
        "ctime, utime, ",
        "enable, sign, ext_data)",
        "values (#{groupId,jdbcType=BIGINT}, #{qqId,jdbcType=BIGINT}, ",
        "#{qqLevel,jdbcType=INTEGER}, #{age,jdbcType=INTEGER}, #{sex,jdbcType=VARCHAR}, ",
        "#{nickName,jdbcType=VARCHAR}, #{nameCard,jdbcType=VARCHAR}, ",
        "#{specialTitle,jdbcType=VARCHAR}, #{email,jdbcType=VARCHAR}, ",
        "#{joinDate,jdbcType=TIMESTAMP}, #{lastSpeakDate,jdbcType=TIMESTAMP}, ",
        "#{ctime,jdbcType=TIMESTAMP}, #{utime,jdbcType=TIMESTAMP}, ",
        "#{enable,jdbcType=BIT}, #{sign,jdbcType=LONGVARCHAR}, #{extData,jdbcType=LONGVARCHAR})"
    })
    int insert(Members record);

    @InsertProvider(type=MembersSqlProvider.class, method="insertSelective")
    int insertSelective(Members record);

    @SelectProvider(type=MembersSqlProvider.class, method="selectByExampleWithBLOBs")
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
    List<Members> selectByExampleWithBLOBs(MembersExample example);

    @SelectProvider(type=MembersSqlProvider.class, method="selectByExample")
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
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT)
    })
    List<Members> selectByExample(MembersExample example);

    @Select({
        "select",
        "group_id, qq_id, qq_level, age, sex, nick_name, name_card, special_title, email, ",
        "join_date, last_speak_date, ctime, utime, enable, sign, ext_data",
        "from members",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and qq_id = #{qqId,jdbcType=BIGINT}"
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
    Members selectByPrimaryKey(@Param("groupId") Long groupId, @Param("qqId") Long qqId);

    @UpdateProvider(type=MembersSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Members record);

    @Update({
        "update members",
        "set qq_level = #{qqLevel,jdbcType=INTEGER},",
          "age = #{age,jdbcType=INTEGER},",
          "sex = #{sex,jdbcType=VARCHAR},",
          "nick_name = #{nickName,jdbcType=VARCHAR},",
          "name_card = #{nameCard,jdbcType=VARCHAR},",
          "special_title = #{specialTitle,jdbcType=VARCHAR},",
          "email = #{email,jdbcType=VARCHAR},",
          "join_date = #{joinDate,jdbcType=TIMESTAMP},",
          "last_speak_date = #{lastSpeakDate,jdbcType=TIMESTAMP},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP},",
          "enable = #{enable,jdbcType=BIT},",
          "sign = #{sign,jdbcType=LONGVARCHAR},",
          "ext_data = #{extData,jdbcType=LONGVARCHAR}",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and qq_id = #{qqId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKeyWithBLOBs(Members record);

    @Update({
        "update members",
        "set qq_level = #{qqLevel,jdbcType=INTEGER},",
          "age = #{age,jdbcType=INTEGER},",
          "sex = #{sex,jdbcType=VARCHAR},",
          "nick_name = #{nickName,jdbcType=VARCHAR},",
          "name_card = #{nameCard,jdbcType=VARCHAR},",
          "special_title = #{specialTitle,jdbcType=VARCHAR},",
          "email = #{email,jdbcType=VARCHAR},",
          "join_date = #{joinDate,jdbcType=TIMESTAMP},",
          "last_speak_date = #{lastSpeakDate,jdbcType=TIMESTAMP},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP},",
          "enable = #{enable,jdbcType=BIT}",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and qq_id = #{qqId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Members record);
}