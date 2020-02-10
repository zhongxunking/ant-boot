# ant-boot框架

1. 简介

> spring-boot带来的高效、便捷，让越来越多的公司采用它作为自己的应用框架。spring-boot本身很强大，但有些能力还可以做到更强大，更易于开发人员使用。本框架是根据spring-boot的一些扩展点，对一部分原有能力进行增强，同时新增一些常用能力。

2. 环境要求：

> * jdk1.8

> 注意：本框架已经上传到[maven中央库](http://search.maven.org/#search%7Cga%7C1%7Corg.antframework.boot)

## 1. 核心
启动阶段会将Spring容器、environment设置到静态类Contexts中，应用如果需要，可以从中获取。

我们在使用spring-boot时自己的pom.xml会继承spring-boot-starter-parent，相应的使用ant-boot时自己的pom需要继承ant-boot-starter-parent：[参考](https://github.com/zhongxunking/configcenter/blob/master/pom.xml)
```xml
<parent>
    <groupId>org.antframework.boot</groupId>
    <artifactId>ant-boot-starter-parent</artifactId>
    <version>2.2.0.RELEASE</version>
</parent>
```

## 2. Lang
使用ant-boot时，启动类需使用@AntBootApplication（继承自@SpringBootApplication），并设置appId（应用id）。在大型互联网公司中系统会有很多，每个系统需要唯一的id用于区分。[参考](https://github.com/zhongxunking/configcenter/blob/master/configcenter-assemble/src/main/java/org/antframework/configcenter/Main.java)

引入依赖：
```xml
<dependency>
    <groupId>org.antframework.boot</groupId>
    <artifactId>ant-boot-starter-lang</artifactId>
</dependency>
```

ant-boot会在启动时给应用创建一个home目录，默认为/var/apps/${appId}。默认的目录可能不满足你的需求，可以通过设置key为app.home的系统属性自定义这个home目录。请保证应用有权限创建这个home目录。

## 3. 配置变更通知机制
随着配置中心的流行，配置变更后，中间件需要对变更的配置热生效这种需求很常见。但spring-boot并没有提供配置变更通知这个能力。为此本框架提供一个通知机制，配置中心可以使用本机制进行通知

1、引入依赖：
```xml
<dependency>
    <groupId>org.antframework.boot</groupId>
    <artifactId>ant-boot-starter-env</artifactId>
</dependency>
```

2、定义配置监听器：
```java
// 监听当前应用变更的配置
@ConfigListener
public class MyConfigListener {
    // 监听所有配置
    @ListenConfigChanged(prefix = "")
    public void listenAll(List<ChangedProperty> changedProperties) {
        // TODO 具体业务代码
    }

    // 监听redis配置（prefix表示需要监听的配置前缀。当以“redis.”开头的配置项被修改时，
    // 被修改的配置会作为入参调用本方法。比如redis.host、redis.port等被修改时都会调用本方法）
    @ListenConfigChanged(prefix = "redis")
    public void listenPool(List<ChangedProperty> changedProperties) {
        // TODO 具体业务代码
    }
    
    // 监听具体某一个配置项（注意：入参不再是List<ChangedProperty>，而是ChangedProperty）
    @ListenConfigChanged(prefix = "redis.host")
    public void listenPool(ChangedProperty changedProperty) {
        // TODO 具体业务代码
    }
}
```
3、发布配置变更通知
```java
Envs.getConfigListeners().onChange(appId, changedProperties)
```


## 4. 日志
spring-boot原生的默认日志系统用于生产环境是存在问题的：一是单个日志文件容量太小，只有10MB；二是日志文件滚动策略未加入日期格式，以后查找不方便。它的设计目的是用于测试环境快速搭建应用；而在生产环境中，期望开发人员定义自己的logback.xml文件来替换它的默认实现。对开发人员来说，定义自己的logback.xml文件比较麻烦。为此本框架对默认的日志系统进行了扩展，不需要定义logback.xml文件就可以使用生产级别的日志系统。

引入依赖：
```xml
<dependency>
    <groupId>org.antframework.boot</groupId>
    <artifactId>ant-boot-starter-logging</artifactId>
</dependency>
```
ant-boot会默认打印控制台、日志文件、error级别日志文件。单个日志文件默认大小是1GB，滚动策略加入了日期，方便以后查找。如果默认配置不满足你的需求（比如想要自己控制日志格式、以及其他详细配置），可以在配置文件（application.properties或application-xxx.properties）通过以“ant.logging.”开头的属性进行配置。原生的logging.level、logging.group依然生效。而其他日志配置不再生效：logging.pattern.console、logging.pattern.file、logging.pattern.level、logging.file、logging.path。

本日志系统提供良好的扩展性，自己实现LogInitializer接口，并将实现类配置到文件META-INF/spring.factories内，即可实现自己初始化日志。具体实现时可参考[FileAppenderInitializer](https://github.com/zhongxunking/ant-boot/blob/master/ant-boot-starters/ant-boot-starter-logging/src/main/java/org/antframework/boot/logging/initializer/FileAppenderInitializer.java)、[spring.factories](https://github.com/zhongxunking/ant-boot/blob/master/ant-boot-starters/ant-boot-starter-logging/src/main/resources/META-INF/spring.factories)

## 5. jpa
spring-boot原生集成的jpa大部分情况已经很好用了，但是对于简单的分页查询还是需要自己组装Specification，比较麻烦。所以本框架对分页查询功能进行增强，对于简单分页查询不用再需要开发人员自己组装Specification。

引入依赖：
```xml
<dependency>
    <groupId>org.antframework.boot</groupId>
    <artifactId>ant-boot-starter-jpa</artifactId>
</dependency>
```
分页查询是采用[SpecificationUtils](https://github.com/zhongxunking/ant-boot/blob/master/ant-boot-starters/ant-boot-starter-jpa/src/main/java/org/antframework/boot/jpa/support/SpecificationUtils.java)定义的语法进行查询，很方便。

## 6. 集成bekit
[bekit](https://github.com/zhongxunking/bekit)是一个很好用的业务框架，里面包含事件总线、流程引擎、服务引擎。同时ant-boot借助bekit的服务引擎提供了一套[服务调用契约](https://github.com/zhongxunking/ant-common-util#3-服务调用契约)，来规范服务调用。

引入依赖：
```
<dependency>
    <groupId>org.antframework.boot</groupId>
    <artifactId>ant-boot-starter-bekit</artifactId>
</dependency>
```
具体使用可参考：https://github.com/zhongxunking/configcenter/blob/master/configcenter-biz/src/main/java/org/antframework/configcenter/biz/provider/AppServiceProvider.java
