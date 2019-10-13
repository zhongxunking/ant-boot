/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-09-11 13:54 创建
 */
package org.antframework.boot.env.listener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.antframework.common.util.tostring.ToString;
import org.antframework.common.util.tostring.format.Mask;

import java.io.Serializable;

/**
 * 变更的配置项
 */
@AllArgsConstructor
@Getter
public final class ChangedProperty implements Serializable {
    // 变更类型
    private final ChangeType type;
    // key
    private final String key;
    // 旧值
    @Mask(secureMask = true)
    private final String oldValue;
    // 新值
    @Mask(secureMask = true)
    private final String newValue;

    @Override
    public String toString() {
        return ToString.toString(this);
    }

    /**
     * 变更类型
     */
    public enum ChangeType {
        // 新增
        ADD,
        // 更新
        UPDATE,
        // 删除
        REMOVE
    }
}
