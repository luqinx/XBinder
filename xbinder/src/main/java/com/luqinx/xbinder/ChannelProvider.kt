package com.luqinx.xbinder

import android.net.Uri
import android.os.IBinder
import com.luqinx.xbinder.serialize.toClass

/**
 * @author  qinchao
 *
 * @since 2022/2/21
 */
internal object ChannelProvider {

    private val CHANNEL_BINDER_MAP: MutableMap<String, ChannelBinder> = mutableMapOf()

    fun getBinderChannel(process: String): ChannelBinder? {
        return CHANNEL_BINDER_MAP[process] ?: synchronized(CHANNEL_BINDER_MAP) {
            // double check
            CHANNEL_BINDER_MAP[process] ?: let {

                val start = System.currentTimeMillis()
                val uri = Uri.parse("content://$process")
                val cursor = contextService.queryProvider(uri)
                var channelBinder: ChannelBinder? = null
                cursor?.apply {
                    extras.classLoader = classloader
                    val binderWrapper =
                        extras.getParcelable<ParcelableBinder>(XBinderProvider.EXTRA_KEY_BINDER)
                    binderWrapper?.apply {
                        val binder = getBinder<IBinder>()
                        binder.linkToDeath({
                            BinderInvoker.dispatchBinderDeath(process)
                        }, 0)
                        val service: ChannelBinder =
                            ChannelBinder.Stub.asInterface(getBinder())
                        CHANNEL_BINDER_MAP[process] = service
                        channelBinder = service
                        channelBinder!!.registerCallbackChannel(XBinder.currentProcessName(),
                            coreChannel
                        )
                    }
                    close()
                }
                logger.d(message = "query provider ${channelBinder?.javaClass} spent ${System.currentTimeMillis() - start}ms")
                return channelBinder
            }
        }
    }

    fun addBinderChannel(process: String, channelBinder: ChannelBinder) {
        synchronized(CHANNEL_BINDER_MAP) {
            CHANNEL_BINDER_MAP[process] = channelBinder
        }
    }

    fun onBinderDeath(process: String) {
        synchronized(CHANNEL_BINDER_MAP) {
            CHANNEL_BINDER_MAP.remove(process)
        }
    }

    internal val coreChannel = object : ChannelBinder.Stub() {
        override fun invokeMethod(rpcArgument: ChannelArgument): ChannelResult {
            logger.d(
                message = "invokeMethod by ${rpcArgument.delegateId}: ${rpcArgument.returnType} ${rpcArgument.method}(${
                    rpcArgument.args?.let {
                        return@let it.contentDeepToString()
                    } ?: ""
                })")
            val result = ChannelResult()
            result.succeed = true
            val clazzImpl: Any?
            when (rpcArgument.method) {
                CORE_METHOD_UNREGISTER_INSTANCE -> {
                    ServiceProvider.unregisterServiceInstance(rpcArgument.fromProcess, rpcArgument.instanceId!!)
                    return result
                }
                CORE_METHOD_NEW_CONSTRUCTOR -> {
                    rpcArgument.run {
                        val start = System.currentTimeMillis()
                        clazzImpl = ServiceProvider.doFind(
                            fromProcess,
                            delegateId,
                            clazz.toClass()!!,
                            genericArgTypes,
                            args
                        )
                        result.returnValue = clazzImpl?.let {
                            "${javaClass}-${hashCode()}"
                        } ?: run {
                            null
                        }
                        result.returnType = String::class.java
                        result.invokeConsumer = System.currentTimeMillis() - start
                        return result
                    }
                }
                else -> {
                    rpcArgument.run {
                        clazzImpl = if (instanceId != null) {
                            ServiceProvider.getServiceInstance(fromProcess, instanceId!!)
                        } else {
                            ServiceProvider.getServiceImpl(fromProcess, delegateId)
                        }
                    }
                }
            }

            if (clazzImpl == null) {
                result.succeed = false
                result.errCode = BinderInvoker.ERROR_CODE_REMOTE_NOT_FOUND
                result.errMessage = "not found the implementation of ${rpcArgument.clazz.toClass()}."
                return result
            }
            try {
                if (rpcArgument.argTypes == null || rpcArgument.argTypes!!.isEmpty()) {
                    val method = clazzImpl.javaClass.getDeclaredMethod(rpcArgument.method)
                    method.isAccessible = true
                    val start = System.currentTimeMillis()
                    result.returnType = method.genericReturnType
                    result.returnValue = method.invoke(clazzImpl)
                    result.invokeConsumer = System.currentTimeMillis() - start
                } else {
                    val method = clazzImpl.javaClass.getDeclaredMethod(rpcArgument.method, *rpcArgument.argTypes!!)
                    method.isAccessible = true
                    val start = System.currentTimeMillis()
                    result.returnType = method.genericReturnType
                    result.returnValue = method.invoke(clazzImpl, *rpcArgument.args!!)
                    result.invokeConsumer = System.currentTimeMillis() - start
                }
            } catch (e: Throwable) {
                result.succeed = false
                result.errMessage = e.message
                exceptionHandler.handle(e)
            }
            return result
        }

        override fun registerCallbackChannel(process: String, channelBinder: ChannelBinder) {
            addBinderChannel(process, channelBinder)
        }
    }
}