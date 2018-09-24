package com.hdh.lifeup.redis;

import com.hdh.lifeup.util.JsonUtil;
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
public class RedisOperator {

    private StringRedisTemplate redisTemplate;

    @Autowired
    public RedisOperator(StringRedisTemplate redisTemplate) {
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
    public long ttl(@NonNull KeyPrefix keyPrefix, @NonNull Object key) {
        String realKey = getRealKey(keyPrefix, key);
        return Optional.ofNullable(redisTemplate.getExpire(realKey)).orElse(-2L);
    }

    /**
     * 设置key的剩余生存时间，单位秒
     * @param key 键
     * @return 成功与否
     */
    public boolean expire(@NonNull KeyPrefix keyPrefix, @NonNull Object key) {
        String realKey = getRealKey(keyPrefix, key);
        return Optional.ofNullable(redisTemplate.expire(realKey, keyPrefix.expireSeconds(), TimeUnit.SECONDS))
                       .orElse(false);
    }

    /**
     * 删除指定的key，删除失败可能是不存在的key，或者其它原因
     * @param key 键
     * @return 成功与否
     */
    public boolean del(@NonNull KeyPrefix keyPrefix, @NonNull Object key) {
        String realKey = getRealKey(keyPrefix, key);
        return Optional.ofNullable(redisTemplate.delete(realKey)).orElse(false);
    }

    // String（字符串）


    /**
     * 存储key-value
     * @param key 键
     * @param value 值
     */
    public void set(@NonNull KeyPrefix keyPrefix, @NonNull Object key, @NonNull Object value) {
        String realKey = getRealKey(keyPrefix, key);
        redisTemplate.opsForValue().set(realKey, JsonUtil.toJson(value));
    }

    public long incr(@NonNull KeyPrefix keyPrefix, @NonNull Object key) {
        String realKey = getRealKey(keyPrefix, key);
        Long count = redisTemplate.opsForValue().increment(realKey, 1L);
        return Optional.ofNullable(count).orElse(0L);
    }

    /**
     * 类型redis 中的mset
     * @param keyAndValues 要求传参格式为,(k1, v1, k2, v2, ... kn, vn)
     */
    public void mset(@NonNull KeyPrefix keyPrefix, @NonNull Object ... keyAndValues) {
        if (keyAndValues.length % 2 != 0) {
            throw new UnsupportedOperationException("要求传参格式为,(k1, v1, k2, v2, ... kn, vn)");
        }
        Map<String, String> map = new HashMap<>(16);
        for (int i = 0,  len = keyAndValues.length; i < len; ++i) {
            map.put(getRealKey(keyPrefix, keyAndValues[i]), JsonUtil.toJson(keyAndValues[++i]));
        }
        redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * setex:设置带过期时间的key，动态设置。setex 键 秒值 真实值
     * @param key 键
     * @param value 值
     */
    public void setex(@NonNull KeyPrefix keyPrefix, Object key, Object value) {
        String realKey = getRealKey(keyPrefix, key);
        redisTemplate.opsForValue().set(realKey, JsonUtil.toJson(value), keyPrefix.expireSeconds(), TimeUnit.SECONDS);
    }

    /**
     * setnx:只有在 key 不存在时设置 key 的值。
     * @param key 键
     * @param value 值
     */
    public boolean setnx(@NonNull KeyPrefix keyPrefix, Object key, Object value) {
        String realKey = getRealKey(keyPrefix, key);
        return Optional.ofNullable(redisTemplate.opsForValue().setIfAbsent(realKey, JsonUtil.toJson(value)))
                       .orElse(false);
    }

    /**
     * 获取对象
     * @param key 键
     * @param <T> 限定类型
     */
    public <T> T get(@NonNull KeyPrefix<T> keyPrefix, @NonNull Object key) {
        String realKey = getRealKey(keyPrefix, key);
        String value = redisTemplate.opsForValue().get(realKey);
        return value != null ?
            JsonUtil.jsonToObject(value, keyPrefix.getValueClass()) : null;
    }

    /**
     * 获取对象List
     * @param key 键
     * @param <T> 限定类型
     */
    public <T> List<T> getList(@NonNull KeyPrefix<T> keyPrefix, @NonNull Object key) {
        String realKey = getRealKey(keyPrefix, key);
        String value = redisTemplate.opsForValue().get(realKey);
        return value != null ?
            JsonUtil.jsonToList(value, keyPrefix.getValueClass()) : null;
    }

    // hash

    /**
     * hset
     * @param key 键
     * @param field k
     * @param value v
     */
    public void hset(@NonNull KeyPrefix keyPrefix, @NonNull Object key, @NonNull String field, @NonNull Object value) {
        String realKey = getRealKey(keyPrefix, key);
        redisTemplate.opsForHash().put(realKey, field, JsonUtil.toJson(value));
    }

    /**
     * 实现命令：HGET key field，返回哈希表 key中给定域 field的值
     * @param key  key
     * @param field field
     * @param <T> 限定类型
     * @return value
     */
    public <T> T hget(@NonNull KeyPrefix<T> keyPrefix, @NonNull Object key, @NonNull String field) {
        String realKey = getRealKey(keyPrefix, key);
        Object value = redisTemplate.opsForHash().get(realKey, field);
        return value != null ?
            JsonUtil.jsonToObject(value.toString(), keyPrefix.getValueClass()) : null;
    }

    private String getRealKey(KeyPrefix keyPrefix, Object key) {
        return keyPrefix.getPrefix() + key;
    }

}
