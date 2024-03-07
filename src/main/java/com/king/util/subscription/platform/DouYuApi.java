package com.king.util.subscription.platform;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.king.model.SubscriptionInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: TODO
 * @author: xyc0123456789
 * @create: 2023/3/25 12:37
 **/
@Slf4j
public class DouYuApi implements PlatformApi {

    private static final String url = "https://web.sinsyth.com/lxapi/douyujx.x?rid=";
    private static final String roomUrl = "https://www.douyu.com/";

    public static final String platformName = "斗鱼";

    @Override
    public SubscriptionInfo getLiveStatus(String roomId) {
        return getDouYuLiveStatus(roomId);
    }

    @Override
    public String platformName() {
        return platformName;
    }

    public static SubscriptionInfo getDouYuLiveStatus(String roomId) {
        try {
            String res = HttpUtil.get(url + roomId);
            JSONObject result = JSONUtil.parseObj(res);
            SubscriptionInfo subscriptionInfo = new SubscriptionInfo(roomId);
            subscriptionInfo.setPlatformName(platformName);
            packageData(result, subscriptionInfo);
            if (subscriptionInfo.getLiveStatus()==-1){
                return null;
            }
            return subscriptionInfo;
        }catch (Exception e){
            log.error("getDouYuLiveStatus err", e);
            return null;
        }
    }

    private static void packageData(JSONObject result,SubscriptionInfo subscription) {
        if (result.getStr("state").equalsIgnoreCase("no")) {
            subscription.setLiveStatus(0);
        } else if (result.getStr("state").equalsIgnoreCase("success")) {
            subscription.setLiveStatus(1);
            JSONObject data = result.getJSONObject("Rendata").getJSONObject("data");
            JSONObject dataToUse = JSONUtil.parseObj(UnicodeUtil.toString(data.toString()).replaceAll("\\\\", ""));
            String roomId = dataToUse.getStr("vid");
            String title = dataToUse.getStr("roomName");
            String nickname = dataToUse.getStr("nickname");
            String avatar = dataToUse.getStr("avatar");
            String keyframeUrl = dataToUse.getStr("roomimg");
            String description = dataToUse.getStr("roominfo");
            subscription.setRoomId(roomId);
            subscription.setTitle(title);
            subscription.setUserName(nickname);
            subscription.setUserCoverUrl(avatar);
            subscription.setKeyframeUrl(keyframeUrl);
            subscription.setDescription(description);
            subscription.setRoomUrl(roomUrl+roomId);
        }else {
            subscription.setLiveStatus(-1);
        }
    }

    public static void main(String[] args) {
        System.out.println(getDouYuLiveStatus("156277"));
    }

}
