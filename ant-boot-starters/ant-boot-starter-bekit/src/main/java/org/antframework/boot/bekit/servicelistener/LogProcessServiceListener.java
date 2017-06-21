/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-05-02 11:31 创建
 */
package org.antframework.boot.bekit.servicelistener;

import org.antframework.boot.bekit.exception.AntBekitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.bekit.event.annotation.listener.Listen;
import top.bekit.service.annotation.listener.ServiceListener;
import top.bekit.service.event.ServiceApplyEvent;
import top.bekit.service.event.ServiceExceptionEvent;
import top.bekit.service.event.ServiceFinishEvent;

/**
 * 日志处理-服务监听器
 */
@ServiceListener(priority = 1)
public class LogProcessServiceListener {
    private static final Logger logger = LoggerFactory.getLogger(LogProcessServiceListener.class);

    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        logger.info("收到请求：serviceName={}, order={}", event.getService(), event.getServiceContext().getOrder());
    }

    @Listen(priorityAsc = false)
    public void listenServiceExceptionEvent(ServiceExceptionEvent event) {
        Throwable throwable = event.getTargetException();
        if (throwable instanceof AntBekitException) {
            AntBekitException antBekitException = (AntBekitException) throwable;
            logger.warn("收到手动异常：status={}, code={}, message={}", antBekitException.getStatus(), antBekitException.getCode(), antBekitException.getMessage());
        } else {
            logger.error("服务执行中发生未知异常：", throwable);
        }
    }

    @Listen(priorityAsc = false)
    public void listenServiceFinishEvent(ServiceFinishEvent event) {
        logger.info("执行结果：serviceName={}, result={}", event.getService(), event.getServiceContext().getResult());
    }
}
