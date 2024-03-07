package com.king.db.dao;

import com.king.db.pojo.BotMessageRecord;
import com.king.db.pojo.BotMessageRecordExample.Criteria;
import com.king.db.pojo.BotMessageRecordExample.Criterion;
import com.king.db.pojo.BotMessageRecordExample;
import java.util.List;
import org.apache.ibatis.jdbc.SQL;

public class BotMessageRecordSqlProvider {
    public String insertSelective(BotMessageRecord record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("bot_message_record");
        
        if (record.getGroupId() != null) {
            sql.VALUES("group_id", "#{groupId,jdbcType=BIGINT}");
        }
        
        if (record.getQqId() != null) {
            sql.VALUES("qq_id", "#{qqId,jdbcType=BIGINT}");
        }
        
        if (record.getSubId() != null) {
            sql.VALUES("sub_id", "#{subId,jdbcType=BIGINT}");
        }
        
        if (record.getSingleId() != null) {
            sql.VALUES("single_id", "#{singleId,jdbcType=INTEGER}");
        }
        
        if (record.getIds() != null) {
            sql.VALUES("ids", "#{ids,jdbcType=VARCHAR}");
        }
        
        if (record.getDate() != null) {
            sql.VALUES("date", "#{date,jdbcType=VARCHAR}");
        }
        
        if (record.getDateTime() != null) {
            sql.VALUES("date_time", "#{dateTime,jdbcType=VARCHAR}");
        }
        
        if (record.getMessageMiraiCode() != null) {
            sql.VALUES("message_mirai_code", "#{messageMiraiCode,jdbcType=VARCHAR}");
        }
        
        if (record.getBotName() != null) {
            sql.VALUES("bot_name", "#{botName,jdbcType=VARCHAR}");
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
        
        if (record.getQuery() != null) {
            sql.VALUES("query", "#{query,jdbcType=LONGVARCHAR}");
        }
        
        if (record.getAnswer() != null) {
            sql.VALUES("answer", "#{answer,jdbcType=LONGVARCHAR}");
        }
        
        return sql.toString();
    }

    public String selectByExampleWithBLOBs(BotMessageRecordExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("group_id");
        } else {
            sql.SELECT("group_id");
        }
        sql.SELECT("qq_id");
        sql.SELECT("sub_id");
        sql.SELECT("single_id");
        sql.SELECT("ids");
        sql.SELECT("date");
        sql.SELECT("date_time");
        sql.SELECT("message_mirai_code");
        sql.SELECT("bot_name");
        sql.SELECT("ctime");
        sql.SELECT("utime");
        sql.SELECT("enable");
        sql.SELECT("query");
        sql.SELECT("answer");
        sql.FROM("bot_message_record");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String selectByExample(BotMessageRecordExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("group_id");
        } else {
            sql.SELECT("group_id");
        }
        sql.SELECT("qq_id");
        sql.SELECT("sub_id");
        sql.SELECT("single_id");
        sql.SELECT("ids");
        sql.SELECT("date");
        sql.SELECT("date_time");
        sql.SELECT("message_mirai_code");
        sql.SELECT("bot_name");
        sql.SELECT("ctime");
        sql.SELECT("utime");
        sql.SELECT("enable");
        sql.FROM("bot_message_record");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(BotMessageRecord record) {
        SQL sql = new SQL();
        sql.UPDATE("bot_message_record");
        
        if (record.getSingleId() != null) {
            sql.SET("single_id = #{singleId,jdbcType=INTEGER}");
        }
        
        if (record.getIds() != null) {
            sql.SET("ids = #{ids,jdbcType=VARCHAR}");
        }
        
        if (record.getDate() != null) {
            sql.SET("date = #{date,jdbcType=VARCHAR}");
        }
        
        if (record.getDateTime() != null) {
            sql.SET("date_time = #{dateTime,jdbcType=VARCHAR}");
        }
        
        if (record.getMessageMiraiCode() != null) {
            sql.SET("message_mirai_code = #{messageMiraiCode,jdbcType=VARCHAR}");
        }
        
        if (record.getBotName() != null) {
            sql.SET("bot_name = #{botName,jdbcType=VARCHAR}");
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
        
        if (record.getQuery() != null) {
            sql.SET("query = #{query,jdbcType=LONGVARCHAR}");
        }
        
        if (record.getAnswer() != null) {
            sql.SET("answer = #{answer,jdbcType=LONGVARCHAR}");
        }
        
        sql.WHERE("group_id = #{groupId,jdbcType=BIGINT}");
        sql.WHERE("qq_id = #{qqId,jdbcType=BIGINT}");
        sql.WHERE("sub_id = #{subId,jdbcType=BIGINT}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, BotMessageRecordExample example, boolean includeExamplePhrase) {
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