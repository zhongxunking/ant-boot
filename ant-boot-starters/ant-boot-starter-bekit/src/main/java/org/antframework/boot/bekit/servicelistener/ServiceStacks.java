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

/**
 * 服务栈操作类
 */
public final class ServiceStacks {
    // 服务栈持有器
    private static final ThreadLocal<Deque<Map<Object, Object>>> STACK_HOLDER = ThreadLocal.withInitial(LinkedList::new);

    // 压入新栈头
    static void push() {
        STACK_HOLDER.get().push(new HashMap<>());
    }

    // 弹出栈头
    static Map<Object, Object> pop() {
        Deque<Map<Object, Object>> stack = STACK_HOLDER.get();
        Map<Object, Object> node = stack.pop();
        if (stack.isEmpty()) {
            STACK_HOLDER.remove();
        }
        return node;
    }

    /**
     * 获取栈头
     */
    public static Map<Object, Object> peek() {
        return STACK_HOLDER.get().peek();
    }
}
