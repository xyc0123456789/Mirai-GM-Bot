package com.king.api.messageapis.group;

import com.king.config.CommonConfig;
import com.king.model.CommonResponse;
import com.king.model.GroupId;
import com.king.model.MessageCacheValueDTO;
import com.king.model.MessageEventContext;
import com.king.util.MyStringUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.king.config.CommonConfig.redisFlag;

@Slf4j
@Component
public class FloodScreen extends AbstractGroupMessageApi {

    @Autowired
    private CacheManager cacheManager;

    private Cache messageCache;

    private static final String MessageKeyCache = "MessageKeyCache";

    @PostConstruct
    private void init() {
        messageCache = cacheManager.getCache(MessageKeyCache);
    }


    @Override
    public boolean condition(MessageEventContext messageEventContext) {
        return redisFlag.get();
    }

    @Override
    public CommonResponse handler(MessageEventContext messageEventContext) {
        MessageEvent messageEvent = messageEventContext.getMessageEvent();
        Contact subject = messageEvent.getSubject();
        String content = messageEventContext.getMessageEvent().getMessage().serializeToMiraiCode();
        ArrayList<Image> images = messageEventContext.getImages();
        String keyName;
        Set<String> set = new HashSet<>();
        if (subject.getId()== GroupId.AIQUN){
            return null;
        }

        if (subject instanceof Group) {
            Group group = (Group) subject;
            keyName = group.getId() + "-" + messageEvent.getSender().getId() + "-";
        } else {
            return null;
        }
        if (!MyStringUtil.isEmpty(content) && !content.contains(CommonConfig.VIDEO_MESSAGE) && !content.contains(CommonConfig.HONGBAO_MESSAGE)) {
            String plainKey = keyName + content;
            set.add(plainKey);
        }
        if (!CollectionUtils.isEmpty(images)) {
            for (Image image : images) {
                String imageContent = image.getImageId();
                set.add(keyName + imageContent);
            }
        }
        if (MyStringUtil.isEmpty(content) && CollectionUtils.isEmpty(images)){
            set.add(keyName+"特殊动画表情等不支持解析的格式jgisbduibsdsji");
        }

        int maxInt = 0;
        if (CollectionUtils.isEmpty(set)){
            return null;
        }
        for (String key : set) {
            maxInt = handlerMessage(key, messageEventContext, maxInt);
        }
        return null;
    }

    private static final int recallCount = 3;
    private static final int kickCount = 5;

    private int handlerMessage(String key, MessageEventContext messageEventContext, int maxInt) {
        try {
            MessageCacheValueDTO messageCacheValueDTO = cacheAddCount(messageCache, key);
            int count = messageCacheValueDTO.getCount();
            if (maxInt<recallCount && count >=recallCount){
                log.info("[" + key + "] 因为重复消息被撤回了");
                recall(messageEventContext);
            }
            MessageEvent messageEvent = messageEventContext.getMessageEvent();
            if (maxInt < kickCount&& count ==kickCount-1){
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.append(new At(messageEvent.getSender().getId())).append("请注意重复消息发言间隔，即刻起15s内再次重复将被踢出群聊");
                messageEvent.getSubject().sendMessage(builder.build());
            }
            if (maxInt < kickCount&& count >=kickCount){
                kick(messageEvent);
                log.info("[" + key + "] has been kicked");
            }
            return Math.max(count, maxInt);
        }catch (Exception e){
            log.info(key + " handle err", e);
        }
        return 0;
    }

    public static MessageCacheValueDTO cacheAddCount(Cache cache, String key) {
        Cache.ValueWrapper valueWrapper;
        try {
            valueWrapper = cache.get(key);
        }catch (RedisConnectionFailureException e){
            log.error("RedisConnectionFailureException err: "+e.getMessage());
            redisFlag.getAndSet(false);
            throw e;
        }
        MessageCacheValueDTO messageCacheValueDTO = null;
        try {
            if (valueWrapper != null && valueWrapper.get() != null) {
                messageCacheValueDTO = (MessageCacheValueDTO) valueWrapper.get();
                assert messageCacheValueDTO != null;
            } else {
                messageCacheValueDTO = new MessageCacheValueDTO();
            }

            messageCacheValueDTO.setLastTime(System.currentTimeMillis());
            messageCacheValueDTO.setCount(messageCacheValueDTO.getCount() + 1);
            cache.put(key, messageCacheValueDTO);
        }catch (Exception e){
            log.error("cacheAddCount err", e);
        }
        return messageCacheValueDTO;
    }

    @Override
    public int sortedOrder() {
        return 0;
    }

    @Override
    public String commandName() {
        return "flood.screen";
    }
}
