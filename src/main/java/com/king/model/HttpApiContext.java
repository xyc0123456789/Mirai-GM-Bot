package com.king.model;

import lombok.Data;
import net.mamoe.mirai.message.data.QuoteReply;

@Data
public class HttpApiContext {

    private MessageEventContext messageEventContext;

    private String url;

    private int statusCode;

    private String request;

    private String response;

    private int count=0;

    private int connectTimeOut = 10000;
    private int readTimeOut = 10000;
}
