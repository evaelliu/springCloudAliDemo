github 仓库地址：https://github.com/evaelliu/springCloudAliDemo.git

nacos GitHub仓库：https://github.com/alibaba/nacos.git

nacos dockerhub仓库：https://registry.hub.docker.com/r/nacos/nacos-server

demo包括内容

    nacos作为spring cloud 注册中心使用（可替代eureka）
    nacos作为dubbo 注册中心使用（可替代zookeeper）
    nacos作为配置中心使用（可替代apollo）
    附：spring cloud gateway 与 nacos集成；spring cloud gateway与knife4j(swagger)继承实现api文档聚合

0.nacos安装部署

    1.官网下载安装，略
    2.docker安装，进入项目docker-compose目录，执行docker-compose -f docker-compose-nacos.yml up -d启动

    验证：http://localhost:8848/nacos nacos/nacos可正常登录即部署完成

1.nacos作为spring cloud 注册中心使用

添加依赖

        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-nacos-discovery</artifactId>
            <version>2.2.0.RELEASE</version>
        </dependency>

配置文件 bootstrap.yml 添加配置

    spring:
        cloud:
            nacos:
                discovery:
                    server-addr: ${REGISTER_HOST:nacos}:${REGISTER_PORT:8848}
                    namespace: dev

然后配置完成

验证：启动集成服务，打开nacos管理界面查看

2.nacos作为dubbo注册中心使用

基于原有dubbo服务

添加依赖

        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-registry-nacos</artifactId>
            <version>2.7.5</version>
        </dependency>

修改配置

    dubbo.registry.address=nacos://nacos:8848

验证：启动集成服务，打开nacos管理界面查看

3. nacos作为配置中心使用
    
添加依赖 

        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-nacos-config</artifactId>
            <version>2.2.0.RELEASE</version>
        </dependency>

bootstrap.yml添加配置

    spring:
        cloud:
            nacos:
                config:
                    server-addr: ${REGISTER_HOST:nacos}:${REGISTER_PORT:8848}
                    namespace: dev
                    file-extension: yml

读取配置类添加@RefreshScope注解
验证：nacos管理界面添加 applicationname.yml配置，在服务内读取

附：gateway集成swagger,knife4j
添加依赖：

    <dependency>
        <groupId>com.github.xiaoymin</groupId>
        <artifactId>knife4j-spring-boot-starter</artifactId>
        <version>3.0.2</version>
    </dependency>
启动类添加 @EnableKnife4j
gateway模块config目录文件直接使用
启动gateway项目
本地访问 http://localhost:8088/doc.html(8088为gateway端口)
