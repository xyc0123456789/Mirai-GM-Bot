package com.king.util.subscription.platform;


import com.king.model.SubscriptionInfo;

/**
 * @Author nwl20
 * @create 2022/10/16 16:12
 */
public interface PlatformApi {
    SubscriptionInfo getLiveStatus(String roomId);


    String platformName();
}
