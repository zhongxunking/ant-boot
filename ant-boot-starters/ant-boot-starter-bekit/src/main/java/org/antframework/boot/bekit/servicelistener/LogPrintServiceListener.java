/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:53 创建
 */
package org.antframework.boot.bekit.servicelistener;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.facade.BizException;
import org.bekit.event.annotation.Listen;
import org.bekit.service.annotation.listener.ServiceListener;
import org.bekit.service.event.ServiceApplyEvent;
import org.bekit.service.event.ServiceExceptionEvent;
import org.bekit.service.event.ServiceFinishEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * 日志打印-服务监听器
 */
@ServiceListener(priority = 10)
public class LogPrintServiceListener {
    private static final Logger logger = LoggerFactory.getLogger(LogPrintServiceListener.class);
    // 需忽略日志打印的服务
    private Set<String> ignoreServices = Contexts.buildProperties(ServiceLogProperties.class).getIgnoreServices();

    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        if (!ignoreServices.contains(event.getService())) {
            logger.info("收到请求：service={}, order={}", event.getService(), event.getServiceContext().getOrder());
        }
    }

    @Listen(priorityAsc = false)
    public void listenServiceExceptionEvent(ServiceExceptionEvent event) {
        Throwable throwable = event.getThrowable();
        if (throwable instanceof BizException) {
            if (!ignoreServices.contains(event.getService())) {
                BizException bizException = (BizException) throwable;
                logger.warn("服务[{}]抛出手动异常：status={}, code={}, message={}", event.getService(), bizException.getStatus(), bizException.getCode(), bizException.getMessage());
            }
        } else {
            logger.error("服务[{}]抛出未知异常：", event.getService(), throwable);
        }
    }

    @Listen(priorityAsc = false)
    public void listenServiceFinishEvent(ServiceFinishEvent event) {
        if (!ignoreServices.contains(event.getService())) {
            logger.info("执行结果：service={}, result={}", event.getService(), event.getServiceContext().getResult());
        }
    }

    /**
     * 服务日志属性
     */
    @ConfigurationProperties(ServiceLogProperties.PREFIX)
    public static class ServiceLogProperties {
        /**
         * 属性前缀
         */
        public static final String PREFIX = "bekit.log";

        /**
         * 选填：不打印日志的服务（以","分隔）
         */
        private Set<String> ignoreServices = new HashSet<>();

        public Set<String> getIgnoreServices() {
            return ignoreServices;
        }

        public void setIgnoreServices(Set<String> ignoreServices) {
            this.ignoreServices = new HashSet<>(ignoreServices);
        }
    }
}
