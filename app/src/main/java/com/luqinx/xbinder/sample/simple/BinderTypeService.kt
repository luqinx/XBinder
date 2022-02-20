package com.luqinx.xbinder.sample.simple

import android.os.IBinder
import com.luqinx.xbinder.IBinderService

/**
 * @author  qinchao
 *
 * @since 2022/2/19
 */
interface BinderTypeService: IBinderService {
    fun getOnewayBinder(): IBinder
}