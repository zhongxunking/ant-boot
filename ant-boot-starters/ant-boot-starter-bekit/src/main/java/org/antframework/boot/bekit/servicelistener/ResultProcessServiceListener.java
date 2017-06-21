/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-05-02 11:15 创建
 */
package org.antframework.boot.bekit.servicelistener;

import org.antframework.boot.bekit.exception.AntBekitException;
import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.facade.Status;
import top.bekit.event.annotation.listener.Listen;
import top.bekit.service.annotation.listener.ServiceListener;
import top.bekit.service.engine.ServiceContext;
import top.bekit.service.event.ServiceApplyEvent;
import top.bekit.service.event.ServiceExceptionEvent;

/**
 * result处理-服务监听器
 */
@ServiceListener(priority = 3)
public class ResultProcessServiceListener {

    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        ServiceContext<AbstractOrder, AbstractResult> serviceContext = event.getServiceContext();
        initResult(serviceContext.getResult());
    }

    @Listen(priorityAsc = false)
    public void listenServiceExceptionEvent(ServiceExceptionEvent event) {
        ServiceContext<AbstractOrder, AbstractResult> serviceContext = event.getServiceContext();

        Throwable throwable = event.getTargetException();
        if (throwable instanceof AntBekitException) {
            setResultByAntBekitException(serviceContext.getResult(), (AntBekitException) throwable);
        } else {
            setResultByUnknownException(serviceContext.getResult(), throwable);
        }
    }

    // 初始化result
    private void initResult(AbstractResult result) {
        result.setStatus(Status.SUCCESS);
        result.setCode("001");
        result.setMessage("成功");
    }

    // 依据AntBekitException设置result
    private void setResultByAntBekitException(AbstractResult result, AntBekitException antBekitException) {
        result.setStatus(antBekitException.getStatus());
        result.setCode(antBekitException.getCode());
        result.setMessage(antBekitException.getMessage());
    }

    // 依据未知异常设置result
    private void setResultByUnknownException(AbstractResult result, Throwable throwable) {
        result.setStatus(Status.PROCESSING);
        result.setCode("001");
        result.setMessage(throwable.getMessage());
    }
}
