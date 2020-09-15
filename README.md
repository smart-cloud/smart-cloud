# 一、功能特征
**一个基于spring cloud实现的脚手架。所实现功能如下：**
- [接口文档自动生成（利用idea yapi插件上传到yapi server）](https://github.com/smart-cloud/smart-cloud#%E4%BA%94%E6%8E%A5%E5%8F%A3%E6%96%87%E6%A1%A3)
- [可以生成mock数据，充分发挥前后端分离的作用](https://github.com/smart-cloud/smart-cloud#%E4%B8%89%E6%8E%A5%E5%8F%A3mock%E6%95%B0%E6%8D%AE)
- [部署灵活，服务可合并（合并后服务间通过内部进程通信；分开后通过rpc通信）部署，合并后也可拆分开部署](https://github.com/smart-cloud/smart-cloud#%E5%9B%9B%E6%9C%8D%E5%8A%A1%E5%90%88%E5%B9%B6%E5%8E%9F%E7%90%86)
- 业务无关代码自动生成
- [接口（加密+签名）安全保证](https://github.com/smart-cloud/smart-cloud-examples#%E4%BA%8C%E6%8E%A5%E5%8F%A3%E5%AE%89%E5%85%A8)
- 业务无关功能（如日志打印、公共配置、常用工具类等）抽象为starter
- [支持多数据源、分表分库、分布式事务](https://github.com/smart-cloud/smart-cloud#1%E5%A4%9A%E6%95%B0%E6%8D%AE%E6%BA%90%E5%86%B2%E7%AA%81)
- 支持多语言（国际化）
- 敏感配置信息支持加密
- [日志敏感数据脱敏](https://github.com/smart-cloud/smart-cloud#%E4%BA%8C%E6%97%A5%E5%BF%97%E6%95%B0%E6%8D%AE%E8%84%B1%E6%95%8F)
- 单体服务开发接阶段测试不依赖其他服务（挡板测试、关闭eureka、sentinel等）
- 技术栈稳定、实用、易用

示例工程见[smart-cloud-examples](https://github.com/smart-cloud/smart-cloud-examples)

# 二、模块说明

```
smart-cloud
├── smart-api-core -- 接口权限、加解密、签名等注解
├── smart-code-generate -- 代码生成
├── smart-common-pojo -- 公共对象
├── smart-mask -- 敏感数据混淆
├── smart-utility -- 工具类
└── smart-could-starter -- 框架封装
     ├── smart-cloud-starter-configure -- 框架配置属性封装
     ├── smart-cloud-starter-core -- 框架核心（自定义注解、异常封装、请求响应公共参数、业务相关工具类）
     ├── smart-cloud-starter-dubbo -- dubbo封装
     ├── smart-cloud-starter-feign -- feign封装（可拆可合、切面处理）
     ├── smart-cloud-starter-job -- 定时任务封装
     ├── smart-cloud-starter-locale -- 国际化封装
     ├── smart-cloud-starter-log -- 日志封装（log4j2封装，支持日志敏感数据脱敏）
     ├── smart-cloud-starter-mock -- mock封装（mock工具类、常用mock策略、请求接口mock拦截器）
     ├── smart-cloud-starter-mybatis -- mybatis封装（支持多数据源、分库分表、分布式事务；通用mapper封装，mapper工具类等;支持通用mapper、mybatis plus）
     ├── smart-cloud-starter-redis -- redis封装
     ├── smart-cloud-starter-test -- test封装
     └── smart-cloud-starter-web -- web封装（切面、异常处理、参数校验）
```

# 三、技术栈

 名称 | 说明 
---|---
[spring boot](https://spring.io/projects/spring-boot/) | 手脚架 
[spring cloud gateway](https://spring.io/projects/spring-cloud-gateway) | 服务网关 
[eureka](https://spring.io/projects/spring-cloud-netflix) | 服务注册 
[spring boot admin](https://github.com/codecentric/spring-boot-admin) | 服务监控 
[openfeign](https://spring.io/projects/spring-cloud-openfeign)、[protostuff](https://github.com/protostuff/protostuff) | 声明式服务调用 
[sleuth](https://spring.io/projects/spring-cloud-sleuth)、[log4j2](https://logging.apache.org/log4j/2.x/) | 链路追踪、日志 
[mybatis](http://www.mybatis.org/mybatis-3/zh/index.html) 、[mapper](https://github.com/abel533/Mapper)、[mybatis plus](https://github.com/baomidou/mybatis-plus)| ORM 
[seata](https://github.com/seata/seata) | 分布式事务
[sentinel](https://github.com/alibaba/Sentinel) | 限流、熔断降级
[sharding jdbc](https://github.com/apache/incubator-shardingsphere) | 分库分表
[redis](https://redis.io/)、[embedded-redis](https://github.com/kstyrc/embedded-redis) | 缓存、集成测试 
[rocketmq](https://github.com/apache/rocketmq) | 消息队列 
[fastdfs](https://github.com/happyfish100/fastdfs) | 文件存储 
[xxl-job](https://github.com/xuxueli/xxl-job)| 定时任务 
[easyexcel](https://github.com/alibaba/easyexcel) | excel导入导出
[Hibernator-Validator](http://hibernate.org/validator/) | 参数校验 
[mockito](https://site.mockito.org/)、[podam](https://github.com/mtedone/podam) | 单元测试、数据mock
[freemarker](https://freemarker.apache.org/) | 用于代码生成
[yapi](https://github.com/YMFE/yapi)、[idea yapi upload plugin](https://github.com/smart-cloud/yapi_upload) | 接口文档 
[jasypt-spring-boot](https://github.com/ulisesbocchio/jasypt-spring-boot) | 配置文件中敏感数据加解密
[Lombok](https://www.projectlombok.org/) | 简化代码 

# 四、服务合并原理
![](docs/images/service_merge.png)

合并服务只需修改pom.xml，将待合并的服务import进去即可。
# 五、相关说明
## （一）服务合并遇到的问题
单个服务以jar的形式，通过maven引入合并服务中。在单体服务中，feign接口通过http请求；服务合并后，feign接口通过内部进程的方式通信。
### 1、多数据源冲突
```
1. 定义单数据源properties对象SingleDataSourceProperties，多数据源配置数据以Map<String, SingleDataSourceProperties>的形式从yml文件中读取；
2. 手动（通过new方式）构建所有需要的bean对象；
3. 手动将bean注入到容器中。
```
**多数据源配置示例：**
```
smart:
  data-sources:
	product:
	  url: jdbc:mysql://127.0.0.1:3306/demo_product?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=Asia/Shanghai
	  username: root
	  password: 123456
	  mapper-interface-location: com.xxx.demo.mall.product.mapper
	  mapper-xml-location: classpath*:com/xxx/demo/mall/product/mybatis/**.xml
	order:
	  url: jdbc:mysql://127.0.0.1:3306/demo_order?characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&serverTimezone=Asia/Shanghai
	  username: root
	  password: 123456
	  mapper-interface-location: com.xxx.demo.mall.order.mapper
	  mapper-xml-location: classpath*:com/xxx/demo/mall/order/mybatis/**.xml
```
### 2、rpc与rpc实现类冲突
```
自定义条件注解封装FeignClient。使其在单体服务时，rpc走feign；在合体服务时，rpc走内部进程通信。
```
### 3、yaml文件的自动加载
```
自定义注解YamlScan，用来加载配置的yaml文件（支持正则匹配）。通过SPI机制，在spring.factories文件中添加EnvironmentPostProcessor的实现类，通过其方法参数SpringApplication获取启动类的信息，从而获取YamlScan注解配置的yaml文件信息。然后将yaml文件加到ConfigurableEnvironment中。
```
### 4、启动类注解冲突
```
自定义条件注解SmartSpringCloudApplicationCondition，只会让启动类标记的启动注解生效。
```
### 5、maven打包异常
```
合体服务打包时，单体服务依赖的包也打进单体服务jar。通过maven profiles解决
```
## （二）日志数据脱敏
```
1.从日志侧切入，自定义标签，打印日志时进行脱敏处理；
2.自定义jackson的序列化器；打印日志时，采用自定义的序列化器；
3.通过反射获取log传入参数的MaskRule注解信息，最终根据注解规则进行字符串的截取与替换。
```

## （三）接口mock数据
接口通过切面拦截的方式，通过反射可以获取返回对象的所有信息，然后根据对象的属性类型，可以随机生成数据；对于特定要求的数据，可以制定mock规则，生成指定格式的数据。

## （四）测试

### 1、单元测试
利用单元测试，提高测试覆盖率。

### 2、集成测试
```
在集成测试下，关闭eureka，减少依赖。
依赖的服务rpc接口，通过mockito走挡板。
通过事务回滚，还原Test case对DB的修改。
```
### 3、系统测试

## （五）接口文档
### 1、接口文档由以下步骤自动生成：
通过[idea yapi upload plugin](https://github.com/smart-cloud/yapi_upload)插件，上传到[yapi server](https://github.com/YMFE/yapi)

### 2、接口文档效果图
#### 概要
![](docs/images/yapi_docs.png)

#### 详情
![](docs/images/yapi_docs_detail.png)