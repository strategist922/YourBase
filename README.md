## YourBase是什么?

* 是一个可用于HBase的分布式SQL引擎

* 是对[H2关系数据库](http://www.h2database.com/html/main.html)的改进和扩展

* 是一个100%纯Java的、将BigTable和RDBMS融合的数据库


## 有哪些应用场景?

* 使用YourBase的分布式SQL引擎，可使用类似MySQL的SQL语法和标准JDBC API读写HBase中的数据，
  支持各种DDL，支持触发器、自定义函数、视图、Join、子查询、Order By、Group By、聚合。

* 对于Client/Server架构的传统单机RDBMS的场景，也可使用YourBase。

* 如果应用想不经过网络直接读写数据库，可使用嵌入式YourBase。


## 运行需要

* HBase 0.94.2 或更高 (0.96未测试)
* JDK 6 或更高 (JDK 7未测试)


## 构建需要

* HBase 0.94.2 或更高 (0.96未测试)
* JDK 6 或更高 (JDK 7未测试)
* Maven 2或更高


## 安装配置

* [YourBase安装配置](https://github.com/codefollower/YourBase/wiki/YourBase%E5%AE%89%E8%A3%85%E9%85%8D%E7%BD%AE)


## 快速入门
* [YourBase快速入门](https://github.com/codefollower/YourBase/wiki/YourBase%E5%BF%AB%E9%80%9F%E5%85%A5%E9%97%A8)


## 开发测试环境搭建

* [YourBase开发测试环境搭建](https://github.com/codefollower/YourBase/wiki/YourBase%E5%BC%80%E5%8F%91%E6%B5%8B%E8%AF%95%E7%8E%AF%E5%A2%83%E6%90%AD%E5%BB%BA)


## 使用文档

* [YourBase使用文档](https://github.com/codefollower/YourBase/wiki/YourBase%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3)


## 开发文档

* [YourBase开发文档](https://github.com/codefollower/YourBase/wiki/YourBase%E5%BC%80%E5%8F%91%E6%96%87%E6%A1%A3)


## Roadmap

* 实现分布式事务

* 实现全局分布式索引

* Join、子查询性能优化

* OLAP存储引擎


## Package

mvn clean package assembly:assembly -Dmaven.test.skip=true

