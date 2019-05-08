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


传播行为

1、PROPAGATION_REQUIRED

如果存在一个事务，则支持当前事务。如果没有事务则开启一个新的事务。 
可以把事务想像成一个胶囊，在这个场景下方法B用的是方法A产生的胶囊（事务）。 

当我执行required 的时候执行之前

| id | name   | age | money |
| ----- | --------- | ----------- | ------- |
| 13 | chenshuang |  12           |      49   |

执行之后

| id | name   | age | money |
| ----- | --------- | ----------- | ------- |
| 13 | chenshuang |  12           |      49   |

执行之前的数据和之后的数据没有发生变化， 证明`get1`方法报错回滚了`required`方法

```java
    @Transactional(propagation = Propagation.REQUIRED)
    public void required(int id){
        int returncode = userMapper.updateByMoney1(id, 1);
        log.info("return code:{}, thread:{} ", returncode, Thread.currentThread().getName());
        this.get1(id);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void get1(int id){
        int returncode = userMapper.updateByMoney1(id, 2);
        log.info("return code:{}, thread:{} ", returncode, Thread.currentThread().getName());
        throw new RuntimeException("ww");
    }
```

2、PROPAGATION_SUPPORTS

如果存在一个事务，支持当前事务。如果没有事务，则非事务的执行。但是对于事务同步的事务管理器，PROPAGATION_SUPPORTS与不使用事务有少许不同。 

当我执行required 的时候执行之前

| id | name   | age | money |
| ----- | --------- | ----------- | ------- |
| 13 | chenshuang |  12           |      49   |

执行之后

| id | name   | age | money |
| ----- | --------- | ----------- | ------- |
| 13 | chenshuang |  12           |      49   |

说明get1方法已经加入到required方法的事务中

```java
    @Transactional(propagation = Propagation.REQUIRED)
    public void required(int id){
        int returncode = userMapper.updateByMoney1(id, 1);
        log.info("return code:{}, thread:{} ", returncode, Thread.currentThread().getName());
        this.get1(id);
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public void get1(int id){
        int returncode = userMapper.updateByMoney1(id, 2);
        log.info("return code:{}, thread:{} ", returncode, Thread.currentThread().getName());
        throw new RuntimeException("ww");
    }
```

单纯的调用get1时，get1方法是非事务的执行的。当调用required时,get1则加入了required的事务中,事务地执行。

当我只执行get1方法

| id | name   | age | money |
| ----- | --------- | ----------- | ------- |
| 13 | chenshuang |  12           |      49   |

执行之后

| id | name   | age | money |
| ----- | --------- | ----------- | ------- |
| 13 | chenshuang |  12           |      51   |


```java
    @Transactional(propagation = Propagation.SUPPORTS)
    public void get1(int id){
        int returncode = userMapper.updateByMoney1(id, 2);
        log.info("return code:{}, thread:{} ", returncode, Thread.currentThread().getName());
        throw new RuntimeException("ww");
    }
```

3、PROPAGATION_MANDATORY

如果已经存在一个事务，支持当前事务。如果没有一个活动的事务，则抛出异常。

当我执行required 的时候执行之前

| id | name   | age | money |
| ----- | --------- | ----------- | ------- |
| 13 | chenshuang |  12           |      49   |

执行之后

| id | name   | age | money |
| ----- | --------- | ----------- | ------- |
| 13 | chenshuang |  12           |      49   |

说明get1方法已经加入到required方法的事务中

```java
    @Transactional(propagation = Propagation.REQUIRED)
    public void required(int id){
        int returncode = userMapper.updateByMoney1(id, 1);
        log.info("return code:{}, thread:{} ", returncode, Thread.currentThread().getName());
        this.get1(id);
    }


    @Transactional(propagation = Propagation.MANDATORY)
    public void get1(int id){
        int returncode = userMapper.updateByMoney1(id, 2);
        log.info("return code:{}, thread:{} ", returncode, Thread.currentThread().getName());
    }
```

