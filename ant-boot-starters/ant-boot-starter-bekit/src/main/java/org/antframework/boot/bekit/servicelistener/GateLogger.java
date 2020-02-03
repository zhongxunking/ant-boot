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
import org.bekit.event.listener.PriorityType;
import org.bekit.service.annotation.listener.ServiceListener;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.event.ServiceApplyEvent;
import org.bekit.service.event.ServiceExceptionEvent;
import org.bekit.service.event.ServiceFinishEvent;
import org.bekit.service.service.ServiceRegistrar;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.function.Function;

/**
 * 出入口日志打印器
 */
@ServiceListener(priority = 20)
@AllArgsConstructor
@Slf4j
public class GateLogger {
    // 服务是否打印出入口日志-缓存
    private final Cache<String, Boolean> ignoreLoggingCache = new Cache<>(new Function<String, Boolean>() {
        @Override
        public Boolean apply(String key) {
            Object service = serviceRegistrar.get(key).getService();
            Class serviceClass = AopUtils.getTargetClass(service);
            return AnnotationUtils.findAnnotation(serviceClass, IgnoreGateLogging.class) != null;
        }
    });
    // 服务是否开启事务-缓存
    private final Cache<String, Boolean> enableTxCache = new Cache<>(new Function<String, Boolean>() {
        @Override
        public Boolean apply(String key) {
            Object service = serviceRegistrar.get(key).getService();
            Class serviceClass = AopUtils.getTargetClass(service);
            return AnnotationUtils.findAnnotation(serviceClass, Service.class).enableTx();
        }
    });
    // 服务持有器
    private final ServiceRegistrar serviceRegistrar;

    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        if (ignoreLoggingCache.get(event.getService())) {
            return;
        }
        if (isInfoLevel(event.getService())) {
            log.info(calcIndent() + "收到请求：service={}, order={}", event.getService(), event.getContext().getOrder());
        } else {
            log.debug(calcIndent() + "收到请求：service={}, order={}", event.getService(), event.getContext().getOrder());
        }
    }

    @Listen(priorityType = PriorityType.DESC)
    public void listenServiceExceptionEvent(ServiceExceptionEvent event) {
        Throwable throwable = event.getThrowable();
        if (!(throwable instanceof BizException)) {
            log.error(calcIndent() + "服务[{}]抛出未知异常：", event.getService(), throwable);
        } else if (!ignoreLoggingCache.get(event.getService())) {
            BizException bizException = (BizException) throwable;
            if (isInfoLevel(event.getService())) {
                log.info(calcIndent() + "服务[{}]抛出手动异常：status={}, code={}, message={}", event.getService(), bizException.getStatus(), bizException.getCode(), bizException.getMessage());
            } else {
                log.debug(calcIndent() + "服务[{}]抛出手动异常：status={}, code={}, message={}", event.getService(), bizException.getStatus(), bizException.getCode(), bizException.getMessage());
            }
        }
    }

    @Listen(priorityType = PriorityType.DESC)
    public void listenServiceFinishEvent(ServiceFinishEvent event) {
        if (ignoreLoggingCache.get(event.getService())) {
            return;
        }
        if (isInfoLevel(event.getService())) {
            log.info(calcIndent() + "执行结果：service={}, result={}", event.getService(), event.getContext().getResult());
        } else {
            log.debug(calcIndent() + "执行结果：service={}, result={}", event.getService(), event.getContext().getResult());
        }
    }

    // 日志级别是否为info
    private boolean isInfoLevel(String service) {
        return ServiceStacks.getDepth() <= 1 || enableTxCache.get(service);
    }

    // 计算日志缩进
    private String calcIndent() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ServiceStacks.getDepth() - 1; i++) {
            builder.append('-');
        }
        return builder.toString();
    }
}
