/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-02-08 20:00 创建
 */
package org.antframework.boot.bekit.servicelistener;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 服务栈操作类
 */
public final class ServiceStacks {
    // 服务栈持有器
    private static final ThreadLocal<Deque<Map>> STACK_HOLDER = ThreadLocal.withInitial(new Supplier<Deque<Map>>() {
        @Override
        public Deque<Map> get() {
            return new LinkedList<>();
        }
    });

    // 压入新节点
    static void push() {
        STACK_HOLDER.get().push(new HashMap());
    }

    // 弹出节点
    static Map pop() {
        Deque<Map> stack = STACK_HOLDER.get();
        Map node = stack.pop();
        if (stack.isEmpty()) {
            STACK_HOLDER.remove();
        }
        return node;
    }

    /**
     * 获取栈头
     */
    public static Map<Object, Object> getHead() {
        return STACK_HOLDER.get().peek();
    }
}
