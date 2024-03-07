package com.king.resource;

import com.king.db.pojo.AccountConfig;
import com.king.db.service.AccountConfigService;
import com.king.resource.notifyimpl.NotifyGroup;
import com.king.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
@ResponseBody
@RequestMapping("/notify")
public class NotifyResource extends AbstractNotify{


    @Autowired
    private NotifyGroup notifyGroup;

    @PostMapping(value  = "notify", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void notify(@RequestBody Map<String,String> request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String type = httpServletRequest.getHeader("type");
        request.put("type", type);
        notifyGroup.handler(request,httpServletRequest,httpServletResponse);
    }

}
