package com.king.db.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BotMessageRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BotMessageRecordExample() {
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
    bot_message_record
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

        public Criteria andGroupIdIsNull() {
            addCriterion("group_id is null");
            return (Criteria) this;
        }

        public Criteria andGroupIdIsNotNull() {
            addCriterion("group_id is not null");
            return (Criteria) this;
        }

        public Criteria andGroupIdEqualTo(Long value) {
            addCriterion("group_id =", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotEqualTo(Long value) {
            addCriterion("group_id <>", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdGreaterThan(Long value) {
            addCriterion("group_id >", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdGreaterThanOrEqualTo(Long value) {
            addCriterion("group_id >=", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdLessThan(Long value) {
            addCriterion("group_id <", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdLessThanOrEqualTo(Long value) {
            addCriterion("group_id <=", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdIn(List<Long> values) {
            addCriterion("group_id in", values, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotIn(List<Long> values) {
            addCriterion("group_id not in", values, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdBetween(Long value1, Long value2) {
            addCriterion("group_id between", value1, value2, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotBetween(Long value1, Long value2) {
            addCriterion("group_id not between", value1, value2, "groupId");
            return (Criteria) this;
        }

        public Criteria andQqIdIsNull() {
            addCriterion("qq_id is null");
            return (Criteria) this;
        }

        public Criteria andQqIdIsNotNull() {
            addCriterion("qq_id is not null");
            return (Criteria) this;
        }

        public Criteria andQqIdEqualTo(Long value) {
            addCriterion("qq_id =", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdNotEqualTo(Long value) {
            addCriterion("qq_id <>", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdGreaterThan(Long value) {
            addCriterion("qq_id >", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdGreaterThanOrEqualTo(Long value) {
            addCriterion("qq_id >=", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdLessThan(Long value) {
            addCriterion("qq_id <", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdLessThanOrEqualTo(Long value) {
            addCriterion("qq_id <=", value, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdIn(List<Long> values) {
            addCriterion("qq_id in", values, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdNotIn(List<Long> values) {
            addCriterion("qq_id not in", values, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdBetween(Long value1, Long value2) {
            addCriterion("qq_id between", value1, value2, "qqId");
            return (Criteria) this;
        }

        public Criteria andQqIdNotBetween(Long value1, Long value2) {
            addCriterion("qq_id not between", value1, value2, "qqId");
            return (Criteria) this;
        }

        public Criteria andSubIdIsNull() {
            addCriterion("sub_id is null");
            return (Criteria) this;
        }

        public Criteria andSubIdIsNotNull() {
            addCriterion("sub_id is not null");
            return (Criteria) this;
        }

        public Criteria andSubIdEqualTo(Long value) {
            addCriterion("sub_id =", value, "subId");
            return (Criteria) this;
        }

        public Criteria andSubIdNotEqualTo(Long value) {
            addCriterion("sub_id <>", value, "subId");
            return (Criteria) this;
        }

        public Criteria andSubIdGreaterThan(Long value) {
            addCriterion("sub_id >", value, "subId");
            return (Criteria) this;
        }

        public Criteria andSubIdGreaterThanOrEqualTo(Long value) {
            addCriterion("sub_id >=", value, "subId");
            return (Criteria) this;
        }

        public Criteria andSubIdLessThan(Long value) {
            addCriterion("sub_id <", value, "subId");
            return (Criteria) this;
        }

        public Criteria andSubIdLessThanOrEqualTo(Long value) {
            addCriterion("sub_id <=", value, "subId");
            return (Criteria) this;
        }

        public Criteria andSubIdIn(List<Long> values) {
            addCriterion("sub_id in", values, "subId");
            return (Criteria) this;
        }

        public Criteria andSubIdNotIn(List<Long> values) {
            addCriterion("sub_id not in", values, "subId");
            return (Criteria) this;
        }

        public Criteria andSubIdBetween(Long value1, Long value2) {
            addCriterion("sub_id between", value1, value2, "subId");
            return (Criteria) this;
        }

        public Criteria andSubIdNotBetween(Long value1, Long value2) {
            addCriterion("sub_id not between", value1, value2, "subId");
            return (Criteria) this;
        }

        public Criteria andSingleIdIsNull() {
            addCriterion("single_id is null");
            return (Criteria) this;
        }

        public Criteria andSingleIdIsNotNull() {
            addCriterion("single_id is not null");
            return (Criteria) this;
        }

        public Criteria andSingleIdEqualTo(Integer value) {
            addCriterion("single_id =", value, "singleId");
            return (Criteria) this;
        }

        public Criteria andSingleIdNotEqualTo(Integer value) {
            addCriterion("single_id <>", value, "singleId");
            return (Criteria) this;
        }

        public Criteria andSingleIdGreaterThan(Integer value) {
            addCriterion("single_id >", value, "singleId");
            return (Criteria) this;
        }

        public Criteria andSingleIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("single_id >=", value, "singleId");
            return (Criteria) this;
        }

        public Criteria andSingleIdLessThan(Integer value) {
            addCriterion("single_id <", value, "singleId");
            return (Criteria) this;
        }

        public Criteria andSingleIdLessThanOrEqualTo(Integer value) {
            addCriterion("single_id <=", value, "singleId");
            return (Criteria) this;
        }

        public Criteria andSingleIdIn(List<Integer> values) {
            addCriterion("single_id in", values, "singleId");
            return (Criteria) this;
        }

        public Criteria andSingleIdNotIn(List<Integer> values) {
            addCriterion("single_id not in", values, "singleId");
            return (Criteria) this;
        }

        public Criteria andSingleIdBetween(Integer value1, Integer value2) {
            addCriterion("single_id between", value1, value2, "singleId");
            return (Criteria) this;
        }

        public Criteria andSingleIdNotBetween(Integer value1, Integer value2) {
            addCriterion("single_id not between", value1, value2, "singleId");
            return (Criteria) this;
        }

        public Criteria andIdsIsNull() {
            addCriterion("ids is null");
            return (Criteria) this;
        }

        public Criteria andIdsIsNotNull() {
            addCriterion("ids is not null");
            return (Criteria) this;
        }

        public Criteria andIdsEqualTo(String value) {
            addCriterion("ids =", value, "ids");
            return (Criteria) this;
        }

        public Criteria andIdsNotEqualTo(String value) {
            addCriterion("ids <>", value, "ids");
            return (Criteria) this;
        }

        public Criteria andIdsGreaterThan(String value) {
            addCriterion("ids >", value, "ids");
            return (Criteria) this;
        }

        public Criteria andIdsGreaterThanOrEqualTo(String value) {
            addCriterion("ids >=", value, "ids");
            return (Criteria) this;
        }

        public Criteria andIdsLessThan(String value) {
            addCriterion("ids <", value, "ids");
            return (Criteria) this;
        }

        public Criteria andIdsLessThanOrEqualTo(String value) {
            addCriterion("ids <=", value, "ids");
            return (Criteria) this;
        }

        public Criteria andIdsLike(String value) {
            addCriterion("ids like", value, "ids");
            return (Criteria) this;
        }

        public Criteria andIdsNotLike(String value) {
            addCriterion("ids not like", value, "ids");
            return (Criteria) this;
        }

        public Criteria andIdsIn(List<String> values) {
            addCriterion("ids in", values, "ids");
            return (Criteria) this;
        }

        public Criteria andIdsNotIn(List<String> values) {
            addCriterion("ids not in", values, "ids");
            return (Criteria) this;
        }

        public Criteria andIdsBetween(String value1, String value2) {
            addCriterion("ids between", value1, value2, "ids");
            return (Criteria) this;
        }

        public Criteria andIdsNotBetween(String value1, String value2) {
            addCriterion("ids not between", value1, value2, "ids");
            return (Criteria) this;
        }

        public Criteria andDateIsNull() {
            addCriterion("date is null");
            return (Criteria) this;
        }

        public Criteria andDateIsNotNull() {
            addCriterion("date is not null");
            return (Criteria) this;
        }

        public Criteria andDateEqualTo(String value) {
            addCriterion("date =", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotEqualTo(String value) {
            addCriterion("date <>", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThan(String value) {
            addCriterion("date >", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateGreaterThanOrEqualTo(String value) {
            addCriterion("date >=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThan(String value) {
            addCriterion("date <", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLessThanOrEqualTo(String value) {
            addCriterion("date <=", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateLike(String value) {
            addCriterion("date like", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotLike(String value) {
            addCriterion("date not like", value, "date");
            return (Criteria) this;
        }

        public Criteria andDateIn(List<String> values) {
            addCriterion("date in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotIn(List<String> values) {
            addCriterion("date not in", values, "date");
            return (Criteria) this;
        }

        public Criteria andDateBetween(String value1, String value2) {
            addCriterion("date between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andDateNotBetween(String value1, String value2) {
            addCriterion("date not between", value1, value2, "date");
            return (Criteria) this;
        }

        public Criteria andDateTimeIsNull() {
            addCriterion("date_time is null");
            return (Criteria) this;
        }

        public Criteria andDateTimeIsNotNull() {
            addCriterion("date_time is not null");
            return (Criteria) this;
        }

        public Criteria andDateTimeEqualTo(String value) {
            addCriterion("date_time =", value, "dateTime");
            return (Criteria) this;
        }

        public Criteria andDateTimeNotEqualTo(String value) {
            addCriterion("date_time <>", value, "dateTime");
            return (Criteria) this;
        }

        public Criteria andDateTimeGreaterThan(String value) {
            addCriterion("date_time >", value, "dateTime");
            return (Criteria) this;
        }

        public Criteria andDateTimeGreaterThanOrEqualTo(String value) {
            addCriterion("date_time >=", value, "dateTime");
            return (Criteria) this;
        }

        public Criteria andDateTimeLessThan(String value) {
            addCriterion("date_time <", value, "dateTime");
            return (Criteria) this;
        }

        public Criteria andDateTimeLessThanOrEqualTo(String value) {
            addCriterion("date_time <=", value, "dateTime");
            return (Criteria) this;
        }

        public Criteria andDateTimeLike(String value) {
            addCriterion("date_time like", value, "dateTime");
            return (Criteria) this;
        }

        public Criteria andDateTimeNotLike(String value) {
            addCriterion("date_time not like", value, "dateTime");
            return (Criteria) this;
        }

        public Criteria andDateTimeIn(List<String> values) {
            addCriterion("date_time in", values, "dateTime");
            return (Criteria) this;
        }

        public Criteria andDateTimeNotIn(List<String> values) {
            addCriterion("date_time not in", values, "dateTime");
            return (Criteria) this;
        }

        public Criteria andDateTimeBetween(String value1, String value2) {
            addCriterion("date_time between", value1, value2, "dateTime");
            return (Criteria) this;
        }

        public Criteria andDateTimeNotBetween(String value1, String value2) {
            addCriterion("date_time not between", value1, value2, "dateTime");
            return (Criteria) this;
        }

        public Criteria andMessageMiraiCodeIsNull() {
            addCriterion("message_mirai_code is null");
            return (Criteria) this;
        }

        public Criteria andMessageMiraiCodeIsNotNull() {
            addCriterion("message_mirai_code is not null");
            return (Criteria) this;
        }

        public Criteria andMessageMiraiCodeEqualTo(String value) {
            addCriterion("message_mirai_code =", value, "messageMiraiCode");
            return (Criteria) this;
        }

        public Criteria andMessageMiraiCodeNotEqualTo(String value) {
            addCriterion("message_mirai_code <>", value, "messageMiraiCode");
            return (Criteria) this;
        }

        public Criteria andMessageMiraiCodeGreaterThan(String value) {
            addCriterion("message_mirai_code >", value, "messageMiraiCode");
            return (Criteria) this;
        }

        public Criteria andMessageMiraiCodeGreaterThanOrEqualTo(String value) {
            addCriterion("message_mirai_code >=", value, "messageMiraiCode");
            return (Criteria) this;
        }

        public Criteria andMessageMiraiCodeLessThan(String value) {
            addCriterion("message_mirai_code <", value, "messageMiraiCode");
            return (Criteria) this;
        }

        public Criteria andMessageMiraiCodeLessThanOrEqualTo(String value) {
            addCriterion("message_mirai_code <=", value, "messageMiraiCode");
            return (Criteria) this;
        }

        public Criteria andMessageMiraiCodeLike(String value) {
            addCriterion("message_mirai_code like", value, "messageMiraiCode");
            return (Criteria) this;
        }

        public Criteria andMessageMiraiCodeNotLike(String value) {
            addCriterion("message_mirai_code not like", value, "messageMiraiCode");
            return (Criteria) this;
        }

        public Criteria andMessageMiraiCodeIn(List<String> values) {
            addCriterion("message_mirai_code in", values, "messageMiraiCode");
            return (Criteria) this;
        }

        public Criteria andMessageMiraiCodeNotIn(List<String> values) {
            addCriterion("message_mirai_code not in", values, "messageMiraiCode");
            return (Criteria) this;
        }

        public Criteria andMessageMiraiCodeBetween(String value1, String value2) {
            addCriterion("message_mirai_code between", value1, value2, "messageMiraiCode");
            return (Criteria) this;
        }

        public Criteria andMessageMiraiCodeNotBetween(String value1, String value2) {
            addCriterion("message_mirai_code not between", value1, value2, "messageMiraiCode");
            return (Criteria) this;
        }

        public Criteria andBotNameIsNull() {
            addCriterion("bot_name is null");
            return (Criteria) this;
        }

        public Criteria andBotNameIsNotNull() {
            addCriterion("bot_name is not null");
            return (Criteria) this;
        }

        public Criteria andBotNameEqualTo(String value) {
            addCriterion("bot_name =", value, "botName");
            return (Criteria) this;
        }

        public Criteria andBotNameNotEqualTo(String value) {
            addCriterion("bot_name <>", value, "botName");
            return (Criteria) this;
        }

        public Criteria andBotNameGreaterThan(String value) {
            addCriterion("bot_name >", value, "botName");
            return (Criteria) this;
        }

        public Criteria andBotNameGreaterThanOrEqualTo(String value) {
            addCriterion("bot_name >=", value, "botName");
            return (Criteria) this;
        }

        public Criteria andBotNameLessThan(String value) {
            addCriterion("bot_name <", value, "botName");
            return (Criteria) this;
        }

        public Criteria andBotNameLessThanOrEqualTo(String value) {
            addCriterion("bot_name <=", value, "botName");
            return (Criteria) this;
        }

        public Criteria andBotNameLike(String value) {
            addCriterion("bot_name like", value, "botName");
            return (Criteria) this;
        }

        public Criteria andBotNameNotLike(String value) {
            addCriterion("bot_name not like", value, "botName");
            return (Criteria) this;
        }

        public Criteria andBotNameIn(List<String> values) {
            addCriterion("bot_name in", values, "botName");
            return (Criteria) this;
        }

        public Criteria andBotNameNotIn(List<String> values) {
            addCriterion("bot_name not in", values, "botName");
            return (Criteria) this;
        }

        public Criteria andBotNameBetween(String value1, String value2) {
            addCriterion("bot_name between", value1, value2, "botName");
            return (Criteria) this;
        }

        public Criteria andBotNameNotBetween(String value1, String value2) {
            addCriterion("bot_name not between", value1, value2, "botName");
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
    bot_message_record
     *
     * @mbg.generated do_not_delete_during_merge Mon Oct 09 14:19:28 CST 2023
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    /**
    bot_message_record
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