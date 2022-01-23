package com.luqinx.xbinder.annotation

import androidx.annotation.IntDef
import com.luqinx.xbinder.annotation.InvokeType.Companion.LOCAL_FIRST
import com.luqinx.xbinder.annotation.InvokeType.Companion.LOCAL_ONLY
import com.luqinx.xbinder.annotation.InvokeType.Companion.REMOTE_FIRST
import com.luqinx.xbinder.annotation.InvokeType.Companion.REMOTE_ONLY

/**
 * @author  qinchao
 *
 * @since 2022/1/22
 */
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@IntDef(value = [LOCAL_FIRST, REMOTE_FIRST, LOCAL_ONLY, REMOTE_ONLY])
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY)
annotation class InvokeType {
    companion object {

        const val LOCAL_FIRST = 1

        const val REMOTE_FIRST = 2

        const val LOCAL_ONLY = 3

        const val REMOTE_ONLY = 4

    }
}