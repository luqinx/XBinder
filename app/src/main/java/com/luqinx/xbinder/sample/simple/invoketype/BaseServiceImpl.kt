package com.luqinx.xbinder.sample.simple.invoketype

import android.annotation.SuppressLint
import com.luqinx.xbinder.XBinder
import com.luqinx.xbinder.sample.BuildConfig

/**
 * @author  qinchao
 *
 * @since 2022/1/22
 */
open class BaseServiceImpl: BaseService {
    @SuppressLint("Assert")
    override fun run() {
        assert(XBinder.currentProcessName() == BuildConfig.APPLICATION_ID)
        println("run ${this.javaClass} service in process ${XBinder.currentProcessName()}")
    }
}