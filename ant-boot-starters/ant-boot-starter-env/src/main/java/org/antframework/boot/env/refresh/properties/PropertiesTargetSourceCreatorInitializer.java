/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-05-19 13:13 创建
 */
package org.antframework.boot.env.refresh.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.config.AopConfigUtils;
import org.springframework.aop.framework.autoproxy.TargetSourceCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import java.util.*;

/**
 * 配置TargetSource创建器--初始化器
 */
@Slf4j
public class PropertiesTargetSourceCreatorInitializer implements BeanFactoryPostProcessor {
    // TargetSource创建器的属性名
    private static final String TARGET_SOURCE_CREATORS_PROPERTY_NAME = "customTargetSourceCreators";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (!beanFactory.containsBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME)) {
            if (!(beanFactory instanceof BeanDefinitionRegistry)) {
                log.warn("无法刷新@ConfigurationProperties配置");
                return;
            }
            AopConfigUtils.registerAutoProxyCreatorIfNecessary((BeanDefinitionRegistry) beanFactory);
        }
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME);
        List<TargetSourceCreator> creators = new ArrayList<>();
        // 获取已有的creator
        TargetSourceCreator[] existingCreators = (TargetSourceCreator[]) beanDefinition.getPropertyValues().get(TARGET_SOURCE_CREATORS_PROPERTY_NAME);
        if (existingCreators != null) {
            creators.addAll(Arrays.asList(existingCreators));
        }
        // 添加PropertiesTargetSourceCreator
        PropertiesTargetSourceCreator creator = beanFactory.getBean(PropertiesTargetSourceCreator.class);
        creator.setIgnoredBeanNames(getIgnoredBeanNames(beanFactory));
        creators.add(creator);
        // 重新设置creator
        beanDefinition.getPropertyValues().add(TARGET_SOURCE_CREATORS_PROPERTY_NAME, creators.toArray(new TargetSourceCreator[0]));
    }

    // 获取需忽略的bean
    private Set<String> getIgnoredBeanNames(ConfigurableListableBeanFactory beanFactory) {
        Set<String> ignoredBeanNames = new HashSet<>();
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition definition = beanFactory.getBeanDefinition(beanName);
            String method = definition.getFactoryMethodName();
            String name = definition.getFactoryBeanName();
            if (method != null || name != null) {
                ignoredBeanNames.add(beanName);
            }
        }
        return ignoredBeanNames;
    }
}
