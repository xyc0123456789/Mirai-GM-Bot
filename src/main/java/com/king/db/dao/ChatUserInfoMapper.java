package com.king.db.dao;

import com.king.db.pojo.ChatUserInfo;
import com.king.db.pojo.ChatUserInfoExample;
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
public interface ChatUserInfoMapper {
    @Delete({
        "delete from chat_user_info",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String userId);

    @Insert({
        "insert into chat_user_info (user_id, prompt, ",
        "last_request_no)",
        "values (#{userId,jdbcType=VARCHAR}, #{prompt,jdbcType=VARCHAR}, ",
        "#{lastRequestNo,jdbcType=VARCHAR})"
    })
    int insert(ChatUserInfo record);

    @InsertProvider(type=ChatUserInfoSqlProvider.class, method="insertSelective")
    int insertSelective(ChatUserInfo record);

    @SelectProvider(type=ChatUserInfoSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="user_id", property="userId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="prompt", property="prompt", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_request_no", property="lastRequestNo", jdbcType=JdbcType.VARCHAR)
    })
    List<ChatUserInfo> selectByExample(ChatUserInfoExample example);

    @Select({
        "select",
        "user_id, prompt, last_request_no",
        "from chat_user_info",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="user_id", property="userId", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="prompt", property="prompt", jdbcType=JdbcType.VARCHAR),
        @Result(column="last_request_no", property="lastRequestNo", jdbcType=JdbcType.VARCHAR)
    })
    ChatUserInfo selectByPrimaryKey(String userId);

    @UpdateProvider(type=ChatUserInfoSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ChatUserInfo record);

    @Update({
        "update chat_user_info",
        "set prompt = #{prompt,jdbcType=VARCHAR},",
          "last_request_no = #{lastRequestNo,jdbcType=VARCHAR}",
        "where user_id = #{userId,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(ChatUserInfo record);
}