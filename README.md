# AutoTable

AutoTable是一个用于MyBatis自动建表的库，它允许开发人员使用注解来定义数据表结构，并能够根据这些注解自动创建相应的数据库表。兼容了MyBatis-Plus框架的注解，能够方便地在项目中自动建表。

### 使用方式

* 导入依赖

```xml
<dependency>
    <groupId>io.github.origami1203</groupId>
    <artifactId>auto-table</artifactId>
    <version>最新版本</version>
</dependency>
```

* 使用注解

```java
// @TableName(value = "sys_user")
@Table(value = "sys_user", comment = "系统用户表")
public class SysUserDO extends BaseEntity {
    
    // @TableId
    @Column(primary = true,comment = "主键")
    private Long id;
    
    // @TableField(value = "name")
    @Column(name = "name", nullable = false, length = 255)
    private String username;
    
    @Column(name = "sex", defaultValue = "1")
    private Byte sex;
    
    private String password;
    
    @Column(jdbcType = JdbcType.CHAR, length = 11)
    private String phone;
    
    // @TableField(exist = false)
    @Column(ignore = true)
    private String fullName;
    
    // get set
}
```

* 配置文件

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: xxxx
    username: root
    password: xxx
    
table:
  auto:
    # 数据库类型，目前只支持mysql
    database: mysql
    # 实体类的package路径
    entity-package: org.origami.system.entity
    # 类似hibernate，目前只支持 none,create,update,validate
    # none: 不做任何操作
    # create： 没有表则创建，有则不做任何操作
    # update：没有表新建表，有新增字段，添加字段，不会删除字段
    # validate：不做任何操作，当数据库与实体类不符时，抛出异常
    ddl-auto: update
    # 建表时的自定义操作
    custom:
      mysql:
        engine: innodb
        charset: utf8mb4
```

最后生成的数据库表

```sql
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(255) NOT NULL,
  `birthday` date DEFAULT NULL,
  `sex` tinyint DEFAULT 1,
  `phone` char(11) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '系统用户表';
