/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-31 17:33 创建
 */
package org.antframework.boot.jpa;

import org.antframework.common.util.query.QueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 查询repository
 */
@NoRepositoryBean
public interface QueryRepository<T, ID extends Serializable> extends Repository<T, ID> {
    /**
     * 分页查询
     *
     * @param queryParams 查询参数
     * @param pageable    分页信息
     * @return 查询结果
     */
    Page<T> query(Collection<QueryParam> queryParams, Pageable pageable);

    /**
     * 查询
     *
     * @param queryParams 查询参数
     * @return 查询结果
     */
    List<T> query(Collection<QueryParam> queryParams);

    /**
     * 排序查询
     *
     * @param queryParams 查询参数
     * @param sort        排序
     * @return 查询结果
     */
    List<T> query(Collection<QueryParam> queryParams, Sort sort);

    /**
     * 统计
     *
     * @param queryParams 查询参数
     * @return 统计结果
     */
    long count(Collection<QueryParam> queryParams);
}
