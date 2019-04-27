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
    // open-in-view的key
    private static final String OPEN_IN_VIEW_KEY = "spring.jpa.open-in-view";
    // 物理命名策略的key
    private static final String PHYSICAL_STRATEGY_KEY = "spring.jpa.hibernate.naming.physical-strategy";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        // open-in-view
        boolean openInViewExisting = event.getEnvironment().containsProperty(OPEN_IN_VIEW_KEY);
        if (!openInViewExisting) {
            // 如果未设置，则默认关闭open-in-view
            System.setProperty(OPEN_IN_VIEW_KEY, Boolean.FALSE.toString());
        }
        // 物理命名策略
        boolean physicalStrategyExisting = event.getEnvironment().containsProperty(PHYSICAL_STRATEGY_KEY);
        if (!physicalStrategyExisting) {
            // 如果未指定命名策略，则使用PhysicalNamingStrategyStandardImpl（表明、字段名与entity类定义的一致）
            System.setProperty(PHYSICAL_STRATEGY_KEY, PhysicalNamingStrategyStandardImpl.class.getName());
        }
    }
}
