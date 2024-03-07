package com.king.db.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandPermissionExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CommandPermissionExample() {
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
    command_permission
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

        public Criteria andContactTypeIsNull() {
            addCriterion("contact_type is null");
            return (Criteria) this;
        }

        public Criteria andContactTypeIsNotNull() {
            addCriterion("contact_type is not null");
            return (Criteria) this;
        }

        public Criteria andContactTypeEqualTo(Integer value) {
            addCriterion("contact_type =", value, "contactType");
            return (Criteria) this;
        }

        public Criteria andContactTypeNotEqualTo(Integer value) {
            addCriterion("contact_type <>", value, "contactType");
            return (Criteria) this;
        }

        public Criteria andContactTypeGreaterThan(Integer value) {
            addCriterion("contact_type >", value, "contactType");
            return (Criteria) this;
        }

        public Criteria andContactTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("contact_type >=", value, "contactType");
            return (Criteria) this;
        }

        public Criteria andContactTypeLessThan(Integer value) {
            addCriterion("contact_type <", value, "contactType");
            return (Criteria) this;
        }

        public Criteria andContactTypeLessThanOrEqualTo(Integer value) {
            addCriterion("contact_type <=", value, "contactType");
            return (Criteria) this;
        }

        public Criteria andContactTypeIn(List<Integer> values) {
            addCriterion("contact_type in", values, "contactType");
            return (Criteria) this;
        }

        public Criteria andContactTypeNotIn(List<Integer> values) {
            addCriterion("contact_type not in", values, "contactType");
            return (Criteria) this;
        }

        public Criteria andContactTypeBetween(Integer value1, Integer value2) {
            addCriterion("contact_type between", value1, value2, "contactType");
            return (Criteria) this;
        }

        public Criteria andContactTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("contact_type not between", value1, value2, "contactType");
            return (Criteria) this;
        }

        public Criteria andContactIdIsNull() {
            addCriterion("contact_id is null");
            return (Criteria) this;
        }

        public Criteria andContactIdIsNotNull() {
            addCriterion("contact_id is not null");
            return (Criteria) this;
        }

        public Criteria andContactIdEqualTo(Long value) {
            addCriterion("contact_id =", value, "contactId");
            return (Criteria) this;
        }

        public Criteria andContactIdNotEqualTo(Long value) {
            addCriterion("contact_id <>", value, "contactId");
            return (Criteria) this;
        }

        public Criteria andContactIdGreaterThan(Long value) {
            addCriterion("contact_id >", value, "contactId");
            return (Criteria) this;
        }

        public Criteria andContactIdGreaterThanOrEqualTo(Long value) {
            addCriterion("contact_id >=", value, "contactId");
            return (Criteria) this;
        }

        public Criteria andContactIdLessThan(Long value) {
            addCriterion("contact_id <", value, "contactId");
            return (Criteria) this;
        }

        public Criteria andContactIdLessThanOrEqualTo(Long value) {
            addCriterion("contact_id <=", value, "contactId");
            return (Criteria) this;
        }

        public Criteria andContactIdIn(List<Long> values) {
            addCriterion("contact_id in", values, "contactId");
            return (Criteria) this;
        }

        public Criteria andContactIdNotIn(List<Long> values) {
            addCriterion("contact_id not in", values, "contactId");
            return (Criteria) this;
        }

        public Criteria andContactIdBetween(Long value1, Long value2) {
            addCriterion("contact_id between", value1, value2, "contactId");
            return (Criteria) this;
        }

        public Criteria andContactIdNotBetween(Long value1, Long value2) {
            addCriterion("contact_id not between", value1, value2, "contactId");
            return (Criteria) this;
        }

        public Criteria andSubContactIdIsNull() {
            addCriterion("sub_contact_id is null");
            return (Criteria) this;
        }

        public Criteria andSubContactIdIsNotNull() {
            addCriterion("sub_contact_id is not null");
            return (Criteria) this;
        }

        public Criteria andSubContactIdEqualTo(Long value) {
            addCriterion("sub_contact_id =", value, "subContactId");
            return (Criteria) this;
        }

        public Criteria andSubContactIdNotEqualTo(Long value) {
            addCriterion("sub_contact_id <>", value, "subContactId");
            return (Criteria) this;
        }

        public Criteria andSubContactIdGreaterThan(Long value) {
            addCriterion("sub_contact_id >", value, "subContactId");
            return (Criteria) this;
        }

        public Criteria andSubContactIdGreaterThanOrEqualTo(Long value) {
            addCriterion("sub_contact_id >=", value, "subContactId");
            return (Criteria) this;
        }

        public Criteria andSubContactIdLessThan(Long value) {
            addCriterion("sub_contact_id <", value, "subContactId");
            return (Criteria) this;
        }

        public Criteria andSubContactIdLessThanOrEqualTo(Long value) {
            addCriterion("sub_contact_id <=", value, "subContactId");
            return (Criteria) this;
        }

        public Criteria andSubContactIdIn(List<Long> values) {
            addCriterion("sub_contact_id in", values, "subContactId");
            return (Criteria) this;
        }

        public Criteria andSubContactIdNotIn(List<Long> values) {
            addCriterion("sub_contact_id not in", values, "subContactId");
            return (Criteria) this;
        }

        public Criteria andSubContactIdBetween(Long value1, Long value2) {
            addCriterion("sub_contact_id between", value1, value2, "subContactId");
            return (Criteria) this;
        }

        public Criteria andSubContactIdNotBetween(Long value1, Long value2) {
            addCriterion("sub_contact_id not between", value1, value2, "subContactId");
            return (Criteria) this;
        }

        public Criteria andCommandNameIsNull() {
            addCriterion("command_name is null");
            return (Criteria) this;
        }

        public Criteria andCommandNameIsNotNull() {
            addCriterion("command_name is not null");
            return (Criteria) this;
        }

        public Criteria andCommandNameEqualTo(String value) {
            addCriterion("command_name =", value, "commandName");
            return (Criteria) this;
        }

        public Criteria andCommandNameNotEqualTo(String value) {
            addCriterion("command_name <>", value, "commandName");
            return (Criteria) this;
        }

        public Criteria andCommandNameGreaterThan(String value) {
            addCriterion("command_name >", value, "commandName");
            return (Criteria) this;
        }

        public Criteria andCommandNameGreaterThanOrEqualTo(String value) {
            addCriterion("command_name >=", value, "commandName");
            return (Criteria) this;
        }

        public Criteria andCommandNameLessThan(String value) {
            addCriterion("command_name <", value, "commandName");
            return (Criteria) this;
        }

        public Criteria andCommandNameLessThanOrEqualTo(String value) {
            addCriterion("command_name <=", value, "commandName");
            return (Criteria) this;
        }

        public Criteria andCommandNameLike(String value) {
            addCriterion("command_name like", value, "commandName");
            return (Criteria) this;
        }

        public Criteria andCommandNameNotLike(String value) {
            addCriterion("command_name not like", value, "commandName");
            return (Criteria) this;
        }

        public Criteria andCommandNameIn(List<String> values) {
            addCriterion("command_name in", values, "commandName");
            return (Criteria) this;
        }

        public Criteria andCommandNameNotIn(List<String> values) {
            addCriterion("command_name not in", values, "commandName");
            return (Criteria) this;
        }

        public Criteria andCommandNameBetween(String value1, String value2) {
            addCriterion("command_name between", value1, value2, "commandName");
            return (Criteria) this;
        }

        public Criteria andCommandNameNotBetween(String value1, String value2) {
            addCriterion("command_name not between", value1, value2, "commandName");
            return (Criteria) this;
        }

        public Criteria andCommandDescIsNull() {
            addCriterion("command_desc is null");
            return (Criteria) this;
        }

        public Criteria andCommandDescIsNotNull() {
            addCriterion("command_desc is not null");
            return (Criteria) this;
        }

        public Criteria andCommandDescEqualTo(String value) {
            addCriterion("command_desc =", value, "commandDesc");
            return (Criteria) this;
        }

        public Criteria andCommandDescNotEqualTo(String value) {
            addCriterion("command_desc <>", value, "commandDesc");
            return (Criteria) this;
        }

        public Criteria andCommandDescGreaterThan(String value) {
            addCriterion("command_desc >", value, "commandDesc");
            return (Criteria) this;
        }

        public Criteria andCommandDescGreaterThanOrEqualTo(String value) {
            addCriterion("command_desc >=", value, "commandDesc");
            return (Criteria) this;
        }

        public Criteria andCommandDescLessThan(String value) {
            addCriterion("command_desc <", value, "commandDesc");
            return (Criteria) this;
        }

        public Criteria andCommandDescLessThanOrEqualTo(String value) {
            addCriterion("command_desc <=", value, "commandDesc");
            return (Criteria) this;
        }

        public Criteria andCommandDescLike(String value) {
            addCriterion("command_desc like", value, "commandDesc");
            return (Criteria) this;
        }

        public Criteria andCommandDescNotLike(String value) {
            addCriterion("command_desc not like", value, "commandDesc");
            return (Criteria) this;
        }

        public Criteria andCommandDescIn(List<String> values) {
            addCriterion("command_desc in", values, "commandDesc");
            return (Criteria) this;
        }

        public Criteria andCommandDescNotIn(List<String> values) {
            addCriterion("command_desc not in", values, "commandDesc");
            return (Criteria) this;
        }

        public Criteria andCommandDescBetween(String value1, String value2) {
            addCriterion("command_desc between", value1, value2, "commandDesc");
            return (Criteria) this;
        }

        public Criteria andCommandDescNotBetween(String value1, String value2) {
            addCriterion("command_desc not between", value1, value2, "commandDesc");
            return (Criteria) this;
        }

        public Criteria andRemark1IsNull() {
            addCriterion("remark1 is null");
            return (Criteria) this;
        }

        public Criteria andRemark1IsNotNull() {
            addCriterion("remark1 is not null");
            return (Criteria) this;
        }

        public Criteria andRemark1EqualTo(String value) {
            addCriterion("remark1 =", value, "remark1");
            return (Criteria) this;
        }

        public Criteria andRemark1NotEqualTo(String value) {
            addCriterion("remark1 <>", value, "remark1");
            return (Criteria) this;
        }

        public Criteria andRemark1GreaterThan(String value) {
            addCriterion("remark1 >", value, "remark1");
            return (Criteria) this;
        }

        public Criteria andRemark1GreaterThanOrEqualTo(String value) {
            addCriterion("remark1 >=", value, "remark1");
            return (Criteria) this;
        }

        public Criteria andRemark1LessThan(String value) {
            addCriterion("remark1 <", value, "remark1");
            return (Criteria) this;
        }

        public Criteria andRemark1LessThanOrEqualTo(String value) {
            addCriterion("remark1 <=", value, "remark1");
            return (Criteria) this;
        }

        public Criteria andRemark1Like(String value) {
            addCriterion("remark1 like", value, "remark1");
            return (Criteria) this;
        }

        public Criteria andRemark1NotLike(String value) {
            addCriterion("remark1 not like", value, "remark1");
            return (Criteria) this;
        }

        public Criteria andRemark1In(List<String> values) {
            addCriterion("remark1 in", values, "remark1");
            return (Criteria) this;
        }

        public Criteria andRemark1NotIn(List<String> values) {
            addCriterion("remark1 not in", values, "remark1");
            return (Criteria) this;
        }

        public Criteria andRemark1Between(String value1, String value2) {
            addCriterion("remark1 between", value1, value2, "remark1");
            return (Criteria) this;
        }

        public Criteria andRemark1NotBetween(String value1, String value2) {
            addCriterion("remark1 not between", value1, value2, "remark1");
            return (Criteria) this;
        }

        public Criteria andRemark2IsNull() {
            addCriterion("remark2 is null");
            return (Criteria) this;
        }

        public Criteria andRemark2IsNotNull() {
            addCriterion("remark2 is not null");
            return (Criteria) this;
        }

        public Criteria andRemark2EqualTo(String value) {
            addCriterion("remark2 =", value, "remark2");
            return (Criteria) this;
        }

        public Criteria andRemark2NotEqualTo(String value) {
            addCriterion("remark2 <>", value, "remark2");
            return (Criteria) this;
        }

        public Criteria andRemark2GreaterThan(String value) {
            addCriterion("remark2 >", value, "remark2");
            return (Criteria) this;
        }

        public Criteria andRemark2GreaterThanOrEqualTo(String value) {
            addCriterion("remark2 >=", value, "remark2");
            return (Criteria) this;
        }

        public Criteria andRemark2LessThan(String value) {
            addCriterion("remark2 <", value, "remark2");
            return (Criteria) this;
        }

        public Criteria andRemark2LessThanOrEqualTo(String value) {
            addCriterion("remark2 <=", value, "remark2");
            return (Criteria) this;
        }

        public Criteria andRemark2Like(String value) {
            addCriterion("remark2 like", value, "remark2");
            return (Criteria) this;
        }

        public Criteria andRemark2NotLike(String value) {
            addCriterion("remark2 not like", value, "remark2");
            return (Criteria) this;
        }

        public Criteria andRemark2In(List<String> values) {
            addCriterion("remark2 in", values, "remark2");
            return (Criteria) this;
        }

        public Criteria andRemark2NotIn(List<String> values) {
            addCriterion("remark2 not in", values, "remark2");
            return (Criteria) this;
        }

        public Criteria andRemark2Between(String value1, String value2) {
            addCriterion("remark2 between", value1, value2, "remark2");
            return (Criteria) this;
        }

        public Criteria andRemark2NotBetween(String value1, String value2) {
            addCriterion("remark2 not between", value1, value2, "remark2");
            return (Criteria) this;
        }

        public Criteria andRemark3IsNull() {
            addCriterion("remark3 is null");
            return (Criteria) this;
        }

        public Criteria andRemark3IsNotNull() {
            addCriterion("remark3 is not null");
            return (Criteria) this;
        }

        public Criteria andRemark3EqualTo(String value) {
            addCriterion("remark3 =", value, "remark3");
            return (Criteria) this;
        }

        public Criteria andRemark3NotEqualTo(String value) {
            addCriterion("remark3 <>", value, "remark3");
            return (Criteria) this;
        }

        public Criteria andRemark3GreaterThan(String value) {
            addCriterion("remark3 >", value, "remark3");
            return (Criteria) this;
        }

        public Criteria andRemark3GreaterThanOrEqualTo(String value) {
            addCriterion("remark3 >=", value, "remark3");
            return (Criteria) this;
        }

        public Criteria andRemark3LessThan(String value) {
            addCriterion("remark3 <", value, "remark3");
            return (Criteria) this;
        }

        public Criteria andRemark3LessThanOrEqualTo(String value) {
            addCriterion("remark3 <=", value, "remark3");
            return (Criteria) this;
        }

        public Criteria andRemark3Like(String value) {
            addCriterion("remark3 like", value, "remark3");
            return (Criteria) this;
        }

        public Criteria andRemark3NotLike(String value) {
            addCriterion("remark3 not like", value, "remark3");
            return (Criteria) this;
        }

        public Criteria andRemark3In(List<String> values) {
            addCriterion("remark3 in", values, "remark3");
            return (Criteria) this;
        }

        public Criteria andRemark3NotIn(List<String> values) {
            addCriterion("remark3 not in", values, "remark3");
            return (Criteria) this;
        }

        public Criteria andRemark3Between(String value1, String value2) {
            addCriterion("remark3 between", value1, value2, "remark3");
            return (Criteria) this;
        }

        public Criteria andRemark3NotBetween(String value1, String value2) {
            addCriterion("remark3 not between", value1, value2, "remark3");
            return (Criteria) this;
        }

        public Criteria andExtDataIsNull() {
            addCriterion("ext_data is null");
            return (Criteria) this;
        }

        public Criteria andExtDataIsNotNull() {
            addCriterion("ext_data is not null");
            return (Criteria) this;
        }

        public Criteria andExtDataEqualTo(String value) {
            addCriterion("ext_data =", value, "extData");
            return (Criteria) this;
        }

        public Criteria andExtDataNotEqualTo(String value) {
            addCriterion("ext_data <>", value, "extData");
            return (Criteria) this;
        }

        public Criteria andExtDataGreaterThan(String value) {
            addCriterion("ext_data >", value, "extData");
            return (Criteria) this;
        }

        public Criteria andExtDataGreaterThanOrEqualTo(String value) {
            addCriterion("ext_data >=", value, "extData");
            return (Criteria) this;
        }

        public Criteria andExtDataLessThan(String value) {
            addCriterion("ext_data <", value, "extData");
            return (Criteria) this;
        }

        public Criteria andExtDataLessThanOrEqualTo(String value) {
            addCriterion("ext_data <=", value, "extData");
            return (Criteria) this;
        }

        public Criteria andExtDataLike(String value) {
            addCriterion("ext_data like", value, "extData");
            return (Criteria) this;
        }

        public Criteria andExtDataNotLike(String value) {
            addCriterion("ext_data not like", value, "extData");
            return (Criteria) this;
        }

        public Criteria andExtDataIn(List<String> values) {
            addCriterion("ext_data in", values, "extData");
            return (Criteria) this;
        }

        public Criteria andExtDataNotIn(List<String> values) {
            addCriterion("ext_data not in", values, "extData");
            return (Criteria) this;
        }

        public Criteria andExtDataBetween(String value1, String value2) {
            addCriterion("ext_data between", value1, value2, "extData");
            return (Criteria) this;
        }

        public Criteria andExtDataNotBetween(String value1, String value2) {
            addCriterion("ext_data not between", value1, value2, "extData");
            return (Criteria) this;
        }

        public Criteria andIsOpenIsNull() {
            addCriterion("is_open is null");
            return (Criteria) this;
        }

        public Criteria andIsOpenIsNotNull() {
            addCriterion("is_open is not null");
            return (Criteria) this;
        }

        public Criteria andIsOpenEqualTo(Boolean value) {
            addCriterion("is_open =", value, "isOpen");
            return (Criteria) this;
        }

        public Criteria andIsOpenNotEqualTo(Boolean value) {
            addCriterion("is_open <>", value, "isOpen");
            return (Criteria) this;
        }

        public Criteria andIsOpenGreaterThan(Boolean value) {
            addCriterion("is_open >", value, "isOpen");
            return (Criteria) this;
        }

        public Criteria andIsOpenGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_open >=", value, "isOpen");
            return (Criteria) this;
        }

        public Criteria andIsOpenLessThan(Boolean value) {
            addCriterion("is_open <", value, "isOpen");
            return (Criteria) this;
        }

        public Criteria andIsOpenLessThanOrEqualTo(Boolean value) {
            addCriterion("is_open <=", value, "isOpen");
            return (Criteria) this;
        }

        public Criteria andIsOpenIn(List<Boolean> values) {
            addCriterion("is_open in", values, "isOpen");
            return (Criteria) this;
        }

        public Criteria andIsOpenNotIn(List<Boolean> values) {
            addCriterion("is_open not in", values, "isOpen");
            return (Criteria) this;
        }

        public Criteria andIsOpenBetween(Boolean value1, Boolean value2) {
            addCriterion("is_open between", value1, value2, "isOpen");
            return (Criteria) this;
        }

        public Criteria andIsOpenNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_open not between", value1, value2, "isOpen");
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
    command_permission
     *
     * @mbg.generated do_not_delete_during_merge Mon Oct 09 14:19:28 CST 2023
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }

    /**
    command_permission
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