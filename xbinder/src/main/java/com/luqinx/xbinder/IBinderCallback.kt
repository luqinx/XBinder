package com.luqinx.xbinder

import androidx.lifecycle.LifecycleOwner

/**
 * @author  qinchao
 *
 * @since 2022/2/20
 */
interface IBinderCallback: IBinderService {
    fun getLifecycleOwner(): LifecycleOwner? = null
}