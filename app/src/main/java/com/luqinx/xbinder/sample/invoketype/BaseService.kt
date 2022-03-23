package com.luqinx.xbinder.sample.invoketype

import com.luqinx.xbinder.ILightBinder

/**
 * @author  qinchao
 *
 * @since 2022/1/22
 */
interface BaseService: ILightBinder {
    /** 此服务本地存在, 不会走Remote **/
    fun run()
}