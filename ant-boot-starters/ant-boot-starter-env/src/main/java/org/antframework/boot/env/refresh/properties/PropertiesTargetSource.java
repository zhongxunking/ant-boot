/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-05-19 18:42 创建
 */
package org.antframework.boot.env.refresh.properties;

import lombok.extern.slf4j.Slf4j;
import org.antframework.boot.core.Contexts;
import org.springframework.aop.TargetSource;

/**
 * 配置TargetSource
 */
@Slf4j
public class PropertiesTargetSource implements TargetSource {
    // 目标类型
    private final Class<?> targetClass;
    // 目标对象
    private Object target;

    public PropertiesTargetSource(Class<?> targetClass) {
        this.targetClass = targetClass;
        refresh();
    }

    /**
     * 刷新配置
     */
    public void refresh() {
        try {
            target = Contexts.buildProperties(targetClass);
            log.info("刷新@ConfigurationProperties配置成功：class={}", targetClass.getName());
        } catch (Throwable e) {
            log.error("刷新@ConfigurationProperties配置失败：class={}", targetClass.getName(), e);
        }
    }

    @Override
    public Class<?> getTargetClass() {
        return targetClass;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public Object getTarget() throws Exception {
        return target;
    }

    @Override
    public void releaseTarget(Object target) throws Exception {
    }
}
