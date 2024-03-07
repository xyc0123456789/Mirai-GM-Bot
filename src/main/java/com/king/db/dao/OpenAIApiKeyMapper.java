package com.king.db.dao;

import com.king.db.pojo.OpenAIApiKey;
import com.king.db.pojo.OpenAIApiKeyExample;
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
public interface OpenAIApiKeyMapper {
    @Delete({
        "delete from open_ai_api_key",
        "where api_key = #{apiKey,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String apiKey);

    @Insert({
        "insert into open_ai_api_key (api_key, rate_limit, ",
        "hard_limit_usd, total_usage)",
        "values (#{apiKey,jdbcType=VARCHAR}, #{rateLimit,jdbcType=BIGINT}, ",
        "#{hardLimitUsd,jdbcType=VARCHAR}, #{totalUsage,jdbcType=VARCHAR})"
    })
    int insert(OpenAIApiKey record);

    @InsertProvider(type=OpenAIApiKeySqlProvider.class, method="insertSelective")
    int insertSelective(OpenAIApiKey record);

    @SelectProvider(type=OpenAIApiKeySqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="api_key", property="apiKey", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="rate_limit", property="rateLimit", jdbcType=JdbcType.BIGINT),
        @Result(column="hard_limit_usd", property="hardLimitUsd", jdbcType=JdbcType.VARCHAR),
        @Result(column="total_usage", property="totalUsage", jdbcType=JdbcType.VARCHAR)
    })
    List<OpenAIApiKey> selectByExample(OpenAIApiKeyExample example);

    @Select({
        "select",
        "api_key, rate_limit, hard_limit_usd, total_usage",
        "from open_ai_api_key",
        "where api_key = #{apiKey,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="api_key", property="apiKey", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="rate_limit", property="rateLimit", jdbcType=JdbcType.BIGINT),
        @Result(column="hard_limit_usd", property="hardLimitUsd", jdbcType=JdbcType.VARCHAR),
        @Result(column="total_usage", property="totalUsage", jdbcType=JdbcType.VARCHAR)
    })
    OpenAIApiKey selectByPrimaryKey(String apiKey);

    @UpdateProvider(type=OpenAIApiKeySqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(OpenAIApiKey record);

    @Update({
        "update open_ai_api_key",
        "set rate_limit = #{rateLimit,jdbcType=BIGINT},",
          "hard_limit_usd = #{hardLimitUsd,jdbcType=VARCHAR},",
          "total_usage = #{totalUsage,jdbcType=VARCHAR}",
        "where api_key = #{apiKey,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(OpenAIApiKey record);
}