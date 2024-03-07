package com.king.db.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SparkKey {
    private String id;

    private String appid;

    private String apikey;

    private String apisecret;

    private String rateLimit;

    private String totalUsage;
}