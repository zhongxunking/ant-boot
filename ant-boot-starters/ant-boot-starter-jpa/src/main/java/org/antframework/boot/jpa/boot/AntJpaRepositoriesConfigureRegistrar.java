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
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Locale;

/**
 * jpa-repository配置类（设置repository实现基础类）
 * <p>
 * 参考：org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfigureRegistrar
 */
public class AntJpaRepositoriesConfigureRegistrar extends AbstractRepositoryConfigurationSourceSupport {

    private BootstrapMode bootstrapMode = null;

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableJpaRepositories.class;
    }

    @Override
    protected Class<?> getConfiguration() {
        return EnableJpaRepositoriesConfiguration.class;
    }

    @Override
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new JpaRepositoryConfigExtension();
    }

    @Override
    protected BootstrapMode getBootstrapMode() {
        return (this.bootstrapMode == null) ? super.getBootstrapMode()
                : this.bootstrapMode;
    }

    @Override
    public void setEnvironment(Environment environment) {
        super.setEnvironment(environment);
        configureBootstrapMode(environment);
    }

    private void configureBootstrapMode(Environment environment) {
        String property = environment
                .getProperty("spring.data.jpa.repositories.bootstrap-mode");
        if (StringUtils.hasText(property)) {
            this.bootstrapMode = BootstrapMode
                    .valueOf(property.toUpperCase(Locale.ENGLISH));
        }
    }

    @EnableJpaRepositories(repositoryBaseClass = JpaQueryRepository.class)
    private static class EnableJpaRepositoriesConfiguration {

    }

}
