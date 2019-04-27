/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-04-27 22:26 创建
 */
package org.antframework.boot.lang;

import org.antframework.common.util.other.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.AbstractEnvironment;

/**
 * 应用操作类
 */
public class Apps {
    /**
     * 如果profile未被设置，则设置profile
     */
    public static void setProfileIfAbsent(String profile) {
        if (StringUtils.isEmpty(PropertyUtils.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME))) {
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, profile);
        }
    }
}
