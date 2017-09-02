/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-31 18:12 创建
 */
package org.antframework.boot.jpa.boot;

import org.antframework.boot.jpa.support.JpaQueryRepository;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

/**
 * jpa-repository配置类（设置repository实现基础类）
 * <p>
 * 参考{@link org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfigureRegistrar}
 */
public class AntJpaRepositoriesConfigureRegistrar extends AbstractRepositoryConfigurationSourceSupport {

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableJpaRepositories.class;
    }

    @Override
    protected Class<?> getConfiguration() {
        return EnableAntJpaRepositoriesConfiguration.class;
    }

    @Override
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new JpaRepositoryConfigExtension();
    }

    @EnableJpaRepositories(repositoryBaseClass = JpaQueryRepository.class)
    private static class EnableAntJpaRepositoriesConfiguration {
    }
}
