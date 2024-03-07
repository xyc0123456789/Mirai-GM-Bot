package com.king.db.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRequestRecord {
    private String requestNo;

    private String userId;

    private String messageId;

    private String requestDate;

    private String requestTime;

    private String role;

    private String content;
}