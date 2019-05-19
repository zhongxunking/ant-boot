/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-05-16 22:53 创建
 */
package org.antframework.boot.env.refresh.placeholder.injector;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * 字段类型占位符注射器
 */
@AllArgsConstructor
@Slf4j
public class FieldPlaceholderInjector implements PlaceholderInjector {
    // 占位符
    private final String placeholder;
    // 目标对象
    private final Object target;
    // 字段
    private final Field field;

    @Override
    public String getPlaceholder() {
        return placeholder;
    }

    @Override
    public void inject(ConfigurableBeanFactory beanFactory) {
        try {
            String strVal = beanFactory.resolveEmbeddedValue(placeholder);
            Object value = beanFactory.getTypeConverter().convertIfNecessary(strVal, field.getType());
            ReflectionUtils.setField(field, target, value);
            log.info("刷新@Value字段成功：field={}，placeholder={}", field, placeholder);
        } catch (Throwable e) {
            log.error("刷新@Value字段出错：field={}，placeholder={}", field, placeholder, e);
        }
    }
}
