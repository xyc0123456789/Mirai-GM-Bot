package com.king.component;

import com.king.config.CommonConfig;
import com.king.db.pojo.AccountConfig;
import com.king.event.group.Bot2MemberEventHandler;
import com.king.event.group.Bot2MessageEventHandler;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMemberEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.File;

import static com.king.config.CommonConfig.getTargetHeartbeatStrategy;
import static com.king.config.CommonConfig.getTargetProtocol;

@Slf4j
public class Bot2 {

    public static Bot bot = null;

    public static Long qq_id;

    public void login(AccountConfig accountConfig){
        if (CommonConfig.getId()==null){
            throw new RuntimeException("配置文件错误");
        }
        Bot bot = BotFactory.INSTANCE.newBot(accountConfig.getAccount(), accountConfig.getPassword(), new BotConfiguration() {{
            // 心跳策略
            setHeartbeatStrategy(getTargetHeartbeatStrategy(accountConfig.getProtocol()));
            // 登录协议
            setProtocol(getTargetProtocol(accountConfig.getProtocol()));
            String wDir = accountConfig.getWorkingDir();
            if (!wDir.endsWith("/")){
                wDir = wDir+"/";
            }
            setWorkingDir(new File(wDir));
            setCacheDir(new File(accountConfig.getCacheDirName()));
            fileBasedDeviceInfo(accountConfig.getDeviceInfoFileName());//设备信息
            // 重定向日志到文件
            redirectBotLogToFile();
            redirectBotLogToDirectory();

            redirectNetworkLogToFile();
            redirectNetworkLogToDirectory();
        }});
        bot.login();
        log.info(accountConfig.getAccount() + " login success");
        Bot2.bot = bot;
        Bot2.qq_id = accountConfig.getAccount();
        // 创建群成员进入离开
        EventChannel<Event> groupMemberEventChannel= GlobalEventChannel.INSTANCE.filter((event) -> event instanceof GroupMemberEvent && ((GroupMemberEvent) event).getBot().getId()==qq_id);
        groupMemberEventChannel.registerListenerHost(new Bot2MemberEventHandler());

        EventChannel<Event> groupMessageEventChannel= GlobalEventChannel.INSTANCE.filter((event) -> event instanceof GroupMessageEvent&& ((GroupMessageEvent) event).getBot().getId()==qq_id);
        groupMessageEventChannel.registerListenerHost(new Bot2MessageEventHandler());

    }
}
