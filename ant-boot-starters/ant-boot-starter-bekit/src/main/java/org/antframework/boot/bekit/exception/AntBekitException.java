/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-05-19 17:08 创建
 */
package org.antframework.boot.bekit.exception;


import org.antframework.common.util.facade.Status;

/**
 * 异常（指明执行结果）
 */
public class AntBekitException extends RuntimeException {
    // 执行结果状态
    private Status status;
    // 结果码
    private String code;

    public AntBekitException(Status status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public Status getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
