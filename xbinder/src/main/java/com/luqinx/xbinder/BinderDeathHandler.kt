package com.luqinx.xbinder

/**
 * @author  qinchao
 *
 * @since 2022/1/22
 */
interface BinderDeathHandler {
    fun onBinderDeath(process: String)

    object IGNORE: BinderDeathHandler {
        override fun onBinderDeath(process: String) {
            // do nothing
        }
    }

}