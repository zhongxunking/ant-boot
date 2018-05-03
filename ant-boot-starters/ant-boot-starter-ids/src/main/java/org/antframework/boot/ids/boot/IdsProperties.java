/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-02-05 00:27 创建
 */
package org.antframework.boot.ids.boot;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * ids属性
 */
@ConfigurationProperties(prefix = IdsProperties.PREFIX)
public class IdsProperties {
    /**
     * 属性前缀
     */
    public static final String PREFIX = "ids";

    /**
     * 必填：id中心服务端地址（比如：http://localhost:6210）
     */
    @NotBlank
    private String serverUrl;

    /**
     * 必填：注册worker使用的zookeeper地址（存在多个zookeeper的话以“,”分隔（比如：192.168.0.1:2181,192.168.0.2:2181））
     */
    @NotEmpty
    private Set<String> zkUrls;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public Set<String> getZkUrls() {
        return zkUrls;
    }

    public void setZkUrls(Set<String> zkUrls) {
        this.zkUrls = zkUrls;
    }
}
