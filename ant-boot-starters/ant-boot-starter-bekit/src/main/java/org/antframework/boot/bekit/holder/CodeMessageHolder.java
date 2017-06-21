/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-05-19 17:56 创建
 */
package org.antframework.boot.bekit.holder;

import org.antframework.boot.bekit.exception.AntBekitException;
import org.antframework.common.util.facade.Status;

/**
 * 结果码、结果描述持有器
 */
public class CodeMessageHolder {
    // 线程变量存储结果信息
    private static final ThreadLocal<CodeMessageInfo> threadLocal = new ThreadLocal<>();

    /**
     * 设置结果码、结果描述
     */
    public static void set(String code, String message) {
        threadLocal.set(new CodeMessageInfo(code, message));
    }

    /**
     * 获取结果码、结果描述
     */
    public static CodeMessageInfo get() {
        return threadLocal.get();
    }

    /**
     * 删除结果码、结果描述
     */
    public static void remove() {
        threadLocal.remove();
    }

    /**
     * 根据现有的结果信息创建AntBekitException
     *
     * @param status 结果状态
     */
    public static AntBekitException newAntBekitException(Status status) {
        CodeMessageInfo codeMessageInfo = get();
        if (codeMessageInfo != null) {
            return new AntBekitException(status, codeMessageInfo.getCode(), codeMessageInfo.getMessage());
        } else {
            return new AntBekitException(status, "001", "未知描述");
        }
    }

    /**
     * 结果码、结果描述-信息
     */
    public static class CodeMessageInfo {
        // 结果码
        private String code;
        // 结果描述
        private String message;

        public CodeMessageInfo(String code, String message) {
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
