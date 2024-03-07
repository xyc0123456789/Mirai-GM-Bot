package com.king.db.dao;

import com.king.db.pojo.ContactListenList;
import com.king.db.pojo.ContactListenListExample.Criteria;
import com.king.db.pojo.ContactListenListExample.Criterion;
import com.king.db.pojo.ContactListenListExample;
import java.util.List;
import org.apache.ibatis.jdbc.SQL;

public class ContactListenListSqlProvider {
    public String insertSelective(ContactListenList record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("contact_listen_list");
        
        if (record.getContactType() != null) {
            sql.VALUES("contact_type", "#{contactType,jdbcType=INTEGER}");
        }
        
        if (record.getContactId() != null) {
            sql.VALUES("contact_id", "#{contactId,jdbcType=BIGINT}");
        }
        
        if (record.getRemark() != null) {
            sql.VALUES("remark", "#{remark,jdbcType=VARCHAR}");
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
        
        if (record.getPermission() != null) {
            sql.VALUES("permission", "#{permission,jdbcType=LONGVARCHAR}");
        }
        
        return sql.toString();
    }

    public String selectByExampleWithBLOBs(ContactListenListExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("contact_type");
        } else {
            sql.SELECT("contact_type");
        }
        sql.SELECT("contact_id");
        sql.SELECT("remark");
        sql.SELECT("ctime");
        sql.SELECT("utime");
        sql.SELECT("enable");
        sql.SELECT("permission");
        sql.FROM("contact_listen_list");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String selectByExample(ContactListenListExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("contact_type");
        } else {
            sql.SELECT("contact_type");
        }
        sql.SELECT("contact_id");
        sql.SELECT("remark");
        sql.SELECT("ctime");
        sql.SELECT("utime");
        sql.SELECT("enable");
        sql.FROM("contact_listen_list");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(ContactListenList record) {
        SQL sql = new SQL();
        sql.UPDATE("contact_listen_list");
        
        if (record.getRemark() != null) {
            sql.SET("remark = #{remark,jdbcType=VARCHAR}");
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
        
        if (record.getPermission() != null) {
            sql.SET("permission = #{permission,jdbcType=LONGVARCHAR}");
        }
        
        sql.WHERE("contact_type = #{contactType,jdbcType=INTEGER}");
        sql.WHERE("contact_id = #{contactId,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, ContactListenListExample example, boolean includeExamplePhrase) {
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