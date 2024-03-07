package com.king.model;

import com.king.util.MyStringUtil;
import lombok.Data;
import net.mamoe.mirai.message.code.MiraiCode;

@Data
public class SubscriptionInfo {

    public SubscriptionInfo() {
    }

    public SubscriptionInfo(String roomId) {
        this.roomId = roomId;
    }

    private String uid;
    private String userName;
    private String platformName;
    private String roomId;
    private Long attention;
    private Long online;
    private String description;
    private Integer liveStatus = 0; //默认不在直播
    private String areaName;
    private String backgroundUrl;
    private String title;
    private String userCoverUrl;
    private String keyframeUrl;
    private String liveTime;
    private String tags;
    private String roomUrl;


    public String constructMessage(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("平台-房间号: ").append(this.getPlatformName()).append(" - ").append(this.getRoomId()).append("\n")
                .append("标题: ").append(this.getTitle()).append("\n");
        if (!MyStringUtil.isEmpty(this.getUserName())){
            stringBuilder.append("主播昵称: ").append(this.getUserName()).append("\n");
        }
        stringBuilder.append("直播状态: ").append(this.getLiveStatusStr()).append("\n");
        if (!MyStringUtil.isEmpty(this.getAreaName())) {
            stringBuilder.append("分区名称: ").append(this.getAreaName()).append("\n");
        }
        if (this.getAttention()!=null) {
            stringBuilder.append("关注人数: ").append(this.getAttention()).append("\n");
        }
        if (this.getOnline()!=null) {
            stringBuilder.append("直播在线: ").append(this.getOnline()).append("\n");
        }
        stringBuilder.append("直播链接: ").append(this.getRoomUrl()).append("\n");
        return stringBuilder.toString();
    }

    public String getLiveStatusStr(){
        Integer status = this.getLiveStatus();
        if (status.equals(0)){
            return "未开播";
        } else if (status.equals(1)) {
            return "已开播";
        }else if (status.equals(2)){
            return "轮播中，未开播";
        }else {
            return String.valueOf(status);
        }
    }

}
