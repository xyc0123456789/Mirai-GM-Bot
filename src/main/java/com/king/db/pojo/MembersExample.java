package com.king.db.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MembersExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MembersExample() {
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
    members
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

        public Criteria andQqLevelIsNull() {
            addCriterion("qq_level is null");
            return (Criteria) this;
        }

        public Criteria andQqLevelIsNotNull() {
            addCriterion("qq_level is not null");
            return (Criteria) this;
        }

        public Criteria andQqLevelEqualTo(Integer value) {
            addCriterion("qq_level =", value, "qqLevel");
            return (Criteria) this;
        }

        public Criteria andQqLevelNotEqualTo(Integer value) {
            addCriterion("qq_level <>", value, "qqLevel");
            return (Criteria) this;
        }

        public Criteria andQqLevelGreaterThan(Integer value) {
            addCriterion("qq_level >", value, "qqLevel");
            return (Criteria) this;
        }

        public Criteria andQqLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("qq_level >=", value, "qqLevel");
            return (Criteria) this;
        }

        public Criteria andQqLevelLessThan(Integer value) {
            addCriterion("qq_level <", value, "qqLevel");
            return (Criteria) this;
        }

        public Criteria andQqLevelLessThanOrEqualTo(Integer value) {
            addCriterion("qq_level <=", value, "qqLevel");
            return (Criteria) this;
        }

        public Criteria andQqLevelIn(List<Integer> values) {
            addCriterion("qq_level in", values, "qqLevel");
            return (Criteria) this;
        }

        public Criteria andQqLevelNotIn(List<Integer> values) {
            addCriterion("qq_level not in", values, "qqLevel");
            return (Criteria) this;
        }

        public Criteria andQqLevelBetween(Integer value1, Integer value2) {
            addCriterion("qq_level between", value1, value2, "qqLevel");
            return (Criteria) this;
        }

        public Criteria andQqLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("qq_level not between", value1, value2, "qqLevel");
            return (Criteria) this;
        }

        public Criteria andAgeIsNull() {
            addCriterion("age is null");
            return (Criteria) this;
        }

        public Criteria andAgeIsNotNull() {
            addCriterion("age is not null");
            return (Criteria) this;
        }

        public Criteria andAgeEqualTo(Integer value) {
            addCriterion("age =", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotEqualTo(Integer value) {
            addCriterion("age <>", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeGreaterThan(Integer value) {
            addCriterion("age >", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeGreaterThanOrEqualTo(Integer value) {
            addCriterion("age >=", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeLessThan(Integer value) {
            addCriterion("age <", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeLessThanOrEqualTo(Integer value) {
            addCriterion("age <=", value, "age");
            return (Criteria) this;
        }

        public Criteria andAgeIn(List<Integer> values) {
            addCriterion("age in", values, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotIn(List<Integer> values) {
            addCriterion("age not in", values, "age");
            return (Criteria) this;
        }

        public Criteria andAgeBetween(Integer value1, Integer value2) {
            addCriterion("age between", value1, value2, "age");
            return (Criteria) this;
        }

        public Criteria andAgeNotBetween(Integer value1, Integer value2) {
            addCriterion("age not between", value1, value2, "age");
            return (Criteria) this;
        }

        public Criteria andSexIsNull() {
            addCriterion("sex is null");
            return (Criteria) this;
        }

        public Criteria andSexIsNotNull() {
            addCriterion("sex is not null");
            return (Criteria) this;
        }

        public Criteria andSexEqualTo(String value) {
            addCriterion("sex =", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotEqualTo(String value) {
            addCriterion("sex <>", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThan(String value) {
            addCriterion("sex >", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexGreaterThanOrEqualTo(String value) {
            addCriterion("sex >=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThan(String value) {
            addCriterion("sex <", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLessThanOrEqualTo(String value) {
            addCriterion("sex <=", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexLike(String value) {
            addCriterion("sex like", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotLike(String value) {
            addCriterion("sex not like", value, "sex");
            return (Criteria) this;
        }

        public Criteria andSexIn(List<String> values) {
            addCriterion("sex in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotIn(List<String> values) {
            addCriterion("sex not in", values, "sex");
            return (Criteria) this;
        }

        public Criteria andSexBetween(String value1, String value2) {
            addCriterion("sex between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andSexNotBetween(String value1, String value2) {
            addCriterion("sex not between", value1, value2, "sex");
            return (Criteria) this;
        }

        public Criteria andNickNameIsNull() {
            addCriterion("nick_name is null");
            return (Criteria) this;
        }

        public Criteria andNickNameIsNotNull() {
            addCriterion("nick_name is not null");
            return (Criteria) this;
        }

        public Criteria andNickNameEqualTo(String value) {
            addCriterion("nick_name =", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotEqualTo(String value) {
            addCriterion("nick_name <>", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameGreaterThan(String value) {
            addCriterion("nick_name >", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameGreaterThanOrEqualTo(String value) {
            addCriterion("nick_name >=", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameLessThan(String value) {
            addCriterion("nick_name <", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameLessThanOrEqualTo(String value) {
            addCriterion("nick_name <=", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameLike(String value) {
            addCriterion("nick_name like", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotLike(String value) {
            addCriterion("nick_name not like", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameIn(List<String> values) {
            addCriterion("nick_name in", values, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotIn(List<String> values) {
            addCriterion("nick_name not in", values, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameBetween(String value1, String value2) {
            addCriterion("nick_name between", value1, value2, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotBetween(String value1, String value2) {
            addCriterion("nick_name not between", value1, value2, "nickName");
            return (Criteria) this;
        }

        public Criteria andNameCardIsNull() {
            addCriterion("name_card is null");
            return (Criteria) this;
        }

        public Criteria andNameCardIsNotNull() {
            addCriterion("name_card is not null");
            return (Criteria) this;
        }

        public Criteria andNameCardEqualTo(String value) {
            addCriterion("name_card =", value, "nameCard");
            return (Criteria) this;
        }

        public Criteria andNameCardNotEqualTo(String value) {
            addCriterion("name_card <>", value, "nameCard");
            return (Criteria) this;
        }

        public Criteria andNameCardGreaterThan(String value) {
            addCriterion("name_card >", value, "nameCard");
            return (Criteria) this;
        }

        public Criteria andNameCardGreaterThanOrEqualTo(String value) {
            addCriterion("name_card >=", value, "nameCard");
            return (Criteria) this;
        }

        public Criteria andNameCardLessThan(String value) {
            addCriterion("name_card <", value, "nameCard");
            return (Criteria) this;
        }

        public Criteria andNameCardLessThanOrEqualTo(String value) {
            addCriterion("name_card <=", value, "nameCard");
            return (Criteria) this;
        }

        public Criteria andNameCardLike(String value) {
            addCriterion("name_card like", value, "nameCard");
            return (Criteria) this;
        }

        public Criteria andNameCardNotLike(String value) {
            addCriterion("name_card not like", value, "nameCard");
            return (Criteria) this;
        }

        public Criteria andNameCardIn(List<String> values) {
            addCriterion("name_card in", values, "nameCard");
            return (Criteria) this;
        }

        public Criteria andNameCardNotIn(List<String> values) {
            addCriterion("name_card not in", values, "nameCard");
            return (Criteria) this;
        }

        public Criteria andNameCardBetween(String value1, String value2) {
            addCriterion("name_card between", value1, value2, "nameCard");
            return (Criteria) this;
        }

        public Criteria andNameCardNotBetween(String value1, String value2) {
            addCriterion("name_card not between", value1, value2, "nameCard");
            return (Criteria) this;
        }

        public Criteria andSpecialTitleIsNull() {
            addCriterion("special_title is null");
            return (Criteria) this;
        }

        public Criteria andSpecialTitleIsNotNull() {
            addCriterion("special_title is not null");
            return (Criteria) this;
        }

        public Criteria andSpecialTitleEqualTo(String value) {
            addCriterion("special_title =", value, "specialTitle");
            return (Criteria) this;
        }

        public Criteria andSpecialTitleNotEqualTo(String value) {
            addCriterion("special_title <>", value, "specialTitle");
            return (Criteria) this;
        }

        public Criteria andSpecialTitleGreaterThan(String value) {
            addCriterion("special_title >", value, "specialTitle");
            return (Criteria) this;
        }

        public Criteria andSpecialTitleGreaterThanOrEqualTo(String value) {
            addCriterion("special_title >=", value, "specialTitle");
            return (Criteria) this;
        }

        public Criteria andSpecialTitleLessThan(String value) {
            addCriterion("special_title <", value, "specialTitle");
            return (Criteria) this;
        }

        public Criteria andSpecialTitleLessThanOrEqualTo(String value) {
            addCriterion("special_title <=", value, "specialTitle");
            return (Criteria) this;
        }

        public Criteria andSpecialTitleLike(String value) {
            addCriterion("special_title like", value, "specialTitle");
            return (Criteria) this;
        }

        public Criteria andSpecialTitleNotLike(String value) {
            addCriterion("special_title not like", value, "specialTitle");
            return (Criteria) this;
        }

        public Criteria andSpecialTitleIn(List<String> values) {
            addCriterion("special_title in", values, "specialTitle");
            return (Criteria) this;
        }

        public Criteria andSpecialTitleNotIn(List<String> values) {
            addCriterion("special_title not in", values, "specialTitle");
            return (Criteria) this;
        }

        public Criteria andSpecialTitleBetween(String value1, String value2) {
            addCriterion("special_title between", value1, value2, "specialTitle");
            return (Criteria) this;
        }

        public Criteria andSpecialTitleNotBetween(String value1, String value2) {
            addCriterion("special_title not between", value1, value2, "specialTitle");
            return (Criteria) this;
        }

        public Criteria andEmailIsNull() {
            addCriterion("email is null");
            return (Criteria) this;
        }

        public Criteria andEmailIsNotNull() {
            addCriterion("email is not null");
            return (Criteria) this;
        }

        public Criteria andEmailEqualTo(String value) {
            addCriterion("email =", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotEqualTo(String value) {
            addCriterion("email <>", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThan(String value) {
            addCriterion("email >", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThanOrEqualTo(String value) {
            addCriterion("email >=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThan(String value) {
            addCriterion("email <", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThanOrEqualTo(String value) {
            addCriterion("email <=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLike(String value) {
            addCriterion("email like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotLike(String value) {
            addCriterion("email not like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailIn(List<String> values) {
            addCriterion("email in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotIn(List<String> values) {
            addCriterion("email not in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailBetween(String value1, String value2) {
            addCriterion("email between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotBetween(String value1, String value2) {
            addCriterion("email not between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andJoinDateIsNull() {
            addCriterion("join_date is null");
            return (Criteria) this;
        }

        public Criteria andJoinDateIsNotNull() {
            addCriterion("join_date is not null");
            return (Criteria) this;
        }

        public Criteria andJoinDateEqualTo(Date value) {
            addCriterion("join_date =", value, "joinDate");
            return (Criteria) this;
        }

        public Criteria andJoinDateNotEqualTo(Date value) {
            addCriterion("join_date <>", value, "joinDate");
            return (Criteria) this;
        }

        public Criteria andJoinDateGreaterThan(Date value) {
            addCriterion("join_date >", value, "joinDate");
            return (Criteria) this;
        }

        public Criteria andJoinDateGreaterThanOrEqualTo(Date value) {
            addCriterion("join_date >=", value, "joinDate");
            return (Criteria) this;
        }

        public Criteria andJoinDateLessThan(Date value) {
            addCriterion("join_date <", value, "joinDate");
            return (Criteria) this;
        }

        public Criteria andJoinDateLessThanOrEqualTo(Date value) {
            addCriterion("join_date <=", value, "joinDate");
            return (Criteria) this;
        }

        public Criteria andJoinDateIn(List<Date> values) {
            addCriterion("join_date in", values, "joinDate");
            return (Criteria) this;
        }

        public Criteria andJoinDateNotIn(List<Date> values) {
            addCriterion("join_date not in", values, "joinDate");
            return (Criteria) this;
        }

        public Criteria andJoinDateBetween(Date value1, Date value2) {
            addCriterion("join_date between", value1, value2, "joinDate");
            return (Criteria) this;
        }

        public Criteria andJoinDateNotBetween(Date value1, Date value2) {
            addCriterion("join_date not between", value1, value2, "joinDate");
            return (Criteria) this;
        }

        public Criteria andLastSpeakDateIsNull() {
            addCriterion("last_speak_date is null");
            return (Criteria) this;
        }

        public Criteria andLastSpeakDateIsNotNull() {
            addCriterion("last_speak_date is not null");
            return (Criteria) this;
        }

        public Criteria andLastSpeakDateEqualTo(Date value) {
            addCriterion("last_speak_date =", value, "lastSpeakDate");
            return (Criteria) this;
        }

        public Criteria andLastSpeakDateNotEqualTo(Date value) {
            addCriterion("last_speak_date <>", value, "lastSpeakDate");
            return (Criteria) this;
        }

        public Criteria andLastSpeakDateGreaterThan(Date value) {
            addCriterion("last_speak_date >", value, "lastSpeakDate");
            return (Criteria) this;
        }

        public Criteria andLastSpeakDateGreaterThanOrEqualTo(Date value) {
            addCriterion("last_speak_date >=", value, "lastSpeakDate");
            return (Criteria) this;
        }

        public Criteria andLastSpeakDateLessThan(Date value) {
            addCriterion("last_speak_date <", value, "lastSpeakDate");
            return (Criteria) this;
        }

        public Criteria andLastSpeakDateLessThanOrEqualTo(Date value) {
            addCriterion("last_speak_date <=", value, "lastSpeakDate");
            return (Criteria) this;
        }

        public Criteria andLastSpeakDateIn(List<Date> values) {
            addCriterion("last_speak_date in", values, "lastSpeakDate");
            return (Criteria) this;
        }

        public Criteria andLastSpeakDateNotIn(List<Date> values) {
            addCriterion("last_speak_date not in", values, "lastSpeakDate");
            return (Criteria) this;
        }

        public Criteria andLastSpeakDateBetween(Date value1, Date value2) {
            addCriterion("last_speak_date between", value1, value2, "lastSpeakDate");
            return (Criteria) this;
        }

        public Criteria andLastSpeakDateNotBetween(Date value1, Date value2) {
            addCriterion("last_speak_date not between", value1, value2, "lastSpeakDate");
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
    members
     *
     * @mbg.generated do_not_delete_during_merge Mon Oct 09 14:19:28 CST 2023
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    /**
    members
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