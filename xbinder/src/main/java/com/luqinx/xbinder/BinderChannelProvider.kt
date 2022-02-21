package com.luqinx.xbinder

import android.net.Uri
import android.os.IBinder

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
                        binderChannel!!.registerCallbackChannel(XBinder.currentProcessName(), coreService)
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
}