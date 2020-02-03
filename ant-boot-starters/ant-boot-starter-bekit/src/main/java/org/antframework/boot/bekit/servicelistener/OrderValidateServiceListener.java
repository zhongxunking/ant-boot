/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:53 创建
 */
package org.antframework.boot.bekit.servicelistener;

import org.antframework.common.util.facade.AbstractOrder;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;
import org.bekit.event.annotation.Listen;
import org.bekit.service.annotation.listener.ServiceListener;
import org.bekit.service.event.ServiceApplyEvent;

/**
 * order校验-服务监听器
 */
@ServiceListener(priority = 40)
public class OrderValidateServiceListener {
    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        Object order = event.getContext().getOrder();
        if (order instanceof AbstractOrder) {
            try {
                ((AbstractOrder) order).check();
            } catch (Throwable e) {
                throw new BizException(Status.FAIL, CommonResultCode.INVALID_PARAMETER.getCode(), CommonResultCode.INVALID_PARAMETER.getMessage() + "：" + e.getMessage(), e);
            }
        }
    }
}
