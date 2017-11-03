/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:53 创建
 */
package org.antframework.boot.bekit.servicelistener;

import org.antframework.boot.bekit.AntBekitException;
import org.antframework.boot.core.Contexts;
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
@ServiceListener(priority = 2)
public class LogPrintServiceListener {
    private static final Logger logger = LoggerFactory.getLogger(LogPrintServiceListener.class);
    // 属性
    private ServiceLogProperties properties = Contexts.buildProperties(ServiceLogProperties.class);

    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        if (!properties.getIgnoreServices().contains(event.getService())) {
            logger.info("收到请求：serviceName={}, order={}", event.getService(), event.getServiceContext().getOrder());
        }
    }

    @Listen(priorityAsc = false)
    public void listenServiceExceptionEvent(ServiceExceptionEvent event) {
        Throwable throwable = event.getTargetException();
        if (throwable instanceof AntBekitException) {
            if (!properties.getIgnoreServices().contains(event.getServiceName())) {
                AntBekitException antBekitException = (AntBekitException) throwable;
                logger.warn("收到手动异常：status={}, code={}, message={}", antBekitException.getStatus(), antBekitException.getCode(), antBekitException.getMessage());
            }
        } else {
            logger.error("服务执行中发生未知异常：", throwable);
        }
    }

    @Listen(priorityAsc = false)
    public void listenServiceFinishEvent(ServiceFinishEvent event) {
        if (!properties.getIgnoreServices().contains(event.getService())) {
            logger.info("执行结果：serviceName={}, result={}", event.getService(), event.getServiceContext().getResult());
        }
    }

    /**
     * 服务日志属性
     */
    @ConfigurationProperties("bekit.log")
    public static class ServiceLogProperties {
        /**
         * 选填：不打印日志的服务
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
