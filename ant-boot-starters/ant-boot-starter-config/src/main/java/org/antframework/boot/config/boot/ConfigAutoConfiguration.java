/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-10-02 18:40 创建
 */
package org.antframework.boot.config.boot;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 */
@Configuration
@Import(ConfigConfiguration.class)
public class ConfigAutoConfiguration {
}
