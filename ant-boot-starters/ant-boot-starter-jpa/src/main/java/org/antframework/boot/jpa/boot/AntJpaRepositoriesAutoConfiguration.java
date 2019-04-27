/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-31 18:14 创建
 */
package org.antframework.boot.jpa.boot;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilderCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;

import javax.sql.DataSource;
import java.util.Map;

/**
 * jpa-repository自动配置类（导入相关配置类）
 * <p>
 * 由于spring-boot原生的集成spring-data-jpa没有提供扩展点，需要扩展功能的话只能自己写引导，参考{@link JpaRepositoriesAutoConfiguration}
 */
@Configuration
@ConditionalOnBean(DataSource.class)
@ConditionalOnClass(JpaRepository.class)
@ConditionalOnMissingBean({JpaRepositoryFactoryBean.class, JpaRepositoryConfigExtension.class})
@ConditionalOnProperty(prefix = "spring.data.jpa.repositories", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter({HibernateJpaAutoConfiguration.class, TaskExecutionAutoConfiguration.class})
@AutoConfigureBefore(JpaRepositoriesAutoConfiguration.class)
@Import(AntJpaRepositoriesConfigureRegistrar.class)
public class AntJpaRepositoriesAutoConfiguration {

    @Bean
    @Conditional(BootstrapExecutorCondition.class)
    public EntityManagerFactoryBuilderCustomizer entityManagerFactoryBootstrapExecutorCustomizer(
            Map<String, AsyncTaskExecutor> taskExecutors) {
        return (builder) -> {
            AsyncTaskExecutor bootstrapExecutor = determineBootstrapExecutor(
                    taskExecutors);
            if (bootstrapExecutor != null) {
                builder.setBootstrapExecutor(bootstrapExecutor);
            }
        };
    }

    private AsyncTaskExecutor determineBootstrapExecutor(
            Map<String, AsyncTaskExecutor> taskExecutors) {
        if (taskExecutors.size() == 1) {
            return taskExecutors.values().iterator().next();
        }
        return taskExecutors
                .get(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME);
    }

    private static final class BootstrapExecutorCondition extends AnyNestedCondition {

        BootstrapExecutorCondition() {
            super(ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnProperty(prefix = "spring.data.jpa.repositories",
                name = "bootstrap-mode", havingValue = "deferred", matchIfMissing = false)
        static class DeferredBootstrapMode {

        }

        @ConditionalOnProperty(prefix = "spring.data.jpa.repositories",
                name = "bootstrap-mode", havingValue = "lazy", matchIfMissing = false)
        static class LazyBootstrapMode {

        }

    }

}
