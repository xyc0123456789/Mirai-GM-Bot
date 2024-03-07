package com.king.util;

import java.util.HashMap;
import java.util.Map;

public class ExtDataUtil {

    public static String putValue(String oriExtData,String... kv){
        if(kv == null || kv.length % 2 != 0) {
            throw new RuntimeException("参数长度错误");
        }
        Map map = JsonUtil.fromJson(oriExtData, Map.class);
        for(int i = 0; i < kv.length / 2; i++) {
            map.put(kv[i * 2], kv[i * 2 +1]);
        }
        return JsonUtil.toJson(map);
    }

    public static String getValue(String extData,String key){

        Map map = JsonUtil.fromJson(extData, Map.class);
        return (String) map.get(key);
    }

}
