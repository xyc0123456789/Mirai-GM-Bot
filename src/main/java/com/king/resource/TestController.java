package com.king.resource;

import com.king.db.pojo.AccountConfig;
import com.king.db.service.AccountConfigService;
import com.king.model.MessageCacheValueDTO;
import com.king.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@ResponseBody
@RequestMapping("/test")
public class TestController extends AbstractNotify{


    @Autowired
    private AccountConfigService accountConfigService;

    @Autowired
    private CacheManager cacheManager;

    @GetMapping("test1")
    public String test1(){
        AccountConfig one = accountConfigService.getOne();
        return JsonUtil.toJson(one);
    }

    @PostMapping(value  = "put", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String put(@RequestBody Map<String,String> request){
        log.info("{}",request);
        Cache test = cacheManager.getCache("test");
        String key = request.get("key");
//        String value = request.get("value");
        Cache.ValueWrapper key1 = test.get(key);
        MessageCacheValueDTO dto = new MessageCacheValueDTO();
        if (key1!=null){
            dto = (MessageCacheValueDTO) key1.get();
        }
        dto.setCount(dto.getCount()+1);
        dto.setLastTime(System.currentTimeMillis());
        test.put(key,dto);


        return key;
    }

    @PostMapping(value  = "get", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String get(@RequestBody Map<String,String> request){
        log.info("{}",request);
        Cache test = cacheManager.getCache("test");
        String key = request.get("key");
//        String value = request.get("value");
        Cache.ValueWrapper key1 = test.get(key);
        if (key1!=null) {
            log.info(key1.get().toString());
            return key1.get().toString();
        }
        return "null";
    }

    @PostMapping(value  = "test2", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String test2(@RequestBody Map<String,String> request){
        System.out.println(request);
        return JsonUtil.toJson(request);
    }

}
