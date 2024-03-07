package com.king.db.subdao;

import com.king.db.dao.GroupSpecialListMapper;
import com.king.db.pojo.GroupSpecialList;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface SubGroupSpecialListMapper extends GroupSpecialListMapper {

}