/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-04-27 22:26 创建
 */
package org.antframework.boot.lang;

import org.antframework.boot.core.Contexts;
import org.springframework.core.env.AbstractEnvironment;

/**
 * 应用操作类
 */
public class Apps {
    /**
     * 如果profile未被设置，则设置profile
     */
    public static void setProfileIfAbsent(String profile) {
        if (Contexts.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME) == null) {
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
        }
    }
}
