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
     * 转换为其他类型result
     *
     * @param resultClass 需转换成的类型
     * @param <T>         result类型
     * @return 其他类型result
     */
    public <T extends AbstractQueryResult> T convertTo(Class<T> resultClass) {
        return convertTo(resultClass, null);
    }

    /**
     * 转换为其他类型result
     *
     * @param resultClass   需转换成的类型
     * @param infoConverter info转换器
     * @param <T>           result类型
     * @return 其他类型result
     */
    public <T extends AbstractQueryResult> T convertTo(Class<T> resultClass, Converter infoConverter) {
        T result = BeanUtils.instantiate(resultClass);
        BeanUtils.copyProperties(this, result, "pageExtractor");
        if (infoConverter == null) {
            FacadeUtils.setQueryResult(result, pageExtractor);
        } else {
            FacadeUtils.setQueryResult(result, pageExtractor, infoConverter);
        }

        return result;
    }
}
