<p align="center">
  <h1 align="center">☁️ Smart Cloud</h1>
  <p align="center">
    <strong>一站式 Spring Cloud 微服务脚手架 —— 让微服务开发像搭积木一样简单</strong>
  </p>
  <p align="center">
    开箱即用 · 可拆可合 · 安全增强 · 全链路可观测
  </p>
</p>

---

<p align="center">
  <a href="https://github.com/smart-cloud/smart-cloud/actions/workflows/build.yml"><img src="https://github.com/smart-cloud/smart-cloud/actions/workflows/build.yml/badge.svg?branch=dev" alt="build"></a>
  <a href="https://codecov.io/gh/smart-cloud/smart-cloud"><img src="https://codecov.io/gh/smart-cloud/smart-cloud/branch/dev/graph/badge.svg" alt="codecov"></a>
  <a href="https://github.com/smart-cloud/smart-cloud/blob/dev/LICENSE"><img src="https://img.shields.io/badge/license-Apache%202-green" alt="license"></a>
  <a href="https://mvnrepository.com/artifact/io.github.smart-cloud/smart-cloud"><img src="https://maven-badges.herokuapp.com/maven-central/io.github.smart-cloud/smart-cloud/badge.svg" alt="Maven Central"></a>
  <a href="https://github.com/smart-cloud/smart-cloud"><img src="https://img.shields.io/github/stars/smart-cloud/smart-cloud?style=social" alt="GitHub stars"></a>
  <a href="https://github.com/smart-cloud/smart-cloud/blob/dev/README_EN.md"><img src="https://img.shields.io/badge/document-English-blue.svg" alt="EN doc"></a>
  <a href="https://github.com/smart-cloud/smart-cloud#readme"><img src="https://img.shields.io/badge/文档-中文版-blue.svg" alt="CN doc"></a>
</p>

---

## 🌟 为什么选择 Smart Cloud？

| 痛点 | Smart Cloud 方案 |
|------|-----------------|
| 微服务拆分早成本高、拆分晚性能扛不住 | 服务**可拆可合**：前期按业务域合并部署，节约机器成本；业务增长后拆开部署，应对高 QPS。合并走进程内调用，拆分走 Feign RPC，一行 pom 切换 |
| 接口文档维护成本高 | **YAPI 自动生成**接口文档，IDEA 插件一键上传 |
| 前后端联调效率低 | 内置 **Mock 数据引擎**，按类型/规则自动生成响应数据 |
| 敏感数据泄露风险 | **全链路脱敏**：日志脱敏 + 配置加密 + 表字段加解密（支持多密钥） |
| 线上问题发现晚 | **实时监控告警**：慢接口 / 异常接口 / 服务离线，企业微信即时通知 |
| 基础设施接入繁琐 | **20+ Starter 组件**按需引入，像搭积木一样组装能力 |

