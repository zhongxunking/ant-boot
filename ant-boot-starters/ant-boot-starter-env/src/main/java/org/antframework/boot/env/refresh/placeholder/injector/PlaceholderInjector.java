/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-05-16 22:50 创建
 */
package org.antframework.boot.env.refresh.placeholder.injector;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * 占位符注射器
 */
public interface PlaceholderInjector {
    /**
     * 获取占位符
     */
    String getPlaceholder();

    /**
     * 注射占位符的值
     *
     * @param beanFactory bean工厂
     */
    void inject(ConfigurableBeanFactory beanFactory);
}
