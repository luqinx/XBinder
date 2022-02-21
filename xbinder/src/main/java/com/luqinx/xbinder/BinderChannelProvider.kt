package com.luqinx.xbinder

import android.net.Uri
import android.os.IBinder
import com.luqinx.xbinder.serialize.toClass

/**
 * @author  qinchao
 *
 * @since 2022/2/21
 */
internal object BinderChannelProvider {

    private val BINDER_CHANNEL_MAP: MutableMap<String, BinderChannel> = mutableMapOf()

    fun getBinderChannel(process: String): BinderChannel? {
        return BINDER_CHANNEL_MAP[process] ?: synchronized(BINDER_CHANNEL_MAP) {
            // double check
            BINDER_CHANNEL_MAP[process] ?: let {

                val start = System.currentTimeMillis()
                val uri = Uri.parse("content://$process")
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                var binderChannel: BinderChannel? = null
                cursor?.apply {
                    extras.classLoader = classloader
                    val binderWrapper =
                        extras.getParcelable<ParcelableBinder>(XBinderProvider.EXTRA_KEY_BINDER)
                    binderWrapper?.apply {
                        val binder = getBinder<IBinder>()
                        binder.linkToDeath({
                            BinderInvoker.dispatchBinderDeath(process)
                        }, 0)
                        val service: BinderChannel =
                            BinderChannel.Stub.asInterface(getBinder())
                        BINDER_CHANNEL_MAP[process] = service
                        binderChannel = service
                        binderChannel!!.registerCallbackChannel(XBinder.currentProcessName(),
                            coreChannel
                        )
                    }
                    close()
                }
                logger.d(message = "query provider ${binderChannel?.javaClass} spent ${System.currentTimeMillis() - start}ms")
                return binderChannel
            }
        }
    }

    fun addBinderChannel(process: String, channel: BinderChannel) {
        synchronized(BINDER_CHANNEL_MAP) {
            BINDER_CHANNEL_MAP[process] = channel
        }
    }

    fun onBinderDeath(process: String) {
        synchronized(BINDER_CHANNEL_MAP) {
            BINDER_CHANNEL_MAP.remove(process)
        }
    }

    internal val coreChannel = object : BinderChannel.Stub() {
        override fun invokeMethod(rpcArgument: ChannelMethodArgument): ChannelMethodResult {
            logger.d(
                message = "invokeMethod by ${rpcArgument.delegateId}: ${rpcArgument.returnType} ${rpcArgument.method}(${
                    rpcArgument.args?.let {
                        return@let it.contentDeepToString()
                    } ?: ""
                })")
            val result = ChannelMethodResult()
            result.succeed = true
            val clazzImpl: IBinderService?
            if (rpcArgument.method == CORE_METHOD_NEW_CONSTRUCTOR) {
                rpcArgument.run {
                    val start = System.currentTimeMillis()
                    clazzImpl = ServiceProvider.doFind(fromProcess, delegateId, clazz.toClass()!!, genericArgTypes, args)
                    result.value = clazzImpl != null
                    result.invokeConsumer = System.currentTimeMillis() - start
                    return result
                }
            } else {
                rpcArgument.run {
                    clazzImpl = ServiceProvider.getServiceImpl(fromProcess, delegateId)
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
                    result.value = method.invoke(clazzImpl)
                    result.invokeConsumer = System.currentTimeMillis() - start
                } else {
                    val method = clazzImpl.javaClass.getDeclaredMethod(rpcArgument.method, *rpcArgument.argTypes!!)
                    method.isAccessible = true
                    val start = System.currentTimeMillis()
                    result.value = method.invoke(clazzImpl, *rpcArgument.args!!)
                    result.invokeConsumer = System.currentTimeMillis() - start
                }
            } catch (e: Throwable) {
                result.succeed = false
                result.errMessage = e.message
                exceptionHandler.handle(e)
            }
            return result
        }

        override fun registerCallbackChannel(process: String, channel: BinderChannel) {
            addBinderChannel(process, channel)
        }

        override fun unRegisterCallbackMethod(fromProcess: String, methodId: String) {
//            ServiceStore.unregisterMethodCallback(fromProcess, methodId)
        }
    }
}