/*
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:53 创建
 */
package org.antframework.boot.bekit.boot;

import org.antframework.boot.bekit.service.CommonQueryService;
import org.antframework.boot.bekit.servicelistener.HolderClearServiceListener;
import org.antframework.boot.bekit.servicelistener.LogPrintServiceListener;
import org.antframework.boot.bekit.servicelistener.OrderValidateServiceListener;
import org.antframework.boot.bekit.servicelistener.ResultMaintainServiceListener;
import org.antframework.boot.jpa.QueryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
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
    public static class ServiceListenerConfiguration {
        // 日志打印-服务监听器
        @Bean
        public LogPrintServiceListener logPrintServiceListener() {
            return new LogPrintServiceListener();
        }

        // 持有器清理-服务监听器
        @Bean
        public HolderClearServiceListener holderClearServiceListener() {
            return new HolderClearServiceListener();
        }

        // result维护-服务监听器
        @Bean
        public ResultMaintainServiceListener resultMaintainServiceListener() {
            return new ResultMaintainServiceListener();
        }

        // order校验-服务监听器
        @Bean
        public OrderValidateServiceListener orderValidateServiceListener() {
            return new OrderValidateServiceListener();
        }
    }

    /**
     * 服务自动配置类
     */
    @Configuration
    @ConditionalOnClass(QueryRepository.class)
    @Import(CommonQueryService.class)
    public static class ServiceConfiguration {
    }
}
