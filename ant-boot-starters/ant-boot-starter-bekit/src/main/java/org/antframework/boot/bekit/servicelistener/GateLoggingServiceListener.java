/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:53 创建
 */
package org.antframework.boot.bekit.servicelistener;

import org.antframework.boot.bekit.IgnoreGateLogging;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.other.Cache;
import org.bekit.event.annotation.Listen;
import org.bekit.service.annotation.listener.ServiceListener;
import org.bekit.service.event.ServiceApplyEvent;
import org.bekit.service.event.ServiceExceptionEvent;
import org.bekit.service.event.ServiceFinishEvent;
import org.bekit.service.service.ServicesHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 出入口日志打印-服务监听器
 */
@ServiceListener(priority = 20)
public class GateLoggingServiceListener {
    private static final Logger logger = LoggerFactory.getLogger(GateLoggingServiceListener.class);
    @Autowired
    private ServicesHolder servicesHolder;
    // 服务是否打印出入口日志-缓存
    private Cache<String, Boolean> cache = new Cache<>(key -> {
        Object service = servicesHolder.getRequiredServiceExecutor(key).getService();
        Class serviceClass = AopUtils.getTargetClass(service);
        return AnnotationUtils.findAnnotation(serviceClass, IgnoreGateLogging.class) == null;
    });

    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        if (cache.get(event.getService())) {
            logger.info("收到请求：service={}, order={}", event.getService(), event.getServiceContext().getOrder());
        }
    }

    @Listen(priorityAsc = false)
    public void listenServiceExceptionEvent(ServiceExceptionEvent event) {
        Throwable throwable = event.getThrowable();
        if (throwable instanceof BizException) {
            if (cache.get(event.getService())) {
                BizException bizException = (BizException) throwable;
                logger.warn("服务[{}]抛出手动异常：status={}, code={}, message={}", event.getService(), bizException.getStatus(), bizException.getCode(), bizException.getMessage());
            }
        } else {
            logger.error("服务[{}]抛出未知异常：", event.getService(), throwable);
        }
    }

    @Listen(priorityAsc = false)
    public void listenServiceFinishEvent(ServiceFinishEvent event) {
        if (cache.get(event.getService())) {
            logger.info("执行结果：service={}, result={}", event.getService(), event.getServiceContext().getResult());
        }
    }
}
