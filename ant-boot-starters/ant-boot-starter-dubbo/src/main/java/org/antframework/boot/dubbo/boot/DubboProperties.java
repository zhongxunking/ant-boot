/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-28 14:26 创建
 */
package org.antframework.boot.dubbo.boot;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * dubbo属性
 */
@ConfigurationProperties(DubboProperties.PREFIX)
public class DubboProperties {
    /**
     * 属性前缀
     */
    public static final String PREFIX = "dubbo";
    /**
     * 默认的dubbo服务注册缓存文件名
     */
    public static final String DEFAULT_REGISTRY_FILE = "dubbo.properties";

    /**
     * 必填：应用负责人
     */
    @NotBlank
    private String owner;
    /**
     * 必填：dubbo服务注册地址
     */
    @NotBlank
    private String registryAddress;
    /**
     * 必填：端口
     */
    @NotNull
    private Integer protocolPort;
    /**
     * 必填：服务提供者超时时间
     */
    @NotNull
    private Integer providerTimeout;
    /**
     * 必填：监控地址
     */
    @NotBlank
    private String monitorAddress;
    /**
     * 选填：dubbo服务注册缓存文件名
     */
    @NotBlank
    private String registryFile = DEFAULT_REGISTRY_FILE;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public Integer getProtocolPort() {
        return protocolPort;
    }

    public void setProtocolPort(Integer protocolPort) {
        this.protocolPort = protocolPort;
    }

    public Integer getProviderTimeout() {
        return providerTimeout;
    }

    public void setProviderTimeout(Integer providerTimeout) {
        this.providerTimeout = providerTimeout;
    }

    public String getMonitorAddress() {
        return monitorAddress;
    }

    public void setMonitorAddress(String monitorAddress) {
        this.monitorAddress = monitorAddress;
    }

    public String getRegistryFile() {
        return registryFile;
    }

    public void setRegistryFile(String registryFile) {
        this.registryFile = registryFile;
    }
}
