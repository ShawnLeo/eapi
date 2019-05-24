spring boot 集成自动生成代码
1:添加配置pom.xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.0.1</version>
</dependency>

2:配置application.yml
mybatis:
    config-location:  classpath:/${targetPackage?replace('.','/')}/dao/mybatis-config.xml
    mapper-locations: classpath:/${targetPackage?replace('.','/')}/dao/xml/*.xml
或
application.properties
mybatis.config-location=classpath:/${targetPackage?replace('.','/')}/dao/mybatis-config.xml
mybatis.mapper-locations=classpath:/${targetPackage?replace('.','/')}/dao/xml/*.xml