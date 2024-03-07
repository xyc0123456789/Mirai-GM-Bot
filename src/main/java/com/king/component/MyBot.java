package com.king.component;

import com.king.config.CommonConfig;
import com.king.event.friend.MyFriendEventMessageHandler;
import com.king.event.group.MyGroupMemberEventHandler;
import com.king.event.group.MyGroupMessageEventHandler;
import com.king.event.group.MyGroupMessageRecallEventHandler;
import com.king.event.group.MyGroupTempMessageEventHandler;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class MyBot {

    public static Bot bot = null;

    public static Long MyBotQQId;

    @Autowired
    private MyFriendEventMessageHandler friendEventHandler;
    @Autowired
    private MyGroupMessageEventHandler groupMessageHandler;
    @Autowired
    private MyGroupMemberEventHandler groupMemberHandler;

    @Autowired
    private MyGroupTempMessageEventHandler groupTempMessageEventHandler;

    @Autowired
    private MyGroupMessageRecallEventHandler groupMessageRecallEventHandler;

    public void login(){
        if (CommonConfig.getId()==null){
            throw new RuntimeException("配置文件错误");
        }

        long qq = CommonConfig.getAccount();
        String password = CommonConfig.getPassword();
        log.info("login qq:{}", qq);
        Bot bot = BotFactory.INSTANCE.newBot(qq, password, new BotConfiguration() {{
            // 心跳策略
            setHeartbeatStrategy(CommonConfig.getHeartBeatStrategy());
            // 登录协议
            setProtocol(CommonConfig.getProtocol());

            // 工作路径
//            String path = ApplicationMain.class.getProtectionDomain().getCodeSource().getLocation().getFile();
//            String path = "/home/appuser/";
            setWorkingDir(new File(CommonConfig.getWorkingDir()));
            setCacheDir(new File(CommonConfig.getCacheDir()));
            fileBasedDeviceInfo(CommonConfig.getDeviceInfoName());//设备信息

            // 重定向日志到文件
            redirectBotLogToFile();
            redirectBotLogToDirectory();

            redirectNetworkLogToFile();
            redirectNetworkLogToDirectory();

        }});
//        CommonConfig.init();
        bot.login();
        log.info(qq + " login success");
        MyBot.bot = bot;
        MyBot.MyBotQQId = qq;
        // 创建群成员进入离开
        EventChannel<Event> groupMemberEventChannel= GlobalEventChannel.INSTANCE.filter((event) ->{
            //                GroupMemberEvent memberEvent = (GroupMemberEvent) event;
            //                return groups.contains(memberEvent.getGroup().getId());
            return event instanceof GroupMemberEvent && ((GroupMemberEvent) event).getBot().getId()==MyBotQQId;
        });
        groupMemberEventChannel.registerListenerHost(groupMemberHandler);

        EventChannel<Event> groupMessageEventChannel= GlobalEventChannel.INSTANCE.filter((event) ->{
            //                GroupMessageEvent messageEvent = (GroupMessageEvent) event;
            //                long groupId = messageEvent.getGroup().getId();
            return event instanceof GroupMessageEvent && ((GroupMessageEvent) event).getBot().getId()==MyBotQQId;
        });
        groupMessageEventChannel.registerListenerHost(groupMessageHandler);


        EventChannel<Event> friendEventChannel= GlobalEventChannel.INSTANCE.filter((event) ->{
            //                FriendMessageEvent friendMessageEvent = (FriendMessageEvent) event;
            //                return friends.contains(friendMessageEvent.getFriend().getId());
            return event instanceof FriendMessageEvent && ((FriendMessageEvent) event).getBot().getId()==MyBotQQId;
        });
        friendEventChannel.registerListenerHost(friendEventHandler);

        EventChannel<Event> groupTempMessageEventChannel= GlobalEventChannel.INSTANCE.filter((event) ->{
            return event instanceof GroupTempMessageEvent && ((GroupTempMessageEvent) event).getBot().getId()==MyBotQQId;
        });
        groupTempMessageEventChannel.registerListenerHost(groupTempMessageEventHandler);

        EventChannel<Event> messageRecallEventEventChannel= GlobalEventChannel.INSTANCE.filter((event) ->{
            return event instanceof MessageRecallEvent.GroupRecall && ((MessageRecallEvent.GroupRecall) event).getBot().getId()==MyBotQQId;
        });
        messageRecallEventEventChannel.registerListenerHost(groupMessageRecallEventHandler);
    }


}
