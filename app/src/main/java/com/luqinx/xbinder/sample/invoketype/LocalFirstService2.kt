package com.luqinx.xbinder.sample.invoketype

/**
 * @author  qinchao
 *
 * @since 2022/1/22
 */
interface LocalFirstService2: BaseService {
    /** 此服务本地不存在, 会走Remote**/
}