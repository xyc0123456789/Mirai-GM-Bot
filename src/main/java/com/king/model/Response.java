package com.king.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class Response implements java.io.Serializable{
    private CODE code;
    private String message;
    private static Map<String, String> pospCodes = new HashMap();

    static {
        pospCodes.put("00", "交易成功");
        pospCodes.put("01", "查发卡行");
        pospCodes.put("03", "无效商户");
        pospCodes.put("04", "没收卡");
        pospCodes.put("05", "身份认证失败");
        pospCodes.put("10", "部分承兑");
        pospCodes.put("11", "重要人物批准(VIP)");
        pospCodes.put("12", "无效的关联交易");
        pospCodes.put("13", "无效金额");
        pospCodes.put("14", "无效卡号(无此账号)");
        pospCodes.put("15", "无此发卡方");
        pospCodes.put("21", "卡未初始化");
        pospCodes.put("22", "故障怀疑，关联交易错误");
        pospCodes.put("25", "找不到原始交易");
        pospCodes.put("30", "报文格式错误");
        pospCodes.put("34", "有作弊嫌疑");
        pospCodes.put("38", "超过允许的密码数输入");
        pospCodes.put("40", "请求的功能尚不支持");
        pospCodes.put("41", "挂失卡");
        pospCodes.put("43", "被窃卡");
        pospCodes.put("45", "降级交易fallback");
        pospCodes.put("51", "资金不足");
        pospCodes.put("54", "过期的卡");
        pospCodes.put("55", "输入密码错");
        pospCodes.put("57", "不允许持卡人进行的交易");
        pospCodes.put("58", "不允许终端进行的交易");
        pospCodes.put("59", "有作弊嫌疑");
        pospCodes.put("61", "超出金额限制");
        pospCodes.put("62", "受限制的卡");
        pospCodes.put("64", "原始金额错误");
        pospCodes.put("65", "超出消费次数限制");
        pospCodes.put("68", "发卡行响应超时");
        pospCodes.put("75", "允许的输入密码次数超限");
        pospCodes.put("77", "交易关闭");
        pospCodes.put("90", "正在日终处理");
        pospCodes.put("91", "发卡方不能操作");
        pospCodes.put("92", "金融机构或中间网络设施找不到");
        pospCodes.put("94", "重复交易");
        pospCodes.put("96", "银联处理中心系统异常、失效");
        pospCodes.put("97", "POS终端号找不到");
        pospCodes.put("98", "银联处理中心收不到发卡方应答");
        pospCodes.put("99", "PIN格式错");
        pospCodes.put("A0", "MAC鉴别失败");
        pospCodes.put("A1", "转账货币不一致");
        pospCodes.put("A2", "有缺陷的成功");
        pospCodes.put("A3", "资金到账行无此账户");
        pospCodes.put("A4", "有缺陷的成功");
        pospCodes.put("A5", "有缺陷的成功");
        pospCodes.put("A6", "交易成功，请向资金到账行确认");
        pospCodes.put("A7", "安全处理失败");
        pospCodes.put("A8", "未上送“转入卡归属地信息”");
        pospCodes.put("A9", "-");
        pospCodes.put("B0", "支付中”");
        pospCodes.put("23", "交易金额过小");
    }

    public enum CODE {
        OK,
        FAIL,
        AUTH_FAIL,
        ACCOUNT_LOCK,
        NEED_LOGIN,
        PARAMS_ERROR,
        SYSTEM_ERROR,
        DATA_NOT_EXISTS,
        DATA_ALREADY_EXISTS,
        TIMEOUT, // 发卡行响应超时
        BALANCE_NOT_ENOUGH, // 余额不足
        PASSWORD_ERROR, // 密码错误
        PASSWORD_COUNT_LIMIT, // 密码输入次数超限
        NOT_CONFIRMED,
        LIMIT_FAIL,
        ACCOUNT_LIMIT,
        LIMIT_TRANSACTION,
        RISK_VALUE,
        ACTIVATE_FAIL,
        ORDERED,
        REPEATED_VALUE,//重复值
        P00,
        P01,
        P03,
        P04,
        P05,
        P10,
        P11,
        P12,
        P13,
        P14,
        P15,
        P21,
        P22,
        P23,
        P25,
        P30,
        P34,
        P38,
        P40,
        P41,
        P43,
        P45,
        P51,
        P54,
        P55,
        P57,
        P58,
        P59,
        P61,
        P62,
        P64,
        P65,
        P68,
        P75,
        P77,
        P90,
        P91,
        P92,
        P94,
        P96,
        P97,
        P98,
        P99,
        PA0,
        PA1,
        PA2,
        PA3,
        PA4,
        PA5,
        PA6,
        PA7,
        PA8,
        PA9,
        //1000-请采集手机号后4位
        FACE_PAY_P1000,
        //支付中
        B0,
        SMS_SEND_LIMIT;
    }

    public Response() {

    }


    public Response(CODE code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(Response response) {
        this.code = response.code;
        this.message = response.message;
    }

    public void setResponse(Response response) {
        this.code = response.code;
        this.message = response.message;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

    @JsonIgnore
    public boolean isOk() {
        return code == CODE.OK;
    }

    @JsonIgnore
    public boolean isTransLimitClose() {
        return code == CODE.LIMIT_TRANSACTION;
    }
    @JsonIgnore
    public boolean isLimitFail(){
        return code == CODE.LIMIT_FAIL;
    }



    @JsonIgnore
    public boolean isRiskValue() {
        return code == CODE.RISK_VALUE;
    }

    @JsonIgnore
    public boolean isOrdered() {
        return code == CODE.ORDERED;
    }

    @JsonIgnore
    public boolean isActivateFail() {
        return code == CODE.ACTIVATE_FAIL;
    }

    @JsonIgnore
    public boolean isNotConfirmed() {
        return code == CODE.NOT_CONFIRMED;
    }

    @JsonIgnore
    public boolean isNumsMore() {
        return code == CODE.P65;
    }

    public static Response pos(String code) {
        if(pospCodes.containsKey(code)) {
            if(code.equals("00")) {
                return Response.ok();
            }
            return new Response(CODE.valueOf("P" + code), pospCodes.get(code));
        }

        return fail("POS其他错误");
    }

    public static Response pos(String code, String message) {
        if(pospCodes.containsKey(code)) {
            if(code.equals("00")) {
                return Response.ok();
            }
            return new Response(CODE.valueOf("P" + code), pospCodes.get(code));
        }

        return fail(message);
    }

    public String posCode() {
        if(this.isOk()) {
            return "00";
        }
        else if(this.isOrdered()) {
            return "B1";
        }

        if(this.code == CODE.FAIL) {
            return "98";
        }

        if(this.code.name().endsWith("NOT_CONFIRMED")) {
            return "B0";
        }

        String code = this.code.toString();
        return code.substring(1);
    }

    public static Response notConfirmed(String message) {
        return new Response(CODE.NOT_CONFIRMED, message);
    }

    public static Response timeout(String message) {
        return new Response(CODE.TIMEOUT, message);
    }

    public static Response ok(String message) {
        return new Response(CODE.OK, message);
    }

    public static Response ordered(String message) {
        return new Response(CODE.ORDERED, message);
    }

    public static Response ok() {
        return new Response(CODE.OK, "成功");
    }

    public static Response paramsError(String message) {
        return new Response(CODE.PARAMS_ERROR, message);
    }

    public static Response dataNotExists(String message) {
        return new Response(CODE.DATA_NOT_EXISTS, message);
    }

    public static Response needLogin(String message) {
        return new Response(CODE.NEED_LOGIN, message);
    }

    public static Response smsLimit(String message) {
        return new Response(CODE.SMS_SEND_LIMIT, message);
    }

    public static Response fail(String message) {
        return new Response(CODE.FAIL, message);
    }
    public static Response activateFail(String message){
        return new Response(CODE.ACTIVATE_FAIL,message);
    }

    public static Response limitFail(String message) {
        return new Response(CODE.LIMIT_FAIL, message);
    }
    public static Response riskValue(String message) {
        return new Response(CODE.RISK_VALUE, message);
    }


    public static Response limitTransaction(String message) {
        return new Response(CODE.LIMIT_TRANSACTION, message);
    }

    public static Response systemError(String message) {
        return new Response(CODE.SYSTEM_ERROR, message);
    }

    public static Response balanceNotEnough(String message) {
        return new Response(CODE.BALANCE_NOT_ENOUGH, message);
    }

    public static Response accountLimit(String message) {
        return new Response(CODE.ACCOUNT_LIMIT, message);
    }

    public static Response authFail(String message) {
        return new Response(CODE.AUTH_FAIL, message);
    }

    public static Response accountLock(String message) {
        return new Response(CODE.ACCOUNT_LOCK, message);
    }

    public static Response repeatedValue(String message) {
        return new Response(CODE.REPEATED_VALUE, message);
    }

    public CODE getCode() {
        return code;
    }

    public void setCode(CODE code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Response notRegMerchant() {
        return pos("03");
    }

    public static Response notRegTerminal() {
        return pos("97");
    }

    public static Response notfoundOrgiTransaction() {
        return pos("25");
    }

    public static Response notSupportTransaction() {
        return pos("58");
    }

    public static Response notSupportAmount() {
        return pos("13");
    }

    public static Response notSupport() {
        return pos("40");
    }

    public static Response error() {
        return pos("98");
    }

    public static Response macWrong() {
        return pos("A0");
    }

    public static Response amountMore() {
        return pos("61");
    }
    public static Response numsMore() {
        return pos("65");
    }

    public static Response repeatRequest() {
        return pos("94");
    }

    public static Response notSupportCard() {
        return pos("14");
    }

    public static Response transClosed() {
        return pos("77");
    }

    public static Response unavailableRelationTransaction() {
        return pos("12");
    }

}
