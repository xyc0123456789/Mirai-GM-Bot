package com.king.db.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpenAIApiKey {
    private String apiKey;

    private Long rateLimit;

    private String hardLimitUsd;

    private String totalUsage;
}