/* 
 * Copyright © 2017 www.lvmama.com
 */

/*
 * 修订记录:
 * @author 钟勋（zhongxun@lvmama.com） 2017-08-31 18:12 创建
 */
package org.antframework.boot.jpa.boot;

import org.antframework.boot.jpa.support.JpaQueryRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * ant-jpa配置类（设置repository实现基础类）
 */
@Configuration
@EnableJpaRepositories(repositoryBaseClass = JpaQueryRepository.class)
public class AntJpaConfiguration {
}
