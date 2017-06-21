/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-05-19 19:43 创建
 */
package org.antframework.boot.bekit.boot;

import org.antframework.boot.bekit.servicelistener.CodeMessageClearServiceListener;
import org.antframework.boot.bekit.servicelistener.LogProcessServiceListener;
import org.antframework.boot.bekit.servicelistener.OrderValidateServiceListener;
import org.antframework.boot.bekit.servicelistener.ResultProcessServiceListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * bekit配置类
 */
@Configuration
public class BekitConfiguration {

    // 日志处理-服务监听器
    @Bean
    public LogProcessServiceListener logProcessServiceListener() {
        return new LogProcessServiceListener();
    }

    // 结果码、结果描述清理-服务监听器
    @Bean
    public CodeMessageClearServiceListener codeMessageClearServiceListener() {
        return new CodeMessageClearServiceListener();
    }

    // result处理-服务监听器
    @Bean
    public ResultProcessServiceListener resultProcessServiceListener() {
        return new ResultProcessServiceListener();
    }

    // order校验-服务监听器
    @Bean
    public OrderValidateServiceListener orderValidateServiceListener() {
        return new OrderValidateServiceListener();
    }

}
