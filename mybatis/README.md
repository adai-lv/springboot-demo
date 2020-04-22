# Spring Boot 项目整合 MyBatis 连接数据库

## MyBatis 简介

`MyBatis` 是一款优秀的持久层框架，它支持定制化 `SQL`、存储过程以及高级映射。
它避免了几乎所有的 `JDBC` 代码和手动设置参数以及获取结果集。
因为 `MyBatis` 可以使用简单的 `XML` 或注解来配置和映射原生信息，将接口和 `Java` 的 `POJOs` (Plain Old Java Objects，普通的 `Java` 对象)映射成数据库中的记录。

通俗地说，`MyBatis` 就是使用 `Java` 程序操作数据库时的一种工具，可以简化使用 `JDBC` 时的很多操作，而且还简化了数据库记录与 `POJO` 之间的映射方式。

## 准备工作

本文将使用开源的数据库连接池 `DBCP`（DataBase Connection Pool）连接 `MySQL` 数据库，并在此基础上讲解如何使用 `MyBatis` 操作数据库。
所以在开始本教程的阅读之前，需要如下准备：

1. 一个 Spring Boot 的 Web 项目，你可以通过 [Spring Initializr](https://start.spring.io/) 页面生成一个空的 `Spring Boot` 项目。
2. 安装 `MySQL` 数据库或者一台 `MySQL` 服务器。
3. 安装 IDEA 或 使用自己顺手的 IDE。

通过 `IDEA` 打开 `Spring Initializr` 页面生成的 `Spring Boot` 项目，删除 `src` 目录，创建一个新 `Module`，名称为 `mybatis`。

项目工程结构如下：

```
├── HELP.md
├── mvnw
├── mvnw.cmd
├── mybatis
│   ├── mybatis.iml
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   └── resources
│       └── test
│           └── java
├── pom.xml
└── springboot-demo.iml
```

## 集成 DBCP

`DBCP` 数据库连接池是 `Apache` 上的一个 `Java` 连接池项目，也是 `Tomcat` 使用的连接池组件。

由于建立数据库连接是一种非常耗时、耗资源的行为，所以通过连接池预先同数据库建立一些连接，放在内存中，应用程序需要建立数据库连接时直接到连接池中申请一个就行，使用完毕后再归还到连接池中。

### 添加依赖

需要在 `mybatis` 模块下 `pom.xml` 中添加清单1的内容。

清单 1. 添加相关依赖

```
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>6.0.6</version>
    <scope>runtime</scope>
</dependency>
 
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-dbcp2</artifactId>
   <version>2.5.0</version>
< /dependency>
```

### 配置数据源

- 在 `mybatis` 模块的 `src/main/resources` 目录下，创建配置文件 `application.yml`，然后添加数据源相关的配置项的值，内容如清单2所示。

清单 2. 数据源配置文件配置项    
```
spring:
  datasource:
    dbcp2:
      url: jdbc:mysql://localhost:3306/springboot-demo?serverTimezone=GMT%2B8&characterEncoding=utf-8
      username: root
      password: 12345678
      driver-class-name: com.mysql.jdbc.Driver
      initial-size: 5
      min-idle: 2
      max-total: 20
      max-wait-millis: 60000
      time-between-eviction-runs-millis: 60000
      min-evictable-idle-time-millis: 300000
      validation-query: SELECT 1 FROM DUAL
      test-while-idle: true
      test-on-borrow: false
    type: org.apache.commons.dbcp2.BasicDataSource
```

- 在 `mybatis` 模块的 `src/main/java` 目录下，创建 `com.philcode.springboot.mybatis.config` 包，在 `config` 包下创建数据源的配置类 `DataSourceConfiguration`，内容如清单3所示。
 
清单 3. 数据源配置类   
```
package com.philcode.springboot.mybatis.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(name = "spring.datasource.dbcp2.url")
public class DataSourceConfiguration {

    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.dbcp2")
    public DataSource dataSource() {
        return new BasicDataSource();
    }
}
```

- 在 `mybatis` 模块的 `src/main/java` 目录下，`com.philcode.springboot.mybatis` 包，创建项目启动类 `MybatisApplication`，内容如清单4所示。
   
清单 4. 项目启动类
```
package com.philcode.springboot.mybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MybatisApplication {

   public static void main(String[] args) {
       SpringApplication.run(MybatisApplication.class, args);
   }
}
```

到这一步，如果能够正常启动项目就意味着我们的连接池配置成功了。

## 集成 MyBatis

### 添加依赖

需要在 `mybatis` 模块下 `pom.xml` 中添加清单5的内容。

清单 5. 添加相关依赖

```
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>
```

### 配置数据库事务和会话工厂

在 `mybatis` 模块的 `com.philcode.springboot.mybatis.config` 包，数据源的配置类 `DataSourceConfiguration`，新增如清单6的内容。
 
清单 6. 配置数据库事务和会话工厂   
```
package com.philcode.springboot.mybatis.config;

......

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

......

@Bean(name = "transactionManager")
public DataSourceTransactionManager dbOneTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
}

@Bean(name = "sqlSessionFactory")
@ConditionalOnMissingBean(name = "sqlSessionFactory")
public SqlSessionFactory dbOneSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
    final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);

    return sessionFactory.getObject();
}
```

### 配置 Mapper 路径

1. 首先指定 *Mapper.java 的扫描路径（即存放 *Mapper.java 的包地址）。我们可以通过在 `DataSourceConfiguration` 类或者 `MybatisApplication` 类上添加 `@MapperScan` 注解来指定扫描路径，如清单7所示。

清单 7. 配置 *Mapper.java 扫描路径

```
@MapperScan(value = { "com.philcode.springboot.mybatis.mapper" }, sqlSessionFactoryRef = "sqlSessionFactory")
public class DataSourceConfiguration {

......

}
```

2. 通过会话工厂指定指定 `*Mapper.xml` 的路径。修改 `SqlSessionFactory` 的 `Bean` 创建方法，如清单8所示。

清单 8. 配置 *Mapper.xml 扫描路径

```
package com.philcode.springboot.mybatis.config;

......

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

......

@Bean(name = "sqlSessionFactory")
@ConditionalOnMissingBean(name = "sqlSessionFactory")
public SqlSessionFactory dbOneSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)throws Exception {
   final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
   sessionFactory.setDataSource(dataSource);

   sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/*Mapper.xml"));

   return sessionFactory.getObject();
}
```

至此，我们的 MyBatis 和 Spring Boot 就整合完成了。

## 基于 XML 方式使用 MyBatis

### 准备工作

我们将通过一个具体的场景来讲解如何使用 `MyBatis` 进行数据库操作：定义一个学生实体和班级实体，并创建对应的数据库表，然后使用 `MyBatis` 对其进行增、删、改、查操作。

- 数据库表结构，如清单9如示；
- 账号类的全路径为 `com.philcode.springboot.mybatis.model.entity.Account`，如清单10如示；
- 用户档案类的全路径为 `com.philcode.springboot.mybatis.model.entity.UserProfile`，如清单11如示；

清单 9. 数据库表结构
```sql
-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '代理键',
  `username` varchar(45) NOT NULL COMMENT '用户名，英文+数字的组合',
  `password` varchar(128) NOT NULL DEFAULT '' COMMENT '密码，加密储存，不可解密',
  `nickname` varchar(45) NOT NULL DEFAULT '' COMMENT '昵称',
  `icon` varchar(255) NOT NULL DEFAULT '' COMMENT '图标',
  `status` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '状态，0正常，1未激活，2已锁定，3永久删除',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uiq_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户账号信息';

-- ----------------------------
-- Table structure for user_profile
-- ----------------------------
DROP TABLE IF EXISTS `user_profile`;
CREATE TABLE `user_profile` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '代理键',
  `account_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '账号ID',
  `realname` varchar(45) NOT NULL DEFAULT '' COMMENT '真实姓名',
  `birtyday` date NULL COMMENT '生日，格式：yyyy-mm-dd',
  `gender` char(1) NOT NULL DEFAULT 'M' COMMENT '性别，M男性，F女性',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uiq_account_id` (`account_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户档案';
```

清单 10. Account 类
```
package com.philcode.springboot.mybatis.model.entity;

public class Account {

    private Long id;

    private String username;

    private String password;

    private String nickname;

    private String icon;

    private Integer status;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    # setter and getter
    ......
}
```

清单 11. UserProfile 类
```
package com.philcode.springboot.mybatis.model.entity;

public class UserProfile {

    private Long id;

    private Long accountId;

    private String realname;

    private String birtyday;

    private String gender;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    # setter and getter
    ......
}
```

### 创建 Mapper 类及 XML 文件

创建好对应的实体类后，还需要为其创建对应的 `*Mapper.java` 和 `*Mapper.xml` 文件，需要注意的是这两类文件需要放到前面配置数据源时指定的路径下。

在本例中 `*Mapper.java` 文件是放在 `com.philcode.springboot.mybatis.mapper` 包下，而 `*Mapper.xml` 则是放在 `resources/mapper` 目录下。

#### 创建 AccountMapper.java 文件

清单 12. AccountMapper.java 接口

```
package com.philcode.springboot.mybatis.mapper;

import com.philcode.springboot.mybatis.model.entity.Account;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountMapper {
    int insert(Account account);
    int updateIgnoreNullById(@Param("student") Account account);
    int deleteById(@Param("id") int id);
    Account findById(@Param("id") int id);
    List<Account> findAll();
    AccountExtend findWithUserProfileById(@Param("id") int id);
}
```

在 `AccountMapper.java` 类中定义了新增，更新（忽略空值），删除，查询六个方法，覆盖了简单的 `CURD` （Create、Update、Retrieve、Delete）操作，
但是这只是方法的定义，那么具体实现呢？与平常所接触到的 `interface` 不一样的是，它的实现不是一个具体的 `Java` 类，而是一个与之对应的 `mapper.xml` 文件，
也就是接下来要看到的 `AccountMapper.xml` 文件。`AccountMapper.java` 类中定义的接口会对应 `AccountMapper.xml` 中的一段 SQL 语句。

#### 创建 AccountMapper.xml 文件

创建一个与 `AccountMapper.java` 类对应的 XML 文件，里面定义了 `AccountMapper.java` 类中定义的六个方法的 SQL 实现，
由于篇幅的原因，完整的 XML 内容可以查看 `mybatis/src/main/resources/mapper/AccountMapper.xml`

```
<mapper namespace="com.philcode.springboot.mybatis.mapper.AccountMapper">

    <resultMap id="BaseResultMap" type="com.philcode.springboot.mybatis.model.entity.Account">
        <id column="id" jdbcType="BIGINT" property="id" />
        <result column="username" jdbcType="VARCHAR" property="username" />
        <result column="password" jdbcType="VARCHAR" property="password" />
        <result column="nickname" jdbcType="VARCHAR" property="nickname" />
        <result column="icon" jdbcType="VARCHAR" property="icon" />
        <result column="status" jdbcType="TINYINT" property="status" />
        <result column="created_at" jdbcType="TIMESTAMP" property="createdAt" />
        <result column="updated_at" jdbcType="TIMESTAMP" property="updatedAt" />
    </resultMap>

    <insert id="insert" parameterType="com.philcode.springboot.mybatis.model.entity.Account" useGeneratedKeys="true" keyProperty="id">
        insert into account(`username`, `password`, `nickname`, `icon`, `status`, `created_at`, `updated_at`)
        values (#{username}, #{password}, #{nickname}, #{icon}, #{status}, #{createdAt}, #{updatedAt})
    </insert>
</mapper>
```

其中 `namespace` 指定了该 `XML` 文件对应的 `java` 类。除了六个方法的定义外，还有一个 `resultMap` 的标签，这个其实定义的是 `sql` 查询的字段与实体类之间的映射关系。
在 `insert` 方法中，使用了 `useGeneratedKeys` 和 `keyProperty` 两个属性，
这两个属性的作用主要是将插入后数据的 `id`，赋值到传进来的实体对象的某个字段，`keyProperty` 就是指定那个字段的名称。

#### 创建 UserProfileMapper.java 文件

清单 14. UserProfileMapper.java 文件

```
package com.philcode.springboot.mybatis.mapper;

import com.philcode.springboot.mybatis.model.entity.UserProfile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserProfileMapper {

    int insert(UserProfile userProfile);
    int updateIgnoreNullById(@Param("userProfile") UserProfile userProfile);
    int deleteById(@Param("id") int id);
    UserProfile findById(@Param("id") int id);
    List<UserProfile> findAll();
}
```

#### 创建 UserProfileMapper.xml 文件

创建一个与 `UserProfileMapper.java` 类对应的 XML 文件，里面定义了 `UserProfileMapper.java` 类中定义的 5 个方法的 SQL 实现，
由于篇幅的原因，完整的 XML 内容可以查看 `mybatis/src/main/resources/mapper/UserProfileMapper.xml`