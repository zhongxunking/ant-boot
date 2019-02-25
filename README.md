# ant-boot框架

1. 简介

> spring-boot带来的高效、便捷，让越来越多的公司采用它作为自己的应用框架。spring-boot官方集成了一些常用的第三方工具，但也不是所有都集成（比如dubbo），对于大型互联网公司很重要的配置中心、应用id这些方案也未直接提供（众口难调，spring-boot也不可能面面俱到）。但是spring-boot提供强大的扩展能力，根据这些能力可以集成spring-boot未集成的第三方工具。但是如果每个系统都自己去集成一遍这些第三方工具，显然是没有必要的。为此本框架继承spring-boot，集成一些常用的工具，并根据具体情况对spring-boot原有的功能进行一些增强。

2. 环境要求：

> * jdk1.8

> 注意：本框架已经上传到[maven中央库](http://search.maven.org/#search%7Cga%7C1%7Corg.antframework.boot)

## 1. 核心
使用ant-boot时，启动类需使用@AntBootApplication（继承自@SpringBootApplication），并设置appId（应用id）。在大型互联网公司中系统会有很多，每个系统需要唯一的id用于区分。[参考](https://github.com/zhongxunking/configcenter/blob/master/configcenter-assemble/src/main/java/org/antframework/configcenter/Main.java)

ant-boot会在启动时给应用创建3个目录，分别是存放配置文件的目录、存放应用自己生成的数据文件目录、存放日志文件的目录，这3个目录可通过静态类Apps获取。这三个目录默认是/var/apps/config/${appId}、/var/apps/data/${appId}、/var/apps/log/${appId}。默认的目录可能不满足你的需求，可以分别通过设置以下系统属性自定义这3个目录：app.config-path、app.data-path、app.log-path。请保证应用有权限创建这三个目录。

ant-boot会在启动时校验当前jdk版本，如果小于jdk1.8，则会报错，让启动失败；同时如果未设置活动profile（当前环境），也会启动失败。启动阶段会将Spring容器、environment设置到静态类Contexts中，应用如果需要，可以从中获取。

我们在使用spring-boot时自己的会继承spring-boot-starter-parent，相应的使用ant-boot时自己的pom需要继承ant-boot-starter-parent：[参考](https://github.com/zhongxunking/configcenter/blob/master/pom.xml)
```xml
<parent>
    <groupId>org.antframework.boot</groupId>
    <artifactId>ant-boot-starter-parent</artifactId>
    <version>1.2.2.RELEASE</version>
</parent>
```
## 2. 日志
spring-boot原生的日志系统用于线上环境是存在问题的。一是单个日志文件容量太小，只有10MB；二是日志文件滚动策略未加入日期格式，以后查找不方便。为此对日志系统进行了扩展。

引入依赖：
```xml
<dependency>
    <groupId>org.antframework.boot</groupId>
    <artifactId>ant-boot-starter-logging</artifactId>
</dependency>
```
ant-boot会默认的打印控制台、info级别日志文件、error级别日志文件。单个日志文件默认大小是1GB，滚动策略加入了日期，方便以后查找。如果默认配置不满足你的需求（比如想要自己控制日志格式、以及其他详细配置），可以在配置文件（application.properties或application-xxx.properties）通过以“ant.logging.”开头的属性进行配置。spring-boot原生的以下日志配置不再生效：logging.pattern.console、logging.pattern.file、logging.pattern.level、logging.file、logging.path，其他日志配置依旧生效（比如logging.level）。

本日志系统提供良好的扩展性，自己实现LogInitializer接口，并将实现类配置到文件META-INF/spring.factories内，即可实现自己初始化日志。具体实现时可参考[FileLogInitializer](https://github.com/zhongxunking/ant-boot/blob/master/ant-boot-starters/ant-boot-starter-logging/src/main/java/org/antframework/boot/logging/initializer/FileLogInitializer.java)、[spring.factories](https://github.com/zhongxunking/ant-boot/blob/master/ant-boot-starters/ant-boot-starter-logging/src/main/resources/META-INF/spring.factories)

## 3. 配置中心
spring-boot没有提供配置中心解决方案，而配置中心对于大型互联网公司又是很重要的，它管理不同应用在不同环境中繁杂的配置。对此ant-boot自己集成配置中心——开源项目[configcenter](https://github.com/zhongxunking/configcenter)。请先按照configcenter的文档将它部署好。

引入依赖：
```xml
<dependency>
    <groupId>org.antframework.boot</groupId>
    <artifactId>ant-boot-starter-config</artifactId>
</dependency>
```
ant-boot会在刚启动时就将配置中心中的配置加入到spring的environment中，这样使用配置中心中的配置就跟使用spring-boot的配置文件一样，可以通过@Value等方式获取。需要注意的是配置中心的配置优先级是最低的。

## 4. 分布式id生成器
生成全局唯一的id（流水号），是很多公司都需要解决的问题。如果还是采用时间戳+随机数形式生成，在并发量大时，很有可能会生成重复的id。重复id的危害就是会导致一系列问题，比如幂等性。对此ant-boot集成分布式id生成器——开源项目[ids](https://github.com/zhongxunking/ids)。请先按照ids的文档将它部署好。

引入依赖：
```xml
<dependency>
    <groupId>org.antframework.boot</groupId>
    <artifactId>ant-boot-starter-id</artifactId>
</dependency>
```
创建管局唯一id：
```java
String id1 = UID.newId();
String id2 = UID.newId();
```
## 5. jpa
spring-boot原生集成的jpa大部分情况已经很好用了，但是对于简单的分页查询还是需要自己组装Specification，比较麻烦。所以本框架对分页查询功能进行增强，对于简单分页查询不用在需要自己组装Specification。

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
