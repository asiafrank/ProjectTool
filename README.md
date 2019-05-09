# 用于生成 BO，DAO 类的工具

一般情况我们都使用 start.spring.io 来生成一个基础项目，然后再使用这个工具来生成 BO 和 DAO 层。
仅支持 MySQL

## config.properties 说明

配置名 | 描述
------------ | -------------
mysql.url | MySQL 连接 URL
mysql.username | MySQL 用户名
mysql.password | MySQL 密码
mysql.tables | 需要生成对应 BO，DAO 的表, 英文逗号分割。
project.path  | 项目路径，如 `C://x/y/z`，那么文件的生成都基于项目路径
java.src.relativePath | java 源码的相对路径，默认是 `/src/main/java`。如果是 maven 项目，则该值不必改动
java.resources.relativePath | java 资源文件夹的相对路径，默认是 `/src/main/resources`
base.module | base 基础类生成所在的模块名，如果存在则代表，生成的 base 基础类会在 `${project.path}/${base.module}/${java.src.relativePath}` 处
base.package | base 基础类生成之后所在的包
bo.module  | bo 类生成所在的模块名
bo.package | bo 接口类生成之后所在的包
bo.clazz.suffix | bo 接口类名称后缀，默认是 BO
bo.impl.package | bo 实现类生成之后所在的包, 默认为 `${bo.package}.impl`
bo.impl.clazz.suffix | bo 实现类名称后缀，默认为 `Impl`
dao.module  | dao 类生成所在的模块名
dao.package | dao 接口类生成之后所在的包
dao.clazz.suffix | dao 接口类名称后缀，默认是 BO
dao.impl.package | dao 实现类生成之后所在的包, 默认为 `${dao.package}.impl`
dao.impl.clazz.suffix | dao 实现类名称后缀，默认为 `Impl`
model.module | model 类生成所在的模块名
model.package | model 类生成所在的包
vo.module | vo 类生成所在的模块名
vo.package | vo 类生成所在的包
mybatis.mapper.module | mapper xml 文件生成所在的模块名
mybatis.mapper.dir | mapper xml 文件生成所在的文件夹名


