package com.king.resource.notifyimpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface NotifyApi {

    void handler(Map<String,String> request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    String type();

}
