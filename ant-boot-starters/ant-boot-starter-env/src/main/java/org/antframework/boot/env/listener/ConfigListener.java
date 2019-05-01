/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-13 14:09 创建
 */
package org.antframework.boot.env.listener;

import java.util.List;

/**
 * 配置监听器
 */
@FunctionalInterface
public interface ConfigListener {
    /**
     * 当配置变更时调用本方法
     *
     * @param appId             配置变更的应用id
     * @param changedProperties 变更的配置
     */
    void onChange(String appId, List<ChangedProperty> changedProperties);
}
