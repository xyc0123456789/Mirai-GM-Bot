package com.king.model;

import lombok.Data;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/11 21:56
 **/
@Data
public class NewBingRequest {
    private String message;
    private String jailbreakConversationId;
    private String parentMessageId;
    private String conversationSignature;
}
