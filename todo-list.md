# todo list

## 异常处理
1. 支持方法抛出异常
2. 支持RemoteException
3. 支持TimeoutException
   
## 同进程避免跨进程调用


## 进程重启监听
1. IBinder增加onBinderRestart()
2. Binder集合类在进程重启后，重新赋值
    
    
## 类型序列化
1. TypeAdapter处理class类型序列化
2. ContextAdapter处理Context序列化
3. MapAdapter处理Map类型序列化
4. ParcelableArrayAdapter处理Parcelable子类的数组序列化
5. BinderAdapter处理接口序列化
6. 支持泛型序列化!!!

## XBinder集合类
1. XBinderArrayList
2. XBinderLinkedList
3. XBinderCopyOnWriteArrayList
4. XBinderHashMap
5. XBinderLinkedHashMap
6. XBinderHashSet
7. XBinderSparseArray, XBinderSparseLongArray, ...
或者使用泛型
集合类要能做到进程死亡后，集合中关联的IBinder要自动清理，或者进程重启后，IBinder有自动连接能力

    
## 增加注解配置
1. @OneWay
2. @Async
3. @JsonSerialize
    
## 优化项
1. 增加Binder池, 要先确认单个binder是否会导致阻塞


## 其他
* 跨进程调用的过程回调 onMethodStart, onMethodDone, onMethodException, onMethodDelay
* 解决国产机型无法唤醒ContentProvider问题，
* 过程回调，提供回调接口，在远程方法执行过程中的不同阶段提供回调
* 在provider中做初始化, 而不是在application