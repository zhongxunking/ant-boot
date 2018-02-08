/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:53 创建
 */
package org.antframework.boot.bekit;

import org.antframework.boot.bekit.servicelistener.ServiceStacks;
import org.antframework.common.util.facade.BizException;
import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;

/**
 * 结果码、描述持有器
 */
public final class CodeMessageHolder {

    /**
     * 设置结果码、描述
     */
    public static void set(String code, String message) {
        ServiceStacks.getHead().put(CodeMessageHolder.class, new CodeMessage(code, message));
    }

    /**
     * 获取结果码、描述
     */
    public static CodeMessage get() {
        return (CodeMessage) ServiceStacks.getHead().get(CodeMessageHolder.class);
    }

    /**
     * 删除结果码、描述
     */
    public static void remove() {
        ServiceStacks.getHead().remove(CodeMessageHolder.class);
    }

    /**
     * 根据现有的结果码、描述创建BizException
     *
     * @param status 结果状态
     */
    public static BizException newBizException(Status status) {
        CodeMessage codeMessage = get();
        if (codeMessage != null) {
            return new BizException(status, codeMessage.getCode(), codeMessage.getMessage());
        } else {
            if (status == Status.SUCCESS) {
                return new BizException(status, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage());
            } else {
                return new BizException(status, CommonResultCode.UNKNOWN_ERROR.getCode(), CommonResultCode.UNKNOWN_ERROR.getMessage());
            }
        }
    }

    /**
     * 结果码、描述
     */
    public static final class CodeMessage {
        // 结果码
        private final String code;
        // 描述
        private final String message;

        public CodeMessage(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
