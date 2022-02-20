package com.luqinx.xbinder

import android.net.Uri
import android.os.DeadObjectException
import android.os.IBinder

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

    fun invokeMethod(
        processName: String,
        rpcArgument: ChannelMethodArgument,
        localInvokeAfterRemoteMissing: Boolean
    ): Any? {
        val start = System.currentTimeMillis()
        var callSuccess = false
        val coreService = getCoreService(processName)
        var consumer = 0L
        try {
            coreService?.invokeMethod(rpcArgument)?.apply {
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
            onBinderDeath(processName)
        } catch (e: Throwable) {
            exceptionHandler.handle(e)
        } finally {
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

    private val BINDER_CHANNEL_SERVICE_MAP: MutableMap<String, BinderChannelService> = mutableMapOf()

    private fun getCoreService(process: String): BinderChannelService? {
        return BINDER_CHANNEL_SERVICE_MAP[process] ?: synchronized(BINDER_CHANNEL_SERVICE_MAP) {
            // double check
            BINDER_CHANNEL_SERVICE_MAP[process] ?: let {

                val start = System.currentTimeMillis()
                val uri = Uri.parse("content://$process")
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                var binderChannelService: BinderChannelService? = null
                cursor?.apply {
                    extras.classLoader = classloader
                    val binderWrapper =
                        extras.getParcelable<ParcelableBinder>(XBinderProvider.EXTRA_KEY_BINDER)
                    binderWrapper?.apply {
                        val binder = getBinder<IBinder>()
                        binder.linkToDeath({
                            onBinderDeath(process)
                        }, 0)
                        val service: BinderChannelService =
                            BinderChannelService.Stub.asInterface(getBinder())
                        BINDER_CHANNEL_SERVICE_MAP[process] = service
                        binderChannelService = service

                    }
                    close()
                }
                logger.d(message = "query provider ${binderChannelService?.javaClass} spent ${System.currentTimeMillis() - start}ms")
                return binderChannelService
            }
        }
    }

    private fun onBinderDeath(process: String) {
        logger.w(message = "process has dead: $process")
//        CallbacksWatcher.onBinderDeath(process)
//        ServiceStore.onBinderDeath(process)
        ServiceProvider.onBinderDeath(process)
        synchronized(BINDER_CHANNEL_SERVICE_MAP) {
            BINDER_CHANNEL_SERVICE_MAP.remove(process)
        }
        binderDeathHandler.onBinderDeath(process)
    }

    fun isRemoteAlive(process: String): Boolean {
        return BINDER_CHANNEL_SERVICE_MAP[process] != null
    }

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


}