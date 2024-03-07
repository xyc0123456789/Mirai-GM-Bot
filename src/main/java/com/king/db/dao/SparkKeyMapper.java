package com.king.db.dao;

import com.king.db.pojo.SparkKey;
import com.king.db.pojo.SparkKeyExample;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

@Mapper
public interface SparkKeyMapper {
    @Delete({
        "delete from spark_key",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String id);

    @Insert({
        "insert into spark_key (id, appid, ",
        "apiKey, apiSecret, ",
        "rate_limit, total_usage)",
        "values (#{id,jdbcType=VARCHAR}, #{appid,jdbcType=VARCHAR}, ",
        "#{apikey,jdbcType=VARCHAR}, #{apisecret,jdbcType=VARCHAR}, ",
        "#{rateLimit,jdbcType=VARCHAR}, #{totalUsage,jdbcType=VARCHAR})"
    })
    int insert(SparkKey record);

    @InsertProvider(type=SparkKeySqlProvider.class, method="insertSelective")
    int insertSelective(SparkKey record);

    @SelectProvider(type=SparkKeySqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="appid", property="appid", jdbcType=JdbcType.VARCHAR),
        @Result(column="apiKey", property="apikey", jdbcType=JdbcType.VARCHAR),
        @Result(column="apiSecret", property="apisecret", jdbcType=JdbcType.VARCHAR),
        @Result(column="rate_limit", property="rateLimit", jdbcType=JdbcType.VARCHAR),
        @Result(column="total_usage", property="totalUsage", jdbcType=JdbcType.VARCHAR)
    })
    List<SparkKey> selectByExample(SparkKeyExample example);

    @Select({
        "select",
        "id, appid, apiKey, apiSecret, rate_limit, total_usage",
        "from spark_key",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="appid", property="appid", jdbcType=JdbcType.VARCHAR),
        @Result(column="apiKey", property="apikey", jdbcType=JdbcType.VARCHAR),
        @Result(column="apiSecret", property="apisecret", jdbcType=JdbcType.VARCHAR),
        @Result(column="rate_limit", property="rateLimit", jdbcType=JdbcType.VARCHAR),
        @Result(column="total_usage", property="totalUsage", jdbcType=JdbcType.VARCHAR)
    })
    SparkKey selectByPrimaryKey(String id);

    @UpdateProvider(type=SparkKeySqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(SparkKey record);

    @Update({
        "update spark_key",
        "set appid = #{appid,jdbcType=VARCHAR},",
          "apiKey = #{apikey,jdbcType=VARCHAR},",
          "apiSecret = #{apisecret,jdbcType=VARCHAR},",
          "rate_limit = #{rateLimit,jdbcType=VARCHAR},",
          "total_usage = #{totalUsage,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(SparkKey record);
}