package com.king.db.pojo;

import java.util.ArrayList;
import java.util.List;

public class SparkKeyExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public SparkKeyExample() {
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
    spark_key
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

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andAppidIsNull() {
            addCriterion("appid is null");
            return (Criteria) this;
        }

        public Criteria andAppidIsNotNull() {
            addCriterion("appid is not null");
            return (Criteria) this;
        }

        public Criteria andAppidEqualTo(String value) {
            addCriterion("appid =", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidNotEqualTo(String value) {
            addCriterion("appid <>", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidGreaterThan(String value) {
            addCriterion("appid >", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidGreaterThanOrEqualTo(String value) {
            addCriterion("appid >=", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidLessThan(String value) {
            addCriterion("appid <", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidLessThanOrEqualTo(String value) {
            addCriterion("appid <=", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidLike(String value) {
            addCriterion("appid like", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidNotLike(String value) {
            addCriterion("appid not like", value, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidIn(List<String> values) {
            addCriterion("appid in", values, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidNotIn(List<String> values) {
            addCriterion("appid not in", values, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidBetween(String value1, String value2) {
            addCriterion("appid between", value1, value2, "appid");
            return (Criteria) this;
        }

        public Criteria andAppidNotBetween(String value1, String value2) {
            addCriterion("appid not between", value1, value2, "appid");
            return (Criteria) this;
        }

        public Criteria andApikeyIsNull() {
            addCriterion("apiKey is null");
            return (Criteria) this;
        }

        public Criteria andApikeyIsNotNull() {
            addCriterion("apiKey is not null");
            return (Criteria) this;
        }

        public Criteria andApikeyEqualTo(String value) {
            addCriterion("apiKey =", value, "apikey");
            return (Criteria) this;
        }

        public Criteria andApikeyNotEqualTo(String value) {
            addCriterion("apiKey <>", value, "apikey");
            return (Criteria) this;
        }

        public Criteria andApikeyGreaterThan(String value) {
            addCriterion("apiKey >", value, "apikey");
            return (Criteria) this;
        }

        public Criteria andApikeyGreaterThanOrEqualTo(String value) {
            addCriterion("apiKey >=", value, "apikey");
            return (Criteria) this;
        }

        public Criteria andApikeyLessThan(String value) {
            addCriterion("apiKey <", value, "apikey");
            return (Criteria) this;
        }

        public Criteria andApikeyLessThanOrEqualTo(String value) {
            addCriterion("apiKey <=", value, "apikey");
            return (Criteria) this;
        }

        public Criteria andApikeyLike(String value) {
            addCriterion("apiKey like", value, "apikey");
            return (Criteria) this;
        }

        public Criteria andApikeyNotLike(String value) {
            addCriterion("apiKey not like", value, "apikey");
            return (Criteria) this;
        }

        public Criteria andApikeyIn(List<String> values) {
            addCriterion("apiKey in", values, "apikey");
            return (Criteria) this;
        }

        public Criteria andApikeyNotIn(List<String> values) {
            addCriterion("apiKey not in", values, "apikey");
            return (Criteria) this;
        }

        public Criteria andApikeyBetween(String value1, String value2) {
            addCriterion("apiKey between", value1, value2, "apikey");
            return (Criteria) this;
        }

        public Criteria andApikeyNotBetween(String value1, String value2) {
            addCriterion("apiKey not between", value1, value2, "apikey");
            return (Criteria) this;
        }

        public Criteria andApisecretIsNull() {
            addCriterion("apiSecret is null");
            return (Criteria) this;
        }

        public Criteria andApisecretIsNotNull() {
            addCriterion("apiSecret is not null");
            return (Criteria) this;
        }

        public Criteria andApisecretEqualTo(String value) {
            addCriterion("apiSecret =", value, "apisecret");
            return (Criteria) this;
        }

        public Criteria andApisecretNotEqualTo(String value) {
            addCriterion("apiSecret <>", value, "apisecret");
            return (Criteria) this;
        }

        public Criteria andApisecretGreaterThan(String value) {
            addCriterion("apiSecret >", value, "apisecret");
            return (Criteria) this;
        }

        public Criteria andApisecretGreaterThanOrEqualTo(String value) {
            addCriterion("apiSecret >=", value, "apisecret");
            return (Criteria) this;
        }

        public Criteria andApisecretLessThan(String value) {
            addCriterion("apiSecret <", value, "apisecret");
            return (Criteria) this;
        }

        public Criteria andApisecretLessThanOrEqualTo(String value) {
            addCriterion("apiSecret <=", value, "apisecret");
            return (Criteria) this;
        }

        public Criteria andApisecretLike(String value) {
            addCriterion("apiSecret like", value, "apisecret");
            return (Criteria) this;
        }

        public Criteria andApisecretNotLike(String value) {
            addCriterion("apiSecret not like", value, "apisecret");
            return (Criteria) this;
        }

        public Criteria andApisecretIn(List<String> values) {
            addCriterion("apiSecret in", values, "apisecret");
            return (Criteria) this;
        }

        public Criteria andApisecretNotIn(List<String> values) {
            addCriterion("apiSecret not in", values, "apisecret");
            return (Criteria) this;
        }

        public Criteria andApisecretBetween(String value1, String value2) {
            addCriterion("apiSecret between", value1, value2, "apisecret");
            return (Criteria) this;
        }

        public Criteria andApisecretNotBetween(String value1, String value2) {
            addCriterion("apiSecret not between", value1, value2, "apisecret");
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

        public Criteria andRateLimitEqualTo(String value) {
            addCriterion("rate_limit =", value, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitNotEqualTo(String value) {
            addCriterion("rate_limit <>", value, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitGreaterThan(String value) {
            addCriterion("rate_limit >", value, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitGreaterThanOrEqualTo(String value) {
            addCriterion("rate_limit >=", value, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitLessThan(String value) {
            addCriterion("rate_limit <", value, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitLessThanOrEqualTo(String value) {
            addCriterion("rate_limit <=", value, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitLike(String value) {
            addCriterion("rate_limit like", value, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitNotLike(String value) {
            addCriterion("rate_limit not like", value, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitIn(List<String> values) {
            addCriterion("rate_limit in", values, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitNotIn(List<String> values) {
            addCriterion("rate_limit not in", values, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitBetween(String value1, String value2) {
            addCriterion("rate_limit between", value1, value2, "rateLimit");
            return (Criteria) this;
        }

        public Criteria andRateLimitNotBetween(String value1, String value2) {
            addCriterion("rate_limit not between", value1, value2, "rateLimit");
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
    spark_key
     *
     * @mbg.generated do_not_delete_during_merge Mon Oct 09 14:19:28 CST 2023
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    /**
    spark_key
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