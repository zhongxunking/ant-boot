/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 15:41 创建
 */
package org.antframework.boot.config.listener.annotation;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 配置被修改事件类型
 */
public class ConfigChangedEventType {
    // 应用编码
    private String appCode;
    // 被修改的属性名前缀
    private String prefix;

    public ConfigChangedEventType(String appCode, String prefix) {
        this.appCode = appCode;
        this.prefix = prefix;
    }

    @Override
    public int hashCode() {
        return Objects.hash(appCode, prefix);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ConfigChangedEventType)) {
            return false;
        }
        ConfigChangedEventType other = (ConfigChangedEventType) obj;
        return StringUtils.equals(appCode, other.appCode)
                && StringUtils.equals(prefix, other.prefix);
    }
}
