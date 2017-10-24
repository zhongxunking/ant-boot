/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-21 18:06 创建
 */
package org.antframework.boot.config.boot;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置中心属性
 */
@ConfigurationProperties(prefix = ConfigcenterProperties.PREFIX)
public class ConfigcenterProperties {
    /**
     * 属性前缀
     */
    public static final String PREFIX = "configcenter";
    /**
     * 是否开启监听的属性名
     */
    public static final String LISTEN_ENABLE_PROPERTY_NAME = PREFIX + ".listen.enable";

    /**
     * 必填：配置中心服务端地址（比如：http://localhost:8080）
     */
    @NotBlank
    private String serverUrl;
    /**
     * 必填：配置中心使用的zookeeper地址，存在多个zookeeper的话以“,”分隔（比如：192.168.0.1:2181,192.168.0.2:2181）
     */
    @NotEmpty
    private String[] zkUrls;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String[] getZkUrls() {
        return zkUrls;
    }

    public void setZkUrls(String[] zkUrls) {
        this.zkUrls = zkUrls;
    }
}
