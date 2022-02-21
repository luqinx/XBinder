package com.luqinx.xbinder

import androidx.lifecycle.LifecycleOwner

/**
 * @author  qinchao
 *
 * @since 2022/2/20
 */
interface IBinderCallback {
    fun getLifecycleOwner(): LifecycleOwner? = null
}