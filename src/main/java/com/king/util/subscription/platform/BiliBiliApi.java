package com.king.util.subscription.platform;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.king.model.SubscriptionInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 查询开播情况
 * @author: xyc0123456789
 * @create: 2023/3/24 21:24
 **/
@Slf4j
public class BiliBiliApi implements PlatformApi {
    private static final String url = "https://api.live.bilibili.com/room/v1/Room/get_info?room_id=";
    private static final String roomUrl = "https://live.bilibili.com/";

    public static final String platformName = "哔哩哔哩";

    public static SubscriptionInfo getBilibiliLiveStatus(String roomId) {
        String s;
        try {
            s = HttpUtil.get(url + roomId);
            JSONObject object = JSONUtil.parseObj(s);
            if (object.getInt("code") == 1) {
                log.error("live room{"+roomId+"} not exist,msg{"+object.getStr("msg")+"}");
                return null;
            }
            Object data = object.getObj("data");
            SubscriptionInfo subscriptionInfo = new SubscriptionInfo(roomId);
            subscriptionInfo.setPlatformName(platformName);
            packageData(data, subscriptionInfo);
            return subscriptionInfo;
        }catch (Exception e){
            return null;
        }
    }

    private static void packageData(Object data, SubscriptionInfo subscriptionInfo) {
        JSONObject info = JSONUtil.parseObj(data);
        String uid = info.getStr("uid");
        String roomId = info.getStr("room_id");
        Long attention = info.getLong("attention");
        Long online = info.getLong("online");
        String description = info.getStr("description");
        String areaName = info.getStr("area_name");
        String backgroundUrl = info.getStr("background");
        String title = info.getStr("title");
        String userCoverUrl = info.getStr("user_cover");
        String keyFrameUrl = info.getStr("keyframe");
        String liveTime = info.getStr("live_time");
        String tags = info.getStr("tags");
        Integer liveStatus = info.getInt("live_status");

        subscriptionInfo.setUid(uid);
        subscriptionInfo.setRoomId(roomId);
        subscriptionInfo.setAttention(attention);
        subscriptionInfo.setOnline(online);
        subscriptionInfo.setDescription(description);
        subscriptionInfo.setAreaName(areaName);
        subscriptionInfo.setBackgroundUrl(backgroundUrl);
        subscriptionInfo.setTitle(title);
        subscriptionInfo.setUserCoverUrl(userCoverUrl);
        subscriptionInfo.setKeyframeUrl(keyFrameUrl);
        subscriptionInfo.setLiveStatus(liveStatus);
        subscriptionInfo.setLiveTime(liveTime);
        subscriptionInfo.setTags(tags);
        subscriptionInfo.setRoomUrl(roomUrl+roomId);
    }

    public static void main(String[] args) {
        SubscriptionInfo subscriptionInfo = getBilibiliLiveStatus("10333473");
        System.out.println(subscriptionInfo);
    }

    @Override
    public SubscriptionInfo getLiveStatus(String roomId) {
        return getBilibiliLiveStatus(roomId);
    }

    @Override
    public String platformName() {
        return platformName;
    }
}
