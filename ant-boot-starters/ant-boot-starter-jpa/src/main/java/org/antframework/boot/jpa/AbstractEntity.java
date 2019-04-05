/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-20 13:44 创建
 */
package org.antframework.boot.jpa;

import lombok.Getter;
import lombok.Setter;
import org.antframework.common.util.tostring.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * 抽象实体
 */
@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity implements Serializable {
    // id主键
    @Id
    @GeneratedValue
    @Column
    private Long id;

    // 创建时间
    @CreationTimestamp
    @Column
    private Date createTime;

    // 更新时间
    @UpdateTimestamp
    @Column
    private Date updateTime;

    @Override
    public String toString() {
        return ToString.toString(this);
    }
}