当 `required`没有事务的时候就会报错 

No existing transaction found for transaction marked with propagation 'mandatory'

```

org.springframework.transaction.IllegalTransactionStateException: No existing transaction found for transaction marked with propagation 'mandatory'

	at org.springframework.transaction.support.AbstractPlatformTransactionManager.getTransaction(AbstractPlatformTransactionManager.java:364)
	at org.springframework.transaction.interceptor.TransactionAspectSupport.createTransactionIfNecessary(TransactionAspectSupport.java:474)
	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:289)
	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:98)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)
	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:688)
	at cn.learning.hikaricp.service.impl.UserServiceImpl$$EnhancerBySpringCGLIB$$d3621ec3.get1(<generated>)
	at cn.learning.hikaricp.HikaricpApplicationTests.get1(HikaricpApplicationTests.java:81)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.springframework.test.context.junit4.statements.RunBeforeTestExecutionCallbacks.evaluate(RunBeforeTestExecutionCallbacks.java:74)
	at org.springframework.test.context.junit4.statements.RunAfterTestExecutionCallbacks.evaluate(RunAfterTestExecutionCallbacks.java:84)
	at org.springframework.test.context.junit4.statements.RunBeforeTestMethodCallbacks.evaluate(RunBeforeTestMethodCallbacks.java:75)
	at org.springframework.test.context.junit4.statements.RunAfterTestMethodCallbacks.evaluate(RunAfterTestMethodCallbacks.java:86)
	at org.springframework.test.context.junit4.statements.SpringRepeat.evaluate(SpringRepeat.java:84)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:251)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:97)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.springframework.test.context.junit4.statements.RunBeforeTestClassCallbacks.evaluate(RunBeforeTestClassCallbacks.java:61)
	at org.springframework.test.context.junit4.statements.RunAfterTestClassCallbacks.evaluate(RunAfterTestClassCallbacks.java:70)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.run(SpringJUnit4ClassRunner.java:190)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
	at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)
	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)

```


4、PROPAGATION_REQUIRES_NEW


使用PROPAGATION_REQUIRES_NEW,需要使用 JtaTransactionManager作为事务管理器。 
它会开启一个新的事务。如果一个事务已经存在，则先将这个存在的事务挂起。

当我执行required 的时候执行之前

| id | name   | age | money |
| ----- | --------- | ----------- | ------- |
| 13 | chenshuang |  12           |      49   |

执行之后

| id | name   | age | money |
| ----- | --------- | ----------- | ------- |
| 13 | chenshuang |  12           |      49   |

说明get1方法已经加入到required方法的事务中

```java
    @Transactional(propagation = Propagation.REQUIRED)
    public void required(int id){
        int returncode = userMapper.updateByMoney1(id, 1);
        log.info("return code:{}, thread:{} ", returncode, Thread.currentThread().getName());
        this.get1(id);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void get1(int id){
        int returncode = userMapper.updateByMoney1(id, 2);
        log.info("return code:{}, thread:{} ", returncode, Thread.currentThread().getName());
    }
```

5、PROPAGATION_NOT_SUPPORTED
PROPAGATION_NOT_SUPPORTED 总是非事务地执行，并挂起任何存在的事务。使用PROPAGATION_NOT_SUPPORTED,也需要使用JtaTransactionManager作为事务管理器。 


6、PROPAGATION_NEVER
总是非事务地执行，如果存在一个活动事务，则抛出异常。

7、PROPAGATION_NESTED

如果一个活动的事务存在，则运行在一个嵌套的事务中。 如果没有活动事务, 则按TransactionDefinition.PROPAGATION_REQUIRED 属性执行。 
这是一个嵌套事务,使用JDBC 3.0驱动时,仅仅支持DataSourceTransactionManager作为事务管理器。 
需要JDBC 驱动的java.sql.Savepoint类。使用PROPAGATION_NESTED，还需要把PlatformTransactionManager的nestedTransactionAllowed属性设为true(属性值默认为false)。