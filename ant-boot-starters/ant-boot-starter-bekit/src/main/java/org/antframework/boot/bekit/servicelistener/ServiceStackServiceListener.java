/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-02-08 17:42 创建
 */
package org.antframework.boot.bekit.servicelistener;

import org.bekit.event.annotation.Listen;
import org.bekit.event.listener.PriorityType;
import org.bekit.service.annotation.listener.ServiceListener;
import org.bekit.service.event.ServiceApplyEvent;
import org.bekit.service.event.ServiceFinishEvent;

/**
 * 服务栈-服务监听器
 */
@ServiceListener(priority = 0)
public class ServiceStackServiceListener {
    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        ServiceStacks.push();
    }

    @Listen(priorityType = PriorityType.DESC)
    public void listenServiceFinishEvent(ServiceFinishEvent event) {
        ServiceStacks.pop();
    }
}
