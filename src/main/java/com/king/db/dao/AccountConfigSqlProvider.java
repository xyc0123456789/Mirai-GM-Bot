package com.king.db.dao;

import com.king.db.pojo.AccountConfig;
import com.king.db.pojo.AccountConfigExample.Criteria;
import com.king.db.pojo.AccountConfigExample.Criterion;
import com.king.db.pojo.AccountConfigExample;
import java.util.List;
import org.apache.ibatis.jdbc.SQL;

public class AccountConfigSqlProvider {
    public String insertSelective(AccountConfig record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("account_config");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=INTEGER}");
        }
        
        if (record.getAccount() != null) {
            sql.VALUES("account", "#{account,jdbcType=BIGINT}");
        }
        
        if (record.getPassword() != null) {
            sql.VALUES("password", "#{password,jdbcType=VARCHAR}");
        }
        
        if (record.getHeartBeatStrategy() != null) {
            sql.VALUES("heart_beat_strategy", "#{heartBeatStrategy,jdbcType=VARCHAR}");
        }
        
        if (record.getProtocol() != null) {
            sql.VALUES("protocol", "#{protocol,jdbcType=VARCHAR}");
        }
        
        if (record.getWorkingDir() != null) {
            sql.VALUES("working_dir", "#{workingDir,jdbcType=VARCHAR}");
        }
        
        if (record.getCacheDirName() != null) {
            sql.VALUES("cache_dir_name", "#{cacheDirName,jdbcType=VARCHAR}");
        }
        
        if (record.getDeviceInfoFileName() != null) {
            sql.VALUES("device_info_file_name", "#{deviceInfoFileName,jdbcType=VARCHAR}");
        }
        
        if (record.getSensitiveWordDirName() != null) {
            sql.VALUES("sensitive_word_dir_name", "#{sensitiveWordDirName,jdbcType=VARCHAR}");
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

    public String selectByExample(AccountConfigExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("id");
        } else {
            sql.SELECT("id");
        }
        sql.SELECT("account");
        sql.SELECT("password");
        sql.SELECT("heart_beat_strategy");
        sql.SELECT("protocol");
        sql.SELECT("working_dir");
        sql.SELECT("cache_dir_name");
        sql.SELECT("device_info_file_name");
        sql.SELECT("sensitive_word_dir_name");
        sql.SELECT("ctime");
        sql.SELECT("utime");
        sql.SELECT("enable");
        sql.FROM("account_config");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(AccountConfig record) {
        SQL sql = new SQL();
        sql.UPDATE("account_config");
        
        if (record.getAccount() != null) {
            sql.SET("account = #{account,jdbcType=BIGINT}");
        }
        
        if (record.getPassword() != null) {
            sql.SET("password = #{password,jdbcType=VARCHAR}");
        }
        
        if (record.getHeartBeatStrategy() != null) {
            sql.SET("heart_beat_strategy = #{heartBeatStrategy,jdbcType=VARCHAR}");
        }
        
        if (record.getProtocol() != null) {
            sql.SET("protocol = #{protocol,jdbcType=VARCHAR}");
        }
        
        if (record.getWorkingDir() != null) {
            sql.SET("working_dir = #{workingDir,jdbcType=VARCHAR}");
        }
        
        if (record.getCacheDirName() != null) {
            sql.SET("cache_dir_name = #{cacheDirName,jdbcType=VARCHAR}");
        }
        
        if (record.getDeviceInfoFileName() != null) {
            sql.SET("device_info_file_name = #{deviceInfoFileName,jdbcType=VARCHAR}");
        }
        
        if (record.getSensitiveWordDirName() != null) {
            sql.SET("sensitive_word_dir_name = #{sensitiveWordDirName,jdbcType=VARCHAR}");
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
        
        sql.WHERE("id = #{id,jdbcType=INTEGER}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, AccountConfigExample example, boolean includeExamplePhrase) {
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