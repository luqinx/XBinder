package com.luqinx.xbinder.sample.invoketype.impl

import android.annotation.SuppressLint
import com.luqinx.xbinder.XBinder
import com.luqinx.xbinder.sample.BuildConfig
import com.luqinx.xbinder.sample.invoketype.BaseService

/**
 * @author  qinchao
 *
 * @since 2022/1/22
 */
open class BaseServiceImpl: BaseService {
    @SuppressLint("Assert")
    override fun run() {
        println("run ${this.javaClass} service in process ${XBinder.currentProcessName()}")
    }
}