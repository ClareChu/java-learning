# java-learning
Java 学习

使用性能最好的连接池 HikariCP

```
HikariCP将尝试仅通过驱动程序管理器来解析驱动程序jdbcUrl，但对于某些较旧的驱动程序，
driverClassName还必须指定。除非您收到明显的错误消息，指出未找到驱动程序，否则请忽略此属性。 默认值：无
```

read uncommit

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