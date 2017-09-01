/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-24 21:51 创建
 */
package org.antframework.boot.jpa;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 构建jpa动态查询条件工具类
 */
public class SpecificationUtils {
    /**
     * 嵌套的属性名分隔符
     */
    public static final char NESTED_ATTRNAME_SEPARATOR = '.';

    /**
     * 根据Map解析
     *
     * @param searchParams 查询参数
     */
    public static <T> Specification<T> parse(Map<String, Object> searchParams) {
        return parse(SearchFilter.parse(searchParams));
    }

    /**
     * 根据查询条件解析
     *
     * @param filters 查询条件
     */
    public static <T> Specification<T> parse(final List<SearchFilter> filters) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                for (SearchFilter filter : filters) {
                    Path<String> path = getPath(root, filter.attrName);
                    Predicate predicate = buildPredicate(cb, path, filter);
                    predicates.add(predicate);
                }

                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

    // 构建Predicate
    private static Predicate buildPredicate(CriteriaBuilder cb, Path path, SearchFilter filter) {
        Predicate predicate;
        switch (filter.operator) {
            case EQ:
                predicate = cb.equal(path, filter.value);
                break;
            case GT:
                predicate = cb.greaterThan(path, (Comparable) filter.value);
                break;
            case GTE:
                predicate = cb.greaterThanOrEqualTo(path, (Comparable) filter.value);
                break;
            case LT:
                predicate = cb.lessThan(path, (Comparable) filter.value);
                break;
            case LTE:
                predicate = cb.lessThanOrEqualTo(path, (Comparable) filter.value);
                break;
            case LIKE:
                predicate = cb.like(path, "%" + filter.value + "%");
                break;
            case LLIKE:
                predicate = cb.like(path, "%" + filter.value);
                break;
            case RLIKE:
                predicate = cb.like(path, filter.value + "%");
                break;
            case NULL:
                predicate = cb.isNull(path);
                break;
            case NOTNULL:
                predicate = cb.isNotNull(path);
                break;
            case IN:
                if (filter.value instanceof Object[]) {
                    predicate = path.in((Object[]) filter.value);
                } else if (filter.value instanceof Collection) {
                    predicate = path.in((Collection) filter.value);
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

    /**
     * 查询条件
     */
    public static class SearchFilter {
        /**
         * 操作符与属性名之间的分隔符
         */
        public static final char OPERATOR_ATTRNAME_SEPARATOR = '_';

        // 属性名
        private String attrName;
        // 操作符
        private Operator operator;
        // 值
        private Object value;

        public SearchFilter(String attrName, Operator operator, Object value) {
            Assert.hasText(attrName, "无效的属性名：" + attrName);
            Assert.notNull(operator, "操作符不能为null");
            this.attrName = attrName;
            this.operator = operator;
            this.value = value;
        }

        /**
         * 解析
         *
         * @param searchParams 需被解析的查询参数
         */
        public static List<SearchFilter> parse(Map<String, Object> searchParams) {
            List<SearchFilter> filters = new ArrayList<>();
            for (String key : searchParams.keySet()) {
                String[] names = StringUtils.split(key, OPERATOR_ATTRNAME_SEPARATOR);
                if (names.length != 2) {
                    throw new IllegalArgumentException("非法查询参数：" + key);
                }
                filters.add(new SearchFilter(names[1], Operator.valueOf(names[0]), searchParams.get(key)));
            }

            return filters;
        }
    }

    /**
     * 操作符
     */
    public enum Operator {
        // 等于
        EQ,
        // 大于
        GT,
        // 大于等于
        GTE,
        // 小于
        LT,
        // 小于等于
        LTE,
        // like操作
        LIKE,
        // 仅左like
        LLIKE,
        // 仅右like
        RLIKE,
        // 等于null
        NULL,
        // 不等于null
        NOTNULL,
        // in操作
        IN
    }
}
