/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2019-05-16 23:02 创建
 */
package org.antframework.boot.env.refresh.placeholder;

import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.SystemPropertyUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * 占位符解析器
 */
public final class PlaceholderParser {
    // 解析器
    private static final PropertyPlaceholderHelper HELPER = new PropertyPlaceholderHelper(
            SystemPropertyUtils.PLACEHOLDER_PREFIX,
            SystemPropertyUtils.PLACEHOLDER_SUFFIX,
            SystemPropertyUtils.VALUE_SEPARATOR,
            true);

    /**
     * 解析
     *
     * @param placeholder 待解析的占位符
     * @return 占位符包含的所有key
     */
    public static Set<String> parse(String placeholder) {
        Set<String> keys = new HashSet<>();
        HELPER.replacePlaceholders(placeholder, key -> {
            keys.add(key);
            return null;
        });
        return keys;
    }
}
