# java-learning
Java 学习

使用性能最好的连接池 HikariCP

```
HikariCP将尝试仅通过驱动程序管理器来解析驱动程序jdbcUrl，但对于某些较旧的驱动程序，
driverClassName还必须指定。除非您收到明显的错误消息，指出未找到驱动程序，否则请忽略此属性。 默认值：无
```

read unCommit

由下面的日志我们可以看到事务 `ThreadPoolTaskExecutor-2` 返回 return code:1时 
数据库已经对数据进行了修改操作 但还没有`commmit`事务此时 事务1去查询该数据 查询到已经修改的脏数据

```
readUnCommit :{}start
readUnCommit :{}start
: MyHikariCP - Starting...
: MyHikariCP - Start completed.
: add method thread name:ThreadPoolTaskExecutor-2
: find user by id: User{id=14, name='chenshuang', age=12, money=39.0}, thread name:ThreadPoolTaskExecutor-2
: return code:1, thread:ThreadPoolTaskExecutor-2 

## 执行了update操作
: sleep time 3m , thread name:ThreadPoolTaskExecutor-2

: add method thread name:ThreadPoolTaskExecutor-1

## 已经读到 ThreadPoolTaskExecutor-2 修改的金额
: find user by id: User{id=14, name='chenshuang', age=12, money=40.0}, thread name:ThreadPoolTaskExecutor-1
: after user money find user by id: User{id=14, name='chenshuang', age=12, money=40.0} , thread name:ThreadPoolTaskExecutor-2
: return code:1, thread:ThreadPoolTaskExecutor-1 
: sleep time 3m , thread name:ThreadPoolTaskExecutor-1
: after user money find user by id: User{id=14, name='chenshuang', age=12, money=41.0} , thread name:ThreadPoolTaskExecutor-1
```


read commit

此时的readcommit 和 repeatable 表示的是一样的没有任何区别， 下面在来解释俩者的区别

```
readCommit :{}start
readCommit :{}start
: MyHikariCP - Starting...
: MyHikariCP - Start completed.
: add method thread name:ThreadPoolTaskExecutor-1
: find user by id: User{id=14, name='chenshuang', age=12, money=37.0}, thread name:ThreadPoolTaskExecutor-1
: return code:1, thread:ThreadPoolTaskExecutor-1 

## 执行了update操作 ThreadPoolTaskExecutor-2 
: sleep time 3m , thread name:ThreadPoolTaskExecutor-1

: add method thread name:ThreadPoolTaskExecutor-2

## 已经读到 ThreadPoolTaskExecutor-1 修改的金额 
: find user by id: User{id=14, name='chenshuang', age=12, money=37.0}, thread name:ThreadPoolTaskExecutor-2
: after user money find user by id: User{id=14, name='chenshuang', age=12, money=38.0} , thread name:ThreadPoolTaskExecutor-1
: return code:1, thread:ThreadPoolTaskExecutor-2 
: sleep time 3m , thread name:ThreadPoolTaskExecutor-2
: after user money find user by id: User{id=14, name='chenshuang', age=12, money=39.0} , thread name:ThreadPoolTaskExecutor-2
```


repeatable

```
repeatable :{}start
repeatable :{}start
: MyHikariCP - Starting...
: MyHikariCP - Start completed.
: add method thread name:ThreadPoolTaskExecutor-2
: find user by id: User{id=14, name='chenshuang', age=12, money=33.0}, thread name:ThreadPoolTaskExecutor-2

## 执行了update操作  ThreadPoolTaskExecutor-2 
: return code:1, thread:ThreadPoolTaskExecutor-2 
: sleep time 3m , thread name:ThreadPoolTaskExecutor-2
: add method thread name:ThreadPoolTaskExecutor-1
: find user by id: User{id=14, name='chenshuang', age=12, money=33.0}, thread name:ThreadPoolTaskExecutor-1
: after user money find user by id: User{id=14, name='chenshuang', age=12, money=34.0} , thread name:ThreadPoolTaskExecutor-2
: return code:1, thread:ThreadPoolTaskExecutor-1 
: sleep time 3m , thread name:ThreadPoolTaskExecutor-1
: after user money find user by id: User{id=14, name='chenshuang', age=12, money=35.0} , thread name:ThreadPoolTaskExecutor-1
```


serializable

不可串行化 ： 这里俩个事务同时查询某张表的时候是不可以同时进行的 相当于表锁，不能并行 


```
serializable :{}start
serializable :{}start
: MyHikariCP - Starting...
: MyHikariCP - Start completed.
: add method thread name:ThreadPoolTaskExecutor-2
: find user by id: User{id=14, name='chenshuang', age=12, money=35.0}, thread name:ThreadPoolTaskExecutor-2
: return code:1, thread:ThreadPoolTaskExecutor-2 
: sleep time 3m , thread name:ThreadPoolTaskExecutor-2


: add method thread name:ThreadPoolTaskExecutor-1
: after user money find user by id: User{id=14, name='chenshuang', age=12, money=36.0} , thread name:ThreadPoolTaskExecutor-2

: find user by id: User{id=14, name='chenshuang', age=12, money=36.0}, thread name:ThreadPoolTaskExecutor-1
: return code:1, thread:ThreadPoolTaskExecutor-1 
: sleep time 3m , thread name:ThreadPoolTaskExecutor-1
: after user money find user by id: User{id=14, name='chenshuang', age=12, money=37.0} , thread name:ThreadPoolTaskExecutor-1
```


现在我们来比较一下 read commit 和repeatable的区别

当一个事务连续查询一条数据， 另一个事务执行update操作并commit


repeatable   

```
: add method thread name:ThreadPoolTaskExecutor-1
: find user by id: User{id=14, name='chenshuang', age=12, money=74.0}, thread name:ThreadPoolTaskExecutor-1
: return code:1, thread:ThreadPoolTaskExecutor-2 
: after user money find user by id: User{id=14, name='chenshuang', age=12, money=75.0} , thread name:ThreadPoolTaskExecutor-2
: after user money find user by id: User{id=14, name='chenshuang', age=12, money=74.0} , thread name:ThreadPoolTaskExecutor-1
```


read commit 第一次读到的数据和第二次读到的数据不一致 感觉出现了幻觉一样的

```
: find user by id: User{id=14, name='chenshuang', age=12, money=77.0}, thread name:ThreadPoolTaskExecutor-1
: return code:1, thread:ThreadPoolTaskExecutor-2 
: after user money find user by id: User{id=14, name='chenshuang', age=12, money=78.0} , thread name:ThreadPoolTaskExecutor-2
    : after user money find user by id: User{id=14, name='chenshuang', age=12, money=78.0} , thread name:ThreadPoolTaskExecutor-1
```