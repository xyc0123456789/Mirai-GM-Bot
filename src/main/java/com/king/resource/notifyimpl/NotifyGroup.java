package com.king.resource.notifyimpl;

import com.king.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class NotifyGroup {

    @Autowired
    private List<NotifyApi> notifyApiList;

    public void handler(Map<String,String> request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        String type = request.get("type");
        if (MyStringUtil.isEmpty(type)){
            log.info(request.toString());
            return;
        }
        for (NotifyApi notifyApi:notifyApiList){
            if (notifyApi.type().equals(type)){
                notifyApi.handler(request, httpServletRequest, httpServletResponse);
                break;
            }
        }
    }

}
