package com.king.model;

import com.king.util.subscription.platform.BiliBiliApi;
import com.king.util.subscription.platform.DouYuApi;
import com.king.util.subscription.platform.PlatformApi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author nwl20
 * @create 2022/10/14 11:03
 */
public enum PlatformEnum {
    BILIBILI(new BiliBiliApi(), new String[]{"b站","bili","bilibili","B站"}),
    DOUYU(new DouYuApi(),new String[]{"斗鱼","douyu"}),
    ;


    PlatformEnum(PlatformApi api, String[] aliasName) {
        this.api = api;
        this.aliasName = aliasName;
    }

    public PlatformApi api;

    public String[] aliasName;

    public static final Map<String, PlatformEnum> enumMap = new HashMap<>();

    static {
        PlatformEnum[] values = PlatformEnum.values();
        for (PlatformEnum platformEnum: values){
            enumMap.put(platformEnum.name(), platformEnum);
            enumMap.put(platformEnum.api.platformName(), platformEnum);
            for (String s:platformEnum.aliasName){
                enumMap.put(s, platformEnum);
            }
        }
    }


    public static PlatformEnum getByName(String platform){
        return enumMap.get(platform);
    }


}
