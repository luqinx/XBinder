package com.luqinx.xbinder

import java.lang.Exception

/**
 * @author  qinchao
 *
 * @since 2022/1/2
 */
class XBinderException(val code: Int, message_: String?, e: Throwable? = null): Exception(message_, e) {

    companion object {
        const val EXCEPTION_CLASS_NOT_FOUND = 1

    }
}