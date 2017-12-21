/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-12-18 14:28 创建
 */
package org.antframework.boot.jpa.support;

import org.antframework.common.util.query.QueryParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 构建jpa动态查询条件工具类
 */
public class SpecificationUtils {
    /**
     * 嵌套的属性名分隔符
     */
    public static final char NESTED_ATTRNAME_SEPARATOR = '.';

    /**
     * 根据查询参数解析出Specification
     *
     * @param queryParams 查询参数
     * @param <T>         实体类型
     * @return 解析出的Specification
     */
    public static <T> Specification<T> parse(Collection<QueryParam> queryParams) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                for (QueryParam queryParam : queryParams) {
                    Path<String> path = getPath(root, queryParam.getAttrName());
                    Predicate predicate = buildPredicate(cb, path, queryParam);
                    predicates.add(predicate);
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    // 构建Predicate
    private static Predicate buildPredicate(CriteriaBuilder cb, Path path, QueryParam queryParam) {
        Predicate predicate;

        Object value = queryParam.getValue();
        switch (queryParam.getOperator()) {
            case EQ:
                predicate = cb.equal(path, value);
                break;
            case NOTEQ:
                predicate = cb.notEqual(path, value);
                break;
            case GT:
                predicate = cb.greaterThan(path, (Comparable) value);
                break;
            case GTE:
                predicate = cb.greaterThanOrEqualTo(path, (Comparable) value);
                break;
            case LT:
                predicate = cb.lessThan(path, (Comparable) value);
                break;
            case LTE:
                predicate = cb.lessThanOrEqualTo(path, (Comparable) value);
                break;
            case LIKE:
                predicate = cb.like(path, value.toString());
                break;
            case NULL:
                predicate = cb.isNull(path);
                break;
            case NOTNULL:
                predicate = cb.isNotNull(path);
                break;
            case IN:
                if (value instanceof Object[]) {
                    predicate = path.in((Object[]) value);
                } else if (value instanceof Collection) {
                    predicate = path.in((Collection) value);
                } else {
                    throw new IllegalArgumentException("非法的IN操作");
                }
                break;
            default:
                throw new IllegalArgumentException("非法的操作符");
        }

        return predicate;
    }

    // 根据属性名获取路径
    private static Path getPath(Root root, String attrName) {
        Path path = root;
        for (String name : StringUtils.split(attrName, NESTED_ATTRNAME_SEPARATOR)) {
            path = path.get(name);
        }
        return path;
    }
}
