/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-12-20 10:58 创建
 */
package org.antframework.boot.bekit;

import org.antframework.boot.bekit.service.CommonQueryService;
import org.springframework.util.ClassUtils;

/**
 * 通用分页查询常量
 */
public final class CommonQueryConstant {
    /**
     * 服务名称
     */
    public static final String SERVICE_NAME = ClassUtils.getShortNameAsProperty(CommonQueryService.class);

    /**
     * dao类型在服务上下文附件中的key（必需）
     */
    public static final String DAO_CLASS_KEY = "DAO_CLASS";

    /**
     * 排序在服务上下文附件中的key（可选）
     */
    public static final String SORT_KEY = "SORT";
}
