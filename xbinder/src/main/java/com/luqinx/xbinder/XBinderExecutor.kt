package com.luqinx.xbinder

import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author  qinchao
 *
 * @since 2022/2/20
 */
object XBinderExecutor: Executor {

    private const val CORE_POOL_SIZE = 2
    private const val MAX_POOL_SIZE = 100
    private const val KEEP_ALIVE_SECONDS = 3L

    private val asyncCallExecutor = ThreadPoolExecutor(
        CORE_POOL_SIZE,
        MAX_POOL_SIZE,
        KEEP_ALIVE_SECONDS,
        TimeUnit.SECONDS,
        SynchronousQueue(),
        AsyncThreadFactory()
    )

    private class AsyncThreadFactory: ThreadFactory {

        private val index = AtomicInteger(0)

        override fun newThread(r: Runnable?): Thread {
            return Thread(r, "xbinder-async-${index.getAndIncrement()}")
        }

    }

    fun executeAsyncCall(command: Runnable) {
        asyncCallExecutor.execute(command)
    }


    override fun execute(command: Runnable?) {
        command?.let {
            asyncCallExecutor.execute(it)
        }
    }
}