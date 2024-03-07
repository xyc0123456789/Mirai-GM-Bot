package com.king.db.dao;

import com.king.db.pojo.TempGroupSpecialList;
import com.king.db.pojo.TempGroupSpecialListExample.Criteria;
import com.king.db.pojo.TempGroupSpecialListExample.Criterion;
import com.king.db.pojo.TempGroupSpecialListExample;
import java.util.List;
import org.apache.ibatis.jdbc.SQL;

public class TempGroupSpecialListSqlProvider {
    public String insertSelective(TempGroupSpecialList record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("temp_group_special_list");
        
        if (record.getGroupId() != null) {
            sql.VALUES("group_id", "#{groupId,jdbcType=BIGINT}");
        }
        
        if (record.getContactId() != null) {
            sql.VALUES("contact_id", "#{contactId,jdbcType=BIGINT}");
        }
        
        if (record.getNickName() != null) {
            sql.VALUES("nick_name", "#{nickName,jdbcType=VARCHAR}");
        }
        
        if (record.getCardName() != null) {
            sql.VALUES("card_name", "#{cardName,jdbcType=VARCHAR}");
        }
        
        if (record.getSpecialTitle() != null) {
            sql.VALUES("special_title", "#{specialTitle,jdbcType=VARCHAR}");
        }
        
        if (record.getPermissionLevel() != null) {
            sql.VALUES("permission_level", "#{permissionLevel,jdbcType=BIGINT}");
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
        
        if (record.getExtData() != null) {
            sql.VALUES("ext_data", "#{extData,jdbcType=LONGVARCHAR}");
        }
        
        return sql.toString();
    }

    public String selectByExampleWithBLOBs(TempGroupSpecialListExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("group_id");
        } else {
            sql.SELECT("group_id");
        }
        sql.SELECT("contact_id");
        sql.SELECT("nick_name");
        sql.SELECT("card_name");
        sql.SELECT("special_title");
        sql.SELECT("permission_level");
        sql.SELECT("ctime");
        sql.SELECT("utime");
        sql.SELECT("enable");
        sql.SELECT("ext_data");
        sql.FROM("temp_group_special_list");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String selectByExample(TempGroupSpecialListExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("group_id");
        } else {
            sql.SELECT("group_id");
        }
        sql.SELECT("contact_id");
        sql.SELECT("nick_name");
        sql.SELECT("card_name");
        sql.SELECT("special_title");
        sql.SELECT("permission_level");
        sql.SELECT("ctime");
        sql.SELECT("utime");
        sql.SELECT("enable");
        sql.FROM("temp_group_special_list");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(TempGroupSpecialList record) {
        SQL sql = new SQL();
        sql.UPDATE("temp_group_special_list");
        
        if (record.getNickName() != null) {
            sql.SET("nick_name = #{nickName,jdbcType=VARCHAR}");
        }
        
        if (record.getCardName() != null) {
            sql.SET("card_name = #{cardName,jdbcType=VARCHAR}");
        }
        
        if (record.getSpecialTitle() != null) {
            sql.SET("special_title = #{specialTitle,jdbcType=VARCHAR}");
        }
        
        if (record.getPermissionLevel() != null) {
            sql.SET("permission_level = #{permissionLevel,jdbcType=BIGINT}");
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
        
        if (record.getExtData() != null) {
            sql.SET("ext_data = #{extData,jdbcType=LONGVARCHAR}");
        }
        
        sql.WHERE("group_id = #{groupId,jdbcType=BIGINT}");
        sql.WHERE("contact_id = #{contactId,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, TempGroupSpecialListExample example, boolean includeExamplePhrase) {
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