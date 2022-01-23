package com.luqinx.xbinder.sample.simple.invoketype

/**
 * @author  qinchao
 *
 * @since 2022/1/22
 */
interface BaseService {
    /** 此服务本地存在, 不会走Remote **/
    fun run()
}