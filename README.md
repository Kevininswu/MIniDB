# MiniDB

MiniDB 是一个 Java 实现的简单的数据库，部分原理参照自 MySQL、PostgreSQL 和 SQLite。实现了以下功能：

- 数据的可靠性和数据恢复
- 两段锁协议（2PL）实现可串行化调度
- MVCC
- 两种事务隔离级别（读提交和可重复读）
- 死锁处理
- 简单的表和字段管理
- 简单的SQL解析
- 基于 socket 的 server 和 client

#整体结构
整个项目分为后端和前端，前后端通过 socket 进行交互。前端读取用户输入，并发送到后端执行，输出返回结果，并等待下一次输入。M后端则需要解析 SQL，如果是合法的 SQL，就尝试执行并返回结果。不包括解析器，MYDB 的后端划分为五个模块，每个模块都又一定的职责，通过接口向其依赖的模块提供方法。五个模块如下：

- Transaction Manager（TM）
- Data Manager（DM）
- Version Manager（VM）
- Index Manager（IM）
- Table Manager（TBM）
![image](https://github.com/user-attachments/assets/96ac4e70-f27b-4c87-8cb6-f939aeee54ac)
每个模块的职责如下：

- TM 通过维护 XID 文件来维护事务的状态，并提供接口供其他模块来查询某个事务的状态。
- DM 直接管理数据库 DB 文件和日志文件。DM 的主要职责有：1) 分页管理 DB 文件，并进行缓存；2) 管理日志文件，保证在发生错误时可以根据日志进行恢复；3) 抽象 DB 文件为 DataItem 供上层模块使用，并提供缓存。
- VM 基于两段锁协议实现了调度序列的可串行化，并实现了 MVCC 以消除读写阻塞。同时实现了两种隔离级别。
- IM 实现了基于 B+ 树的索引，BTW，目前 where 只支持已索引字段。
- TBM 实现了对字段和表的管理。同时，解析 SQL 语句，并根据语句操作表。

## 运行方式

注意首先需要在 pom.xml 中调整编译版本，如果导入 IDE，请更改项目的编译版本以适应你的 JDK

首先执行以下命令编译源码：

```shell
mvn compile
```

接着执行以下命令以 /tmp/minidb 作为路径创建数据库：
无法创建中间路径 需要提前创建temp文件夹

```shell
mvn exec:java -D"exec.mainClass"="com.kevin.minidb.backend.Launcher" -D"exec.args"="-create D:\temp\minidb"
```

随后通过以下命令以默认参数启动数据库服务：

```shell
mvn exec:java -D"exec.mainClass"="com.kevin.minidb.backend.Launcher" -D"exec.args"="-open D:\temp\minidb"
```

这时数据库服务就已经启动在本机的 9999 端口。重新启动一个终端，执行以下命令启动客户端连接数据库：

```shell
mvn exec:java -D"exec.mainClass"="com.kevin.minidb.client.Launcher"
```

会启动一个交互式命令行，就可以在这里输入类 SQL 语法，回车会发送语句到服务，并输出执行的结果。

一个执行示例：

![img.png](img.png)
![img_1.png](img_1.png)
