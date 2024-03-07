package com.king.resource;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
public abstract class AbstractNotify {

    public static void toResponse(String message, HttpServletResponse httpServletResponse){
        try {
            PrintWriter writer = httpServletResponse.getWriter();
            writer.write(message);
            writer.flush();
        }catch (Exception e){
            log.error("toResponse err", e);
        }
    }

}
