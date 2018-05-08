/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-28 14:25 创建
 */
package org.antframework.boot.dubbo.boot;

import com.alibaba.dubbo.config.*;
import com.alibaba.dubbo.config.spring.AnnotationBean;
import org.antframework.boot.core.Apps;
import org.antframework.boot.core.Contexts;
import org.antframework.boot.dubbo.DubboReferenceFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;

/**
 * dubbo自动配置类
 */
@Configuration
public class DubboAutoConfiguration {
    // dubbo属性
    private DubboProperties properties = Contexts.buildProperties(DubboProperties.class);

    // 解析dubbo注解配置
    @Bean
    public static AnnotationBean annotationBean() {
        AnnotationBean annotationBean = new AnnotationBean();
        annotationBean.setPackage(StringUtils.join(Contexts.getBasePackages(), ','));
        return annotationBean;
    }

    // 应用配置
    @Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(Apps.getAppId());
        applicationConfig.setOwner(properties.getOwner());
        return applicationConfig;
    }

    // dubbo服务注册配置
    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setProtocol("zookeeper");
        registryConfig.setAddress(properties.getRegistryAddress());
        registryConfig.setFile(Apps.getConfigPath() + File.separator + properties.getRegistryFile());
        return registryConfig;
    }

    // 协议配置
    @Bean
    public ProtocolConfig protocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(properties.getProtocolPort());
        protocolConfig.setThreads(properties.getProtocolThreads());
        return protocolConfig;
    }

    // 服务提供者配置
    @Bean
    public ProviderConfig providerConfig() {
        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setTimeout(properties.getProviderTimeout());
        providerConfig.setRetries(0);
        providerConfig.setDelay(-1);
        return providerConfig;
    }

    // 服务消费者配置
    @Bean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setCheck(false);
        consumerConfig.setLoadbalance("roundrobin");
        return consumerConfig;
    }

    // dubbo监听器配置
    @Bean
    public MonitorConfig monitorConfig() {
        MonitorConfig monitorConfig = new MonitorConfig();
        monitorConfig.setProtocol("dubbo");
        monitorConfig.setAddress(properties.getMonitorAddress());
        return monitorConfig;
    }

    // dubbo服务引用工厂
    @Bean
    public DubboReferenceFactory dubboReferenceFactory(ApplicationConfig applicationConfig, List<RegistryConfig> registryConfigs) {
        return new DubboReferenceFactory(applicationConfig, registryConfigs);
    }
}
