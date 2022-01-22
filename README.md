# XBinder


## 优势
* 无需手写aidl

* 像普通接口一样的调用调用方式, 更简单、更简洁

* 支持跨进程接口回调, 包括匿名的跨进程接口回调

* 支持跨App多进程调用，多个App多个进程都可以支持sa

* 支持lifecycle

* 支持oneway @Oneway

* 支持异步调用 @Async



## 对比Andromeda

*直接使用接口进行跨进程通信, 无需手写aidl

*支持任意Callback接口, 不是固定统一的一个Callback，接口方法的入参和返回值符合进程通信的参数即可

*像普通接口一样的调用方式, 不用与binder直接交互

*LocalService和RemoteService无缝衔接, 不用人为区分

*Andromeda只支持App内跨进程