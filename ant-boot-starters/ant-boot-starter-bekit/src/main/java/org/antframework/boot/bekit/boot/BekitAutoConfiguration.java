/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-22 11:53 创建
 */
package org.antframework.boot.bekit.boot;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * bekit自动配置类（导入相关配置类）
 */
@Configuration
@Import(BekitConfiguration.class)
public class BekitAutoConfiguration {
}
