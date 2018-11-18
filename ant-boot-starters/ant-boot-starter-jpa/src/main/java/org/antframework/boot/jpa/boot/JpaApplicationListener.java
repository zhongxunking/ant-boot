/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-11-17 17:14 创建
 */
package org.antframework.boot.jpa.boot;

import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * jpa应用监听器
 */
public class JpaApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    // 命名策略属性名
    private static final String STRATEGY_PROPERTY_NAME = "spring.jpa.hibernate.naming.physical-strategy";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        boolean existing = event.getEnvironment().containsProperty(STRATEGY_PROPERTY_NAME);
        if (!existing) {
            // 如果未指定命名策略，则使用PhysicalNamingStrategyStandardImpl（表明、字段名与entity类定义的一致）
            System.setProperty(STRATEGY_PROPERTY_NAME, PhysicalNamingStrategyStandardImpl.class.getName());
        }
    }
}
