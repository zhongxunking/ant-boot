/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 15:32 创建
 */
package org.antframework.boot.config.listener.annotation;

import org.antframework.configcenter.client.core.ChangedProperty;

import java.util.List;

/**
 * 配置被修改事件
 */
public class ConfigChangedEvent {
    // 应用编码
    private String appCode;
    // 被修改的属性名前缀
    private String prefix;
    // 被修改的属性
    private List<ChangedProperty> changedProperties;

    public ConfigChangedEvent(String appCode, String prefix, List<ChangedProperty> changedProperties) {
        this.appCode = appCode;
        this.prefix = prefix;
        this.changedProperties = changedProperties;
    }

    public String getAppCode() {
        return appCode;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<ChangedProperty> getChangedProperties() {
        return changedProperties;
    }
}
