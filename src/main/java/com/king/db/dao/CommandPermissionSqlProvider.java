package com.king.db.dao;

import com.king.db.pojo.CommandPermission;
import com.king.db.pojo.CommandPermissionExample.Criteria;
import com.king.db.pojo.CommandPermissionExample.Criterion;
import com.king.db.pojo.CommandPermissionExample;
import java.util.List;
import org.apache.ibatis.jdbc.SQL;

public class CommandPermissionSqlProvider {
    public String insertSelective(CommandPermission record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("command_permission");
        
        if (record.getContactType() != null) {
            sql.VALUES("contact_type", "#{contactType,jdbcType=INTEGER}");
        }
        
        if (record.getContactId() != null) {
            sql.VALUES("contact_id", "#{contactId,jdbcType=BIGINT}");
        }
        
        if (record.getSubContactId() != null) {
            sql.VALUES("sub_contact_id", "#{subContactId,jdbcType=BIGINT}");
        }
        
        if (record.getCommandName() != null) {
            sql.VALUES("command_name", "#{commandName,jdbcType=VARCHAR}");
        }
        
        if (record.getCommandDesc() != null) {
            sql.VALUES("command_desc", "#{commandDesc,jdbcType=VARCHAR}");
        }
        
        if (record.getRemark1() != null) {
            sql.VALUES("remark1", "#{remark1,jdbcType=VARCHAR}");
        }
        
        if (record.getRemark2() != null) {
            sql.VALUES("remark2", "#{remark2,jdbcType=VARCHAR}");
        }
        
        if (record.getRemark3() != null) {
            sql.VALUES("remark3", "#{remark3,jdbcType=VARCHAR}");
        }
        
        if (record.getExtData() != null) {
            sql.VALUES("ext_data", "#{extData,jdbcType=VARCHAR}");
        }
        
        if (record.getIsOpen() != null) {
            sql.VALUES("is_open", "#{isOpen,jdbcType=BIT}");
        }
        
        if (record.getCtime() != null) {
            sql.VALUES("ctime", "#{ctime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUtime() != null) {
            sql.VALUES("utime", "#{utime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getEnable() != null) {
            sql.VALUES("enable", "#{enable,jdbcType=BIT}");
        }
        
        return sql.toString();
    }

    public String selectByExample(CommandPermissionExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("contact_type");
        } else {
            sql.SELECT("contact_type");
        }
        sql.SELECT("contact_id");
        sql.SELECT("sub_contact_id");
        sql.SELECT("command_name");
        sql.SELECT("command_desc");
        sql.SELECT("remark1");
        sql.SELECT("remark2");
        sql.SELECT("remark3");
        sql.SELECT("ext_data");
        sql.SELECT("is_open");
        sql.SELECT("ctime");
        sql.SELECT("utime");
        sql.SELECT("enable");
        sql.FROM("command_permission");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(CommandPermission record) {
        SQL sql = new SQL();
        sql.UPDATE("command_permission");
        
        if (record.getCommandDesc() != null) {
            sql.SET("command_desc = #{commandDesc,jdbcType=VARCHAR}");
        }
        
        if (record.getRemark1() != null) {
            sql.SET("remark1 = #{remark1,jdbcType=VARCHAR}");
        }
        
        if (record.getRemark2() != null) {
            sql.SET("remark2 = #{remark2,jdbcType=VARCHAR}");
        }
        
        if (record.getRemark3() != null) {
            sql.SET("remark3 = #{remark3,jdbcType=VARCHAR}");
        }
        
        if (record.getExtData() != null) {
            sql.SET("ext_data = #{extData,jdbcType=VARCHAR}");
        }
        
        if (record.getIsOpen() != null) {
            sql.SET("is_open = #{isOpen,jdbcType=BIT}");
        }
        
        if (record.getCtime() != null) {
            sql.SET("ctime = #{ctime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUtime() != null) {
            sql.SET("utime = #{utime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getEnable() != null) {
            sql.SET("enable = #{enable,jdbcType=BIT}");
        }
        
        sql.WHERE("contact_type = #{contactType,jdbcType=INTEGER}");
        sql.WHERE("contact_id = #{contactId,jdbcType=BIGINT}");
        sql.WHERE("sub_contact_id = #{subContactId,jdbcType=BIGINT}");
        sql.WHERE("command_name = #{commandName,jdbcType=VARCHAR}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, CommandPermissionExample example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        
        if (sb.length() > 0) {
            sql.WHERE(sb.toString());
        }
    }
}