/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-29 00:02 创建
 */
package org.antframework.boot.redis.boot;

import org.antframework.boot.core.Apps;
import org.antframework.boot.redis.NameSpaceRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;

/**
 * redis命名空间自动配置类
 */
@Configuration
public class RedisNameSpaceAutoConfiguration {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    public void init() {
        customKeySerializer(redisTemplate);
        customKeySerializer(stringRedisTemplate);
    }

    // 定制key序列化器（使其具备命名空间功能）
    private void customKeySerializer(RedisTemplate redisTemplate) {
        // 命名空间为"${appCode}:"
        byte[] namespace = (Apps.getAppCode() + ":").getBytes(Charset.forName("utf-8"));
        NameSpaceRedisSerializer keySerializer = new NameSpaceRedisSerializer(namespace, redisTemplate.getKeySerializer());
        redisTemplate.setKeySerializer(keySerializer);
    }
}
