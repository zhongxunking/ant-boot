/* 
 * Copyright © 2017 www.lvmama.com
 */

/*
 * 修订记录:
 * @author 钟勋（zhongxun@lvmama.com） 2017-08-31 18:14 创建
 */
package org.antframework.boot.jpa.boot;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * ant-jpa自动配置类（导入相关配置类）
 */
@Configuration
@Import(AntJpaConfiguration.class)
public class AntJpaAutoConfiguration {
}
