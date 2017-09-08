/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:53 创建
 */
package org.antframework.boot.bekit.servicelistener;

import org.antframework.boot.bekit.CodeMessageHolder;
import org.bekit.event.annotation.listener.Listen;
import org.bekit.service.annotation.listener.ServiceListener;
import org.bekit.service.event.ServiceApplyEvent;
import org.bekit.service.event.ServiceFinishEvent;

/**
 * 持有器清理-服务监听器
 */
@ServiceListener(priority = 1)
public class HolderClearServiceListener {
    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        // 结果码、结果描述持有器压入keeper
        CodeMessageHolder.pushKeeper();
    }

    @Listen(priorityAsc = false)
    public void listenServiceFinishEvent(ServiceFinishEvent event) {
        // 结果码、结果描述持有器弹出keeper
        CodeMessageHolder.popKeeper();
    }
}
