/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-05 00:27 创建
 */
package org.antframework.boot.config.boot;

import org.bekit.event.boot.EventBusAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 配置中心自动配置类（导入相关配置类）
 */
@Configuration
@AutoConfigureAfter(EventBusAutoConfiguration.class)
@Import(ConfigConfiguration.class)
public class ConfigAutoConfiguration {
}
