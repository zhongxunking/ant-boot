/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:53 创建
 */
package org.antframework.boot.bekit.servicelistener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antframework.boot.bekit.IgnoreGateLogging;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.other.Cache;
import org.bekit.event.annotation.Listen;
import org.bekit.service.annotation.listener.ServiceListener;
import org.bekit.service.event.ServiceApplyEvent;
import org.bekit.service.event.ServiceExceptionEvent;
import org.bekit.service.event.ServiceFinishEvent;
import org.bekit.service.service.ServicesHolder;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 出入口日志打印-服务监听器
 */
@ServiceListener(priority = 20)
@AllArgsConstructor
@Slf4j
public class GateLoggingServiceListener {
    // 服务持有器
    private ServicesHolder servicesHolder;
    // 服务是否打印出入口日志-缓存
    private final Cache<String, Boolean> cache = new Cache<>(key -> {
        Object service = servicesHolder.getRequiredServiceExecutor(key).getService();
        Class serviceClass = AopUtils.getTargetClass(service);
        return AnnotationUtils.findAnnotation(serviceClass, IgnoreGateLogging.class) == null;
    });

    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        if (cache.get(event.getService())) {
            log.info("收到请求：service={}, order={}", event.getService(), event.getServiceContext().getOrder());
        }
    }

    @Listen(priorityAsc = false)
    public void listenServiceExceptionEvent(ServiceExceptionEvent event) {
        Throwable throwable = event.getThrowable();
        if (throwable instanceof BizException) {
            if (cache.get(event.getService())) {
                BizException bizException = (BizException) throwable;
                log.warn("服务[{}]抛出手动异常：status={}, code={}, message={}", event.getService(), bizException.getStatus(), bizException.getCode(), bizException.getMessage());
            }
        } else {
            log.error("服务[{}]抛出未知异常：", event.getService(), throwable);
        }
    }

    @Listen(priorityAsc = false)
    public void listenServiceFinishEvent(ServiceFinishEvent event) {
        if (cache.get(event.getService())) {
            log.info("执行结果：service={}, result={}", event.getService(), event.getServiceContext().getResult());
        }
    }
}
