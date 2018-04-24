/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:53 创建
 */
package org.antframework.boot.bekit.boot;

import org.antframework.boot.bekit.service.CommonQueryService;
import org.antframework.boot.bekit.servicelistener.*;
import org.antframework.boot.jpa.QueryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * bekit自动配置类
 */
@Configuration
public class BekitAutoConfiguration {

    /**
     * 服务监听器配置类
     */
    @Configuration
    @Import({ServiceStackServiceListener.class,
            HolderServiceListener.class,
            GateLoggingServiceListener.class,
            ResultMaintainServiceListener.class,
            OrderValidateServiceListener.class})
    public static class ServiceListenerConfiguration {
    }

    /**
     * 服务配置类
     */
    @Configuration
    @ConditionalOnClass(QueryRepository.class)
    @Import(CommonQueryService.class)
    public static class ServiceConfiguration {
    }
}
