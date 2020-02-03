/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-12-19 20:22 创建
 */
package org.antframework.boot.bekit.service;

import lombok.AllArgsConstructor;
import org.antframework.boot.bekit.CommonQueries;
import org.antframework.boot.jpa.QueryRepository;
import org.antframework.common.util.facade.AbstractQueryOrder;
import org.antframework.common.util.facade.FacadeUtils;
import org.antframework.common.util.other.Cache;
import org.antframework.common.util.query.QueryParam;
import org.antframework.common.util.query.annotation.QueryParams;
import org.bekit.service.annotation.service.Service;
import org.bekit.service.annotation.service.ServiceExecute;
import org.bekit.service.engine.ServiceContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 通用查询服务
 */
@Service
@AllArgsConstructor
public class CommonQueryService {
    // 查询执行器缓存
    private final Cache<Class<?>, QueryExecutor> queryExecutorsCache = new Cache<>(this::parseToQueryExecutor);
    // 应用上下文
    private final ApplicationContext applicationContext;

    @ServiceExecute
    public void execute(ServiceContext<AbstractQueryOrder, CommonQueries.CommonQueryResult> context) throws Throwable {
        AbstractQueryOrder order = context.getOrder();
        CommonQueries.CommonQueryResult result = context.getResult();
        // 获取dao类型
        Class<?> daoClass = (Class<?>) context.getAttachment().get(CommonQueries.DAO_CLASS_KEY);
        Assert.notNull(daoClass, "附件中缺少" + CommonQueries.DAO_CLASS_KEY);
        // 查询
        Pageable pageable = PageRequest.of(order.getPageNo() - 1, order.getPageSize(), (Sort) context.getAttachment().get(CommonQueries.SORT_KEY));
        Page<?> page = queryExecutorsCache.get(daoClass).execute(QueryParams.parse(order), pageable);
        // 设置查询结果
        result.setPageExtractor(new FacadeUtils.SpringDataPageExtractor<>(page));
    }

    // 解析出查询执行器
    private QueryExecutor parseToQueryExecutor(Class<?> daoClass) {
        // 获取查询方法
        Method queryMethod = ReflectionUtils.findMethod(daoClass, "query", Collection.class, Pageable.class);
        if (queryMethod == null) {
            throw new IllegalArgumentException(String.format("dao[%s]需要定义query方法（Page<T> query(Collection<QueryParam> queryParams, Pageable pageable)），或者继承[%s]", daoClass.getName(), QueryRepository.class.getName()));
        }
        Class<?> genericClass = ResolvableType.forMethodParameter(queryMethod, 0).getGeneric(0).resolve(Object.class);
        if (genericClass != QueryParam.class) {
            throw new IllegalArgumentException(String.format("dao[%s]定义的query方法[%s]入参Collection的范型必须是%s", daoClass.getName(), queryMethod, QueryParam.class.getName()));
        }
        if (queryMethod.getReturnType() != Page.class) {
            throw new IllegalArgumentException(String.format("dao[%s]定义的query方法[%s]返回类型必须是%s", daoClass.getName(), queryMethod, Page.class.getName()));
        }
        // 创建查询执行器
        return new QueryExecutor(applicationContext.getBean(daoClass), queryMethod);
    }

    // 查询执行器
    @AllArgsConstructor
    private static class QueryExecutor {
        // dao对象
        private final Object dao;
        // 查询方法
        private final Method queryMethod;

        // 执行
        Page<?> execute(Collection<QueryParam> queryParams, Pageable pageable) throws Throwable {
            try {
                return (Page<?>) queryMethod.invoke(dao, new Object[]{queryParams, pageable});
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        }
    }
}
