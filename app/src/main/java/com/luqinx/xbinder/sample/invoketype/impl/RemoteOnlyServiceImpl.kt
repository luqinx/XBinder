package com.luqinx.xbinder.sample.invoketype.impl

import com.luqinx.xbinder.XBinder
import com.luqinx.xbinder.sample.BuildConfig

/**
 * @author  qinchao
 *
 * @since 2022/3/23
 */
class RemoteOnlyServiceImpl: BaseServiceImpl() {
    override fun run() {
        super.run()
        assert(XBinder.currentProcessName() != BuildConfig.APPLICATION_ID)
    }
}