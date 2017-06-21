/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-05-19 19:13 创建
 */
package org.antframework.boot.bekit.servicelistener;

import org.antframework.boot.bekit.holder.CodeMessageHolder;
import top.bekit.event.annotation.listener.Listen;
import top.bekit.service.annotation.listener.ServiceListener;
import top.bekit.service.event.ServiceApplyEvent;
import top.bekit.service.event.ServiceFinishEvent;

/**
 * 结果码、结果描述清理-服务监听器
 */
@ServiceListener(priority = 2)
public class CodeMessageClearServiceListener {
    @Listen
    public void listenServiceApplyEvent(ServiceApplyEvent event) {
        CodeMessageHolder.remove();
    }

    @Listen(priorityAsc = false)
    public void listenServiceFinishEvent(ServiceFinishEvent event) {
        CodeMessageHolder.remove();
    }
}
