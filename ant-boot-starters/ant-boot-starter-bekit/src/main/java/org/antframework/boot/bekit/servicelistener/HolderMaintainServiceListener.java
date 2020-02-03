/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-02-08 22:04 创建
 */
package org.antframework.boot.bekit.servicelistener;

import org.antframework.boot.bekit.CodeMessageHolder;
import org.bekit.event.annotation.Listen;
import org.bekit.event.listener.PriorityType;
import org.bekit.service.annotation.listener.ServiceListener;
import org.bekit.service.event.ServiceApplyEvent;
import org.bekit.service.event.ServiceFinishEvent;

/**
 * 持有器维护-服务监听器
 */
@ServiceListener(priority = 10)
public class HolderMaintainServiceListener {
    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        // 入栈结果码、描述
        ServiceStacks.peek().put(CodeMessageHolder.class, CodeMessageHolder.get());
        CodeMessageHolder.remove();
    }

    @Listen(priorityType = PriorityType.DESC)
    public void listenServiceFinishEvent(ServiceFinishEvent event) {
        // 出栈结果码、描述
        CodeMessageHolder.set((CodeMessageHolder.CodeMessage) ServiceStacks.peek().remove(CodeMessageHolder.class));
    }
}
