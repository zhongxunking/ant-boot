/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:53 创建
 */
package org.antframework.boot.bekit;

import org.antframework.common.util.facade.CommonResultCode;
import org.antframework.common.util.facade.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * 结果码、结果描述持有器
 */
public class CodeMessageHolder {
    // keeper持有器
    private static final ThreadLocal<List<Keeper<CodeMessageInfo>>> KEEPERS_HOLDER = new ThreadLocal<List<Keeper<CodeMessageInfo>>>() {
        @Override
        protected List<Keeper<CodeMessageInfo>> initialValue() {
            return new ArrayList<>(2);
        }
    };

    /**
     * 设置结果码、结果描述
     */
    public static void set(String code, String message) {
        getCurrentKeeper().set(new CodeMessageInfo(code, message));
    }

    /**
     * 获取结果码、结果描述
     */
    public static CodeMessageInfo get() {
        return getCurrentKeeper().get();
    }

    /**
     * 删除结果码、结果描述
     */
    public static void remove() {
        getCurrentKeeper().set(null);
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
            if (status == Status.SUCCESS) {
                return new AntBekitException(status, CommonResultCode.SUCCESS.getCode(), CommonResultCode.SUCCESS.getMessage());
            } else {
                return new AntBekitException(status, CommonResultCode.UNKNOWN_ERROR.getCode(), CommonResultCode.UNKNOWN_ERROR.getMessage());
            }
        }
    }

    /**
     * push新持有器（本方法由框架调用，使用方不能调用）
     */
    public static void pushKeeper() {
        KEEPERS_HOLDER.get().add(new Keeper<>());
    }

    /**
     * 弹出最上层持有器（本方法由框架调用，使用方不能调用）
     */
    public static void popKeeper() {
        List<Keeper<CodeMessageInfo>> keepers = KEEPERS_HOLDER.get();
        if (keepers.size() > 0) {
            keepers.remove(keepers.size() - 1);
        }
        if (keepers.size() <= 0) {
            KEEPERS_HOLDER.remove();
        }
    }

    // 获取当前持有器
    private static Keeper<CodeMessageInfo> getCurrentKeeper() {
        List<Keeper<CodeMessageInfo>> keepers = KEEPERS_HOLDER.get();
        if (keepers.size() <= 0) {
            keepers.add(new Keeper<>());
        }
        return keepers.get(keepers.size() - 1);
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

    // 持有器
    private static class Keeper<T> {
        private T t;

        public T get() {
            return t;
        }

        public void set(T t) {
            this.t = t;
        }
    }
}
