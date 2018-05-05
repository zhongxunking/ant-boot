/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-31 00:20 创建
 */
package org.antframework.boot.redis.boot;

import org.antframework.boot.core.Contexts;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.Map;

/**
 * redis注解式缓存管理器-定制器
 */
public class RedisCacheManagerCustomizer implements CacheManagerCustomizer<RedisCacheManager> {

    @Override
    public void customize(RedisCacheManager cacheManager) {
        RedisExpireProperties properties = Contexts.buildProperties(RedisExpireProperties.class);
        if (properties.getDefaultExpire() != null) {
            cacheManager.setDefaultExpiration(properties.getDefaultExpire());
        }
        if (properties.getExpires() != null && !properties.getExpires().isEmpty()) {
            cacheManager.setExpires(properties.getExpires());
        }
    }

    /**
     * redis注解式缓存属性
     */
    @ConfigurationProperties("ant.cache.redis")
    public static class RedisExpireProperties {
        /**
         * 选填：默认的注解式缓存有效时间（单位：秒），0或不填表示永远有效
         */
        private Long defaultExpire;
        /**
         * 选填：设置指定的注解式缓存有效时间。key：cacheName（缓存名称），value：有效时间（单位：秒）。value为0表示该缓存永远有效，不填表示使用默认有效时间。
         */
        private Map<String, Long> expires;

        public Long getDefaultExpire() {
            return defaultExpire;
        }

        public void setDefaultExpire(Long defaultExpire) {
            this.defaultExpire = defaultExpire;
        }

        public Map<String, Long> getExpires() {
            return expires;
        }

        public void setExpires(Map<String, Long> expires) {
            this.expires = expires;
        }
    }
}
