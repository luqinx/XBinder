package com.luqinx.xbinder

import android.os.DeadObjectException

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
internal object BinderInvoker {

    const val ERROR_CODE_SUCCESS = 0
    const val ERROR_CODE_REMOTE_NOT_FOUND = 1
    const val ERROR_CODE_WITH_EXCEPTION = 2
    const val ERROR_CODE_ONEWAY_ERROR = 3

    private val threadLocal = ThreadLocal<InvokerHolder>()

    fun invokeMethod(
        processName: String,
        rpcArgument: ChannelArgument,
        localInvokeAfterRemoteMissing: Boolean
    ): Any? {
        val start = System.currentTimeMillis()
        var callSuccess = false
        val channelBinder = ChannelProvider.getBinderChannel(processName)
        var consumer = 0L
        try {
            threadLocal.set(InvokerHolder(processName))
            channelBinder?.invokeMethod(rpcArgument)?.apply {
                if (succeed) {
                    callSuccess = true
                    consumer = invokeConsumer
                    return value
                } else {
                    if (localInvokeAfterRemoteMissing && errCode == ERROR_CODE_REMOTE_NOT_FOUND) {
                        throw XBinderException(errCode, errMessage)
                    }
                    logger.e(
                        message = errMessage
                            ?: "invoke error !! (${rpcArgument.returnType} ${rpcArgument.method}(${
                            rpcArgument.genericArgTypes?.let {
                                return@let it.contentDeepToString()
                            } ?: ""
                            }))")
                }
            } ?: run {
                logger.e(message = "rpc provider not found!!! $processName")
            }
        } catch (e: DeadObjectException) {
            exceptionHandler.handle(e)
            dispatchBinderDeath(processName)
        } catch (e: Throwable) {
            exceptionHandler.handle(e)
        } finally {
            threadLocal.set(null)

            val delta = System.currentTimeMillis() - start
            if (showSlowInvoke(delta)) {
                logger.i(
                    message = "${rpcArgument.returnType} ${rpcArgument.method}(${
                    rpcArgument.args?.let {
                        return@let it.contentDeepToString()
                    } ?: ""
                    }) in ${rpcArgument.clazz} invoked ${if (callSuccess) "success" else "fail"} & spent ${consumer}/${delta}ms")
            }
        }
        return null
    }

    fun dispatchBinderDeath(process: String) {
        logger.w(message = "process has dead: $process")
        ChannelProvider.onBinderDeath(process)
//        CallbacksWatcher.onBinderDeath(process)
//        ServiceStore.onBinderDeath(process)
        ServiceProvider.onBinderDeath(process)
        binderDeathHandler.onBinderDeath(process)
    }

//    fun isRemoteAlive(process: String): Boolean {
//        return BINDER_CHANNEL_MAP[process] != null
//    }

    private fun showSlowInvoke(consumer: Long): Boolean {
        return when {
            invokeThreshold == XBinderInitOptions.INVOKE_THRESHOLD_FORCE_ENABLE -> {
                true
            }
            invokeThreshold == XBinderInitOptions.INVOKE_THRESHOLD_DISABLE -> {
                false
            }
            consumer > invokeThreshold -> {
                true
            }
            else -> false
        }
    }

    fun invokeProcess(): String {
        return threadLocal.get()?.toProcess!!
    }

    class InvokerHolder(val toProcess:String,)
}