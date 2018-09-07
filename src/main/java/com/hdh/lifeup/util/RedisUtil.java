package com.hdh.lifeup.util;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * RedisUtil class<br/>
 *
 * @author hdonghong
 * @since 2018/09/07
 */
@Component
public class RedisUtil {

    private StringRedisTemplate redisTemplate;

    @Autowired
    public RedisUtil(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Key（键）

    /**
     * 返回指定格式的key 集合，比如 keys *
     * @param pattern 指定格式的key
     * @return key 集合
     */
    public Set<String> keys(@NonNull String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 实现命令：TTL key，以秒为单位，返回给定 key的剩余生存时间(TTL, time to live)。
     * -1表示永不过期，-2表示不存在
     * @param key 键
     * @return 剩余生存时间，单位秒
     */
    public long ttl(@NonNull String key) {
        return Optional.ofNullable(redisTemplate.getExpire(key)).orElse(-2L);
    }

    /**
     * 设置key的剩余生存时间，单位秒
     * @param key 键
     * @param timeoutSeconds 剩余生存时间
     * @return 成功与否
     */
    public boolean expire(@NonNull String key, long timeoutSeconds) {
        return Optional.ofNullable(redisTemplate.expire(key, timeoutSeconds, TimeUnit.SECONDS))
                       .orElse(false);
    }

    /**
     * 删除指定的key，删除失败可能是不存在的key，或者其它原因
     * @param key 键
     * @return 成功与否
     */
    public boolean del(@NonNull String key) {
        return Optional.ofNullable(redisTemplate.delete(key)).orElse(false);
    }

    // String（字符串）


    /**
     * 存储key-value
     * @param key 键
     * @param value 值
     */
    public void set(@NonNull String key, @NonNull Object value) {
        redisTemplate.opsForValue().set(key, JsonUtil.toJson(value));
    }

    /**
     * 类型redis 中的mset
     * @param keyAndValues 要求传参格式为,(k1, v1, k2, v2, ... kn, vn)
     */
    public void mset(@NonNull Object ... keyAndValues) {
        if (keyAndValues.length % 2 != 0) {
            throw new UnsupportedOperationException("要求传参格式为,(k1, v1, k2, v2, ... kn, vn)");
        }
        Map<String, String> map = new HashMap<>(16);
        for (int i = 0,  len = keyAndValues.length; i < len; ++i) {
            map.put(keyAndValues[i].toString(), JsonUtil.toJson(keyAndValues[++i]));
        }
        redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * setex:设置带过期时间的key，动态设置。setex 键 秒值 真实值
     * @param key 键
     * @param timeoutSeconds 剩余生存时间
     * @param value 值
     */
    public void setex(String key, long timeoutSeconds, Object value) {
        redisTemplate.opsForValue().set(key, JsonUtil.toJson(value), timeoutSeconds, TimeUnit.SECONDS);
    }

    /**
     * setnx:只有在 key 不存在时设置 key 的值。
     * @param key 键
     * @param value 值
     */
    public boolean setnx(String key, Object value) {
        return Optional.ofNullable(redisTemplate.opsForValue().setIfAbsent(key, JsonUtil.toJson(value))).orElse(false);
    }

    /**
     * 获取对象
     * @param key 键
     * @param valueType 对象class
     * @param <T> 限定类型
     */
    public <T> T get(@NonNull String key, @NonNull Class<T> valueType) {
        String value = redisTemplate.opsForValue().get(key);
        return value != null ?
            JsonUtil.jsonToObject(value, valueType) : null;
    }

    /**
     * 获取对象List
     * @param key 键
     * @param valueType 对象class
     * @param <T> 限定类型
     */
    public <T> List<T> getList(@NonNull String key, @NonNull Class<T> valueType) {
        String value = redisTemplate.opsForValue().get(key);
        return value != null ?
            JsonUtil.jsonToList(value, valueType) : null;
    }

    // hash

    /**
     * hset
     * @param key 键
     * @param field k
     * @param value v
     */
    public void hset(@NonNull String key, @NonNull String field, @NonNull Object value) {
        redisTemplate.opsForHash().put(key, field, JsonUtil.toJson(value));
    }

    /**
     * 实现命令：HGET key field，返回哈希表 key中给定域 field的值
     * @param key  key
     * @param field field
     * @param valueType 返回的value类型
     * @param <T> 限定类型
     * @return value
     */
    public <T> T hget(@NonNull String key, @NonNull String field, @NonNull Class<T> valueType) {
        Object value = redisTemplate.opsForHash().get(key, field);
        return value != null ?
            JsonUtil.jsonToObject(value.toString(), valueType) : null;
    }

}
