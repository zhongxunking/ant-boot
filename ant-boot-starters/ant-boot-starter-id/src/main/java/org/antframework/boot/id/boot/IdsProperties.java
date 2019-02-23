/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-02-05 00:27 创建
 */
package org.antframework.boot.id.boot;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Set;

/**
 * ids配置
 */
@ConfigurationProperties("ids")
@Validated
@Getter
@Setter
public class IdsProperties {
    /**
     * 选填：数据中心id（比如：01。如果不存在多个数据中心，则不用填）
     */
    private String idcId;
    /**
     * 必填：注册worker使用的zookeeper地址（存在多个zookeeper的话以“,”分隔（比如：192.168.0.1:2181,192.168.0.2:2181））
     */
    @NotEmpty
    private Set<String> zkUrls;
    /**
     * 选填：加密种子（如果不需要对UID进行加密，则不用填）
     */
    private Long encryptionSeed;
}
