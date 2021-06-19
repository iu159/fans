package edu.wuyi.fans.shiro.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisCache;
import org.crazycake.shiro.serializer.ObjectSerializer;
import org.crazycake.shiro.serializer.RedisSerializer;
import org.crazycake.shiro.serializer.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/13
 */
public class RedisCacheManager implements CacheManager {
    private final Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap();
    private RedisSerializer keySerializer = new StringSerializer();
    private RedisSerializer valueSerializer = new ObjectSerializer();
    private IRedisManager redisManager;
    private static final int DEFAULT_EXPIRE = 1800;
    private int expire = 1800;
    public static final String DEFAULT_CACHE_KEY_PREFIX = "shiro:cache:";
    private String keyPrefix = "shiro:cache:";
    public static final String DEFAULT_PRINCIPAL_ID_FIELD_NAME = "authCacheKey or id";
    private String principalIdFieldName = "authCacheKey or id";

    public RedisCacheManager() {
    }

    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        this.logger.debug("get cache, name=" + name);
        Cache cache = (Cache)this.caches.get(name);
        if (cache == null) {
            cache = new RedisCache(this.redisManager, this.keySerializer, this.valueSerializer, this.keyPrefix + name + ":", this.expire, this.principalIdFieldName);
            this.caches.put(name, cache);
        }

        return (Cache)cache;
    }

    public IRedisManager getRedisManager() {
        return this.redisManager;
    }

    public void setRedisManager(IRedisManager redisManager) {
        this.redisManager = redisManager;
    }

    public String getKeyPrefix() {
        return this.keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public RedisSerializer getKeySerializer() {
        return this.keySerializer;
    }

    public void setKeySerializer(RedisSerializer keySerializer) {
        this.keySerializer = keySerializer;
    }

    public RedisSerializer getValueSerializer() {
        return this.valueSerializer;
    }

    public void setValueSerializer(RedisSerializer valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public int getExpire() {
        return this.expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getPrincipalIdFieldName() {
        return this.principalIdFieldName;
    }

    public void setPrincipalIdFieldName(String principalIdFieldName) {
        this.principalIdFieldName = principalIdFieldName;
    }
}
