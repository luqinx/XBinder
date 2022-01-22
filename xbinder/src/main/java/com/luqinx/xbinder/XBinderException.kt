package com.luqinx.xbinder

import java.lang.Exception

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
class XBinderException(private val code: Int, private val message_: String?, private val e: Throwable? = null): Exception(message_, e) {

    companion object {
        const val EXCEPTION_CLASS_NOT_FOUND = 1

    }
}