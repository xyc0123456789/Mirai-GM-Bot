package com.king.db.dao;

import com.king.db.pojo.SensitiveWord;
import com.king.db.pojo.SensitiveWordExample;
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
public interface SensitiveWordMapper {
    @Delete({
        "delete from sensitive_word",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into sensitive_word (id, mseeage, ",
        "lable, ctime, ",
        "utime, ext_data)",
        "values (#{id,jdbcType=BIGINT}, #{mseeage,jdbcType=VARCHAR}, ",
        "#{lable,jdbcType=INTEGER}, #{ctime,jdbcType=TIMESTAMP}, ",
        "#{utime,jdbcType=TIMESTAMP}, #{extData,jdbcType=LONGVARCHAR})"
    })
    int insert(SensitiveWord record);

    @InsertProvider(type=SensitiveWordSqlProvider.class, method="insertSelective")
    int insertSelective(SensitiveWord record);

    @SelectProvider(type=SensitiveWordSqlProvider.class, method="selectByExampleWithBLOBs")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="mseeage", property="mseeage", jdbcType=JdbcType.VARCHAR),
        @Result(column="lable", property="lable", jdbcType=JdbcType.INTEGER),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="ext_data", property="extData", jdbcType=JdbcType.LONGVARCHAR)
    })
    List<SensitiveWord> selectByExampleWithBLOBs(SensitiveWordExample example);

    @SelectProvider(type=SensitiveWordSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="mseeage", property="mseeage", jdbcType=JdbcType.VARCHAR),
        @Result(column="lable", property="lable", jdbcType=JdbcType.INTEGER),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP)
    })
    List<SensitiveWord> selectByExample(SensitiveWordExample example);

    @Select({
        "select",
        "id, mseeage, lable, ctime, utime, ext_data",
        "from sensitive_word",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="mseeage", property="mseeage", jdbcType=JdbcType.VARCHAR),
        @Result(column="lable", property="lable", jdbcType=JdbcType.INTEGER),
        @Result(column="ctime", property="ctime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="utime", property="utime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="ext_data", property="extData", jdbcType=JdbcType.LONGVARCHAR)
    })
    SensitiveWord selectByPrimaryKey(Long id);

    @UpdateProvider(type=SensitiveWordSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(SensitiveWord record);

    @Update({
        "update sensitive_word",
        "set mseeage = #{mseeage,jdbcType=VARCHAR},",
          "lable = #{lable,jdbcType=INTEGER},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP},",
          "ext_data = #{extData,jdbcType=LONGVARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKeyWithBLOBs(SensitiveWord record);

    @Update({
        "update sensitive_word",
        "set mseeage = #{mseeage,jdbcType=VARCHAR},",
          "lable = #{lable,jdbcType=INTEGER},",
          "ctime = #{ctime,jdbcType=TIMESTAMP},",
          "utime = #{utime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(SensitiveWord record);
}