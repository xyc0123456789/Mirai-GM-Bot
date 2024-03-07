package com.king.db.service;


import com.king.db.pojo.Subscription;
import com.king.db.pojo.SubscriptionExample;
import com.king.db.subdao.SubSubscriptionMapper;
import com.king.model.PlatformEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Service
public class SubscriptionService {

    @Autowired
    private SubSubscriptionMapper subSubscriptionMapper;


    //获取全部订阅列表
    public List<Subscription> getAll(){
        SubscriptionExample example = new SubscriptionExample();
        SubscriptionExample.Criteria criteria = example.createCriteria();
        criteria.andEnableEqualTo(true);
        return subSubscriptionMapper.selectByExample(example);
    }

    public List<Subscription> getByGroup(Long groupId){
        SubscriptionExample example = new SubscriptionExample();
        SubscriptionExample.Criteria criteria = example.createCriteria();
        criteria.andGroupIdEqualTo(groupId);
        criteria.andEnableEqualTo(true);
        return subSubscriptionMapper.selectByExample(example);
    }

    //按群分组分别通知
    public Set<Long> getGroupsBySubscrip(String platform, String roomId){
        SubscriptionExample example = new SubscriptionExample();
        SubscriptionExample.Criteria criteria = example.createCriteria();
        criteria.andEnableEqualTo(true);
        criteria.andPlatformEqualTo(platform);
        criteria.andRoomIdEqualTo(roomId);
        List<Subscription> subscriptions = subSubscriptionMapper.selectByExample(example);
        Set<Long> groupSet = new HashSet<>();
        for (Subscription subscription : subscriptions){
            groupSet.add(subscription.getGroupId());
        }
        return groupSet;
    }


    public Set<Long> getIdsByGroupAndRoom(String platform,String roomId,Long groupId){
        SubscriptionExample example = new SubscriptionExample();
        SubscriptionExample.Criteria criteria = example.createCriteria();
        criteria.andEnableEqualTo(true);
        criteria.andPlatformEqualTo(platform);
        criteria.andRoomIdEqualTo(roomId);
        criteria.andGroupIdEqualTo(groupId);
        List<Subscription> subscriptions = subSubscriptionMapper.selectByExample(example);
        Set<Long> idSet = new HashSet<>();
        for (Subscription subscription : subscriptions){
            idSet.add(subscription.getQqId());
        }
        return idSet;
    }

    //添加
    public void add(PlatformEnum platform, String roomId, Long groupId, Long qqId){
        Subscription subscription = new Subscription();
        subscription.setCtime(new Date());
        subscription.setEnable(true);
        subscription.setPlatform(platform.api.platformName());
        subscription.setRoomId(roomId);
        subscription.setGroupId(groupId);
        subscription.setQqId(qqId);
        subscription.setIsGroup(!Objects.equals(groupId, qqId));
        Subscription ori = subSubscriptionMapper.selectByPrimaryKey(groupId, platform.api.platformName(), roomId, qqId);
        if (ori==null) {
            subSubscriptionMapper.insert(subscription);
        }else {
            update(platform,roomId,groupId,qqId,true);
        }
    }


    //修改
    public boolean update(PlatformEnum platform,String roomId,Long groupId,Long qqId, boolean enable){
        Subscription ori = subSubscriptionMapper.selectByPrimaryKey(groupId, platform.api.platformName(), roomId, qqId);
        if (ori==null) {
            return false;
        }
        Subscription subscription = new Subscription();
        subscription.setEnable(enable);
        subscription.setPlatform(platform.api.platformName());
        subscription.setRoomId(roomId);
        subscription.setGroupId(groupId);
        subscription.setQqId(qqId);
        subscription.setUtime(new Date());
        subscription.setIsGroup(!Objects.equals(groupId, qqId));
        subSubscriptionMapper.updateByPrimaryKeySelective(subscription);
        return true;
    }
}