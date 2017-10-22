/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-20 16:06 创建
 */
package org.antframework.boot.log;

/**
 * 日志初始化器
 */
public interface LogInitializer {

    /**
     * 初始化
     *
     * @param logContext 日志上下文
     */
    void init(LogContext logContext);
}
