package com.hfut.stock.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hfut.stock.constan.StockConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Author:yuyang
 * Date:2024-04-24
 * Time:23:57
 */
@Configuration
//开启SpringCache缓存注解功能支持
@EnableCaching
public class CacheConfig {
    @Bean
    public RedisTemplate redisTemplate(@Autowired RedisConnectionFactory redisConnectionFactory){
        //1.构建RedisTemplate模板对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        //2.为不同的数据结构设置不同的序列化方案
        //设置key序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        //设置value序列化方式
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        //设置hash中field字段序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        //设置hash中value的序列化方式
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        //5.初始化参数设置
        template.afterPropertiesSet();
        return template;
    }
    /**
     * 构建缓存bean
     * @return
     */
    @Bean
    public Cache<String,Object> caffeineCache(){
        Cache<String, Object> cache = Caffeine
                .newBuilder()
                .maximumSize(200)//设置缓存数量上限
//                .expireAfterAccess(1, TimeUnit.SECONDS)//访问1秒后删除
//                .expireAfterWrite(1,TimeUnit.SECONDS)//写入1秒后删除
                .initialCapacity(100)// 初始的缓存空间大小
                .recordStats()//开启统计
                .build();
        return cache;
    }
    /**
     * 定义序列化方式
     * @return
     */
    @Bean
    public StringRedisSerializer stringRedisSerializer(){
        return new StringRedisSerializer();
    }

    /**
     * 配置 cacheManager 代替默认的cacheManager （缓存管理器）
     * 当前使用的redis缓存做为底层实现，如果将来想替换缓存方案，那么只需调整CacheManager的实现细节即可
     * 其他代码无需改动
     * @param factory RedisConnectionFactory
     * @return  CacheManager
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        //定义redis数据序列化的对象
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        //jackson序列化方式对象
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        //设置被序列化的对象的属性都可访问：暴力反射
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //仅仅序列化对象的属性，且属性不可为final修饰
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        serializer.setObjectMapper(objectMapper);
        // 配置key value序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                //关闭控制存储--》禁止缓存value为null的数据
                .disableCachingNullValues()
                //修改前缀与key的间隔符号，默认是::  eg:name:findById
                .computePrefixWith(cacheName->cacheName+":");

        //设置特有的Redis配置
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        //定制化的Cache 设置过期时间 eg:以role：开头的缓存存活时间为10s
//        cacheConfigurations.put("role",customRedisCacheConfiguration(config,Duration.ofSeconds(20)));
        cacheConfigurations.put(StockConstant.STOCK,customRedisCacheConfiguration(config, Duration.ofHours(24)));
        //构建redis缓存管理器
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                //Cache事务支持,保证reids下的缓存与数据库下的数据一致性
                .transactionAware()
                .withInitialCacheConfigurations(cacheConfigurations)
                .cacheDefaults(config)
                .build();
        //设置过期时间
        return cacheManager;
    }

    /**
     * 设置RedisConfiguration配置
     * @param config
     * @param ttl
     * @return
     */
    public RedisCacheConfiguration customRedisCacheConfiguration(RedisCacheConfiguration config, Duration ttl) {
        //设置缓存缺省超时时间
        return config.entryTtl(ttl);
    }
}
