package com.king.db.dao;

import com.king.db.pojo.Subscription;
import com.king.db.pojo.SubscriptionExample;
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
public interface SubscriptionMapper {
    @Delete({
        "delete from subscription",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and platform = #{platform,jdbcType=VARCHAR}",
          "and room_id = #{roomId,jdbcType=VARCHAR}",
          "and qq_id = #{qqId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(@Param("groupId") Long groupId, @Param("platform") String platform, @Param("roomId") String roomId, @Param("qqId") Long qqId);

    @Insert({
        "insert into subscription (group_id, platform, ",
        "room_id, qq_id, is_group, ",
        "remark, ctime, ",
        "utime, enable)",
        "values (#{groupId,jdbcType=BIGINT}, #{platform,jdbcType=VARCHAR}, ",
        "#{roomId,jdbcType=VARCHAR}, #{qqId,jdbcType=BIGINT}, #{isGroup,jdbcType=BIT}, ",
        "#{remark,jdbcType=VARCHAR}, #{ctime,jdbcType=TIMESTAMP}, ",
        "#{utime,jdbcType=TIMESTAMP}, #{enable,jdbcType=BIT})"
    })
    int insert(Subscription record);

    @InsertProvider(type=SubscriptionSqlProvider.class, method="insertSelective")
    int insertSelective(Subscription record);

    @SelectProvider(type=SubscriptionSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="platform", property="platform", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="room_id", property="roomId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="qq_id", property="qqId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="is_group", property="isGroup", jdbcType=JdbcType.BIT),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT)
    })
    List<Subscription> selectByExample(SubscriptionExample example);

    @Select({
        "select",
        "group_id, platform, room_id, qq_id, is_group, remark, ctime, utime, enable",
        "from subscription",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and platform = #{platform,jdbcType=VARCHAR}",
          "and room_id = #{roomId,jdbcType=VARCHAR}",
          "and qq_id = #{qqId,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="group_id", property="groupId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="platform", property="platform", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="room_id", property="roomId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="qq_id", property="qqId", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="is_group", property="isGroup", jdbcType=JdbcType.BIT),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT)
    })
    Subscription selectByPrimaryKey(@Param("groupId") Long groupId, @Param("platform") String platform, @Param("roomId") String roomId, @Param("qqId") Long qqId);

    @UpdateProvider(type=SubscriptionSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Subscription record);

    @Update({
        "update subscription",
        "set is_group = #{isGroup,jdbcType=BIT},",
          "remark = #{remark,jdbcType=VARCHAR},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP},",
          "enable = #{enable,jdbcType=BIT}",
        "where group_id = #{groupId,jdbcType=BIGINT}",
          "and platform = #{platform,jdbcType=VARCHAR}",
          "and room_id = #{roomId,jdbcType=VARCHAR}",
          "and qq_id = #{qqId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(Subscription record);
}