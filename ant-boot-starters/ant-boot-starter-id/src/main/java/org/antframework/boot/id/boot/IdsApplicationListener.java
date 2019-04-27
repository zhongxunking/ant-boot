/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2018-02-05 00:25 创建
 */
package org.antframework.boot.id.boot;

import org.antframework.boot.core.Contexts;
import org.antframework.common.util.other.IPUtils;
import org.antframework.idcenter.spring.boot.IdcenterProperties;
import org.antframework.ids.IdsParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

import java.io.File;

/**
 * ids应用监听器
 */
public class IdsApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    // 应用端口属性名
    private static final String SERVER_PORT_PROPERTY_NAME = "server.port";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        IdsProperties properties = Contexts.buildProperties(IdsProperties.class);
        String worker = IPUtils.getIPV4() + ":" + event.getEnvironment().getProperty(SERVER_PORT_PROPERTY_NAME, "8080");
        // 设置ids初始化所需要的数据
        if (properties.getIdcId() != null) {
            System.setProperty(IdsParams.IDC_ID_KEY, properties.getIdcId());
        }
        System.setProperty(IdsParams.IDCENTER_URL_KEY, IdcenterProperties.INSTANCE.getServerUrl());
        System.setProperty(IdsParams.HOME_PATH_KEY, Contexts.getHome() + File.separator + "ids");
        System.setProperty(IdsParams.WORKER_KEY, worker);
        System.setProperty(IdsParams.ZK_URLS_KEY, StringUtils.join(properties.getZkUrls(), ','));
        if (properties.getEncryptionSeed() != null) {
            System.setProperty(IdsParams.ENCRYPTION_SEED_KEY, properties.getEncryptionSeed().toString());
        }
    }
}
