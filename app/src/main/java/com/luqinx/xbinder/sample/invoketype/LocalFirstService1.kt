package com.luqinx.xbinder.sample.invoketype

/**
 * @author  qinchao
 *
 * @since 2022/1/22
 */
interface LocalFirstService1: BaseService {
    /** 此服务本地存在, 不会走Remote**/
}