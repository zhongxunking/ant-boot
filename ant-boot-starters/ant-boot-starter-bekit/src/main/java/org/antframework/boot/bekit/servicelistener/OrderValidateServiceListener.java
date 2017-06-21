/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-05-09 15:54 创建
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

/**
 * order校验-服务监听器
 */
@ServiceListener(priority = 4)
public class OrderValidateServiceListener {

    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        ServiceContext<AbstractOrder, AbstractResult> serviceContext = event.getServiceContext();
        try {
            serviceContext.getOrder().check();
        } catch (Throwable e) {
            throw new AntBekitException(Status.FAIL, "001", e.getMessage());
        }
    }

}