> 📦 示例工程：[smart-cloud-examples](https://github.com/smart-cloud/smart-cloud-examples) —— 开箱即跑，5 分钟上手

---

## ✨ 核心特性一览

### 🚀 开发提效
- **接口文档自动生成** — IDEA YAPI 插件一键上传到 YAPI Server
- **Mock 数据引擎** — 按返回类型随机生成数据，支持自定义 Mock 规则
- **代码自动生成** — 业务无关代码（VO/DTO/Mapper 等）一键生成
- **接口多版本控制** — 轻松管理 API 多版本共存

### 🔒 安全增强
- **接口加解密 & 签名** — 请求/响应自动加解密，防篡改签名校验
- **敏感配置加密** — 基于 Jasypt 实现配置文件加密
- **表字段加解密** — 隐私字段自动加解密，支持多密钥管理
- **日志脱敏** — 自定义注解驱动，日志输出自动脱敏敏感信息

### 🏗️ 架构弹性
- **服务可拆可合** — 前期按业务域合并部署节约成本，业务增长后拆分部署应对高并发。合并走进程内通信，拆分走 RPC 通信，灵活切换
- **多数据源 & 分库分表** — MyBatis-Plus + Dynamic-DataSource + ShardingSphere
- **接口限流 & 熔断降级** — Sentinel 集成，支持配置中心动态刷新
- **MQ 消费失败重试** — 自定义注解实现延迟队列重新消费

### 📊 可观测性
- **服务监控** — Spring Boot Admin 监控（状态/GC/线程/内存/CPU/Tomcat）
- **接口监控** — 慢接口、异常接口实时监控 + 连续失败熔断告警
- **企业微信告警** — 服务离线/健康检查失败即时通知，支持 @提醒人
- **全链路日志** — Web/Feign/Method/MyBatis/MQ 日志，级别可配置
- **链路追踪** — HTTP 请求链路出入参 & 耗时打印

### 🧪 测试友好
- **单体无依赖测试** — 关闭 Nacos/Sentinel，Mock RPC 接口，独立开发测试
- **集成测试套件** — H2 数据库 + Embedded Redis + RabbitMQ Mock
- **单元测试封装** — Mockito + Podam 数据 Mock，提高覆盖率

---

## 🚀 快速开始

### 1. 引入依赖

```xml
<parent>
    <groupId>io.github.smart-cloud</groupId>
    <artifactId>smart-cloud</artifactId>
    <version>${latest-version}</version>
</parent>
```

### 2. 按需引入 Starter

```xml
<!-- Web 服务 -->
<dependency>
    <groupId>io.github.smart-cloud</groupId>
    <artifactId>smart-cloud-starter-web</artifactId>
</dependency>

<!-- MyBatis-Plus ORM -->
<dependency>
    <groupId>io.github.smart-cloud</groupId>
    <artifactId>smart-cloud-starter-mybatis-plus</artifactId>
</dependency>

<!-- Redis 缓存 & 分布式锁 -->
<dependency>
    <groupId>io.github.smart-cloud</groupId>
    <artifactId>smart-cloud-starter-redis</artifactId>
</dependency>
```

### 3. 查看完整示例

👉 前往 [smart-cloud-examples](https://github.com/smart-cloud/smart-cloud-examples) 获取开箱即用的完整示例工程。

---

## 🧩 模块结构

```
smart-cloud
├── smart-api-core                          # 接口权限、加解密、签名注解 & 用户上下文
│   ├── smart-api-annotation                #   接口权限、加解密、签名等注解
│   └── smart-user-context                  #   用户上下文
├── smart-code-generate                     # 代码生成工具
├── smart-common-pojo                       # 公共对象（VO、DTO、BO、DO）
├── smart-common-web                        # Web 模块公共处理
├── smart-constants                         # 常量模块
├── smart-exception                         # 异常模块
├── smart-design-pattern-utility            # 设计模式工具
├── smart-utility                           # 通用工具类
├── smart-test                              # 测试封装
│   ├── smart-cloud-starter-test            #   测试基础封装
│   ├── smart-cloud-starter-test-mock-rabbitmq  # RabbitMQ Mock
│   ├── smart-cloud-starter-test-mock-redis     # Redis Mock
│   └── smart-cloud-test-core               #   单元测试 / 集成测试封装
└── smart-cloud-starter                     # 🧱 Starter 组件库
    ├── smart-cloud-starter-api-version     #   接口多版本控制
    ├── smart-cloud-starter-configure       #   框架配置属性封装
    ├── smart-cloud-starter-core            #   核心（注解、异常、请求响应公共参数）
    ├── smart-cloud-starter-elasticsearch-dynamic-datasource  # ES 动态数据源
    ├── smart-cloud-starter-feign           #   Feign 封装（可拆可合、切面处理）
    ├── smart-cloud-starter-global-id       #   分布式 ID 生成器
    ├── smart-cloud-starter-job             #   定时任务（XXL-Job）
    ├── smart-cloud-starter-locale          #   国际化
    ├── smart-cloud-starter-log4j2          #   Log4j2 日志（支持脱敏）
    ├── smart-cloud-starter-log-mask        #   日志脱敏
    ├── smart-cloud-starter-logback         #   Logback 日志（支持脱敏）
    ├── smart-cloud-starter-method-log      #   方法切面日志
    ├── smart-cloud-starter-mock            #   Mock（工具类、策略、拦截器）
    ├── smart-cloud-starter-mp-shardingjdbc #   MyBatis-Plus + 分库分表
    ├── smart-cloud-starter-mybatis-plus    #   MyBatis-Plus + 多数据源
    ├── smart-cloud-starter-rabbitmq        #   RabbitMQ（消费失败自动重试）
    ├── smart-cloud-starter-rate-limit      #   接口限流（Sentinel）
    ├── smart-cloud-starter-redis           #   Redis + 分布式锁
    ├── smart-cloud-starter-static-discovery    # 静态服务发现
    ├── smart-cloud-starter-trace-debug     #   链路调试（出入参 + 耗时）
    ├── smart-cloud-starter-web             #   Web 封装（日志切面、异常处理、参数校验）
    └── smart-cloud-starter-monitor         #   📊 监控组件
        ├── smart-cloud-monitor-common      #     监控公共代码
        ├── smart-cloud-starter-monitor-api #     慢接口 / 异常接口监控
        └── smart-cloud-starter-monitor-admin   # Spring Boot Admin + 企业微信告警
```

---

## 🛠️ 技术栈

| 分类 | 技术 | 用途 |
|------|------|------|
| **基础框架** | [Spring Boot](https://spring.io/projects/spring-boot/) | 脚手架 |
| | [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway) | 服务网关 |
| | [Nacos](https://nacos.io/zh-cn/docs/what-is-nacos.html) | 服务注册 & 配置中心 |
| **服务调用** | [OpenFeign](https://spring.io/projects/spring-cloud-openfeign) | 声明式 RPC |
| | [Sentinel](https://github.com/alibaba/Sentinel) | 限流、熔断降级 |
| **数据层** | [MyBatis](http://www.mybatis.org/mybatis-3/zh/index.html) + [MyBatis-Plus](https://github.com/baomidou/mybatis-plus) | ORM |
| | [Dynamic-DataSource](https://mp.baomidou.com/guide/dynamic-datasource.html) | 多数据源 |
| | [ShardingSphere](https://github.com/apache/incubator-shardingsphere) | 分库分表 |
| | [Redis](https://redis.io/) | 缓存 & 分布式锁 |
| **消息队列** | [RabbitMQ](https://www.rabbitmq.com/) | 异步消息 |
| **监控运维** | [Spring Boot Admin](https://github.com/codecentric/spring-boot-admin) | 服务监控 |
| | [Sleuth](https://spring.io/projects/spring-cloud-sleuth) | 链路追踪 |
| **工具链** | [XXL-Job](https://github.com/xuxueli/xxl-job) | 分布式定时任务 |
| | [EasyExcel](https://github.com/alibaba/easyexcel) | Excel 导入导出 |
| | [FastDFS](https://github.com/happyfish100/fastdfs) | 分布式文件存储 |
| | [Jasypt](https://github.com/ulisesbocchio/jasypt-spring-boot) | 配置加密 |
| | [Lombok](https://www.projectlombok.org/) | 代码简化 |
| **测试** | [Mockito](https://site.mockito.org/) + [Podam](https://github.com/mtedone/podam) | 单元测试 & 数据 Mock |
| | [H2](http://www.h2database.com/html/tutorial.html) | 数据库集成测试 |
| | [Embedded Redis](https://github.com/ozimov/embedded-redis) | Redis 集成测试 |
| | [RabbitMQ Mock](https://github.com/fridujo/rabbitmq-mock) | MQ 集成测试 |
| **文档** | [YAPI](https://github.com/YMFE/yapi) + [IDEA 插件](https://github.com/smart-cloud/yapi_upload) | 接口文档自动生成 |
| **代码生成** | [FreeMarker](https://freemarker.apache.org/) | 模板引擎 |

---

## 🔄 服务合并原理

Smart Cloud 最具特色的能力：**服务可拆可合**。

> 新项目前期为了节约成本，可按业务域将多个服务合并部署；当业务发展起来后，用户量增大、QPS 变高，可将服务拆开独立部署，实现弹性伸缩。

![](docs/images/service_merge.png)

- **合并部署**：只需在 `pom.xml` 中引入待合并服务的依赖，服务间自动切换为进程内调用
- **拆分部署**：各服务独立部署，通过 Feign RPC 远程通信
- **自动切换**：通过 [SmartFeignClient](https://github.com/smart-cloud/smart-cloud/blob/dev/smart-cloud-starter/smart-cloud-starter-feign/src/main/java/org/smartframework/cloud/starter/rpc/feign/annotation/SmartFeignClient.java) 注解 + [条件判断](https://github.com/smart-cloud/smart-cloud/blob/463cc09b6c2f8a0b947f0a2fcc157ee037ba419d/smart-cloud-starter/smart-cloud-starter-feign/src/main/java/org/smartframework/cloud/starter/rpc/feign/condition/SmartFeignClientCondition.java#L32)，无需修改任何业务代码

### 合并部署解决的关键问题

| 问题 | 解决方案 |
|------|---------|
| Bean 名称冲突 | 自定义 Bean 名称生成规则 |
| RPC 接口与实现类冲突 | `SmartFeignClient` 条件注解，合并时走进程内调用 |
| YAML 配置自动加载 | `@YamlScan` 注解 + `EnvironmentPostProcessor` SPI 机制 |
| 启动类注解冲突 | `SmartSpringCloudApplicationCondition` 条件注解 |
| Maven 打包异常 | Maven Profiles 控制依赖打包范围 |

---

## 📊 服务监控 & 告警

通过 Spring Boot Admin 监控服务状态，扩展 Actuator 接口监听接口异常情况，并通过企业微信通知。

- **服务离线**：查询 GitLab 作业记录，若最近半小时（可配置）无 Tag 作业，则发送通知并 @提醒人
- **接口异常**：滑动窗口统计接口失败率，超过阈值则健康检查失败
- **连续失败熔断**：接口连续失败触发熔断告警

| 服务离线通知 | 健康检查失败 | 服务上线 |
|:---:|:---:|:---:|
| ![](docs/images/monitor/off_line.png) | ![](docs/images/monitor/down.png) | ![](docs/images/monitor/up.png) |

---

## 🔒 日志脱敏

```
1. 自定义脱敏标签，打印日志时自动触发脱敏处理
2. 自定义 Jackson 序列化器，日志输出使用专用序列化器
3. 反射读取 @MaskRule 注解规则，按规则进行字符串截取与替换
```

---

## 📄 接口文档

通过 [IDEA YAPI Upload Plugin](https://github.com/smart-cloud/yapi_upload) 一键上传到 [YAPI Server](https://github.com/YMFE/yapi)。

| 文档概览 | 文档详情 |
|:---:|:---:|
| ![](docs/images/yapi_docs.png) | ![](docs/images/yapi_docs_detail.png) |

---

## 📖 错误码参考

| 模块 | Code | 说明 |
|------|:----:|------|
| smart-constants | 200 | 成功 |
| | 101 | 校验失败 |
| | 102 | 数据不存在 |
| | 103 | 数据已存在 |
| | 400 | 签名错误 |
| | 401 | 无权限访问 |
| | 404 | 请求 URL 错误 |
| | 408 | 请求超时 |
| | 409 | 重复提交 |
| | 412 | 参数不全 |
| | 413 | 属性未配置 |
| | 415 | 请求方式不支持 |
| | 416 | 请求类型不支持 |
| | 417 | 获取锁失败 |
| | 418 | 上传文件大小超限 |
| | 419 | 会话已失效，请重新登录 |
| | 420 | 请求入参 JSON 解析异常 |
| | 421 | 接口访问太频繁 |
| | 500 | 服务器异常 |
| | 501 | 获取 Request 失败 |
| | 502 | 获取 Response 失败 |
| | 503 | RPC 请求失败 |
| smart-cloud-starter-web | 2001 | 待校验参数不能为 null |
| smart-cloud-starter-elasticsearch-dynamic-datasource | 3001 | ES DS Key 不存在 |
| | 3002 | ES 数据源未找到 |
| | 3003 | ES 动态数据源属性未配置 |

---

## 🤝 参与贡献

欢迎 Star ⭐ 、Fork 🍴 、PR 🔧 ！

1. Fork 本仓库
2. 创建你的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交你的修改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 发起 Pull Request

---

## ⭐ Star History

[![Star History Chart](https://star-history.dera.page/svg?repos=smart-cloud/smart-cloud&type=Date)](https://star-history.dera.page/#smart-cloud/smart-cloud&Date)

---

## 📜 License

本项目基于 [Apache License 2.0](LICENSE) 开源协议。
