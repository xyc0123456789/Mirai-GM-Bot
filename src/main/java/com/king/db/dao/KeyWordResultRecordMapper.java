package com.king.db.dao;

import com.king.db.pojo.KeyWordResultRecord;
import com.king.db.pojo.KeyWordResultRecordExample;
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
public interface KeyWordResultRecordMapper {
    @Delete({
        "delete from key_word_result_record",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into key_word_result_record (id, type, ",
        "md5, content, ctime, ",
        "utime, enable)",
        "values (#{id,jdbcType=BIGINT}, #{type,jdbcType=VARCHAR}, ",
        "#{md5,jdbcType=VARCHAR}, #{content,jdbcType=VARCHAR}, #{ctime,jdbcType=TIMESTAMP}, ",
        "#{utime,jdbcType=TIMESTAMP}, #{enable,jdbcType=BIT})"
    })
    int insert(KeyWordResultRecord record);

    @InsertProvider(type=KeyWordResultRecordSqlProvider.class, method="insertSelective")
    int insertSelective(KeyWordResultRecord record);

    @SelectProvider(type=KeyWordResultRecordSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
        @Result(column="md5", property="md5", jdbcType=JdbcType.VARCHAR),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT)
    })
    List<KeyWordResultRecord> selectByExample(KeyWordResultRecordExample example);

    @Select({
        "select",
        "id, type, md5, content, ctime, utime, enable",
        "from key_word_result_record",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="type", property="type", jdbcType=JdbcType.VARCHAR),
        @Result(column="md5", property="md5", jdbcType=JdbcType.VARCHAR),
        @Result(column="content", property="content", jdbcType=JdbcType.VARCHAR),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="enable", property="enable", jdbcType=JdbcType.BIT)
    })
    KeyWordResultRecord selectByPrimaryKey(Long id);

    @UpdateProvider(type=KeyWordResultRecordSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(KeyWordResultRecord record);

    @Update({
        "update key_word_result_record",
        "set type = #{type,jdbcType=VARCHAR},",
          "md5 = #{md5,jdbcType=VARCHAR},",
          "content = #{content,jdbcType=VARCHAR},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP},",
          "enable = #{enable,jdbcType=BIT}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(KeyWordResultRecord record);
}