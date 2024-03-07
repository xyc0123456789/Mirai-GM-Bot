package com.king.db.dao;

import com.king.db.pojo.OpenAIApiKey;
import com.king.db.pojo.OpenAIApiKeyExample.Criteria;
import com.king.db.pojo.OpenAIApiKeyExample.Criterion;
import com.king.db.pojo.OpenAIApiKeyExample;
import java.util.List;
import org.apache.ibatis.jdbc.SQL;

public class OpenAIApiKeySqlProvider {
    public String insertSelective(OpenAIApiKey record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("open_ai_api_key");
        
        if (record.getApiKey() != null) {
            sql.VALUES("api_key", "#{apiKey,jdbcType=VARCHAR}");
        }
        
        if (record.getRateLimit() != null) {
            sql.VALUES("rate_limit", "#{rateLimit,jdbcType=BIGINT}");
        }
        
        if (record.getHardLimitUsd() != null) {
            sql.VALUES("hard_limit_usd", "#{hardLimitUsd,jdbcType=VARCHAR}");
        }
        
        if (record.getTotalUsage() != null) {
            sql.VALUES("total_usage", "#{totalUsage,jdbcType=VARCHAR}");
        }
        
        return sql.toString();
    }

    public String selectByExample(OpenAIApiKeyExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("api_key");
        } else {
            sql.SELECT("api_key");
        }
        sql.SELECT("rate_limit");
        sql.SELECT("hard_limit_usd");
        sql.SELECT("total_usage");
        sql.FROM("open_ai_api_key");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(OpenAIApiKey record) {
        SQL sql = new SQL();
        sql.UPDATE("open_ai_api_key");
        
        if (record.getRateLimit() != null) {
            sql.SET("rate_limit = #{rateLimit,jdbcType=BIGINT}");
        }
        
        if (record.getHardLimitUsd() != null) {
            sql.SET("hard_limit_usd = #{hardLimitUsd,jdbcType=VARCHAR}");
        }
        
        if (record.getTotalUsage() != null) {
            sql.SET("total_usage = #{totalUsage,jdbcType=VARCHAR}");
        }
        
        sql.WHERE("api_key = #{apiKey,jdbcType=VARCHAR}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, OpenAIApiKeyExample example, boolean includeExamplePhrase) {
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