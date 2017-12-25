/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-12-20 10:44 创建
 */
package org.antframework.boot.bekit;

import org.antframework.common.util.facade.AbstractQueryResult;
import org.antframework.common.util.facade.AbstractResult;
import org.antframework.common.util.facade.FacadeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * 通用查询结果
 */
public class CommonQueryResult extends AbstractResult {
    // 页面提取器
    private FacadeUtils.PageExtractor pageExtractor;

    public void setPageExtractor(FacadeUtils.PageExtractor pageExtractor) {
        this.pageExtractor = pageExtractor;
    }

    /**
     * 转换为指定的目标类型
     *
     * @param targetType 目标类型
     * @param <T>        目标类型
     * @return 目标类型对象
     */
    public <T extends AbstractQueryResult> T convertTo(Class<T> targetType) {
        return convertTo(targetType, null);
    }

    /**
     * 转换为指定的目标类型
     *
     * @param targetType    目标类型
     * @param infoConverter info转换器
     * @param <T>           目标类型
     * @return 目标类型对象
     */
    public <T extends AbstractQueryResult> T convertTo(Class<T> targetType, Converter infoConverter) {
        T target = BeanUtils.instantiate(targetType);
        convertTo(target, infoConverter);
        return target;
    }

    /**
     * 转换到指定目标对象
     *
     * @param target        目标对象
     * @param infoConverter info转换器
     * @param <T>           目标对象类型
     */
    public <T extends AbstractQueryResult> void convertTo(T target, Converter infoConverter) {
        BeanUtils.copyProperties(this, target, "pageExtractor");
        if (!isSuccess()) {
            return;
        }
        if (infoConverter == null) {
            FacadeUtils.setQueryResult(target, pageExtractor);
        } else {
            FacadeUtils.setQueryResult(target, pageExtractor, infoConverter);
        }
    }
}
