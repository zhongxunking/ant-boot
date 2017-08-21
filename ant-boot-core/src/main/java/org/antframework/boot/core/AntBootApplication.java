/* 
 * 作者：钟勋 (e-mail:zhongxunking@163.com)
 */

/*
 * 修订记录:
 * @author 钟勋 2017-08-21 21:42 创建
 */
package org.antframework.boot.core;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.annotation.*;

/**
 * ant-boot应用
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootApplication
public @interface AntBootApplication {

    /**
     * 应用编码
     */
    String appCode();

    /**
     * http端口（-1：关闭端口，0：随机端口，大于0：固定端口）
     */
    int httpPort();

}
