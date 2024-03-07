package com.king.db.pojo;

import java.util.ArrayList;
import java.util.List;

public class OpenAIApiKeyExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public OpenAIApiKeyExample() {
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
    open_ai_api_key
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

        public Criteria andApiKeyIsNull() {
            addCriterion("api_key is null");
            return (Criteria) this;
        }

        public Criteria andApiKeyIsNotNull() {
            addCriterion("api_key is not null");
            return (Criteria) this;
        }

        public Criteria andApiKeyEqualTo(String value) {
            addCriterion("api_key =", value, "apiKey");
            return (Criteria) this;
        }

        public Criteria andApiKeyNotEqualTo(String value) {
            addCriterion("api_key <>", value, "apiKey");
            return (Criteria) this;
        }

        public Criteria andApiKeyGreaterThan(String value) {
            addCriterion("api_key >", value, "apiKey");
            return (Criteria) this;
        }

        public Criteria andApiKeyGreaterThanOrEqualTo(String value) {
            addCriterion("api_key >=", value, "apiKey");
            return (Criteria) this;
        }

        public Criteria andApiKeyLessThan(String value) {
            addCriterion("api_key <", value, "apiKey");
            return (Criteria) this;
        }

        public Criteria andApiKeyLessThanOrEqualTo(String value) {
            addCriterion("api_key <=", value, "apiKey");
            return (Criteria) this;
        }

        public Criteria andApiKeyLike(String value) {
            addCriterion("api_key like", value, "apiKey");
            return (Criteria) this;
        }

        public Criteria andApiKeyNotLike(String value) {
            addCriterion("api_key not like", value, "apiKey");
            return (Criteria) this;
        }

        public Criteria andApiKeyIn(List<String> values) {
            addCriterion("api_key in", values, "apiKey");
            return (Criteria) this;
        }

        public Criteria andApiKeyNotIn(List<String> values) {
            addCriterion("api_key not in", values, "apiKey");
            return (Criteria) this;
        }

        public Criteria andApiKeyBetween(String value1, String value2) {
            addCriterion("api_key between", value1, value2, "apiKey");
            return (Criteria) this;
        }

        public Criteria andApiKeyNotBetween(String value1, String value2) {
            addCriterion("api_key not between", value1, value2, "apiKey");
            return (Criteria) this;
        }

        public Criteria andRateLimitIsNull() {
            addCriterion("rate_limit is null");
            return (Criteria) this;
        }

        public Criteria andRateLimitIsNotNull() {
            addCriterion("rate_limit is not null");
            return (Criteria) this;
        }

