package com.king.db.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountConfigExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AccountConfigExample() {
        oredCriteria = new ArrayList<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
    account_config
     *
     * @mbg.generated Mon Oct 09 14:19:28 CST 2023
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andAccountIsNull() {
            addCriterion("account is null");
            return (Criteria) this;
        }

        public Criteria andAccountIsNotNull() {
            addCriterion("account is not null");
            return (Criteria) this;
        }

        public Criteria andAccountEqualTo(Long value) {
            addCriterion("account =", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotEqualTo(Long value) {
            addCriterion("account <>", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountGreaterThan(Long value) {
            addCriterion("account >", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountGreaterThanOrEqualTo(Long value) {
            addCriterion("account >=", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLessThan(Long value) {
            addCriterion("account <", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountLessThanOrEqualTo(Long value) {
            addCriterion("account <=", value, "account");
            return (Criteria) this;
        }

        public Criteria andAccountIn(List<Long> values) {
            addCriterion("account in", values, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotIn(List<Long> values) {
            addCriterion("account not in", values, "account");
            return (Criteria) this;
        }

        public Criteria andAccountBetween(Long value1, Long value2) {
            addCriterion("account between", value1, value2, "account");
            return (Criteria) this;
        }

        public Criteria andAccountNotBetween(Long value1, Long value2) {
            addCriterion("account not between", value1, value2, "account");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNull() {
            addCriterion("password is null");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNotNull() {
            addCriterion("password is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordEqualTo(String value) {
            addCriterion("password =", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotEqualTo(String value) {
            addCriterion("password <>", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThan(String value) {
            addCriterion("password >", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("password >=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThan(String value) {
            addCriterion("password <", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThanOrEqualTo(String value) {
            addCriterion("password <=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLike(String value) {
            addCriterion("password like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotLike(String value) {
            addCriterion("password not like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordIn(List<String> values) {
            addCriterion("password in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotIn(List<String> values) {
            addCriterion("password not in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordBetween(String value1, String value2) {
            addCriterion("password between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotBetween(String value1, String value2) {
            addCriterion("password not between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andHeartBeatStrategyIsNull() {
            addCriterion("heart_beat_strategy is null");
            return (Criteria) this;
        }

        public Criteria andHeartBeatStrategyIsNotNull() {
            addCriterion("heart_beat_strategy is not null");
            return (Criteria) this;
        }

        public Criteria andHeartBeatStrategyEqualTo(String value) {
            addCriterion("heart_beat_strategy =", value, "heartBeatStrategy");
            return (Criteria) this;
        }

        public Criteria andHeartBeatStrategyNotEqualTo(String value) {
            addCriterion("heart_beat_strategy <>", value, "heartBeatStrategy");
            return (Criteria) this;
        }

        public Criteria andHeartBeatStrategyGreaterThan(String value) {
            addCriterion("heart_beat_strategy >", value, "heartBeatStrategy");
            return (Criteria) this;
        }

        public Criteria andHeartBeatStrategyGreaterThanOrEqualTo(String value) {
            addCriterion("heart_beat_strategy >=", value, "heartBeatStrategy");
            return (Criteria) this;
        }

        public Criteria andHeartBeatStrategyLessThan(String value) {
            addCriterion("heart_beat_strategy <", value, "heartBeatStrategy");
            return (Criteria) this;
        }

        public Criteria andHeartBeatStrategyLessThanOrEqualTo(String value) {
            addCriterion("heart_beat_strategy <=", value, "heartBeatStrategy");
            return (Criteria) this;
        }

        public Criteria andHeartBeatStrategyLike(String value) {
            addCriterion("heart_beat_strategy like", value, "heartBeatStrategy");
            return (Criteria) this;
        }

        public Criteria andHeartBeatStrategyNotLike(String value) {
            addCriterion("heart_beat_strategy not like", value, "heartBeatStrategy");
            return (Criteria) this;
        }

        public Criteria andHeartBeatStrategyIn(List<String> values) {
            addCriterion("heart_beat_strategy in", values, "heartBeatStrategy");
            return (Criteria) this;
        }

        public Criteria andHeartBeatStrategyNotIn(List<String> values) {
            addCriterion("heart_beat_strategy not in", values, "heartBeatStrategy");
            return (Criteria) this;
        }

        public Criteria andHeartBeatStrategyBetween(String value1, String value2) {
            addCriterion("heart_beat_strategy between", value1, value2, "heartBeatStrategy");
            return (Criteria) this;
        }

        public Criteria andHeartBeatStrategyNotBetween(String value1, String value2) {
            addCriterion("heart_beat_strategy not between", value1, value2, "heartBeatStrategy");
            return (Criteria) this;
        }

        public Criteria andProtocolIsNull() {
            addCriterion("protocol is null");
            return (Criteria) this;
        }

        public Criteria andProtocolIsNotNull() {
            addCriterion("protocol is not null");
            return (Criteria) this;
        }

        public Criteria andProtocolEqualTo(String value) {
            addCriterion("protocol =", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolNotEqualTo(String value) {
            addCriterion("protocol <>", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolGreaterThan(String value) {
            addCriterion("protocol >", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolGreaterThanOrEqualTo(String value) {
            addCriterion("protocol >=", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolLessThan(String value) {
            addCriterion("protocol <", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolLessThanOrEqualTo(String value) {
            addCriterion("protocol <=", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolLike(String value) {
            addCriterion("protocol like", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolNotLike(String value) {
            addCriterion("protocol not like", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolIn(List<String> values) {
            addCriterion("protocol in", values, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolNotIn(List<String> values) {
            addCriterion("protocol not in", values, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolBetween(String value1, String value2) {
            addCriterion("protocol between", value1, value2, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolNotBetween(String value1, String value2) {
            addCriterion("protocol not between", value1, value2, "protocol");
            return (Criteria) this;
        }

        public Criteria andWorkingDirIsNull() {
            addCriterion("working_dir is null");
            return (Criteria) this;
        }

        public Criteria andWorkingDirIsNotNull() {
            addCriterion("working_dir is not null");
            return (Criteria) this;
        }

        public Criteria andWorkingDirEqualTo(String value) {
            addCriterion("working_dir =", value, "workingDir");
            return (Criteria) this;
        }

        public Criteria andWorkingDirNotEqualTo(String value) {
            addCriterion("working_dir <>", value, "workingDir");
            return (Criteria) this;
        }

        public Criteria andWorkingDirGreaterThan(String value) {
            addCriterion("working_dir >", value, "workingDir");
            return (Criteria) this;
        }

        public Criteria andWorkingDirGreaterThanOrEqualTo(String value) {
            addCriterion("working_dir >=", value, "workingDir");
            return (Criteria) this;
        }

        public Criteria andWorkingDirLessThan(String value) {
            addCriterion("working_dir <", value, "workingDir");
            return (Criteria) this;
        }

        public Criteria andWorkingDirLessThanOrEqualTo(String value) {
            addCriterion("working_dir <=", value, "workingDir");
            return (Criteria) this;
        }

        public Criteria andWorkingDirLike(String value) {
            addCriterion("working_dir like", value, "workingDir");
            return (Criteria) this;
        }

        public Criteria andWorkingDirNotLike(String value) {
            addCriterion("working_dir not like", value, "workingDir");
            return (Criteria) this;
        }

        public Criteria andWorkingDirIn(List<String> values) {
            addCriterion("working_dir in", values, "workingDir");
            return (Criteria) this;
        }

        public Criteria andWorkingDirNotIn(List<String> values) {
            addCriterion("working_dir not in", values, "workingDir");
            return (Criteria) this;
        }

        public Criteria andWorkingDirBetween(String value1, String value2) {
            addCriterion("working_dir between", value1, value2, "workingDir");
            return (Criteria) this;
        }

        public Criteria andWorkingDirNotBetween(String value1, String value2) {
            addCriterion("working_dir not between", value1, value2, "workingDir");
            return (Criteria) this;
        }

        public Criteria andCacheDirNameIsNull() {
            addCriterion("cache_dir_name is null");
            return (Criteria) this;
        }

        public Criteria andCacheDirNameIsNotNull() {
            addCriterion("cache_dir_name is not null");
            return (Criteria) this;
        }

        public Criteria andCacheDirNameEqualTo(String value) {
            addCriterion("cache_dir_name =", value, "cacheDirName");
            return (Criteria) this;
        }

        public Criteria andCacheDirNameNotEqualTo(String value) {
            addCriterion("cache_dir_name <>", value, "cacheDirName");
            return (Criteria) this;
        }

        public Criteria andCacheDirNameGreaterThan(String value) {
            addCriterion("cache_dir_name >", value, "cacheDirName");
            return (Criteria) this;
        }

        public Criteria andCacheDirNameGreaterThanOrEqualTo(String value) {
            addCriterion("cache_dir_name >=", value, "cacheDirName");
            return (Criteria) this;
        }

        public Criteria andCacheDirNameLessThan(String value) {
            addCriterion("cache_dir_name <", value, "cacheDirName");
            return (Criteria) this;
        }

        public Criteria andCacheDirNameLessThanOrEqualTo(String value) {
            addCriterion("cache_dir_name <=", value, "cacheDirName");
            return (Criteria) this;
        }

        public Criteria andCacheDirNameLike(String value) {
            addCriterion("cache_dir_name like", value, "cacheDirName");
            return (Criteria) this;
        }

        public Criteria andCacheDirNameNotLike(String value) {
            addCriterion("cache_dir_name not like", value, "cacheDirName");
            return (Criteria) this;
        }

        public Criteria andCacheDirNameIn(List<String> values) {
            addCriterion("cache_dir_name in", values, "cacheDirName");
            return (Criteria) this;
        }

        public Criteria andCacheDirNameNotIn(List<String> values) {
            addCriterion("cache_dir_name not in", values, "cacheDirName");
            return (Criteria) this;
        }

        public Criteria andCacheDirNameBetween(String value1, String value2) {
            addCriterion("cache_dir_name between", value1, value2, "cacheDirName");
            return (Criteria) this;
        }

        public Criteria andCacheDirNameNotBetween(String value1, String value2) {
            addCriterion("cache_dir_name not between", value1, value2, "cacheDirName");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoFileNameIsNull() {
            addCriterion("device_info_file_name is null");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoFileNameIsNotNull() {
            addCriterion("device_info_file_name is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoFileNameEqualTo(String value) {
            addCriterion("device_info_file_name =", value, "deviceInfoFileName");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoFileNameNotEqualTo(String value) {
            addCriterion("device_info_file_name <>", value, "deviceInfoFileName");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoFileNameGreaterThan(String value) {
            addCriterion("device_info_file_name >", value, "deviceInfoFileName");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoFileNameGreaterThanOrEqualTo(String value) {
            addCriterion("device_info_file_name >=", value, "deviceInfoFileName");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoFileNameLessThan(String value) {
            addCriterion("device_info_file_name <", value, "deviceInfoFileName");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoFileNameLessThanOrEqualTo(String value) {
            addCriterion("device_info_file_name <=", value, "deviceInfoFileName");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoFileNameLike(String value) {
            addCriterion("device_info_file_name like", value, "deviceInfoFileName");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoFileNameNotLike(String value) {
            addCriterion("device_info_file_name not like", value, "deviceInfoFileName");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoFileNameIn(List<String> values) {
            addCriterion("device_info_file_name in", values, "deviceInfoFileName");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoFileNameNotIn(List<String> values) {
            addCriterion("device_info_file_name not in", values, "deviceInfoFileName");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoFileNameBetween(String value1, String value2) {
            addCriterion("device_info_file_name between", value1, value2, "deviceInfoFileName");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoFileNameNotBetween(String value1, String value2) {
            addCriterion("device_info_file_name not between", value1, value2, "deviceInfoFileName");
            return (Criteria) this;
        }

        public Criteria andSensitiveWordDirNameIsNull() {
            addCriterion("sensitive_word_dir_name is null");
            return (Criteria) this;
        }

        public Criteria andSensitiveWordDirNameIsNotNull() {
            addCriterion("sensitive_word_dir_name is not null");
            return (Criteria) this;
        }

        public Criteria andSensitiveWordDirNameEqualTo(String value) {
            addCriterion("sensitive_word_dir_name =", value, "sensitiveWordDirName");
            return (Criteria) this;
        }

        public Criteria andSensitiveWordDirNameNotEqualTo(String value) {
            addCriterion("sensitive_word_dir_name <>", value, "sensitiveWordDirName");
            return (Criteria) this;
        }

        public Criteria andSensitiveWordDirNameGreaterThan(String value) {
            addCriterion("sensitive_word_dir_name >", value, "sensitiveWordDirName");
            return (Criteria) this;
        }

        public Criteria andSensitiveWordDirNameGreaterThanOrEqualTo(String value) {
            addCriterion("sensitive_word_dir_name >=", value, "sensitiveWordDirName");
            return (Criteria) this;
        }

        public Criteria andSensitiveWordDirNameLessThan(String value) {
            addCriterion("sensitive_word_dir_name <", value, "sensitiveWordDirName");
            return (Criteria) this;
        }

        public Criteria andSensitiveWordDirNameLessThanOrEqualTo(String value) {
            addCriterion("sensitive_word_dir_name <=", value, "sensitiveWordDirName");
            return (Criteria) this;
        }

        public Criteria andSensitiveWordDirNameLike(String value) {
            addCriterion("sensitive_word_dir_name like", value, "sensitiveWordDirName");
            return (Criteria) this;
        }

        public Criteria andSensitiveWordDirNameNotLike(String value) {
            addCriterion("sensitive_word_dir_name not like", value, "sensitiveWordDirName");
            return (Criteria) this;
        }

        public Criteria andSensitiveWordDirNameIn(List<String> values) {
            addCriterion("sensitive_word_dir_name in", values, "sensitiveWordDirName");
            return (Criteria) this;
        }

        public Criteria andSensitiveWordDirNameNotIn(List<String> values) {
            addCriterion("sensitive_word_dir_name not in", values, "sensitiveWordDirName");
            return (Criteria) this;
        }

        public Criteria andSensitiveWordDirNameBetween(String value1, String value2) {
            addCriterion("sensitive_word_dir_name between", value1, value2, "sensitiveWordDirName");
            return (Criteria) this;
        }

        public Criteria andSensitiveWordDirNameNotBetween(String value1, String value2) {
            addCriterion("sensitive_word_dir_name not between", value1, value2, "sensitiveWordDirName");
            return (Criteria) this;
        }

        public Criteria andCtimeIsNull() {
            addCriterion("ctime is null");
            return (Criteria) this;
        }

        public Criteria andCtimeIsNotNull() {
            addCriterion("ctime is not null");
            return (Criteria) this;
        }

        public Criteria andCtimeEqualTo(Date value) {
            addCriterion("ctime =", value, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeNotEqualTo(Date value) {
            addCriterion("ctime <>", value, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeGreaterThan(Date value) {
            addCriterion("ctime >", value, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeGreaterThanOrEqualTo(Date value) {
            addCriterion("ctime >=", value, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeLessThan(Date value) {
            addCriterion("ctime <", value, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeLessThanOrEqualTo(Date value) {
            addCriterion("ctime <=", value, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeIn(List<Date> values) {
            addCriterion("ctime in", values, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeNotIn(List<Date> values) {
            addCriterion("ctime not in", values, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeBetween(Date value1, Date value2) {
            addCriterion("ctime between", value1, value2, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeNotBetween(Date value1, Date value2) {
            addCriterion("ctime not between", value1, value2, "ctime");
            return (Criteria) this;
        }

        public Criteria andUtimeIsNull() {
            addCriterion("utime is null");
            return (Criteria) this;
        }

        public Criteria andUtimeIsNotNull() {
            addCriterion("utime is not null");
            return (Criteria) this;
        }

        public Criteria andUtimeEqualTo(Date value) {
            addCriterion("utime =", value, "utime");
            return (Criteria) this;
        }

        public Criteria andUtimeNotEqualTo(Date value) {
            addCriterion("utime <>", value, "utime");
            return (Criteria) this;
        }

        public Criteria andUtimeGreaterThan(Date value) {
            addCriterion("utime >", value, "utime");
            return (Criteria) this;
        }

        public Criteria andUtimeGreaterThanOrEqualTo(Date value) {
            addCriterion("utime >=", value, "utime");
            return (Criteria) this;
        }

        public Criteria andUtimeLessThan(Date value) {
            addCriterion("utime <", value, "utime");
            return (Criteria) this;
        }

        public Criteria andUtimeLessThanOrEqualTo(Date value) {
            addCriterion("utime <=", value, "utime");
            return (Criteria) this;
        }

        public Criteria andUtimeIn(List<Date> values) {
            addCriterion("utime in", values, "utime");
            return (Criteria) this;
        }

        public Criteria andUtimeNotIn(List<Date> values) {
            addCriterion("utime not in", values, "utime");
            return (Criteria) this;
        }

        public Criteria andUtimeBetween(Date value1, Date value2) {
            addCriterion("utime between", value1, value2, "utime");
            return (Criteria) this;
        }

        public Criteria andUtimeNotBetween(Date value1, Date value2) {
            addCriterion("utime not between", value1, value2, "utime");
            return (Criteria) this;
        }

        public Criteria andEnableIsNull() {
            addCriterion("enable is null");
            return (Criteria) this;
        }

        public Criteria andEnableIsNotNull() {
            addCriterion("enable is not null");
            return (Criteria) this;
        }

        public Criteria andEnableEqualTo(Boolean value) {
            addCriterion("enable =", value, "enable");
            return (Criteria) this;
        }

        public Criteria andEnableNotEqualTo(Boolean value) {
            addCriterion("enable <>", value, "enable");
            return (Criteria) this;
        }

        public Criteria andEnableGreaterThan(Boolean value) {
            addCriterion("enable >", value, "enable");
            return (Criteria) this;
        }

        public Criteria andEnableGreaterThanOrEqualTo(Boolean value) {
            addCriterion("enable >=", value, "enable");
            return (Criteria) this;
        }

        public Criteria andEnableLessThan(Boolean value) {
            addCriterion("enable <", value, "enable");
            return (Criteria) this;
        }

        public Criteria andEnableLessThanOrEqualTo(Boolean value) {
            addCriterion("enable <=", value, "enable");
            return (Criteria) this;
        }

        public Criteria andEnableIn(List<Boolean> values) {
            addCriterion("enable in", values, "enable");
            return (Criteria) this;
        }

        public Criteria andEnableNotIn(List<Boolean> values) {
            addCriterion("enable not in", values, "enable");
            return (Criteria) this;
        }

        public Criteria andEnableBetween(Boolean value1, Boolean value2) {
            addCriterion("enable between", value1, value2, "enable");
            return (Criteria) this;
        }

        public Criteria andEnableNotBetween(Boolean value1, Boolean value2) {
            addCriterion("enable not between", value1, value2, "enable");
            return (Criteria) this;
        }
    }

    /**
    account_config
     *
     * @mbg.generated do_not_delete_during_merge Mon Oct 09 14:19:28 CST 2023
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    /**
    account_config
     *
     * @mbg.generated Mon Oct 09 14:19:28 CST 2023
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}