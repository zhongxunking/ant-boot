/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-31 17:49 创建
 */
package org.antframework.boot.jpa.support;

import org.antframework.boot.jpa.QueryRepository;
import org.antframework.common.util.query.QueryParam;
import org.antframework.common.util.query.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 包含易使用查询功能的Repository实现基类
 */
public class JpaQueryRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements QueryRepository<T, ID> {

    public JpaQueryRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    @Override
    public Page<T> query(Collection<QueryParam> queryParams, Pageable pageable) {
        return findAll(SpecificationUtils.parse(queryParams), pageable);
    }

    @Override
    public List<T> query(Collection<QueryParam> queryParams) {
        return findAll(SpecificationUtils.parse(queryParams));
    }

    @Override
    public List<T> query(Collection<QueryParam> queryParams, Sort sort) {
        return findAll(SpecificationUtils.parse(queryParams), sort);
    }

    @Override
    public long count(Collection<QueryParam> queryParams) {
        return count(SpecificationUtils.parse(queryParams));
    }
}
