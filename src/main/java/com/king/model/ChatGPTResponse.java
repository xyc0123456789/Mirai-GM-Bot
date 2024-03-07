package com.king.model;

import lombok.Data;

@Data
public class ChatGPTResponse extends Response{
    public ChatGPTResponse(Response response) {
        super(response);
    }

    private String requestContent;
    private boolean math;
    private int botIndex;

}