        public Criteria andRateLimitEqualTo(Long value) {
            addCriterion("rate_limit =", value, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitNotEqualTo(Long value) {
            addCriterion("rate_limit <>", value, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitGreaterThan(Long value) {
            addCriterion("rate_limit >", value, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitGreaterThanOrEqualTo(Long value) {
            addCriterion("rate_limit >=", value, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitLessThan(Long value) {
            addCriterion("rate_limit <", value, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitLessThanOrEqualTo(Long value) {
            addCriterion("rate_limit <=", value, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitIn(List<Long> values) {
            addCriterion("rate_limit in", values, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitNotIn(List<Long> values) {
            addCriterion("rate_limit not in", values, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitBetween(Long value1, Long value2) {
            addCriterion("rate_limit between", value1, value2, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitNotBetween(Long value1, Long value2) {
            addCriterion("rate_limit not between", value1, value2, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andHardLimitUsdIsNull() {
            addCriterion("hard_limit_usd is null");
            return (Criteria) this;
        }

        public Criteria andHardLimitUsdIsNotNull() {
            addCriterion("hard_limit_usd is not null");
            return (Criteria) this;
        }

        public Criteria andHardLimitUsdEqualTo(String value) {
            addCriterion("hard_limit_usd =", value, "hardLimitUsd");
            return (Criteria) this;
        }

        public Criteria andHardLimitUsdNotEqualTo(String value) {
            addCriterion("hard_limit_usd <>", value, "hardLimitUsd");
            return (Criteria) this;
        }

        public Criteria andHardLimitUsdGreaterThan(String value) {
            addCriterion("hard_limit_usd >", value, "hardLimitUsd");
            return (Criteria) this;
        }

        public Criteria andHardLimitUsdGreaterThanOrEqualTo(String value) {
            addCriterion("hard_limit_usd >=", value, "hardLimitUsd");
            return (Criteria) this;
        }

        public Criteria andHardLimitUsdLessThan(String value) {
            addCriterion("hard_limit_usd <", value, "hardLimitUsd");
            return (Criteria) this;
        }

        public Criteria andHardLimitUsdLessThanOrEqualTo(String value) {
            addCriterion("hard_limit_usd <=", value, "hardLimitUsd");
            return (Criteria) this;
        }

        public Criteria andHardLimitUsdLike(String value) {
            addCriterion("hard_limit_usd like", value, "hardLimitUsd");
            return (Criteria) this;
        }

        public Criteria andHardLimitUsdNotLike(String value) {
            addCriterion("hard_limit_usd not like", value, "hardLimitUsd");
            return (Criteria) this;
        }

        public Criteria andHardLimitUsdIn(List<String> values) {
            addCriterion("hard_limit_usd in", values, "hardLimitUsd");
            return (Criteria) this;
        }

        public Criteria andHardLimitUsdNotIn(List<String> values) {
            addCriterion("hard_limit_usd not in", values, "hardLimitUsd");
            return (Criteria) this;
        }

        public Criteria andHardLimitUsdBetween(String value1, String value2) {
            addCriterion("hard_limit_usd between", value1, value2, "hardLimitUsd");
            return (Criteria) this;
        }

        public Criteria andHardLimitUsdNotBetween(String value1, String value2) {
            addCriterion("hard_limit_usd not between", value1, value2, "hardLimitUsd");
            return (Criteria) this;
        }

        public Criteria andTotalUsageIsNull() {
            addCriterion("total_usage is null");
            return (Criteria) this;
        }

        public Criteria andTotalUsageIsNotNull() {
            addCriterion("total_usage is not null");
            return (Criteria) this;
        }

        public Criteria andTotalUsageEqualTo(String value) {
            addCriterion("total_usage =", value, "totalUsage");
            return (Criteria) this;
        }

        public Criteria andTotalUsageNotEqualTo(String value) {
            addCriterion("total_usage <>", value, "totalUsage");
            return (Criteria) this;
        }

        public Criteria andTotalUsageGreaterThan(String value) {
            addCriterion("total_usage >", value, "totalUsage");
            return (Criteria) this;
        }

        public Criteria andTotalUsageGreaterThanOrEqualTo(String value) {
            addCriterion("total_usage >=", value, "totalUsage");
            return (Criteria) this;
        }

        public Criteria andTotalUsageLessThan(String value) {
            addCriterion("total_usage <", value, "totalUsage");
            return (Criteria) this;
        }

        public Criteria andTotalUsageLessThanOrEqualTo(String value) {
            addCriterion("total_usage <=", value, "totalUsage");
            return (Criteria) this;
        }

        public Criteria andTotalUsageLike(String value) {
            addCriterion("total_usage like", value, "totalUsage");
            return (Criteria) this;
        }

        public Criteria andTotalUsageNotLike(String value) {
            addCriterion("total_usage not like", value, "totalUsage");
            return (Criteria) this;
        }

        public Criteria andTotalUsageIn(List<String> values) {
            addCriterion("total_usage in", values, "totalUsage");
            return (Criteria) this;
        }

        public Criteria andTotalUsageNotIn(List<String> values) {
            addCriterion("total_usage not in", values, "totalUsage");
            return (Criteria) this;
        }

        public Criteria andTotalUsageBetween(String value1, String value2) {
            addCriterion("total_usage between", value1, value2, "totalUsage");
            return (Criteria) this;
        }

        public Criteria andTotalUsageNotBetween(String value1, String value2) {
            addCriterion("total_usage not between", value1, value2, "totalUsage");
            return (Criteria) this;
        }
    }

    /**
    open_ai_api_key
     *
     * @mbg.generated do_not_delete_during_merge Mon Oct 09 14:19:28 CST 2023
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    /**
    open_ai_api_key
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