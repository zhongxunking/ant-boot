/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-28 23:49 创建
 */
package org.antframework.boot.redis;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * 具备命名空间装饰功能的redis序列化器
 */
public class NameSpaceRedisSerializer implements RedisSerializer {
    // 命名空间
    private byte[] namespace;
    // 目标redis序列化器
    private RedisSerializer target;

    public NameSpaceRedisSerializer(byte[] namespace, RedisSerializer target) {
        this.namespace = namespace;
        this.target = target;
    }

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return ArrayUtils.addAll(namespace, target.serialize(o));
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return target.deserialize(ArrayUtils.subarray(bytes, namespace.length, bytes.length));
    }
}
