/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-05-16 22:59 创建
 */
package org.antframework.boot.env.refresh.placeholder.injector;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 方法类型占位符注射器
 */
@AllArgsConstructor
public class MethodPlaceholderInjector implements PlaceholderInjector {
    // 占位符
    private final String placeholder;
    // 目标对象
    private final Object target;
    // 方法
    private final Method method;

    @Override
    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    public void inject(ConfigurableBeanFactory beanFactory) {
        String strVal = beanFactory.resolveEmbeddedValue(placeholder);
        Object value = beanFactory.getTypeConverter().convertIfNecessary(strVal, method.getParameterTypes()[0]);
        ReflectionUtils.invokeMethod(method, target, value);
    }
}
