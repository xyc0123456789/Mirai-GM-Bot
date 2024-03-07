package com.king.model;

import lombok.Data;

@Data
public class CommonResponse extends Response{
    private String bizCode;
    private String bizMessage;
}
