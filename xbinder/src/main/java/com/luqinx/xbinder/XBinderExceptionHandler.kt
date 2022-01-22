package com.luqinx.xbinder

/**
 * @author  qinchao
 *
 * @since 2022/1/10
 */
interface XBinderExceptionHandler {
    fun handle(e: Throwable)

    object SimpleHandler: XBinderExceptionHandler {
        override fun handle(e: Throwable) {
            e.printStackTrace()
        }

    }
}