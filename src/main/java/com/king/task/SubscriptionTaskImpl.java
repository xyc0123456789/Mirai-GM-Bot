package com.king.task;

import com.king.component.MyBot;
import com.king.db.pojo.Subscription;
import com.king.db.service.SubscriptionService;
import com.king.model.PlatformEnum;
import com.king.model.SubscriptionInfo;
import com.king.util.FileUtil;
import com.king.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 实际行为
 * @author: xyc0123456789
 * @create: 2023/3/24 22:38
 **/
@Slf4j
@Component
public class SubscriptionTaskImpl {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostConstruct
    private void init(){
        reload();
    }

    private static final AtomicInteger count = new AtomicInteger(0);

    //维护一个订阅列表，其他等到状态变化了再进行查询
    private static final Map<String, Boolean> statusMap = new Hashtable<>();

    public void add(PlatformEnum platform,String roomId, boolean status){
        statusMap.put(platform.api.platformName()+"-"+roomId, status);
    }

    public void remove(PlatformEnum platform,String roomId){
        statusMap.remove(platform.api.platformName()+"-"+roomId);
    }

    private static final ExecutorService poolExecutor = Executors.newFixedThreadPool(3);

    public void reload(){
        List<Subscription> all = subscriptionService.getAll();
        Map<String, Boolean> tmp = new HashMap<>();
        if (!CollectionUtils.isEmpty(all)) {
            for (Subscription subscription : all) {
                tmp.put(subscription.getPlatform() + "-" + subscription.getRoomId(), false);
            }
        }
        statusMap.clear();
        statusMap.putAll(tmp);
        doTask(false);
    }

    public void doTask(boolean send){
        log.info("do task:{}",statusMap);
        List<SubscriptionInfo> allStatus = getAllStatus();
        //查找状态变化的直播
        for (SubscriptionInfo subscriptionInfo: allStatus){
            String key = subscriptionInfo.getPlatformName()+"-"+subscriptionInfo.getRoomId();
            Boolean status = statusMap.get(key);
            if (status==null){
                log.error("key: {} not found", key);
                continue;
            }
            boolean newStatus = subscriptionInfo.getLiveStatus() == 1;
            if (send && !status && newStatus){
                sendNotifys(subscriptionInfo);
            }
            if (!status.equals(newStatus)){
                statusMap.put(key, newStatus);
            }
        }

        int countAndAdd = count.getAndAdd(1);
        if (countAndAdd%10==0){
            count.getAndSet(1);
            for (SubscriptionInfo subscriptionInfo: allStatus){
                log.info("platform: {} roomId: {} title: {} 状态: {}", subscriptionInfo.getPlatformName(), subscriptionInfo.getRoomId(), subscriptionInfo.getTitle(), subscriptionInfo.getLiveStatus());
            }
        }
    }

    public List<SubscriptionInfo> getAllStatus(){
        List<SubscriptionInfo> allStatus = new CopyOnWriteArrayList<>();
        int tmpCount = 0;
        for (String key:statusMap.keySet()){
            int index = key.indexOf("-");
            String platform = key.substring(0, index);
            String roomId = key.substring(index+1);
            PlatformEnum platformEnum = PlatformEnum.getByName(platform);
            if (platformEnum==null){
                continue;
            }
            poolExecutor.submit(()->{
                SubscriptionInfo liveStatus = platformEnum.api.getLiveStatus(roomId);
                if (liveStatus!=null) {
                    allStatus.add(liveStatus);
                }
            });
            tmpCount++;
            try {Thread.sleep(500);} catch (InterruptedException ignore) {}
        }
        return allStatus;
    }

    /**
     * 发送给所有订阅者
     * @param subscriptionInfo
     */
    private void sendNotifys(SubscriptionInfo subscriptionInfo){
        try {
            Set<Long> groupsBySubscrip = subscriptionService.getGroupsBySubscrip(subscriptionInfo.getPlatformName(), subscriptionInfo.getRoomId());
            if (!CollectionUtils.isEmpty(groupsBySubscrip)) {
                for (long groupId : groupsBySubscrip) {
                    sendNotify(subscriptionInfo, groupId, true);
                }
            }
        }catch (Exception e){
            log.error("sendNotify err", e);
        }
    }

    /**
     * 发送至指定群
     * @param subscriptionInfo
     * @param contactId
     */
    public void sendNotify(SubscriptionInfo subscriptionInfo, Long contactId, boolean atFlag){
        File image=null;
        Contact contact;
        boolean isGroup = false;
        try {
            Group group = MyBot.bot.getGroup(contactId);
            if (group==null){
                contact = MyBot.bot.getFriend(contactId);
                if (contact==null) {
                    return;
                }
            }else {
                isGroup = true;
                contact = group;
            }
            //基本信息
            MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
            messageChainBuilder.append(subscriptionInfo.constructMessage());
            //at
            if (isGroup&&atFlag) {
                Set<Long> memberIds = subscriptionService.getIdsByGroupAndRoom(subscriptionInfo.getPlatformName(), subscriptionInfo.getRoomId(), contactId);
                if (!CollectionUtils.isEmpty(memberIds)) {
                    for (Long id : memberIds) {
                        messageChainBuilder.append(new At(id));
                    }
                }
            }
            //图
            String keyframeUrl = subscriptionInfo.getKeyframeUrl();
            if (MyStringUtil.isEmpty(keyframeUrl)){
                keyframeUrl = subscriptionInfo.getUserCoverUrl();
            }
            if (!MyStringUtil.isEmpty(keyframeUrl)) {
                image = FileUtil.downLoadImage(keyframeUrl, null);
                if (image != null) {
                    Image imageToSend = Contact.uploadImage(contact, image);
                    messageChainBuilder.append(imageToSend);
                }
            }
            contact.sendMessage(messageChainBuilder.build());
        }catch (Exception e){
            log.error("sendNotify "+contactId ,e);
        }finally {
            FileUtil.deleteFile(image);
        }
    }

}
