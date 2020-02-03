/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:53 创建
 */
package org.antframework.boot.bekit.servicelistener;

import org.antframework.common.util.facade.*;
import org.bekit.event.annotation.Listen;
import org.bekit.event.listener.PriorityType;
import org.bekit.service.annotation.listener.ServiceListener;
import org.bekit.service.event.ServiceApplyEvent;
import org.bekit.service.event.ServiceExceptionEvent;

/**
 * result维护-服务监听器
 */
@ServiceListener(priority = 30)
public class ResultMaintainServiceListener {
    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        Object result = event.getContext().getResult();
        if (result instanceof AbstractResult) {
            FacadeUtils.initSuccess((AbstractResult) result);
        }
    }

    @Listen(priorityType = PriorityType.DESC)
    public void listenServiceExceptionEvent(ServiceExceptionEvent event) {
        Object result = event.getContext().getResult();
        if (result instanceof AbstractResult) {
            Throwable throwable = event.getThrowable();
            if (throwable instanceof BizException) {
                setResultByBizException((AbstractResult) result, (BizException) throwable);
            } else {
                setResultByUnknownException((AbstractResult) result, throwable);
            }
        }
    }

    // 依据BizException设置result
    private void setResultByBizException(AbstractResult result, BizException bizException) {
        result.setStatus(bizException.getStatus());
        result.setCode(bizException.getCode());
        result.setMessage(bizException.getMessage());
    }

    // 依据未知异常设置result
    private void setResultByUnknownException(AbstractResult result, Throwable throwable) {
        result.setStatus(Status.PROCESSING);
        result.setCode(CommonResultCode.UNKNOWN_ERROR.getCode());
        result.setMessage(throwable.getMessage());
    }
}
