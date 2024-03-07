package com.king.config;


import com.king.component.Bot2;
import com.king.component.MyBot;
import com.king.db.pojo.AccountConfig;
import com.king.db.service.AccountConfigService;
import com.king.db.service.CommandPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
@Slf4j
public class ApplicationConfig  implements ApplicationListener<ApplicationStartedEvent> {


    public static final String MESSAGE_CONTENT_CACHE = "MESSAGE_CONTENT_CACHE";
    public static final String PDF_CONTENT_CACHE = "PDF_CONTENT_CACHE";



    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration defaultConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                // 设置缓存有效期15s
                .entryTtl(Duration.ofSeconds(15))
                // 配置 key 序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(RedisSerializer.string()))
                // 配置 value 序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(RedisSerializer.json()));

        RedisCacheConfiguration messageCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                // 设置缓存有效期15s
                .entryTtl(Duration.ofDays(1))
                // 配置 key 序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(RedisSerializer.string()))
                // 配置 value 序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(RedisSerializer.json()));

        RedisCacheConfiguration pdfCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                // 设置缓存有效期30min
                .entryTtl(Duration.ofMinutes(30))
                // 配置 key 序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(RedisSerializer.string()))
                // 配置 value 序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(RedisSerializer.json()));

        return RedisCacheManager.builder(factory)
                // 设置默认的策略
                .cacheDefaults(defaultConfiguration)
                // 设置指定 cache 的策略
                 .withCacheConfiguration(MESSAGE_CONTENT_CACHE, messageCacheConfiguration)
                .withCacheConfiguration(PDF_CONTENT_CACHE, pdfCacheConfiguration)
                .build();
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        AccountConfigService accountConfigService = applicationStartedEvent.getApplicationContext().getBean(AccountConfigService.class);
        AccountConfig accountConfig = accountConfigService.getOne();
        CommonConfig.init(accountConfig);
        MyBot myBot = applicationStartedEvent.getApplicationContext().getBean(MyBot.class);
        myBot.login();

        AccountConfig bot2Config = accountConfigService.getById(222);
        if (bot2Config!=null) {
            Bot2 bot2 = new Bot2();
            bot2.login(bot2Config);
        }

        CommandPermissionService commandPermissionService = applicationStartedEvent.getApplicationContext().getBean(CommandPermissionService.class);
        commandPermissionService.reloadPermission();
    }
}
