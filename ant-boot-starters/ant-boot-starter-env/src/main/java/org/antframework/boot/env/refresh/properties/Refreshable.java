/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-05-19 18:34 创建
 */
package org.antframework.boot.env.refresh.properties;

import java.lang.annotation.*;

/**
 * 可刷新（打在@ConfigurationProperties旁，表示当配置变更时，需要刷新该配置类）
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Refreshable {
}
