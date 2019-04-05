/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-20 16:06 创建
 */
package org.antframework.boot.logging;

import org.antframework.boot.logging.core.LogContext;

/**
 * 日志初始化器（实现类必须具有默认构造函数）
 */
public interface LogInitializer {
    /**
     * 初始化
     *
     * @param logContext 日志上下文
     */
    void initialize(LogContext logContext);
}
