**_SpringBoot + SpringMVC + Mybatis + Swagger2 + RocketMq + RESTFulAPI_**


`统一异常处理`
exception实现了自定义统一异常处理,返回类型为json。
code 为异常编码
message 为异常消息内容
url 为请求的URL
data 为请求返回的Error数据

`统一返回类型`
工具包utils中的MessageVo为统一返回类型处理类,返回类型为json。
返回类型主要包括三个属性 code, info, data。
code 为处理结果对应编码, 
info 为处理结果对应信息, 
data 为接口返回数据。

`Swagger2`
访问路径:http://localhost:8080/swagger-ui.html

`Banner`
启动Spring Boot项目的时候，控制台会默认输出启动图案
1.在src/main/resources下新建一个banner.txt文档 
2.通过http://patorjk.com/software/taag网站生成需要的字符，将字符拷贝到步骤1所创建的txt文档中

`Shiro`
Shiro实现了不同用户登录的权限控制，包含了三个核心组件：Subject, SecurityManager 和 Realms。
Subject代表了当前用户的安全操作。
SecurityManager则管理所有用户的安全操作。它是Shiro框架的核心，Shiro通过SecurityManager来管理内部组件实例，并通过它来提供安全管理的各种服务。
Realm充当了Shiro与应用安全数据间的“桥梁”或者“连接器”。当对用户执行认证（登录）和授权（访问控制）验证时，Shiro会从应用配置的Realm中查找用户及其权限信息。

`RocketMq`
MQConsumerConfiguration类为消费者类，暂时只实现了一种消费方式(有序)
MQProducerConfiguration类为生产者类，生产者有多种方式，着重研究事务消息(多用于跨系统转账交易)。
